<?php

//require 'datos/ConexionBD.php';

class seguimiento
{

    const NOMBRE_TABLA = "seguimiento";
	const ID_SEGUIMIENTO = "idSeguimiento";
    const ID_PU = "idPU";
    const FECHA = "fecha";
	const COMENTARIOS = "comentarios";
	const SATISFACCION = "satisfaccion";

    const CODIGO_EXITO = 1;
    const ESTADO_EXITO = 1;
    const ESTADO_ERROR = 2;
    const ESTADO_ERROR_BD = 3;
    const ESTADO_ERROR_PARAMETROS = 4;
    const ESTADO_NO_ENCONTRADO = 5;

    public static function get($peticion)
    {

		if(!empty($peticion[0]))
			return self::obtenerSeguimiento($peticion[0]);

    }

    public static function post($peticion)
    {

        $body = file_get_contents('php://input');
        $seguimiento = json_decode($body);
        $idPU=$peticion[0];

        $idSeguimiento = Seguimiento::crear($idPU, $seguimiento);

        http_response_code(201);
        return [
            "estado" => self::CODIGO_EXITO,
            "mensaje" => "Seguimiento creado",
            "id" => $idSeguimiento
        ];

    }

    public static function delete($peticion)
    {

        if ($peticion[0]==='idPU') {
            if (self::eliminar($peticion[1]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::CODIGO_EXITO,
                    "mensaje" => "Registro eliminado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El PU al que intentas acceder no existe", 404);
            }
        } if ($peticion[0]==='idPlan') {
            if (self::eliminar2($peticion[1]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::CODIGO_EXITO,
                    "mensaje" => "Registro eliminado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El idPlan al que intentas acceder no existe", 404);
            }
        } if ($peticion[0]==='idPaciente') {
            if (self::eliminar3($peticion[1]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::CODIGO_EXITO,
                    "mensaje" => "Registro eliminado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El idPaciente al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_ERROR_PARAMETROS, "Falta id", 422);
        }

    }

    /**
     * Obtiene la colección de Seguimiento o un solo PU indicado por el identificador
     * @param int $idPlan identificador del cliente
     * @param null $idPU identificador del PU (Opcional)
     * @return array registros de la tabla PU
     * @throws Exception
     */
    private function obtenerSeguimiento($idPU)
    {
        try {
            
                $comando = "SELECT * FROM " . self::NOMBRE_TABLA .
                    " WHERE " . self::ID_PU . "=?";
                
                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idPU
                $sentencia->bindParam(1, $idPU, PDO::PARAM_INT);

           

            // Ejecutar sentencia preparada
            if ($sentencia->execute()) {
                http_response_code(200);
                return $sentencia->fetchAll(PDO::FETCH_ASSOC);
                    /*[
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $sentencia->fetchAll(PDO::FETCH_ASSOC)
                    ];*/
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }
	
    /**
     * Añade un nuevo PU asociado a un usuario
     * @param int $idPU identificador del usuario
     * @param mixed $PU datos del PU
     * @return string identificador del PU
     * @throws ExcPUcionApi
     */
    private function crear($idPU, $seguimiento)
    {
        if ($seguimiento) {
            try {

                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();
                // Sentencia INSERT
                $comando = "INSERT INTO " . self::NOMBRE_TABLA . " ( " .
                    self::FECHA . "," .
                    self::ID_SEGUIMIENTO . "," .
					self::COMENTARIOS . "," .
					self::SATISFACCION . "," .
                    self::ID_PU . ")" .
                    " VALUES(?,?,?,?,?)";
                    
       
                // Preparar la sentencia
                $fecha = $seguimiento->fecha;
				$comentarios = $seguimiento->comentarios;
				$satisfaccion = $seguimiento->satisfaccion;
				$idSeguimiento = $seguimiento->idSeguimiento;

                $sentencia = $pdo->prepare($comando);
                $sentencia->bindParam(1, $fecha);
                $sentencia->bindParam(2, $idSeguimiento);
				$sentencia->bindParam(3, $comentarios);
				$sentencia->bindParam(4, $satisfaccion);
                $sentencia->bindParam(5, $idPU);


                

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

    /**
     * Elimina un seguimiento asociado a un usuario
     * @param int $idPU identificador del usuario
     * @param int $idPU identificador del seguimiento
     * @return bool true si la eliminación se pudo realizar, en caso contrario false
     * @throws Exception excepcion por errores en la base de datos
     */
    private function eliminar($idPU)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::ID_PU . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idPU);

            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }
    private function eliminar2($idPlan)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE  FROM seguimiento WHERE  idPU=(SELECT idPU FROM planesusuarios WHERE idPlan=?)";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idPlan);

            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }
    private function eliminar3($idPaciente)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE  FROM seguimiento WHERE  idPU=(SELECT idPU FROM planesusuarios WHERE idPaciente=?)";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idPaciente);

            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }
}

