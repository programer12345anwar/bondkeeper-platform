import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { Button, TextField, Alert, CircularProgress } from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import { authApi } from '../../api';
import { getErrorMessage } from '../../api/axios';
import { setCredentials } from '../../store/authSlice';
import toast from 'react-hot-toast';

export default function Register() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const data = await authApi.register(form);
      dispatch(
        setCredentials({
          accessToken: data.accessToken,
          refreshToken: data.refreshToken,
          user: data.user,
        })
      );
      toast.success('Account created!');
      navigate('/dashboard');
    } catch (err) {
      const msg = getErrorMessage(err);
      setError(msg);
      toast.error(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-full items-center justify-center bg-gradient-to-br from-slate-50 via-indigo-50/30 to-violet-50 p-6 dark:from-[#0f1117] dark:via-indigo-950/20 dark:to-[#0f1117]">
      <div className="w-full max-w-md rounded-3xl border border-slate-200 bg-white p-8 shadow-xl dark:border-slate-700 dark:bg-[#161922]">
        <div className="mb-8 text-center">
          <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-indigo-600 text-white">
            <FavoriteIcon />
          </div>
          <h1 className="text-2xl font-bold">Create account</h1>
          <p className="mt-2 text-slate-500">Start nurturing your relationships</p>
        </div>

        {error && <Alert severity="error" className="mb-4">{error}</Alert>}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-3">
            <TextField label="First name" required value={form.firstName} onChange={(e) => setForm({ ...form, firstName: e.target.value })} />
            <TextField label="Last name" required value={form.lastName} onChange={(e) => setForm({ ...form, lastName: e.target.value })} />
          </div>
          <TextField fullWidth label="Email" type="email" required value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          <TextField fullWidth label="Password" type="password" required helperText="Min 8 characters" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
          <Button fullWidth type="submit" variant="contained" size="large" disabled={loading}>
            {loading ? <CircularProgress size={24} color="inherit" /> : 'Create account'}
          </Button>
        </form>

        <p className="mt-6 text-center text-sm text-slate-500">
          Already have an account?{' '}
          <Link to="/login" className="font-semibold text-indigo-600 hover:underline">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
