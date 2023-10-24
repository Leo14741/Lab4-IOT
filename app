const express = require('express');
const mysql = require('mysql2');
const fs = require('fs');
const path = require('path');

const app = express();

const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'salchipapa',
  database: 'hr_v2'
});

connection.connect();

app.get('/trabajadores', (req, res) => {
  connection.query('SELECT * FROM employees', (error, results, fields) => {
    if (error) throw error;
    res.json(results);
  });
});

app.get('/trabajadores/:manager_id', async (req, res) => {
    const codigoTutor = req.params.manager_id;
    try {
      const listaTrabajadores = await obtenerListaTrabajadoresPorCodigoTutor(codigoTutor);
  
      if (!listaTrabajadores || listaTrabajadores.length === 0) {
        return res.status(404).send('No se encontraron trabajadores para el tutor especificado.');
      }
  
      const textoLista = construirTextoLista(listaTrabajadores);
  
      const filePath = path.join(__dirname, `lista_trabajadores_${codigoTutor}.txt`);
      fs.writeFileSync(filePath, textoLista);
  
      res.sendFile(filePath, (err) => {
        if (err) {
          console.error(err);
          res.status(err.status).end();
        } else {
          // Archivo enviado con éxito, ahora eliminémoslo
          fs.unlinkSync(filePath);
        }
      });
    } catch (error) {
      console.error(error);
      res.status(500).send('Error interno del servidor');
    }
  });
  
  // Resto del código...
  
  
  
// Función para obtener la lista de trabajadores de un tutor
function obtenerListaTrabajadoresPorCodigoTutor(codigoTutor) {
    return new Promise((resolve, reject) => {
        const sql = `SELECT * FROM hr_v2.employees WHERE manager_id = ?`;
        connection.query(sql, [codigoTutor], function (error, results, fields) {
            if (error) reject(error);
            resolve(results);
        });
    });
}

  
  // Función para construir el texto de la lista en el formato adecuado
  function construirTextoLista(listaTrabajadores) {
    let texto = '';
    listaTrabajadores.forEach(trabajador => {
      texto += `${trabajador.first_name} ${trabajador.last_name}\n`;
    });
    return texto;
  }



app.get('/empleados/:employee_id', async (req, res) => {
    const id = req.params.employee_id;
    try {
      const empleado = await obtenerInfoEmpleado(id);
  
      if (!empleado || empleado.length === 0) {
        return res.status(404).json({ error: 'No se encontraron trabajadores para el tutor especificado.' });
      }
  
      res.json(empleado[0]);
    } catch (error) {
      console.error(error);
      res.status(500).json({ error: 'Error interno del servidor' });
    }
  });

// Función para obtener la info del empleado
function obtenerInfoEmpleado(id) {
    return new Promise((resolve, reject) => {
        const sql = `SELECT * FROM hr_v2.employees WHERE employee_id = ?`;
        connection.query(sql, [id], function (error, results, fields) {
            if (error) reject(error);
            resolve(results);
        });
    });
}

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log(`Servidor en ejecución en el puerto ${PORT}`);
});
