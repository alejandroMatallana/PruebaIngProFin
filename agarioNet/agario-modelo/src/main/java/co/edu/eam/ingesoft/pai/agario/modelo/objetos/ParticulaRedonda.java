package co.edu.eam.ingesoft.pai.agario.modelo.objetos;

/**
 * 
 * @author Alejandro
 */
public class ParticulaRedonda extends Particula{

	private double radio;
	
	public double getRadio() {
		return radio;
	}

	public void setRadio(double radio) {
		
		this.radio = radio;
		alto=radio*2;
		ancho=radio*2;
	}
}
