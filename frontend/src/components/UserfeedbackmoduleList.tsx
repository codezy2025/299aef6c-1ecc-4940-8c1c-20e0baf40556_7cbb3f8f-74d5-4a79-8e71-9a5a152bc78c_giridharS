/**
 * 🏗️  DEVELOPMENT GUIDE - User feedback module List Component
 * 
 * 📋 Original Requirements: ### CREATED_BY_API ###
User feedback module built to accept a form which gets the feedback from the user
 * 
 * 🚀 Enhancement Ideas:
 * - Add search/filter functionality
 * - Implement sorting for all columns
 * - Add bulk operations (delete, update status)
 * - Include export functionality (CSV, PDF)
 * - Add infinite scrolling or virtual scrolling
 * - Implement row selection with checkboxes
 * 
 * 💡 Props to Consider Adding:
 * - searchTerm?: string
 * - filters?: Record<string, any>
 * - sortConfig?: { key: string, direction: 'asc' | 'desc' }
 * - isLoading?: boolean
 * - onBulkAction?: (action: string, ids: string[]) => void
 * 
 * 🔧 Libraries to Consider:
 * - @tanstack/react-table for advanced features
 * - react-window for virtualization
 * - fuse.js for fuzzy search
 */

import React from 'react';
import { UserFeedbackModule } from '../types/UserFeedbackModuleTypes';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Button } from '@/components/ui/button';

interface UserFeedbackModuleListProps {
  data: UserFeedbackModule[];
  onEdit: (feedback: UserFeedbackModule) => void;
  onDelete: (id: string) => void;
}

const UserFeedbackModuleList: React.FC<UserFeedbackModuleListProps> = ({ data, onEdit, onDelete }) => {
  return (
    <div className="w-full overflow-x-auto">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Feedback</TableHead>
            <TableHead>Rating</TableHead>
            <TableHead>Submitted By</TableHead>
            <TableHead>Created At</TableHead>
            <TableHead>Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {data.length === 0 ? (
            <TableRow>
              <TableCell colSpan={5} className="text-center">
                No feedback available
              </TableCell>
            </TableRow>
          ) : (
            data.map((feedback) => (
              <TableRow key={feedback.id}>
                <TableCell className="font-medium">{feedback.feedback}</TableCell>
                <TableCell>{feedback.rating}</TableCell>
                <TableCell>{feedback.submittedBy}</TableCell>
                <TableCell>{new Date(feedback.createdAt).toLocaleString()}</TableCell>
                <TableCell>
                  <div className="flex gap-2">
                    <Button variant="outline" size="sm" onClick={() => onEdit(feedback)}>
                      Edit
                    </Button>
                    <Button variant="destructive" size="sm" onClick={() => onDelete(feedback.id)}>
                      Delete
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </div>
  );
};

export default UserFeedbackModuleList;