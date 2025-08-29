/**
 * 🏗️  DEVELOPMENT GUIDE - Database Integration & Vector Stores Form Component
 * 
 * 📋 Original Requirements: # **Module-Level Documentation: Database Integration & Vector Stores**

---

## **1. Module Name & Overview**

- **Module Name**: `Database Integration & Vector Stores`
- **Summary**: This module provides a unified, extensible interface for integrating relational databases with vector-based AI systems, enabling semantic search, natural language-to-SQL (NL2SQL) generation, and knowledge graph construction. It supports multiple vector database backends and ensures deterministic, reproducible training data management.
- **Business Domain Context**: Core infrastructure for AI-powered code and data intelligence platforms. Enables developers and analysts to query databases using natural language, generate SQL, and explore codebases with AI-guided insights.
- **Owner/Team**: AI-Powered Project Intelligence Team

---

## **2. Public Interfaces / APIs**

### **Exposed Methods/Classes**

| Method | Parameters | Returns | Description |
|-------|------------|--------|-------------|
| `add_question_sql(question: str, sql: str, tag: str = "Manually Trained")` | `question`, `sql`, `tag` | `str` (UUID) | Adds a question–SQL pair to training data with deterministic UUID |
| `add_ddl(ddl: str)` | `ddl` (SQL DDL) | `str` (UUID) | Stores schema definition in vector store |
| `add_documentation(doc: str)` | `doc` (text) | `str` (UUID) | Adds human-readable documentation |
| `get_similar_question_sql(question: str)` | `question` | `list[str]` | Retrieves similar questions from training data |
| `get_related_ddl(question: str)` | `question` | `list[str]` | Returns relevant DDL statements |
| `get_related_documentation(question: str)` | `question` | `list[str]` | Returns relevant documentation snippets |
| `get_training_data()` | None | `pd.DataFrame` | Exports all training data as DataFrame |
| `remove_training_data(id: str)` | `id` | `bool` | Deletes training record by ID |
| `get_related_training_data_cached(question: str)` | `question` | `TrainingData` | Caches and returns related training data |

### **REST/RPC Endpoints (via VannaDB_VectorStore)**

| Endpoint | Method | Description |
|--------|--------|-------------|
| `POST /rpc/add_sql` | RPC | Add question–SQL pair |
| `POST /rpc/add_ddl` | RPC | Add DDL statement |
| `POST /rpc/add_documentation` | RPC | Add documentation |
| `POST /rpc/get_training_data` | RPC | Retrieve all training data |
| `POST /rpc/remove_training_data` | RPC | Delete training record |
| `POST /query` | GraphQL | Query functions, get related data |

### **Example Usage (Python)**

```python
from vanna.vannadb import VannaDB_VectorStore

vn = VannaDB_VectorStore(vanna_model="my_project", vanna_api_key="your_key")

# Add training data
question = "Top 5 customers in US by sales"
sql = """
SELECT c.c_name, SUM(o.o_totalprice) AS total_sales
FROM customers c
JOIN orders o ON c.c_custkey = o.o_custkey
WHERE c.c_nationkey = (SELECT n_nationkey FROM nation WHERE n_name = 'UNITED STATES')
GROUP BY c.c_name
ORDER BY total_sales DESC
LIMIT 5;
"""
id = vn.add_question_sql(question=question, sql=sql, tag="User-Generated")

# Retrieve related data
similar_questions = vn.get_similar_question_sql(question)
related_ddl = vn.get_related_ddl(question)
```

---

## **3. Dependencies**

### **Internal Dependencies**
- `VannaBase` (abstract base class) – defines core interface
- `VannaAdvanced` – extends functionality for functions and advanced features
- `Cache` – manages in-memory state (e.g., `MemoryCache`)
- `AuthInterface` – handles authentication for protected endpoints

### **External Libraries**
- `requests` – HTTP client for API calls
- `pandas` – data handling and export
- `dataclasses` – for structured data types
- `json` – serialization/deserialization
- `flask` – for web API layer (in `VannaFlaskAPI`)
- `flasgger` – Swagger documentation
- `flask-sock` – WebSocket support

### **System/Service Dependencies**
- **Vector Database Backend**: FAISS, ChromaDB, OpenSearch, VannaDB
- **LLM Providers**: OpenAI, Cohere, ZhipuAI, QianWenAI, Mistral, Xinference
- **Database Connectors**: PostgreSQL, MySQL, Snowflake, Oracle (via `DBConnector`)
- **Authentication Services**: JWT, OAuth2, NoAuth (dev)

### **Mermaid Diagram: graph LR**
```mermaid
graph LR
    A[Database Integration & Vector Stores] --> B[VannaBase]
    A --> C[Vector Databases: FAISS, ChromaDB, OpenSearch, VannaDB]
    A --> D[LLM Providers: OpenAI, Cohere, ZhipuAI, QianWenAI, Mistral, Xinference]
    A --> E[Database Connectors: PostgreSQL, MySQL, Snowflake, Oracle]
    A --> F[AuthInterface: JWT, OAuth2, NoAuth]
    A --> G[Cache: MemoryCache, Redis, SQLite]
    B --> H[Flask API Layer]
    H --> I[Frontend: Svelte/React/Streamlit]
    I --> J[User]
```

---

## **4. Configuration**

### **Environment Variables**
```env
# Vector Store
VECTOR_DB_PATH=./vector_db
GRAPH_PATH=./graph

# LLM Providers
OPENAI_API_KEY=your_openai_key
ZHIPUAI_API_KEY=your_zhipuai_key
QIANFAN_API_KEY=your_qianfan_key
MISTRAL_API_KEY=your_mistral_key

# Database
DATABASE_URL=postgresql://user:password@localhost:5432/vanna_db

# Security
SECRET_KEY=your_flask_secret_key
JWT_SECRET_KEY=your_jwt_secret_key

# VannaDB
VANNA_MODEL=my_project
VANNA_API_KEY=your_vanna_api_key
```

### **JSON/YAML Config Example**
```yaml
# config.yaml
vector_store:
  backend: "faiss"
  path: "./vector_db"
  use_disk: true

llm:
  provider: "openai"
  model: "gpt-3.5-turbo"
  temperature: 0.7

database:
  type: "postgresql"
  url: "postgresql://user:pass@localhost:5432/vanna_db"

auth:
  type: "jwt"
  secret_key: "your_jwt_secret"

cache:
  type: "memory"
  max_size: 1000
```

### **Default & Override Values**
| Setting | Default | Override |
|--------|--------|----------|
| `vector_store.backend` | `faiss` | `chromadb`, `opensearch`, `vannadb` |
| `llm.provider` | `openai` | `cohere`, `zhipuai`, `qianwen` |
| `cache.type` | `memory` | `redis`, `sqlite` |
| `auth.type` | `noauth` | `jwt`, `oauth2` |
| `use_disk` | `false` | `true` (for persistent FAISS) |

---

## **5. Data Models / Schema**

### **Main Data Models**

#### `TrainingData` (Dataclass)
```python
@dataclass
class TrainingData:
    questions: List[str]
    ddl: List[str]
    documentation: List[str]
```

#### `QuestionSQLPair`
```python
@dataclass
class QuestionSQLPair:
    question: str
    sql: str
    tag: str
    id: str  # deterministic UUID
    created_at: datetime
```

#### `StringData`
```python
@dataclass
class StringData:
    data: str
    id: str
```

#### `Status`, `StatusWithId`
```python
@dataclass
class Status:
    success: bool
    message: str

@dataclass
class StatusWithId:
    success: bool
    message: str
    id: str
```

### **Table/Collections Structure**

| Collection | Type | Fields | Purpose |
|----------|------|--------|--------|
| `training_data` | Vector DB | `id`, `question`, `sql`, `tag`, `created_at` | Stores NL2SQL pairs |
| `ddl_statements` | Vector DB | `id`, `ddl`, `created_at` | Schema definitions |
| `documentation` | Vector DB | `id`, `content`, `created_at` | Business context |
| `sql_functions` | Vector DB | `function_name`, `sql_template`, `post_processing_code`, `arguments` | Reusable functions |
| `related_training_data_cache` | In-Memory | `question`, `cached_training_data` | Speeds up repeated queries |

### **Mermaid Diagram: erDiagram**
```mermaid
erDiagram
    TRAINING_DATA ||--o{ QUESTION_SQL_PAIR : contains
    TRAINING_DATA ||--o{ DDL_STATEMENT : references
    TRAINING_DATA ||--o{ DOCUMENTATION : includes
    SQL_FUNCTIONS ||--o{ QUESTION_SQL_PAIR : uses
    QUESTION_SQL_PAIR {
        string id PK
        string question
        string sql
        string tag
        datetime created_at
    }
    DDL_STATEMENT {
        string id PK
        string ddl
        datetime created_at
    }
    DOCUMENTATION {
        string id PK
        string content
        datetime created_at
    }
    SQL_FUNCTIONS {
        string function_name PK
        string description
        string sql_template
        string post_processing_code
        string arguments
    }
```

---

## **6. Workflow & Logic**

### **Main Operations**

#### **1. Training Data Ingestion**
```mermaid
flowchart TD
    A[User submits question and SQL] --> B[Generate deterministic UUID]
    B --> C[Store in vector database]
    C --> D[Update cache with related data]
    D --> E[Return success]
```

#### **2. Semantic Search & Retrieval**
```mermaid
flowchart TD
    A[User asks: "Top 5 US customers"] --> B[Generate embedding]
    B --> C[Search vector DB for similar questions]
    C --> D[Retrieve related DDL and documentation]
    D --> E[Build context for LLM]
    E --> F[Generate SQL]
```

#### **3. Function Generation**
```mermaid
stateDiagram
    [*] --> Idle
    Idle --> Generating: User requests function
    Generating --> Instantiating: Generate template
    Instantiating --> Running: Instantiate with arguments
    Running --> Success: Return SQL + Plotly code
    Success --> Idle
    Generating --> Error: Failed to generate
    Error --> Idle
```

### **Business Rules**
- All training data entries must have a **deterministic UUID** based on input content.
- `add_question_sql()` returns the same ID for identical inputs.
- `get_related_training_data_cached()` uses in-memory caching to avoid redundant API calls.
- `remove_training_data()` only deletes by ID; no batch deletion.
- `get_training_data()` returns all data, including metadata.

---

## **7. Sequence & Interaction Flow**

### **Sequence Diagram: User Query Lifecycle**
```mermaid
sequenceDiagram
    participant User
    participant VannaFlaskAPI
    participant VannaDB_VectorStore
    participant LLM
    participant Database

    User->>VannaFlaskAPI: Ask: "Top 5 US customers"
    VannaFlaskAPI->>VannaDB_VectorStore: get_similar_question_sql(question)
    VannaDB_VectorStore->>VannaDB_VectorStore: Check cache
    VannaDB_VectorStore->>VannaDB_VectorStore: Fetch related DDL & docs
    VannaDB_VectorStore->>LLM: Generate SQL with context
    LLM->>VannaDB_VectorStore: Return SQL
    VannaDB_VectorStore->>Database: Execute SQL
    Database->>VannaDB_VectorStore: Return DataFrame
    VannaDB_VectorStore->>VannaFlaskAPI: Return results
    VannaFlaskAPI->>User: Show SQL, table, chart
```

---

## **8. Error Handling**

### **Known Errors & Exceptions**
| Error | Code | Handling |
|------|------|----------|
| `API Error` | `500` | Retry with exponential backoff |
| `Invalid SQL` | `400` | Call `fix_sql()` with error |
| `Authentication Failed` | `401` | Redirect to login |
| `No Training Data` | `404` | Show prompt to add data |
| `Model Not Trained` | `400` | Train model first |

### **Error Handling Strategy**
- All API calls wrapped in try-except blocks.
- `retry` decorator used for network failures.
- `fix_sql()` automatically retries with error context.
- `log()` method used for debugging (via WebSocket in debug mode).
- Custom error classes: `VannaError`, `VectorStoreError`, `LLMError`.

### **Retry, Timeout, Fallback**
- **Timeout**: 30 seconds for LLM calls
- **Retry**: 3 attempts with 1s, 2s, 4s backoff
- **Fallback**: Use cached SQL if LLM fails
- **Fallback Model**: If OpenAI fails, try Cohere

---

## **9. Security Considerations**

### **Auth Checks**
- All endpoints protected by `@requires_auth`
- `AuthInterface` abstracts auth logic (JWT, OAuth, NoAuth)
- `is_logged_in()` checks session validity

### **Role-Based Access**
- `user_id` and `project_id` passed in context
- Access controlled per project
- Admins can delete any training data

### **Data Protection**
- **Encryption**: Data at rest (disk) and in transit (HTTPS)
- **Secrets**: API keys stored in `.env`, not code
- **Input Sanitization**: Prevents injection via `sanitize_model_name`
- **No Data Exposure**: `allow_llm_to_see_data=False` by default

---

## **10. Testing**

### **Unit & Integration Test Summary**
- **Unit Tests**: 95% coverage on `VannaDB_VectorStore`
- **Integration Tests**: End-to-end with FAISS, ChromaDB, VannaDB
- **Mocked LLMs**: Use `unittest.mock` for LLM responses
- **Database Tests**: Use in-memory SQLite for testing

### **How to Run Tests**
```bash
# Run all tests
pytest tests/

# Run specific test
pytest tests/test_vector_store.py -v

# Run with coverage
pytest tests/ --cov=vanna --cov-report=html
```

### **Sample Test Cases**
```python
def test_deterministic_uuid():
    vn = VannaDB_VectorStore(...)
    id1 = vn.add_question_sql("Q", "SELECT * FROM t")
    id2 = vn.add_question_sql("Q", "SELECT * FROM t")
    assert id1 == id2

def test_get_related_training_data():
    vn = VannaDB_VectorStore(...)
    vn.add_question_sql("Q", "SELECT * FROM t")
    data = vn.get_related_training_data_cached("Q")
    assert len(data.questions) > 0
```

---

## **11. Logging & Observability**

### **Logs Emitted**
| Level | Message |
|------|--------|
| `INFO` | "Added training data: id=abc123" |
| `DEBUG` | "Searching vector DB for similar questions" |
| `ERROR` | "Failed to connect to VannaDB" |
| `WARNING` | "Cache miss for question: 'Top 5 customers'" |

### **Metrics Generated**
- `training_data_added_total` (counter)
- `vector_search_duration_seconds` (histogram)
- `llm_call_count` (counter)
- `sql_execution_time_seconds` (gauge)

### **Dashboards & Alerts**
- **Grafana Dashboard**: Real-time query performance, cache hit rate
- **Alerts**: 
  - Cache hit rate < 80%
  - LLM latency > 5s
  - Failed training data adds

### **Mermaid Diagram: graph TD**
```mermaid
graph TD
    A[Module] --> B[Logging System: Structured JSON]
    B --> C[Monitoring: Prometheus]
    C --> D[Alerting: Alertmanager]
    C --> E[Visualization: Grafana]
    D --> F[Slack/Email]
    E --> G[Dashboard: Query Performance]
```

---

## **12. Performance**

### **Known Issues**
- FAISS in-memory: Limited by RAM
- Vector search: O(n) in worst case
- First query: Slower due to cache warm-up

### **Benchmarks**
| Operation | Avg Time | Max Time |
|---------|----------|----------|
| `add_question_sql` | 120ms | 300ms |
| `get_similar_question_sql` | 80ms | 200ms |
| `get_training_data()` | 1.2s | 3.5s |
| `get_related_ddl()` | 60ms | 150ms |

### **Scaling Recommendations**
- Use **disk-based FAISS** for large datasets
- Deploy **Redis** for distributed caching
- Use **OpenSearch** for high-throughput vector search
- Scale LLMs with **Xinference** or **Kubernetes**
- Use **load balancer** for Flask API

---

## **13. How to Extend or Modify**

### **Tips for Adding New Features**
- Extend `VannaBase` with new methods
- Implement new `VectorStore` backend (e.g., Pinecone)
- Add new LLM provider (e.g., Anthropic)
- Create custom `AuthInterface` (e.g., SSO)

### **Key Extension Points**
- `VannaBase.train()`
- `VannaBase.get_sql()`
- `VannaBase.connect_to_database()`
- `Cache` interface
- `AuthInterface`

### **Known TODOs**
- Add support for **multi-tenant** vector stores
- Implement **vector database migration** tool
- Add **data lineage tracking**
- Support **real-time training updates**

---

## **14. Versioning & Change Log**

| Version | Date | Changes |
|--------|------|--------|
| 1.0 | 2025-04-05 | Initial release |
| 1.1 | 2025-05-10 | Added `get_related_training_data_cached()` |
| 1.2 | 2025-06-15 | Added `VannaDB_VectorStore` support |
| 1.3 | 2025-07-20 | Added `deterministic_uuid` for reproducibility |

### **Git History**
- [PR #123: Add deterministic UUIDs](https://github.com/vanna-ai/vanna/pull/123)
- [PR #145: Support VannaDB backend](https://github.com/vanna-ai/vanna/pull/145)
- [PR #167: Add caching layer](https://github.com/vanna-ai/vanna/pull/167)

---

## **15. Mermaid Diagram Summary**

| Diagram | Type | Description |
|--------|------|-------------|
| System Interaction | sequenceDiagram | Full user query lifecycle |
| Data Model | erDiagram | Relationships between training data, DDL, docs |
| Workflow | flowchart TD | Training data ingestion and retrieval |
| Dependencies | graph LR | Internal and external component connections |
| Logging/Monitoring | graph TD | Log → Prometheus → Grafana → Alerting |

--- 

✅ **Module Recreated Successfully**  
This documentation provides a complete, self-contained blueprint for recreating the **Database Integration & Vector Stores** module from scratch, including all interfaces, data models, workflows, and integration points.
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
 * - initialData?: Partial<Database Integration & Vector Stores> (for edit mode)
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
import { DatabaseIntegrationFormValues, DatabaseIntegrationFormProps } from '../types/Database Integration & Vector StoresTypes';

const DatabaseIntegrationForm: React.FC<DatabaseIntegrationFormProps> = ({ onSubmit }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<DatabaseIntegrationFormValues>();

  const onSubmitHandler = (data: DatabaseIntegrationFormValues) => {
    onSubmit(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmitHandler)} className="space-y-6">
      <div>
        <label htmlFor="question" className="block text-sm font-medium text-gray-700">
          Question
        </label>
        <input
          type="text"
          id="question"
          {...register('question', { required: 'Question is required' })}
          className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        />
        {errors.question && (
          <p className="mt-1 text-sm text-red-500">{errors.question.message}</p>
        )}
      </div>

      <div>
        <label htmlFor="sql" className="block text-sm font-medium text-gray-700">
          SQL Query
        </label>
        <textarea
          id="sql"
          rows={4}
          {...register('sql', { required: 'SQL is required' })}
          className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        />
        {errors.sql && (
          <p className="mt-1 text-sm text-red-500">{errors.sql.message}</p>
        )}
      </div>

      <div>
        <label htmlFor="tag" className="block text-sm font-medium text-gray-700">
          Tag (Optional)
        </label>
        <input
          type="text"
          id="tag"
          {...register('tag')}
          className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <div>
        <label htmlFor="ddl" className="block text-sm font-medium text-gray-700">
          DDL Statement (Optional)
        </label>
        <textarea
          id="ddl"
          rows={3}
          {...register('ddl')}
          className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <div>
        <label htmlFor="documentation" className="block text-sm font-medium text-gray-700">
          Documentation (Optional)
        </label>
        <textarea
          id="documentation"
          rows={3}
          {...register('documentation')}
          className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <div className="flex justify-end">
        <button
          type="submit"
          className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
        >
          Submit
        </button>
      </div>
    </form>
  );
};

export default DatabaseIntegrationForm;