import { useNavigate } from 'react-router-dom';
import { Container, Box, Button, Typography, Grid, Card, CardContent } from '@mui/material';
import { useAuth } from '../context/AuthContext';

export default function HomePage() {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  const featureCards = [
    {
      icon: '✦',
      title: 'Discover Artists',
      desc: 'Find talented musicians, dancers, painters, and creators worldwide.',
      gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    },
    {
      icon: '◈',
      title: 'Book Sessions',
      desc: 'Schedule personalized calls, mentorship sessions, or digital offerings.',
      gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    },
    {
      icon: '◉',
      title: 'Real-time Chat',
      desc: 'Connect instantly with artists through our integrated messaging platform.',
      gradient: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)',
    },
  ];

  return (
    <Box>
      {/* Hero Section */}
      <Box
        sx={{
          background: 'linear-gradient(135deg, #0f0c29 0%, #302b63 40%, #24243e 100%)',
          color: '#fff',
          position: 'relative',
          overflow: 'hidden',
          borderRadius: 0,
          '&::before': {
            content: '""',
            position: 'absolute',
            top: '-50%',
            right: '-20%',
            width: '600px',
            height: '600px',
            borderRadius: '50%',
            background: 'radial-gradient(circle, rgba(102,126,234,0.15) 0%, transparent 70%)',
          },
          '&::after': {
            content: '""',
            position: 'absolute',
            bottom: '-30%',
            left: '-10%',
            width: '500px',
            height: '500px',
            borderRadius: '50%',
            background: 'radial-gradient(circle, rgba(240,147,251,0.1) 0%, transparent 70%)',
          },
        }}
      >
        <Container maxWidth="lg" sx={{ position: 'relative', zIndex: 1 }}>
          <Box sx={{ py: { xs: 8, md: 14 }, textAlign: 'center', maxWidth: 800, mx: 'auto' }}>
            <Typography
              variant="h1"
              sx={{
                fontSize: { xs: '2.5rem', md: '4rem' },
                fontWeight: 900,
                lineHeight: 1.1,
                mb: 3,
                letterSpacing: '-0.03em',
              }}
            >
              Connect with the{' '}
              <Box
                component="span"
                sx={{
                  background: 'linear-gradient(135deg, #667eea 0%, #f093fb 50%, #fda085 100%)',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                }}
              >
                world's best
              </Box>{' '}
              artists
            </Typography>

            <Typography
              variant="h5"
              sx={{
                color: 'rgba(255,255,255,0.6)',
                fontWeight: 400,
                mb: 5,
                lineHeight: 1.6,
                fontSize: { xs: '1.1rem', md: '1.35rem' },
              }}
            >
              Book personalized sessions with creators, mentors & artists.
              One platform. Infinite possibilities.
            </Typography>

            {!isAuthenticated ? (
              <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
                <Button
                  size="large"
                  onClick={() => navigate('/signup')}
                  sx={{
                    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    color: '#fff',
                    px: 5,
                    py: 1.75,
                    fontSize: '1.05rem',
                    fontWeight: 700,
                    borderRadius: '14px',
                    '&:hover': {
                      background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                      boxShadow: '0 8px 30px rgba(102,126,234,0.5)',
                      transform: 'translateY(-2px)',
                    },
                  }}
                >
                  Start for free →
                </Button>
                <Button
                  size="large"
                  onClick={() => navigate('/artists')}
                  sx={{
                    color: 'rgba(255,255,255,0.85)',
                    px: 4,
                    py: 1.75,
                    fontSize: '1.05rem',
                    fontWeight: 500,
                    borderRadius: '14px',
                    border: '1px solid rgba(255,255,255,0.2)',
                    '&:hover': {
                      background: 'rgba(255,255,255,0.08)',
                      borderColor: 'rgba(255,255,255,0.35)',
                    },
                  }}
                >
                  Explore artists
                </Button>
              </Box>
            ) : (
              <Button
                size="large"
                onClick={() => navigate('/artists')}
                sx={{
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  color: '#fff',
                  px: 5,
                  py: 1.75,
                  fontSize: '1.05rem',
                  fontWeight: 700,
                  borderRadius: '14px',
                  '&:hover': {
                    background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                    boxShadow: '0 8px 30px rgba(102,126,234,0.5)',
                    transform: 'translateY(-2px)',
                  },
                }}
              >
                Browse Artists →
              </Button>
            )}
          </Box>
        </Container>
      </Box>

      {/* Feature Cards */}
      <Container maxWidth="lg">
        <Box sx={{ py: 10 }}>
          <Typography
            variant="h3"
            align="center"
            sx={{ mb: 1.5, fontSize: { xs: '1.8rem', md: '2.4rem' } }}
          >
            How it works
          </Typography>
          <Typography
            variant="subtitle1"
            align="center"
            sx={{ mb: 6, fontSize: '1.1rem', color: '#64648b' }}
          >
            Three simple steps to connect with incredible talent
          </Typography>

          <Grid container spacing={4}>
            {featureCards.map((card, idx) => (
              <Grid item xs={12} md={4} key={idx}>
                <Card
                  sx={{
                    height: '100%',
                    border: '1px solid rgba(0,0,0,0.04)',
                    p: 1,
                  }}
                >
                  <CardContent sx={{ p: 3 }}>
                    <Box
                      sx={{
                        width: 56,
                        height: 56,
                        borderRadius: '14px',
                        background: card.gradient,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        mb: 3,
                        fontSize: '1.5rem',
                        color: '#fff',
                      }}
                    >
                      {card.icon}
                    </Box>
                    <Typography variant="h6" sx={{ mb: 1.5, fontWeight: 700 }}>
                      {card.title}
                    </Typography>
                    <Typography variant="body2" sx={{ color: '#64648b', lineHeight: 1.7 }}>
                      {card.desc}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Box>
      </Container>
    </Box>
  );
}
