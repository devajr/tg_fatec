/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Devair
 */
public class ClockLabelData extends JLabel {
    public ClockLabelData() {
        Timer t = new Timer(1000, e -> setText(getDateTime()));
        t.setInitialDelay(0);
        t.start();
    }

    private String getDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
}
