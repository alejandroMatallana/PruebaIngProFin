package co.edu.eam.ingesoft.pai.agario.servidor.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import co.edu.eam.ingesoft.pai.agario.servidor.logica.Escenario;

/**
 * Servidor.
 * 
 * @author Alejandro
 * 
 */
public class Servidor extends Observable implements Runnable {

	public static final int ALTO = 3000;
	public static final int ANCHO = 3000;
	
	private List<HiloCliente> clientes;//CLientes dentro del servidor.

	private int puerto; //puerto de escurcha.

	public Servidor(int port) {
		super();
		this.puerto = port;
		clientes = new ArrayList<>();
	}

	@Override
	public void run() {
		Escenario juego = new Escenario(ANCHO, ALTO);
		new Thread(juego).start();
		try (ServerSocket servidor = new ServerSocket(puerto);) {
			while (true) {
				try {
					System.out.println("esperando conexion....");
					Socket conexion = servidor.accept();
					System.out.println("conexion establecida.");
					HiloCliente cliente = new HiloCliente(conexion, this, juego);//Crea el hilo para el cliente.
					clientes.add(cliente);//Lo agrega a la lista de clientes...
					setChanged();//Notifica que alguien se conecto..
					notifyObservers(clientes);
					new Thread(cliente).start(); //Inicia el hilo....
				} catch (Exception excepcion) {
					excepcion.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void quitarCliente(HiloCliente cliente) {
		clientes.remove(cliente);
		deleteObserver(cliente);
	}

	/**
	 * Metodo para enviar un mensaje a un cliente.
	 * @param id, destinatario
	 * @param obj, mensaje a enviar.
	 * @throws IOException
	 */
	public synchronized void enviarMensajeA(String destinatarioId, Object mensaje)
			throws IOException {
		for (HiloCliente cliente : clientes) {
			if (destinatarioId.equals(cliente.getIdentificador())) {//Lo busca por id...
				cliente.enviarMensaje(mensaje.toString());
				notifyObservers(new Object[] { destinatarioId, mensaje });
			}
		}
	}

	/**
	 * Metodo para enviar las particulas a todos.
	 */
	public void actualizarParticulas() {
		for (HiloCliente cliente : clientes) {
			try {
				cliente.enviarParticulas();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}

	/**
	 * Metodo para enviar un mensaje a todos.
	 * @param id, destinatario
	 * @param obj, mensaje a enviar.
	 * @throws IOException
	 */
	public synchronized void enviarMensajeTodos(Object mensaje) throws IOException {
		for (HiloCliente cliente : clientes) {
			cliente.enviarMensaje(mensaje.toString());
			setChanged(); //Notifica el envio del mensaje a todos...
			notifyObservers(mensaje);
		}
	}

	/**
	 * Metodo para informar a todos los clientes la nueva lista.
	 * @throws IOException
	 */
	public void actualizarListaCLientes() throws IOException {
		enviarMensajeTodos("lista");
		enviarMensajeTodos(getClientes());
	}

	/**
	 * getter de la lista de clientes...
	 * @return
	 */
	public List<String> getClientes() {
		List<String> listaClientes = new ArrayList<>();
		for (HiloCliente cliente : clientes) {
			listaClientes.add(cliente.getIdentificador());
		}
		return listaClientes;
	}

	public static void main(String[] args) {
		new Thread(new Servidor(45000)).start();
	}

}
