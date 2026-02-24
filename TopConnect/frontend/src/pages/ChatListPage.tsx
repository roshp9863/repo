import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Box,
  Typography,
  CircularProgress,
  Alert,
  Card,
  CardActionArea,
  Avatar,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Divider,
  Badge,
  Button,
} from '@mui/material';
import { chatAPI } from '../services/apiClient';

export default function ChatListPage() {
  const navigate = useNavigate();
  const [threads, setThreads] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchThreads();
  }, []);

  const fetchThreads = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await chatAPI.list(0, 20);
      setThreads(response.data.data.content || []);
    } catch (err: any) {
      setError('Failed to load chats.');
      setThreads([]);
    } finally {
      setLoading(false);
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
        <Container maxWidth="md">
          <Typography variant="h3" sx={{ fontWeight: 800, mb: 1, fontSize: { xs: '1.8rem', md: '2.5rem' } }}>
            Messages
          </Typography>
          <Typography sx={{ color: 'rgba(255,255,255,0.6)', fontSize: '1.1rem' }}>
            Your conversations with artists
          </Typography>
        </Container>
      </Box>

      <Container maxWidth="md">
        <Box sx={{ py: 5 }}>
          {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}

          {threads.length === 0 ? (
            <Box sx={{ textAlign: 'center', py: 10, bgcolor: '#fff', borderRadius: '16px', border: '1px solid rgba(0,0,0,0.04)' }}>
              <Typography variant="h6" sx={{ color: '#64648b', mb: 2 }}>
                No conversations yet
              </Typography>
              <Typography variant="body2" sx={{ color: '#64648b', mb: 3 }}>
                Start chatting by visiting an artist's profile
              </Typography>
              <Button variant="contained" onClick={() => navigate('/artists')}>
                Discover Artists →
              </Button>
            </Box>
          ) : (
            <Card sx={{ overflow: 'hidden' }}>
              <List sx={{ p: 0 }}>
                {threads.map((thread: any, index: number) => (
                  <React.Fragment key={thread.id}>
                    <ListItem
                      component={CardActionArea}
                      onClick={() => navigate(`/chat/${thread.id}`)}
                      sx={{ py: 2.5, px: 3 }}
                    >
                      <ListItemAvatar>
                        <Badge badgeContent={thread.unreadCount || 0} color="error" invisible={!thread.unreadCount}>
                          <Avatar
                            src={thread.artistAvatarUrl}
                            sx={{
                              width: 48,
                              height: 48,
                              background: 'linear-gradient(135deg, #667eea 0%, #f093fb 100%)',
                              fontWeight: 700,
                            }}
                          >
                            {(thread.artistDisplayName || 'A').charAt(0)}
                          </Avatar>
                        </Badge>
                      </ListItemAvatar>
                      <ListItemText
                        primary={
                          <Typography variant="subtitle1" sx={{ fontWeight: thread.unreadCount ? 700 : 500, color: '#1a1a2e' }}>
                            {thread.artistDisplayName || 'Artist'}
                          </Typography>
                        }
                        secondary={
                          <Box>
                            <Typography
                              variant="body2"
                              sx={{
                                color: '#64648b',
                                overflow: 'hidden',
                                textOverflow: 'ellipsis',
                                whiteSpace: 'nowrap',
                                maxWidth: '400px',
                              }}
                            >
                              {thread.lastMessage || 'No messages yet'}
                            </Typography>
                            {thread.lastMessageAt && (
                              <Typography variant="caption" sx={{ color: '#a0a0b8' }}>
                                {new Date(thread.lastMessageAt).toLocaleString()}
                              </Typography>
                            )}
                          </Box>
                        }
                      />
                    </ListItem>
                    {index < threads.length - 1 && <Divider />}
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
