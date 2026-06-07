import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Button, CircularProgress } from '@mui/material';
import Navbar from '../../components/layout/Navbar';
import ContactForm, { toContactPayload } from './ContactForm';
import { useCategories, usePriorities } from '../../hooks/useQueries';
import { contactApi } from '../../api';
import { getErrorMessage } from '../../api/axios';
import toast from 'react-hot-toast';

export default function AddContact() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { data: categories = [] } = useCategories();
  const { data: priorities = [] } = usePriorities();
  const [form, setForm] = useState({});

  const mutation = useMutation({
    mutationFn: (payload) => contactApi.create(payload),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['contacts'] });
      toast.success('Contact created');
      navigate(`/contacts/${data.id}`);
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  return (
    <div>
      <Navbar title="Add Contact" subtitle="Create a new relationship" />
      <main className="mx-auto max-w-2xl p-6">
        <div className="rounded-2xl border border-slate-200 bg-white p-6 dark:border-slate-700 dark:bg-[#161922]">
          <ContactForm form={form} onChange={setForm} categories={categories} priorities={priorities} />
          <div className="mt-6 flex gap-3">
            <Button variant="outlined" onClick={() => navigate('/contacts')}>Cancel</Button>
            <Button
              variant="contained"
              disabled={!form.name || mutation.isPending}
              onClick={() => mutation.mutate(toContactPayload(form))}
            >
              {mutation.isPending ? <CircularProgress size={22} /> : 'Create Contact'}
            </Button>
          </div>
        </div>
      </main>
    </div>
  );
}
