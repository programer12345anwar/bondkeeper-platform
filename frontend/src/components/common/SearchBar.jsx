import { TextField, InputAdornment } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

export default function SearchBar({ value, onChange, placeholder = 'Search...', className = '' }) {
  return (
    <TextField
      fullWidth
      size="small"
      value={value}
      onChange={(e) => onChange(e.target.value)}
      placeholder={placeholder}
      className={className}
      InputProps={{
        startAdornment: (
          <InputAdornment position="start">
            <SearchIcon className="text-slate-400" fontSize="small" />
          </InputAdornment>
        ),
        className: 'rounded-xl bg-white dark:bg-[#161922]',
      }}
    />
  );
}
