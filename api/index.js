const express = require('express');
const bodyParser = require('body-parser');
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
app.get('/tutor/:codigo', (req, res) => {
    let codigoTutor = req.params.codigo;
    let query = 'SELECT * FROM employees WHERE employees.manager_id = ?';
    let params = [codigoTutor];
    conn.query(query, params, (err, results) => {
        if (err) throw err;
        res.json(results);
    })
})

// retorna informacion de trabajador en especifico
app.get('/tutor/trabajador/:codigo', (req, res) => {
    let codigoTrabajador = req.params.codigo;
    let query = 'SELECT * FROM employees WHERE employees.employee_id = ?';
    let params = [codigoTrabajador]
    conn.query(query, params, (err, results) => {
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
                        res.json({'msg':'ok'})
                    })
                }
                else{
                    res.json({'msg':'Trabajador ocupado'});
                }
            })
            
        }
        else{
            res.json({'msg':'Trabajador inválido'});
        }
    })
})

app.get('/trabajador/tutoria/:codigo', (req, res) => {
    let query = 'SELECT * FROM employees WHERE meeting = 1 AND meeting_date is not null AND employee_id = ?'
    let params = [req.params.codigo];

    conn.query(query, params, (err, results) => {
        if (err) throw err;
        if (results && results.length){
            res.json({'msg':'ok'});
        }else{
            res.json({'msg':'No cuenta con tutoria'})
        }
    })
})

app.post('/trabajador/tutoria/', bodyParser.urlencoded({extended: true}), (req, res) => {
    let codigoTrabajador = req.body.codigoTrabajador;
    let feedback = req.body.feedback;

    let query1 = 'SELECT * FROM employees '+
    'WHERE meeting = 1 AND meeting_date < CURRENT_TIMESTAMP AND employee_feedback is null AND employee_id = ?';
    let params1 = [codigoTrabajador];

    conn.query(query1, params1, (err, results) => {
        if (err) throw err;
        if (results && results.length > 0){

            let query2 = 'UPDATE employees SET employee_feedback = ? WHERE employee_id = ?'
            let params2 = [feedback, codigoTrabajador];

            conn.query(query2, params2, (err, results) => {
                if (err) throw err;
                res.json({'msg':'ok'});
            })
        }
        else{
            res.json({'msg':'Tutoria inválida'})
        }
    })
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