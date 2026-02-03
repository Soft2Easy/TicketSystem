import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

function AddProducts() {
  const { eventId } = useParams();
  const navigate = useNavigate();

  const [tickets, setTickets] = useState([
    { name: '', description: '', price: '' }
  ]);
  const [error, setError] = useState(null);

  const handleChange = (i, field, value) => {
    const updated = [...tickets];
    updated[i][field] = value;
    setTickets(updated);
  };

  const addTicket = () => {
    setTickets([...tickets, { name: '', description: '', price: '' }]);
  };

  const submitTickets = async e => {
    e.preventDefault();

    try {
      for (const t of tickets) {
        const res = await fetch('/products/create', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            eventId: Number(eventId),
            name: t.name,
            description: t.description,
            price: Number(t.price)
          })
        });

        if (!res.ok) throw new Error('Ticket creation failed');
      }

      alert('Tickets created successfully');
      navigate('/events');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="card shadow-sm">
      <div className="card-body">
        <h3>Add Tickets for Event #{eventId}</h3>

        {error && <div className="alert alert-danger">{error}</div>}

        <form onSubmit={submitTickets}>
          {tickets.map((t, i) => (
            <div key={i} className="border p-3 mb-3">
              <input className="form-control mb-2" placeholder="Ticket name" onChange={e => handleChange(i, 'name', e.target.value)} required />
              <input className="form-control mb-2" placeholder="Description" onChange={e => handleChange(i, 'description', e.target.value)} />
              <input type="number" className="form-control" placeholder="Price" onChange={e => handleChange(i, 'price', e.target.value)} required />
            </div>
          ))}

          <button type="button" className="btn btn-secondary mb-2" onClick={addTicket}>
            + Add another ticket
          </button>

          <button className="btn btn-primary">Save Tickets</button>
        </form>
      </div>
    </div>
  );
}

export default AddProducts;
