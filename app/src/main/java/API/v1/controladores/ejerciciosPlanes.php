<?php

//require 'datos/ConexionBD.php';

class ejerciciosPlanes
{

    const NOMBRE_TABLA = "ejerciciosplanes";
    const ID_EP = "idEP";
    const REPETICIONES = "repeticiones";
    const ID_PLAN = "idPlan";
    const ID_EJERCICIO = "idEjercicio";

    const CODIGO_EXITO = 1;
    const ESTADO_EXITO = 1;
    const ESTADO_ERROR = 2;
    const ESTADO_ERROR_BD = 3;
    const ESTADO_ERROR_PARAMETROS = 4;
    const ESTADO_NO_ENCONTRADO = 5;

    public static function get($peticion)
    {
        //$idPlan = EPs::autorizar();
        if (!empty($peticion[0]))
            return self::obtenerEP($peticion[0]);

    }

    public static function post($peticion)
    {
        //$idPlan = clientes::autorizar();

        $body = file_get_contents('php://input');
        $EP = json_decode($body);
        $idPlan=$peticion[0];
        $idEjercicio=$peticion[1];
        $idEP = EjerciciosPlanes::crear($idPlan,$idEjercicio, $EP);

        http_response_code(201);
        return [
            "estado" => self::CODIGO_EXITO,
            "mensaje" => "EjerciciosPlanes creado",
            "id" => $idEP
        ];

    }

    public static function put($peticion)
    {

        if (!empty($peticion[0])) {
            $body = file_get_contents('php://input');
            $EP = json_decode($body);
            if (self::actualizar($EP, $peticion[0]) > 0) {

                http_response_code(200);
                return [
                    "estado" => self::CODIGO_EXITO,
                    "mensaje" => "Registro actualizado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El EP al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_ERROR_PARAMETROS, "Falta id", 422);
        }
    }

    public static function delete($peticion)
    {

        if ($peticion[0]==="idEjercicio") {
            if (!empty($peticion[1]) && self::eliminar($peticion[1]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::CODIGO_EXITO,
                    "mensaje" => "Registro eliminado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El idEjercicio al que intentas acceder no existe", 404);
            }
        } else if ($peticion[0]==="idPlan") {
            if (!empty($peticion[1]) && self::eliminar2($peticion[1]) > 0) {
                http_response_code(200);
                return [
                    "estado" => self::CODIGO_EXITO,
                    "mensaje" => "Registro eliminado correctamente"
                ];
            } else {
                throw new ExcepcionApi(self::ESTADO_NO_ENCONTRADO,
                    "El idPlan al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_ERROR_PARAMETROS, "Falta id", 422);
        }

    }

    /**
     * Obtiene la colección de EjerciciosPlanes o un solo EP indicado por el identificador
     * @param int $idPlan identificador del cliente
     * @param null $idEP identificador del EP (Opcional)
     * @return array registros de la tabla EP
     * @throws Exception
     */
    private function obtenerEP($idPlan)
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

    /**
     * Añade un nuevo EP asociado a un usuario
     * @param int $idPlan identificador del usuario
     * @param mixed $EP datos del EP
     * @return string identificador del EP
     * @throws ExcepcionApi
     */
    private function crear($idPlan,$idEjercicio, $EP)
    {
        if ($EP) {
            try {

                $pdo = ConexionBD::obtenerInstancia()->obtenerBD();

                // Sentencia INSERT
                $comando = "INSERT INTO " . self::NOMBRE_TABLA . " ( " .
                    self::ID_EP . "," .
                    self::REPETICIONES . "," .
                    self::ID_PLAN . "," .
                    self::ID_EJERCICIO . ")" .
                    " VALUES(?,?,?,?)";

                // Preparar la sentencia
                $repeticiones = $EP->repeticiones;
                $idEP = $EP->idEP;
                $sentencia = $pdo->prepare($comando);
                $sentencia->bindParam(1, $idEP);
                $sentencia->bindParam(2, $repeticiones);
                $sentencia->bindParam(3, $idPlan);
                $sentencia->bindParam(4, $idEjercicio);


                

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
     * @param object $EP objeto con los valores nuevos del EP
     * @param int $idEP
     * @return PDOStatement
     * @throws Exception
     */
    private function actualizar($EP, $idEP)
    {
        try {
            // Creando consulta UPDATE
            
            //$consulta2 = "UPDATE ejerciciosplanes SET repeticiones = '20' WHERE idEP"
            $consulta = "UPDATE " . self::NOMBRE_TABLA .
                " SET " . self::REPETICIONES . "=?" .
                " WHERE " . self::ID_EP . "=?";
                

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($consulta);
            $repeticiones = $EP->repeticiones;
            $sentencia->bindParam(1, $repeticiones);
            $sentencia->bindParam(2, $idEP);
            // Ejecutar la sentencia
            echo "$consulta";
            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }


    /**
     * Elimina un EP asociado a un usuario
     * @param int $idPlan identificador del usuario
     * @param int $idEP identificador del EP
     * @return bool true si la eliminación se pudo realizar, en caso contrario false
     * @throws Exception excepcion por errores en la base de datos
     */
    private function eliminar($idEjercicio)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::ID_EJERCICIO . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idEjercicio);

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
}

