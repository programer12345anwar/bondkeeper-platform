import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Button, TextField, CircularProgress } from '@mui/material';
import Navbar from '../../components/layout/Navbar';
import { useProfile } from '../../hooks/useQueries';
import { userApi } from '../../api';
import { setUser } from '../../store/authSlice';
import { getErrorMessage } from '../../api/axios';
import toast from 'react-hot-toast';
import { PageHeaderSkeleton } from '../../components/common/LoadingSkeleton';

export default function Profile() {
  const dispatch = useDispatch();
  const queryClient = useQueryClient();
  const { data: profile, isLoading } = useProfile();
  const [form, setForm] = useState({ firstName: '', lastName: '', email: '', password: '' });

  useEffect(() => {
    if (profile) {
      setForm({ firstName: profile.firstName, lastName: profile.lastName, email: profile.email, password: '' });
    }
  }, [profile]);

  const mutation = useMutation({
    mutationFn: (payload) => {
      const body = { ...payload };
      if (!body.password) delete body.password;
      return userApi.updateProfile(body);
    },
    onSuccess: (data) => {
      dispatch(setUser(data));
      queryClient.invalidateQueries({ queryKey: ['profile'] });
      toast.success('Profile updated');
      setForm((f) => ({ ...f, password: '' }));
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  if (isLoading) {
    return (
      <div>
        <Navbar title="Settings" />
        <main className="p-6"><PageHeaderSkeleton /></main>
      </div>
    );
  }

  return (
    <div>
      <Navbar title="Settings" subtitle="Manage your profile" />
      <main className="mx-auto max-w-lg p-6">
        <div className="rounded-2xl border border-slate-200 bg-white p-6 dark:border-slate-700 dark:bg-[#161922]">
          <h2 className="mb-6 text-lg font-semibold">User Profile</h2>
          <div className="space-y-4">
            <TextField fullWidth label="First name" value={form.firstName} onChange={(e) => setForm({ ...form, firstName: e.target.value })} />
            <TextField fullWidth label="Last name" value={form.lastName} onChange={(e) => setForm({ ...form, lastName: e.target.value })} />
            <TextField fullWidth label="Email" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
            <TextField fullWidth label="New password" type="password" helperText="Leave blank to keep current" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
          </div>
          <Button className="mt-6" variant="contained" disabled={mutation.isPending} onClick={() => mutation.mutate(form)}>
            {mutation.isPending ? <CircularProgress size={22} /> : 'Save Profile'}
          </Button>
        </div>
      </main>
    </div>
  );
}
