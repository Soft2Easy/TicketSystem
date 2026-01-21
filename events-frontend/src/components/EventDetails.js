import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';

function EventDetails() {
  const { eventId } = useParams();

  const [event, setEvent] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!eventId) return;
    fetchEventDetails();
  }, [eventId]);

  const fetchEventDetails = async () => {
    try {
      setLoading(true);
      setError(null);

      const [eventResponse, productResponse] = await Promise.all([
        fetch(`/events/${eventId}`),
        fetch(`/products?eventId=${eventId}`)
      ]);

      if (!eventResponse.ok) {
        throw new Error('Event not found');
      }

      if (!productResponse.ok) {
        throw new Error('Failed to load products');
      }

      const eventData = await eventResponse.json();
      const productData = await productResponse.json();

      setEvent(eventData);
      setProducts(productData); // <-- list of products
    } catch (err) {
      console.error('Error fetching details:', err);
      setError(err.message);
    } finally {
      setLoading(false);
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
        <h4>Event Not Found</h4>
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
            {event.organizer?.description}
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
      {products.length > 0 && (
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
                  <strong>R {product.price.toLocaleString()}</strong>
                  <button className="btn btn-success">
                    Purchase Ticket
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default EventDetails;
