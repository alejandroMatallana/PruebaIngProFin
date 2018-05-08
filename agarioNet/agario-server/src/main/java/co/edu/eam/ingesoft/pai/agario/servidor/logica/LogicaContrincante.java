package co.edu.eam.ingesoft.pai.agario.servidor.logica;

import java.awt.Point;
import java.util.Random;

import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Contrincante;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Particula;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.ParticulaRedonda;

/**
 * Particula ((Contrincante)getParticula()).
 * @author ALejandro
 */
public class LogicaContrincante extends LogicaParticula {

	public LogicaContrincante() {
		super(null, null);
	}

	public LogicaContrincante(Escenario mundo, Particula contrincante) {
		super(mundo, contrincante);
	}

	@Override
	public boolean verificarColision(Particula particula) {
		if (particula != this.getParticula()) {
			double distanciaMinima = ((ParticulaRedonda) getParticula())
					.getRadio() + ((ParticulaRedonda) particula).getRadio();
			Point otraParticula = new Point(particula.getPosicionX(), particula.getPosicionY());
			Point yo = new Point(getParticula().getPosicionX(), getParticula().getPosicionY());

			if (otraParticula.distance(yo.getX(), yo.getY()) <= distanciaMinima) {
				return true;
			}
		}
		return false;
	}

	@Override
	public double calcularArea() {
		return Math.PI * ((ParticulaRedonda) getParticula()).getRadio()
				* ((ParticulaRedonda) getParticula()).getRadio();
	}

	/**
	 * Metodo para aumentar el area en un porcentaje.
	 * @param porcentaje
	 */
	public void aumentarArea(double area) {
		double a = calcularArea() + area;
		((ParticulaRedonda) getParticula()).setRadio((Math.sqrt(a / Math.PI)));
	}

	/**
	 * Metodo para aumentar el area en un porcentaje.z
	 * @param porcentaje
	 */
	public void disminuirArea(double area) {

		double a = calcularArea() - area;
		if (a > 0) {
			((ParticulaRedonda) getParticula())
					.setRadio(Math.sqrt(a / Math.PI));
		} else {
			((ParticulaRedonda) getParticula())
			.setRadio(0.9);
		}
		if (((ParticulaRedonda) getParticula()).getRadio() <= 0) {//Murio la particula.
			((ParticulaRedonda) getParticula()).setVivo(false);
		}
	}
}
