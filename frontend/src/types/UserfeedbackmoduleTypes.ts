/**
 * 🏗️  DEVELOPMENT GUIDE - User feedback module Types
 * 
 * 📋 Original Requirements: ### CREATED_BY_API ###
User feedback module built to accept a form which gets the feedback from the user
 * 
 * 🚀 Enhancement Ideas:
 * - Add validation schemas using Zod or Yup
 * - Create utility types for API responses (ApiResponse<User feedback module>)
 * - Add enums for status fields or categories
 * - Consider adding computed fields or getters
 * - Add types for search/filter parameters
 * 
 * 💡 Example Extensions:
 * - export enum User feedback moduleStatus { ACTIVE = 'active', INACTIVE = 'inactive' }
 * - export type User feedback moduleSearchParams = Pick<User feedback module, 'name' | 'status'>
 * - export type User feedback moduleUpdateData = Partial<Omit<User feedback module, 'id' | 'createdAt'>>
 */

export interface UserFeedbackModule {
  id: string;
  userId: string;
  feedback: string;
  rating: number;
  createdAt: Date;
  updatedAt: Date;
}

export interface UserFeedbackModuleFormData {
  feedback: string;
  rating: number;
}