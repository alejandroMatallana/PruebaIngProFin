package co.edu.eam.ingesoft.pai.agario.servidor.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import co.edu.eam.ingesoft.pai.agario.modelo.dto.MovimientoDTO;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Comida;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Contrincante;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Particula;
import co.edu.eam.ingesoft.pai.agario.servidor.comm.HiloCliente;

/**
 * Mundo del juego.
 * 
 * @author Alejandro
 * 
 */
public class Escenario extends Observable implements Runnable {

	private int ancho;
	
	private int alto;

	private Map<String, LogicaParticula> particulas;

	public Escenario() {
		particulas = new ConcurrentHashMap<>();
	}
	
	public Escenario(int ancho, int alto) {
		this();
		this.ancho = ancho;
		this.alto = alto;
		for (int i = 0; i < 500; i++) {
			crearComida();
		}
	}

	/**
	 * Metodo para crear otro contrincante. 
	 * @param identificador
	 * @return
	 */
	public boolean registrarContrincante(String identificador) {
		Contrincante contricante = new Contrincante();
		contricante.setNombre(identificador);
		contricante.setRadio(25);
		if (particulas.containsKey(identificador)) {
			return false;
		} else {
			LogicaContrincante logicaContrincante = new LogicaContrincante(this, contricante);
			do {
				contricante.setPosicionX(new Random().nextInt(ancho));
				contricante.setPosicionY(new Random().nextInt(alto));

			} while (!verificarColision(logicaContrincante).isEmpty());
			particulas.put(identificador, logicaContrincante);
			return true;
		}
	}

	/**
	 * Metodo para mover el contrincante.
	 * 
	 * @param xcliente
	 * @param ycliente
	 * @param anchoMundo
	 * @param altoMundo
	 */
	public List<Particula> moverContrincante(MovimientoDTO dtoMovimiento) {
		int direccionNuevaX = (dtoMovimiento.getPosicionMouseX() - dtoMovimiento.getAnchoEscenarioCLiente() / 2) / 10;
		int direccionNuevaY = (dtoMovimiento.getPosicionMouseY() - dtoMovimiento.getAltoEscenacioCliente() / 2) / 10;
		LogicaContrincante logicaContrincanteYo = (LogicaContrincante) particulas.get(dtoMovimiento
				.getIdentificador());
		if (logicaContrincanteYo != null) {
			logicaContrincanteYo.moverX(direccionNuevaX);
			logicaContrincanteYo.moverY(direccionNuevaY);
			synchronized (particulas) {
				Map<String, LogicaParticula> listaParticulas = verificarColision(logicaContrincanteYo);
				for (Entry<String, LogicaParticula> logicaParticulas : listaParticulas.entrySet()) { //Acciones de choque.
					LogicaParticula logicaParticula = logicaParticulas.getValue();
					if (logicaParticula instanceof LogicaComida) {//Choque contra comida
						LogicaComida logicaComida = (LogicaComida) logicaParticula;
						logicaContrincanteYo.aumentarArea(logicaComida.calcularArea() * 2);
						particulas.remove(logicaParticulas.getKey());
						continue;
					}
					if (logicaParticula instanceof LogicaContrincante) { //Se choco contra contrincante.
						LogicaContrincante logicaContrincante = (LogicaContrincante) logicaParticula;
						{
							if (logicaContrincanteYo.calcularArea() < logicaParticula.calcularArea()) {//Soy menor que mi contrincante.
								double area = logicaContrincanteYo.calcularArea();
								logicaContrincanteYo.disminuirArea(area * 0.2);
								logicaContrincante.aumentarArea(area * 0.2);
							}
							if (logicaParticula.calcularArea() < logicaContrincanteYo.calcularArea()) {//Soy mayor que mi contrincante.
								double area = logicaContrincanteYo.calcularArea();
								logicaContrincante.disminuirArea(area * 0.2);
								logicaContrincanteYo.aumentarArea(area * 0.2);
							}
							if (logicaContrincante.calcularArea() <= Math.PI) {//El contrincante a muerto, se removera del escenario
								particulas.remove(logicaParticulas.getKey());
							}
							if (logicaContrincanteYo.calcularArea() <= Math.PI) {//Yo he muerto, me removeran del escenario
								particulas.remove(dtoMovimiento.getIdentificador());
							}
							continue;
						}
					}
				}
			}
		}
		return getParticulas();

	}

	public LogicaParticula getParticula(String particulaId) {
		return particulas.get(particulaId);
	}

	public void eliminarContrincante(String identificador) {
		particulas.remove(identificador);
	}

	/**
	 * Metodo para obtener las particulas dentro de un area especifica.
	 * @return todas las particulas.
	 */
	public List<Particula> getParticulas() {
		List<Particula> listaParticulas = new ArrayList<>();
		for (LogicaParticula logicaParticula : particulas.values()) {
			Particula particula = logicaParticula.getParticula();
			if (particula instanceof Contrincante) {
				Contrincante contrincante = (Contrincante) particula;
				if (contrincante.getNombre().equalsIgnoreCase(HiloCliente.ADMIN)) {
					continue;
				}
			}
			listaParticulas.add(particula);
		}
		return listaParticulas;
	}

	/**
	 * Metodo para obtener las particulas dentro de un area especifica.
	 * @param x, punto central en x
	 * @param y, punto central en y
	 * @param ancho, ancho del area de busqueda
	 * @param alto, alto del area de busqueda.
	 * @return
	 */
	public List<Particula> getParticulas(int puntoCentralX, int puntoCentralY, int anchoAreaBusqueda, int altoAreaBusqueda) {
		int nuevaUbicacionX1 = puntoCentralX - anchoAreaBusqueda / 2;
		int nuevaUbicacionY1 = puntoCentralY - altoAreaBusqueda / 2;
		int nuevaUbicacionX2 = puntoCentralX + anchoAreaBusqueda / 2;
		int nuevaUbicacionY2 = puntoCentralY + altoAreaBusqueda / 2;
		List<Particula> listaParticulas = new ArrayList<>();
		for (LogicaParticula logicaParticula : particulas.values()) {
			Particula particula = logicaParticula.getParticula();
			if (particula.getPosicionX() >= nuevaUbicacionX1 && particula.getPosicionX() <= nuevaUbicacionX2
					&& particula.getPosicionY() >= nuevaUbicacionY1 && particula.getPosicionY() <= nuevaUbicacionY2) {
				listaParticulas.add(particula);
			}
		}
		return listaParticulas;
	}

	/**
	 * Metodo para determinar la colision de una particula con otras.
	 * @param logicaParticulaEvaluar
	 * @return lista de particulas en colision con el parametro.
	 */
	public Map<String, LogicaParticula> verificarColision(LogicaParticula logicaParticulaEvaluar) {
		Map<String, LogicaParticula> listaLogicaParticulas = new HashMap<>();
		for (Entry<String, LogicaParticula> logicaParticula : particulas.entrySet()) {
			if (logicaParticulaEvaluar.verificarColision(logicaParticula.getValue().getParticula())) {
				if (logicaParticula != logicaParticulaEvaluar) {
					if (logicaParticula instanceof LogicaContrincante) {
						LogicaContrincante logicaContrincante = (LogicaContrincante) logicaParticula;
						if (logicaContrincante.getParticula() instanceof Contrincante) {
							Contrincante contrincante = (Contrincante) logicaContrincante
									.getParticula();
							if (contrincante.getNombre().equals("admin")) {
								continue;
							}
						}
					}
					listaLogicaParticulas.put(logicaParticula.getKey(), logicaParticula.getValue());
				}
			}
		}
		return listaLogicaParticulas;
	}

	public void run() {
		while (true) {
			if (particulas.size() < 1000) {
				crearComida();
				List<Particula> listaParticulas = getParticulas();
				setChanged();
				notifyObservers(listaParticulas);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * metodo para crear comida.
	 */
	private void crearComida() {
		Comida comida = new Comida(5);
		LogicaComida logicaComida = null;
		logicaComida = new LogicaComida(this, comida);
		do {
			comida.setPosicionX(new Random().nextInt(ancho));
			comida.setPosicionY(new Random().nextInt(alto));
		} while (!verificarColision(logicaComida).isEmpty());
		particulas.put(String.valueOf(new Random().nextInt(100000)), logicaComida);
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}
}
