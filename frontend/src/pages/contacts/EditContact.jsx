import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Button, CircularProgress } from '@mui/material';
import Navbar from '../../components/layout/Navbar';
import ContactForm, { toContactPayload } from './ContactForm';
import { useCategories, usePriorities, useContact } from '../../hooks/useQueries';
import { contactApi } from '../../api';
import { getErrorMessage } from '../../api/axios';
import toast from 'react-hot-toast';
import { PageHeaderSkeleton } from '../../components/common/LoadingSkeleton';

export default function EditContact() {
  const { id } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { data: contact, isLoading } = useContact(id);
  const { data: categories = [] } = useCategories();
  const { data: priorities = [] } = usePriorities();
  const [form, setForm] = useState(null);

  useEffect(() => {
    if (contact) {
      setForm({
        name: contact.name,
        phoneNumber: contact.phoneNumber ?? '',
        whatsappNumber: contact.whatsappNumber ?? '',
        notes: contact.notes ?? '',
        relationshipType: contact.relationshipType,
        relationshipScore: contact.relationshipScore,
        innerCircle: contact.innerCircle,
        categoryId: contact.categoryId,
        priorityLevelId: contact.priorityLevelId,
      });
    }
  }, [contact]);

  const mutation = useMutation({
    mutationFn: (payload) => contactApi.update(id, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['contacts'] });
      toast.success('Contact updated');
      navigate(`/contacts/${id}`);
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  if (isLoading || !form) {
    return (
      <div>
        <Navbar title="Edit Contact" />
        <main className="p-6"><PageHeaderSkeleton /></main>
      </div>
    );
  }

  return (
    <div>
      <Navbar title="Edit Contact" subtitle={contact.name} />
      <main className="mx-auto max-w-2xl p-6">
        <div className="rounded-2xl border border-slate-200 bg-white p-6 dark:border-slate-700 dark:bg-[#161922]">
          <ContactForm form={form} onChange={setForm} categories={categories} priorities={priorities} />
          <div className="mt-6 flex gap-3">
            <Button variant="outlined" onClick={() => navigate(`/contacts/${id}`)}>Cancel</Button>
            <Button variant="contained" disabled={mutation.isPending} onClick={() => mutation.mutate(toContactPayload(form))}>
              {mutation.isPending ? <CircularProgress size={22} /> : 'Save Changes'}
            </Button>
          </div>
        </div>
      </main>
    </div>
  );
}
