
package fatec.tg.controle;

import fatec.tg.model.PlanoContas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Devair
 */
public class DaoPlanodeConta {
         private Connection conn;

    public DaoPlanodeConta(Connection conn) {
        this.conn = conn;
    }
         
     
     public  PlanoContas consultar (int codPlano ) {
        PlanoContas d = null;
   
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from planocontas where " +
                                                 "codplano = ? ");
            
            ps.setInt(1, codPlano);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new PlanoContas(codPlano);
                d.setNome(rs.getString("nomePlano"));
               
            
            }
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }    
     

    
}
