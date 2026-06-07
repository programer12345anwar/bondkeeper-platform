import { createSlice } from '@reduxjs/toolkit';

const loadAuth = () => {
  try {
    const raw = localStorage.getItem('bondkeeper_auth');
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
};

const saved = loadAuth();

const initialState = {
  accessToken: saved?.accessToken ?? null,
  refreshToken: saved?.refreshToken ?? null,
  user: saved?.user ?? null,
  isAuthenticated: Boolean(saved?.accessToken),
};

const persist = (state) => {
  localStorage.setItem(
    'bondkeeper_auth',
    JSON.stringify({
      accessToken: state.accessToken,
      refreshToken: state.refreshToken,
      user: state.user,
    })
  );
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (state, action) => {
      state.accessToken = action.payload.accessToken;
      state.refreshToken = action.payload.refreshToken;
      state.user = action.payload.user;
      state.isAuthenticated = true;
      persist(state);
    },
    setTokens: (state, action) => {
      state.accessToken = action.payload.accessToken;
      state.refreshToken = action.payload.refreshToken;
      if (action.payload.user) state.user = action.payload.user;
      state.isAuthenticated = true;
      persist(state);
    },
    setUser: (state, action) => {
      state.user = action.payload;
      persist(state);
    },
    logout: (state) => {
      state.accessToken = null;
      state.refreshToken = null;
      state.user = null;
      state.isAuthenticated = false;
      localStorage.removeItem('bondkeeper_auth');
    },
  },
});

export const { setCredentials, setTokens, setUser, logout } = authSlice.actions;
export default authSlice.reducer;
