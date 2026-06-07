export default function DashboardCard({ title, value, subtitle, icon: Icon, accent = 'indigo', children }) {
  const accents = {
    indigo: 'from-indigo-500/10 to-indigo-600/5 border-indigo-200 dark:border-indigo-800',
    red: 'from-red-500/10 to-red-600/5 border-red-200 dark:border-red-800',
    amber: 'from-amber-500/10 to-amber-600/5 border-amber-200 dark:border-amber-800',
    emerald: 'from-emerald-500/10 to-emerald-600/5 border-emerald-200 dark:border-emerald-800',
  };

  const iconColors = {
    indigo: 'text-indigo-600 bg-indigo-100 dark:bg-indigo-900/40 dark:text-indigo-400',
    red: 'text-red-600 bg-red-100 dark:bg-red-900/40 dark:text-red-400',
    amber: 'text-amber-600 bg-amber-100 dark:bg-amber-900/40 dark:text-amber-400',
    emerald: 'text-emerald-600 bg-emerald-100 dark:bg-emerald-900/40 dark:text-emerald-400',
  };

  return (
    <div
      className={`rounded-2xl border bg-gradient-to-br p-6 shadow-sm ${accents[accent] ?? accents.indigo}`}
    >
      <div className="flex items-start justify-between">
        <div>
          <p className="text-sm font-medium text-slate-500 dark:text-slate-400">{title}</p>
          <p className="mt-2 text-3xl font-bold tracking-tight text-slate-900 dark:text-white">{value}</p>
          {subtitle && <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">{subtitle}</p>}
        </div>
        {Icon && (
          <div className={`rounded-xl p-3 ${iconColors[accent] ?? iconColors.indigo}`}>
            <Icon />
          </div>
        )}
      </div>
      {children && <div className="mt-4">{children}</div>}
    </div>
  );
}
