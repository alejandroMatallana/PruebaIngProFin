package co.edu.eam.ingesoft.pai.agario.servidor.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;


import co.edu.eam.ingesoft.pai.agario.modelo.dto.MovimientoDTO;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Comida;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Contrincante;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Particula;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.ParticulaRedonda;
import co.edu.eam.ingesoft.pai.agario.servidor.logica.Escenario;
import co.edu.eam.ingesoft.pai.agario.servidor.logica.LogicaContrincante;
import co.edu.eam.ingesoft.pai.agario.servidor.logica.LogicaParticula;

/**
 * Clase encargada de atender los mensjaes del cliente.
 * @author Alejandro
 */
public class HiloCliente implements Runnable, Observer {

	public static final String CASO_LOGIN = "login";
	public static final String CASO_MOVIMIENTO = "movimiento";
	public static final String ERROR = "ERROR";
	public static final String OK = "OK";
	public static final String ADMIN = "ADMIN";
	public static final String PARTICULAS = "particulas";
	public static final String CONTRINCANTE = "contrincante";
	public static final String COMIDA = "comida";
	
	private int alto;
	
	private int ancho;
	
	private Escenario juego;

	private Socket conexion;

	private PrintStream salida;

	private BufferedReader entrada;

	private boolean terminado;

	private Servidor servidor;

	private String identificador;

	private boolean listo;

	public HiloCliente() {
		super();
	}
	
	/**
	 * Recibe el socket desde el servidor.
	 * @param con, socket
	 * @param servidor, referencia al servidor.
	 */
	public HiloCliente(Socket con, Servidor servidor, Escenario juego) {
		super();
		this.conexion = con;
		this.juego = juego;
		this.servidor = servidor;
		juego.addObserver(this);
		listo = false;
	}

	public void run() {//Hilo encargado de manejar los mensajes entrantes desde el cliente al servidor.

		terminado = false; //Empieza "vivo"
		try {
			conexion.setReceiveBufferSize(conexion.getReceiveBufferSize() * 10);
			//Se crean los flujos de E/S
			salida = new PrintStream(conexion.getOutputStream());
			entrada = new BufferedReader(new InputStreamReader(
					conexion.getInputStream()));
			while (!terminado) {//Aqui se implementa el protocolo...
				String orden = entrada.readLine();
				if(orden.equalsIgnoreCase(CASO_LOGIN)) {
					CasoLogin();
				}
				else if(orden.equalsIgnoreCase(CASO_MOVIMIENTO)) {
					CasoMovimiento();
				}
			}
		} catch (Exception excepcion) {
			Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE,
					null, excepcion);
		} finally {
			try {
				conexion.close();
			} catch (IOException excepcion) {
				Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE,
						null, excepcion);
			}
		}
		servidor.quitarCliente(this);
		juego.eliminarContrincante(identificador);
	}
	
	public void CasoLogin() throws IOException {
		String usuario = entrada.readLine();
		listo = juego.registrarContrincante(usuario);
		if (!listo) {
			terminado = true;
			enviarMensaje(ERROR);
		} else
			enviarMensaje(OK);

		identificador = usuario;
        servidor.actualizarParticulas();
	}
	
	public void CasoMovimiento() throws IOException {
		MovimientoDTO dtoMovimiento = new MovimientoDTO();
		dtoMovimiento.fromString(entrada.readLine());
		alto=dtoMovimiento.getAltoEscenacioCliente();
		ancho=dtoMovimiento.getAnchoEscenarioCLiente();
		List<Particula> listaParticulas = null;
		if (!identificador.equals(ADMIN)) {
			listaParticulas = juego.moverContrincante(dtoMovimiento);
		} else {
			listaParticulas=juego.getParticulas();
		}
		servidor.actualizarParticulas();
		LogicaContrincante log = (LogicaContrincante) juego
				.getParticula(identificador);
		if (((ParticulaRedonda) log.getParticula()).getRadio() < 10) {
			terminado = true;
		}
	}
	
	/**
	 * Metodo para enviar las particulas que el cliente puede ver.
	 * @return
	 * @throws IOException 
	 */
	public void  enviarParticulas() throws IOException{
		if(identificador.equals(ADMIN)){
			enviarMensaje(PARTICULAS);
			enviarMensaje(listParticulasToString(juego.getParticulas()));
			return;
		}
		LogicaParticula logicaParticula=juego.getParticula(identificador);
		if(logicaParticula!=null){
			Particula particula=logicaParticula.getParticula();
			List<Particula> particulas=juego.getParticulas(particula.getPosicionX(), particula.getPosicionY(), ancho, alto);
			enviarMensaje(PARTICULAS);
			enviarMensaje(listParticulasToString(particulas));
		}
	}

	/**
	 * Metodo para enviar un mensaje al cliente.
	 * @param obj
	 * @throws IOException
	 */
	public void enviarMensaje(String obj) throws IOException {
		salida.println(obj);
		salida.flush();
	}

	public void terminar() {//Cerrar conexion con el cliente.
		terminado = true;
	}

	public String getIdentificador() {
		return identificador;
	}

	@Override
	public String toString() {
		return identificador;
	}

	@Override
	public void update(Observable observable, Object arg) {
		try {
			if (listo) {
				enviarMensaje(PARTICULAS);
				enviarMensaje((listParticulasToString(juego.getParticulas())));
			}
		} catch (Exception excepcion) {
			excepcion.printStackTrace();
		}
	}

	/**
	 * Metodo para convertir en string
	 * @return representacion string del arreglo.
	 */
	public String listParticulasToString(List<Particula> listaParticulas) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Particula particula : listaParticulas) {
			if (particula instanceof Contrincante) {
				Contrincante contrincante = (Contrincante) particula;
				stringBuilder.append(CONTRINCANTE + ",");
				stringBuilder.append(contrincante.getNombre() + ",")
					.append(contrincante.getRadio() + ",")
					.append(contrincante.getPosicionX() + ",").append(contrincante.getPosicionY() + ",")
					.append(contrincante.getColor().getRGB()).append("@@");
			}
			if (particula instanceof Comida) {
				Comida comida = (Comida) particula;
				stringBuilder.append(COMIDA + ",")
					.append(comida.getRadio() + ",").append(comida.getPosicionX() + ",")
					.append(comida.getPosicionY() + ",")
					.append(comida.getColor().getRGB()).append("@@");
			}
		}
		return stringBuilder.toString();
	}

}
