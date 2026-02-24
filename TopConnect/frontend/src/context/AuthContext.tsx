import React, { createContext, useContext, useState, useCallback, useEffect } from 'react';
import { authAPI } from '../services/apiClient';

interface User {
  id: string;
  email: string;
  role: 'ADMIN' | 'ARTIST' | 'USER';
  status: string;
}

interface AuthContextType {
  user: User | null;
  accessToken: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  signup: (email: string, password: string, role: string, displayName?: string, instagramUrl?: string, youtubeUrl?: string, certificateInfo?: string) => Promise<void>;
  verifyEmail: (token: string) => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(
    localStorage.getItem('accessToken')
  );
  const [refreshToken, setRefreshToken] = useState<string | null>(
    localStorage.getItem('refreshToken')
  );
  const [isLoading, setIsLoading] = useState(true);

  const logout = useCallback(() => {
    setUser(null);
    setAccessToken(null);
    setRefreshToken(null);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }, []);

  // Restore user session on mount
  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token && !user) {
      authAPI.me()
        .then((res) => {
          if (res.data?.data) {
            setUser(res.data.data);
          } else {
            logout();
          }
        })
        .catch((err) => {
          console.error('Failed to restore user session:', err);
          logout();
        })
        .finally(() => {
          setIsLoading(false);
        });
    } else {
      setIsLoading(false);
    }
  }, []);

  const login = useCallback(async (email: string, password: string) => {
    const response = await authAPI.login(email, password);
    const data = response.data;
    setAccessToken(data.data.accessToken);
    setRefreshToken(data.data.refreshToken);
    setUser(data.data.user);
    localStorage.setItem('accessToken', data.data.accessToken);
    localStorage.setItem('refreshToken', data.data.refreshToken);
  }, []);

  const signup = useCallback(
    async (email: string, password: string, role: string, displayName?: string,
           instagramUrl?: string, youtubeUrl?: string, certificateInfo?: string) => {
      const response = await authAPI.signup(email, password, role, displayName, instagramUrl, youtubeUrl, certificateInfo);
      if (response.status >= 400) {
        throw new Error(response.data?.message || 'Signup failed');
      }
    },
    []
  );

  const verifyEmail = useCallback(async (token: string) => {
    const response = await authAPI.verifyEmail(token);
    if (response.status >= 400) {
      throw new Error(response.data?.message || 'Verification failed');
    }
  }, []);

  const value = {
    user,
    accessToken,
    refreshToken,
    isAuthenticated: !!user && !!accessToken,
    isLoading,
    login,
    logout,
    signup,
    verifyEmail,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}