const express = require('express');
const router = express.Router();
const db = require('../database');
const crypto = require('crypto');

// Login route
router.post('/', (req, res) => {
  const { email, password } = req.body;

  // Generate the password hash using SHA512
  const passwordHash = crypto.createHash('sha512').update(password).digest('hex');

  // Check if the email and password match a user in the database
  const loginQuery = `SELECT * FROM table_user WHERE email = ? AND password = ?`;
  db.query(loginQuery, [email, passwordHash], (err, result) => {
    if (err) {
      console.error('Error executing query', err);
      return res.status(500).json({ message: 'Internal server error' });
    }

    // If the email and password match, create a session and send a success response
    if (result.length > 0) {
      req.session.email = email;
      return res.status(200).json({ message: 'Login successful' });
    }

    // If the email and password don't match, send an error response
    return res.status(401).json({ message: 'Invalid email or password' });
  });
});

router.post('/logout', (req, res) => {
    req.session.destroy((err) => {
      if (err) {
        console.error('Error destroying session', err);
        return res.status(500).json({ message: 'Internal server error' });
      }
  
      return res.status(200).json({ message: 'Logout successful' });
    });
});

module.exports = router;