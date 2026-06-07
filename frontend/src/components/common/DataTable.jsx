import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from '@mui/material';

export default function DataTable({ columns, rows, onRowClick, emptyMessage = 'No data found' }) {
  if (!rows?.length) {
    return (
      <div className="rounded-2xl border border-dashed border-slate-300 p-12 text-center text-slate-500 dark:border-slate-600">
        {emptyMessage}
      </div>
    );
  }

  return (
    <TableContainer
      component={Paper}
      elevation={0}
      className="rounded-2xl border border-slate-200 dark:border-slate-700"
    >
      <Table>
        <TableHead>
          <TableRow className="bg-slate-50 dark:bg-slate-800/50">
            {columns.map((col) => (
              <TableCell key={col.field} className="font-semibold text-slate-600 dark:text-slate-300">
                {col.headerName}
              </TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow
              key={row.id}
              hover
              onClick={() => onRowClick?.(row)}
              className={onRowClick ? 'cursor-pointer' : ''}
            >
              {columns.map((col) => (
                <TableCell key={col.field}>
                  {col.renderCell ? col.renderCell(row) : row[col.field]}
                </TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
