package co.edu.eam.ingesoft.pai.agario.modelo.dto;

import java.io.Serializable;

/**
 * Clase para enviar un movimiento al servidor.
 * @author Alejandro
 */
public class MovimientoDTO  implements Serializable{

	private int posicionMouseX;//Punto donde estaba el mouse en x en el escenario.
	
	private int posicionMouseY;//Punto donde estaba el mouse en y en el escenario.
	
	private int anchoEscenarioCLiente;//Ancho del escenario en el cliente.
	
	private int altoEscenacioCliente;//Alto del escenario en el cliente.
	
	private String identificador;//Nombre del cliente.
	
	public MovimientoDTO() { }

	public MovimientoDTO(int posicionMouseX, int posicionMouseY, int anchoEscenarioCLiente,
			int altoEscenacioCliente,String identificador) {
		super();
		this.posicionMouseX = posicionMouseX;
		this.posicionMouseY = posicionMouseY;
		this.anchoEscenarioCLiente = anchoEscenarioCLiente;
		this.altoEscenacioCliente = altoEscenacioCliente;
		this.identificador=identificador;
	}

	public int getPosicionMouseX() {
		return posicionMouseX;
	}

	public void setPosicionMouseX(int posicionMouseX) {
		this.posicionMouseX = posicionMouseX;
	}

	public int getPosicionMouseY() {
		return posicionMouseY;
	}

	public void setPosicionMouseY(int posicionMouseY) {
		this.posicionMouseY = posicionMouseY;
	}

	public int getAnchoEscenarioCLiente() {
		return anchoEscenarioCLiente;
	}

	public void setAnchoEscenarioCLiente(int anchoEscenarioCLiente) {
		this.anchoEscenarioCLiente = anchoEscenarioCLiente;
	}

	public int getAltoEscenacioCliente() {
		return altoEscenacioCliente;
	}

	public void setAltoEscenacioCliente(int altoEscenacioCliente) {
		this.altoEscenacioCliente = altoEscenacioCliente;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	@Override
	public String toString() {
		return posicionMouseX+","+posicionMouseY+","+altoEscenacioCliente+","+anchoEscenarioCLiente+","+identificador;
	}
	
	/**
	 * convertir cadena a objeto.
	 * @param cadena
	 */
	public void fromString(String cadena){
		String data[]=cadena.split(",");
		posicionMouseX=Integer.valueOf(data[0]);
		posicionMouseY=Integer.valueOf(data[1]);
		altoEscenacioCliente=Integer.valueOf(data[2]);
		anchoEscenarioCLiente=Integer.valueOf(data[3]);
		identificador=data[4];
	}
	
}
