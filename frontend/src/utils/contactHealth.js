const MS_PER_DAY = 86400000;

export function daysSince(dateStr) {
  if (!dateStr) return Infinity;
  const date = new Date(dateStr);
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  date.setHours(0, 0, 0, 0);
  return Math.floor((today - date) / MS_PER_DAY);
}

export function getContactHealth(contact, priorityLevels = []) {
  const priority = priorityLevels.find((p) => p.id === contact.priorityLevelId);
  const frequency = priority?.reminderFrequencyDays ?? 30;
  const elapsed = daysSince(contact.lastInteractionDate);

  if (elapsed === Infinity) return 'overdue';
  if (elapsed > frequency) return 'overdue';
  if (elapsed >= frequency - 7) return 'dueSoon';
  if ((contact.relationshipScore ?? 0) >= 70) return 'healthy';
  if (elapsed <= frequency * 0.5) return 'healthy';
  return 'dueSoon';
}

export function categorizeContacts(contacts, priorityLevels) {
  const overdue = [];
  const dueSoon = [];
  const healthy = [];

  contacts.forEach((contact) => {
    const health = getContactHealth(contact, priorityLevels);
    if (health === 'overdue') overdue.push(contact);
    else if (health === 'dueSoon') dueSoon.push(contact);
    else healthy.push(contact);
  });

  return { overdue, dueSoon, healthy };
}

export function scoreColor(score) {
  if (score >= 70) return '#10B981';
  if (score >= 40) return '#F59E0B';
  return '#EF4444';
}

export function scoreLabel(score) {
  if (score >= 70) return 'Healthy';
  if (score >= 40) return 'Needs attention';
  return 'At risk';
}

export function formatDate(dateStr) {
  if (!dateStr) return 'Never';
  return new Date(dateStr).toLocaleDateString(undefined, {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  });
}

export const RELATIONSHIP_TYPES = [
  'FAMILY',
  'FRIEND',
  'MENTOR',
  'COLLEAGUE',
  'RELATIVE',
  'OTHER',
];

export const INTERACTION_TYPES = [
  'CALL',
  'MESSAGE',
  'MEETING',
  'VIDEO_CALL',
  'EMAIL',
  'SOCIAL',
  'OTHER',
];
