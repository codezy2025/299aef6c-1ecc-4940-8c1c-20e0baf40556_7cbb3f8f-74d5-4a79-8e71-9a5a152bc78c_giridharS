/**
 * 🏗️  DEVELOPMENT GUIDE - User feedback module Page Component
 * 
 * 📋 Original Requirements: ### CREATED_BY_API ###
User feedback module built to accept a form which gets the feedback from the user
 * 
 * 🚀 Enhancement Ideas:
 * - Add URL-based filtering and search
 * - Implement breadcrumb navigation
 * - Add export/import functionality
 * - Include real-time updates (WebSocket/SSE)
 * - Add keyboard shortcuts for common actions
 * - Implement undo/redo functionality
 * 
 * 💡 State Management Improvements:
 * - Use useReducer for complex state logic
 * - Add optimistic updates for better UX
 * - Implement proper error boundaries
 * - Add loading skeletons instead of spinners
 * 
 * 🔧 User Experience:
 * - Add confirmation dialogs for destructive actions
 * - Implement toast notifications for feedback
 * - Add drag-and-drop for reordering
 * - Include accessibility features (ARIA labels)
 * 
 * 📱 Responsive Design:
 * - Add mobile-specific components
 * - Implement swipe actions for mobile
 * - Consider drawer/modal layouts for small screens
 */

import React, { useState, useEffect } from 'react';
import UserFeedbackModuleForm from '../components/UserFeedbackModuleForm';
import UserFeedbackModuleList from '../components/UserFeedbackModuleList';
import userFeedbackModuleService from '../services/userFeedbackModuleService';
import { UserFeedbackModule, UserFeedbackModuleFormValues } from '../types/UserFeedbackModuleTypes';

const UserFeedbackModulePage: React.FC = () => {
  const [feedbacks, setFeedbacks] = useState<UserFeedbackModule[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [editingId, setEditingId] = useState<string | null>(null);

  useEffect(() => {
    const fetchFeedbacks = async () => {
      try {
        const data = await userFeedbackModuleService.getAll();
        setFeedbacks(data);
      } catch (err: any) {
        setError(err.message || 'Failed to fetch feedbacks');
      } finally {
        setLoading(false);
      }
    };

    fetchFeedbacks();
  }, []);

  const handleCreate = async (values: UserFeedbackModuleFormValues) => {
    try {
      const newFeedback = await userFeedbackModuleService.create(values);
      setFeedbacks(prev => [...prev, newFeedback]);
    } catch (err: any) {
      setError(err.message || 'Failed to create feedback');
    }
  };

  const handleUpdate = async (id: string, values: UserFeedbackModuleFormValues) => {
    try {
      const updatedFeedback = await userFeedbackModuleService.update(id, values);
      setFeedbacks(prev => prev.map(feedback => feedback.id === id ? updatedFeedback : feedback));
      setEditingId(null);
    } catch (err: any) {
      setError(err.message || 'Failed to update feedback');
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await userFeedbackModuleService.delete(id);
      setFeedbacks(prev => prev.filter(feedback => feedback.id !== id));
    } catch (err: any) {
      setError(err.message || 'Failed to delete feedback');
    }
  };

  const handleEdit = (feedback: UserFeedbackModule) => {
    setEditingId(feedback.id);
  };

  if (loading) return <div className="p-4">Loading...</div>;
  if (error) return <div className="p-4 text-red-500">Error: {error}</div>;

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">User Feedback Module</h1>
      <UserFeedbackModuleForm
        onSubmit={editingId ? (values) => handleUpdate(editingId, values) : handleCreate}
        initialValues={editingId ? feedbacks.find(f => f.id === editingId) : undefined}
        onCancel={() => setEditingId(null)}
      />
      <div className="mt-6">
        <UserFeedbackModuleList
          feedbacks={feedbacks}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      </div>
    </div>
  );
};

export default UserFeedbackModulePage;