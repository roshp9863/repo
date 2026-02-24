import React, { useState, useEffect } from 'react';
import {
  Container, Box, Typography, CircularProgress, Alert, Button,
  List, ListItem, Divider, Card,
} from '@mui/material';
import { notificationAPI } from '../services/apiClient';
import { useSnackbar } from 'notistack';

export default function NotificationsPage() {
  const { enqueueSnackbar } = useSnackbar();
  const [notifications, setNotifications] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    fetchNotifications();
    fetchUnreadCount();
  }, []);

  const fetchNotifications = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await notificationAPI.list(0, 50);
      setNotifications(res.data.data?.content || []);
    } catch (err: any) {
      setError('Failed to load notifications');
    } finally {
      setLoading(false);
    }
  };

  const fetchUnreadCount = async () => {
    try {
      const res = await notificationAPI.unreadCount();
      setUnreadCount(res.data.data?.count || 0);
    } catch { /* ignore */ }
  };

  const handleMarkAsRead = async (id: string) => {
    try {
      await notificationAPI.markAsRead(id);
      setNotifications(prev =>
        prev.map(n => n.id === id ? { ...n, read: true, status: 'SENT' } : n)
      );
      setUnreadCount(prev => Math.max(0, prev - 1));
    } catch {
      enqueueSnackbar('Failed to mark as read', { variant: 'error' });
    }
  };

  const handleMarkAllRead = async () => {
    try {
      await notificationAPI.markAllAsRead();
      setNotifications(prev => prev.map(n => ({ ...n, read: true, status: 'SENT' })));
      setUnreadCount(0);
      enqueueSnackbar('All notifications marked as read', { variant: 'success' });
    } catch {
      enqueueSnackbar('Failed to mark all as read', { variant: 'error' });
    }
  };

  const getTypeGradient = (type: string) => {
    const gradients: Record<string, string> = {
      BOOKING_REQUEST: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)',
      BOOKING_CREATED: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      BOOKING_CONFIRMED: 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)',
      BOOKING_CANCELLED: 'linear-gradient(135deg, #f5576c 0%, #ff6b6b 100%)',
      BOOKING_COMPLETED: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
      MESSAGE: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      PAYMENT: 'linear-gradient(135deg, #38b2ac 0%, #4fd1c5 100%)',
    };
    return gradients[type] || gradients.BOOKING_CREATED;
  };

  const getIcon = (type: string) => {
    const icons: Record<string, string> = {
      BOOKING_REQUEST: '📅',
      BOOKING_CREATED: '🎫',
      BOOKING_CONFIRMED: '✅',
      BOOKING_CANCELLED: '❌',
      BOOKING_COMPLETED: '🎉',
      MESSAGE: '💬',
      PAYMENT: '💰',
    };
    return icons[type] || '📢';
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
      <Box sx={{ background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%)', color: '#fff', py: { xs: 5, md: 7 }, borderRadius: 0 }}>
        <Container maxWidth="md">
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 2 }}>
            <Box>
              <Typography variant="h3" sx={{ fontWeight: 800, mb: 1, fontSize: { xs: '1.8rem', md: '2.5rem' } }}>
                Notifications
              </Typography>
              <Typography sx={{ color: 'rgba(255,255,255,0.6)', fontSize: '1.1rem' }}>
                Stay updated with your activity
              </Typography>
            </Box>
            {unreadCount > 0 && (
              <Button
                onClick={handleMarkAllRead}
                sx={{
                  color: 'rgba(255,255,255,0.8)',
                  border: '1px solid rgba(255,255,255,0.2)',
                  borderRadius: '12px',
                  px: 3,
                  '&:hover': { background: 'rgba(255,255,255,0.1)' },
                }}
              >
                Mark all as read ({unreadCount})
              </Button>
            )}
          </Box>
        </Container>
      </Box>

      <Container maxWidth="md">
        <Box sx={{ py: 5 }}>
          {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}

          {notifications.length === 0 ? (
            <Box sx={{ textAlign: 'center', py: 10, bgcolor: '#fff', borderRadius: '16px', border: '1px solid rgba(0,0,0,0.04)' }}>
              <Typography sx={{ fontSize: '3rem', mb: 2 }}>🔔</Typography>
              <Typography variant="h6" sx={{ color: '#64648b', fontWeight: 600 }}>You're all caught up!</Typography>
              <Typography variant="body2" sx={{ color: '#a0a0b8', mt: 1 }}>
                Notifications about bookings, messages, and updates will appear here.
              </Typography>
            </Box>
          ) : (
            <Card sx={{ overflow: 'hidden' }}>
              <List sx={{ p: 0 }}>
                {notifications.map((n: any, index: number) => (
                  <React.Fragment key={n.id}>
                    <ListItem
                      onClick={() => !n.read && handleMarkAsRead(n.id)}
                      sx={{
                        py: 2.5,
                        px: 3,
                        cursor: n.read ? 'default' : 'pointer',
                        bgcolor: n.read ? 'transparent' : 'rgba(102,126,234,0.03)',
                        transition: 'background 0.2s',
                        '&:hover': { bgcolor: n.read ? 'rgba(0,0,0,0.01)' : 'rgba(102,126,234,0.06)' },
                      }}
                    >
                      <Box sx={{ display: 'flex', width: '100%', gap: 2.5, alignItems: 'start' }}>
                        <Box
                          sx={{
                            width: 44,
                            height: 44,
                            borderRadius: '12px',
                            background: getTypeGradient(n.type),
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            fontSize: '1.2rem',
                            flexShrink: 0,
                          }}
                        >
                          {getIcon(n.type)}
                        </Box>
                        <Box sx={{ flex: 1, minWidth: 0 }}>
                          <Typography
                            variant="subtitle2"
                            sx={{ fontWeight: n.read ? 500 : 700, color: '#1a1a2e', mb: 0.3 }}
                          >
                            {n.title || n.type?.replace(/_/g, ' ')}
                          </Typography>
                          <Typography variant="body2" sx={{ color: '#64648b', mb: 0.5 }}>
                            {n.message || ''}
                          </Typography>
                          <Typography variant="caption" sx={{ color: '#a0a0b8' }}>
                            {n.createdAt
                              ? new Date(n.createdAt).toLocaleDateString('en-US', { month: 'short', day: 'numeric' }) +
                                ' at ' +
                                new Date(n.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
                              : ''}
                          </Typography>
                        </Box>
                        {!n.read && (
                          <Box
                            sx={{
                              width: 10,
                              height: 10,
                              borderRadius: '50%',
                              background: 'linear-gradient(135deg, #667eea, #f093fb)',
                              flexShrink: 0,
                              mt: 1,
                              boxShadow: '0 0 8px rgba(102,126,234,0.5)',
                            }}
                          />
                        )}
                      </Box>
                    </ListItem>
                    {index < notifications.length - 1 && <Divider />}
                  </React.Fragment>
                ))}
              </List>
            </Card>
          )}
        </Box>
      </Container>
    </Box>
  );
}
