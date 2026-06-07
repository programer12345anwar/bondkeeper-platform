import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Provider, useSelector } from 'react-redux';
import { ThemeProvider, CssBaseline } from '@mui/material';
import { Toaster } from 'react-hot-toast';
import { store } from '../store';
import { getMuiTheme } from '../theme/muiTheme';
import { useEffect } from 'react';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 30_000,
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

function ThemeWrapper({ children }) {
  const theme = useSelector((state) => state.ui.theme);

  useEffect(() => {
    document.documentElement.classList.toggle('dark', theme === 'dark');
  }, [theme]);

  return (
    <ThemeProvider theme={getMuiTheme(theme)}>
      <CssBaseline />
      {children}
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 4000,
          style: {
            borderRadius: '12px',
            background: theme === 'dark' ? '#161922' : '#fff',
            color: theme === 'dark' ? '#f1f5f9' : '#0f172a',
            border: theme === 'dark' ? '1px solid #2a2f3a' : '1px solid #e2e8f0',
          },
        }}
      />
    </ThemeProvider>
  );
}

export default function AppProviders({ children }) {
  return (
    <Provider store={store}>
      <QueryClientProvider client={queryClient}>
        <ThemeWrapper>{children}</ThemeWrapper>
      </QueryClientProvider>
    </Provider>
  );
}
