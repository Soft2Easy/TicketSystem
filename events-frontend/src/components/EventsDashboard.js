import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';

function EventsDashboard() {
  const navigate = useNavigate();
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchEvents = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Try proxy first
      const response = await fetch('/all-events');
      const text = await response.text();
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }
      
      // Check if we got HTML (proxy failed)
      if (text.trim().startsWith('<!DOCTYPE') || text.trim().startsWith('<html')) {
        // Try direct connection
        const directResponse = await fetch('http://localhost:8081/all-events');
        const directText = await directResponse.text();
        
        if (!directResponse.ok) {
          throw new Error(`Direct connection failed: HTTP ${directResponse.status}`);
        }
        
        const data = JSON.parse(directText);
        setEvents(data);
      } else {
        const data = JSON.parse(text);
        setEvents(data);
      }
      
    } catch (err) {
      console.error('Error fetching events:', err);
      setError(err.message);
      setEvents([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEvents();
  }, []);

  // Format date for display
  const formatDate = (dateString) => {
    if (!dateString) return 'Not specified';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  // Format date range
  const formatDateRange = (startDate, endDate) => {
    if (!startDate || !endDate) return formatDate(startDate) || 'Not specified';
    
    const start = new Date(startDate);
    const end = new Date(endDate);
    
    // If same month and year
    if (start.getMonth() === end.getMonth() && start.getFullYear() === end.getFullYear()) {
      return `${start.getDate()} - ${end.getDate()} ${start.toLocaleDateString('en-US', { month: 'short', year: 'numeric' })}`;
    }
    
    // Different months
    return `${formatDate(startDate)} to ${formatDate(endDate)}`;
  };

  // Handle view details - NAVIGATE TO EVENT DETAILS PAGE
  const handleViewDetails = (event) => {
    if (event && event.id) {
      navigate(`/event/${event.id}`);
    } else {
      console.log('Event details:', event);
      alert(`Event Details:\n\nName: ${event.name}\nOrganizer: ${event.organizer?.name}\nVenue: ${event.venue?.name}\nLocation: ${event.venue?.city}, ${event.venue?.country}\nDates: ${formatDateRange(event.startDate, event.endDate)}`);
    }
  };

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header bg-primary text-white">
          <h1 className="h3 mb-0">Events Dashboard</h1>
        </div>
        
        <div className="card-body">
          {loading && (
            <div className="text-center py-4">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
              <p className="mt-2">Loading events...</p>
            </div>
          )}
          
          {error && (
            <div className="alert alert-danger" role="alert">
              <h4 className="alert-heading">Error Loading Events</h4>
              <p>{error}</p>
              <hr />
              <button 
                className="btn btn-danger"
                onClick={fetchEvents}
              >
                Retry
              </button>
            </div>
          )}
          
          {!loading && !error && (
            <>
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h2 className="h4">Events ({events.length})</h2>
                <div>
                  <button 
                    className="btn btn-sm btn-outline-primary me-2"
                    onClick={fetchEvents}
                  >
                    <i className="bi bi-arrow-clockwise me-1"></i>
                    Refresh
                  </button>
                  <button 
                    className="btn btn-sm btn-outline-success"
                    onClick={() => console.log('Events data:', events)}
                  >
                    <i className="bi bi-bug me-1"></i>
                    Debug Data
                  </button>
                </div>
              </div>
              
              {events.length === 0 ? (
                <div className="alert alert-info" role="alert">
                  <i className="bi bi-info-circle me-2"></i>
                  No events found. Try refreshing or check if the backend server is running.
                </div>
              ) : (
                <div className="table-responsive">
                  <table className="table table-striped table-hover">
                    <thead className="table-dark">
                      <tr>
                        <th scope="col">Event Name</th>
                        <th scope="col">Organizer</th>
                        <th scope="col">Venue</th>
                        <th scope="col">Location</th>
                        <th scope="col">Date Range</th>
                        <th scope="col" style={{ width: '150px' }}>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {events.map((event, index) => (
                        <tr key={event.id || index}>
                          <td>
                            <strong>{event.name || 'Unnamed Event'}</strong>
                            {event.organizer?.description && (
                              <div className="text-muted small mt-1">
                                <i className="bi bi-info-circle me-1"></i>
                                {event.organizer.description}
                              </div>
                            )}
                          </td>
                          <td>
                            <div className="d-flex align-items-center">
                              <i className="bi bi-person-badge me-2 text-primary"></i>
                              <div>
                                {event.organizer?.name || 'Not specified'}
                                {event.organizer?.id && (
                                  <div className="text-muted small">ID: {event.organizer.id}</div>
                                )}
                              </div>
                            </div>
                          </td>
                          <td>
                            <div className="d-flex align-items-center">
                              <i className="bi bi-building me-2 text-secondary"></i>
                              {event.venue?.name || 'Not specified'}
                            </div>
                          </td>
                          <td>
                            <div className="d-flex align-items-center">
                              <i className="bi bi-geo-alt me-2 text-success"></i>
                              <div>
                                {event.venue?.city && event.venue?.country 
                                  ? `${event.venue.city}, ${event.venue.country}`
                                  : event.venue?.city || event.venue?.country || 'Not specified'
                                }
                                {event.venue?.street && (
                                  <div className="text-muted small">{event.venue.street}</div>
                                )}
                              </div>
                            </div>
                          </td>
                          <td>
                            <div className="d-flex align-items-center">
                              <i className="bi bi-calendar-event me-2 text-warning"></i>
                              <div>
                                {formatDateRange(event.startDate, event.endDate)}
                                {event.startDate && event.endDate && (
                                  <div className="text-muted small">
                                    ({event.startDate} to {event.endDate})
                                  </div>
                                )}
                              </div>
                            </div>
                          </td>
                          <td>
                            <button 
                              className="btn btn-primary btn-sm w-100"
                              onClick={() => handleViewDetails(event)}
                              title="View full event details"
                            >
                              <i className="bi bi-eye me-1"></i>
                              View Details
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </>
          )}
        </div>
        
        <div className="card-footer text-muted">
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <small>
                <i className="bi bi-calendar-event me-1"></i>
                <strong>Total Events:</strong> {events.length}
              </small>
              {events.length > 0 && (
                <small className="ms-3">
                  <i className="bi bi-calendar-check me-1"></i>
                  <strong>Upcoming:</strong> {
                    events.filter(e => {
                      const start = e.startDate ? new Date(e.startDate) : null;
                      return start && start > new Date();
                    }).length
                  }
                </small>
              )}
            </div>
            <div>
              <small>
                <i className="bi bi-clock me-1"></i>
                Last updated: {new Date().toLocaleTimeString()}
              </small>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default EventsDashboard;