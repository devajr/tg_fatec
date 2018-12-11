/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import fatec.tg.model.Agencia;
import fatec.tg.model.Banco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoAgencia {
    
     private ArrayList<Agencia> agencias;
       private Connection conn;

    public DaoAgencia(Connection conn) {
        this.conn = conn;
    }
    
      public void inserir(Agencia agencia) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO agencia(codBanco,numAgencia, situacao, telefone) VALUES(?,?,?,?)");
          
            ps.setInt(1, agencia.getCodBanco());
            ps.setInt(2, agencia.getNumAgencia());
            ps.setString(3, agencia.getSituacao());
            ps.setString(4, agencia.getTelefone());

          
            ps.execute();
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
        public void alterar(Agencia agencia) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE agencia set telefone = ?,situacao = ?" +
                                                 "where codbanco = ? and  numAgencia = ?" );
            
            
            
                ps.setString(1, agencia.getTelefone());
                ps.setString(2, agencia.getSituacao());
                ps.setInt(3, agencia.getCodBanco());
                ps.setInt(4, agencia.getNumAgencia());
                ps.execute();
                  ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
        public  Agencia consultar (int codBanco,int numAgencia) {
        Agencia d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from agencia where " +
                                                 "codBanco = ? and numAgencia = ?");
            
            ps.setInt(1, codBanco);
            ps.setInt(2,numAgencia);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new Agencia (codBanco,numAgencia);
                d.setSituacao(rs.getString("situacao"));
                d.setTelefone(rs.getString("telefone"));
                
            }
            rs.close();
              ps.close();
              
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }    
        public void excluir(Agencia agencia) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM agencia where codbanco = ? and numAgencia = ?");
            
            ps.setInt(1, agencia.getCodBanco());
            ps.setInt(1, agencia.getNumAgencia());

            ps.execute();
       
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
        
       public ArrayList<Agencia> listarAgencia(int codBanco){
        Agencia i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT * from agencia where codbanco=? and situacao='A'"); 
            
            ps.setInt(1, codBanco);
            ResultSet rs = ps.executeQuery();
            agencias = new ArrayList<Agencia>();

            while(rs.next() == true){
                        i = new Agencia(codBanco,rs.getInt("numAgencia"));
                        i.setSituacao(rs.getString("situacao"));
                        i.setTelefone(rs.getString("telefone"));
                agencias.add(i);
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return agencias;
    }  
      
    
}
