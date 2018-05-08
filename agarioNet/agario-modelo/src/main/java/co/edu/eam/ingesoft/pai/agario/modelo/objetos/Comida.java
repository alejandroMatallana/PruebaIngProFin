package co.edu.eam.ingesoft.pai.agario.modelo.objetos;

/**
 * Comida.
 * @author Alejandro
 */
public class Comida extends ParticulaRedonda{

	/**
	 * Método que sirve crear una comida en el tablero
	 * @param radio, el radio de la comida 
	 */
	public Comida(double radio) {
		super();
		setRadio(radio);
	}
	
	@Override
	public String toString() {
		return "Comida [radio=" + getRadio() + ", x=" + posicionX + ", y=" + posicionY + ", alto="
				+ alto + ", ancho=" + ancho + ", color=" + color + "]";
	}
}
