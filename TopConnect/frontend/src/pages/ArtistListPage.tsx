import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Box, Typography, CircularProgress, Grid, Card, CardContent, Button, TextField, MenuItem, Select, FormControl, InputLabel, Alert, Avatar, Chip } from '@mui/material';
import { artistAPI } from '../services/apiClient';

const CATEGORY_ICONS: Record<string, string> = {
  ACTOR: '🎭', DANCER: '💃', CRICKETER: '🏏', PAINTER: '🎨',
  YOUTUBER: '📺', INFLUENCER: '⭐', CREATOR: '✨', SINGER: '🎤',
};

export default function ArtistListPage() {
  const navigate = useNavigate();
  const [artists, setArtists] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [page, setPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState('');
  const [filterType, setFilterType] = useState('all');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [categories, setCategories] = useState<string[]>([]);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => { fetchCategories(); }, []);
  useEffect(() => { fetchArtists(); }, [page, searchQuery, filterType, selectedCategory]);

  const fetchCategories = async () => {
    try {
      const res = await artistAPI.getCategories();
      setCategories(res.data.data || []);
    } catch { /* ignore */ }
  };

  const fetchArtists = async () => {
    setLoading(true);
    setError('');
    try {
      let response;
      if (searchQuery.trim()) {
        response = await artistAPI.search(searchQuery, page, 12);
      } else if (filterType === 'featured') {
        response = await artistAPI.getFeatured(page, 12);
      } else {
        response = await artistAPI.list(page, 12, selectedCategory || undefined);
      }
      setArtists(response.data.data.content || []);
      setTotalPages(response.data.data.totalPages || 0);
    } catch (err: any) {
      setError('Failed to load artists. Please try again.');
      setArtists([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(e.target.value);
    setPage(0);
  };

  const handleFilterChange = (e: any) => {
    setFilterType(e.target.value);
    setSelectedCategory('');
    setPage(0);
  };

  const handleCategoryClick = (cat: string) => {
    setSelectedCategory(prev => prev === cat ? '' : cat);
    setSearchQuery('');
    setFilterType('all');
    setPage(0);
  };

  return (
    <Box>
      {/* Page Header */}
      <Box
        sx={{
          background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)',
          color: '#fff',
          py: { xs: 5, md: 7 },
          position: 'relative',
          overflow: 'hidden',
          borderRadius: 0,
          '&::after': {
            content: '""',
            position: 'absolute',
            top: '-40%',
            right: '-15%',
            width: '400px',
            height: '400px',
            borderRadius: '50%',
            background: 'radial-gradient(circle, rgba(240,147,251,0.12) 0%, transparent 70%)',
          },
        }}
      >
        <Container maxWidth="lg" sx={{ position: 'relative', zIndex: 1 }}>
          <Typography variant="h3" sx={{ fontWeight: 800, mb: 1.5, fontSize: { xs: '1.8rem', md: '2.5rem' } }}>
            Discover Artists
          </Typography>
          <Typography sx={{ color: 'rgba(255,255,255,0.6)', fontSize: '1.1rem', maxWidth: 500 }}>
            Find your next creative connection. Browse talented artists and book a session today.
          </Typography>
        </Container>
      </Box>

      <Container maxWidth="lg">
        <Box sx={{ py: 5 }}>
          {/* Search & Filter */}
          <Box sx={{ display: 'flex', gap: 2, mb: 3, flexWrap: 'wrap', p: 3, borderRadius: '16px', bgcolor: '#fff', boxShadow: '0 4px 20px rgba(0,0,0,0.04)', border: '1px solid rgba(0,0,0,0.04)' }}>
            <TextField label="Search artists..." variant="outlined" value={searchQuery} onChange={handleSearch} placeholder="Search by name" sx={{ flex: 1, minWidth: 250 }} size="medium" />
            <FormControl sx={{ minWidth: 180 }}>
              <InputLabel>Filter</InputLabel>
              <Select value={filterType} onChange={handleFilterChange} label="Filter">
                <MenuItem value="all">All Artists</MenuItem>
                <MenuItem value="featured">Featured Only</MenuItem>
              </Select>
            </FormControl>
          </Box>

          {/* Category Filter Chips */}
          {categories.length > 0 && (
            <Box sx={{ display: 'flex', gap: 1, mb: 4, flexWrap: 'wrap' }}>
              {categories.map(cat => (
                <Chip
                  key={cat}
                  label={`${CATEGORY_ICONS[cat] || '🎯'} ${cat.charAt(0) + cat.slice(1).toLowerCase()}`}
                  onClick={() => handleCategoryClick(cat)}
                  variant={selectedCategory === cat ? 'filled' : 'outlined'}
                  sx={{
                    fontWeight: 600,
                    fontSize: '0.8rem',
                    px: 1,
                    borderRadius: '10px',
                    ...(selectedCategory === cat
                      ? { background: 'linear-gradient(135deg, #667eea, #764ba2)', color: '#fff', borderColor: 'transparent' }
                      : { borderColor: 'rgba(102,126,234,0.3)', color: '#667eea', '&:hover': { background: 'rgba(102,126,234,0.06)' } }),
                  }}
                />
              ))}
              {selectedCategory && (
                <Chip label="✕ Clear" size="small" onClick={() => { setSelectedCategory(''); setPage(0); }}
                  sx={{ fontWeight: 600, color: '#f5576c', borderColor: '#f5576c' }} variant="outlined" />
              )}
            </Box>
          )}

          {/* Default category suggestions when none are in DB yet */}
          {categories.length === 0 && !loading && (
            <Box sx={{ display: 'flex', gap: 1, mb: 4, flexWrap: 'wrap' }}>
              {Object.entries(CATEGORY_ICONS).map(([cat, icon]) => (
                <Chip key={cat} label={`${icon} ${cat.charAt(0) + cat.slice(1).toLowerCase()}`}
                  variant="outlined" sx={{ fontWeight: 500, fontSize: '0.8rem', borderRadius: '10px', borderColor: 'rgba(0,0,0,0.1)', color: '#64648b' }} />
              ))}
            </Box>
          )}

          {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}

          {loading && (
            <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
              <CircularProgress sx={{ color: '#667eea' }} />
            </Box>
          )}

          {!loading && artists.length === 0 && !error && (
            <Box sx={{ textAlign: 'center', py: 10 }}>
              <Typography variant="h6" sx={{ color: '#64648b', fontWeight: 500 }}>
                {selectedCategory ? `No ${selectedCategory.toLowerCase()} artists found yet.` : 'No artists found. Try adjusting your search.'}
              </Typography>
            </Box>
          )}

          {!loading && artists.length > 0 && (
            <>
              <Grid container spacing={3} sx={{ mb: 4 }}>
                {artists.map((artist: any) => (
                  <Grid item xs={12} sm={6} md={4} key={artist.id}>
                    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', cursor: 'pointer', overflow: 'hidden' }}
                      onClick={() => navigate(`/artists/${artist.id}`)}>
                      <Box sx={{ height: 200, background: artist.avatarUrl ? `url(${artist.avatarUrl})` : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', backgroundSize: 'cover', backgroundPosition: 'center', display: 'flex', alignItems: 'flex-end', p: 2, position: 'relative' }}>
                        {!artist.avatarUrl && (
                          <Avatar sx={{ width: 64, height: 64, fontSize: '1.8rem', fontWeight: 800, bgcolor: 'rgba(255,255,255,0.2)', backdropFilter: 'blur(10px)', border: '2px solid rgba(255,255,255,0.3)' }}>
                            {(artist.displayName || 'A').charAt(0)}
                          </Avatar>
                        )}
                        {artist.featured && (
                          <Chip label="Featured" size="small" sx={{ position: 'absolute', top: 12, right: 12, background: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)', color: '#1a1a2e', fontWeight: 700, fontSize: '0.7rem' }} />
                        )}
                      </Box>
                      <CardContent sx={{ flexGrow: 1, p: 3 }}>
                        <Typography variant="h6" sx={{ fontWeight: 700, mb: 0.5 }}>{artist.displayName}</Typography>
                        {/* Category chips */}
                        {artist.categories && artist.categories.length > 0 && (
                          <Box sx={{ display: 'flex', gap: 0.5, flexWrap: 'wrap', mb: 1.5 }}>
                            {artist.categories.slice(0, 3).map((cat: string) => (
                              <Chip key={cat} label={`${CATEGORY_ICONS[cat] || ''} ${cat.charAt(0) + cat.slice(1).toLowerCase()}`}
                                size="small" sx={{ fontSize: '0.65rem', fontWeight: 600, bgcolor: 'rgba(102,126,234,0.06)', color: '#667eea', height: 22 }} />
                            ))}
                          </Box>
                        )}
                        {artist.bio && (
                          <Typography variant="body2" sx={{ color: '#64648b', mb: 2, lineHeight: 1.6 }}>
                            {artist.bio.length > 90 ? artist.bio.substring(0, 90) + '...' : artist.bio}
                          </Typography>
                        )}
                        {artist.ratingCount > 0 && (
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                            <Typography sx={{ color: '#f6d365', fontSize: '0.9rem' }}>★</Typography>
                            <Typography variant="body2" sx={{ fontWeight: 600 }}>{(artist.ratingAggregate || 0).toFixed(1)}</Typography>
                            <Typography variant="caption" sx={{ color: '#64648b' }}>({artist.ratingCount})</Typography>
                          </Box>
                        )}
                      </CardContent>
                      <Box sx={{ px: 3, pb: 3 }}>
                        <Button variant="contained" fullWidth size="medium">View Profile</Button>
                      </Box>
                    </Card>
                  </Grid>
                ))}
              </Grid>

              {/* Pagination */}
              <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 4, alignItems: 'center' }}>
                <Button disabled={page === 0} onClick={() => setPage(page - 1)} variant="outlined" sx={{ borderRadius: '10px' }}>← Previous</Button>
                <Chip label={`${page + 1} / ${totalPages || 1}`} sx={{ fontWeight: 600, bgcolor: 'rgba(102,126,234,0.1)', color: '#667eea' }} />
                <Button disabled={page >= totalPages - 1} onClick={() => setPage(page + 1)} variant="outlined" sx={{ borderRadius: '10px' }}>Next →</Button>
              </Box>
            </>
          )}
        </Box>
      </Container>
    </Box>
  );
}
