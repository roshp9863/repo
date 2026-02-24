import React, { useState, useEffect } from 'react';
import {
  Container, Box, Typography, Button, Card, CardContent, Grid, Chip,
  CircularProgress, Dialog, DialogTitle, DialogContent, DialogActions,
  TextField, MenuItem, Tabs, Tab,
} from '@mui/material';
import { portfolioAPI } from '../services/apiClient';
import { useAuth } from '../context/AuthContext';
import { useSnackbar } from 'notistack';

const ARTIST_CATEGORIES = [
  { value: 'WORK_SAMPLE', label: 'Work Sample', icon: '🎨', description: 'Showcase your work' },
  { value: 'ACHIEVEMENT', label: 'Achievement', icon: '🏆', description: 'Awards & milestones' },
  { value: 'CERTIFICATION', label: 'Certification', icon: '📜', description: 'Certifications & training' },
  { value: 'EXPERIENCE', label: 'Experience', icon: '💼', description: 'Professional experience' },
  { value: 'SKILL', label: 'Skill', icon: '⚡', description: 'Your specialties' },
];

const FAN_CATEGORIES = [
  { value: 'GOAL', label: 'Goal', icon: '🎯', description: 'What you want to achieve' },
  { value: 'JOURNEY', label: 'Journey', icon: '🚀', description: 'Your learning progress' },
  { value: 'SKILL', label: 'Skill Level', icon: '📊', description: 'Current abilities' },
  { value: 'WORK_SAMPLE', label: 'My Work', icon: '🎨', description: 'Work to get feedback on' },
  { value: 'ACHIEVEMENT', label: 'Achievement', icon: '🏆', description: 'What you\'ve accomplished' },
];

const categoryGradients: Record<string, string> = {
  WORK_SAMPLE: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
  ACHIEVEMENT: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)',
  CERTIFICATION: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
  EXPERIENCE: 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)',
  SKILL: 'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)',
  GOAL: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
  JOURNEY: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
};

interface PortfolioForm {
  title: string;
  description: string;
  category: string;
  mediaUrl: string;
  mediaType: string;
  tags: string;
  visibility: string;
}

const emptyForm: PortfolioForm = {
  title: '', description: '', category: '', mediaUrl: '',
  mediaType: '', tags: '', visibility: 'PUBLIC',
};

export default function MyPortfolioPage() {
  const { user } = useAuth();
  const { enqueueSnackbar } = useSnackbar();
  const [items, setItems] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [form, setForm] = useState<PortfolioForm>(emptyForm);
  const [submitting, setSubmitting] = useState(false);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [activeTab, setActiveTab] = useState(0);
  const [deleteConfirmId, setDeleteConfirmId] = useState<string | null>(null);

  const isArtist = user?.role === 'ARTIST';
  const categories = isArtist ? ARTIST_CATEGORIES : FAN_CATEGORIES;

  useEffect(() => { fetchPortfolio(); }, []);

  const fetchPortfolio = async () => {
    setLoading(true);
    try {
      const res = await portfolioAPI.getMine();
      setItems(res.data.data || []);
    } catch { enqueueSnackbar('Failed to load portfolio', { variant: 'error' }); }
    finally { setLoading(false); }
  };

  const validate = (): boolean => {
    const e: Record<string, string> = {};
    if (!form.title.trim()) e.title = 'Title is required';
    if (!form.category) e.category = 'Select a category';
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) return;
    setSubmitting(true);
    try {
      if (editingId) {
        await portfolioAPI.update(editingId, form);
        enqueueSnackbar('Portfolio item updated!', { variant: 'success' });
      } else {
        await portfolioAPI.create(form);
        enqueueSnackbar('Portfolio item added!', { variant: 'success' });
      }
      setDialogOpen(false);
      fetchPortfolio();
    } catch (err: any) {
      enqueueSnackbar(err.response?.data?.message || 'Failed to save', { variant: 'error' });
    } finally { setSubmitting(false); }
  };

  const handleEdit = (item: any) => {
    setEditingId(item.id);
    setForm({
      title: item.title, description: item.description || '',
      category: item.category, mediaUrl: item.mediaUrl || '',
      mediaType: item.mediaType || '', tags: item.tags || '',
      visibility: item.visibility || 'PUBLIC',
    });
    setErrors({});
    setDialogOpen(true);
  };

  const handleDelete = async (id: string) => {
    try {
      await portfolioAPI.delete(id);
      enqueueSnackbar('Item removed', { variant: 'success' });
      setDeleteConfirmId(null);
      fetchPortfolio();
    } catch { enqueueSnackbar('Failed to remove', { variant: 'error' }); }
  };

  const allCategories = ['ALL', ...categories.map(c => c.value)];
  const filtered = activeTab === 0 ? items : items.filter(i => i.category === allCategories[activeTab]);

  if (loading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', py: 12 }}><CircularProgress sx={{ color: '#667eea' }} /></Box>;
  }

  return (
    <Box>
      {/* Header */}
      <Box sx={{ background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)', color: '#fff', py: { xs: 5, md: 7 }, borderRadius: 0 }}>
        <Container maxWidth="lg">
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 2 }}>
            <Box>
              <Typography variant="h3" sx={{ fontWeight: 800, mb: 1, fontSize: { xs: '1.8rem', md: '2.5rem' } }}>
                {isArtist ? '🎨 My Portfolio' : '🚀 My Journey'}
              </Typography>
              <Typography sx={{ color: 'rgba(255,255,255,0.6)', fontSize: '1.1rem' }}>
                {isArtist
                  ? 'Showcase your work, skills, and achievements to attract fans'
                  : 'Track your goals, progress, and share your work for feedback'}
              </Typography>
            </Box>
            <Button
              variant="contained" size="large"
              onClick={() => { setEditingId(null); setForm(emptyForm); setErrors({}); setDialogOpen(true); }}
              sx={{
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                px: 4, py: 1.5, fontWeight: 700, borderRadius: '14px',
                boxShadow: '0 4px 20px rgba(102,126,234,0.4)',
                '&:hover': { boxShadow: '0 6px 30px rgba(102,126,234,0.6)' },
              }}
            >
              + Add Item
            </Button>
          </Box>

          {/* Stats */}
          <Box sx={{ display: 'flex', gap: 4, mt: 4, flexWrap: 'wrap' }}>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 800 }}>{items.length}</Typography>
              <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.5)' }}>Total Items</Typography>
            </Box>
            {categories.slice(0, 3).map(cat => {
              const count = items.filter(i => i.category === cat.value).length;
              return count > 0 ? (
                <Box key={cat.value}>
                  <Typography variant="h4" sx={{ fontWeight: 800 }}>{count}</Typography>
                  <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.5)' }}>{cat.label}</Typography>
                </Box>
              ) : null;
            })}
          </Box>
        </Container>
      </Box>

      <Container maxWidth="lg">
        <Box sx={{ py: 4 }}>
          {/* Category Tabs */}
          <Tabs
            value={activeTab}
            onChange={(_, v) => setActiveTab(v)}
            variant="scrollable" scrollButtons="auto"
            sx={{
              mb: 4,
              '& .MuiTab-root': { fontWeight: 600, textTransform: 'none', borderRadius: '10px', mr: 1, minHeight: 40 },
              '& .Mui-selected': { color: '#667eea' },
              '& .MuiTabs-indicator': { background: '#667eea', borderRadius: 2 },
            }}
          >
            <Tab label="All" />
            {categories.map(c => <Tab key={c.value} label={`${c.icon} ${c.label}`} />)}
          </Tabs>

          {filtered.length === 0 ? (
            <Box sx={{ textAlign: 'center', py: 10, bgcolor: '#fff', borderRadius: '20px', border: '2px dashed rgba(102,126,234,0.2)' }}>
              <Typography sx={{ fontSize: '3rem', mb: 2 }}>{isArtist ? '🎨' : '🚀'}</Typography>
              <Typography variant="h5" sx={{ fontWeight: 700, mb: 1 }}>
                {items.length === 0 ? 'Start building your portfolio' : 'No items in this category'}
              </Typography>
              <Typography sx={{ color: '#64648b', mb: 3, maxWidth: 400, mx: 'auto' }}>
                {isArtist
                  ? 'Add work samples, achievements, and certifications to attract fans.'
                  : 'Share your goals and journey so artists can tailor their advice to you.'}
              </Typography>
              {items.length === 0 && (
                <Button variant="contained" onClick={() => { setEditingId(null); setForm(emptyForm); setErrors({}); setDialogOpen(true); }}>
                  Add Your First Item
                </Button>
              )}
            </Box>
          ) : (
            <Grid container spacing={3}>
              {filtered.map((item: any) => (
                <Grid item xs={12} sm={6} md={4} key={item.id}>
                  <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
                    <Box sx={{ height: 6, background: categoryGradients[item.category] || categoryGradients.WORK_SAMPLE }} />

                    {/* Media preview */}
                    {item.mediaUrl && item.mediaType === 'IMAGE' && (
                      <Box sx={{ height: 160, backgroundImage: `url(${item.mediaUrl})`, backgroundSize: 'cover', backgroundPosition: 'center' }} />
                    )}

                    <CardContent sx={{ flexGrow: 1, p: 3 }}>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', mb: 1.5 }}>
                        <Chip
                          label={categories.find(c => c.value === item.category)?.label || item.category}
                          size="small"
                          sx={{ fontWeight: 600, fontSize: '0.7rem', bgcolor: 'rgba(102,126,234,0.08)', color: '#667eea' }}
                        />
                        <Chip
                          label={item.visibility === 'PUBLIC' ? '🌐 Public' : '🔒 Private'}
                          size="small"
                          variant="outlined"
                          sx={{ fontSize: '0.65rem' }}
                        />
                      </Box>
                      <Typography variant="h6" sx={{ fontWeight: 700, mb: 1 }}>{item.title}</Typography>
                      {item.description && (
                        <Typography variant="body2" sx={{ color: '#64648b', mb: 2, lineHeight: 1.7, display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                          {item.description}
                        </Typography>
                      )}
                      {item.tags && (
                        <Box sx={{ display: 'flex', gap: 0.5, flexWrap: 'wrap', mt: 'auto' }}>
                          {item.tags.split(',').map((tag: string, i: number) => (
                            <Chip key={i} label={tag.trim()} size="small" variant="outlined" sx={{ fontSize: '0.65rem' }} />
                          ))}
                        </Box>
                      )}
                      {item.mediaUrl && item.mediaType === 'LINK' && (
                        <Typography variant="caption" sx={{ color: '#667eea', display: 'block', mt: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                          🔗 {item.mediaUrl}
                        </Typography>
                      )}
                    </CardContent>

                    <Box sx={{ px: 3, pb: 2.5, display: 'flex', gap: 1 }}>
                      <Button size="small" variant="outlined" onClick={() => handleEdit(item)} sx={{ borderRadius: '10px', flex: 1 }}>
                        Edit
                      </Button>
                      <Button size="small" color="error" onClick={() => setDeleteConfirmId(item.id)} sx={{ borderRadius: '10px', minWidth: 'auto', px: 1.5 }}>
                        ✕
                      </Button>
                    </Box>
                  </Card>
                </Grid>
              ))}
            </Grid>
          )}
        </Box>
      </Container>

      {/* Create / Edit Dialog */}
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth PaperProps={{ sx: { borderRadius: '20px', overflow: 'hidden' } }}>
        <Box sx={{ height: 6, background: form.category ? (categoryGradients[form.category] || categoryGradients.WORK_SAMPLE) : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }} />
        <DialogTitle sx={{ fontWeight: 700, fontSize: '1.4rem', pt: 3 }}>
          {editingId ? 'Edit Portfolio Item' : 'Add to Portfolio'}
        </DialogTitle>
        <DialogContent sx={{ px: 3, pb: 1 }}>
          {/* Category Selection */}
          <Typography variant="subtitle2" sx={{ fontWeight: 700, mb: 1.5, mt: 1 }}>Category *</Typography>
          <Grid container spacing={1} sx={{ mb: 3 }}>
            {categories.map(cat => (
              <Grid item xs={6} sm={4} key={cat.value}>
                <Box
                  onClick={() => { setForm({ ...form, category: cat.value }); setErrors({ ...errors, category: '' }); }}
                  sx={{
                    p: 1.5, borderRadius: '12px', cursor: 'pointer', textAlign: 'center',
                    border: form.category === cat.value ? '2px solid #667eea' : '2px solid rgba(0,0,0,0.06)',
                    bgcolor: form.category === cat.value ? 'rgba(102,126,234,0.04)' : 'transparent',
                    transition: 'all 0.2s',
                    '&:hover': { borderColor: 'rgba(102,126,234,0.3)' },
                  }}
                >
                  <Typography sx={{ fontSize: '1.3rem' }}>{cat.icon}</Typography>
                  <Typography variant="caption" sx={{ fontWeight: 700, display: 'block' }}>{cat.label}</Typography>
                </Box>
              </Grid>
            ))}
          </Grid>
          {errors.category && <Typography variant="caption" color="error" sx={{ display: 'block', mt: -2, mb: 2 }}>{errors.category}</Typography>}

          <TextField label="Title *" fullWidth value={form.title} onChange={e => { setForm({ ...form, title: e.target.value }); setErrors({ ...errors, title: '' }); }}
            error={!!errors.title} helperText={errors.title} sx={{ mb: 2 }} />

          <TextField label="Description" fullWidth multiline rows={3} value={form.description}
            onChange={e => setForm({ ...form, description: e.target.value })}
            helperText={isArtist ? 'Describe your work or achievement' : 'Describe your goal or progress'} sx={{ mb: 2 }} />

          <Grid container spacing={2} sx={{ mb: 2 }}>
            <Grid item xs={8}>
              <TextField label="Media URL (optional)" fullWidth value={form.mediaUrl}
                onChange={e => setForm({ ...form, mediaUrl: e.target.value })}
                helperText="Image, video, or link URL"
              />
            </Grid>
            <Grid item xs={4}>
              <TextField label="Media Type" fullWidth select value={form.mediaType}
                onChange={e => setForm({ ...form, mediaType: e.target.value })}>
                <MenuItem value="">None</MenuItem>
                <MenuItem value="IMAGE">Image</MenuItem>
                <MenuItem value="VIDEO">Video</MenuItem>
                <MenuItem value="AUDIO">Audio</MenuItem>
                <MenuItem value="LINK">Link</MenuItem>
                <MenuItem value="DOCUMENT">Document</MenuItem>
              </TextField>
            </Grid>
          </Grid>

          <TextField label="Tags (comma-separated)" fullWidth value={form.tags}
            onChange={e => setForm({ ...form, tags: e.target.value })}
            helperText="e.g. vocals, jazz, beginner" sx={{ mb: 2 }} />

          <TextField label="Visibility" fullWidth select value={form.visibility}
            onChange={e => setForm({ ...form, visibility: e.target.value })}>
            <MenuItem value="PUBLIC">🌐 Public — visible to everyone</MenuItem>
            <MenuItem value="PRIVATE">🔒 Private — only you can see</MenuItem>
          </TextField>
        </DialogContent>

        <DialogActions sx={{ px: 3, pb: 3, pt: 2 }}>
          <Button onClick={() => setDialogOpen(false)} sx={{ borderRadius: '10px', px: 3 }}>Cancel</Button>
          <Button variant="contained" onClick={handleSubmit} disabled={submitting} sx={{ borderRadius: '10px', px: 4 }}>
            {submitting ? 'Saving...' : editingId ? 'Update' : 'Add to Portfolio'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Confirmation */}
      <Dialog open={!!deleteConfirmId} onClose={() => setDeleteConfirmId(null)} PaperProps={{ sx: { borderRadius: '16px' } }}>
        <DialogTitle sx={{ fontWeight: 700 }}>Remove Item?</DialogTitle>
        <DialogContent><Typography variant="body2" sx={{ color: '#64648b' }}>This will permanently remove this portfolio item.</Typography></DialogContent>
        <DialogActions sx={{ px: 3, pb: 2.5 }}>
          <Button onClick={() => setDeleteConfirmId(null)} sx={{ borderRadius: '10px' }}>Keep</Button>
          <Button variant="contained" color="error" onClick={() => deleteConfirmId && handleDelete(deleteConfirmId)} sx={{ borderRadius: '10px' }}>Remove</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

