import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Box, Typography, CircularProgress, Alert, Card, TextField, Button, Paper, Divider } from '@mui/material';
import { chatAPI } from '../services/apiClient';
import { useAuth } from '../context/AuthContext';
import { useSnackbar } from 'notistack';

export default function ChatPage() {
  const { threadId } = useParams<{ threadId: string }>();
  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();
  const { user } = useAuth();
  const [messages, setMessages] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [newMessage, setNewMessage] = useState('');
  const [sending, setSending] = useState(false);

  useEffect(() => {
    if (threadId) {
      fetchMessages();
      // Poll for new messages every 3 seconds
      const interval = setInterval(fetchMessages, 3000);
      return () => clearInterval(interval);
    }
  }, [threadId]);

  const fetchMessages = async () => {
    if (!threadId) return;

    try {
      const response = await chatAPI.getMessages(threadId, 0, 50);
      // Messages come in desc order, reverse for display
      const msgs = response.data.data.content || [];
      setMessages(msgs.reverse());
      setError('');
    } catch (err: any) {
      console.error('Error fetching messages:', err);
      if (loading) {
        setError('Failed to load messages. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleSendMessage = async () => {
    if (!threadId || !newMessage.trim()) return;

    setSending(true);
    try {
      await chatAPI.sendMessage(threadId, newMessage);
      setNewMessage('');
      // Immediately fetch updated messages
      await fetchMessages();
    } catch (err: any) {
      const message = err.response?.data?.message || 'Failed to send message';
      enqueueSnackbar(message, { variant: 'error' });
    } finally {
      setSending(false);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
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
    <Box sx={{ height: 'calc(100vh - 68px)', display: 'flex', flexDirection: 'column', bgcolor: '#f8f9fe' }}>
      {/* Chat header */}
      <Box
        sx={{
          px: 3,
          py: 2,
          bgcolor: '#fff',
          borderBottom: '1px solid rgba(0,0,0,0.06)',
          display: 'flex',
          alignItems: 'center',
          gap: 2,
        }}
      >
        <Button
          onClick={() => navigate('/chats')}
          sx={{ minWidth: 'auto', color: '#64648b', fontWeight: 600, '&:hover': { color: '#1a1a2e' } }}
        >
          ← Back
        </Button>
        <Divider orientation="vertical" flexItem />
        <Typography variant="h6" sx={{ fontWeight: 700 }}>
          Chat
        </Typography>
      </Box>

      {error && <Alert severity="error" sx={{ m: 2, borderRadius: '12px' }}>{error}</Alert>}

      {/* Messages */}
      <Box
        sx={{
          flex: 1,
          overflowY: 'auto',
          px: 3,
          py: 3,
          display: 'flex',
          flexDirection: 'column',
          gap: 1.5,
        }}
      >
        {messages.length === 0 ? (
          <Box sx={{ textAlign: 'center', py: 8 }}>
            <Typography sx={{ color: '#a0a0b8', fontSize: '1rem' }}>
              No messages yet. Say hello! 👋
            </Typography>
          </Box>
        ) : (
          messages.map((message: any, index: number) => {
            const isCurrentUser = message.senderId === user?.id;
            return (
              <Box key={message.id || index} sx={{ display: 'flex', justifyContent: isCurrentUser ? 'flex-end' : 'flex-start' }}>
                <Paper
                  elevation={0}
                  sx={{
                    p: 2,
                    maxWidth: '70%',
                    borderRadius: isCurrentUser ? '16px 16px 4px 16px' : '16px 16px 16px 4px',
                    background: isCurrentUser
                      ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                      : '#ffffff',
                    color: isCurrentUser ? '#fff' : '#1a1a2e',
                    boxShadow: isCurrentUser
                      ? '0 4px 15px rgba(102,126,234,0.3)'
                      : '0 2px 8px rgba(0,0,0,0.06)',
                    border: isCurrentUser ? 'none' : '1px solid rgba(0,0,0,0.04)',
                  }}
                >
                  {!isCurrentUser && message.senderName && (
                    <Typography variant="caption" sx={{ fontWeight: 700, color: '#667eea', display: 'block', mb: 0.5 }}>
                      {message.senderName}
                      {message.source === 'WHATSAPP' && (
                        <span style={{ marginLeft: 6, fontSize: '0.7rem', color: '#25D366', fontWeight: 600 }}>
                          via WhatsApp
                        </span>
                      )}
                    </Typography>
                  )}
                  <Typography variant="body2" sx={{ lineHeight: 1.6 }}>
                    {message.message}
                  </Typography>
                  <Typography
                    variant="caption"
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: 0.5,
                      mt: 0.5,
                      opacity: 0.6,
                      fontSize: '0.7rem',
                    }}
                  >
                    {message.createdAt ? new Date(message.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : ''}
                    {message.source === 'WHATSAPP' && isCurrentUser && (
                      <span style={{ color: '#25D366' }}>• WhatsApp</span>
                    )}
                  </Typography>
                </Paper>
              </Box>
            );
          })
        )}
      </Box>

      {/* Input */}
      <Box
        sx={{
          px: 3,
          py: 2,
          bgcolor: '#fff',
          borderTop: '1px solid rgba(0,0,0,0.06)',
          display: 'flex',
          gap: 1.5,
        }}
      >
        <TextField
          fullWidth
          multiline
          maxRows={3}
          placeholder="Type a message..."
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          onKeyDown={handleKeyDown}
          disabled={sending}
          size="small"
          sx={{
            '& .MuiOutlinedInput-root': {
              borderRadius: '14px',
              bgcolor: '#f8f9fe',
            },
          }}
        />
        <Button
          variant="contained"
          onClick={handleSendMessage}
          disabled={sending || !newMessage.trim()}
          sx={{
            alignSelf: 'flex-end',
            borderRadius: '14px',
            px: 3,
            minWidth: 80,
          }}
        >
          {sending ? '...' : 'Send'}
        </Button>
      </Box>
    </Box>
  );
}
