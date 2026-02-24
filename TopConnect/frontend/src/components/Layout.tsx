import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Container,
  Avatar,
  Badge,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemText,
  useMediaQuery,
  useTheme,
} from '@mui/material';
import { useAuth } from '../context/AuthContext';
import { notificationAPI } from '../services/apiClient';

interface LayoutProps {
  children: React.ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const { isAuthenticated, user, logout } = useAuth();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [mobileOpen, setMobileOpen] = useState(false);
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    if (isAuthenticated) {
      notificationAPI.unreadCount()
        .then(res => setUnreadCount(res.data?.data?.count || 0))
        .catch(() => {});
    }
  }, [isAuthenticated, location.pathname]);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path: string) => location.pathname === path || location.pathname.startsWith(path + '/');

  const navItems = [
    { label: 'Discover', path: '/artists', always: true },
    { label: 'Bookings', path: '/bookings', auth: true },
    ...(user?.role === 'ARTIST' ? [{ label: 'My Services', path: '/dashboard/services', auth: true }] : []),
    { label: 'Portfolio', path: '/portfolio', auth: true },
    ...(user?.role === 'ADMIN' ? [{ label: '⚙️ Admin', path: '/admin', auth: true }] : []),
  ];

  const visibleNavItems = navItems.filter(item => item.always || (item.auth && isAuthenticated));

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', bgcolor: '#f8f9fe' }}>
      <AppBar
        position="sticky"
        elevation={0}
        sx={{
          background: '#0a0a1a',
          borderBottom: '1px solid rgba(102,126,234,0.12)',
        }}
      >
        <Container maxWidth="xl">
          <Toolbar disableGutters sx={{ py: 0.8, gap: 1 }}>
            {/* Logo */}
            <Box
              onClick={() => navigate('/')}
              sx={{
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                gap: 1.5,
                mr: { xs: 'auto', md: 4 },
                '&:hover .logo-icon': {
                  transform: 'scale(1.05)',
                  boxShadow: '0 0 20px rgba(102,126,234,0.5)',
                },
              }}
            >
              <Box
                className="logo-icon"
                sx={{
                  width: 38,
                  height: 38,
                  borderRadius: '12px',
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  transition: 'all 0.3s ease',
                  boxShadow: '0 2px 12px rgba(102,126,234,0.3)',
                }}
              >
                <Typography sx={{ fontWeight: 900, fontSize: '1rem', color: '#fff', letterSpacing: '-0.05em' }}>
                  TC
                </Typography>
              </Box>
              <Typography
                variant="h6"
                sx={{
                  fontWeight: 800,
                  fontSize: '1.2rem',
                  color: '#fff',
                  letterSpacing: '-0.03em',
                  display: { xs: 'none', sm: 'block' },
                }}
              >
                Top<span style={{ color: '#a78bfa' }}>Connect</span>
              </Typography>
            </Box>

            {/* Desktop Nav */}
            {!isMobile && (
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5, flex: 1 }}>
                {visibleNavItems.map(item => (
                  <Button
                    key={item.path}
                    onClick={() => navigate(item.path)}
                    sx={{
                      color: isActive(item.path) ? '#fff' : 'rgba(255,255,255,0.55)',
                      fontWeight: isActive(item.path) ? 700 : 500,
                      fontSize: '0.85rem',
                      px: 2,
                      py: 0.8,
                      borderRadius: '10px',
                      position: 'relative',
                      transition: 'all 0.2s ease',
                      background: isActive(item.path)
                        ? 'linear-gradient(135deg, rgba(102,126,234,0.15) 0%, rgba(167,139,250,0.1) 100%)'
                        : 'transparent',
                      '&:hover': {
                        color: '#fff',
                        background: 'rgba(255,255,255,0.06)',
                      },
                      '&::after': isActive(item.path) ? {
                        content: '""',
                        position: 'absolute',
                        bottom: 0,
                        left: '50%',
                        transform: 'translateX(-50%)',
                        width: '60%',
                        height: '2px',
                        background: 'linear-gradient(90deg, #667eea, #a78bfa)',
                        borderRadius: '2px',
                      } : {},
                    }}
                  >
                    {item.label}
                  </Button>
                ))}
              </Box>
            )}

            {/* Right Side */}
            <Box sx={{ display: 'flex', alignItems: 'center', gap: { xs: 0.5, md: 1.5 } }}>
              {isAuthenticated ? (
                <>
                  {/* Notification Bell */}
                  <IconButton
                    onClick={() => navigate('/notifications')}
                    sx={{
                      color: isActive('/notifications') ? '#a78bfa' : 'rgba(255,255,255,0.5)',
                      '&:hover': { color: '#a78bfa', background: 'rgba(167,139,250,0.1)' },
                    }}
                  >
                    <Badge
                      badgeContent={unreadCount}
                      max={9}
                      sx={{
                        '& .MuiBadge-badge': {
                          background: 'linear-gradient(135deg, #f093fb, #f5576c)',
                          color: '#fff',
                          fontSize: '0.65rem',
                          fontWeight: 700,
                          minWidth: 18,
                          height: 18,
                        },
                      }}
                    >
                      <Typography sx={{ fontSize: '1.2rem' }}>🔔</Typography>
                    </Badge>
                  </IconButton>

                  {/* User Avatar */}
                  <Box
                    onClick={() => navigate('/portfolio')}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: 1,
                      cursor: 'pointer',
                      py: 0.5,
                      px: 1.5,
                      borderRadius: '12px',
                      border: '1px solid rgba(255,255,255,0.08)',
                      transition: 'all 0.2s',
                      '&:hover': {
                        background: 'rgba(255,255,255,0.05)',
                        borderColor: 'rgba(102,126,234,0.3)',
                      },
                    }}
                  >
                    <Avatar
                      sx={{
                        width: 30,
                        height: 30,
                        fontSize: '0.8rem',
                        fontWeight: 700,
                        background: user?.role === 'ARTIST'
                          ? 'linear-gradient(135deg, #667eea, #764ba2)'
                          : 'linear-gradient(135deg, #f093fb, #f5576c)',
                      }}
                    >
                      {user?.email?.charAt(0).toUpperCase()}
                    </Avatar>
                    {!isMobile && (
                      <Box>
                        <Typography sx={{ color: '#fff', fontSize: '0.78rem', fontWeight: 600, lineHeight: 1.2 }}>
                          {user?.email?.split('@')[0]}
                        </Typography>
                        <Typography sx={{ color: 'rgba(255,255,255,0.4)', fontSize: '0.65rem', fontWeight: 500 }}>
                          {user?.role === 'ARTIST' ? 'Artist' : 'Fan'}
                        </Typography>
                      </Box>
                    )}
                  </Box>

                  {/* Logout */}
                  <Button
                    size="small"
                    onClick={handleLogout}
                    sx={{
                      color: 'rgba(255,255,255,0.45)',
                      fontSize: '0.78rem',
                      px: { xs: 1, md: 2 },
                      py: 0.6,
                      borderRadius: '10px',
                      minWidth: 'auto',
                      '&:hover': {
                        color: '#f5576c',
                        background: 'rgba(245,87,108,0.1)',
                      },
                    }}
                  >
                    {isMobile ? '↪' : 'Logout'}
                  </Button>
                </>
              ) : (
                <>
                  <Button
                    onClick={() => navigate('/login')}
                    sx={{
                      color: 'rgba(255,255,255,0.7)',
                      fontWeight: 500,
                      fontSize: '0.85rem',
                      px: 2,
                      '&:hover': { color: '#fff', background: 'rgba(255,255,255,0.06)' },
                    }}
                  >
                    Log in
                  </Button>
                  <Button
                    onClick={() => navigate('/signup')}
                    sx={{
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      color: '#fff',
                      fontWeight: 600,
                      fontSize: '0.85rem',
                      px: 3,
                      py: 0.9,
                      borderRadius: '10px',
                      boxShadow: '0 2px 12px rgba(102,126,234,0.3)',
                      '&:hover': {
                        background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
                        boxShadow: '0 4px 20px rgba(102,126,234,0.5)',
                      },
                    }}
                  >
                    Get Started
                  </Button>
                </>
              )}

              {/* Mobile menu toggle */}
              {isMobile && isAuthenticated && (
                <IconButton
                  onClick={() => setMobileOpen(!mobileOpen)}
                  sx={{ color: 'rgba(255,255,255,0.6)', ml: 0.5 }}
                >
                  <Typography sx={{ fontSize: '1.3rem' }}>{mobileOpen ? '✕' : '☰'}</Typography>
                </IconButton>
              )}
            </Box>
          </Toolbar>
        </Container>

        {/* Mobile Drawer */}
        {isMobile && (
          <Drawer
            anchor="top"
            open={mobileOpen}
            onClose={() => setMobileOpen(false)}
            PaperProps={{
              sx: {
                mt: '64px',
                background: '#0a0a1a',
                borderBottom: '1px solid rgba(102,126,234,0.15)',
              },
            }}
          >
            <List sx={{ px: 2, py: 1 }}>
              {visibleNavItems.map(item => (
                <ListItem
                  key={item.path}
                  onClick={() => { navigate(item.path); setMobileOpen(false); }}
                  sx={{
                    borderRadius: '10px',
                    mb: 0.5,
                    cursor: 'pointer',
                    background: isActive(item.path) ? 'rgba(102,126,234,0.1)' : 'transparent',
                    '&:hover': { background: 'rgba(255,255,255,0.05)' },
                  }}
                >
                  <ListItemText
                    primary={item.label}
                    sx={{
                      '& .MuiListItemText-primary': {
                        color: isActive(item.path) ? '#a78bfa' : 'rgba(255,255,255,0.7)',
                        fontWeight: isActive(item.path) ? 700 : 500,
                        fontSize: '0.9rem',
                      },
                    }}
                  />
                </ListItem>
              ))}
            </List>
          </Drawer>
        )}
      </AppBar>

      <Box component="main" sx={{ flexGrow: 1 }}>
        {children}
      </Box>

      {/* Footer */}
      <Box
        component="footer"
        sx={{
          py: 4,
          px: 2,
          mt: 'auto',
          background: '#0a0a1a',
          borderTop: '1px solid rgba(102,126,234,0.08)',
          textAlign: 'center',
        }}
      >
        <Typography variant="body2" sx={{ color: 'rgba(255,255,255,0.35)', fontWeight: 400, fontSize: '0.8rem' }}>
          © 2026 TopConnect — Artist Booking Platform
        </Typography>
      </Box>
    </Box>
  );
}
