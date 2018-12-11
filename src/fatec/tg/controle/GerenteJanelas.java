/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import java.awt.Dimension;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author Devair
 */
public class GerenteJanelas {
    private static JDesktopPane jdesktopPane;
    
   
    public GerenteJanelas(JDesktopPane jDesktopPane) {
        GerenteJanelas.jdesktopPane=jDesktopPane;
    }
    
    public void abrirJanelas(JInternalFrame jInternalFrame){
        if(jInternalFrame.isVisible()){
            jInternalFrame.toFront();
            jInternalFrame.requestFocus();
           Dimension d = jInternalFrame.getDesktopPane().getSize();
           jInternalFrame.setLocation((d.width - jInternalFrame.getSize().width) / 2, (d.height - jInternalFrame.getSize().height) / 2);
        }
        else{
            jdesktopPane.add(jInternalFrame);
            jInternalFrame.setVisible(true);
        }
        
    }
        public void setPosicao(JInternalFrame jInternalFrame){
        Dimension d = jInternalFrame.getDesktopPane().getSize();
        jInternalFrame.setLocation((d.width - jInternalFrame.getSize().width) / 2, (d.height - jInternalFrame.getSize().height) / 2);   
        }
    
    
}
