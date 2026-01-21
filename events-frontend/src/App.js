import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import EventsDashboard from './components/EventsDashboard';
import EventDetails from './components/EventDetails';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
          <div className="container">
            <Link className="navbar-brand" to="/">
              <i className="bi bi-calendar-event me-2"></i>
              Event Management System
            </Link>
            <div className="navbar-nav">
              <Link className="nav-link" to="/">
                <i className="bi bi-house-door me-1"></i>
                Dashboard
              </Link>
            </div>
          </div>
        </nav>
        
        <div className="container mt-4">
          <Routes>
            <Route path="/" element={<EventsDashboard />} />
            <Route path="/event/:eventId" element={<EventDetails />} />
          </Routes>
        </div>
        
        <footer className="mt-5 py-3 bg-light border-top">
          <div className="container text-center">
            <small className="text-muted">
              © 2024 Event Management System | All events in South Africa
            </small>
          </div>
        </footer>
      </div>
    </Router>
  );
}

export default App;