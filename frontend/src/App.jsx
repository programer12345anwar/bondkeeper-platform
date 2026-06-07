import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import AppProviders from './providers/AppProviders';
import AppLayout from './components/layout/AppLayout';
import ProtectedRoute, { GuestRoute } from './components/common/ProtectedRoute';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import Dashboard from './pages/Dashboard';
import ContactList from './pages/contacts/ContactList';
import ContactDetail from './pages/contacts/ContactDetail';
import AddContact from './pages/contacts/AddContact';
import EditContact from './pages/contacts/EditContact';
import Categories from './pages/categories/Categories';
import PriorityLevels from './pages/priorities/PriorityLevels';
import Profile from './pages/settings/Profile';

export default function App() {
  return (
    <AppProviders>
      <BrowserRouter>
        <Routes>
          <Route element={<GuestRoute />}>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Route>

          <Route element={<ProtectedRoute />}>
            <Route element={<AppLayout />}>
              <Route index element={<Navigate to="/dashboard" replace />} />
              <Route path="/dashboard" element={<Dashboard />} />
              <Route path="/contacts" element={<ContactList />} />
              <Route path="/contacts/new" element={<AddContact />} />
              <Route path="/contacts/:id" element={<ContactDetail />} />
              <Route path="/contacts/:id/edit" element={<EditContact />} />
              <Route path="/categories" element={<Categories />} />
              <Route path="/priorities" element={<PriorityLevels />} />
              <Route path="/settings" element={<Profile />} />
            </Route>
          </Route>

          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AppProviders>
  );
}
