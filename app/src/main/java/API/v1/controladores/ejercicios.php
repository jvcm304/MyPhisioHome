<?php
//require 'datos/ConexionBD.php';

class ejercicios
{
    // Datos de la tabla "usuario"
    const NOMBRE_TABLA = "ejercicios";
    const ID_EJERCICIO = "idEjercicio";
    const NOMBRE = "nombre";
    const DESCRIPCION = "descripcion";
    const TIPS = "tips";
    const IMAGEN = "imagen";
    const CATEGORIA = "categoria";
	const TIPO = "tipo";
 

    const ESTADO_CREACION_EXITOSA = 1;
    const ESTADO_CREACION_FALLIDA = 2;
    const ESTADO_ERROR_BD = 3;
    const ESTADO_URL_INCORRECTA = 6;
    const ESTADO_FALLA_DESCONOCIDA = 7;
    const ESTADO_PARAMETROS_INCORRECTOS = 8;

    public static function post($peticion)
    {
        if ($peticion[0] == 'buscar') {
            return self::buscar();
        } else {
			$body = file_get_contents('php://input');
			$ejercicio = json_decode($body);
            $idEjercicio = ejercicios::crear($ejercicio);

        http_response_code(201);
        return [
            "estado" => self::ESTADO_CREACION_EXITOSA,
            "mensaje" => "ejercicio creado",
            "id" => $idEjercicio
        ];
        }
    }
	   /**
     * Añade un nuevo ejercicio 
     * @param mixed $ejercicio datos del ejercicio
     * @return string identificador del ejercicio
     * @throws ExcepcionApi
     */
    private function crear($ejercicio)
    {
        if ($ejercicio) {
            try {

                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();

                // Sentencia INSERT
                $comando = "INSERT INTO " . self::NOMBRE_TABLA . " ( " .
                    self::NOMBRE . "," .
                    self::ID_EJERCICIO . "," .
                    self::DESCRIPCION . "," .
					self::CATEGORIA . "," .
					self::TIPS . "," .
					self::IMAGEN . "," .
                    self::TIPO . ")" .
                    " VALUES(?,?,?,?,?,?,?)";

                // Preparar la sentencia
                $nombre = $ejercicio->nombre;
                $idEjercicio=$ejercicio->idEjercicio;
                $descripcion = $ejercicio->descripcion;
                $categoria = $ejercicio->categoria;
				$tips = $ejercicio->tips;
                $imagen = $ejercicio->imagen;
                $tipo = $ejercicio->tipo;

                $sentencia = $pdo->prepare($comando);
                $sentencia->bindParam(1, $nombre);
                $sentencia->bindParam(2, $idEjercicio);
                $sentencia->bindParam(3, $descripcion);
                $sentencia->bindParam(4, $categoria);
				$sentencia->bindParam(5, $tips);
                $sentencia->bindParam(6, $imagen);
                $sentencia->bindParam(7, $tipo);
				
                $sentencia->execute();

                // Retornar en el último id insertado
                return $pdo->lastInsertId();

            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }
        } else {
            throw new ExcepcionApi(
                self::ESTADO_PARAMETROS_INCORRECTOS,
                utf8_encode("Error en existencia o sintaxis de parámetros"));
        }

    }
    private function buscar()
    {
        $body = file_get_contents('php://input');
        $producto = json_decode($body);

        $idEjercicio = $producto->idEjercicio;

        //$busqueda = "%".($nombre)."%";
        $comando = "SELECT * FROM " . self::NOMBRE_TABLA .
           " WHERE " . self::ID_EJERCICIO . "=?";
        //$comando = "SELECT * FROM ejercicios WHERE nombre LIKE '%arne%'";

        try {
            $pdo = ConexionBD::obtenerInstancia()->obtenerBD();
            $sentencia = $pdo->prepare($comando);
            $sentencia->bindParam(1, $busqueda);
            $sentencia->execute();

            if ($sentencia) {
                $resultado = $sentencia->fetchAll(PDO::FETCH_ASSOC);

            } else {
                return false;
            }
        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }


    public function get()
    {
        $comando = "SELECT * FROM " . self::NOMBRE_TABLA . "";

        $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

        if ($sentencia->execute())  {
            return $sentencia->fetchAll(PDO::FETCH_ASSOC);//PDO::FETCH_ASSOC);
            }      
        else
            return null;
    }
	
	public static function delete($peticion)
    {
        if (!empty($peticion[0])) {
            if (self::eliminar($peticion[0]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::ESTADO_CREACION_EXITOSA,
                    "mensaje" => "Registro eliminado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA,
                    "El ejercicio al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS, "Falta id", 422);
        }

    }
	/**
     * Elimina un ejercicio
     * @param int $idEjercicio identificador del ejercicio
     * @return bool true si la eliminación se pudo realizar, en caso contrario false
     * @throws Exception excepcion por errores en la base de datos
     */
	private function eliminar( $idEjercicio)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::ID_EJERCICIO . "=? " ;
            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idEjercicio);

            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }
	
	
	public static function put($peticion)
    {
        if (!empty($peticion[0])) {
            $body = file_get_contents('php://input');
            $ejercicio = json_decode($body);
            if (self::actualizar( $ejercicio, $peticion[0]) > 0) {

                http_response_code(200);
                return [
                    "estado" => self::ESTADO_CREACION_EXITOSA,
                    "mensaje" => "Registro actualizado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA,
                    "El ejercicio al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS, "Falta id", 422);
        }
    }
	
	/**
     * Actualiza el ejercicio 
     * @param int $
     * @param object $ejercicio objeto con los valores nuevos del ejercicio
     * @param int $idEjercicio
     * @return PDOStatement
     * @throws Exception
     */
    private function actualizar( $ejercicio, $idEjercicio)
    {
        try {
            // Creando consulta UPDATE
            $consulta = "UPDATE " . self::NOMBRE_TABLA .
                " SET " . self::NOMBRE . "=?," .
                self::DESCRIPCION . "=?," .
				self::TIPS . "=?," .
				self::CATEGORIA . "=?," .
				self::IMAGEN . "=?," .
				self::TIPO . "=?," .
                " WHERE " . self::ID_EJERCICIO . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($consulta);
            $nombre = $ejercicio->nombre;
            $descripcion = $ejercicio->descripcion;
			$tips = $ejercicio->tips;
            $categoria = $ejercicio->categoria;
			$imagen = $ejercicio->imagen;
            $tipo = $ejercicio->tipo;
            $sentencia->bindParam(1, $nombre);
            $sentencia->bindParam(2, $descripcion);
			$sentencia->bindParam(3, $tips);
            $sentencia->bindParam(4, $categoria);
			$sentencia->bindParam(5, $imagen);
            $sentencia->bindParam(6, $tipo);
            $sentencia->bindParam(7, $idEjercicio);



            // Ejecutar la sentencia
            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

}

