import React, { useState, useEffect } from 'react';
import {
  Container, Box, Typography, Card, CardContent, Grid, Chip, CircularProgress,
  Button, Tabs, Tab, Table, TableBody, TableCell, TableContainer, TableHead,
  TableRow, Paper, TextField, Dialog, DialogTitle, DialogContent, DialogActions,
  MenuItem, Select, FormControl, InputLabel, Alert, Avatar, IconButton, Tooltip,
} from '@mui/material';
import { adminAPI } from '../services/apiClient';
import { useSnackbar } from 'notistack';

const STATUS_COLORS: Record<string, string> = {
  ACTIVE: '#38b2ac', PENDING: '#fda085', SUSPENDED: '#f5576c', DELETED: '#64648b',
  CONFIRMED: '#38b2ac', COMPLETED: '#667eea', CANCELLED: '#f5576c',
  SUCCEEDED: '#38b2ac', FAILED: '#f5576c',
};

export default function AdminDashboardPage() {
  const { enqueueSnackbar } = useSnackbar();
  const [activeTab, setActiveTab] = useState(0);
  const [stats, setStats] = useState<any>(null);
  const [users, setUsers] = useState<any[]>([]);
  const [artists, setArtists] = useState<any[]>([]);
  const [bookings, setBookings] = useState<any[]>([]);
  const [payments, setPayments] = useState<any[]>([]);
  const [commissions, setCommissions] = useState<any[]>([]);
  const [pendingVerifications, setPendingVerifications] = useState<any[]>([]);
  const [rejectDialogOpen, setRejectDialogOpen] = useState(false);
  const [rejectArtistId, setRejectArtistId] = useState('');
  const [rejectReason, setRejectReason] = useState('');
  const [loading, setLoading] = useState(true);
  const [userFilter, setUserFilter] = useState('');
  const [bookingFilter, setBookingFilter] = useState('');
  const [commissionDialogOpen, setCommissionDialogOpen] = useState(false);
  const [selectedArtist, setSelectedArtist] = useState<any>(null);
  const [newRate, setNewRate] = useState('');

  useEffect(() => { fetchDashboard(); }, []);
  useEffect(() => {
    if (activeTab === 1) fetchUsers();
    if (activeTab === 2) fetchArtists();
    if (activeTab === 3) fetchBookings();
    if (activeTab === 4) fetchPayments();
    if (activeTab === 5) fetchCommissions();
    if (activeTab === 6) fetchPendingVerifications();
  }, [activeTab, userFilter, bookingFilter]);

  const fetchDashboard = async () => {
    setLoading(true);
    try {
      const res = await adminAPI.dashboard();
      setStats(res.data.data);
    } catch (err: any) {
      enqueueSnackbar(err.response?.data?.message || 'Failed to load dashboard', { variant: 'error' });
    } finally { setLoading(false); }
  };

  const fetchUsers = async () => {
    try {
      const res = await adminAPI.listUsers(0, 100, userFilter || undefined);
      setUsers(res.data.data.content || []);
    } catch { enqueueSnackbar('Failed to load users', { variant: 'error' }); }
  };

  const fetchArtists = async () => {
    try {
      const res = await adminAPI.listArtists(0, 100);
      setArtists(res.data.data.content || []);
    } catch { enqueueSnackbar('Failed to load artists', { variant: 'error' }); }
  };

  const fetchBookings = async () => {
    try {
      const res = await adminAPI.listBookings(0, 100, bookingFilter || undefined);
      setBookings(res.data.data.content || []);
    } catch { enqueueSnackbar('Failed to load bookings', { variant: 'error' }); }
  };

  const fetchPayments = async () => {
    try {
      const res = await adminAPI.listPayments(0, 100);
      setPayments(res.data.data.content || []);
    } catch { enqueueSnackbar('Failed to load payments', { variant: 'error' }); }
  };

  const fetchCommissions = async () => {
    try {
      const res = await adminAPI.listCommissions();
      setCommissions(res.data.data || []);
    } catch { enqueueSnackbar('Failed to load commissions', { variant: 'error' }); }
  };

  const fetchPendingVerifications = async () => {
    try {
      const res = await adminAPI.listPendingVerifications(0, 100);
      setPendingVerifications(res.data.data.content || []);
    } catch { enqueueSnackbar('Failed to load pending verifications', { variant: 'error' }); }
  };

  const handleVerifyArtist = async (artistId: string) => {
    try {
      await adminAPI.verifyArtist(artistId);
      enqueueSnackbar('Artist verified successfully! They are now visible to fans.', { variant: 'success' });
      fetchPendingVerifications();
      fetchDashboard();
    } catch { enqueueSnackbar('Failed to verify artist', { variant: 'error' }); }
  };

  const handleRejectArtist = async () => {
    if (!rejectReason.trim()) {
      enqueueSnackbar('Please provide a reason for rejection', { variant: 'warning' });
      return;
    }
    try {
      await adminAPI.rejectArtist(rejectArtistId, rejectReason);
      enqueueSnackbar('Artist rejected. They have been notified.', { variant: 'info' });
      setRejectDialogOpen(false);
      setRejectReason('');
      setRejectArtistId('');
      fetchPendingVerifications();
      fetchDashboard();
    } catch { enqueueSnackbar('Failed to reject artist', { variant: 'error' }); }
  };

  const handleUpdateUserStatus = async (userId: string, newStatus: string) => {
    try {
      await adminAPI.updateUserStatus(userId, newStatus);
      enqueueSnackbar(`User ${newStatus.toLowerCase()} successfully`, { variant: 'success' });
      fetchUsers();
    } catch { enqueueSnackbar('Failed to update user', { variant: 'error' }); }
  };

  const handleToggleFeatured = async (artistId: string) => {
    try {
      await adminAPI.toggleFeatured(artistId);
      enqueueSnackbar('Featured status toggled', { variant: 'success' });
      fetchArtists();
    } catch { enqueueSnackbar('Failed to toggle featured', { variant: 'error' }); }
  };

  const handleSetCommission = async () => {
    const rate = parseFloat(newRate);
    if (isNaN(rate) || rate < 0 || rate > 100) {
      enqueueSnackbar('Rate must be between 0 and 100', { variant: 'warning' });
      return;
    }
    try {
      if (selectedArtist) {
        await adminAPI.setArtistCommission(selectedArtist.id, rate);
        enqueueSnackbar(`Commission set to ${rate}% for ${selectedArtist.displayName || selectedArtist.artistName}`, { variant: 'success' });
      } else {
        await adminAPI.setDefaultCommission(rate);
        enqueueSnackbar(`Default commission set to ${rate}%`, { variant: 'success' });
      }
      setCommissionDialogOpen(false);
      setNewRate('');
      setSelectedArtist(null);
      fetchCommissions();
      fetchDashboard();
    } catch { enqueueSnackbar('Failed to update commission', { variant: 'error' }); }
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
      <Box sx={{ background: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%)', color: '#fff', py: { xs: 4, md: 6 } }}>
        <Container maxWidth="lg">
          <Typography variant="h3" sx={{ fontWeight: 800, mb: 1, fontSize: { xs: '1.6rem', md: '2.2rem' } }}>
            ⚙️ Admin Dashboard
          </Typography>
          <Typography sx={{ color: 'rgba(255,255,255,0.5)', fontSize: '1rem' }}>
            Manage users, artists, bookings, payments & commissions
          </Typography>

          {/* Stats Cards */}
          {stats && (
            <Grid container spacing={2} sx={{ mt: 3 }}>
              {[
                { label: 'Total Users', value: stats.totalUsers, icon: '👥', color: '#667eea' },
                { label: 'Artists', value: stats.totalArtists, icon: '🎨', color: '#f093fb' },
                { label: 'Fans', value: stats.totalFans, icon: '❤️', color: '#f5576c' },
                { label: 'Pending Verify', value: stats.pendingVerifications || 0, icon: '🔍', color: '#ff9800' },
                { label: 'Bookings', value: stats.totalBookings, icon: '📅', color: '#38b2ac' },
                { label: 'Revenue', value: `$${Number(stats.totalRevenue || 0).toFixed(2)}`, icon: '💰', color: '#fda085' },
              ].map((s) => (
                <Grid item xs={6} sm={4} md={2} key={s.label}>
                  <Card sx={{ bgcolor: 'rgba(255,255,255,0.06)', backdropFilter: 'blur(10px)', border: '1px solid rgba(255,255,255,0.08)' }}>
                    <CardContent sx={{ p: 2, '&:last-child': { pb: 2 } }}>
                      <Typography sx={{ fontSize: '1.3rem', mb: 0.5 }}>{s.icon}</Typography>
                      <Typography variant="h5" sx={{ fontWeight: 800, color: s.color }}>{s.value}</Typography>
                      <Typography variant="caption" sx={{ color: 'rgba(255,255,255,0.5)' }}>{s.label}</Typography>
                    </CardContent>
                  </Card>
                </Grid>
              ))}
            </Grid>
          )}
        </Container>
      </Box>

      <Container maxWidth="lg">
        {/* Tabs */}
        <Tabs value={activeTab} onChange={(_, v) => setActiveTab(v)} sx={{ mt: 3, mb: 3,
          '& .MuiTab-root': { fontWeight: 700, textTransform: 'none', fontSize: '0.9rem' },
          '& .Mui-selected': { color: '#667eea' },
          '& .MuiTabs-indicator': { background: '#667eea', height: 3, borderRadius: 2 },
        }}>
          <Tab label="Overview" />
          <Tab label="Users" />
          <Tab label="Artists" />
          <Tab label="Bookings" />
          <Tab label="Payments" />
          <Tab label="Commission" />
          <Tab label={`🔍 Verifications ${stats?.pendingVerifications ? `(${stats.pendingVerifications})` : ''}`} />
        </Tabs>

        {/* ══════ TAB 0: Overview ══════ */}
        {activeTab === 0 && stats && (
          <Grid container spacing={3} sx={{ pb: 5 }}>
            <Grid item xs={12} md={6}>
              <Card>
                <CardContent>
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>💰 Revenue Breakdown</Typography>
                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography sx={{ color: '#64648b' }}>Total Revenue</Typography>
                      <Typography sx={{ fontWeight: 700 }}>${Number(stats.totalRevenue || 0).toFixed(2)}</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography sx={{ color: '#64648b' }}>Platform Commission ({Number(stats.defaultCommissionRate)}%)</Typography>
                      <Typography sx={{ fontWeight: 700, color: '#667eea' }}>${Number(stats.platformCommission || 0).toFixed(2)}</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography sx={{ color: '#64648b' }}>Artist Earnings</Typography>
                      <Typography sx={{ fontWeight: 700, color: '#38b2ac' }}>${Number(stats.artistEarnings || 0).toFixed(2)}</Typography>
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={6}>
              <Card>
                <CardContent>
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>📊 Booking Stats</Typography>
                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography sx={{ color: '#64648b' }}>Total Bookings</Typography>
                      <Typography sx={{ fontWeight: 700 }}>{stats.totalBookings}</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography sx={{ color: '#64648b' }}>Pending</Typography>
                      <Typography sx={{ fontWeight: 700, color: '#fda085' }}>{stats.pendingBookings}</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography sx={{ color: '#64648b' }}>Completed</Typography>
                      <Typography sx={{ fontWeight: 700, color: '#38b2ac' }}>{stats.completedBookings}</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                      <Typography sx={{ color: '#64648b' }}>Successful Payments</Typography>
                      <Typography sx={{ fontWeight: 700 }}>{stats.totalPayments}</Typography>
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        )}

        {/* ══════ TAB 1: Users ══════ */}
        {activeTab === 1 && (
          <Box sx={{ pb: 5 }}>
            <Box sx={{ display: 'flex', gap: 2, mb: 3, flexWrap: 'wrap' }}>
              <FormControl size="small" sx={{ minWidth: 160 }}>
                <InputLabel>Filter by Role</InputLabel>
                <Select value={userFilter} onChange={(e) => setUserFilter(e.target.value)} label="Filter by Role">
                  <MenuItem value="">All</MenuItem>
                  <MenuItem value="USER">Fans</MenuItem>
                  <MenuItem value="ARTIST">Artists</MenuItem>
                  <MenuItem value="ADMIN">Admins</MenuItem>
                </Select>
              </FormControl>
            </Box>
            <TableContainer component={Paper} sx={{ borderRadius: '14px' }}>
              <Table>
                <TableHead>
                  <TableRow sx={{ bgcolor: '#f8f9fe' }}>
                    <TableCell sx={{ fontWeight: 700 }}>Email</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Role</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Status</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Phone</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Joined</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {users.map((u: any) => (
                    <TableRow key={u.id} hover>
                      <TableCell>{u.email}</TableCell>
                      <TableCell>
                        <Chip label={u.role} size="small" sx={{ fontWeight: 600, fontSize: '0.7rem',
                          bgcolor: u.role === 'ADMIN' ? 'rgba(102,126,234,0.1)' : u.role === 'ARTIST' ? 'rgba(240,147,251,0.1)' : 'rgba(56,178,172,0.1)',
                          color: u.role === 'ADMIN' ? '#667eea' : u.role === 'ARTIST' ? '#d06ee0' : '#38b2ac',
                        }} />
                      </TableCell>
                      <TableCell>
                        <Chip label={u.status} size="small" sx={{ fontWeight: 600, fontSize: '0.7rem',
                          bgcolor: `${STATUS_COLORS[u.status] || '#64648b'}20`, color: STATUS_COLORS[u.status] || '#64648b',
                        }} />
                      </TableCell>
                      <TableCell sx={{ color: '#64648b', fontSize: '0.85rem' }}>{u.phoneNumber || '—'}</TableCell>
                      <TableCell sx={{ color: '#64648b', fontSize: '0.85rem' }}>{u.createdAt ? new Date(u.createdAt).toLocaleDateString() : '—'}</TableCell>
                      <TableCell>
                        {u.role !== 'ADMIN' && (
                          <Box sx={{ display: 'flex', gap: 0.5 }}>
                            {u.status !== 'ACTIVE' && (
                              <Button size="small" onClick={() => handleUpdateUserStatus(u.id, 'ACTIVE')}
                                sx={{ fontSize: '0.7rem', color: '#38b2ac' }}>Activate</Button>
                            )}
                            {u.status !== 'SUSPENDED' && (
                              <Button size="small" onClick={() => handleUpdateUserStatus(u.id, 'SUSPENDED')}
                                sx={{ fontSize: '0.7rem', color: '#f5576c' }}>Suspend</Button>
                            )}
                          </Box>
                        )}
                      </TableCell>
                    </TableRow>
                  ))}
                  {users.length === 0 && (
                    <TableRow><TableCell colSpan={6} sx={{ textAlign: 'center', py: 4, color: '#64648b' }}>No users found</TableCell></TableRow>
                  )}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
        )}

        {/* ══════ TAB 2: Artists ══════ */}
        {activeTab === 2 && (
          <Box sx={{ pb: 5 }}>
            <TableContainer component={Paper} sx={{ borderRadius: '14px' }}>
              <Table>
                <TableHead>
                  <TableRow sx={{ bgcolor: '#f8f9fe' }}>
                    <TableCell sx={{ fontWeight: 700 }}>Artist</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Email</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Categories</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Services</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Rating</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Commission</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Status</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {artists.map((a: any) => (
                    <TableRow key={a.id} hover>
                      <TableCell>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
                          <Avatar src={a.avatarUrl} sx={{ width: 36, height: 36, bgcolor: '#667eea', fontSize: '0.9rem' }}>
                            {(a.displayName || 'A').charAt(0)}
                          </Avatar>
                          <Box>
                            <Typography sx={{ fontWeight: 600, fontSize: '0.9rem' }}>{a.displayName}</Typography>
                            {a.featured && <Chip label="Featured" size="small" sx={{ height: 18, fontSize: '0.6rem', bgcolor: 'rgba(253,160,133,0.2)', color: '#fda085', fontWeight: 700 }} />}
                          </Box>
                        </Box>
                      </TableCell>
                      <TableCell sx={{ fontSize: '0.85rem', color: '#64648b' }}>{a.email}</TableCell>
                      <TableCell>
                        <Box sx={{ display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
                          {(a.categories || []).map((c: string) => (
                            <Chip key={c} label={c} size="small" sx={{ fontSize: '0.6rem', height: 20 }} />
                          ))}
                          {(!a.categories || a.categories.length === 0) && <Typography sx={{ color: '#a0a0b8', fontSize: '0.8rem' }}>—</Typography>}
                        </Box>
                      </TableCell>
                      <TableCell>
                        <Typography sx={{ fontSize: '0.85rem' }}>{a.activeOfferingsCount}/{a.offeringsCount}</Typography>
                      </TableCell>
                      <TableCell>
                        {a.ratingCount > 0 ? (
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.3 }}>
                            <Typography sx={{ color: '#f6d365', fontSize: '0.8rem' }}>★</Typography>
                            <Typography sx={{ fontSize: '0.85rem', fontWeight: 600 }}>{Number(a.ratingAggregate || 0).toFixed(1)}</Typography>
                            <Typography sx={{ fontSize: '0.7rem', color: '#64648b' }}>({a.ratingCount})</Typography>
                          </Box>
                        ) : '—'}
                      </TableCell>
                      <TableCell>
                        <Chip label={`${a.commissionRate}%`} size="small" sx={{ fontWeight: 700, fontSize: '0.75rem', bgcolor: 'rgba(102,126,234,0.1)', color: '#667eea',
                          cursor: 'pointer', '&:hover': { bgcolor: 'rgba(102,126,234,0.2)' } }}
                          onClick={() => { setSelectedArtist(a); setNewRate(String(a.commissionRate)); setCommissionDialogOpen(true); }}
                        />
                      </TableCell>
                      <TableCell>
                        <Chip label={a.accountStatus || 'ACTIVE'} size="small" sx={{ fontWeight: 600, fontSize: '0.7rem',
                          bgcolor: `${STATUS_COLORS[a.accountStatus] || '#38b2ac'}20`, color: STATUS_COLORS[a.accountStatus] || '#38b2ac',
                        }} />
                      </TableCell>
                      <TableCell>
                        <Button size="small" onClick={() => handleToggleFeatured(a.id)}
                          sx={{ fontSize: '0.7rem', color: a.featured ? '#f5576c' : '#fda085' }}>
                          {a.featured ? 'Unfeature' : '⭐ Feature'}
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                  {artists.length === 0 && (
                    <TableRow><TableCell colSpan={8} sx={{ textAlign: 'center', py: 4, color: '#64648b' }}>No artists found</TableCell></TableRow>
                  )}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
        )}

        {/* ══════ TAB 3: Bookings ══════ */}
        {activeTab === 3 && (
          <Box sx={{ pb: 5 }}>
            <FormControl size="small" sx={{ minWidth: 160, mb: 3 }}>
              <InputLabel>Status</InputLabel>
              <Select value={bookingFilter} onChange={(e) => setBookingFilter(e.target.value)} label="Status">
                <MenuItem value="">All</MenuItem>
                <MenuItem value="PENDING">Pending</MenuItem>
                <MenuItem value="CONFIRMED">Confirmed</MenuItem>
                <MenuItem value="COMPLETED">Completed</MenuItem>
                <MenuItem value="CANCELLED">Cancelled</MenuItem>
              </Select>
            </FormControl>
            <TableContainer component={Paper} sx={{ borderRadius: '14px' }}>
              <Table>
                <TableHead>
                  <TableRow sx={{ bgcolor: '#f8f9fe' }}>
                    <TableCell sx={{ fontWeight: 700 }}>Fan</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Artist</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Service</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Price</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Date</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Status</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Payment</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {bookings.map((b: any) => (
                    <TableRow key={b.id} hover>
                      <TableCell sx={{ fontSize: '0.85rem' }}>{b.fanEmail || '—'}</TableCell>
                      <TableCell sx={{ fontSize: '0.85rem', fontWeight: 600 }}>{b.artistName || '—'}</TableCell>
                      <TableCell>
                        <Box>
                          <Typography sx={{ fontSize: '0.85rem', fontWeight: 600 }}>{b.offeringTitle || '—'}</Typography>
                          {b.offeringType && <Typography sx={{ fontSize: '0.7rem', color: '#64648b' }}>{b.offeringType}</Typography>}
                        </Box>
                      </TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>{b.offeringPrice ? `$${b.offeringPrice}` : '—'}</TableCell>
                      <TableCell sx={{ fontSize: '0.8rem', color: '#64648b' }}>{b.scheduledAt ? new Date(b.scheduledAt).toLocaleDateString() : '—'}</TableCell>
                      <TableCell>
                        <Chip label={b.status} size="small" sx={{ fontWeight: 600, fontSize: '0.7rem',
                          bgcolor: `${STATUS_COLORS[b.status] || '#64648b'}20`, color: STATUS_COLORS[b.status] || '#64648b',
                        }} />
                      </TableCell>
                      <TableCell>
                        {b.paymentStatus ? (
                          <Chip label={`${b.paymentStatus} $${b.paymentAmount || 0}`} size="small" sx={{ fontWeight: 600, fontSize: '0.65rem',
                            bgcolor: `${STATUS_COLORS[b.paymentStatus] || '#64648b'}20`, color: STATUS_COLORS[b.paymentStatus] || '#64648b',
                          }} />
                        ) : <Typography sx={{ color: '#a0a0b8', fontSize: '0.8rem' }}>No payment</Typography>}
                      </TableCell>
                    </TableRow>
                  ))}
                  {bookings.length === 0 && (
                    <TableRow><TableCell colSpan={7} sx={{ textAlign: 'center', py: 4, color: '#64648b' }}>No bookings found</TableCell></TableRow>
                  )}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
        )}

        {/* ══════ TAB 4: Payments ══════ */}
        {activeTab === 4 && (
          <Box sx={{ pb: 5 }}>
            <TableContainer component={Paper} sx={{ borderRadius: '14px' }}>
              <Table>
                <TableHead>
                  <TableRow sx={{ bgcolor: '#f8f9fe' }}>
                    <TableCell sx={{ fontWeight: 700 }}>Fan</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Artist</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Amount</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Commission</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Artist Payout</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Status</TableCell>
                    <TableCell sx={{ fontWeight: 700 }}>Date</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {payments.map((p: any) => (
                    <TableRow key={p.id} hover>
                      <TableCell sx={{ fontSize: '0.85rem' }}>{p.fanEmail || '—'}</TableCell>
                      <TableCell sx={{ fontSize: '0.85rem', fontWeight: 600 }}>{p.artistName || '—'}</TableCell>
                      <TableCell sx={{ fontWeight: 700 }}>${Number(p.amount || 0).toFixed(2)}</TableCell>
                      <TableCell>
                        <Tooltip title={`${p.commissionRate || 10}% rate`}>
                          <Typography sx={{ fontWeight: 600, color: '#667eea', fontSize: '0.9rem' }}>
                            ${Number(p.commissionAmount || 0).toFixed(2)}
                          </Typography>
                        </Tooltip>
                      </TableCell>
                      <TableCell>
                        <Typography sx={{ fontWeight: 600, color: '#38b2ac', fontSize: '0.9rem' }}>
                          ${Number(p.artistPayout || 0).toFixed(2)}
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Chip label={p.status} size="small" sx={{ fontWeight: 600, fontSize: '0.7rem',
                          bgcolor: `${STATUS_COLORS[p.status] || '#64648b'}20`, color: STATUS_COLORS[p.status] || '#64648b',
                        }} />
                      </TableCell>
                      <TableCell sx={{ fontSize: '0.8rem', color: '#64648b' }}>{p.createdAt ? new Date(p.createdAt).toLocaleDateString() : '—'}</TableCell>
                    </TableRow>
                  ))}
                  {payments.length === 0 && (
                    <TableRow><TableCell colSpan={7} sx={{ textAlign: 'center', py: 4, color: '#64648b' }}>No payments found</TableCell></TableRow>
                  )}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
        )}

        {/* ══════ TAB 5: Commission ══════ */}
        {activeTab === 5 && (
          <Box sx={{ pb: 5 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
              <Typography variant="h6" sx={{ fontWeight: 700 }}>Commission Configuration</Typography>
              <Button variant="contained" onClick={() => { setSelectedArtist(null); setNewRate(String(stats?.defaultCommissionRate || 10)); setCommissionDialogOpen(true); }}
                sx={{ background: 'linear-gradient(135deg, #667eea, #764ba2)', borderRadius: '10px' }}>
                Edit Default Rate
              </Button>
            </Box>
            <Grid container spacing={3}>
              {commissions.map((c: any) => (
                <Grid item xs={12} sm={6} md={4} key={c.id}>
                  <Card sx={{ overflow: 'hidden' }}>
                    <Box sx={{ height: 4, background: c.isDefault ? 'linear-gradient(135deg, #667eea, #764ba2)' : 'linear-gradient(135deg, #38b2ac, #4fd1c5)' }} />
                    <CardContent sx={{ p: 3 }}>
                      <Typography variant="h4" sx={{ fontWeight: 800, color: '#667eea', mb: 1 }}>{Number(c.commissionRate).toFixed(1)}%</Typography>
                      <Typography sx={{ fontWeight: 700, mb: 0.5 }}>
                        {c.isDefault ? '🏦 Platform Default' : `🎨 ${c.artistName || 'Artist Override'}`}
                      </Typography>
                      <Typography variant="body2" sx={{ color: '#64648b' }}>
                        {c.isDefault ? 'Applies to all artists without custom rate' : 'Custom rate for this artist'}
                      </Typography>
                    </CardContent>
                  </Card>
                </Grid>
              ))}
            </Grid>

            <Typography variant="h6" sx={{ fontWeight: 700, mt: 5, mb: 2 }}>Set Per-Artist Commission</Typography>
            <Typography variant="body2" sx={{ color: '#64648b', mb: 3 }}>
              Click the commission percentage on any artist in the Artists tab to set a custom rate.
            </Typography>
          </Box>
        )}

        {/* ══════ TAB 6: Verifications ══════ */}
        {activeTab === 6 && (
          <Box sx={{ pb: 5 }}>
            <Typography variant="h6" sx={{ fontWeight: 700, mb: 3 }}>
              🔍 Pending Artist Verifications ({pendingVerifications.length})
            </Typography>
            {pendingVerifications.length === 0 ? (
              <Alert severity="success" sx={{ borderRadius: '12px' }}>
                No pending verifications. All artist applications have been reviewed! 🎉
              </Alert>
            ) : (
              <Grid container spacing={3}>
                {pendingVerifications.map((artist: any) => (
                  <Grid item xs={12} md={6} key={artist.id}>
                    <Card sx={{ overflow: 'hidden', border: '2px solid #ff9800' }}>
                      <Box sx={{ height: 4, background: 'linear-gradient(135deg, #ff9800, #f57c00)' }} />
                      <CardContent sx={{ p: 3 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
                          <Avatar src={artist.avatarUrl} sx={{ width: 48, height: 48, bgcolor: '#667eea' }}>
                            {artist.displayName?.[0] || '?'}
                          </Avatar>
                          <Box>
                            <Typography variant="h6" sx={{ fontWeight: 700 }}>{artist.displayName}</Typography>
                            <Typography variant="body2" sx={{ color: '#64648b' }}>{artist.email}</Typography>
                          </Box>
                          <Chip label="PENDING" size="small" sx={{ ml: 'auto', bgcolor: '#fff3e0', color: '#e65100', fontWeight: 700 }} />
                        </Box>

                        {artist.bio && (
                          <Typography variant="body2" sx={{ color: '#64648b', mb: 2 }}>{artist.bio}</Typography>
                        )}

                        <Typography variant="subtitle2" sx={{ fontWeight: 700, mb: 1, color: '#1a1a2e' }}>
                          Verification Proof:
                        </Typography>

                        {artist.instagramUrl && (
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                            <Typography variant="body2" sx={{ fontWeight: 600 }}>📸 Instagram:</Typography>
                            <Typography
                              component="a"
                              href={artist.instagramUrl}
                              target="_blank"
                              rel="noopener"
                              variant="body2"
                              sx={{ color: '#667eea', textDecoration: 'underline', wordBreak: 'break-all' }}
                            >
                              {artist.instagramUrl}
                            </Typography>
                          </Box>
                        )}

                        {artist.youtubeUrl && (
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                            <Typography variant="body2" sx={{ fontWeight: 600 }}>🎬 YouTube:</Typography>
                            <Typography
                              component="a"
                              href={artist.youtubeUrl}
                              target="_blank"
                              rel="noopener"
                              variant="body2"
                              sx={{ color: '#f5576c', textDecoration: 'underline', wordBreak: 'break-all' }}
                            >
                              {artist.youtubeUrl}
                            </Typography>
                          </Box>
                        )}

                        {artist.certificateInfo && (
                          <Box sx={{ mb: 1 }}>
                            <Typography variant="body2" sx={{ fontWeight: 600 }}>📜 Certifications:</Typography>
                            <Typography variant="body2" sx={{ color: '#64648b', mt: 0.5, pl: 2, borderLeft: '3px solid #e0e0e0' }}>
                              {artist.certificateInfo}
                            </Typography>
                          </Box>
                        )}

                        {artist.categories?.length > 0 && (
                          <Box sx={{ mt: 2, display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
                            {artist.categories.map((cat: string) => (
                              <Chip key={cat} label={cat} size="small" sx={{ fontSize: '0.7rem', bgcolor: '#f0f0ff', color: '#667eea' }} />
                            ))}
                          </Box>
                        )}

                        <Typography variant="caption" sx={{ color: '#999', display: 'block', mt: 2 }}>
                          Applied: {new Date(artist.createdAt).toLocaleDateString()}
                        </Typography>

                        <Box sx={{ display: 'flex', gap: 2, mt: 3 }}>
                          <Button
                            variant="contained"
                            fullWidth
                            onClick={() => handleVerifyArtist(artist.id)}
                            sx={{ background: 'linear-gradient(135deg, #38b2ac, #4fd1c5)', fontWeight: 700, borderRadius: '10px' }}
                          >
                            ✅ Approve
                          </Button>
                          <Button
                            variant="outlined"
                            fullWidth
                            color="error"
                            onClick={() => { setRejectArtistId(artist.id); setRejectDialogOpen(true); }}
                            sx={{ fontWeight: 700, borderRadius: '10px' }}
                          >
                            ❌ Reject
                          </Button>
                        </Box>
                      </CardContent>
                    </Card>
                  </Grid>
                ))}
              </Grid>
            )}
          </Box>
        )}
      </Container>

      {/* Commission Edit Dialog */}
      <Dialog open={commissionDialogOpen} onClose={() => setCommissionDialogOpen(false)} maxWidth="xs" fullWidth>
        <DialogTitle sx={{ fontWeight: 700 }}>
          {selectedArtist ? `Set Commission for ${selectedArtist.displayName || selectedArtist.artistName}` : 'Set Default Commission Rate'}
        </DialogTitle>
        <DialogContent>
          <TextField
            label="Commission Rate (%)"
            type="number"
            value={newRate}
            onChange={(e) => setNewRate(e.target.value)}
            fullWidth
            sx={{ mt: 2 }}
            inputProps={{ min: 0, max: 100, step: 0.5 }}
            helperText="Enter a percentage between 0 and 100"
          />
        </DialogContent>
        <DialogActions sx={{ p: 3, pt: 1 }}>
          <Button onClick={() => setCommissionDialogOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleSetCommission}
            sx={{ background: 'linear-gradient(135deg, #667eea, #764ba2)' }}>
            Save
          </Button>
        </DialogActions>
      </Dialog>

      {/* Reject Artist Dialog */}
      <Dialog open={rejectDialogOpen} onClose={() => setRejectDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle sx={{ fontWeight: 700, color: '#f5576c' }}>
          Reject Artist Verification
        </DialogTitle>
        <DialogContent>
          <Alert severity="warning" sx={{ mb: 2 }}>
            The artist will be notified with your rejection reason. They can update their profile and resubmit.
          </Alert>
          <TextField
            label="Rejection Reason"
            value={rejectReason}
            onChange={(e) => setRejectReason(e.target.value)}
            fullWidth
            multiline
            rows={3}
            required
            placeholder="e.g., Instagram account has less than 800K followers, certificate not verifiable..."
            sx={{ mt: 1 }}
          />
        </DialogContent>
        <DialogActions sx={{ p: 3, pt: 1 }}>
          <Button onClick={() => setRejectDialogOpen(false)}>Cancel</Button>
          <Button variant="contained" color="error" onClick={handleRejectArtist}>
            Reject
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

