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
    
    // query para verificar que el trabajador corresponde al tutor
    let query1 = 'SELECT e.* FROM employees e '+
    'INNER JOIN employees m ON (e.manager_id = m.employee_id) '+
    'WHERE e.employee_id = ? AND m.employee_id = ?';
    let params1 = [codigoTrabajador, codigoTutor];

    conn.query(query1, params1, (err, results) => {
        if (err) throw err;
        if (results && results.length > 0){

            let query2 = 'SELECT * FROM employees WHERE meeting = 0 AND employee_id = ?';
            let params2 = [codigoTrabajador];

            conn.query(query2, params2, (err, results) => {
                if (err) throw err;
                if (results && results.length > 0){

                    let query3 = 'UPDATE employees SET meeting = 1, meeting_date = DATE_ADD(CURRENT_TIMESTAMP, interval 5 minute) '+
                    'WHERE employee_id = ?';
                    let params3 = [codigoTrabajador];

                    conn.query(query3, params3, (err, results) => {
                        if (err) throw err;
                        res.json({'result':'ok'})
                    })
                }
                else{
                    res.json({'error':'trabajador ocupado'});
                }
            })
            
        }
        else{
            res.json({'error':'trabajador invalido'});
        }
    })
})

app.listen(PORT, () => {
    console.log('Servidor corriendo en ',PORT);
})