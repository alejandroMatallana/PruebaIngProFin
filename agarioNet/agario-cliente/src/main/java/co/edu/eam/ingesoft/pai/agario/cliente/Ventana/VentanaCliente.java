/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.eam.ingesoft.pai.agario.cliente.Ventana;

import co.edu.eam.ingesoft.pai.agario.cliente.controlador.ControladorVentanaCliente;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Contrincante;
import co.edu.eam.ingesoft.pai.agario.modelo.objetos.Particula;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;

/**
 *
 * @author Alejandro
 */
public class VentanaCliente extends javax.swing.JFrame implements Observer {

    public static ControladorVentanaCliente controlador;
    private String nombreUsuario;

    public VentanaCliente() {
        initComponents();
        setLocationRelativeTo(this);
        controlador = new ControladorVentanaCliente();
        controlador.addObserver(this);
        nombreUsuario = "";
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        mundo1 = new co.edu.eam.ingesoft.pai.agario.cliente.modelo.Mundo();
        mundo = new co.edu.eam.ingesoft.pai.agario.cliente.modelo.Mundo();
        JBConectarse = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout mundoLayout = new javax.swing.GroupLayout(mundo);
        mundo.setLayout(mundoLayout);
        mundoLayout.setHorizontalGroup(
            mundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 835, Short.MAX_VALUE)
        );
        mundoLayout.setVerticalGroup(
            mundoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 462, Short.MAX_VALUE)
        );

        JBConectarse.setText("Empezar");
        JBConectarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBConectarseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mundo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JBConectarse, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(309, 309, 309))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mundo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JBConectarse)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBConectarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBConectarseActionPerformed

        try {
            String usuario = JOptionPane.showInputDialog("Para continuar por favor introduzca su nombre de usuario");
            if (usuario.equals("")) {
                JOptionPane.showMessageDialog(null, "No intrudujo ningun nombre"
                        + "\nIntentelo nuevamente", "Informe", JOptionPane.WARNING_MESSAGE);
            } else {
                boolean res = controlador.conectarse(usuario);
                if (res) {
                    nombreUsuario = usuario;
                    mundo.setYo(nombreUsuario);
                    new Thread(controlador).start();
                    enviarMiPosicion(100, 200, mundo.getHeight(), mundo.getWidth(), nombreUsuario);
                    JBConectarse.setEnabled(false);

                } else {
                    JOptionPane.showMessageDialog(null, "Eror al intentar registrar intentelo nuevamente", "Informe", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Acción Cancelada", "Informe", JOptionPane.INFORMATION_MESSAGE);
            ex.printStackTrace();
        }

    }//GEN-LAST:event_JBConectarseActionPerformed

    /**
     * Metodo para enviar la posicion de la particula al servidor
     * @param x, es la posicion x de la particula
     * @param y, es la posición y de la particula
     * @param alto, es el alto de la particula
     * @param ancho, es el ancho de la particula
     * @param name, es el nombre de la particula
     */
    public static void enviarMiPosicion(int x, int y, int alto, int ancho, String name) {
        try {
            controlador.enviarPosicion(x, y, alto, ancho, name);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBConectarse;
    private co.edu.eam.ingesoft.pai.agario.cliente.modelo.Mundo mundo;
    private co.edu.eam.ingesoft.pai.agario.cliente.modelo.Mundo mundo1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable obser, Object object) {
        List<Particula> particulas = (List<Particula>) object;
        if (!particulas.isEmpty()) {
            mundo.setPelotas(particulas);
            mundo.repaint();
        }

    }
}
