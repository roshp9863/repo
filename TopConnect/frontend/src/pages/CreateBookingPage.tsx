import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Box,
  Typography,
  CircularProgress,
  Alert,
  Button,
  Card,
  CardContent,
  TextField,
  Grid,
  Divider,
  Chip,
} from '@mui/material';
import { offeringAPI, bookingAPI, paymentAPI } from '../services/apiClient';
import { useAuth } from '../context/AuthContext';
import { useSnackbar } from 'notistack';

export default function CreateBookingPage() {
  const { offeringId } = useParams<{ offeringId: string }>();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { enqueueSnackbar } = useSnackbar();
  const [offering, setOffering] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  // Block artists from accessing this page
  useEffect(() => {
    if (user?.role === 'ARTIST') {
      enqueueSnackbar('Artists cannot book services. Please sign up with a fan account to book.', { variant: 'warning' });
      navigate('/artists');
    }
  }, [user]);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    scheduledAt: '',
    notes: '',
    goalNotes: '',
  });

  useEffect(() => {
    if (offeringId) {
      fetchOffering();
    }
  }, [offeringId]);

  const fetchOffering = async () => {
    if (!offeringId) return;
    setLoading(true);
    setError('');
    try {
      const response = await offeringAPI.get(offeringId);
      setOffering(response.data.data);
    } catch (err: any) {
      console.error('Error fetching offering:', err);
      setError('Failed to load offering details.');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!offeringId) return;

    if (!formData.scheduledAt) {
      setError('Please select a date and time for the session.');
      return;
    }

    setSubmitting(true);
    setError('');

    try {
      // Create booking
      const bookingResponse = await bookingAPI.create({
        offeringId,
        scheduledAt: new Date(formData.scheduledAt).toISOString(),
        notes: formData.notes,
        goalNotes: formData.goalNotes,
      });

      const booking = bookingResponse.data.data;
      enqueueSnackbar('Booking created successfully!', { variant: 'success' });

      // Attempt payment (dev mode will auto-confirm)
      try {
        const paymentResponse = await paymentAPI.createCheckout(booking.id);
        const paymentData = paymentResponse.data.data;

        if (paymentData.publishableKey === 'dev_mode') {
          enqueueSnackbar('Payment processed (dev mode) - Booking confirmed!', { variant: 'success' });
          navigate(`/booking/${booking.id}`);
        } else {
          // In production, redirect to Stripe checkout
          window.location.href = paymentData.sessionId;
        }
      } catch (payErr: any) {
        console.error('Payment error:', payErr);
        enqueueSnackbar('Booking created, but payment pending. You can pay later.', { variant: 'warning' });
        navigate(`/booking/${booking.id}`);
      }
    } catch (err: any) {
      const message = err.response?.data?.message || 'Failed to create booking';
      setError(message);
      enqueueSnackbar(message, { variant: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <Container maxWidth="md">
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (error && !offering) {
    return (
      <Container maxWidth="md">
        <Box sx={{ py: 4 }}>
          <Alert severity="error">{error}</Alert>
          <Button onClick={() => navigate('/artists')} sx={{ mt: 2 }}>
            Back to Artists
          </Button>
        </Box>
      </Container>
    );
  }

  // Get the minimum datetime (now + 1 hour)
  const minDateTime = new Date(Date.now() + 3600000).toISOString().slice(0, 16);

  return (
    <Box>
      {/* Header */}
      <Box sx={{ background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)', color: '#fff', py: { xs: 4, md: 6 }, borderRadius: 0 }}>
        <Container maxWidth="md">
          <Button onClick={() => navigate(-1)} sx={{ color: 'rgba(255,255,255,0.6)', mb: 2, '&:hover': { color: '#fff' } }}>
            ← Back
          </Button>
          <Typography variant="h3" sx={{ fontWeight: 800, fontSize: { xs: '1.8rem', md: '2.2rem' } }}>
            Book a Session
          </Typography>
          <Typography sx={{ color: 'rgba(255,255,255,0.6)', mt: 1 }}>
            Complete your booking details below
          </Typography>
        </Container>
      </Box>

      <Container maxWidth="md">
        <Box sx={{ py: 5 }}>
          <Grid container spacing={4}>
            {/* Offering Details */}
            <Grid item xs={12} md={5}>
              <Card sx={{ overflow: 'hidden', position: 'sticky', top: 100 }}>
                <Box sx={{ height: 6, background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }} />
                <CardContent sx={{ p: 3 }}>
                  <Typography variant="overline" sx={{ color: '#667eea', fontWeight: 700 }}>
                    {offering?.type?.replace(/_/g, ' ')}
                  </Typography>
                  <Typography variant="h6" sx={{ fontWeight: 700, mt: 0.5, mb: 1 }}>
                    {offering?.title}
                  </Typography>
                  <Typography variant="body2" sx={{ color: '#64648b', mb: 2, lineHeight: 1.7 }}>
                    {offering?.description}
                  </Typography>
                  <Chip label={`${offering?.durationMinutes} min`} size="small" variant="outlined" sx={{ mb: 2 }} />
                  <Divider sx={{ my: 2 }} />
                  <Typography
                    variant="h4"
                    sx={{
                      fontWeight: 800,
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      WebkitBackgroundClip: 'text',
                      WebkitTextFillColor: 'transparent',
                    }}
                  >
                    ${offering?.price}
                    <Typography component="span" sx={{ fontSize: '1rem', color: '#64648b', WebkitTextFillColor: '#64648b' }}>
                      {' '}{offering?.currency || 'USD'}
                    </Typography>
                  </Typography>
                </CardContent>
              </Card>
            </Grid>

            {/* Booking Form */}
            <Grid item xs={12} md={7}>
              <Card>
                <CardContent sx={{ p: 3 }}>
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 3 }}>
                    Schedule Your Session
                  </Typography>

                  {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}

                  <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2.5 }}>
                    <TextField
                      label="Preferred Date & Time"
                      type="datetime-local"
                      name="scheduledAt"
                      value={formData.scheduledAt}
                      onChange={handleChange}
                      required
                      fullWidth
                      InputLabelProps={{ shrink: true }}
                      inputProps={{ min: minDateTime }}
                    />

                    <TextField
                      label="What are your goals for this session?"
                      name="goalNotes"
                      value={formData.goalNotes}
                      onChange={handleChange}
                      multiline
                      rows={3}
                      fullWidth
                      placeholder="E.g., I want to learn how to mix vocals..."
                    />

                    <TextField
                      label="Additional Notes (optional)"
                      name="notes"
                      value={formData.notes}
                      onChange={handleChange}
                      multiline
                      rows={2}
                      fullWidth
                      placeholder="Any additional information for the artist..."
                    />

                    <Divider sx={{ my: 1 }} />

                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', px: 1 }}>
                      <Typography variant="body1" sx={{ color: '#64648b' }}>Total</Typography>
                      <Typography variant="h5" sx={{ fontWeight: 800, color: '#1a1a2e' }}>
                        ${offering?.price} {offering?.currency || 'USD'}
                      </Typography>
                    </Box>

                    <Button
                      type="submit"
                      variant="contained"
                      size="large"
                      disabled={submitting}
                      fullWidth
                      sx={{ py: 1.75, fontSize: '1rem' }}
                    >
                      {submitting ? 'Processing...' : `Book & Pay $${offering?.price}`}
                    </Button>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Box>
      </Container>
    </Box>
  );
}

