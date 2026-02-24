import React, { useState, useEffect } from 'react';
import {
  Container,
  Box,
  Typography,
  Button,
  Card,
  CardContent,
  Grid,
  Chip,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  InputAdornment,
} from '@mui/material';
import { offeringAPI, artistAPI } from '../services/apiClient';
import { useSnackbar } from 'notistack';

const SERVICE_TYPES = [
  { value: 'CALL', label: 'Phone Call', icon: '📞', description: 'One-on-one voice call' },
  { value: 'VIDEO', label: 'Video Call', icon: '🎥', description: 'Face-to-face video session' },
  { value: 'CHAT', label: 'Chat Session', icon: '💬', description: 'Text-based consultation' },
  { value: 'MENTORING', label: 'Mentoring', icon: '🎓', description: 'Guided mentoring session' },
  { value: 'WORKSHOP', label: 'Workshop', icon: '🛠️', description: 'Group or solo workshop' },
  { value: 'REVIEW', label: 'Review / Feedback', icon: '📝', description: 'Review work & give feedback' },
  { value: 'DIGITAL_DOWNLOAD', label: 'Digital Content', icon: '📦', description: 'Downloadable content or files' },
  { value: 'CUSTOM', label: 'Custom Service', icon: '✨', description: 'Define your own service' },
];

const typeGradients: Record<string, string> = {
  CALL: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
  VIDEO: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
  CHAT: 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)',
  MENTORING: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
  WORKSHOP: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)',
  REVIEW: 'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)',
  DIGITAL_DOWNLOAD: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)',
  CUSTOM: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
};

interface OfferingForm {
  type: string;
  title: string;
  description: string;
  durationMinutes: string;
  price: string;
  currency: string;
}

const emptyForm: OfferingForm = {
  type: '',
  title: '',
  description: '',
  durationMinutes: '',
  price: '',
  currency: 'USD',
};

export default function ArtistDashboardPage() {
  const { enqueueSnackbar } = useSnackbar();
  const [offerings, setOfferings] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [form, setForm] = useState<OfferingForm>(emptyForm);
  const [submitting, setSubmitting] = useState(false);
  const [deleteConfirmId, setDeleteConfirmId] = useState<string | null>(null);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [categorySaving, setCategorySaving] = useState(false);

  const ALL_CATEGORIES = ['ACTOR', 'DANCER', 'CRICKETER', 'PAINTER', 'YOUTUBER', 'INFLUENCER', 'CREATOR', 'SINGER'];
  const CATEGORY_ICONS: Record<string, string> = {
    ACTOR: '🎭', DANCER: '💃', CRICKETER: '🏏', PAINTER: '🎨',
    YOUTUBER: '📺', INFLUENCER: '⭐', CREATOR: '✨', SINGER: '🎤',
  };

  useEffect(() => {
    fetchOfferings();
    fetchProfile();
  }, []);

  const fetchOfferings = async () => {
    setLoading(true);
    try {
      const response = await offeringAPI.getMine();
      setOfferings(response.data.data || []);
    } catch (err: any) {
      enqueueSnackbar('Failed to load services', { variant: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const fetchProfile = async () => {
    try {
      const res = await artistAPI.getMyProfile();
      setSelectedCategories(res.data.data?.categories || []);
    } catch {
      // first load may not have categories yet
    }
  };

  const handleCategoryToggle = (cat: string) => {
    setSelectedCategories(prev =>
      prev.includes(cat) ? prev.filter(c => c !== cat) : [...prev, cat]
    );
  };

  const handleSaveCategories = async () => {
    setCategorySaving(true);
    try {
      await artistAPI.updateProfile({ categories: selectedCategories });
      enqueueSnackbar('Categories updated!', { variant: 'success' });
    } catch {
      enqueueSnackbar('Failed to save categories', { variant: 'error' });
    } finally {
      setCategorySaving(false);
    }
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};
    if (!form.type) newErrors.type = 'Please select a service type';
    if (!form.title.trim()) newErrors.title = 'Title is required';
    if (!form.price || parseFloat(form.price) <= 0) newErrors.price = 'Price must be greater than 0';
    if (!form.durationMinutes || parseInt(form.durationMinutes) <= 0)
      newErrors.durationMinutes = 'Duration must be greater than 0';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleOpenCreate = () => {
    setEditingId(null);
    setForm(emptyForm);
    setErrors({});
    setDialogOpen(true);
  };

  const handleOpenEdit = (offering: any) => {
    setEditingId(offering.id);
    setForm({
      type: offering.type,
      title: offering.title,
      description: offering.description || '',
      durationMinutes: String(offering.durationMinutes || ''),
      price: String(offering.price || ''),
      currency: offering.currency || 'USD',
    });
    setErrors({});
    setDialogOpen(true);
  };

  const handleSubmit = async () => {
    if (!validateForm()) return;

    setSubmitting(true);
    try {
      const payload = {
        type: form.type,
        title: form.title.trim(),
        description: form.description.trim(),
        durationMinutes: parseInt(form.durationMinutes),
        price: parseFloat(form.price),
        currency: form.currency,
      };

      if (editingId) {
        await offeringAPI.update(editingId, payload);
        enqueueSnackbar('Service updated successfully!', { variant: 'success' });
      } else {
        await offeringAPI.create(payload);
        enqueueSnackbar('Service created successfully!', { variant: 'success' });
      }
      setDialogOpen(false);
      fetchOfferings();
    } catch (err: any) {
      const msg = err.response?.data?.message || 'Failed to save service';
      enqueueSnackbar(msg, { variant: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  const handleToggleActive = async (offering: any) => {
    try {
      await offeringAPI.update(offering.id, { active: !offering.active });
      enqueueSnackbar(
        offering.active ? 'Service deactivated' : 'Service activated',
        { variant: 'success' }
      );
      fetchOfferings();
    } catch (err: any) {
      enqueueSnackbar('Failed to update service status', { variant: 'error' });
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await offeringAPI.delete(id);
      enqueueSnackbar('Service removed', { variant: 'success' });
      setDeleteConfirmId(null);
      fetchOfferings();
    } catch (err: any) {
      enqueueSnackbar('Failed to remove service', { variant: 'error' });
    }
  };

  const activeOfferings = offerings.filter((o) => o.active);
  const inactiveOfferings = offerings.filter((o) => !o.active);

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
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 2 }}>
            <Box>
              <Typography variant="h3" sx={{ fontWeight: 800, mb: 1, fontSize: { xs: '1.8rem', md: '2.5rem' } }}>
                My Services
              </Typography>
              <Typography sx={{ color: 'rgba(255,255,255,0.6)', fontSize: '1.1rem' }}>
                Manage your offerings and set your own pricing
              </Typography>
            </Box>
            <Button
              variant="contained"
              size="large"
              onClick={handleOpenCreate}
              sx={{
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                px: 4,
                py: 1.5,
                fontSize: '1rem',
                fontWeight: 700,
                borderRadius: '14px',
                boxShadow: '0 4px 20px rgba(102,126,234,0.4)',
                '&:hover': {
                  boxShadow: '0 6px 30px rgba(102,126,234,0.6)',
                },
              }}
            >
              + Add New Service
            </Button>
          </Box>

          {/* Stats */}
          <Box sx={{ display: 'flex', gap: 4, mt: 4, flexWrap: 'wrap' }}>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 800, color: '#fff' }}>
                {activeOfferings.length}
              </Typography>
              <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.5)' }}>
                Active Services
              </Typography>
            </Box>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 800, color: '#fff' }}>
                {offerings.length}
              </Typography>
              <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.5)' }}>
                Total Services
              </Typography>
            </Box>
            {activeOfferings.length > 0 && (
              <Box>
                <Typography variant="h4" sx={{ fontWeight: 800, color: '#fff' }}>
                  ${Math.min(...activeOfferings.map((o: any) => parseFloat(o.price)))} - ${Math.max(...activeOfferings.map((o: any) => parseFloat(o.price)))}
                </Typography>
                <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.5)' }}>
                  Price Range
                </Typography>
              </Box>
            )}
          </Box>
        </Container>
      </Box>

      <Container maxWidth="lg">
        <Box sx={{ py: 5 }}>
          {/* My Categories */}
          <Card sx={{ mb: 4, overflow: 'hidden' }}>
            <Box sx={{ height: 4, background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }} />
            <CardContent sx={{ p: 3 }}>
              <Typography variant="h6" sx={{ fontWeight: 700, mb: 0.5 }}>My Categories</Typography>
              <Typography variant="body2" sx={{ color: '#64648b', mb: 2 }}>
                Select the categories that describe you. Fans can filter artists by category.
              </Typography>
              <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mb: 2 }}>
                {ALL_CATEGORIES.map(cat => (
                  <Chip
                    key={cat}
                    label={`${CATEGORY_ICONS[cat]} ${cat.charAt(0) + cat.slice(1).toLowerCase()}`}
                    onClick={() => handleCategoryToggle(cat)}
                    variant={selectedCategories.includes(cat) ? 'filled' : 'outlined'}
                    sx={{
                      fontWeight: 600,
                      fontSize: '0.82rem',
                      borderRadius: '10px',
                      ...(selectedCategories.includes(cat)
                        ? { background: 'linear-gradient(135deg, #667eea, #764ba2)', color: '#fff' }
                        : { borderColor: 'rgba(102,126,234,0.3)', color: '#667eea' }),
                    }}
                  />
                ))}
              </Box>
              <Button variant="contained" size="small" onClick={handleSaveCategories} disabled={categorySaving}
                sx={{ borderRadius: '10px', px: 3, background: 'linear-gradient(135deg, #667eea, #764ba2)' }}>
                {categorySaving ? 'Saving...' : 'Save Categories'}
              </Button>
            </CardContent>
          </Card>

          {offerings.length === 0 ? (
            <Box
              sx={{
                textAlign: 'center',
                py: 10,
                bgcolor: '#fff',
                borderRadius: '20px',
                border: '2px dashed rgba(102,126,234,0.2)',
              }}
            >
              <Typography sx={{ fontSize: '3rem', mb: 2 }}>🎨</Typography>
              <Typography variant="h5" sx={{ fontWeight: 700, mb: 1 }}>
                No services yet
              </Typography>
              <Typography sx={{ color: '#64648b', mb: 3, maxWidth: 400, mx: 'auto' }}>
                Start by adding your services — set your own prices for calls, video sessions, chats, and more.
              </Typography>
              <Button variant="contained" size="large" onClick={handleOpenCreate}>
                Create Your First Service
              </Button>
            </Box>
          ) : (
            <>
              {/* Active Services */}
              {activeOfferings.length > 0 && (
                <Box sx={{ mb: 5 }}>
                  <Typography variant="h5" sx={{ fontWeight: 800, mb: 3 }}>
                    Active Services
                  </Typography>
                  <Grid container spacing={3}>
                    {activeOfferings.map((offering: any) => (
                      <Grid item xs={12} sm={6} md={4} key={offering.id}>
                        <Card
                          sx={{
                            height: '100%',
                            display: 'flex',
                            flexDirection: 'column',
                            overflow: 'hidden',
                            transition: 'transform 0.2s, box-shadow 0.2s',
                            '&:hover': {
                              transform: 'translateY(-4px)',
                              boxShadow: '0 12px 40px rgba(0,0,0,0.12)',
                            },
                          }}
                        >
                          {/* Type gradient bar */}
                          <Box sx={{ height: 6, background: typeGradients[offering.type] || typeGradients.CALL }} />
                          <CardContent sx={{ flexGrow: 1, p: 3 }}>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', mb: 2 }}>
                              <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                                <Chip
                                  label={SERVICE_TYPES.find((s) => s.value === offering.type)?.label || offering.type}
                                  size="small"
                                  sx={{
                                    fontWeight: 600,
                                    fontSize: '0.7rem',
                                    bgcolor: 'rgba(102,126,234,0.08)',
                                    color: '#667eea',
                                  }}
                                />
                                <Chip
                                  label={`${offering.durationMinutes} min`}
                                  size="small"
                                  variant="outlined"
                                  sx={{ fontWeight: 500, fontSize: '0.7rem' }}
                                />
                              </Box>
                              <Chip
                                label="Active"
                                size="small"
                                sx={{
                                  bgcolor: 'rgba(56,178,172,0.1)',
                                  color: '#38b2ac',
                                  fontWeight: 600,
                                  fontSize: '0.65rem',
                                }}
                              />
                            </Box>

                            <Typography variant="h6" sx={{ fontWeight: 700, mb: 1 }}>
                              {offering.title}
                            </Typography>
                            {offering.description && (
                              <Typography
                                variant="body2"
                                sx={{ color: '#64648b', mb: 2, lineHeight: 1.7, display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}
                              >
                                {offering.description}
                              </Typography>
                            )}

                            <Typography
                              variant="h5"
                              sx={{
                                fontWeight: 800,
                                mt: 'auto',
                                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                                WebkitBackgroundClip: 'text',
                                WebkitTextFillColor: 'transparent',
                              }}
                            >
                              ${offering.price}
                              <Typography
                                component="span"
                                sx={{ fontSize: '0.85rem', color: '#64648b', WebkitTextFillColor: '#64648b', ml: 0.5 }}
                              >
                                {offering.currency || 'USD'}
                              </Typography>
                            </Typography>
                          </CardContent>

                          <Box sx={{ px: 3, pb: 2.5, display: 'flex', gap: 1 }}>
                            <Button
                              size="small"
                              variant="outlined"
                              onClick={() => handleOpenEdit(offering)}
                              sx={{ borderRadius: '10px', flex: 1 }}
                            >
                              Edit
                            </Button>
                            <Button
                              size="small"
                              color="warning"
                              onClick={() => handleToggleActive(offering)}
                              sx={{ borderRadius: '10px' }}
                            >
                              Deactivate
                            </Button>
                            <Button
                              size="small"
                              color="error"
                              onClick={() => setDeleteConfirmId(offering.id)}
                              sx={{ borderRadius: '10px', minWidth: 'auto', px: 1.5 }}
                            >
                              ✕
                            </Button>
                          </Box>
                        </Card>
                      </Grid>
                    ))}
                  </Grid>
                </Box>
              )}

              {/* Inactive Services */}
              {inactiveOfferings.length > 0 && (
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 2, color: '#64648b' }}>
                    Inactive Services ({inactiveOfferings.length})
                  </Typography>
                  <Grid container spacing={2}>
                    {inactiveOfferings.map((offering: any) => (
                      <Grid item xs={12} sm={6} md={4} key={offering.id}>
                        <Card sx={{ opacity: 0.65, overflow: 'hidden' }}>
                          <Box sx={{ height: 4, background: '#ccc' }} />
                          <CardContent sx={{ p: 2.5 }}>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
                              <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                                {offering.title}
                              </Typography>
                              <Chip label="Inactive" size="small" sx={{ bgcolor: 'rgba(0,0,0,0.06)', fontSize: '0.65rem' }} />
                            </Box>
                            <Typography variant="body2" sx={{ color: '#64648b', mb: 1 }}>
                              ${offering.price} · {offering.durationMinutes} min
                            </Typography>
                            <Box sx={{ display: 'flex', gap: 1 }}>
                              <Button size="small" variant="outlined" onClick={() => handleToggleActive(offering)} sx={{ borderRadius: '10px' }}>
                                Reactivate
                              </Button>
                              <Button size="small" onClick={() => handleOpenEdit(offering)} sx={{ borderRadius: '10px' }}>
                                Edit
                              </Button>
                            </Box>
                          </CardContent>
                        </Card>
                      </Grid>
                    ))}
                  </Grid>
                </Box>
              )}
            </>
          )}
        </Box>
      </Container>

      {/* Create / Edit Dialog */}
      <Dialog
        open={dialogOpen}
        onClose={() => setDialogOpen(false)}
        maxWidth="sm"
        fullWidth
        PaperProps={{ sx: { borderRadius: '20px', overflow: 'hidden' } }}
      >
        <Box sx={{ height: 6, background: form.type ? (typeGradients[form.type] || typeGradients.CALL) : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }} />
        <DialogTitle sx={{ fontWeight: 700, fontSize: '1.4rem', pt: 3 }}>
          {editingId ? 'Edit Service' : 'Add New Service'}
        </DialogTitle>
        <DialogContent sx={{ px: 3, pb: 1 }}>
          <Typography variant="body2" sx={{ color: '#64648b', mb: 3 }}>
            {editingId
              ? 'Update your service details and pricing.'
              : 'Set up a new service with your custom pricing. Fans will see this on your profile.'}
          </Typography>

          {/* Service Type Selection */}
          <Typography variant="subtitle2" sx={{ fontWeight: 700, mb: 1.5 }}>
            Service Type *
          </Typography>
          <Grid container spacing={1.5} sx={{ mb: 3 }}>
            {SERVICE_TYPES.map((stype) => (
              <Grid item xs={6} sm={3} key={stype.value}>
                <Box
                  onClick={() => {
                    setForm({ ...form, type: stype.value });
                    setErrors({ ...errors, type: '' });
                  }}
                  sx={{
                    p: 2,
                    borderRadius: '14px',
                    cursor: 'pointer',
                    border: form.type === stype.value
                      ? '2px solid #667eea'
                      : '2px solid rgba(0,0,0,0.06)',
                    bgcolor: form.type === stype.value ? 'rgba(102,126,234,0.04)' : 'transparent',
                    transition: 'all 0.2s',
                    '&:hover': {
                      borderColor: 'rgba(102,126,234,0.3)',
                      bgcolor: 'rgba(102,126,234,0.02)',
                    },
                  }}
                >
                  <Typography sx={{ fontSize: '1.5rem', mb: 0.5 }}>{stype.icon}</Typography>
                  <Typography variant="subtitle2" sx={{ fontWeight: 700, fontSize: '0.85rem' }}>
                    {stype.label}
                  </Typography>
                  <Typography variant="caption" sx={{ color: '#64648b' }}>
                    {stype.description}
                  </Typography>
                </Box>
              </Grid>
            ))}
          </Grid>
          {errors.type && (
            <Typography variant="caption" color="error" sx={{ display: 'block', mt: -2, mb: 2 }}>
              {errors.type}
            </Typography>
          )}

          <TextField
            label="Service Title"
            fullWidth
            required
            value={form.title}
            onChange={(e) => {
              setForm({ ...form, title: e.target.value });
              setErrors({ ...errors, title: '' });
            }}
            error={!!errors.title}
            helperText={errors.title || 'E.g., "30-Minute Vocal Coaching Call"'}
            sx={{ mb: 2.5 }}
          />

          <TextField
            label="Description"
            fullWidth
            multiline
            rows={3}
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            helperText="Describe what the fan will get from this service"
            sx={{ mb: 2.5 }}
          />

          <Grid container spacing={2} sx={{ mb: 2.5 }}>
            <Grid item xs={6}>
              <TextField
                label="Price"
                fullWidth
                required
                type="number"
                value={form.price}
                onChange={(e) => {
                  setForm({ ...form, price: e.target.value });
                  setErrors({ ...errors, price: '' });
                }}
                error={!!errors.price}
                helperText={errors.price}
                InputProps={{
                  startAdornment: <InputAdornment position="start">$</InputAdornment>,
                }}
                inputProps={{ min: 1, step: '0.01' }}
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                label="Duration (minutes)"
                fullWidth
                required
                type="number"
                value={form.durationMinutes}
                onChange={(e) => {
                  setForm({ ...form, durationMinutes: e.target.value });
                  setErrors({ ...errors, durationMinutes: '' });
                }}
                error={!!errors.durationMinutes}
                helperText={errors.durationMinutes}
                inputProps={{ min: 5, step: 5 }}
              />
            </Grid>
          </Grid>

          <TextField
            label="Currency"
            fullWidth
            select
            value={form.currency}
            onChange={(e) => setForm({ ...form, currency: e.target.value })}
            sx={{ mb: 1 }}
          >
            <MenuItem value="USD">USD ($)</MenuItem>
            <MenuItem value="EUR">EUR (€)</MenuItem>
            <MenuItem value="GBP">GBP (£)</MenuItem>
            <MenuItem value="INR">INR (₹)</MenuItem>
            <MenuItem value="CAD">CAD (C$)</MenuItem>
            <MenuItem value="AUD">AUD (A$)</MenuItem>
          </TextField>
        </DialogContent>

        <DialogActions sx={{ px: 3, pb: 3, pt: 2 }}>
          <Button onClick={() => setDialogOpen(false)} sx={{ borderRadius: '10px', px: 3 }}>
            Cancel
          </Button>
          <Button
            variant="contained"
            onClick={handleSubmit}
            disabled={submitting}
            sx={{ borderRadius: '10px', px: 4 }}
          >
            {submitting ? 'Saving...' : editingId ? 'Update Service' : 'Create Service'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={!!deleteConfirmId}
        onClose={() => setDeleteConfirmId(null)}
        PaperProps={{ sx: { borderRadius: '16px' } }}
      >
        <DialogTitle sx={{ fontWeight: 700 }}>Remove Service?</DialogTitle>
        <DialogContent>
          <Typography variant="body2" sx={{ color: '#64648b' }}>
            This will deactivate the service. Existing bookings won't be affected.
          </Typography>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2.5 }}>
          <Button onClick={() => setDeleteConfirmId(null)} sx={{ borderRadius: '10px' }}>
            Keep
          </Button>
          <Button
            variant="contained"
            color="error"
            onClick={() => deleteConfirmId && handleDelete(deleteConfirmId)}
            sx={{ borderRadius: '10px' }}
          >
            Remove
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

