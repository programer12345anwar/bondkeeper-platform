# BondKeeper Frontend

React 19 web application for BondKeeper — a premium relationship management dashboard.

## Tech Stack

- React 19 + Vite 6
- Tailwind CSS 4
- Redux Toolkit (auth, UI theme)
- React Query (server state)
- Axios (API + JWT refresh)
- React Router 7
- Material UI 6

## Getting Started

```bash
cd frontend
npm install
npm run dev
```

App runs at http://localhost:5173

API proxy forwards `/api` → http://localhost:8080 (ensure backend is running).

### Demo login

- Email: `demo@bondkeeper.app`
- Password: `password`

## Folder Structure

```
src/
├── api/              # Axios instance + API modules
├── store/            # Redux slices (auth, ui)
├── hooks/            # React Query hooks
├── providers/        # AppProviders wrapper
├── theme/            # MUI theme
├── utils/            # Contact health helpers
├── components/
│   ├── layout/       # Sidebar, Navbar, AppLayout
│   ├── common/       # Modal, SearchBar, DataTable, etc.
│   ├── contacts/     # ContactCard
│   └── dashboard/    # DashboardCard
└── pages/
    ├── auth/         # Login, Register
    ├── contacts/     # List, Detail, Add, Edit
    ├── categories/
    ├── priorities/
    └── settings/
```

## Features

- JWT authentication with auto token refresh
- Dark / light mode
- Responsive sidebar layout
- Dashboard with overdue / due soon / healthy contacts
- Contact search, filters, pagination
- Category & priority CRUD
- Interaction logging + relationship score
- Toast notifications & loading skeletons
