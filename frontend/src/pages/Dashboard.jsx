import { Link } from 'react-router-dom';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import ScheduleIcon from '@mui/icons-material/Schedule';
import FavoriteIcon from '@mui/icons-material/Favorite';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import Navbar from '../components/layout/Navbar';
import DashboardCard from '../components/dashboard/DashboardCard';
import ContactCard from '../components/contacts/ContactCard';
import { CardSkeleton } from '../components/common/LoadingSkeleton';
import { useContacts, useCategories, usePriorities } from '../hooks/useQueries';
import { categorizeContacts, scoreColor } from '../utils/contactHealth';

export default function Dashboard() {
  const { data: contacts = [], isLoading } = useContacts();
  const { data: categories = [] } = useCategories();
  const { data: priorities = [] } = usePriorities();

  const { overdue, dueSoon, healthy } = categorizeContacts(contacts, priorities);
  const avgScore = contacts.length
    ? Math.round(contacts.reduce((s, c) => s + (c.relationshipScore ?? 0), 0) / contacts.length)
    : 0;

  const catMap = Object.fromEntries(categories.map((c) => [c.id, c.name]));
  const priMap = Object.fromEntries(priorities.map((p) => [p.id, p]));

  const renderSection = (title, items, emptyMsg) => (
    <section className="mb-8">
      <div className="mb-4 flex items-center justify-between">
        <h2 className="text-lg font-semibold">{title}</h2>
        <Link to="/contacts" className="text-sm font-medium text-indigo-600 hover:underline dark:text-indigo-400">
          View all
        </Link>
      </div>
      {items.length === 0 ? (
        <p className="rounded-2xl border border-dashed border-slate-300 p-8 text-center text-slate-500 dark:border-slate-600">
          {emptyMsg}
        </p>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
          {items.slice(0, 3).map((contact) => (
            <ContactCard
              key={contact.id}
              contact={contact}
              categoryName={catMap[contact.categoryId]}
              priorityName={priMap[contact.priorityLevelId]?.levelName}
              priorityColor={priMap[contact.priorityLevelId]?.colorCode}
            />
          ))}
        </div>
      )}
    </section>
  );

  return (
    <div>
      <Navbar title="Dashboard" subtitle="Your relationship health at a glance" />
      <main className="p-6">
        {isLoading ? (
          <CardSkeleton count={4} />
        ) : (
          <>
            <div className="mb-8 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
              <DashboardCard title="Overdue" value={overdue.length} subtitle="Past check-in window" icon={WarningAmberIcon} accent="red" />
              <DashboardCard title="Due Soon" value={dueSoon.length} subtitle="Within 7 days" icon={ScheduleIcon} accent="amber" />
              <DashboardCard title="Healthy" value={healthy.length} subtitle="Strong connections" icon={FavoriteIcon} accent="emerald" />
              <DashboardCard title="Avg Score" value={avgScore} subtitle={`${contacts.length} total contacts`} icon={TrendingUpIcon} accent="indigo">
                <div className="h-2 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
                  <div className="h-full rounded-full transition-all" style={{ width: `${avgScore}%`, backgroundColor: scoreColor(avgScore) }} />
                </div>
              </DashboardCard>
            </div>

            {renderSection('Overdue Contacts', overdue, 'All caught up — no overdue contacts!')}
            {renderSection('Due Soon', dueSoon, 'No contacts due for check-in soon.')}
            {renderSection('Healthy Relationships', healthy, 'Log interactions to build healthy scores.')}
          </>
        )}
      </main>
    </div>
  );
}
