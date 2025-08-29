import './App.css';

const theme = {
  background: '#FFFFFF',
  text: '#1E293B',
  primary: '#3B82F6',
  secondary: '#F8FAFC'
};

function App() {
  return (
    <div style={{
      display: 'flex',
      minHeight: '100vh',
      backgroundColor: theme.background,
      color: theme.text
    }}>

      <aside style={{
        width: '220px',
        backgroundColor: theme.primary,
        color: 'white',
        padding: '1rem'
      }}>
        <h2>My App</h2>
        <nav>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            <li style={{ marginBottom: '1rem', cursor: 'pointer' }}>Dashboard</li>
            <li style={{ marginBottom: '1rem', cursor: 'pointer' }}>Profile</li>
            <li style={{ marginBottom: '1rem', cursor: 'pointer' }}>Settings</li>
          </ul>
        </nav>
      </aside>

      <main style={{ flex: 1, padding: '2rem' }}>
        <h1>Welcome to the Dashboard</h1>
        <p>This is your main content area. Here are some quick stats and features you can display:</p>

        <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
          <div style={{ flex: 1, padding: '1rem', backgroundColor: '#f5f5f5', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
            <h3>Total Users</h3>
            <p>1,250</p>
          </div>
          <div style={{ flex: 1, padding: '1rem', backgroundColor: '#f5f5f5', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
            <h3>Active Sessions</h3>
            <p>89</p>
          </div>
          <div style={{ flex: 1, padding: '1rem', backgroundColor: '#f5f5f5', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
            <h3>Notifications</h3>
            <p>5 unread</p>
          </div>
        </div>

        <button style={{
          backgroundColor: theme.primary,
          color: 'white',
          border: 'none',
          padding: '10px 20px',
          borderRadius: '5px',
          marginRight: '10px'
        }}>Primary Color</button>

        <button style={{
          backgroundColor: theme.secondary,
          color: 'white',
          border: 'none',
          padding: '10px 20px',
          borderRadius: '5px'
        }}>Secondary Color</button>

      </main>
    </div>
  );
}

export default App;
