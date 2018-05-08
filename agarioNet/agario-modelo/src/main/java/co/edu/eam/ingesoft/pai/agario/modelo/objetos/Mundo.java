package co.edu.eam.ingesoft.pai.agario.modelo.objetos;
/**
 * Mundo del juego.
 * @author Alejandro
 *
 */
public class Mundo implements Runnable{

	private int ancho;
	
	private int alto;
	
	public void run() {
		
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
