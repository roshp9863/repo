import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Box, Typography, CircularProgress, Card, CardContent, CardActions, Button, Alert, Chip, Grid, Dialog, DialogTitle, DialogContent, DialogActions, TextField } from '@mui/material';
import { bookingAPI } from '../services/apiClient';
import { useSnackbar } from 'notistack';

export default function BookingsPage() {
  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();
  const [bookings, setBookings] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);
  const [selectedBooking, setSelectedBooking] = useState<any>(null);
  const [cancelReason, setCancelReason] = useState('');
  const [cancelling, setCancelling] = useState(false);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await bookingAPI.list(0, 20);
      setBookings(response.data.data.content || []);
    } catch (err: any) {
      console.error('Error fetching bookings:', err);
      setError('Failed to load bookings. Please try again.');
      setBookings([]);
    } finally {
      setLoading(false);
    }
  };

  const handleViewBooking = (bookingId: string) => {
    navigate(`/booking/${bookingId}`);
  };

  const handleCancelClick = (booking: any) => {
    setSelectedBooking(booking);
    setCancelReason('');
    setCancelDialogOpen(true);
  };

  const handleConfirmCancel = async () => {
    if (!selectedBooking) return;

    setCancelling(true);
    try {
      await bookingAPI.cancel(selectedBooking.id, cancelReason);
      enqueueSnackbar('Booking cancelled successfully', { variant: 'success' });
      setCancelDialogOpen(false);
      fetchBookings();
    } catch (err: any) {
      const message = err.response?.data?.message || 'Failed to cancel booking';
      enqueueSnackbar(message, { variant: 'error' });
    } finally {
      setCancelling(false);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'CONFIRMED': return 'success';
      case 'PENDING': return 'warning';
      case 'CANCELLED': return 'error';
      case 'COMPLETED': return 'info';
      default: return 'default';
    }
  };

  const getStatusGradient = (status: string) => {
    switch (status) {
      case 'CONFIRMED': return 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)';
      case 'PENDING': return 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)';
      case 'CANCELLED': return 'linear-gradient(135deg, #f5576c 0%, #ff6b6b 100%)';
      case 'COMPLETED': return 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
      default: return 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 12 }}>
        <CircularProgress sx={{ color: '#667eea' }} />
      </Box>
    );
  }

  return (
    <Box>
      {/* Header */}
      <Box
        sx={{
          background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)',
          color: '#fff',
          py: { xs: 5, md: 7 },
          borderRadius: 0,
        }}
      >
        <Container maxWidth="lg">
          <Typography variant="h3" sx={{ fontWeight: 800, mb: 1, fontSize: { xs: '1.8rem', md: '2.5rem' } }}>
            My Bookings
          </Typography>
          <Typography sx={{ color: 'rgba(255,255,255,0.6)', fontSize: '1.1rem' }}>
            Track and manage all your sessions
          </Typography>
        </Container>
      </Box>

      <Container maxWidth="lg">
        <Box sx={{ py: 5 }}>
          {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}

          {bookings.length === 0 ? (
            <Box sx={{ textAlign: 'center', py: 10, bgcolor: '#fff', borderRadius: '16px', border: '1px solid rgba(0,0,0,0.04)' }}>
              <Typography variant="h6" sx={{ color: '#64648b', mb: 2 }}>No bookings yet</Typography>
              <Button variant="contained" onClick={() => navigate('/artists')}>
                Discover Artists →
              </Button>
            </Box>
          ) : (
            <Grid container spacing={3}>
              {bookings.map((booking: any) => (
                <Grid item xs={12} md={6} key={booking.id}>
                  <Card sx={{ overflow: 'hidden' }}>
                    {/* Status bar */}
                    <Box sx={{ height: 5, background: getStatusGradient(booking.status) }} />
                    <CardContent sx={{ p: 3 }}>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', mb: 2 }}>
                        <Typography variant="h6" sx={{ fontWeight: 700 }}>
                          {booking.offeringTitle || 'Service'}
                        </Typography>
                        <Chip
                          label={booking.status}
                          color={getStatusColor(booking.status) as any}
                          size="small"
                          sx={{ fontWeight: 600, fontSize: '0.7rem' }}
                        />
                      </Box>

                      {booking.artistDisplayName && (
                        <Typography variant="body2" sx={{ mb: 0.5, color: '#64648b' }}>
                          with <strong style={{ color: '#1a1a2e' }}>{booking.artistDisplayName}</strong>
                        </Typography>
                      )}
                      {booking.scheduledAt && (
                        <Typography variant="body2" sx={{ mb: 0.5, color: '#64648b' }}>
                          {new Date(booking.scheduledAt).toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })}
                        </Typography>
                      )}
                      {booking.offeringPrice && (
                        <Typography variant="body2" sx={{ fontWeight: 700, mt: 1, color: '#667eea' }}>
                          ${booking.offeringPrice} {booking.offeringCurrency || 'USD'}
                        </Typography>
                      )}
                    </CardContent>

                    <Box sx={{ px: 3, pb: 2.5, display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                      <Button size="small" variant="outlined" onClick={() => handleViewBooking(booking.id)} sx={{ borderRadius: '10px' }}>
                        View Details
                      </Button>
                      {booking.status === 'COMPLETED' && (
                        <Button size="small" variant="contained" onClick={() => handleViewBooking(booking.id)}
                          sx={{ borderRadius: '10px', background: 'linear-gradient(135deg, #f6d365, #fda085)', color: '#1a1a2e', fontWeight: 700,
                            '&:hover': { boxShadow: '0 3px 12px rgba(253,133,0.4)' } }}>
                          ⭐ Review
                        </Button>
                      )}
                      {(booking.status === 'CONFIRMED' || booking.status === 'PENDING') && (
                        <Button size="small" color="error" variant="text" onClick={() => handleCancelClick(booking)} sx={{ borderRadius: '10px' }}>
                          Cancel
                        </Button>
                      )}
                    </Box>
                  </Card>
                </Grid>
              ))}
            </Grid>
          )}
        </Box>
      </Container>
      <Dialog open={cancelDialogOpen} onClose={() => setCancelDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Cancel Booking</DialogTitle>
        <DialogContent>
          <Typography variant="body2" sx={{ mb: 2 }}>
            Are you sure you want to cancel this booking? This action cannot be undone.
          </Typography>
          <TextField
            fullWidth
            multiline
            rows={3}
            label="Reason for cancellation (optional)"
            value={cancelReason}
            onChange={(e) => setCancelReason(e.target.value)}
            placeholder="Tell the artist why you're cancelling..."
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setCancelDialogOpen(false)}>
            Keep Booking
          </Button>
          <Button
            onClick={handleConfirmCancel}
            color="error"
            variant="contained"
            disabled={cancelling}
          >
            {cancelling ? 'Cancelling...' : 'Confirm Cancel'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

