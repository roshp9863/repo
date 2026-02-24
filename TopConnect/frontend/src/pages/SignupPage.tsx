import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Box, TextField, Button, Typography, Paper, Alert, ToggleButton, ToggleButtonGroup, Divider } from '@mui/material';
import { useAuth } from '../context/AuthContext';
import { useSnackbar } from 'notistack';

export default function SignupPage() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    role: 'USER',
    displayName: '',
    instagramUrl: '',
    youtubeUrl: '',
    certificateInfo: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const { signup } = useAuth();
  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();

  const handleChange = (e: any) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRoleChange = (_: any, newRole: string | null) => {
    if (newRole) setFormData({ ...formData, role: newRole });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    if (formData.role === 'ARTIST' && !formData.displayName) {
      setError('Display name is required for artists');
      return;
    }
    if (formData.role === 'ARTIST' && !formData.instagramUrl && !formData.youtubeUrl && !formData.certificateInfo) {
      setError('Please provide at least one verification proof: Instagram URL, YouTube URL, or certificate details.');
      return;
    }
    setLoading(true);
    try {
      await signup(formData.email, formData.password, formData.role, formData.displayName,
        formData.instagramUrl, formData.youtubeUrl, formData.certificateInfo);
      if (formData.role === 'ARTIST') {
        enqueueSnackbar('Account created! Your artist profile is under review. You\'ll be notified once verified.', { variant: 'info' });
      } else {
        enqueueSnackbar('Account created! Check your email to verify.', { variant: 'success' });
      }
      navigate('/verify-email');
    } catch (err: any) {
      const message = err.response?.data?.message || err.message || 'Signup failed';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: 'calc(100vh - 130px)',
        display: 'flex',
        alignItems: 'center',
        py: 4,
        background: 'linear-gradient(135deg, #f8f9fe 0%, #eef0fb 100%)',
      }}
    >
      <Container maxWidth="sm">
        <Paper
          elevation={0}
          sx={{
            p: { xs: 4, md: 6 },
            border: '1px solid rgba(0,0,0,0.06)',
            boxShadow: '0 8px 40px rgba(0,0,0,0.06)',
          }}
        >
          <Box sx={{ textAlign: 'center', mb: 4 }}>
            <Box
              sx={{
                width: 56,
                height: 56,
                borderRadius: '16px',
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                mx: 'auto',
                mb: 3,
              }}
            >
              <Typography sx={{ fontWeight: 900, fontSize: '1.3rem', color: '#fff' }}>TC</Typography>
            </Box>
            <Typography variant="h4" sx={{ fontWeight: 800, mb: 1 }}>
              Create your account
            </Typography>
            <Typography variant="body1" sx={{ color: '#64648b' }}>
              Join TopConnect and start connecting
            </Typography>
          </Box>

          {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}

          <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2.5 }}>
            {/* Role Toggle */}
            <Box sx={{ textAlign: 'center' }}>
              <Typography variant="body2" sx={{ mb: 1, fontWeight: 600, color: '#64648b' }}>
                I want to join as
              </Typography>
              <ToggleButtonGroup
                value={formData.role}
                exclusive
                onChange={handleRoleChange}
                sx={{
                  '& .MuiToggleButton-root': {
                    px: 4,
                    py: 1.2,
                    borderRadius: '12px !important',
                    border: '2px solid #e0e0e0 !important',
                    fontWeight: 600,
                    textTransform: 'none',
                    '&.Mui-selected': {
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      color: '#fff',
                      borderColor: '#667eea !important',
                      '&:hover': {
                        background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                      },
                    },
                  },
                }}
              >
                <ToggleButton value="USER">Fan / Mentee</ToggleButton>
                <ToggleButton value="ARTIST">Artist / Creator</ToggleButton>
              </ToggleButtonGroup>
            </Box>

            <TextField
              label="Email address"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              fullWidth
            />

            {formData.role === 'ARTIST' && (
              <TextField
                label="Display Name"
                name="displayName"
                value={formData.displayName}
                onChange={handleChange}
                required
                fullWidth
                placeholder="Your artist / stage name"
              />
            )}

            {formData.role === 'ARTIST' && (
              <>
                <Divider sx={{ my: 1 }}>
                  <Typography variant="body2" sx={{ color: '#64648b', fontWeight: 600 }}>
                    Verification Proof (at least one required)
                  </Typography>
                </Divider>
                <Alert severity="info" sx={{ mb: 0 }}>
                  To maintain platform quality, artist accounts require verification.
                  Provide at least one: social media profile with significant following,
                  or professional certifications/degrees.
                </Alert>
                <TextField
                  label="Instagram Profile URL"
                  name="instagramUrl"
                  value={formData.instagramUrl}
                  onChange={handleChange}
                  fullWidth
                  placeholder="https://instagram.com/yourprofile (800K+ followers)"
                  helperText="Min 800K followers required"
                />
                <TextField
                  label="YouTube Channel URL"
                  name="youtubeUrl"
                  value={formData.youtubeUrl}
                  onChange={handleChange}
                  fullWidth
                  placeholder="https://youtube.com/@yourchannel (10K+ subscribers)"
                  helperText="Min 10K subscribers required"
                />
                <TextField
                  label="Certifications / Qualifications"
                  name="certificateInfo"
                  value={formData.certificateInfo}
                  onChange={handleChange}
                  fullWidth
                  multiline
                  rows={3}
                  placeholder="e.g., Certified Personal Trainer (ACE), MBA from IIM-A, ABRSM Grade 8 Piano..."
                  helperText="Describe your professional certifications, degrees, or qualifications"
                />
              </>
            )}

            <TextField
              label="Password"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              fullWidth
            />

            <TextField
              label="Confirm Password"
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
              fullWidth
            />

            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={loading}
              sx={{ mt: 1, py: 1.5, fontSize: '1rem' }}
            >
              {loading ? 'Creating account...' : 'Create account'}
            </Button>
          </Box>

          <Typography variant="body2" align="center" sx={{ mt: 4, color: '#64648b' }}>
            Already have an account?{' '}
            <Box
              component="span"
              onClick={() => navigate('/login')}
              sx={{
                cursor: 'pointer',
                fontWeight: 700,
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
              }}
            >
              Sign in →
            </Box>
          </Typography>
        </Paper>
      </Container>
    </Box>
  );
}
