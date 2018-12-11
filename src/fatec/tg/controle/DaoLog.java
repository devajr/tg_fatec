/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import fatec.tg.model.Titulo;
import fatec.tg.model.TituloParcela;
import fatec.tg.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;

import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.objects.NativeDate;

/**
 *
 * @author Devair
 */
public class DaoLog {
    
    private Connection conn;

    public DaoLog(Connection conn) {
        this.conn = conn;
    }
       public int obterCodLog(){
         PreparedStatement ps = null;
         int id=0;
         try{
           ps = conn.prepareStatement("select MAX(codigo) from log");
            
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                if(rs.getString("max(codigo)")==null){
                   id=0; 
                }   
                else{
                     id= rs.getInt("max(codigo)");
                }
               
            }
                rs.close();
                ps.close();
        
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        return (id+1);
    }

    public void LogExclusao(Usuario usuario,TituloParcela titulo,String motivo){
         usuario=Sessao.getInstance().getUsuario();
         PreparedStatement ps = null;
         Titulo t =  new DaoTitulo(conn).consultar(titulo.getNumLancamento());
         DateTimeFormatter d = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
         LocalDateTime timePoint = LocalDateTime.now();
         try { 
         ps = conn.prepareStatement("INSERT INTO log(codigo,usuario,titulo,data,motivo) VALUES(?,?,?,?,?)");
         
         ps.setInt(1, obterCodLog());
         ps.setString(2, String.valueOf(usuario.getCodUsuario()+"|"+ usuario.getUsuario()));
         ps.setString(3, String.valueOf(titulo.getNumLancamento()+"|"+titulo.getNumParcela()+"|"+titulo.getValorParcela()+
                 "|"+"codFornec: "+"|"+t.getFornecedor().getCodFornec()));     
         ps.setString(4, String.valueOf(timePoint.format(d)));        
         ps.setString(5, motivo);
      
         
         ps.execute();
         ps.close();
         } catch (SQLException ex) {
            Logger.getLogger(DaoLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
