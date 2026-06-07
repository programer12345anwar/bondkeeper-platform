import { Link } from 'react-router-dom';
import { scoreColor, scoreLabel, formatDate } from '../../utils/contactHealth';

export default function ContactCard({ contact, categoryName, priorityName, priorityColor }) {
  const score = contact.relationshipScore ?? 0;

  return (
    <Link
      to={`/contacts/${contact.id}`}
      className="group block rounded-2xl border border-slate-200 bg-white p-5 shadow-sm transition hover:-translate-y-0.5 hover:border-brand-500/40 hover:shadow-md dark:border-slate-700 dark:bg-[#161922] dark:hover:border-indigo-500/40"
    >
      <div className="flex items-start justify-between gap-3">
        <div className="min-w-0 flex-1">
          <div className="flex items-center gap-2">
            <h3 className="truncate font-semibold text-slate-900 group-hover:text-indigo-600 dark:text-white dark:group-hover:text-indigo-400">
              {contact.name}
            </h3>
            {contact.innerCircle && (
              <span className="shrink-0 rounded-full bg-amber-100 px-2 py-0.5 text-xs font-medium text-amber-700 dark:bg-amber-900/40 dark:text-amber-300">
                Inner
              </span>
            )}
          </div>
          <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">
            {contact.relationshipType?.replace('_', ' ')}
            {categoryName && ` · ${categoryName}`}
          </p>
        </div>
        <div
          className="flex h-12 w-12 shrink-0 flex-col items-center justify-center rounded-xl text-white"
          style={{ backgroundColor: scoreColor(score) }}
        >
          <span className="text-sm font-bold leading-none">{score}</span>
        </div>
      </div>

      <div className="mt-4 flex flex-wrap items-center gap-2 text-xs text-slate-500 dark:text-slate-400">
        <span className="rounded-lg bg-slate-100 px-2 py-1 dark:bg-slate-800">{scoreLabel(score)}</span>
        {priorityName && (
          <span
            className="rounded-lg px-2 py-1 font-medium text-white"
            style={{ backgroundColor: priorityColor || '#6366f1' }}
          >
            {priorityName}
          </span>
        )}
        <span>Last: {formatDate(contact.lastInteractionDate)}</span>
      </div>
    </Link>
  );
}
