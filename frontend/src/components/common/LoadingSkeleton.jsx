import { Skeleton } from '@mui/material';

export function CardSkeleton({ count = 1 }) {
  return (
    <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
      {Array.from({ length: count }).map((_, i) => (
        <div
          key={i}
          className="rounded-2xl border border-slate-200 bg-white p-5 dark:border-slate-700 dark:bg-[#161922]"
        >
          <Skeleton variant="rounded" height={24} width="60%" />
          <Skeleton variant="rounded" height={16} width="40%" className="mt-3" />
          <Skeleton variant="rounded" height={48} className="mt-4" />
        </div>
      ))}
    </div>
  );
}

export function TableSkeleton({ rows = 5 }) {
  return (
    <div className="space-y-3">
      {Array.from({ length: rows }).map((_, i) => (
        <Skeleton key={i} variant="rounded" height={52} />
      ))}
    </div>
  );
}

export function PageHeaderSkeleton() {
  return (
    <div className="mb-8">
      <Skeleton variant="rounded" height={36} width={240} />
      <Skeleton variant="rounded" height={20} width={360} className="mt-2" />
    </div>
  );
}
