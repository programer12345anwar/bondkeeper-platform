import {
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormControlLabel,
  Switch,
} from '@mui/material';
import { RELATIONSHIP_TYPES } from '../../utils/contactHealth';

const defaultValues = {
  name: '',
  phoneNumber: '',
  whatsappNumber: '',
  notes: '',
  relationshipType: 'FRIEND',
  relationshipScore: 50,
  innerCircle: false,
  categoryId: '',
  priorityLevelId: '',
};

export default function ContactForm({ form, onChange, categories = [], priorities = [] }) {
  const values = { ...defaultValues, ...form };

  const set = (field, value) => onChange({ ...values, [field]: value });

  return (
    <div className="grid gap-4 sm:grid-cols-2">
      <TextField className="sm:col-span-2" fullWidth required label="Name" value={values.name} onChange={(e) => set('name', e.target.value)} />
      <TextField fullWidth label="Phone" value={values.phoneNumber} onChange={(e) => set('phoneNumber', e.target.value)} />
      <TextField fullWidth label="WhatsApp" value={values.whatsappNumber} onChange={(e) => set('whatsappNumber', e.target.value)} />
      <FormControl fullWidth>
        <InputLabel>Relationship</InputLabel>
        <Select label="Relationship" value={values.relationshipType} onChange={(e) => set('relationshipType', e.target.value)}>
          {RELATIONSHIP_TYPES.map((t) => (
            <MenuItem key={t} value={t}>{t.replace('_', ' ')}</MenuItem>
          ))}
        </Select>
      </FormControl>
      <TextField fullWidth type="number" label="Score (0-100)" inputProps={{ min: 0, max: 100 }} value={values.relationshipScore} onChange={(e) => set('relationshipScore', Number(e.target.value))} />
      <FormControl fullWidth>
        <InputLabel>Category</InputLabel>
        <Select label="Category" value={values.categoryId ?? ''} onChange={(e) => set('categoryId', e.target.value || null)}>
          <MenuItem value="">None</MenuItem>
          {categories.map((c) => (
            <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl fullWidth>
        <InputLabel>Priority</InputLabel>
        <Select label="Priority" value={values.priorityLevelId ?? ''} onChange={(e) => set('priorityLevelId', e.target.value || null)}>
          <MenuItem value="">None</MenuItem>
          {priorities.map((p) => (
            <MenuItem key={p.id} value={p.id}>{p.levelName}</MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControlLabel className="sm:col-span-2" control={<Switch checked={Boolean(values.innerCircle)} onChange={(e) => set('innerCircle', e.target.checked)} />} label="Inner circle" />
      <TextField className="sm:col-span-2" fullWidth multiline rows={3} label="Notes" value={values.notes} onChange={(e) => set('notes', e.target.value)} />
    </div>
  );
}

export function toContactPayload(form) {
  return {
    name: form.name,
    phoneNumber: form.phoneNumber || null,
    whatsappNumber: form.whatsappNumber || null,
    notes: form.notes || null,
    relationshipType: form.relationshipType,
    relationshipScore: form.relationshipScore ?? 50,
    innerCircle: Boolean(form.innerCircle),
    categoryId: form.categoryId ? Number(form.categoryId) : null,
    priorityLevelId: form.priorityLevelId ? Number(form.priorityLevelId) : null,
  };
}
