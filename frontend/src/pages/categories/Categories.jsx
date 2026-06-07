import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Button, TextField, IconButton, CircularProgress } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import Navbar from '../../components/layout/Navbar';
import DataTable from '../../components/common/DataTable';
import Modal from '../../components/common/Modal';
import { TableSkeleton } from '../../components/common/LoadingSkeleton';
import { useCategories } from '../../hooks/useQueries';
import { categoryApi } from '../../api';
import { getErrorMessage } from '../../api/axios';
import toast from 'react-hot-toast';

export default function Categories() {
  const queryClient = useQueryClient();
  const { data: categories = [], isLoading } = useCategories();
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ name: '', description: '' });

  const saveMutation = useMutation({
    mutationFn: (payload) =>
      editing ? categoryApi.update(editing.id, payload) : categoryApi.create(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] });
      toast.success(editing ? 'Category updated' : 'Category created');
      setOpen(false);
      setEditing(null);
      setForm({ name: '', description: '' });
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  const deleteMutation = useMutation({
    mutationFn: (id) => categoryApi.delete(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] });
      toast.success('Category deleted');
    },
    onError: (err) => toast.error(getErrorMessage(err)),
  });

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', description: '' });
    setOpen(true);
  };

  const openEdit = (row) => {
    setEditing(row);
    setForm({ name: row.name, description: row.description ?? '' });
    setOpen(true);
  };

  const columns = [
    { field: 'name', headerName: 'Name' },
    { field: 'description', headerName: 'Description', renderCell: (r) => r.description || '—' },
    {
      field: 'actions',
      headerName: 'Actions',
      renderCell: (r) => (
        <div className="flex gap-1" onClick={(e) => e.stopPropagation()}>
          <IconButton size="small" onClick={() => openEdit(r)}><EditIcon fontSize="small" /></IconButton>
          <IconButton size="small" color="error" onClick={() => deleteMutation.mutate(r.id)}><DeleteIcon fontSize="small" /></IconButton>
        </div>
      ),
    },
  ];

  return (
    <div>
      <Navbar title="Categories" subtitle="Organize your contacts" />
      <main className="p-6">
        <div className="mb-6 flex justify-end">
          <Button variant="contained" startIcon={<AddIcon />} onClick={openCreate}>Add Category</Button>
        </div>
        {isLoading ? <TableSkeleton /> : <DataTable columns={columns} rows={categories} />}

        <Modal
          open={open}
          onClose={() => setOpen(false)}
          title={editing ? 'Edit Category' : 'New Category'}
          actions={
            <>
              <Button onClick={() => setOpen(false)}>Cancel</Button>
              <Button variant="contained" disabled={!form.name || saveMutation.isPending} onClick={() => saveMutation.mutate(form)}>
                {saveMutation.isPending ? <CircularProgress size={20} /> : 'Save'}
              </Button>
            </>
          }
        >
          <div className="space-y-4 pt-2">
            <TextField fullWidth required label="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            <TextField fullWidth multiline rows={2} label="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          </div>
        </Modal>
      </main>
    </div>
  );
}
