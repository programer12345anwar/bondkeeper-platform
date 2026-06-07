import { createSlice } from '@reduxjs/toolkit';

const savedTheme = localStorage.getItem('bondkeeper_theme') || 'light';

const uiSlice = createSlice({
  name: 'ui',
  initialState: {
    theme: savedTheme,
    sidebarOpen: true,
  },
  reducers: {
    toggleTheme: (state) => {
      state.theme = state.theme === 'light' ? 'dark' : 'light';
      localStorage.setItem('bondkeeper_theme', state.theme);
    },
    setTheme: (state, action) => {
      state.theme = action.payload;
      localStorage.setItem('bondkeeper_theme', state.theme);
    },
    toggleSidebar: (state) => {
      state.sidebarOpen = !state.sidebarOpen;
    },
    setSidebarOpen: (state, action) => {
      state.sidebarOpen = action.payload;
    },
  },
});

export const { toggleTheme, setTheme, toggleSidebar, setSidebarOpen } = uiSlice.actions;
export default uiSlice.reducer;
