/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import fatec.tg.model.Titulo;
import fatec.tg.model.TituloRecorrente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Devair
 */
public class DaoRecorrente {
    
    
     private Connection conn;

    public DaoRecorrente(Connection conn) {
        this.conn = conn;
    }
       public void inserir(TituloRecorrente titulo) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO TituloRecorrente(codfornec, codbanco,codsubconta,"
                    + "qtdparcela,frequencia,diasprotesto,juros,multa,historico"
                    + ") VALUES(?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, titulo.getFornecedor().getCodFornec());
            //System.out.println( titulo.getFornecedor().getCodFornec());
            if(titulo.getBanco()!=null)
            ps.setInt(2, titulo.getBanco().getCodBanco());
           // System.out.println(titulo.getDtEmissao());
            ps.setInt(3, titulo.getSubconta().getCodigo());
          //  System.out.println(titulo.getQtdParcela());
           // System.out.println(titulo.getFornecedor().getCodFornec());
            ps.setDouble(4, titulo.getQtdParcelas());
           // System.out.println(titulo.getJuros());
            ps.setInt(5, titulo.getFrequencia());
           // System.out.println(titulo.getNdProtesto());
            ps.setInt(6, titulo.getDiasProtesto());
            ps.setDouble(7, titulo.getJuros());
           // System.out.println(titulo.getMulta());
            ps.setDouble(8, titulo.getMulta());
            //System.out.println(titulo.getBanco().getCodBanco());
            ps.setString(9, titulo.getHistorico());
           // System.out.println(titulo.getHistorico());
         

            ps.execute();
             
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
public  TituloRecorrente consultar (int codFornec ) {
        TituloRecorrente d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from TituloRecorrente where " +
                                                 "codFornec = ? ");
            
            ps.setInt(1, codFornec);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new TituloRecorrente(new DaoFornecedor(conn).consultar(rs.getInt("codfornec")));
                d.setSubconta(new DaoSubconta(conn).consultar(rs.getInt("codSubconta")));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setQtdParcelas(rs.getInt("qtdparcela"));
                d.setJuros(rs.getDouble("juros"));
                d.setMulta(rs.getDouble("multa"));
                d.setHistorico(rs.getString("historico"));
                d.setDiasProtesto(rs.getInt("diasprotesto"));
                d.setFrequencia(rs.getInt("frequencia"));
               
                
            }
             rs.close();  
             ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }    
     
     public void excluir(TituloRecorrente titulo) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM tituloRecorrente where codfornec = ?");
            
            ps.setInt(1, titulo.getFornecedor().getCodFornec());
                      
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
    
}
