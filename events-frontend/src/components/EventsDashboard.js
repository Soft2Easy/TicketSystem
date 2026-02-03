import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

function EventsDashboard() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  // -------------------- Fetch Events --------------------
  const fetchEvents = async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await fetch('/all-events'); // Make sure backend returns all events

      if (!response.ok) {
        throw new Error(`Failed to fetch events (HTTP ${response.status})`);
      }

      const data = await response.json();
      setEvents(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error('Failed to load events', err);
      setError('Unable to load events. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  // Fetch events on component mount
  useEffect(() => {
    fetchEvents();
  }, []);

  return (
    <>
      {/* Header */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className="mb-0">Events Dashboard</h2>

        <div className="d-flex gap-2">
          {/* Refresh Button */}
          <button
            className="btn btn-outline-primary"
            onClick={fetchEvents}
            disabled={loading}
          >
            {loading ? 'Refreshing...' : '🔄 Refresh'}
          </button>

          {/* Add Event Button */}
          {/* This ONLY navigates to the EventPage.js creation page */}
          <button
            className="btn btn-primary"
            onClick={() => navigate('/events/add')} // no ID is passed
          >
            ➕ Add Event
          </button>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}

      {/* Loading Spinner */}
      {loading && (
        <div className="text-center mt-5">
          <div className="spinner-border text-primary" role="status" />
        </div>
      )}

      {/* Empty State */}
      {!loading && !error && events.length === 0 && (
        <div className="alert alert-info">
          No events available. Click <strong>Add Event</strong> to create one.
        </div>
      )}

      {/* Events Grid */}
      {!loading && !error && events.length > 0 && (
        <div className="row">
          {events.map(event => (
            <div key={event.id} className="col-md-4 mb-4">
              <div className="card h-100 shadow-sm">
                <div className="card-body">
                  <h5 className="card-title">{event.name ?? 'Untitled Event'}</h5>

                  {/* Venue Info */}
                  <p className="card-text text-muted mb-1">
                    📍 {event.venue
                      ? `${event.venue.name}, ${event.venue.city}`
                      : 'Venue TBD'}
                  </p>

                  {/* Date Info */}
                  <p className="card-text text-muted">
                    📅 {event.startDate ?? 'Date not set'}
                  </p>
                </div>

                {/* View Details Button */}
                <div className="card-footer bg-white border-top-0">
                  <button
                    className="btn btn-sm btn-outline-secondary w-100"
                    onClick={() => navigate(`/events/${event.id}`)}
                  >
                    View Details
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </>
  );
}

export default EventsDashboard;
