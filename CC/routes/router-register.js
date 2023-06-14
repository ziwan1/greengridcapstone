const express = require('express');
const router = express.Router();
const db = require('../database');
const crypto = require('crypto');

// Register route
router.post('/', (req, res) => {
  const { email, password } = req.body;

  // Check if the email already exists
  const checkUserQuery = `SELECT * FROM table_user WHERE email = ?`;
  db.query(checkUserQuery, [email], (err, result) => {
    if (err) {
      console.error('Error executing query', err);
      return res.status(500).json({ message: 'Internal server error' });
    }

    // If the email already exists, send an error response
    if (result.length > 0) {
      return res.status(409).json({ message: 'Email already exists' });
    }

    // Generate the password hash using SHA512
    const passwordHash = crypto.createHash('sha512').update(password).digest('hex');

    // Insert the new user into the database
    const insertUserQuery = `INSERT INTO table_user (email, password) VALUES (?, ?)`;
    db.query(insertUserQuery, [email, passwordHash], (err, result) => {
      if (err) {
        console.error('Error executing query', err);
        return res.status(500).json({ message: 'Internal server error' });
      }

      return res.status(201).json({ message: 'User registered successfully' });
    });
  });
});

module.exports = router;