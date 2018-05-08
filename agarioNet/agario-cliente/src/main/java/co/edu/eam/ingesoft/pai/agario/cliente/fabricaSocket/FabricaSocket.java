/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.eam.ingesoft.pai.agario.cliente.fabricaSocket;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Alejandro Esta fabrica genera un socket cuando haya una conexion
 *         cliente - servidor
 */
public class FabricaSocket {

	private String ip;

	private int puerto;

	public FabricaSocket(String ip, int puerto) {
		this.ip = ip;
		this.puerto = puerto;
	}

	/**
	 * Metodo que genera un socket con los datos indicados
	 * @return, un socket de conexion
	 * @throws IOException
	 */
	public Socket generarSocket() throws IOException {
		Socket socket = new Socket(ip, puerto);
		return socket;
	}
}
