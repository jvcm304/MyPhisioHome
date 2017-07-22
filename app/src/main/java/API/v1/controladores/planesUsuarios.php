<?php

//require 'datos/ConexionBD.php';

class planesUsuario
{

    const NOMBRE_TABLA = "planesusuarios";
    const ID_PU = "idPU";
    const SERIES = "series";
	const TIEMPO = "tiempo";
	const DIAS = "dias";
    const ID_PLAN = "idPlan";
    const ID_PACIENTE = "idPaciente";

    const CODIGO_EXITO = 1;
    const ESTADO_EXITO = 1;
    const ESTADO_ERROR = 2;
    const ESTADO_ERROR_BD = 3;
    const ESTADO_ERROR_PARAMETROS = 4;
    const ESTADO_NO_ENCONTRADO = 5;

    public static function get($peticion)
    {
        //$idPlan = PUs::autorizar();
        if ($peticion[0]=="plan"){
			if(!empty($peticion[1]))
				return self::obtenerPlanes($peticion[1]);
		}else if($peticion[0]=="paciente"){
			if(!empty($peticion[1]))
				return self::obtenerPacientes($peticion[1]);
		}

    }

    public static function post($peticion)
    {
        //$idPlan = clientes::autorizar();

        $body = file_get_contents('php://input');
        $PU = json_decode($body);
        $idPlan=$peticion[0];
        $idPaciente=$peticion[1];
        $idPU = planesUsuario::crear($idPlan,$idPaciente, $PU);

        http_response_code(201);
        return [
            "estado" => self::CODIGO_EXITO,
            "mensaje" => "EjerciciosPlanes creado",
            "id" => $idPU
        ];

    }

    public static function put($peticion)
    {

        if (!empty($peticion[0])) {
            $body = file_get_contents('php://input');
            $PU = json_decode($body);
            if (self::actualizar($PU, $peticion[0]) > 0) {

                http_response_code(200);
                return [
                    "estado" => self::CODIGO_EXITO,
                    "mensaje" => "Registro actualizado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El PU al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_ERROR_PARAMETROS, "Falta id", 422);
        }
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
        } else if ($peticion[0]==='idPlan') {
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
        } else if ($peticion[0]==='idPaciente') {
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
     * Obtiene la colección de EjerciciosPlanes o un solo PU indicado por el identificador
     * @param int $idPlan identificador del cliente
     * @param null $idPU identificador del PU (Opcional)
     * @return array registros de la tabla PU
     * @throws Exception
     */
    private function obtenerPlanes($idPlan)
    {
        try {
            
                $comando = "SELECT * FROM " . self::NOMBRE_TABLA .
                    " WHERE " . self::ID_PLAN . "=?";
                
                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idPlan
                $sentencia->bindParam(1, $idPlan, PDO::PARAM_INT);

           

            // Ejecutar sentencia preparada
            if ($sentencia->execute()) {
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $sentencia->fetchAll(PDO::FETCH_ASSOC)
                    ];
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }
	
	private function obtenerPacientes($idPaciente)
    {
        try {
            
                $comando = "SELECT * FROM " . self::NOMBRE_TABLA .
                    " WHERE " . self::ID_PACIENTE . "=?";
                
                // Preparar sentencia
                $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);
                // Ligar idPlan
                $sentencia->bindParam(1, $idPaciente, PDO::PARAM_INT);

           

            // Ejecutar sentencia preparada
            if ($sentencia->execute()) {
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_EXITO,
                        "datos" => $sentencia->fetchAll(PDO::FETCH_ASSOC)
                    ];
            } else
                throw new ExcepcionApi(self::ESTADO_ERROR, "Se ha producido un error");

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }
	
	

    /**
     * Añade un nuevo PU asociado a un usuario
     * @param int $idPlan identificador del usuario
     * @param mixed $PU datos del PU
     * @return string identificador del PU
     * @throws ExcPUcionApi
     */
    private function crear($idPlan,$idPaciente, $PU)
    {
        if ($PU) {
            try {

                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();

                // Sentencia INSERT
                $comando = "INSERT INTO " . self::NOMBRE_TABLA . " ( " .
                    self::ID_PU . "," .
                    self::TIEMPO . "," .
					self::SERIES . "," .
					self::DIAS . "," .
                    self::ID_PLAN . "," .
                    self::ID_PACIENTE . ")" .
                    " VALUES(?,?,?,?,?,?)";

                // Preparar la sentencia
                $tiempo = $PU->tiempo;
				$series = $PU->series;
				$dias = $PU->dias;
				$idPU = $PU->idPU;
                $sentencia = $pdo->prepare($comando);
                $sentencia->bindParam(1, $idPU);
                $sentencia->bindParam(2, $tiempo);
				$sentencia->bindParam(3, $series);
				$sentencia->bindParam(4, $dias);
                $sentencia->bindParam(5, $idPlan);
                $sentencia->bindParam(6, $idPaciente);


                

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
     * Actualiza el EP especificado por idPlan
     * @param int $idPlan
     * @param object $PU objeto con los valores nuevos del PU
     * @param int $idPU
     * @return PDOStatement
     * @throws Exception
     */
    private function actualizar($PU, $idPU)
    {
        try {
            // Creando consulta UPDATE
            $consulta = "UPDATE " . self::NOMBRE_TABLA .
                " SET " . self::TIEMPO . "=?," .
				" SET " . self::SERIES . "=?," .
				" SET " . self::DIAS . "=?," .
                " WHERE " . self::ID_PU . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($consulta);
            $tiempo = $PU->tiempo;
			$series = $PU->series;
			$dias = $PU->dias;
            $sentencia->bindParam(1, $tiempo);
			$sentencia->bindParam(2, $series);
			$sentencia->bindParam(3, $dias);
            $sentencia->bindParam(4, $idPU);
            // Ejecutar la sentencia
            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }


    /**
     * Elimina un PU asociado a un usuario
     * @param int $idPlan identificador del usuario
     * @param int $idPU identificador del PU
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
            $comando = "DELETE FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::ID_PLAN . "=?";

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
            $comando = "DELETE FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::ID_PACIENTE . "=?";

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

