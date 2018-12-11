/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import fatec.tg.model.ArquivoRemessa;
import fatec.tg.model.Banco;
import fatec.tg.model.RemessaTitulo;
import fatec.tg.model.TituloParcela;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoRemessaTitulo {
     private ArrayList<ArquivoRemessa> arquivosRem;
      
     private Connection conn;

    public DaoRemessaTitulo(Connection conn) {
        this.conn = conn;
    }
    
    public  RemessaTitulo consultar (ArquivoRemessa arquivorRemessa, TituloParcela tituloParcela ) {
         RemessaTitulo d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from remessatitulo where " +
                                                 "codremessa=? and numLanc=? and numParc=?");
            
            ps.setInt(1, arquivorRemessa.getCodigo());
            ps.setInt(2, tituloParcela.getNumLancamento());
            ps.setInt(3, tituloParcela.getNumParcela());
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new RemessaTitulo(arquivorRemessa,tituloParcela); 
                d.setStatus(rs.getString("status"));
            }
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    } 
    public void inserir(RemessaTitulo remessaTitulo) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO remessaTitulo(codRemessa,numLanc,numParc) VALUES(?,?,?)");
          
            ps.setInt(1, remessaTitulo.getArquivoRemessa().getCodigo());
            ps.setInt(2, remessaTitulo.getTituloParcela().getNumLancamento());
            ps.setInt(3, remessaTitulo.getTituloParcela().getNumParcela());
            
            ps.execute();
             
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     
     public void excluir(RemessaTitulo remessaTitulo) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM remessaTitulo where codRemessa = ? and numlanc=? and numParc=?");
            
            ps.setInt(1, remessaTitulo.getArquivoRemessa().getCodigo());
            ps.setInt(2, remessaTitulo.getTituloParcela().getNumLancamento());
            ps.setInt(3, remessaTitulo.getTituloParcela().getNumParcela());          
            ps.execute();
            
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
}
