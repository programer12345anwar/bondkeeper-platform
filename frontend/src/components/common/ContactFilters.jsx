import { FormControl, InputLabel, Select, MenuItem, FormControlLabel, Switch, Box } from '@mui/material';

export default function ContactFilters({
  categories = [],
  priorities = [],
  categoryId,
  priorityLevelId,
  innerCircle,
  onCategoryChange,
  onPriorityChange,
  onInnerCircleChange,
}) {
  return (
    <Box className="flex flex-wrap gap-3">
      <FormControl size="small" className="min-w-[160px]">
        <InputLabel>Category</InputLabel>
        <Select
          label="Category"
          value={categoryId ?? ''}
          onChange={(e) => onCategoryChange(e.target.value || null)}
        >
          <MenuItem value="">All</MenuItem>
          {categories.map((c) => (
            <MenuItem key={c.id} value={c.id}>
              {c.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <FormControl size="small" className="min-w-[160px]">
        <InputLabel>Priority</InputLabel>
        <Select
          label="Priority"
          value={priorityLevelId ?? ''}
          onChange={(e) => onPriorityChange(e.target.value || null)}
        >
          <MenuItem value="">All</MenuItem>
          {priorities.map((p) => (
            <MenuItem key={p.id} value={p.id}>
              {p.levelName}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <FormControlLabel
        control={
          <Switch
            checked={Boolean(innerCircle)}
            onChange={(e) => onInnerCircleChange(e.target.checked || null)}
          />
        }
        label="Inner circle only"
      />
    </Box>
  );
}
