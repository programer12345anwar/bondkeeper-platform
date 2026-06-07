# BondKeeper — Entity Relationship Diagram

## Mermaid ER Diagram

```mermaid
erDiagram
    USERS ||--o{ CATEGORIES : owns
    USERS ||--o{ PRIORITY_LEVELS : owns
    USERS ||--o{ CONTACTS : owns

    CATEGORIES ||--o{ CONTACTS : categorizes
    PRIORITY_LEVELS ||--o{ CONTACTS : prioritizes

    CONTACTS ||--o{ INTERACTIONS : has
    CONTACTS ||--o{ REMINDERS : has

    USERS {
        bigserial id PK
        varchar first_name
        varchar last_name
        varchar email UK
        varchar password
        timestamptz created_at
        timestamptz updated_at
    }

    CATEGORIES {
        bigserial id PK
        varchar name
        text description
        bigint user_id FK
        timestamptz created_at
        timestamptz updated_at
    }

    PRIORITY_LEVELS {
        bigserial id PK
        varchar level_name
        int reminder_frequency_days
        varchar color_code
        bigint user_id FK
        timestamptz created_at
        timestamptz updated_at
    }

    CONTACTS {
        bigserial id PK
        varchar name
        varchar phone_number
        varchar whatsapp_number
        text notes
        varchar relationship_type
        int relationship_score
        date last_interaction_date
        boolean inner_circle
        bigint category_id FK
        bigint priority_level_id FK
        bigint user_id FK
        timestamptz created_at
        timestamptz updated_at
    }

    INTERACTIONS {
        bigserial id PK
        varchar interaction_type
        date interaction_date
        text notes
        bigint contact_id FK
        timestamptz created_at
        timestamptz updated_at
    }

    REMINDERS {
        bigserial id PK
        text reminder_message
        varchar reminder_type
        date reminder_date
        bigint contact_id FK
        timestamptz created_at
        timestamptz updated_at
    }
```

## Relationship Summary

| Parent        | Child           | Cardinality | On Delete   |
|---------------|-----------------|-------------|-------------|
| User          | Category        | 1:N         | CASCADE     |
| User          | PriorityLevel   | 1:N         | CASCADE     |
| User          | Contact         | 1:N         | CASCADE     |
| Category      | Contact         | 1:N         | SET NULL    |
| PriorityLevel | Contact         | 1:N         | SET NULL    |
| Contact       | Interaction     | 1:N         | CASCADE     |
| Contact       | Reminder        | 1:N         | CASCADE     |

## Constraints

- **Unique**: `(user_id, name)` on categories
- **Unique**: `(user_id, level_name)` on priority_levels
- **Unique**: `email` on users
- **Check**: `relationship_score` between 0 and 100
- **Check**: `reminder_frequency_days` > 0

## Enum Values

### relationship_type (Contact)
`FAMILY`, `FRIEND`, `MENTOR`, `COLLEAGUE`, `RELATIVE`, `OTHER`

### interaction_type (Interaction)
`CALL`, `MESSAGE`, `MEETING`, `VIDEO_CALL`, `EMAIL`, `SOCIAL`, `OTHER`

### reminder_type (Reminder)
`BIRTHDAY`, `ANNIVERSARY`, `FOLLOW_UP`, `CHECK_IN`, `CUSTOM`

## Indexes

| Table          | Index                              | Purpose                    |
|----------------|------------------------------------|----------------------------|
| categories     | user_id                            | User-scoped queries        |
| priority_levels| user_id                            | User-scoped queries        |
| contacts       | user_id                            | User-scoped queries        |
| contacts       | (user_id, inner_circle) partial    | Inner circle filter        |
| interactions   | contact_id, interaction_date DESC  | Contact history timeline   |
| reminders      | contact_id, reminder_date          | Due reminder lookups       |
