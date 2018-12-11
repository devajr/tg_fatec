/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import fatec.tg.model.FornecFisica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoFornecPF {
     private ArrayList<FornecFisica> fornecedores;
        private Connection conn;

    public DaoFornecPF(Connection conn) {
        this.conn = conn;
    }

    public void inserir(FornecFisica fornecFisica) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO fornecFisica(codfornec, nome,cpf,rg ) VALUES(?,?,?,?)");
            ps.setInt(1, fornecFisica.getCodFornec());
            ps.setString(2, fornecFisica.getNome());
            ps.setString(3, fornecFisica.getCpf());
            ps.setString(4, fornecFisica.getRg());
         

            ps.execute();
            
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     public void alterar(FornecFisica fornecFisica) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE fornecFisica set nome=?,cpf=?,rg=?"
                   + "where codfornec = ?");
            
            
                ps.setString(1, fornecFisica.getNome());
                ps.setString(2,fornecFisica.getCpf());
                ps.setString(3, fornecFisica.getRg());
                ps.setInt(4, fornecFisica.getCodFornec());
               

                ps.execute();
                 
                ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
    }
    
     public void excluir(FornecFisica fornecFisica) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM fornecFisica where codFornec = ?");
            
            ps.setInt(1, fornecFisica.getCodFornec());
                      
            ps.execute();
               
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     public  FornecFisica consultar (int codigo) {
         FornecFisica d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from fornecFisica where " +
                                                 "codFornec = ?");
            
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new FornecFisica(rs.getInt("codfornec"));
                d.setCpf(rs.getString("cpf"));
                d.setNome(rs.getString("nome"));
                d.setRg(rs.getString("rg"));
                
                
            }
             rs.close();  
             ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }  
    public ArrayList<FornecFisica> listarNomeFornecedores(String nome){
        FornecFisica i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT * from fornecfisica where nome=?");
            
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
           
            fornecedores = new ArrayList<FornecFisica>();
            
            
            while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from fornecfisica where " +
                                                 "codfornec = ?");
                
                 ps.setString(1, rs.getString("codfornec"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new FornecFisica(rs.getInt("codfornec"));
                        i.setCpf(r.getString("cpf"));
                        i.setNome(r.getString("nome"));
                        i.setRg(r.getString("rg"));
                 
                       

                    }
                fornecedores.add(i);
                r.close();
                //crisndo array list para listar os curso
            }
             rs.close();  
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return fornecedores;
    }   
    
    public ArrayList<FornecFisica> listarFornecedoresPF(){
        FornecFisica i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT * from fornecfisica");
            
           
            ResultSet rs = ps.executeQuery();
           
            fornecedores = new ArrayList<FornecFisica>();
            
            
            while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from fornecfisica where " +
                                                 "codfornec = ?");
                
                 ps.setString(1, rs.getString("codfornec"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new FornecFisica(rs.getInt("codfornec"));
                        i.setCpf(r.getString("cpf"));
                        i.setNome(r.getString("nome"));
                        i.setRg(r.getString("rg"));
                 
                       

                    }
                fornecedores.add(i);
                r.close();
                //crisndo array list para listar os curso
            }
             rs.close();  
             ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return fornecedores;
    }  
         public  FornecFisica buscaFornecPF (String nome) {
         FornecFisica d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from fornecFisica where " +
                                                 "nome = ?");
            
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new FornecFisica(rs.getInt("codfornec"));
                d.setCpf(rs.getString("cpf"));
                d.setNome(rs.getString("nome"));
                d.setRg(rs.getString("rg"));
                
                
            }
             rs.close();  
             ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }  
    
    
}
