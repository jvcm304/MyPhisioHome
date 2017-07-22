<?php

require 'datos/ConexionBD.php';

class Pacientes
{
    // Datos de la tabla "Pacientes"
    const NOMBRE_TABLA = "pacientes";
    const ID_PACIENTE = "idPaciente";
    const NOMBRE = "nombre";
    const PASSWORD = "password";
    const EMAIL = "email";
    const IMAGEN = "imagen";
    const ESTATURA = "estatura";
    const SEXO = "sexo";
    const PESO = "peso";
    const APELLIDOS = "apellidos";
    const FECNACIMIENTO = "fecNacimiento";
    const CLAVE_API = "claveApi";

    const ESTADO_CREACION_EXITOSA = 1;
    const ESTADO_CREACION_FALLIDA = 2;
    const ESTADO_ERROR_BD = 3;
    const ESTADO_AUSENCIA_CLAVE_API = 4;
    const ESTADO_CLAVE_NO_AUTORIZADA = 5;
    const ESTADO_URL_INCORRECTA = 6;
    const ESTADO_FALLA_DESCONOCIDA = 7;
    const ESTADO_PARAMETROS_INCORRECTOS = 8;

    public static function post($peticion)
    {
        if ($peticion[0] == 'registro') {
            return self::registrar();
        } else if ($peticion[0] == 'login') {
            return self::loguear();
        } else {
            throw new ExcepcionApi(self::ESTADO_URL_INCORRECTA, "Url mal formada", 400);
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

    /**
     * Crea un nuevo usuario en la base de datos
     */

    
    private function registrar()
    {
        $cuerpo = file_get_contents('php://input');
        $usuario = json_decode($cuerpo);
		
        $resultado = self::crear($usuario);

        switch ($resultado) {
            case self::ESTADO_CREACION_EXITOSA:
                http_response_code(200);
                return
                    [
                        "estado" => self::ESTADO_CREACION_EXITOSA,
                        "mensaje" => utf8_encode("¡Registro con éxito!")
                    ];
                break;
            case self::ESTADO_CREACION_FALLIDA:
                throw new ExcepcionApi(self::ESTADO_CREACION_FALLIDA, "Ha ocurrido un error");
                break;
            default:
                throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA, "Falla desconocida", 400);
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
                    "El paciente al que intentas acceder no existe", 404);
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS, "Falta id", 422);
        }

    }
    /**
     * Elimina un paciente
     * @param int $idPaciente identificador del plan
     * @return bool true si la eliminación se pudo realizar, en caso contrario false
     * @throws Exception excepcion por errores en la base de datos
     */
	private function eliminar( $idPaciente)
    {
        try {
            // Sentencia DELETE
            $comando = "DELETE FROM " . self::NOMBRE_TABLA .
                " WHERE " . self::ID_PACIENTE . "=? " ;
            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $idPaciente);

            $sentencia->execute();

            return $sentencia->rowCount();

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

    /**
     * Crea un nuevo usuario en la tabla "pacientes"
     * @param mixed $datosUsuario columnas del registro
     * @return int codigo para determinar si la inserción fue exitosa
     */
    private function crear($datosUsuario)
    {
        $nombre = $datosUsuario->nombre;
		
        $password = $datosUsuario->password;
        $passwordEncriptada = self::encriptarpassword($password);
        $idPaciente =$datosUsuario->idPaciente;
        $email = $datosUsuario->email;
        $imagen = $datosUsuario->imagen;
        $estatura = $datosUsuario->estatura;
        $sexo = $datosUsuario->sexo;
        $peso = $datosUsuario->peso;
        $apellidos = $datosUsuario->apellidos;
        $fecNacimiento = $datosUsuario->fecNacimiento;
        $claveApi = self::generarClaveApi();

        try {

            $pdo = ConexionBD::obtenerInstancia()->obtenerBD();

            // Sentencia INSERT
            $comando = "INSERT INTO " . self::NOMBRE_TABLA . " ( " .
                self::ID_PACIENTE . "," .
                self::NOMBRE . "," .
                self::PASSWORD . "," .
                self::CLAVE_API . "," .
                self::EMAIL . "," .
                self::IMAGEN . "," .
                self::ESTATURA . "," .
                self::SEXO . "," .
                self::PESO . "," .
                self::FECNACIMIENTO . ")" .
                " VALUES(?,?,?,?,?,?,?,?,?,?)";
            $sentencia = $pdo->prepare($comando);
            $sentencia->bindParam(1, $idPaciente);
            $sentencia->bindParam(2, $nombre);
            $sentencia->bindParam(3, $passwordEncriptada);
            $sentencia->bindParam(4, $claveApi);
            $sentencia->bindParam(5, $email);
            $sentencia->bindParam(6, $imagen);
            $sentencia->bindParam(7, $estatura);
            $sentencia->bindParam(8, $sexo);
            $sentencia->bindParam(9, $peso);
            $sentencia->bindParam(10, $fecNacimiento);

            $resultado = $sentencia->execute();

            if ($resultado) {
                return self::ESTADO_CREACION_EXITOSA;
            } else {
                return self::ESTADO_CREACION_FALLIDA;
            }
        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }

    }

    /**
     * Protege la contraseña con un algoritmo de encriptado
     * @param $passwordPlana
     * @return bool|null|string
     */
    private function encriptarpassword($passwordPlana)
    {
        if ($passwordPlana)
            return password_hash($passwordPlana, PASSWORD_DEFAULT);
        else return null;
    }

    private function generarClaveApi()
    {
        return md5(microtime() . rand());
    }

    private function loguear()
    {
        $respuesta = array();

        $body = file_get_contents('php://input');
        $usuario = json_decode($body);

        $email = $usuario->email;
        $password = $usuario->password;


        if (self::autenticar($email, $password)) {
            $usuarioBD = self::obtenerUsuarioPoremail($email);

            if ($usuarioBD != NULL) {
                http_response_code(200);
                $respuesta["nombre"] = $usuarioBD["nombre"];
                $respuesta["email"] = $usuarioBD["email"];
                $respuesta["idPaciente"] = $usuarioBD["idPaciente"];
                $respuesta["imagen"] = $usuarioBD["imagen"];
                $respuesta["fecNacimiento"] = $usuarioBD["fecNacimiento"];
                $respuesta["estatura"] = $usuarioBD["estatura"];
                $respuesta["sexo"] = $usuarioBD["sexo"];
                $respuesta["peso"] = $usuarioBD["peso"];
                $respuesta["apellidos"] = $usuarioBD["apellidos"];
                $respuesta["claveApi"] = $usuarioBD["claveApi"];
                return ["estado" => 1, "usuario" => $respuesta];
            } else {
                throw new ExcepcionApi(self::ESTADO_FALLA_DESCONOCIDA,
                    "Ha ocurrido un error");
            }
        } else {
            throw new ExcepcionApi(self::ESTADO_PARAMETROS_INCORRECTOS,
                utf8_encode("email o contraseña inválidos"));
        }
    }

    private function autenticar($email, $password)
    {
        $comando = "SELECT password FROM " . self::NOMBRE_TABLA .
            " WHERE " . self::EMAIL . "=?";

        try {

            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

            $sentencia->bindParam(1, $email);

            $sentencia->execute();

            if ($sentencia) {
                $resultado = $sentencia->fetch();

                if (self::validarpassword($password, $resultado['password'])) {

                    return true;
                } else return false;
            } else {
                return false;
            }
        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }

    private function validarpassword($passwordPlana, $passwordHash)
    {   
        return password_verify($passwordPlana, $passwordHash);
    }


    private function obtenerUsuarioPoremail($email)
    {
        $comando = "SELECT " .
            self::NOMBRE . "," .
            self::PASSWORD . "," .
            self::EMAIL . "," .
            self::CLAVE_API . ",".
            self::IMAGEN . "," .
            self::ESTATURA . "," .
            self::SEXO . "," .
            self::PESO . "," .
            self::ID_PACIENTE . "," .
            self::APELLIDOS . "," .
            self::FECNACIMIENTO . " " .
            " FROM " . self::NOMBRE_TABLA .
            " WHERE " . self::EMAIL . "=?";

        $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

        $sentencia->bindParam(1, $email);

        if ($sentencia->execute())
            return $sentencia->fetch(PDO::FETCH_ASSOC);
        else
            return null;
    }

    /**
     * Otorga los permisos a un usuario para que acceda a los recursos
     * @return null o el id del usuario autorizado
     * @throws Exception
     */
    public static function autorizar()
    {
        $cabeceras = apache_request_headers();

        if (isset($cabeceras["Authorization"])) {

            $claveApi = $cabeceras["Authorization"];

            if (usuarios::validarClaveApi($claveApi)) {
                return usuarios::obtenerIdPaciente($claveApi);
            } else {
                throw new ExcepcionApi(
                    self::ESTADO_CLAVE_NO_AUTORIZADA, "Clave de API no autorizada", 401);
            }

        } else {
            throw new ExcepcionApi(
                self::ESTADO_AUSENCIA_CLAVE_API,
                utf8_encode("Se requiere Clave del API para autenticación"));
        }
    }

    /**
     * Comprueba la existencia de la clave para la api
     * @param $claveApi
     * @return bool true si existe o false en caso contrario
     */
    private function validarClaveApi($claveApi)
    {
        $comando = "SELECT COUNT(" . self::ID_PACIENTE . ")" .
            " FROM " . self::NOMBRE_TABLA .
            " WHERE " . self::CLAVE_API . "=?";

        $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

        $sentencia->bindParam(1, $claveApi);

        $sentencia->execute();

        return $sentencia->fetchColumn(0) > 0;
    }

    /**
     * Obtiene el valor de la columna "idUsuario" basado en la clave de api
     * @param $claveApi
     * @return null si este no fue encontrado
     */
    private function obtenerIdPaciente($claveApi)
    {
        $comando = "SELECT " . self::ID_PACIENTE .
            " FROM " . self::NOMBRE_TABLA .
            " WHERE " . self::CLAVE_API . "=?";

        $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($comando);

        $sentencia->bindParam(1, $claveApi);

        if ($sentencia->execute()) {
            $resultado = $sentencia->fetch();
            return $resultado['idPaciente'];
        } else
            return null;
    }
	
	public static function put($peticion)
    {
        if (!empty($peticion[0])) {
            $body = file_get_contents('php://input');
            $paciente = json_decode($body,false);
            $actualizado=self::actualizar( $paciente, $peticion[0]);
            if ( $actualizado > 0) {
                http_response_code(200);
                return [
                    "estado" => self::ESTADO_CREACION_EXITOSA,
                    "mensaje" => "Registro actualizado correctamente"
                ];
            } else {
                http_response_code(404);
                return [
                    "estado" => '0',
                    "mensaje" => "Registro no actualizado"
                ];
            }
            
        } else {
            throw new ExcepcionApi(self::ESTADO_ERROR_PARAMETROS, "Falta id", 422);
        }
    }
	
	/**
     * Actualiza el paciente 
     * @param int $
     * @param object $paciente objeto con los valores nuevos del paciente
     * @param int $idPaciente
     * @return PDOStatement
     * @throws Exception
     */
    private function actualizar( $paciente, $idPaciente)
    {
        try {
            // Creando consulta UPDATE
            $consulta = "UPDATE " . self::NOMBRE_TABLA .
                " SET " . self::NOMBRE . "=?," .
				self::EMAIL . "=?," .
				self::IMAGEN . "=?," .
				self::FECNACIMIENTO . "=?," .
				self::PESO . "=?," .
				self::ESTATURA . "=?," .
				self::SEXO . "=?" .
                " WHERE " . self::ID_PACIENTE . "=?";

            // Preparar la sentencia
            $sentencia = ConexionBD::obtenerInstancia()->obtenerBD()->prepare($consulta);
            $nombre = $paciente->nombre;
			$email = $paciente->email;
			$imagen = $paciente->imagen;
			$fecNacimiento = $paciente->fecNacimiento;
            $peso = $paciente->peso;
			$estatura = $paciente->estatura;
            $sexo = $paciente->sexo;
            $sentencia->bindParam(1, $nombre);
            $sentencia->bindParam(2, $email);
			$sentencia->bindParam(3, $imagen);
            $sentencia->bindParam(4, $fecNacimiento);
			$sentencia->bindParam(5, $peso);
            $sentencia->bindParam(6, $estatura);
			$sentencia->bindParam(7, $sexo);
            $sentencia->bindParam(8, $idPaciente);



            // Ejecutar la sentencia
            $sentencia->execute();
            $actualizados=$sentencia->rowCount();
            //echo $actuailizados;
            return $actualizados;

        } catch (PDOException $e) {
            throw new ExcepcionApi(self::ESTADO_ERROR_BD, $e->getMessage());
        }
    }
	
}

