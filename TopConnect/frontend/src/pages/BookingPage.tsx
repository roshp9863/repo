import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Box, Typography, CircularProgress, Alert, Button, Card, CardContent, Grid, Chip, Divider, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Rating } from '@mui/material';
import { bookingAPI, paymentAPI, reviewAPI } from '../services/apiClient';
import { useSnackbar } from 'notistack';

export default function BookingPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();
  const [booking, setBooking] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actionLoading, setActionLoading] = useState(false);
  const [reviewOpen, setReviewOpen] = useState(false);
  const [reviewRating, setReviewRating] = useState<number | null>(0);
  const [reviewComment, setReviewComment] = useState('');
  const [reviewTags, setReviewTags] = useState<string[]>([]);
  const [reviewSubmitting, setReviewSubmitting] = useState(false);
  const [hasReviewed, setHasReviewed] = useState(false);

  const PREDEFINED_TAGS = [
    { key: 'VERY_HELPFUL', label: '💡 Very Helpful' },
    { key: 'GREAT_COMMUNICATOR', label: '💬 Great Communicator' },
    { key: 'ON_TIME', label: '⏰ On Time' },
    { key: 'EXCEEDED_EXPECTATIONS', label: '🚀 Exceeded Expectations' },
    { key: 'PROFESSIONAL', label: '👔 Professional' },
    { key: 'FRIENDLY', label: '😊 Friendly' },
    { key: 'EXPERT_KNOWLEDGE', label: '🎓 Expert Knowledge' },
    { key: 'GOOD_VALUE', label: '💰 Good Value' },
    { key: 'HIGHLY_RECOMMENDED', label: '⭐ Highly Recommended' },
    { key: 'CREATIVE', label: '🎨 Creative' },
    { key: 'PATIENT', label: '🧘 Patient' },
    { key: 'NEEDS_IMPROVEMENT', label: '📝 Needs Improvement' },
  ];

  useEffect(() => {
    if (id) {
      fetchBooking();
      checkReview();
    }
  }, [id]);

  const fetchBooking = async () => {
    if (!id) return;
    setLoading(true);
    setError('');
    try {
      const response = await bookingAPI.get(id);
      setBooking(response.data.data);
    } catch (err: any) {
      console.error('Error fetching booking:', err);
      setError('Failed to load booking details. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const checkReview = async () => {
    if (!id) return;
    try {
      const res = await reviewAPI.checkReviewed(id);
      setHasReviewed(res.data.data.reviewed);
    } catch { /* ignore */ }
  };

  const handleSubmitReview = async () => {
    if (!id || !reviewRating) {
      enqueueSnackbar('Please select a rating', { variant: 'warning' });
      return;
    }
    setReviewSubmitting(true);
    try {
      await reviewAPI.submit({
        bookingId: id,
        rating: reviewRating,
        comment: reviewComment.trim() || undefined,
        tags: reviewTags.length > 0 ? reviewTags : undefined,
      });
      enqueueSnackbar('Thank you for your review! ⭐', { variant: 'success' });
      setReviewOpen(false);
      setHasReviewed(true);
    } catch (err: any) {
      const msg = err.response?.data?.message || 'Failed to submit review';
      enqueueSnackbar(msg, { variant: 'error' });
    } finally {
      setReviewSubmitting(false);
    }
  };

  const toggleTag = (tag: string) => {
    setReviewTags(prev => prev.includes(tag) ? prev.filter(t => t !== tag) : [...prev, tag]);
  };


  const handlePayNow = async () => {
    if (!id) return;

    setActionLoading(true);
    try {
      const response = await paymentAPI.createCheckout(id);
      const paymentData = response.data.data;
      if (paymentData.publishableKey === 'dev_mode') {
        enqueueSnackbar('Payment processed (dev mode) - Booking confirmed!', { variant: 'success' });
        fetchBooking();
      } else {
        window.location.href = paymentData.sessionId;
      }
    } catch (err: any) {
      const message = err.response?.data?.message || 'Payment failed';
      enqueueSnackbar(message, { variant: 'error' });
    } finally {
      setActionLoading(false);
    }
  };

  const handleCompleteBooking = async () => {
    if (!id) return;

    setActionLoading(true);
    try {
      await bookingAPI.complete(id);
      enqueueSnackbar('Booking marked as completed!', { variant: 'success' });
      fetchBooking();
    } catch (err: any) {
      const message = err.response?.data?.message || 'Failed to complete booking';
      enqueueSnackbar(message, { variant: 'error' });
    } finally {
      setActionLoading(false);
    }
  };

  const handleCancelBooking = async () => {
    if (!id) return;

    const confirmed = window.confirm('Are you sure you want to cancel this booking?');
    if (!confirmed) return;

    setActionLoading(true);
    try {
      await bookingAPI.cancel(id);
      enqueueSnackbar('Booking cancelled successfully', { variant: 'success' });
      navigate('/bookings');
    } catch (err: any) {
      const message = err.response?.data?.message || 'Failed to cancel booking';
      enqueueSnackbar(message, { variant: 'error' });
    } finally {
      setActionLoading(false);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 12 }}>
        <CircularProgress sx={{ color: '#667eea' }} />
      </Box>
    );
  }

  if (error || !booking) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ py: 6 }}>
          <Alert severity="error">{error || 'Booking not found'}</Alert>
          <Button onClick={() => navigate('/bookings')} sx={{ mt: 2 }}>← Back to Bookings</Button>
        </Box>
      </Container>
    );
  }

  const getStatusGradient = (status: string) => {
    switch (status) {
      case 'CONFIRMED': return 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)';
      case 'PENDING': return 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)';
      case 'CANCELLED': return 'linear-gradient(135deg, #f5576c 0%, #ff6b6b 100%)';
      case 'COMPLETED': return 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
      default: return 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
    }
  };

  return (
    <Box>
      {/* Header */}
      <Box sx={{ background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)', color: '#fff', py: { xs: 4, md: 6 }, borderRadius: 0 }}>
        <Container maxWidth="lg">
          <Button onClick={() => navigate('/bookings')} sx={{ color: 'rgba(255,255,255,0.6)', mb: 2, '&:hover': { color: '#fff' } }}>
            ← Back to Bookings
          </Button>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 2 }}>
            <Typography variant="h3" sx={{ fontWeight: 800, fontSize: { xs: '1.8rem', md: '2.2rem' } }}>
              Booking Details
            </Typography>
            <Chip
              label={booking.status}
              sx={{ fontWeight: 700, fontSize: '0.8rem', background: getStatusGradient(booking.status), color: '#fff' }}
            />
          </Box>
        </Container>
      </Box>

      <Container maxWidth="lg">
        <Box sx={{ py: 5 }}>
          <Grid container spacing={4}>
            {/* Main Details */}
            <Grid item xs={12} md={8}>
              <Card sx={{ overflow: 'hidden' }}>
                <Box sx={{ height: 6, background: getStatusGradient(booking.status) }} />
                <CardContent sx={{ p: 4 }}>
                  {booking.offeringTitle && (
                    <Box sx={{ mb: 4 }}>
                      <Typography variant="overline" sx={{ color: '#667eea', fontWeight: 700 }}>Service</Typography>
                      <Typography variant="h5" sx={{ fontWeight: 700, mb: 1 }}>{booking.offeringTitle}</Typography>
                      {booking.offeringDescription && (
                        <Typography variant="body2" sx={{ color: '#64648b', lineHeight: 1.7 }}>{booking.offeringDescription}</Typography>
                      )}
                      <Box sx={{ mt: 1.5, display: 'flex', gap: 1 }}>
                        {booking.offeringType && (
                          <Chip label={booking.offeringType.replace(/_/g, ' ')} size="small" sx={{ bgcolor: 'rgba(102,126,234,0.08)', color: '#667eea', fontWeight: 600 }} />
                        )}
                        {booking.durationMinutes && (
                          <Chip label={`${booking.durationMinutes} min`} size="small" variant="outlined" />
                        )}
                      </Box>
                    </Box>
                  )}

                  <Divider sx={{ my: 3 }} />

                  {booking.artistDisplayName && (
                    <Box sx={{ mb: 3 }}>
                      <Typography variant="overline" sx={{ color: '#667eea', fontWeight: 700 }}>Artist</Typography>
                      <Typography variant="body1" sx={{ fontWeight: 600 }}>{booking.artistDisplayName}</Typography>
                    </Box>
                  )}

                  <Divider sx={{ my: 3 }} />

                  <Box>
                    <Typography variant="overline" sx={{ color: '#667eea', fontWeight: 700 }}>Schedule</Typography>
                    {booking.scheduledAt && (
                      <Typography variant="body1" sx={{ fontWeight: 500, mt: 0.5 }}>
                        {new Date(booking.scheduledAt).toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
                        {' at '}
                        {new Date(booking.scheduledAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </Typography>
                    )}
                    {booking.meetingLink && (
                      <Button href={booking.meetingLink} target="_blank" rel="noopener noreferrer" variant="outlined" size="small" sx={{ mt: 1.5 }}>
                        Join Meeting →
                      </Button>
                    )}
                  </Box>

                  {(booking.goalNotes || booking.notes) && (
                    <>
                      <Divider sx={{ my: 3 }} />
                      {booking.goalNotes && (
                        <Box sx={{ mb: 2 }}>
                          <Typography variant="overline" sx={{ color: '#667eea', fontWeight: 700 }}>Goals</Typography>
                          <Box sx={{ mt: 0.5, p: 2, bgcolor: '#f8f9fe', borderRadius: '12px' }}>
                            <Typography variant="body2" sx={{ color: '#64648b' }}>{booking.goalNotes}</Typography>
                          </Box>
                        </Box>
                      )}
                      {booking.notes && (
                        <Box>
                          <Typography variant="overline" sx={{ color: '#667eea', fontWeight: 700 }}>Notes</Typography>
                          <Box sx={{ mt: 0.5, p: 2, bgcolor: '#f8f9fe', borderRadius: '12px' }}>
                            <Typography variant="body2" sx={{ color: '#64648b' }}>{booking.notes}</Typography>
                          </Box>
                        </Box>
                      )}
                    </>
                  )}
                </CardContent>
              </Card>
            </Grid>

            {/* Sidebar */}
            <Grid item xs={12} md={4}>
              <Card sx={{ position: 'sticky', top: 100 }}>
                <CardContent sx={{ p: 3 }}>
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>Summary</Typography>

                  {booking.offeringPrice && (
                    <Box sx={{ mb: 2 }}>
                      <Typography variant="body2" sx={{ color: '#64648b', mb: 0.5 }}>Total Price</Typography>
                      <Typography
                        variant="h4"
                        sx={{
                          fontWeight: 800,
                          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                          WebkitBackgroundClip: 'text',
                          WebkitTextFillColor: 'transparent',
                        }}
                      >
                        ${booking.offeringPrice}
                        <Typography component="span" sx={{ fontSize: '1rem', color: '#64648b', WebkitTextFillColor: '#64648b' }}>
                          {' '}{booking.offeringCurrency || 'USD'}
                        </Typography>
                      </Typography>
                    </Box>
                  )}

                  <Divider sx={{ my: 2 }} />

                  {/* Link to fan's portfolio - helps artist understand their journey */}
                  {booking.userId && (
                    <Button
                      variant="outlined"
                      fullWidth
                      onClick={() => navigate(`/portfolio/view/${booking.userId}`)}
                      sx={{ mb: 2, borderRadius: '10px', fontWeight: 600 }}
                    >
                      📁 View Fan's Portfolio
                    </Button>
                  )}

                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
                    {booking.status === 'PENDING' && (
                      <>
                        <Button variant="contained" fullWidth onClick={handlePayNow} disabled={actionLoading} sx={{ py: 1.5 }}>
                          {actionLoading ? 'Processing...' : `Pay $${booking.offeringPrice || '0'}`}
                        </Button>
                        <Button variant="text" color="error" fullWidth onClick={handleCancelBooking} disabled={actionLoading} sx={{ borderRadius: '10px' }}>
                          Cancel Booking
                        </Button>
                      </>
                    )}
                    {booking.status === 'CONFIRMED' && (
                      <>
                        <Button variant="contained" fullWidth onClick={handleCompleteBooking} disabled={actionLoading} sx={{ py: 1.5 }}>
                          {actionLoading ? 'Processing...' : 'Mark Complete'}
                        </Button>
                        <Button variant="text" color="error" fullWidth onClick={handleCancelBooking} disabled={actionLoading}>
                          Cancel Booking
                        </Button>
                      </>
                    )}
                    {booking.status === 'COMPLETED' && (
                      <>
                        <Alert severity="success" sx={{ borderRadius: '12px', mb: 2 }}>Session completed. Thank you!</Alert>
                        {hasReviewed ? (
                          <Chip label="✅ You've reviewed this session" sx={{ fontWeight: 600, bgcolor: 'rgba(56,178,172,0.1)', color: '#38b2ac' }} />
                        ) : (
                          <Button variant="contained" fullWidth onClick={() => setReviewOpen(true)}
                            sx={{ py: 1.5, background: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)', color: '#1a1a2e', fontWeight: 700, fontSize: '1rem',
                              '&:hover': { boxShadow: '0 4px 20px rgba(253,160,133,0.5)' } }}>
                            ⭐ Leave a Review
                          </Button>
                        )}
                      </>
                    )}
                    {booking.status === 'CANCELLED' && (
                      <Alert severity="error" sx={{ borderRadius: '12px' }}>This booking was cancelled.</Alert>
                    )}
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Box>
      </Container>

      {/* Review Dialog */}
      <Dialog open={reviewOpen} onClose={() => !reviewSubmitting && setReviewOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle sx={{ fontWeight: 700, pb: 0.5 }}>⭐ Rate Your Experience</DialogTitle>
        <DialogContent>
          <Box sx={{ pt: 1 }}>
            {/* Star Rating */}
            <Box sx={{ textAlign: 'center', mb: 3 }}>
              <Typography sx={{ color: '#64648b', mb: 1.5, fontWeight: 500 }}>
                How would you rate this session?
              </Typography>
              <Rating
                value={reviewRating}
                onChange={(_, val) => setReviewRating(val)}
                size="large"
                sx={{ fontSize: '3rem',
                  '& .MuiRating-iconFilled': { color: '#f6d365' },
                  '& .MuiRating-iconHover': { color: '#fda085' },
                }}
              />
              <Typography sx={{ mt: 1, fontWeight: 700, fontSize: '1.1rem', color:
                reviewRating === 5 ? '#38b2ac' : reviewRating === 4 ? '#667eea' : reviewRating === 3 ? '#fda085' : reviewRating === 2 ? '#f5576c' : reviewRating === 1 ? '#f5576c' : '#64648b'
              }}>
                {reviewRating === 5 && 'Excellent! 🌟'}
                {reviewRating === 4 && 'Very Good 👍'}
                {reviewRating === 3 && 'Good 😊'}
                {reviewRating === 2 && 'Fair 😐'}
                {reviewRating === 1 && 'Poor 😞'}
                {!reviewRating && 'Tap to rate'}
              </Typography>
            </Box>

            {/* Predefined Tags */}
            <Typography sx={{ fontWeight: 600, mb: 1.5, color: '#1a1a2e' }}>
              What stood out? <Typography component="span" sx={{ color: '#64648b', fontWeight: 400 }}>(Select all that apply)</Typography>
            </Typography>
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mb: 3 }}>
              {PREDEFINED_TAGS.map(t => (
                <Chip key={t.key} label={t.label} onClick={() => toggleTag(t.key)}
                  variant={reviewTags.includes(t.key) ? 'filled' : 'outlined'}
                  sx={{
                    fontWeight: 600, fontSize: '0.78rem', borderRadius: '10px', cursor: 'pointer',
                    ...(reviewTags.includes(t.key)
                      ? { background: 'linear-gradient(135deg, #667eea, #764ba2)', color: '#fff', borderColor: 'transparent' }
                      : { borderColor: 'rgba(102,126,234,0.25)', color: '#667eea', '&:hover': { bgcolor: 'rgba(102,126,234,0.06)' } }),
                  }}
                />
              ))}
            </Box>

            {/* Comment */}
            <TextField
              label="Additional comments (optional)"
              multiline rows={3} fullWidth
              value={reviewComment}
              onChange={(e) => setReviewComment(e.target.value)}
              placeholder="Share more details about your experience..."
              sx={{ '& .MuiOutlinedInput-root': { borderRadius: '14px' } }}
            />
          </Box>
        </DialogContent>
        <DialogActions sx={{ p: 3, pt: 1 }}>
          <Button onClick={() => setReviewOpen(false)} disabled={reviewSubmitting}>Cancel</Button>
          <Button variant="contained" onClick={handleSubmitReview} disabled={reviewSubmitting || !reviewRating}
            sx={{ px: 4, background: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)', color: '#1a1a2e', fontWeight: 700,
              '&:hover': { boxShadow: '0 4px 20px rgba(253,160,133,0.5)' } }}>
            {reviewSubmitting ? 'Submitting...' : 'Submit Review'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
