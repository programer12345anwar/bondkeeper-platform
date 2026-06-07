import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Pagination } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import Navbar from '../../components/layout/Navbar';
import SearchBar from '../../components/common/SearchBar';
import ContactFilters from '../../components/common/ContactFilters';
import ContactCard from '../../components/contacts/ContactCard';
import { CardSkeleton } from '../../components/common/LoadingSkeleton';
import { useCategories, usePriorities, useContactSearch } from '../../hooks/useQueries';

export default function ContactList() {
  const [query, setQuery] = useState('');
  const [categoryId, setCategoryId] = useState(null);
  const [priorityLevelId, setPriorityLevelId] = useState(null);
  const [innerCircle, setInnerCircle] = useState(null);
  const [page, setPage] = useState(0);

  const { data: categories = [] } = useCategories();
  const { data: priorities = [] } = usePriorities();

  const searchParams = {
    query: query || undefined,
    categoryId: categoryId || undefined,
    priorityLevelId: priorityLevelId || undefined,
    innerCircle: innerCircle ?? undefined,
    page,
    size: 12,
    sortBy: 'name',
    sortDirection: 'asc',
  };

  const { data, isLoading, isFetching } = useContactSearch(searchParams);
  const contacts = data?.content ?? [];
  const totalPages = data?.totalPages ?? 0;

  const catMap = Object.fromEntries(categories.map((c) => [c.id, c.name]));
  const priMap = Object.fromEntries(priorities.map((p) => [p.id, p]));

  return (
    <div>
      <Navbar title="Contacts" subtitle="Manage your relationships" />
      <main className="p-6">
        <div className="mb-6 flex flex-wrap items-center justify-between gap-4">
          <div className="flex-1 min-w-[240px] max-w-md">
            <SearchBar value={query} onChange={setQuery} placeholder="Search contacts..." />
          </div>
          <Button component={Link} to="/contacts/new" variant="contained" startIcon={<AddIcon />} className="rounded-xl">
            Add Contact
          </Button>
        </div>

        <div className="mb-6">
          <ContactFilters
            categories={categories}
            priorities={priorities}
            categoryId={categoryId}
            priorityLevelId={priorityLevelId}
            innerCircle={innerCircle}
            onCategoryChange={setCategoryId}
            onPriorityChange={setPriorityLevelId}
            onInnerCircleChange={setInnerCircle}
          />
        </div>

        {isLoading || isFetching ? (
          <CardSkeleton count={6} />
        ) : contacts.length === 0 ? (
          <div className="rounded-2xl border border-dashed border-slate-300 p-16 text-center dark:border-slate-600">
            <p className="text-slate-500">No contacts found</p>
            <Button component={Link} to="/contacts/new" className="mt-4" variant="outlined">
              Add your first contact
            </Button>
          </div>
        ) : (
          <>
            <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
              {contacts.map((contact) => (
                <ContactCard
                  key={contact.id}
                  contact={contact}
                  categoryName={catMap[contact.categoryId]}
                  priorityName={priMap[contact.priorityLevelId]?.levelName}
                  priorityColor={priMap[contact.priorityLevelId]?.colorCode}
                />
              ))}
            </div>
            {totalPages > 1 && (
              <div className="mt-8 flex justify-center">
                <Pagination count={totalPages} page={page + 1} onChange={(_, p) => setPage(p - 1)} color="primary" />
              </div>
            )}
          </>
        )}
      </main>
    </div>
  );
}
