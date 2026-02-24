import React, { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { Container, Box, Typography, CircularProgress, Alert, Button, Paper } from '@mui/material';
import { useAuth } from '../context/AuthContext';
import { useSnackbar } from 'notistack';

export default function VerifyEmailPage() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { verifyEmail } = useAuth();
  const { enqueueSnackbar } = useSnackbar();
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState('');
  const [verified, setVerified] = React.useState(false);
  const token = searchParams.get('token');

  useEffect(() => { if (token) verify(); }, [token]);

  const verify = async () => {
    setLoading(true);
    try {
      await verifyEmail(token!);
      setVerified(true);
      enqueueSnackbar('Email verified!', { variant: 'success' });
      setTimeout(() => navigate('/login'), 2000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Verification failed');
    } finally {
      setLoading(false);
    }
  };

  if (!token) {
    return (
      <Box sx={{ minHeight: 'calc(100vh - 130px)', display: 'flex', alignItems: 'center', background: 'linear-gradient(135deg, #f8f9fe 0%, #eef0fb 100%)' }}>
        <Container maxWidth="sm">
          <Paper elevation={0} sx={{ p: 6, textAlign: 'center', border: '1px solid rgba(0,0,0,0.06)', boxShadow: '0 8px 40px rgba(0,0,0,0.06)' }}>
            <Box sx={{ width: 64, height: 64, borderRadius: '50%', background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', mx: 'auto', mb: 3, fontSize: '1.8rem' }}>
              ✉️
            </Box>
            <Typography variant="h4" sx={{ fontWeight: 800, mb: 1.5 }}>Check your email</Typography>
            <Typography sx={{ color: '#64648b', mb: 3, lineHeight: 1.7 }}>
              We've sent a verification link to your email address. Click it to activate your account.
            </Typography>
            <Alert severity="info" sx={{ mb: 3, textAlign: 'left' }}>
              <strong>Dev Mode:</strong> Your account is auto-verified. Go ahead and log in!
            </Alert>
            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
              <Button variant="contained" onClick={() => navigate('/login')}>Go to Login</Button>
              <Button variant="outlined" onClick={() => navigate('/')}>Back to Home</Button>
            </Box>
          </Paper>
        </Container>
      </Box>
    );
  }

  return (
    <Box sx={{ minHeight: 'calc(100vh - 130px)', display: 'flex', alignItems: 'center', background: 'linear-gradient(135deg, #f8f9fe 0%, #eef0fb 100%)' }}>
      <Container maxWidth="sm">
        <Paper elevation={0} sx={{ p: 6, textAlign: 'center', border: '1px solid rgba(0,0,0,0.06)', boxShadow: '0 8px 40px rgba(0,0,0,0.06)' }}>
          <Typography variant="h4" sx={{ fontWeight: 800, mb: 3 }}>Verifying Email</Typography>
          {loading && <CircularProgress sx={{ my: 3, color: '#667eea' }} />}
          {error && !loading && (
            <>
              <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>
              <Button variant="contained" onClick={() => navigate('/login')} sx={{ mt: 3 }}>Go to Login</Button>
            </>
          )}
          {verified && !loading && (
            <Alert severity="success" sx={{ mt: 2 }}>Email verified! Redirecting to login...</Alert>
          )}
        </Paper>
      </Container>
    </Box>
  );
}
