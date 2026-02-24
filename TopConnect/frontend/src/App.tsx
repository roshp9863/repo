import React, { Suspense } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { SnackbarProvider } from 'notistack';
import { ThemeProvider, createTheme, CssBaseline, CircularProgress, Box, Typography } from '@mui/material';

import { AuthProvider, useAuth } from './context/AuthContext';
import Layout from './components/Layout';

// ─── Lazy-loaded pages (code-split per route) ───────
const LoginPage = React.lazy(() => import('./pages/LoginPage'));
const SignupPage = React.lazy(() => import('./pages/SignupPage'));
const VerifyEmailPage = React.lazy(() => import('./pages/VerifyEmailPage'));
const HomePage = React.lazy(() => import('./pages/HomePage'));
const ArtistListPage = React.lazy(() => import('./pages/ArtistListPage'));
const ArtistDetailPage = React.lazy(() => import('./pages/ArtistDetailPage'));
const CreateBookingPage = React.lazy(() => import('./pages/CreateBookingPage'));
const BookingPage = React.lazy(() => import('./pages/BookingPage'));
const ChatPage = React.lazy(() => import('./pages/ChatPage'));
const ChatListPage = React.lazy(() => import('./pages/ChatListPage'));
const BookingsPage = React.lazy(() => import('./pages/BookingsPage'));
const NotificationsPage = React.lazy(() => import('./pages/NotificationsPage'));
const ArtistDashboardPage = React.lazy(() => import('./pages/ArtistDashboardPage'));
const MyPortfolioPage = React.lazy(() => import('./pages/MyPortfolioPage'));
const ViewPortfolioPage = React.lazy(() => import('./pages/ViewPortfolioPage'));
const AdminDashboardPage = React.lazy(() => import('./pages/AdminDashboardPage'));

// ─── React Query defaults ───────────────────────────
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 2 * 60 * 1000,       // 2 minutes — reduces refetches
      retry: 2,                         // retry failed requests twice
      refetchOnWindowFocus: false,      // don't spam the server on tab switch
    },
    mutations: {
      retry: 1,
    },
  },
});

// ─── Loading Spinner for lazy-loaded routes ─────────
function PageLoader() {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '60vh', gap: 2 }}>
      <CircularProgress size={40} sx={{ color: '#667eea' }} />
      <Typography sx={{ color: '#64648b', fontSize: '0.9rem' }}>Loading...</Typography>
    </Box>
  );
}

// ─── Error Boundary ─────────────────────────────────
class ErrorBoundary extends React.Component<
  { children: React.ReactNode },
  { hasError: boolean; error: Error | null }
> {
  constructor(props: { children: React.ReactNode }) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error: Error) {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, info: React.ErrorInfo) {
    console.error('ErrorBoundary caught:', error, info);
  }

  render() {
    if (this.state.hasError) {
      return (
        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '60vh', gap: 3, px: 3 }}>
          <Typography sx={{ fontSize: '3rem' }}>😵</Typography>
          <Typography variant="h5" sx={{ fontWeight: 700, color: '#1a1a2e' }}>
            Something went wrong
          </Typography>
          <Typography sx={{ color: '#64648b', textAlign: 'center', maxWidth: 450 }}>
            An unexpected error occurred. Please refresh the page or try again later.
          </Typography>
          <Box
            component="button"
            onClick={() => { this.setState({ hasError: false, error: null }); window.location.reload(); }}
            sx={{
              px: 4, py: 1.5, borderRadius: '12px', border: 'none', cursor: 'pointer',
              background: 'linear-gradient(135deg, #667eea, #764ba2)', color: '#fff',
              fontWeight: 600, fontSize: '0.95rem',
              '&:hover': { opacity: 0.9 },
            }}
          >
            Refresh Page
          </Box>
        </Box>
      );
    }
    return this.props.children;
  }
}

const theme = createTheme({
  palette: {
    primary: {
      main: '#667eea',
      light: '#8b9cf7',
      dark: '#4c5fd5',
      contrastText: '#ffffff',
    },
    secondary: {
      main: '#f093fb',
      light: '#f5b5fc',
      dark: '#d06ee0',
      contrastText: '#ffffff',
    },
    background: {
      default: '#f8f9fe',
      paper: '#ffffff',
    },
    text: {
      primary: '#1a1a2e',
      secondary: '#64648b',
    },
    error: {
      main: '#f5576c',
    },
    warning: {
      main: '#fda085',
    },
    success: {
      main: '#38b2ac',
    },
    info: {
      main: '#667eea',
    },
  },
  typography: {
    fontFamily: '"Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
    h1: { fontWeight: 800, letterSpacing: '-0.02em' },
    h2: { fontWeight: 800, letterSpacing: '-0.02em' },
    h3: { fontWeight: 700, letterSpacing: '-0.01em' },
    h4: { fontWeight: 700, letterSpacing: '-0.01em' },
    h5: { fontWeight: 600 },
    h6: { fontWeight: 600 },
    subtitle1: { fontWeight: 500, color: '#64648b' },
    body1: { lineHeight: 1.7 },
    body2: { lineHeight: 1.6 },
    button: { fontWeight: 600, textTransform: 'none' as const, letterSpacing: '0.01em' },
  },
  shape: {
    borderRadius: 14,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          padding: '10px 24px',
          fontSize: '0.938rem',
          boxShadow: 'none',
          '&:hover': {
            boxShadow: '0 4px 14px rgba(102, 126, 234, 0.4)',
          },
        },
        contained: {
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          '&:hover': {
            background: 'linear-gradient(135deg, #764ba2 0%, #667eea 100%)',
            boxShadow: '0 6px 20px rgba(102, 126, 234, 0.45)',
          },
        },
        outlined: {
          borderWidth: 2,
          '&:hover': {
            borderWidth: 2,
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 16,
          boxShadow: '0 4px 24px rgba(0, 0, 0, 0.06)',
          border: '1px solid rgba(0, 0, 0, 0.04)',
          transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
          '&:hover': {
            boxShadow: '0 12px 40px rgba(0, 0, 0, 0.1)',
            transform: 'translateY(-4px)',
          },
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          borderRadius: 16,
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 12,
            '&:hover .MuiOutlinedInput-notchedOutline': {
              borderColor: '#667eea',
            },
            '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
              borderColor: '#667eea',
              borderWidth: 2,
            },
          },
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          fontWeight: 500,
        },
      },
    },
    MuiAlert: {
      styleOverrides: {
        root: {
          borderRadius: 12,
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          boxShadow: 'none',
        },
      },
    },
  },
});

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRole?: string;
}

function ProtectedRoute({ children, requiredRole }: ProtectedRouteProps) {
  const { isAuthenticated, user } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && user?.role !== requiredRole) {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <SnackbarProvider maxSnack={3}>
          <Router>
            <AuthProvider>
              <Layout>
                <ErrorBoundary>
                  <Suspense fallback={<PageLoader />}>
                    <Routes>
                  {/* Public Routes */}
                  <Route path="/" element={<HomePage />} />
                  <Route path="/login" element={<LoginPage />} />
                  <Route path="/signup" element={<SignupPage />} />
                  <Route path="/verify-email" element={<VerifyEmailPage />} />
                  <Route path="/artists" element={<ArtistListPage />} />
                  <Route path="/artists/:id" element={<ArtistDetailPage />} />

                  {/* Protected Routes */}
                  <Route
                    path="/book/:offeringId"
                    element={
                      <ProtectedRoute>
                        <CreateBookingPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/bookings"
                    element={
                      <ProtectedRoute>
                        <BookingsPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/booking/:id"
                    element={
                      <ProtectedRoute>
                        <BookingPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/chats"
                    element={
                      <ProtectedRoute>
                        <ChatListPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/chat/:threadId"
                    element={
                      <ProtectedRoute>
                        <ChatPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/notifications"
                    element={
                      <ProtectedRoute>
                        <NotificationsPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/dashboard/services"
                    element={
                      <ProtectedRoute requiredRole="ARTIST">
                        <ArtistDashboardPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/portfolio"
                    element={
                      <ProtectedRoute>
                        <MyPortfolioPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/portfolio/view/:userId"
                    element={
                      <ProtectedRoute>
                        <ViewPortfolioPage />
                      </ProtectedRoute>
                    }
                  />
                  <Route
                    path="/admin"
                    element={
                      <ProtectedRoute requiredRole="ADMIN">
                        <AdminDashboardPage />
                      </ProtectedRoute>
                    }
                  />

                  {/* Catch all */}
                  <Route path="*" element={<Navigate to="/" replace />} />
                    </Routes>
                  </Suspense>
                </ErrorBoundary>
              </Layout>
            </AuthProvider>
          </Router>
        </SnackbarProvider>
      </ThemeProvider>
    </QueryClientProvider>
  );
}

export default App;
