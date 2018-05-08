/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.eam.ingesoft.pai.agario.cliente.controlador;

import co.edu.eam.ingesoft.pai.agario.cliente.fabricaSocket.FabricaSocket;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Comida;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Contrincante;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Particula;
import co.edu.eam.ingesoft.pai.agario.servidor.comm.HiloCliente;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author Alejandro
 */
public class ControladorVentanaCliente extends Observable implements Runnable {

	public static final String IP = "localhost";
	public static final int PUERTO = 45000;
	
    private PrintStream salida;//Flujo de salida
    
    private BufferedReader entrada;//Flujo de entrada
    
    private FabricaSocket fabrica;//Fabrica que genera el socket para la conexion
    
    private Socket connection;//Socket de conexion

    public ControladorVentanaCliente() {
    	fabrica = new FabricaSocket(IP, 45000);
    }

    public Socket getSocket() throws IOException {
        return fabrica.generarSocket();
    }

    /**
     * Método para conectar un usuario, realiza el login
     * @param nombreUsuario, es el nombre del usuario para conectarce en el juego
     * @return, true si conecta, false si no
     * @throws Exception
     */
    public boolean conectarse(String nombreUsuario) throws Exception {
        connection = getSocket();
        salida = new PrintStream(connection.getOutputStream());
        entrada = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        salida.println(HiloCliente.CASO_LOGIN);
        salida.println(nombreUsuario);
        String mensaje = (String) entrada.readLine();
        if (mensaje.equals(HiloCliente.OK)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metodo que envia la posicion actual de mi pelota
     * @param posicionPelotaX, es la posicion en x de la pelota
     * @param posicionPelotaY, es la posicion actual en y de la pelota
     * @param altoContenedor, alto del contenedor
     * @param anchoContenedor, ancho del contenedor
     * @param usuario, es el nombre del usuario
     * @throws Exception
     */
    public void enviarPosicion(int posicionPelotaX, int posicionPelotaY, int altoContenedor, int anchoContenedor, String usuario) throws Exception {
        salida.println(HiloCliente.CASO_MOVIMIENTO);
        salida.println(posicionPelotaX + "," + posicionPelotaY + "," + altoContenedor + "," + anchoContenedor + "," + usuario);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String orden = entrada.readLine();
                if (orden.equals(HiloCliente.PARTICULAS)) {
                    String cadenaMensaje = entrada.readLine();
                    String[] seccion = cadenaMensaje.split("@@");
                    List<Particula> objetos = new ArrayList<>();
                    for (int i = 0; i < seccion.length; i++) {
                        String[] datos = seccion[i].split(",");
                        for (int j = 0; j < datos.length; j++) {
                            String tipo = datos[0];
                            if (tipo.equals(HiloCliente.CONTRINCANTE)) {
                                String nombre = datos[1];
                                double radio = Double.parseDouble(datos[2]);
                                int posicionContrincanteX = Integer.parseInt(datos[3]);
                                int posicionContrincanteY = Integer.parseInt(datos[4]);
                                int colorRGB = Integer.parseInt(datos[5]);
                                Color color = new Color(colorRGB);
                                Contrincante contrincante = new Contrincante();
                                contrincante.setColor(color);
                                contrincante.setNombre(nombre);
                                contrincante.setRadio(radio);
                                contrincante.setPosicionX(posicionContrincanteX);
                                contrincante.setPosicionY(posicionContrincanteY);
                                objetos.add(contrincante);
                            } else if (tipo.equals(HiloCliente.COMIDA)) {
                                double radio = Double.parseDouble(datos[1]);
                                int posicionComidaX = Integer.parseInt(datos[2]);
                                int posicionComidaY = Integer.parseInt(datos[3]);
                                int color = Integer.parseInt(datos[4]);
                                Color co = new Color(color);
                                Comida comida = new Comida(radio);
                                comida.setPosicionX(posicionComidaX);
                                comida.setPosicionY(posicionComidaY);
                                comida.setColor(co);
                                objetos.add(comida);
                            }
                        }
                    }
                    setChanged();
                    notifyObservers(objetos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
