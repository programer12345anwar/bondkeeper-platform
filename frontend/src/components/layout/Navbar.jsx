import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { IconButton, Avatar, Menu, MenuItem, Divider } from '@mui/material';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import LightModeIcon from '@mui/icons-material/LightMode';
import LogoutIcon from '@mui/icons-material/Logout';
import MenuIcon from '@mui/icons-material/Menu';
import { useState } from 'react';
import { toggleTheme, setSidebarOpen } from '../../store/uiSlice';
import { logout } from '../../store/authSlice';
import { authApi } from '../../api';
import toast from 'react-hot-toast';

export default function Navbar({ title, subtitle }) {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const theme = useSelector((state) => state.ui.theme);
  const sidebarOpen = useSelector((state) => state.ui.sidebarOpen);
  const user = useSelector((state) => state.auth.user);
  const [anchorEl, setAnchorEl] = useState(null);

  const handleLogout = async () => {
    try {
      await authApi.logout();
    } catch {
      /* ignore */
    }
    dispatch(logout());
    toast.success('Logged out');
    navigate('/login');
  };

  const initials = user
    ? `${user.firstName?.[0] ?? ''}${user.lastName?.[0] ?? ''}`.toUpperCase()
    : '?';

  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between border-b border-slate-200 bg-white/80 px-6 backdrop-blur dark:border-slate-800 dark:bg-[#0f1117]/80">
      <div className="flex items-center gap-3">
        {!sidebarOpen && (
          <IconButton size="small" onClick={() => dispatch(setSidebarOpen(true))} className="lg:hidden">
            <MenuIcon />
          </IconButton>
        )}
        <div>
          <h1 className="text-lg font-semibold text-slate-900 dark:text-white">{title}</h1>
          {subtitle && <p className="text-sm text-slate-500 dark:text-slate-400">{subtitle}</p>}
        </div>
      </div>

      <div className="flex items-center gap-2">
        <IconButton onClick={() => dispatch(toggleTheme())} aria-label="Toggle theme">
          {theme === 'dark' ? <LightModeIcon /> : <DarkModeIcon />}
        </IconButton>

        <IconButton onClick={(e) => setAnchorEl(e.currentTarget)}>
          <Avatar sx={{ width: 36, height: 36, bgcolor: '#6366f1', fontSize: 14 }}>{initials}</Avatar>
        </IconButton>

        <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={() => setAnchorEl(null)}>
          <MenuItem disabled className="text-sm opacity-100">
            {user?.firstName} {user?.lastName}
            <br />
            <span className="text-xs text-slate-500">{user?.email}</span>
          </MenuItem>
          <Divider />
          <MenuItem onClick={() => { setAnchorEl(null); navigate('/settings'); }}>
            Settings
          </MenuItem>
          <MenuItem onClick={handleLogout}>
            <LogoutIcon fontSize="small" className="mr-2" /> Logout
          </MenuItem>
        </Menu>
      </div>
    </header>
  );
}
