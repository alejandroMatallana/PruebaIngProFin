package co.edu.eam.ingesoft.pai.agario.servidor.logica;

import java.io.Serializable;

import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Particula;

/**
 * Clase que representa un objeto en el mundo.
 * @author Alejandro
 *
 */
public abstract class LogicaParticula implements Serializable{

	private Particula particula;
	
	protected Escenario mundo;
	
	public LogicaParticula(Escenario mundo,Particula particula) {
		this.mundo=mundo;
		this.particula=particula;
	}
	
	/**
	 * Metodo para mover en el eje X la particula
	 * @param movimientoX, el movimiento en X
	 */
	public void moverX(int movimientoX){
		
		int nuevaPosicionX=particula.getPosicionX()+movimientoX;
		if(nuevaPosicionX<0){
			nuevaPosicionX=(int)particula.getAncho();
		}
		if(nuevaPosicionX>mundo.getAncho()){
			nuevaPosicionX=(int)mundo.getAncho()-(int)particula.getAncho();
		}
		particula.setPosicionX(nuevaPosicionX);
	}
	
	/**
	 * Metodo para mover en el eje Y la particula
	 * @param movimientoY
	 */
	public void moverY(int movimientoY){
		
		int nuevaPosicionY=particula.getPosicionY()+movimientoY;
		if(nuevaPosicionY<0){
			nuevaPosicionY=(int)particula.getAlto();
		}
		if(nuevaPosicionY>mundo.getAlto()){
			nuevaPosicionY=(int)mundo.getAlto()-(int)particula.getAlto();
		}
		particula.setPosicionY(nuevaPosicionY);
	}

	/**
	 * Metodo para verificar si dos particulas se chocan.
	 * @param particula, particula a verificar si se choc ocn esta
	 * @return, true si hubo colision,
	 */
	public abstract boolean verificarColision(Particula particula);
	/**
	 * Metodo para determinar el area de la particula.
	 * @return
	 */
	public abstract double calcularArea();

	public Escenario getMundo() {
		return mundo;
	}

	public void setMundo(Escenario mundo) {
		this.mundo = mundo;
	}

	public Particula getParticula() {
		return particula;
	}

	public void setParticula(Particula particula) {
		this.particula = particula;
	}
}
