const express = require('express');
const mysql = require('mysql2');
const app = express();

const PORT = '8080'

let conn = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'hr_v2'
})

conn.connect(err => {
    if (err) throw err;
    console.log('Conexion exitosa con database');
});

// retorna lista de trabajadores de tutor en especifico
app.get('/tutor/:codigoTutor', (req, res) => {
    let codigoTutor = req.params.codigoTutor;
    let query = 'SELECT * FROM employees WHERE employees.manager_id = '+codigoTutor;
    conn.query(query, (err, results) => {
        if (err) throw err;
        res.json(results);
    })
})

// retorna informacion de trabajador en especifico
app.get('/tutor/trabajador/:codigoTrabajador', (req, res) => {
    let codigoTrabajador = req.params.codigoTrabajador;
    let query = 'SELECT * FROM employees WHERE employees.employee_id = '+codigoTrabajador;
    conn.query(query, (err, results) => {
        if (err) throw err;
        res.json(results);
    })
})

// creacion de tutoria
app.post('/tutor/tutorias/:codigoTutor/:codigoTrabajador', (req, res) => {
    let codigoTutor = req.params.codigoTutor;
    let codigoTrabajador = req.params.codigoTrabajador;
    
    
})



// retorna informacion de trabajador
app.get('/trabajador/:codigoTrabajador', (req, res) => {
    let codigoTrabajador = req.params.codigoTrabajador;
    let query = 'SELECT * FROM employees WHERE employees.employee_id = '+codigoTrabajador;
    conn.query(query, (err, results) => {
        if (err) throw err;
        res.json(results);
    })
})

app.listen(PORT, () => {
    console.log('Servidor corriendo en ',PORT);
})