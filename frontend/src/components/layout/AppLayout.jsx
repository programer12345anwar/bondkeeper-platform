import { Outlet } from 'react-router-dom';
import { useSelector } from 'react-redux';
import Sidebar from './Sidebar';

export default function AppLayout() {
  const sidebarOpen = useSelector((state) => state.ui.sidebarOpen);

  return (
    <div className="min-h-full">
      <Sidebar />
      <div
        className={`min-h-screen transition-all duration-300 ${
          sidebarOpen ? 'pl-64' : 'pl-[72px]'
        } max-lg:pl-0`}
      >
        <Outlet />
      </div>
    </div>
  );
}
