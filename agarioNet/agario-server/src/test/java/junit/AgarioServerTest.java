package junit;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Comida;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Contrincante;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Particula;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.ParticulaRedonda;
import co.edu.eam.ingesoft.pai.agario.servidor.comm.HiloCliente;
import co.edu.eam.ingesoft.pai.agario.servidor.comm.Servidor;
import co.edu.eam.ingesoft.pai.agario.servidor.logica.Escenario;
import co.edu.eam.ingesoft.pai.agario.servidor.logica.LogicaContrincante;


public class AgarioServerTest {
	
	HiloCliente cliente;
	
	public AgarioServerTest() {
		cliente = new HiloCliente();
	}
	
	/**
	 * Clase para ejecutar las pruebas unitarias 
	 */
	
	@Test
	public void aumentarArea() {
		LogicaContrincante logicaContrincante = new LogicaContrincante(new Escenario(4000, 4000), new ParticulaRedonda());
		logicaContrincante.aumentarArea(20);
		assertEquals(2.5231325220201604, ((ParticulaRedonda)logicaContrincante.getParticula()).getRadio(),3);
	}
	
	@Test(expected = NullPointerException.class)
	public void aumentarArea_ParticulaNull() {
		LogicaContrincante logicaContrincante = new LogicaContrincante(new Escenario(4000, 4000), null);
		logicaContrincante.aumentarArea(20);
	}
	
	@Test
	public void aumentarArea_EscenarioNull() {
		LogicaContrincante logicaContrincante = new LogicaContrincante(null, new ParticulaRedonda());
		logicaContrincante.aumentarArea(20);
		assertEquals(2.5231325220201604, ((ParticulaRedonda)logicaContrincante.getParticula()).getRadio(),3);
	}
	
	@Test(expected = NullPointerException.class)
	public void aumentarArea_LogicaContrincanteNull() {
		LogicaContrincante logicaContrincante = null;
		logicaContrincante.aumentarArea(20);
	}
	
	@Test
	public void listarParticulas() {
		HiloCliente cliente = new HiloCliente();
		List<Particula> listaParticulas = new ArrayList<>();
		Particula contrincante = new Contrincante();
		((Contrincante)contrincante).setNombre("Miguel");
		contrincante.setColor(Color.BLACK);
		Particula comida = new Comida(23);
		comida.setColor(Color.BLACK);
		listaParticulas.add(contrincante);
		listaParticulas.add(comida);
		assertEquals("contrincante,Miguel,0.0,0,0,-16777216@@comida,23.0,0,0,-16777216@@", cliente.listParticulasToString(listaParticulas));
	}
	
	/**
	 * Lista la particulas
	 */
	@Test
	public void listarParticulas_ListaVacia() {
		HiloCliente cliente = new HiloCliente();
		List<Particula> listaParticulas = new ArrayList<>();
		assertEquals("", cliente.listParticulasToString(listaParticulas));
	}
	
	@Test(expected = NullPointerException.class)
	public void listarParticulas_ListaNull() {
		HiloCliente cliente = new HiloCliente();
		List<Particula> listaParticulas = null;
		cliente.listParticulasToString(listaParticulas);
	}
	
}