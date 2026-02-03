import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

function AddEvent() {
  const navigate = useNavigate();

  const [organizers, setOrganizers] = useState([]);
  const [venues, setVenues] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [form, setForm] = useState({
    name: '',
    startDate: '',
    endDate: '',
    organizerId: '',
    venueId: ''
  });

  /* -------------------- Load dropdown data -------------------- */
  useEffect(() => {
    Promise.all([
      fetch('/organizers').then(res => res.json()),
      fetch('/venues/all').then(res => res.json())
    ])
      .then(([orgs, vens]) => {
        setOrganizers(orgs);
        setVenues(vens);
      })
      .catch(() => setError('Failed to load data'));
  }, []);

  /* -------------------- Form helpers -------------------- */
  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  /* -------------------- Submit -------------------- */
  const handleSubmit = async e => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const res = await fetch('/event/create', {   // ✅ FIXED ENDPOINT
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          name: form.name,
          startDate: form.startDate,
          endDate: form.endDate,
          organizer: { id: Number(form.organizerId) },
          venue: { id: Number(form.venueId) }
        })
      });

      if (!res.ok) {
        throw new Error(`Event creation failed (${res.status})`);
      }

      const createdEvent = await res.json();

      if (!createdEvent?.id) {
        throw new Error('Event created but no ID returned from server');
      }

      console.log('Created event:', createdEvent);

      // ✅ CORRECT NAVIGATION
      navigate(`/events/${createdEvent.id}/products`);

    } catch (err) {
      console.error(err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  /* -------------------- UI -------------------- */
  return (
    <div className="card shadow-sm">
      <div className="card-body">
        <h3>Create Event</h3>

        {error && <div className="alert alert-danger">{error}</div>}

        <form onSubmit={handleSubmit}>
          <input
            className="form-control mb-2"
            name="name"
            placeholder="Event name"
            onChange={handleChange}
            required
          />

          <input
            type="date"
            className="form-control mb-2"
            name="startDate"
            onChange={handleChange}
            required
          />

          <input
            type="date"
            className="form-control mb-2"
            name="endDate"
            onChange={handleChange}
            required
          />

          <select
            className="form-select mb-2"
            name="organizerId"
            onChange={handleChange}
            required
          >
            <option value="">Select Organizer</option>
            {organizers.map(o => (
              <option key={o.id} value={o.id}>{o.name}</option>
            ))}
          </select>

          <select
            className="form-select mb-3"
            name="venueId"
            onChange={handleChange}
            required
          >
            <option value="">Select Venue</option>
            {venues.map(v => (
              <option key={v.id} value={v.id}>
                {v.name} — {v.city}
              </option>
            ))}
          </select>

          <button className="btn btn-primary" disabled={loading}>
            {loading ? 'Creating...' : 'Create Event & Add Tickets'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddEvent;
