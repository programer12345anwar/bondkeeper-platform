import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Button, TextField, IconButton, CircularProgress, Chip } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import Navbar from '../../components/layout/Navbar';
import DataTable from '../../components/common/DataTable';
import Modal from '../../components/common/Modal';
import { TableSkeleton } from '../../components/common/LoadingSkeleton';
import { usePriorities } from '../../hooks/useQueries';
import { priorityApi } from '../../api';
import { getErrorMessage } from '../../api/axios';
import toast from 'react-hot-toast';

export default function PriorityLevels() {
  const queryClient = useQueryClient();
  const { data: priorities = [], isLoading } = usePriorities();
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ levelName: '', reminderFrequencyDays: 14, colorCode: '#6366f1' });

  const saveMutation = useMutation({
    mutationFn: (payload) =>
      editing ? priorityApi.update(editing.id, payload) : priorityApi.create(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['priorities'] });
      toast.success(editing ? 'Priority updated' : 'Priority created');
      setOpen(false);
      setEditing(null);
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  const deleteMutation = useMutation({
    mutationFn: (id) => priorityApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['priorities'] });
      toast.success('Priority deleted');
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  const columns = [
    {
      field: 'levelName',
      headerName: 'Level',
      renderCell: (r) => (
        <Chip label={r.levelName} size="small" sx={{ bgcolor: r.colorCode, color: '#fff' }} />
      ),
    },
    { field: 'reminderFrequencyDays', headerName: 'Check-in (days)' },
    { field: 'colorCode', headerName: 'Color' },
    {
      field: 'actions',
      headerName: 'Actions',
      renderCell: (r) => (
        <div className="flex gap-1">
          <IconButton size="small" onClick={() => { setEditing(r); setForm({ levelName: r.levelName, reminderFrequencyDays: r.reminderFrequencyDays, colorCode: r.colorCode }); setOpen(true); }}>
            <EditIcon fontSize="small" />
          </IconButton>
          <IconButton size="small" color="error" onClick={() => deleteMutation.mutate(r.id)}>
            <DeleteIcon fontSize="small" />
          </IconButton>
        </div>
      ),
    },
  ];

  return (
    <div>
      <Navbar title="Priority Levels" subtitle="Set reminder frequency per tier" />
      <main className="p-6">
        <div className="mb-6 flex justify-end">
          <Button variant="contained" startIcon={<AddIcon />} onClick={() => { setEditing(null); setForm({ levelName: '', reminderFrequencyDays: 14, colorCode: '#6366f1' }); setOpen(true); }}>
            Add Priority
          </Button>
        </div>
        {isLoading ? <TableSkeleton /> : <DataTable columns={columns} rows={priorities} />}

        <Modal
          open={open}
          onClose={() => setOpen(false)}
          title={editing ? 'Edit Priority' : 'New Priority'}
          actions={
            <>
              <Button onClick={() => setOpen(false)}>Cancel</Button>
              <Button variant="contained" disabled={!form.levelName || saveMutation.isPending} onClick={() => saveMutation.mutate(form)}>
                {saveMutation.isPending ? <CircularProgress size={20} /> : 'Save'}
              </Button>
            </>
          }
        >
          <div className="space-y-4 pt-2">
            <TextField fullWidth required label="Level name" value={form.levelName} onChange={(e) => setForm({ ...form, levelName: e.target.value })} />
            <TextField fullWidth type="number" label="Reminder frequency (days)" value={form.reminderFrequencyDays} onChange={(e) => setForm({ ...form, reminderFrequencyDays: Number(e.target.value) })} />
            <TextField fullWidth label="Color (hex)" value={form.colorCode} onChange={(e) => setForm({ ...form, colorCode: e.target.value })} helperText="e.g. #6366f1" />
          </div>
        </Modal>
      </main>
    </div>
  );
}
