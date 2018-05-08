/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.eam.ingesoft.pai.agario.cliente.modelo;

import co.edu.eam.ingesoft.pai.agario.cliente.Ventana.VentanaCliente;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Contrincante;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Particula;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * @author Alejandro
 */
public class Mundo extends JPanel implements MouseMotionListener {

    private List<Particula> pelotas;
    private String miNombre;
    
    public Mundo() {
        setBackground(Color.white);
        updateUI();
        pelotas = new ArrayList<>();
        miNombre = null;
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics grafica) {
        super.paint(grafica); 
        for (int i = 0; i < pelotas.size(); i++) {
            Particula particula = pelotas.get(i);
            Point punto = null;
            Contrincante yo = encontrarme();
            punto = particula.calcularPosicionRelativa(yo.getPosicionX(), yo.getPosicionY(), getWidth(), getHeight());
            grafica.setColor(particula.getColor());
            grafica.fillOval((((int) punto.getX()) - ((int) particula.getAncho() / 2)), (((int) punto.getY()) - ((int) particula.getAlto() / 2)), (int) particula.getAlto(), (int) particula.getAncho());

            if (particula instanceof Contrincante) {

                Contrincante contrincante = (Contrincante) particula;
                if (contrincante.getNombre() != null && !contrincante.getNombre().isEmpty()) {
                    grafica.setColor(Color.BLACK);
                    DecimalFormat df = new DecimalFormat("##.###");
                    grafica.drawString(
                            contrincante.getNombre() + "[r="
                            + df.format(contrincante.getRadio()) + ",x = "
                            + contrincante.getPosicionX() + ",y = " + contrincante.getPosicionY()
                            + "]", punto.x - (int) contrincante.getAncho(), punto.y);

                }

            }
        }
    }

    /**
     * Metodo que encuentra mi pelota en la lista de particulas
     * @return, mi particula, identificada con mi Nombre
     */
    public Contrincante encontrarme() {
        for (int i = 0; i < pelotas.size(); i++) {
            if (pelotas.get(i) instanceof Contrincante) {
                if (((Contrincante) pelotas.get(i)).getNombre().equalsIgnoreCase(miNombre)) {
                    return ((Contrincante) pelotas.get(i));
                }
            }
        }
        return null;
    }

    public void setYo(String yo) {
        this.miNombre = yo;
    }

    public void setPelotas(List<Particula> pelotas) {
        this.pelotas = pelotas;
    }

    @Override
    public void mouseDragged(MouseEvent me) {

    }

    /**
     * Evento del mouse para que la pelota se mueva 
     * la misma direccion 
     * @param me 
     */
    @Override
    public void mouseMoved(MouseEvent me) {
        if (miNombre != null) {
            int posicionX = me.getX();
            int posicionY = me.getY();
            int alto = getHeight();
            int ancho = getWidth();
            String nombre = miNombre;
            VentanaCliente.enviarMiPosicion(posicionX, posicionY, alto, ancho, nombre);
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Mundo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
