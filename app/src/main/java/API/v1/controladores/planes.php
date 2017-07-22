<?php
//require 'datos/ConexionBD.php';

class planes
{
    // Datos de la tabla "usuario"
    const NOMBRE_TABLA = "planes";
    const ID_PLAN = "idPlan";
    const NOMBRE = "nombre";
    const DESCRIPCION = "descripcion";
    const CATEGORIA = "categoria";
    const SERIES = "series";
	const TIEMPO = "tiempo";
 

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
			$plan = json_decode($body);
            $idPlan = planes::crear($plan);

        http_response_code(201);
        return [
            "estado" => self::ESTADO_CREACION_EXITOSA,
            "mensaje" => "plan creado",
            "id" => $idPlan
        ];
        }
    }
	   /**
     * Añade un nuevo plan 
     * @param mixed $plan datos del plan
     * @return string identificador del plan
     * @throws ExcepcionApi
     */
    private function crear($plan)
    {
        if ($plan) {
            try {

                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();

                // Sentencia INSERT
                $comando = "INSERT INTO " . self::NOMBRE_TABLA . " ( " .
                    self::ID_PLAN . "," .
                    self::NOMBRE . "," .
                    self::DESCRIPCION . "," .
					self::CATEGORIA . ")" .
                    " VALUES(?,?,?,?)";

                // Preparar la sentencia
                $nombre = $plan->nombre;
                $descripcion = $plan->descripcion;
                $categoria = $plan->categoria;
                $series = $plan->series;
                $tiempo = $plan->tiempo;
                $idPlan = $plan->idPlan;

                $sentencia = $pdo->prepare($comando);
                $sentencia->bindParam(1, $idPlan);
                $sentencia->bindParam(2, $nombre);
                $sentencia->bindParam(3, $descripcion);
                $sentencia->bindParam(4, $categoria);

				
                $sentencia->execute();

                // Retornar en el último id insertado
                return $pdo->lastInsertId();

            } catch (PDOException $e) {
                throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
            }
        } else {
            throw new ExcepcionApi(
                self::ESTADO_ERROR_PARAMETROS,
                utf8_encode("Error en existencia o sintaxis de parámetros"));
        }

    }
    private function buscar()
    {
        $body = file_get_contents('php://input');
        $producto = json_decode($body);

        $nombre = $producto->nombre;

        $busqueda = "%".($nombre)."%";
        $comando = "SELECT * FROM " . self::NOMBRE_TABLA .
           " WHERE " . self::NOMBRE . " LIKE ? " ;
        //$comando = "SELECT * FROM planes WHERE nombre LIKE '%arne%'";

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


   public function get($peticion)
    {
        if(empty($peticion[0])){
            $comando = "SELECT * FROM " . self::NOMBRE_TABLA . "";

            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            if ($sentencia->execute())  {
               return $sentencia->fetchAll(PDO::FETCH_ASSOC);//PDO::FETCH_ASSOC);
            } else{
               return null;
            }
        }else{
            $comando = "SELECT * FROM " . self::NOMBRE_TABLA . 
            " WHERE " . self::ID_PLAN . "=? " ;
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
            $sentencia->bindParam(1, $peticion[0]);

            if ($sentencia->execute())  {
               return $sentencia->fetchAll(PDO::FETCH_ASSOC);//PDO::FETCH_ASSOC);
            } else{
               return null;
            }


        }
        
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
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El plan al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS, "Falta id", 422);
        }

    }
	/**
     * Elimina un plan
     * @param int $idPlan identificador del plan
     * @return bool true si la eliminación se pudo realizar, en caso contrario false
     * @throws Exception excepcion por errores en la base de datos
     */
	private function eliminar( $idPlan)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::ID_PLAN . "=? " ;
            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idPlan);

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
            $plan = json_decode($body);
            if (self::actualizar( $plan, $peticion[0]) > 0) {

                http_response_code(200);
                return [
                    "estado" => self::ESTADO_CREACION_EXITOSA,
                    "mensaje" => "Registro actualizado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El plan al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS, "Falta id", 422);
        }
    }
	
	/**
     * Actualiza el plan 
     * @param int $
     * @param object $plan objeto con los valores nuevos del plan
     * @param int $idPlan
     * @return PDOStatement
     * @throws Exception
     */
    private function actualizar( $plan, $idPlan)
    {
        try {
            // Creando consulta UPDATE
            $consulta = "UPDATE " . self::NOMBRE_TABLA .
                " SET " . self::NOMBRE . "=?," .
                self::DESCRIPCION . "=?," .
				self::CATEGORIA . "=?," .
				self::SERIES . "=?," .
				self::TIEMPO . "=?," .
                " WHERE " . self::ID_PLAN . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($consulta);
            $nombre = $plan->nombre;
            $descripcion = $plan->descripcion;
            $categoria = $plan->categoria;
			$series = $plan->series;
            $tiempo = $plan->tiempo;
            $sentencia->bindParam(1, $nombre);
            $sentencia->bindParam(2, $descripcion);
            $sentencia->bindParam(3, $categoria);
			$sentencia->bindParam(4, $series);
            $sentencia->bindParam(5, $tiempo);
            $sentencia->bindParam(6, $idPlan);



            // Ejecutar la sentencia
            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

}

