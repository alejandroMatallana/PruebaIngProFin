package co.edu.eam.ingesoft.pai.agario.modelo.objetos;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

/**
 * Clase que representa un objeto en el mundo.
 * @author Alejandro
 *
 */
public class Particula implements Serializable{

	protected int posicionX;//Posicion de la particula en el eje X
	
	protected int posicionY;//Posicion de la particula en el eje y
	
	protected double alto;
	
	protected double ancho;
	
	protected boolean vivo;//Variable que determina si esta viva la particula
	
	protected Color color;
	
	public Particula() {
		color=new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
	}
	
	public Particula(int posicionX, int posicionY, int alto, int ancho, boolean vivo) {
		super();
		this.posicionX = posicionX;
		this.posicionY = posicionY;
		this.alto = alto;
		this.ancho = ancho;
		this.vivo = vivo;
	}

	/**
	 * Metodo para calcular la posicion relativa de la particula al escenario del cliente.
	 * @param xservidor, posicion  en x del contrincante en el servidor.
	 * @param yservidor, posicion  en y del contrincante en el servidor.
	 * @param anchoc, ancho del mundo del cliente.
	 * @param altoc, alto del mundo del cliente.
	 * @return
	 */
	public Point calcularPosicionRelativa(int xservidor,int yservidor,int anchoCliente, int altoCliente){
		int posicionx,posiciony;
		int escalaX = xservidor-anchoCliente/2;
		int escalaY = yservidor-altoCliente/2;
		posicionx=posicionX-escalaX;
		posiciony=posicionY-escalaY;
		return new Point(posicionx, posiciony);
	}
	
	public int getPosicionX() {
		return posicionX;
	}

	public void setPosicionX(int posicionX) {
		this.posicionX = posicionX;
	}

	public int getPosicionY() {
		return posicionY;
	}

	public void setPosicionY(int posicionY) {
		this.posicionY = posicionY;
	}

	public double getAlto() {
		return alto;
	}

	public void setAlto(double alto) {
		this.alto = alto;
	}

	public double getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public boolean isVivo() {
		return vivo;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "Particula [x=" + posicionX + ", y=" + posicionY + ", color=" + color + "]";
	}
}
