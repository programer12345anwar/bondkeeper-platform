import { NavLink } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import DashboardIcon from '@mui/icons-material/Dashboard';
import PeopleIcon from '@mui/icons-material/People';
import CategoryIcon from '@mui/icons-material/Category';
import FlagIcon from '@mui/icons-material/Flag';
import SettingsIcon from '@mui/icons-material/Settings';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import FavoriteIcon from '@mui/icons-material/Favorite';
import { setSidebarOpen } from '../../store/uiSlice';

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: DashboardIcon },
  { to: '/contacts', label: 'Contacts', icon: PeopleIcon },
  { to: '/categories', label: 'Categories', icon: CategoryIcon },
  { to: '/priorities', label: 'Priority Levels', icon: FlagIcon },
  { to: '/settings', label: 'Settings', icon: SettingsIcon },
];

export default function Sidebar() {
  const dispatch = useDispatch();
  const open = useSelector((state) => state.ui.sidebarOpen);

  return (
    <aside
      className={`fixed left-0 top-0 z-40 flex h-full flex-col border-r border-slate-200 bg-white transition-all duration-300 dark:border-slate-800 dark:bg-[#0f1117] ${
        open ? 'w-64' : 'w-[72px]'
      }`}
    >
      <div className="flex h-16 items-center gap-3 border-b border-slate-200 px-4 dark:border-slate-800">
        <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-xl bg-indigo-600 text-white">
          <FavoriteIcon fontSize="small" />
        </div>
        {open && (
          <div>
            <p className="font-bold text-slate-900 dark:text-white">BondKeeper</p>
            <p className="text-xs text-slate-500">Relationships</p>
          </div>
        )}
      </div>

      <nav className="flex-1 space-y-1 p-3">
        {navItems.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) =>
              `flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition ${
                isActive
                  ? 'bg-indigo-50 text-indigo-700 dark:bg-indigo-900/30 dark:text-indigo-300'
                  : 'text-slate-600 hover:bg-slate-100 dark:text-slate-400 dark:hover:bg-slate-800'
              }`
            }
            title={!open ? label : undefined}
          >
            <Icon fontSize="small" />
            {open && <span>{label}</span>}
          </NavLink>
        ))}
      </nav>

      <button
        type="button"
        onClick={() => dispatch(setSidebarOpen(!open))}
        className="m-3 flex items-center justify-center rounded-xl border border-slate-200 p-2 text-slate-500 hover:bg-slate-50 dark:border-slate-700 dark:hover:bg-slate-800"
        aria-label="Toggle sidebar"
      >
        <ChevronLeftIcon className={`transition ${open ? '' : 'rotate-180'}`} fontSize="small" />
      </button>
    </aside>
  );
}
