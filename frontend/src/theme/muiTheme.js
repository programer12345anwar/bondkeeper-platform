import { createTheme } from '@mui/material/styles';

export const getMuiTheme = (mode) =>
  createTheme({
    palette: {
      mode,
      primary: { main: '#6366f1', light: '#818cf8', dark: '#4f46e5' },
      secondary: { main: '#8b5cf6' },
      background: {
        default: mode === 'dark' ? '#0f1117' : '#f8fafc',
        paper: mode === 'dark' ? '#161922' : '#ffffff',
      },
    },
    typography: {
      fontFamily: '"Inter", ui-sans-serif, system-ui, sans-serif',
    },
    shape: { borderRadius: 12 },
    components: {
      MuiButton: {
        styleOverrides: {
          root: { textTransform: 'none', fontWeight: 600 },
        },
      },
      MuiCard: {
        styleOverrides: {
          root: {
            backgroundImage: 'none',
            border: mode === 'dark' ? '1px solid #2a2f3a' : '1px solid #e2e8f0',
          },
        },
      },
    },
  });
