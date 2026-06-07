import { Dialog, DialogTitle, DialogContent, DialogActions, IconButton } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';

export default function Modal({ open, onClose, title, children, actions, maxWidth = 'sm' }) {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth={maxWidth} PaperProps={{ className: 'rounded-2xl' }}>
      <DialogTitle className="flex items-center justify-between pr-2 font-semibold">
        {title}
        <IconButton onClick={onClose} size="small" aria-label="Close">
          <CloseIcon fontSize="small" />
        </IconButton>
      </DialogTitle>
      <DialogContent dividers>{children}</DialogContent>
      {actions && <DialogActions className="px-6 py-4">{actions}</DialogActions>}
    </Dialog>
  );
}
