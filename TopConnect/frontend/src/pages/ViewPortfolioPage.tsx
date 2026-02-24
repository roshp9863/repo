import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container, Box, Typography, Button, Card, CardContent, Grid, Chip,
  CircularProgress, Alert, Tabs, Tab,
} from '@mui/material';
import { portfolioAPI } from '../services/apiClient';

const categoryGradients: Record<string, string> = {
  WORK_SAMPLE: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
  ACHIEVEMENT: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)',
  CERTIFICATION: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
  EXPERIENCE: 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)',
  SKILL: 'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)',
  GOAL: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
  JOURNEY: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
};

const categoryLabels: Record<string, string> = {
  WORK_SAMPLE: '🎨 Work Sample', ACHIEVEMENT: '🏆 Achievement',
  CERTIFICATION: '📜 Certification', EXPERIENCE: '💼 Experience',
  SKILL: '⚡ Skill', GOAL: '🎯 Goal', JOURNEY: '🚀 Journey',
};

export default function ViewPortfolioPage() {
  const { userId } = useParams<{ userId: string }>();
  const navigate = useNavigate();
  const [items, setItems] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState(0);

  useEffect(() => {
    if (userId) fetchPortfolio();
  }, [userId]);

  const fetchPortfolio = async () => {
    setLoading(true);
    try {
      const res = await portfolioAPI.getPublic(userId!);
      setItems(res.data.data || []);
    } catch {
      setError('Could not load portfolio.');
    } finally {
      setLoading(false);
    }
  };

  const uniqueCategories = ['ALL', ...Array.from(new Set(items.map(i => i.category)))];
  const filtered = activeTab === 0 ? items : items.filter(i => i.category === uniqueCategories[activeTab]);

  const userRole = items.length > 0 ? items[0].userRole : null;
  const isArtistPortfolio = userRole === 'ARTIST';

  if (loading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', py: 12 }}><CircularProgress sx={{ color: '#667eea' }} /></Box>;
  }

  if (error) {
    return (
      <Container maxWidth="lg"><Box sx={{ py: 6 }}>
        <Alert severity="error">{error}</Alert>
        <Button onClick={() => navigate(-1)} sx={{ mt: 2 }}>← Go Back</Button>
      </Box></Container>
    );
  }

  return (
    <Box>
      <Box sx={{ background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)', color: '#fff', py: { xs: 5, md: 7 }, borderRadius: 0 }}>
        <Container maxWidth="lg">
          <Button onClick={() => navigate(-1)} sx={{ color: 'rgba(255,255,255,0.6)', mb: 2, '&:hover': { color: '#fff' } }}>
            ← Go Back
          </Button>
          <Typography variant="h3" sx={{ fontWeight: 800, mb: 1, fontSize: { xs: '1.8rem', md: '2.5rem' } }}>
            {isArtistPortfolio ? '🎨 Artist Portfolio' : '🚀 Fan Journey'}
          </Typography>
          <Typography sx={{ color: 'rgba(255,255,255,0.6)', fontSize: '1.1rem' }}>
            {isArtistPortfolio
              ? 'Work samples, achievements, and credentials'
              : 'Goals, progress, and learning journey'}
          </Typography>
          <Box sx={{ mt: 3 }}>
            <Typography variant="h4" sx={{ fontWeight: 800 }}>{items.length}</Typography>
            <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.5)' }}>Portfolio Items</Typography>
          </Box>
        </Container>
      </Box>

      <Container maxWidth="lg">
        <Box sx={{ py: 4 }}>
          {items.length === 0 ? (
            <Box sx={{ textAlign: 'center', py: 10, bgcolor: '#fff', borderRadius: '20px', border: '2px dashed rgba(102,126,234,0.2)' }}>
              <Typography sx={{ fontSize: '3rem', mb: 2 }}>📁</Typography>
              <Typography variant="h5" sx={{ fontWeight: 700, mb: 1 }}>No public portfolio items yet</Typography>
              <Typography sx={{ color: '#64648b' }}>This user hasn't added any public portfolio items.</Typography>
            </Box>
          ) : (
            <>
              {uniqueCategories.length > 2 && (
                <Tabs
                  value={activeTab} onChange={(_, v) => setActiveTab(v)}
                  variant="scrollable" scrollButtons="auto"
                  sx={{
                    mb: 4,
                    '& .MuiTab-root': { fontWeight: 600, textTransform: 'none', borderRadius: '10px', mr: 1 },
                    '& .Mui-selected': { color: '#667eea' },
                    '& .MuiTabs-indicator': { background: '#667eea', borderRadius: 2 },
                  }}
                >
                  <Tab label="All" />
                  {uniqueCategories.slice(1).map(c => (
                    <Tab key={c} label={categoryLabels[c] || c} />
                  ))}
                </Tabs>
              )}

              <Grid container spacing={3}>
                {filtered.map((item: any) => (
                  <Grid item xs={12} sm={6} md={4} key={item.id}>
                    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
                      <Box sx={{ height: 6, background: categoryGradients[item.category] || categoryGradients.WORK_SAMPLE }} />
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
                          <Typography variant="body2" sx={{ color: '#64648b', lineHeight: 1.7, display: '-webkit-box', WebkitLineClamp: 4, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
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
            </>
          )}
        </Box>
      </Container>
    </Box>
  );
}

