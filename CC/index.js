// Definisi Library yang digunakan
const express = require('express');
const session = require('express-session');
const bodyParser = require('body-parser');
const app = express();

// Definisi lokasi file router
const registerRoute = require('./routes/router-register');
const loginRoute = require('./routes/router-login');
const predictRoute = require('./routes/router-predict');

// Configurasi dan gunakan library
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())

app.get("/", (req, res) => {
    console.log("Response success")
    res.send("Response Success!")
})

// Configurasi library session
app.use(session({
    resave: false,
    saveUninitialized: false,
    secret: 'P@ss0rd!@#',
    name: 'secretName',
    cookie: {
        sameSite: true,
        maxAge: 60000
    },
}))

// Gunakan routes yang telah didefinisikan
app.use('/register', registerRoute);
app.use('/login', loginRoute);
app.use('/predict', predictRoute);

// Gunakan port server
app.listen(8080, () => {
    console.log('Server Berjalan di Port : ' + 8080);
});