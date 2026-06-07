import { useQuery } from '@tanstack/react-query';
import { categoryApi, contactApi, priorityApi, interactionApi, userApi } from '../api';

export function useCategories() {
  return useQuery({ queryKey: ['categories'], queryFn: categoryApi.getAll });
}

export function usePriorities() {
  return useQuery({ queryKey: ['priorities'], queryFn: priorityApi.getAll });
}

export function useContacts() {
  return useQuery({ queryKey: ['contacts'], queryFn: contactApi.getAll });
}

export function useContact(id) {
  return useQuery({
    queryKey: ['contacts', id],
    queryFn: () => contactApi.getById(id),
    enabled: Boolean(id),
  });
}

export function useContactSearch(params) {
  return useQuery({
    queryKey: ['contacts', 'search', params],
    queryFn: () => contactApi.search(params),
    placeholderData: (prev) => prev,
  });
}

export function useInteractions(contactId) {
  return useQuery({
    queryKey: ['interactions', contactId],
    queryFn: () => interactionApi.getByContact(contactId),
    enabled: Boolean(contactId),
  });
}

export function useProfile() {
  return useQuery({ queryKey: ['profile'], queryFn: userApi.getProfile });
}
