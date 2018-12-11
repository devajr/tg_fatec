/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;
import fatec.tg.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Geovana Cânovas
 */
public class DaoUsuario {
       private Connection conn;

    public DaoUsuario(Connection conn) {
        this.conn = conn;
    }
    
    public Usuario consultar (int codLogin) {
        Usuario d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from login where " +
                                                 "codLogin = ?");
            
            ps.setInt(1, codLogin);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new Usuario (rs.getInt("codlogin"));
                d.setUsuario(rs.getString("usuario"));
                d.setSenha(rs.getString("senha"));
            }
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return d;
    }
}
