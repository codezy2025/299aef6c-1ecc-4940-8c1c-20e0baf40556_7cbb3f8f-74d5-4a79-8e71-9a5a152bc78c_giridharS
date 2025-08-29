/**
 * 🏗️  DEVELOPMENT GUIDE - User feedback module Service
 * 
 * 📋 Original Requirements: ### CREATED_BY_API ###
User feedback module built to accept a form which gets the feedback from the user
 * 
 * 🚀 Enhancement Ideas:
 * - Add request/response interceptors for error handling
 * - Implement retry logic for failed requests
 * - Add caching layer (React Query, SWR)
 * - Include request cancellation support
 * - Add batch operations (bulkCreate, bulkUpdate)
 * - Implement optimistic updates
 * 
 * 💡 Methods to Consider Adding:
 * - search(query: string): Promise<User feedback module[]>
 * - bulkDelete(ids: string[]): Promise<void>
 * - export(): Promise<Blob>
 * - getStats(): Promise<{User feedback moduleStats}>
 * 
 * 🔧 Error Handling:
 * - Create custom error classes
 * - Add request/response logging
 * - Implement exponential backoff for retries
 * 
 * 🚀 Performance:
 * - Add request deduplication
 * - Implement response caching
 * - Consider using React Query for state management
 */

import axios from 'axios';
import { UserFeedback, UserFeedbackCreate, UserFeedbackUpdate } from '../types/UserFeedbackModuleTypes';

const API_BASE_URL = '/api/user-feedback';

const userFeedbackModuleService = {
  getAll: async (): Promise<UserFeedback[]> => {
    const response = await axios.get<UserFeedback[]>(API_BASE_URL);
    return response.data;
  },

  create: async (feedbackData: UserFeedbackCreate): Promise<UserFeedback> => {
    const response = await axios.post<UserFeedback>(API_BASE_URL, feedbackData);
    return response.data;
  },

  update: async (id: string, feedbackData: UserFeedbackUpdate): Promise<UserFeedback> => {
    const response = await axios.put<UserFeedback>(`${API_BASE_URL}/${id}`, feedbackData);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/${id}`);
  }
};

export default userFeedbackModuleService;