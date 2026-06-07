import { useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import {
  Button,
  Chip,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  CircularProgress,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import RefreshIcon from '@mui/icons-material/Refresh';
import Navbar from '../../components/layout/Navbar';
import DataTable from '../../components/common/DataTable';
import Modal from '../../components/common/Modal';
import { PageHeaderSkeleton } from '../../components/common/LoadingSkeleton';
import { useContact, useCategories, usePriorities, useInteractions } from '../../hooks/useQueries';
import { contactApi, interactionApi } from '../../api';
import { formatDate, scoreColor, scoreLabel, INTERACTION_TYPES } from '../../utils/contactHealth';
import { getErrorMessage } from '../../api/axios';
import toast from 'react-hot-toast';

export default function ContactDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { data: contact, isLoading } = useContact(id);
  const { data: categories = [] } = useCategories();
  const { data: priorities = [] } = usePriorities();
  const { data: interactions = [], refetch: refetchInteractions } = useInteractions(id);

  const [interactionOpen, setInteractionOpen] = useState(false);
  const [interactionForm, setInteractionForm] = useState({
    interactionType: 'CALL',
    interactionDate: new Date().toISOString().slice(0, 10),
    notes: '',
  });

  const category = categories.find((c) => c.id === contact?.categoryId);
  const priority = priorities.find((p) => p.id === contact?.priorityLevelId);

  const deleteMutation = useMutation({
    mutationFn: () => contactApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['contacts'] });
      toast.success('Contact deleted');
      navigate('/contacts');
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  const interactionMutation = useMutation({
    mutationFn: (payload) => interactionApi.create({ ...payload, contactId: Number(id) }),
    onSuccess: (data) => {
      refetchInteractions();
      queryClient.invalidateQueries({ queryKey: ['contacts'] });
      queryClient.invalidateQueries({ queryKey: ['contacts', id] });
      toast.success(`Interaction logged · Score: ${data.updatedRelationshipScore ?? 'updated'}`);
      setInteractionOpen(false);
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  const recalcMutation = useMutation({
    mutationFn: () => interactionApi.recalculateScore(id),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['contacts'] });
      queryClient.invalidateQueries({ queryKey: ['contacts', id] });
      toast.success(`Score updated: ${data.relationshipScore}`);
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  if (isLoading || !contact) {
    return (
      <div>
        <Navbar title="Contact" />
        <main className="p-6"><PageHeaderSkeleton /></main>
      </div>
    );
  }

  const score = contact.relationshipScore ?? 0;

  const interactionColumns = [
    { field: 'interactionType', headerName: 'Type', renderCell: (r) => r.interactionType?.replace('_', ' ') },
    { field: 'interactionDate', headerName: 'Date', renderCell: (r) => formatDate(r.interactionDate) },
    { field: 'notes', headerName: 'Notes', renderCell: (r) => r.notes || '—' },
  ];

  return (
    <div>
      <Navbar title={contact.name} subtitle={contact.relationshipType?.replace('_', ' ')} />
      <main className="p-6">
        <div className="mb-6 flex flex-wrap gap-3">
          <Button component={Link} to={`/contacts/${id}/edit`} variant="outlined" startIcon={<EditIcon />}>
            Edit
          </Button>
          <Button variant="outlined" startIcon={<RefreshIcon />} onClick={() => recalcMutation.mutate()} disabled={recalcMutation.isPending}>
            Recalculate Score
          </Button>
          <Button variant="contained" onClick={() => setInteractionOpen(true)}>Log Interaction</Button>
          <Button color="error" variant="outlined" startIcon={<DeleteIcon />} onClick={() => deleteMutation.mutate()} disabled={deleteMutation.isPending}>
            Delete
          </Button>
        </div>

        <div className="grid gap-6 lg:grid-cols-3">
          <div className="rounded-2xl border border-slate-200 bg-white p-6 dark:border-slate-700 dark:bg-[#161922] lg:col-span-1">
            <div className="flex flex-col items-center text-center">
              <div className="flex h-24 w-24 items-center justify-center rounded-2xl text-3xl font-bold text-white" style={{ backgroundColor: scoreColor(score) }}>
                {score}
              </div>
              <p className="mt-3 font-semibold">{scoreLabel(score)}</p>
              {contact.innerCircle && <Chip label="Inner Circle" size="small" color="warning" className="mt-2" />}
            </div>
            <dl className="mt-6 space-y-3 text-sm">
              <div><dt className="text-slate-500">Phone</dt><dd>{contact.phoneNumber || '—'}</dd></div>
              <div><dt className="text-slate-500">WhatsApp</dt><dd>{contact.whatsappNumber || '—'}</dd></div>
              <div><dt className="text-slate-500">Category</dt><dd>{category?.name || '—'}</dd></div>
              <div><dt className="text-slate-500">Priority</dt><dd>{priority ? <Chip label={priority.levelName} size="small" sx={{ bgcolor: priority.colorCode, color: '#fff' }} /> : '—'}</dd></div>
              <div><dt className="text-slate-500">Last interaction</dt><dd>{formatDate(contact.lastInteractionDate)}</dd></div>
            </dl>
            {contact.notes && (
              <div className="mt-4 rounded-xl bg-slate-50 p-4 text-sm dark:bg-slate-800/50">
                <p className="font-medium text-slate-500">Notes</p>
                <p className="mt-1">{contact.notes}</p>
              </div>
            )}
          </div>

          <div className="lg:col-span-2">
            <h2 className="mb-4 text-lg font-semibold">Interaction History</h2>
            <DataTable columns={interactionColumns} rows={interactions} emptyMessage="No interactions yet" />
          </div>
        </div>

        <Modal
          open={interactionOpen}
          onClose={() => setInteractionOpen(false)}
          title="Log Interaction"
          actions={
            <>
              <Button onClick={() => setInteractionOpen(false)}>Cancel</Button>
              <Button variant="contained" disabled={interactionMutation.isPending} onClick={() => interactionMutation.mutate(interactionForm)}>
                {interactionMutation.isPending ? <CircularProgress size={20} /> : 'Save'}
              </Button>
            </>
          }
        >
          <div className="space-y-4 pt-2">
            <FormControl fullWidth>
              <InputLabel>Type</InputLabel>
              <Select label="Type" value={interactionForm.interactionType} onChange={(e) => setInteractionForm({ ...interactionForm, interactionType: e.target.value })}>
                {INTERACTION_TYPES.map((t) => <MenuItem key={t} value={t}>{t.replace('_', ' ')}</MenuItem>)}
              </Select>
            </FormControl>
            <TextField fullWidth type="date" label="Date" InputLabelProps={{ shrink: true }} value={interactionForm.interactionDate} onChange={(e) => setInteractionForm({ ...interactionForm, interactionDate: e.target.value })} />
            <TextField fullWidth multiline rows={3} label="Notes" value={interactionForm.notes} onChange={(e) => setInteractionForm({ ...interactionForm, notes: e.target.value })} />
          </div>
        </Modal>
      </main>
    </div>
  );
}
