import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

import EventsDashboard from './components/EventsDashboard';
import EventPage from './components/EventPage';
import EventDetails from './components/EventDetails';
import AddTickets from './components/AddTickets';

function App() {
  return (
    <Router>
      <Routes>

        {/* Redirect root to dashboard */}
        <Route path="/" element={<Navigate to="/events" replace />} />

        {/* Dashboard */}
        <Route path="/events" element={<EventsDashboard />} />

        {/* Create Event */}
        <Route path="/events/add" element={<EventPage />} />

        {/* Event Details */}
        <Route path="/events/:id" element={<EventDetails />} />

        {/* Add Tickets */}
        <Route
          path="/events/:eventId/products"
          element={<AddTickets />}
        />

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/events" replace />} />

      </Routes>
    </Router>
  );
}

export default App;
