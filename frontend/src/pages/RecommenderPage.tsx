/**
 * 🏗️  DEVELOPMENT GUIDE - Recommender Page Component
 * 
 * 📋 Original Requirements: # **Module-Level Documentation: Recommender**

---

## **1. Module Name & Overview**

- **Module name**: `Recommender`
- **Summary**: The `Recommender` class is a core AI-powered component responsible for generating context-aware, follow-up questions to guide users through project exploration, onboarding, debugging, and knowledge retention. It leverages a Large Language Model (LLM) to analyze project metadata stored in Graph Modeling Language (GML) files and produce actionable, intelligent recommendations.
- **Business domain context**: This module is part of an AI-driven code and data intelligence platform designed to democratize access to technical knowledge across software teams. It enables non-experts to understand complex codebases and databases through natural language interaction.
- **Owner/team**: AI-Powered Project Intelligence Team

---

## **2. Public Interfaces / APIs**

### **Exposed Methods**

| Method | Parameters | Returns | Description |
|-------|------------|--------|-------------|
| `__init__()` | None | None | Initializes the Recommender with DeepSeek LLM client and `GRAPH_PATH` from environment variables. |
| `load_documents(user_id, project_id)` | `user_id` (str), `project_id` (str) | None | Loads project metadata from a GML file and populates instance variables with `project_document`, `database_document`, and `module_document`. |
| `generate_questions(user_id, project_id)` | `user_id` (str), `project_id` (str) | `List[str]` | Generates 5 general project exploration questions based on project and module summaries. |
| `generate_database_questions(user_id, project_id)` | `user_id` (str), `project_id` (str) | `List[str]` | Generates 5 database-specific questions based on the database schema summary. |
| `generate_questions_from_user_query(user_id, project_id, user_question)` | `user_id` (str), `project_id` (str), `user_question` (str) | `List[str]` | Generates 5 follow-up questions based on a user's specific query and project context. |

### **REST/RPC/GraphQL Endpoints**
- Not applicable. This is a Python class, not a standalone service with exposed endpoints.

### **Parameters and Response Structures**

- **`user_id`**: String identifier for the user (e.g., `"123"`).
- **`project_id`**: String identifier for the project (e.g., `"456"`).
- **`user_question`**: Natural language string describing the user's query (e.g., `"How do I add authentication?"`).

**Response Format**:
```json
[
  "What is the main goal of this project?",
  "How are the modules structured and related?",
  "What is the business logic behind the database schema?",
  "How can I optimize the performance of this module?",
  "What are the key dependencies and their roles?"
]
```

### **Example Usage / Code Snippets**

```python
from recommender import Recommender

# Initialize the Recommender
recommender = Recommender()

# Generate general project questions
questions = recommender.generate_questions(user_id="123", project_id="456")
print(questions)

# Generate database-specific questions
db_questions = recommender.generate_database_questions(user_id="123", project_id="456")
print(db_questions)

# Generate follow-up questions based on user input
followup_questions = recommender.generate_questions_from_user_query(
    user_id="123",
    project_id="456",
    user_question="How do I add authentication?"
)
print(followup_questions)
```

---

## **3. Dependencies**

### **Internal Dependencies**
- `networkx`: Used to read and parse GML files.
- `openai`: Used to interface with the DeepSeek LLM via OpenAI-compatible API.
- `pathlib` and `os`: For file system operations and path construction.

### **External Libraries**
- `openai`: For LLM inference (DeepSeek API).
- `networkx`: For graph data processing.
- `python-dotenv`: For loading environment variables.

### **System/Service Dependencies**
- **DeepSeek API**: Requires valid API key (`DEEPSEEK_API_KEY`) and access to the DeepSeek LLM endpoint.
- **File System**: Requires read access to `GRAPH_PATH/{user_id}/{project_id}/{project_id}.gml`.
- **Environment Variables**:
  - `DEEPSEEK_API_KEY`: API key for DeepSeek LLM.
  - `GRAPH_PATH`: Base directory for storing project GML files.

### **Mermaid Diagram: graph LR**
```mermaid
graph LR
    A[Recommender] --> B[DeepSeek LLM]
    A --> C[File System]
    C --> D[GRAPH_PATH/user_id/project_id/project_id.gml]
    A --> E[NetworkX]
    E --> F[Parse GML File]
    B --> G[Generate Questions]
    G --> H[Return List of Questions]
```

---

## **4. Configuration**

### **Environment Variables Used**
| Variable | Purpose | Example |
|--------|--------|--------|
| `DEEPSEEK_API_KEY` | API key for DeepSeek LLM | `sk-deepseek-abc123...` |
| `GRAPH_PATH` | Base path for project GML files | `/data/projects` |

### **JSON/YAML/TOML Config Examples**
```yaml
# .env
DEEPSEEK_API_KEY=sk-deepseek-abc123...
GRAPH_PATH=/data/projects
```

### **Default and Override Values**
- `GRAPH_PATH`: Default is `./graphs` if not set.
- `DEEPSEEK_API_KEY`: Required; no default — must be provided.
- `base_url`: Hardcoded to `https://api.deepseek.com/` (cannot be overridden in current implementation).

---

## **5. Data Models / Schema**

### **Main Data Models / Classes**
- `Recommender`: Core class that manages document loading and question generation.

### **Table/Collection Structure**
- **No database tables**. Data is stored in **file-based GML files**.

### **Field Types and Validations**
- **GML File Fields** (in `project_id.gml`):
  - `id`: Integer (must be `0` for root node).
  - `label`: String (e.g., `"Root"`).
  - `summary`: String (project overview).
  - `database_summary`: String (database schema description).
  - `module_summary`: String (module breakdown).

### **Relationships to Other Models**
- **`DocumentProcessor`**: Generates the GML file via `build_graph()` and populates `database_summary`, `module_summary`, and `summary`.
- **`VannaFlaskApp`**: Uses `generate_questions_from_user_query()` to power follow-up suggestions in the UI.

### **Mermaid Diagram: erDiagram**
```mermaid
erDiagram
    PROJECT ||--o{ DOCUMENT : has
    PROJECT {
        string user_id
        string project_id
        string summary
        string database_summary
        string module_summary
    }
    DOCUMENT {
        string file_path
        string generated_doc
    }
    PROJECT }o--|| DOCUMENT : "generated from"
```

---

## **6. Workflow & Logic**

### **Main Operations / Workflows**

#### **1. General Question Generation**
1. `load_documents()` reads `project_id.gml`.
2. Extracts `project_document` and `module_document`.
3. Sends prompt to LLM: *"Generate 5 general questions about the project and modules."*
4. Parses LLM output into a list of 5 questions.

#### **2. Database-Specific Question Generation**
1. `load_documents()` reads `project_id.gml`.
2. Extracts `database_document`.
3. Sends prompt to LLM: *"Generate 5 questions focused on the database schema."*
4. Parses output into 5 questions.

#### **3. Follow-Up Question Generation**
1. `load_documents()` reads `project_id.gml`.
2. Extracts `project_document`, `module_document`, `database_document`.
3. Constructs context with user’s question.
4. Sends prompt: *"Generate 5 follow-up questions based on the user's query and project context."*
5. Parses and returns list of questions.

### **Business Rules**
- All questions must be answerable from the provided project metadata.
- Questions must be numbered (e.g., `1. What is...`).
- If GML file is missing, all document fields are empty strings.

### **Mermaid Diagram: flowchart TD**
```mermaid
flowchart TD
    A[Start] --> B{Is GML file available?}
    B -->|Yes| C[Load project_id.gml]
    C --> D[Extract summary, database_summary, module_summary]
    D --> E[Send to LLM with prompt]
    E --> F[Parse LLM output]
    F --> G[Return list of 5 questions]
    B -->|No| H[Set all documents to empty string]
    H --> I[Return empty list]
```

---

## **7. Sequence & Interaction Flow**

### **Request Lifecycle**
1. User calls `generate_questions(user_id, project_id)`.
2. `load_documents()` checks for `GRAPH_PATH/user_id/project_id/project_id.gml`.
3. If file exists, it is parsed with `networkx.read_gml()`.
4. Root node attributes are extracted.
5. Prompt is constructed and sent to DeepSeek LLM.
6. LLM response is parsed into a list of questions.
7. List is returned to the caller.

### **Internal Control Flow**
- All methods are synchronous.
- No async events or job triggers.

### **Mermaid Diagram: sequenceDiagram**
```mermaid
sequenceDiagram
    participant User
    participant Recommender
    participant NetworkX
    participant DeepSeek LLM

    User->>Recommender: generate_questions(user_id, project_id)
    Recommender->>NetworkX: read_gml(file_path)
    NetworkX-->>Recommender: GML graph
    Recommender->>Recommender: Extract root node attributes
    Recommender->>DeepSeek LLM: Send prompt + context
    DeepSeek LLM-->>Recommender: Return JSON response
    Recommender->>Recommender: Parse questions
    Recommender-->>User: Return list of 5 questions
```

---

## **8. Error Handling**

### **Known Errors and Exceptions**
| Error | Cause | Handling |
|------|------|---------|
| `FileNotFoundError` | GML file not found | Set all document fields to empty string |
| `OpenAIError` | LLM API failure | Catch and return empty list |
| `ValueError` | Invalid GML format | Log error, return empty list |
| `KeyError` | Missing root node | Log error, return empty list |

### **How Errors Are Caught and Handled**
- All file operations wrapped in `try-except`.
- LLM calls wrapped in `try-except` with fallback to empty list.
- Errors logged via `print()` and `logging`.

### **Retry, Timeout, or Fallback Behavior**
- No retries.
- No timeouts (uses default LLM client behavior).
- Fallback: Return empty list of questions if any step fails.

### **Custom Error Classes or Codes**
- No custom error classes.
- Uses standard Python exceptions.

---

## **9. Security Considerations**

### **Auth Checks Performed**
- None. This module does not perform authentication.

### **Role-Based Access or Token Validation**
- None. Relies on upstream services to enforce access control.

### **Data Encryption or Sensitive Data Protection**
- GML files are stored in plain text.
- No encryption of project metadata.
- API key (`DEEPSEEK_API_KEY`) is stored in `.env` and not exposed in code.

---

## **10. Testing**

### **Unit and Integration Test Summary**
- Unit tests cover:
  - `load_documents()` with valid/invalid GML files.
  - `generate_questions()` with mock LLM.
  - `generate_database_questions()` with mock LLM.
  - `generate_questions_from_user_query()` with mock context.
- Integration tests verify:
  - GML file parsing.
  - LLM prompt construction and response parsing.

### **Coverage Reports**
- Not provided in documentation.

### **How to Run Tests**
```bash
pytest tests/test_recommender.py
```

### **Sample Test Cases or Scenarios**
```python
def test_load_documents_missing_file():
    recommender = Recommender()
    recommender.load_documents("123", "456")
    assert recommender.project_document == ""
    assert recommender.database_document == ""
    assert recommender.module_document == ""

def test_generate_questions():
    recommender = Recommender()
    # Mock LLM response
    with patch('openai.OpenAI.chat.completions.create') as mock:
        mock.return_value.choices[0].message.content = "1. What is the goal?\n2. How is it structured?"
        questions = recommender.generate_questions("123", "456")
        assert len(questions) == 2
        assert questions[0] == "What is the goal?"
```

---

## **11. Logging & Observability**

### **Logs Emitted**
- `print()` statements for debugging.
- No structured logging (e.g., `logging.info()`).

### **Metrics Generated**
- None.

### **Dashboards or Alerts**
- None.

### **Mermaid Diagram: graph TD**
```mermaid
graph TD
    Recommender -->|Logs| Logging System
    Logging System -->|Sends to| Monitoring/Alerting
```

---

## **12. Performance**

### **Known Performance Issues or Constraints**
- GML file parsing is synchronous and blocks execution.
- LLM calls are slow (1–5 seconds per request).
- No caching of LLM responses.

### **Benchmarks, Profiling Tools, or Load Test Results**
- Not provided.

### **Recommendations for Scaling**
- Add LLM response caching (e.g., Redis).
- Use async processing for LLM calls.
- Implement rate limiting.
- Cache GML file content in memory.

---

## **13. How to Extend or Modify**

### **Tips for Adding New Features Safely**
- Add new question types as new methods (e.g., `generate_module_questions()`).
- Use `@property` for document fields to avoid direct access.
- Keep LLM prompts in constants or config files.

### **Key Extension Points**
- `generate_questions()`: Add new prompt templates.
- `load_documents()`: Support new file formats (e.g., JSON).
- `generate_questions_from_user_query()`: Add context enrichment.

### **Known TODOs or Technical Debt Areas**
- No error handling for malformed GML.
- No support for multiple GML files.
- Hardcoded LLM model (`deepseek-chat`).
- No caching of parsed GML data.

---

## **14. Versioning & Change Log**

- **Last updated version**: 1.0
- **Git history or changelog links**: Not provided.
- **Important PRs or architectural changes**:
  - Initial implementation of `Recommender` class.
  - Integration with DeepSeek LLM.
  - GML file-based metadata storage.

---

## **15. Mermaid Diagram Summary**

| Diagram | Type | Description |
|-------|------|-------------|
| System Interaction | sequenceDiagram | Flow between `Recommender`, `NetworkX`, and `DeepSeek LLM` |
| Data Model | erDiagram | Relationships between `PROJECT` and `DOCUMENT` |
| Workflow | flowchart TD | Logical steps in question generation |
| Dependencies | graph LR | Internal and external calls |
| Logging/Monitoring | graph TD | Logs flow to monitoring system |

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
import { RecommenderForm } from '../components/RecommenderForm';
import { RecommenderList } from '../components/RecommenderList';
import { recommenderService } from '../services';
import { Recommender, RecommenderFormData } from '../types/RecommenderTypes';

const RecommenderPage: React.FC = () => {
  const [recommenders, setRecommenders] = useState<Recommender[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchRecommenders = async () => {
      try {
        const data = await recommenderService.getAll();
        setRecommenders(data);
      } catch (err) {
        setError('Failed to fetch recommenders');
        console.error('Error fetching recommenders:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchRecommenders();
  }, []);

  const handleCreate = async (formData: RecommenderFormData) => {
    try {
      const newRecommender = await recommenderService.create(formData);
      setRecommenders(prev => [...prev, newRecommender]);
    } catch (err) {
      setError('Failed to create recommender');
      console.error('Error creating recommender:', err);
    }
  };

  const handleUpdate = async (id: string, formData: RecommenderFormData) => {
    try {
      const updatedRecommender = await recommenderService.update(id, formData);
      setRecommenders(prev => 
        prev.map(recommender => 
          recommender.id === id ? updatedRecommender : recommender
        )
      );
    } catch (err) {
      setError('Failed to update recommender');
      console.error('Error updating recommender:', err);
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await recommenderService.delete(id);
      setRecommenders(prev => prev.filter(recommender => recommender.id !== id));
    } catch (err) {
      setError('Failed to delete recommender');
      console.error('Error deleting recommender:', err);
    }
  };

  if (loading) {
    return <div className="p-4">Loading...</div>;
  }

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Recommender System</h1>
      
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}
      
      <RecommenderForm onSubmit={handleCreate} />
      
      <div className="mt-8">
        <h2 className="text-xl font-semibold mb-4">Recommenders</h2>
        <RecommenderList 
          recommenders={recommenders} 
          onUpdate={handleUpdate} 
          onDelete={handleDelete} 
        />
      </div>
    </div>
  );
};

export default RecommenderPage;