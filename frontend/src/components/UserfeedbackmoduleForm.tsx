/**
 * 🏗️  DEVELOPMENT GUIDE - User feedback module Form Component
 * 
 * 📋 Original Requirements: ### CREATED_BY_API ###
User feedback module built to accept a form which gets the feedback from the user
 * 
 * 🚀 Enhancement Ideas:
 * - Add form validation with Zod/Yup schema
 * - Implement auto-save functionality
 * - Add file upload capabilities if needed
 * - Include conditional fields based on other inputs
 * - Add form steps/wizard for complex forms
 * - Implement real-time validation feedback
 * 
 * 💡 Props to Consider Adding:
 * - initialData?: Partial<User feedback module> (for edit mode)
 * - onCancel?: () => void
 * - isLoading?: boolean
 * - validationSchema?: ZodSchema
 * 
 * 🔧 Libraries to Consider:
 * - @hookform/resolvers for validation
 * - react-hook-form-devtools for debugging
 */

import React from 'react';
import { useForm } from 'react-hook-form';
import { UserFeedbackFormData } from '../types/User feedback moduleTypes';

interface UserFeedbackFormProps {
  onSubmit: (data: UserFeedbackFormData) => void;
}

const UserFeedbackForm: React.FC<UserFeedbackFormProps> = ({ onSubmit }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UserFeedbackFormData>();

  const onSubmitHandler = (data: UserFeedbackFormData) => {
    onSubmit(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmitHandler)} className="user-feedback-form">
      <div className="form-group">
        <label htmlFor="feedback">Feedback</label>
        <textarea
          id="feedback"
          {...register('feedback', { required: 'Feedback is required' })}
          className={errors.feedback ? 'error' : ''}
          rows={4}
        />
        {errors.feedback && <span className="error-message">{errors.feedback.message}</span>}
      </div>

      <div className="form-group">
        <label htmlFor="rating">Rating</label>
        <select
          id="rating"
          {...register('rating', { required: 'Rating is required' })}
          className={errors.rating ? 'error' : ''}
        >
          <option value="">Select a rating</option>
          <option value={1}>1 - Poor</option>
          <option value={2}>2 - Fair</option>
          <option value={3}>3 - Good</option>
          <option value={4}>4 - Very Good</option>
          <option value={5}>5 - Excellent</option>
        </select>
        {errors.rating && <span className="error-message">{errors.rating.message}</span>}
      </div>

      <div className="form-group">
        <label htmlFor="email">Email (Optional)</label>
        <input
          type="email"
          id="email"
          {...register('email', {
            pattern: {
              value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
              message: 'Please enter a valid email',
            },
          })}
          className={errors.email ? 'error' : ''}
        />
        {errors.email && <span className="error-message">{errors.email.message}</span>}
      </div>

      <button type="submit" className="submit-button">
        Submit Feedback
      </button>
    </form>
  );
};

export default UserFeedbackForm;