import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

function EventDetails() {
  // ✅ FIX: param name must match route `/events/:id`
  const { id } = useParams();

  const [event, setEvent] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!id) {
      setError('Invalid event ID');
      setLoading(false);
      return;
    }

    fetchEventDetails();
  }, [id]);

  const fetchEventDetails = async () => {
    try {
      setLoading(true);
      setError(null);

      const [eventResponse, productResponse] = await Promise.all([
        fetch(`/events/${id}`),
        fetch(`/products?eventId=${id}`)
      ]);

      if (!eventResponse.ok) {
        throw new Error(`Event not found (HTTP ${eventResponse.status})`);
      }

      const eventData = await eventResponse.json();
      setEvent(eventData);

      // Products are optional – don’t fail the page if they error
      if (productResponse.ok) {
        const productData = await productResponse.json();
        setProducts(Array.isArray(productData) ? productData : []);
      } else {
        setProducts([]);
      }
    } catch (err) {
      console.error('Error fetching event details:', err);
      setError(err.message);
    } finally {
      setLoading(false); // ✅ prevents infinite spinner
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Not specified';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  // -------------------- UI STATES --------------------

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status" />
        <p className="mt-2">Loading event details...</p>
      </div>
    );
  }

  if (error || !event) {
    return (
      <div className="alert alert-danger">
        <h4>Unable to load event</h4>
        <p>{error || 'The event you are looking for does not exist.'}</p>
        <Link to="/" className="btn btn-primary">
          Back to Dashboard
        </Link>
      </div>
    );
  }

  return (
    <div className="event-details">
      <Link to="/" className="btn btn-outline-secondary mb-3">
        ← Back to Events
      </Link>

      <h1>{event.name}</h1>
      <p className="lead text-muted">
        {formatDate(event.startDate)} – {formatDate(event.endDate)}
      </p>

      {/* Event Info */}
      <div className="card mb-4">
        <div className="card-body">
          <h5>Organizer</h5>
          <p>
            <strong>{event.organizer?.name}</strong><br />
            {event.organizer?.description || 'No description'}
          </p>

          <h5>Venue</h5>
          <p>
            <strong>{event.venue?.name}</strong><br />
            {event.venue?.street}<br />
            {event.venue?.city}, {event.venue?.country}
          </p>
        </div>
      </div>

      {/* Products / Tickets */}
      {products.length > 0 ? (
        <div className="card mb-4">
          <div className="card-header bg-success text-white">
            <h3 className="h5 mb-0">Tickets</h3>
          </div>
          <div className="card-body">
            {products.map((product) => (
              <div key={product.id} className="border-bottom pb-3 mb-3">
                <h4>{product.name}</h4>
                <p>{product.description}</p>
                <div className="d-flex justify-content-between align-items-center">
                  <strong>R {product.price?.toLocaleString()}</strong>
                  <button className="btn btn-success">
                    Purchase Ticket
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      ) : (
        <div className="alert alert-info">
          No tickets available for this event.
        </div>
      )}
    </div>
  );
}

export default EventDetails;
