import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Box, Typography, CircularProgress, Button, Alert, Grid, Card, CardContent, Chip, Avatar, Tabs, Tab, Dialog, DialogTitle, DialogContent, DialogActions, Rating, LinearProgress } from '@mui/material';
import { artistAPI, chatAPI, portfolioAPI, bookingAPI, paymentAPI, reviewAPI } from '../services/apiClient';
import { useAuth } from '../context/AuthContext';
import { useSnackbar } from 'notistack';

const CATEGORY_ICONS: Record<string, string> = {
  ACTOR: '🎭', DANCER: '💃', CRICKETER: '🏏', PAINTER: '🎨',
  YOUTUBER: '📺', INFLUENCER: '⭐', CREATOR: '✨', SINGER: '🎤',
};

export default function ArtistDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();
  const { enqueueSnackbar } = useSnackbar();
  const [artist, setArtist] = useState<any>(null);
  const [offerings, setOfferings] = useState<any[]>([]);
  const [portfolio, setPortfolio] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [offeringsLoading, setOfferingsLoading] = useState(false);
  const [error, setError] = useState('');
  const [activeSection, setActiveSection] = useState(0);
  const [chatPaymentOpen, setChatPaymentOpen] = useState(false);
  const [chatProcessing, setChatProcessing] = useState(false);
  const [reviews, setReviews] = useState<any[]>([]);
  const [reviewSummary, setReviewSummary] = useState<any>(null);

  const TAG_LABELS: Record<string, string> = {
    VERY_HELPFUL: '💡 Very Helpful', GREAT_COMMUNICATOR: '💬 Great Communicator', ON_TIME: '⏰ On Time',
    EXCEEDED_EXPECTATIONS: '🚀 Exceeded Expectations', PROFESSIONAL: '👔 Professional', FRIENDLY: '😊 Friendly',
    EXPERT_KNOWLEDGE: '🎓 Expert Knowledge', GOOD_VALUE: '💰 Good Value', HIGHLY_RECOMMENDED: '⭐ Highly Recommended',
    CREATIVE: '🎨 Creative', PATIENT: '🧘 Patient', NEEDS_IMPROVEMENT: '📝 Needs Improvement',
  };

  useEffect(() => { fetchArtistDetails(); }, [id]);

  const fetchArtistDetails = async () => {
    if (!id) return;
    setLoading(true);
    setError('');
    try {
      const response = await artistAPI.get(id);
      const artistData = response.data.data;
      setArtist(artistData);
      await fetchOfferings();
      // Fetch artist portfolio by their userId
      if (artistData?.userId) {
        try {
          const pRes = await portfolioAPI.getPublic(artistData.userId);
          setPortfolio(pRes.data.data || []);
        } catch { /* artist may not have portfolio items yet */ }
      }
      // Fetch reviews
      try {
        const [revRes, sumRes] = await Promise.all([
          reviewAPI.getArtistReviews(id, 0, 20),
          reviewAPI.getArtistSummary(id),
        ]);
        setReviews(revRes.data.data.content || []);
        setReviewSummary(sumRes.data.data);
      } catch { /* no reviews yet */ }
    } catch (err: any) {
      setError('Failed to load artist profile.');
    } finally {
      setLoading(false);
    }
  };

  const fetchOfferings = async () => {
    if (!id) return;
    setOfferingsLoading(true);
    try {
      const response = await artistAPI.getOfferings(id, 0, 10);
      setOfferings(response.data.data.content || []);
    } catch (err: any) { /* ignore */ }
    finally { setOfferingsLoading(false); }
  };

  const handleBooking = (offeringId: string) => {
    if (!isAuthenticated) { navigate('/login'); return; }
    if (user?.role === 'ARTIST') {
      enqueueSnackbar('Artists cannot book services. Please sign up with a fan account to book.', { variant: 'warning' });
      return;
    }
    navigate(`/book/${offeringId}`);
  };

  const handleMessage = async () => {
    if (!isAuthenticated) { navigate('/login'); return; }
    if (!artist) return;

    // If artist has a CHAT offering, show payment dialog first
    const chatOffering = offerings.find((o: any) => o.type === 'CHAT');
    if (chatOffering && user?.role !== 'ARTIST') {
      setChatPaymentOpen(true);
      return;
    }

    // No chat offering — just open the chat directly (for artists chatting, or free chat)
    try {
      const response = await chatAPI.createOrGetThread(artist.id);
      navigate(`/chat/${response.data.data.threadId}`);
    } catch (err: any) {
      enqueueSnackbar('Failed to start chat.', { variant: 'error' });
    }
  };

  const handleChatPayment = async () => {
    if (!artist) return;
    const chatOffering = offerings.find((o: any) => o.type === 'CHAT');
    if (!chatOffering) return;

    setChatProcessing(true);
    try {
      // Create a booking for the CHAT offering
      const bookingRes = await bookingAPI.create({
        offeringId: chatOffering.id,
        scheduledAt: new Date().toISOString(),
        notes: 'Chat session payment',
      });
      const booking = bookingRes.data.data;

      // Process payment
      const payRes = await paymentAPI.createCheckout(booking.id);
      const paymentData = payRes.data.data;

      if (paymentData.publishableKey === 'dev_mode') {
        enqueueSnackbar('Chat payment processed! Opening conversation...', { variant: 'success' });
      }

      // Open chat thread
      const threadRes = await chatAPI.createOrGetThread(artist.id);
      setChatPaymentOpen(false);
      navigate(`/chat/${threadRes.data.data.threadId}`);
    } catch (err: any) {
      const msg = err.response?.data?.message || 'Failed to process chat payment';
      enqueueSnackbar(msg, { variant: 'error' });
    } finally {
      setChatProcessing(false);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 12 }}>
        <CircularProgress sx={{ color: '#667eea' }} />
      </Box>
    );
  }

  if (error || !artist) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ py: 6 }}>
          <Alert severity="error">{error || 'Artist not found'}</Alert>
          <Button onClick={() => navigate('/artists')} sx={{ mt: 2 }}>← Back to Artists</Button>
        </Box>
      </Container>
    );
  }

  const offeringTypeGradients: Record<string, string> = {
    CALL: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    VIDEO: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    CHAT: 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)',
    MENTORING: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
    WORKSHOP: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)',
    REVIEW: 'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)',
    DIGITAL_DOWNLOAD: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)',
    CUSTOM: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
  };

  const portfolioCategoryGradients: Record<string, string> = {
    WORK_SAMPLE: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    ACHIEVEMENT: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)',
    CERTIFICATION: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
    EXPERIENCE: 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)',
    SKILL: 'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)',
  };

  const categoryLabels: Record<string, string> = {
    WORK_SAMPLE: '🎨 Work Sample', ACHIEVEMENT: '🏆 Achievement',
    CERTIFICATION: '📜 Certification', EXPERIENCE: '💼 Experience',
    SKILL: '⚡ Skill', GOAL: '🎯 Goal', JOURNEY: '🚀 Journey',
  };

  return (
    <Box>
      {/* Hero Banner */}
      <Box
        sx={{
          background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)',
          color: '#fff',
          py: { xs: 5, md: 8 },
          position: 'relative',
          overflow: 'hidden',
          borderRadius: 0,
          '&::before': {
            content: '""',
            position: 'absolute',
            top: '-60%',
            right: '-20%',
            width: '500px',
            height: '500px',
            borderRadius: '50%',
            background: 'radial-gradient(circle, rgba(102,126,234,0.15) 0%, transparent 70%)',
          },
        }}
      >
        <Container maxWidth="lg" sx={{ position: 'relative', zIndex: 1 }}>
          <Button onClick={() => navigate('/artists')} sx={{ color: 'rgba(255,255,255,0.6)', mb: 3, '&:hover': { color: '#fff' } }}>
            ← Back to Artists
          </Button>
          <Grid container spacing={4} alignItems="center">
            <Grid item xs={12} md={2}>
              <Avatar src={artist.avatarUrl} sx={{ width: { xs: 100, md: 120 }, height: { xs: 100, md: 120 }, fontSize: '3rem', fontWeight: 800, background: 'linear-gradient(135deg, #667eea 0%, #f093fb 100%)', border: '4px solid rgba(255,255,255,0.2)', boxShadow: '0 8px 32px rgba(0,0,0,0.3)' }}>
                {(artist.displayName || 'A').charAt(0)}
              </Avatar>
            </Grid>
            <Grid item xs={12} md={7}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 1, flexWrap: 'wrap' }}>
                <Typography variant="h3" sx={{ fontWeight: 800, fontSize: { xs: '2rem', md: '2.5rem' } }}>
                  {artist.displayName}
                </Typography>
                {artist.featured && (
                  <Chip label="Featured" size="small" sx={{ background: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)', color: '#1a1a2e', fontWeight: 700 }} />
                )}
                {portfolio.length > 0 && (
                  <Chip label={`${portfolio.length} portfolio items`} size="small" sx={{ bgcolor: 'rgba(255,255,255,0.1)', color: 'rgba(255,255,255,0.8)', fontWeight: 500 }} />
                )}
              </Box>
              {artist.bio && (
                <Typography sx={{ color: 'rgba(255,255,255,0.6)', lineHeight: 1.7, maxWidth: 500 }}>
                  {artist.bio}
                </Typography>
              )}
              {/* Categories */}
              {artist.categories && artist.categories.length > 0 && (
                <Box sx={{ display: 'flex', gap: 0.8, flexWrap: 'wrap', mt: 1.5 }}>
                  {artist.categories.map((cat: string) => (
                    <Chip key={cat} label={`${CATEGORY_ICONS[cat] || '🎯'} ${cat.charAt(0) + cat.slice(1).toLowerCase()}`}
                      size="small" sx={{ bgcolor: 'rgba(255,255,255,0.1)', color: 'rgba(255,255,255,0.9)', fontWeight: 600, fontSize: '0.72rem', backdropFilter: 'blur(4px)' }} />
                  ))}
                </Box>
              )}
              {artist.ratingCount > 0 && (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mt: 2 }}>
                  <Typography sx={{ color: '#f6d365' }}>★</Typography>
                  <Typography sx={{ fontWeight: 700 }}>{(artist.ratingAggregate || 0).toFixed(1)}</Typography>
                  <Typography sx={{ color: 'rgba(255,255,255,0.5)' }}>({artist.ratingCount} reviews)</Typography>
                </Box>
              )}
            </Grid>
            <Grid item xs={12} md={3} sx={{ display: 'flex', justifyContent: { md: 'flex-end' } }}>
              {artist.hasChatOffering ? (
                <Button size="large" onClick={handleMessage} sx={{ color: '#fff', px: 4, py: 1.5, borderRadius: '14px', border: '2px solid rgba(255,255,255,0.2)', fontWeight: 600, '&:hover': { background: 'rgba(255,255,255,0.1)', borderColor: 'rgba(255,255,255,0.4)' } }}>
                  💬 Send Message
                </Button>
              ) : (
                <Button size="large" disabled sx={{ color: 'rgba(255,255,255,0.3)', px: 4, py: 1.5, borderRadius: '14px', border: '2px solid rgba(255,255,255,0.08)', fontWeight: 600 }}>
                  Chat Not Available
                </Button>
              )}
            </Grid>
          </Grid>
        </Container>
      </Box>

      {/* Tabs: Services | Portfolio */}
      <Container maxWidth="lg">
        <Box sx={{ pt: 4 }}>
          <Tabs
            value={activeSection}
            onChange={(_, v) => setActiveSection(v)}
            sx={{
              mb: 4,
              '& .MuiTab-root': { fontWeight: 700, textTransform: 'none', fontSize: '1rem', px: 3 },
              '& .Mui-selected': { color: '#667eea' },
              '& .MuiTabs-indicator': { background: '#667eea', height: 3, borderRadius: 2 },
            }}
          >
            <Tab label={`Services (${offerings.length})`} />
            <Tab label={`Portfolio (${portfolio.length})`} />
            <Tab label={`Reviews (${reviewSummary?.totalReviews || 0})`} />
          </Tabs>
        </Box>

        {/* Services Tab */}
        {activeSection === 0 && (
          <Box sx={{ pb: 6 }}>
            {offeringsLoading ? (
              <Box sx={{ display: 'flex', justifyContent: 'center', py: 6 }}><CircularProgress sx={{ color: '#667eea' }} /></Box>
            ) : offerings.length > 0 ? (
              <Grid container spacing={3}>
                {offerings.map((offering: any) => (
                  <Grid item xs={12} sm={6} md={4} key={offering.id}>
                    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
                      <Box sx={{ height: 6, background: offeringTypeGradients[offering.type] || offeringTypeGradients.CALL }} />
                      <CardContent sx={{ flexGrow: 1, p: 3 }}>
                        <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
                          <Chip label={offering.type?.replace(/_/g, ' ')} size="small" sx={{ fontWeight: 600, fontSize: '0.7rem', bgcolor: 'rgba(102,126,234,0.08)', color: '#667eea' }} />
                          <Chip label={`${offering.durationMinutes} min`} size="small" variant="outlined" sx={{ fontWeight: 500, fontSize: '0.7rem' }} />
                        </Box>
                        <Typography variant="h6" sx={{ fontWeight: 700, mb: 1 }}>{offering.title}</Typography>
                        <Typography variant="body2" sx={{ color: '#64648b', mb: 3, lineHeight: 1.7 }}>{offering.description}</Typography>
                        <Typography variant="h5" sx={{ fontWeight: 800, background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
                          ${offering.price}
                          <Typography component="span" sx={{ fontSize: '0.9rem', color: '#64648b', WebkitTextFillColor: '#64648b' }}> {offering.currency || 'USD'}</Typography>
                        </Typography>
                      </CardContent>
                      <Box sx={{ px: 3, pb: 3 }}>
                        {user?.role === 'ARTIST' ? (
                          <Button variant="outlined" fullWidth disabled sx={{ py: 1.2, fontSize: '0.8rem' }}>
                            Sign in as Fan to Book
                          </Button>
                        ) : (
                          <Button variant="contained" fullWidth onClick={() => handleBooking(offering.id)}>
                            Book Now →
                          </Button>
                        )}
                      </Box>
                    </Card>
                  </Grid>
                ))}
              </Grid>
            ) : (
              <Box sx={{ textAlign: 'center', py: 8, bgcolor: '#fff', borderRadius: '16px', border: '1px solid rgba(0,0,0,0.04)' }}>
                <Typography sx={{ color: '#64648b' }}>No offerings available yet. Check back soon!</Typography>
              </Box>
            )}
          </Box>
        )}

        {/* Portfolio Tab */}
        {activeSection === 1 && (
          <Box sx={{ pb: 6 }}>
            {portfolio.length > 0 ? (
              <Grid container spacing={3}>
                {portfolio.map((item: any) => (
                  <Grid item xs={12} sm={6} md={4} key={item.id}>
                    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
                      <Box sx={{ height: 6, background: portfolioCategoryGradients[item.category] || portfolioCategoryGradients.WORK_SAMPLE }} />
                      {item.mediaUrl && item.mediaType === 'IMAGE' && (
                        <Box sx={{ height: 180, backgroundImage: `url(${item.mediaUrl})`, backgroundSize: 'cover', backgroundPosition: 'center' }} />
                      )}
                      <CardContent sx={{ flexGrow: 1, p: 3 }}>
                        <Chip
                          label={categoryLabels[item.category] || item.category}
                          size="small"
                          sx={{ fontWeight: 600, fontSize: '0.7rem', bgcolor: 'rgba(102,126,234,0.08)', color: '#667eea', mb: 1.5 }}
                        />
                        <Typography variant="h6" sx={{ fontWeight: 700, mb: 1 }}>{item.title}</Typography>
                        {item.description && (
                          <Typography variant="body2" sx={{ color: '#64648b', lineHeight: 1.7, display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                            {item.description}
                          </Typography>
                        )}
                        {item.tags && (
                          <Box sx={{ display: 'flex', gap: 0.5, flexWrap: 'wrap', mt: 2 }}>
                            {item.tags.split(',').map((tag: string, i: number) => (
                              <Chip key={i} label={tag.trim()} size="small" variant="outlined" sx={{ fontSize: '0.65rem' }} />
                            ))}
                          </Box>
                        )}
                        {item.mediaUrl && item.mediaType === 'LINK' && (
                          <Button size="small" href={item.mediaUrl} target="_blank" sx={{ mt: 1.5, fontSize: '0.8rem' }}>
                            🔗 View Link
                          </Button>
                        )}
                      </CardContent>
                    </Card>
                  </Grid>
                ))}
              </Grid>
            ) : (
              <Box sx={{ textAlign: 'center', py: 8, bgcolor: '#fff', borderRadius: '16px', border: '1px solid rgba(0,0,0,0.04)' }}>
                <Typography sx={{ fontSize: '2rem', mb: 1 }}>📁</Typography>
                <Typography sx={{ color: '#64648b' }}>This artist hasn't added portfolio items yet.</Typography>
              </Box>
            )}
          </Box>
        )}

        {/* Reviews Tab */}
        {activeSection === 2 && (
          <Box sx={{ pb: 6 }}>
            {reviewSummary && reviewSummary.totalReviews > 0 ? (
              <>
                {/* Review Summary Card */}
                <Card sx={{ mb: 4, overflow: 'hidden' }}>
                  <Box sx={{ height: 4, background: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)' }} />
                  <CardContent sx={{ p: 4 }}>
                    <Grid container spacing={4} alignItems="center">
                      {/* Average Rating */}
                      <Grid item xs={12} sm={4} sx={{ textAlign: 'center' }}>
                        <Typography variant="h2" sx={{ fontWeight: 800, color: '#1a1a2e', lineHeight: 1 }}>
                          {Number(reviewSummary.averageRating).toFixed(1)}
                        </Typography>
                        <Rating value={reviewSummary.averageRating} precision={0.1} readOnly size="large"
                          sx={{ mt: 1, '& .MuiRating-iconFilled': { color: '#f6d365' } }} />
                        <Typography sx={{ color: '#64648b', mt: 0.5, fontWeight: 500 }}>
                          {reviewSummary.totalReviews} review{reviewSummary.totalReviews !== 1 ? 's' : ''}
                        </Typography>
                      </Grid>
                      {/* Rating Distribution */}
                      <Grid item xs={12} sm={4}>
                        {reviewSummary.ratingDistribution && Object.entries(reviewSummary.ratingDistribution).map(([stars, count]: any) => {
                          const pct = reviewSummary.totalReviews > 0 ? (count / reviewSummary.totalReviews) * 100 : 0;
                          return (
                            <Box key={stars} sx={{ display: 'flex', alignItems: 'center', gap: 1.5, mb: 0.8 }}>
                              <Typography sx={{ fontSize: '0.8rem', fontWeight: 600, minWidth: 20, textAlign: 'right' }}>{stars}★</Typography>
                              <LinearProgress variant="determinate" value={pct} sx={{ flex: 1, height: 8, borderRadius: 4,
                                bgcolor: 'rgba(0,0,0,0.04)',
                                '& .MuiLinearProgress-bar': { background: stars >= 4 ? '#f6d365' : stars === 3 ? '#fda085' : '#f5576c', borderRadius: 4 },
                              }} />
                              <Typography sx={{ fontSize: '0.75rem', color: '#64648b', minWidth: 20 }}>{count}</Typography>
                            </Box>
                          );
                        })}
                      </Grid>
                      {/* Top Tags */}
                      <Grid item xs={12} sm={4}>
                        <Typography sx={{ fontWeight: 600, mb: 1.5, fontSize: '0.9rem' }}>Top Highlights</Typography>
                        <Box sx={{ display: 'flex', gap: 0.8, flexWrap: 'wrap' }}>
                          {(reviewSummary.topTags || []).slice(0, 6).map((t: any) => (
                            <Chip key={t.tag} label={`${TAG_LABELS[t.tag] || t.tag} (${t.count})`} size="small"
                              sx={{ fontSize: '0.68rem', fontWeight: 600, bgcolor: 'rgba(102,126,234,0.06)', color: '#667eea', borderRadius: '8px' }} />
                          ))}
                          {(!reviewSummary.topTags || reviewSummary.topTags.length === 0) && (
                            <Typography sx={{ color: '#a0a0b8', fontSize: '0.85rem' }}>No tags yet</Typography>
                          )}
                        </Box>
                      </Grid>
                    </Grid>
                  </CardContent>
                </Card>

                {/* Individual Reviews */}
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                  {reviews.map((review: any) => (
                    <Card key={review.id} sx={{ overflow: 'hidden' }}>
                      <CardContent sx={{ p: 3 }}>
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1.5 }}>
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                            <Avatar sx={{ width: 40, height: 40, bgcolor: '#667eea', fontSize: '0.9rem', fontWeight: 700 }}>
                              {(review.reviewerName || 'U').charAt(0).toUpperCase()}
                            </Avatar>
                            <Box>
                              <Typography sx={{ fontWeight: 600, fontSize: '0.9rem' }}>{review.reviewerName || 'User'}</Typography>
                              <Typography variant="caption" sx={{ color: '#64648b' }}>
                                {review.createdAt ? new Date(review.createdAt).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' }) : ''}
                              </Typography>
                            </Box>
                          </Box>
                          <Rating value={review.rating} readOnly size="small" sx={{ '& .MuiRating-iconFilled': { color: '#f6d365' } }} />
                        </Box>
                        {review.comment && (
                          <Typography sx={{ color: '#1a1a2e', lineHeight: 1.7, mb: 1.5, fontSize: '0.95rem' }}>
                            "{review.comment}"
                          </Typography>
                        )}
                        {review.tags && review.tags.length > 0 && (
                          <Box sx={{ display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
                            {review.tags.map((tag: string) => (
                              <Chip key={tag} label={TAG_LABELS[tag] || tag} size="small"
                                sx={{ fontSize: '0.65rem', fontWeight: 600, bgcolor: 'rgba(102,126,234,0.04)', color: '#667eea', height: 24, borderRadius: '8px' }} />
                            ))}
                          </Box>
                        )}
                      </CardContent>
                    </Card>
                  ))}
                </Box>
              </>
            ) : (
              <Box sx={{ textAlign: 'center', py: 10, bgcolor: '#fff', borderRadius: '16px', border: '1px solid rgba(0,0,0,0.04)' }}>
                <Typography sx={{ fontSize: '2.5rem', mb: 1 }}>⭐</Typography>
                <Typography variant="h6" sx={{ fontWeight: 600, mb: 0.5 }}>No reviews yet</Typography>
                <Typography sx={{ color: '#64648b' }}>Be the first to book a session and leave a review!</Typography>
              </Box>
            )}
          </Box>
        )}
      </Container>

      {/* Chat Payment Dialog */}
      <Dialog open={chatPaymentOpen} onClose={() => !chatProcessing && setChatPaymentOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle sx={{ fontWeight: 700, pb: 1 }}>💬 Start a Chat Session</DialogTitle>
        <DialogContent>
          {(() => {
            const chatOffering = offerings.find((o: any) => o.type === 'CHAT');
            if (!chatOffering) return null;
            return (
              <Box sx={{ pt: 1 }}>
                <Typography variant="body1" sx={{ color: '#64648b', mb: 2 }}>
                  To message <strong>{artist?.displayName}</strong>, you need to pay for a chat session.
                </Typography>
                <Card sx={{ bgcolor: '#f8f9fe', overflow: 'hidden' }}>
                  <Box sx={{ height: 4, background: 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)' }} />
                  <CardContent sx={{ p: 3 }}>
                    <Typography variant="h6" sx={{ fontWeight: 700, mb: 0.5 }}>{chatOffering.title}</Typography>
                    {chatOffering.description && (
                      <Typography variant="body2" sx={{ color: '#64648b', mb: 2 }}>{chatOffering.description}</Typography>
                    )}
                    <Box sx={{ display: 'flex', alignItems: 'baseline', gap: 0.5 }}>
                      <Typography variant="h4" sx={{ fontWeight: 800, background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
                        ${chatOffering.price}
                      </Typography>
                      <Typography sx={{ color: '#64648b' }}>{chatOffering.currency || 'USD'}</Typography>
                    </Box>
                  </CardContent>
                </Card>
              </Box>
            );
          })()}
        </DialogContent>
        <DialogActions sx={{ p: 3, pt: 1 }}>
          <Button onClick={() => setChatPaymentOpen(false)} disabled={chatProcessing}>Cancel</Button>
          <Button variant="contained" onClick={handleChatPayment} disabled={chatProcessing}
            sx={{ px: 4, background: 'linear-gradient(135deg, #667eea, #764ba2)', '&:hover': { background: 'linear-gradient(135deg, #764ba2, #667eea)' } }}>
            {chatProcessing ? 'Processing...' : 'Pay & Chat'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
