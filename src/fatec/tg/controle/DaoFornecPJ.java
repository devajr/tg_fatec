/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;


import fatec.tg.model.FornecJuridica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoFornecPJ {
    private ArrayList<FornecJuridica> fornecedores;
    private Connection conn;

    public DaoFornecPJ(Connection conn) {
        this.conn = conn;
    }
    public void inserir(FornecJuridica fornecJuridica) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO fornecJuridica(codfornec, razaosocial,nomefant,cnpj,inscestadual ) VALUES(?,?,?,?,?)");
            ps.setInt(1, fornecJuridica.getCodFornec());
            ps.setString(2, fornecJuridica.getRazaoSocial());
            ps.setString(3, fornecJuridica.getNomeFan());
            ps.setString(4, fornecJuridica.getCnpj());
            ps.setString(5, fornecJuridica.getInscEst());
         

            ps.execute();
               
             ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     public void alterar(FornecJuridica fornecJuridica) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE fornecJuridica set razaoSocial=?,nomefant=?,cnpj=?,inscestadual=?"
                   + "where codfornec = ?");
            
            
                ps.setString(1, fornecJuridica.getRazaoSocial());
                ps.setString(2,fornecJuridica.getNomeFan());
                ps.setString(3, fornecJuridica.getCnpj());
                ps.setString(4, fornecJuridica.getInscEst());
                ps.setInt(5, fornecJuridica.getCodFornec());
                
               

                ps.execute();
                
                ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
    }
    
     public void excluir(FornecJuridica fornecJuridica) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM fornecJuridica where codFornec = ?");
            
            ps.setInt(1, fornecJuridica.getCodFornec());
                      
            ps.execute();
          
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     public  FornecJuridica consultar (int codigo) {
         FornecJuridica d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from fornecJuridica where " +
                                                 "codFornec = ?");
            
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new FornecJuridica(rs.getInt("codfornec"));
                d.setCnpj(rs.getString("cnpj"));
                d.setRazaoSocial(rs.getString("razaosocial"));
                d.setNomeFan(rs.getString("nomefant"));
                d.setInscEst(rs.getString("inscestadual"));
                
            }
             rs.close();  
             ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }  
      public ArrayList<FornecJuridica> listarNomeFornecedores(String nome){
        FornecJuridica i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT * from fornecjuridica where nomefant=?");
            
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
           
            fornecedores = new ArrayList<FornecJuridica>();
            
            
            while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from fornecjuridica where " +
                                                 "codfornec = ?");
                
                 ps.setString(1, rs.getString("codfornec"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new FornecJuridica (rs.getInt("codfornec"));
                        i.setCnpj(r.getString("cnpj"));
                        i.setRazaoSocial(r.getString("razaosocial"));
                        i.setInscEst(r.getString("inscestadual"));
                        i.setNomeFan(r.getString("nomefant"));
                       

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
      
       public ArrayList<FornecJuridica> listarFornecedoresPJ() {
        FornecJuridica i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT * from fornecjuridica ");
            
            
            ResultSet rs = ps.executeQuery();
           
            fornecedores = new ArrayList<FornecJuridica>();
            
            
            while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from fornecjuridica where " +
                                                 "codfornec = ?");
                
                 ps.setString(1, rs.getString("codfornec"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new FornecJuridica (rs.getInt("codfornec"));
                        i.setCnpj(r.getString("cnpj"));
                        i.setRazaoSocial(r.getString("razaosocial"));
                        i.setInscEst(r.getString("inscestadual"));
                        i.setNomeFan(r.getString("nomefant"));
                       

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
       public  FornecJuridica buscaFornecPJ (String nomeFant) {
         FornecJuridica d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from fornecJuridica where " +
                                                 "nomeFant = ?");
            
            ps.setString(1, nomeFant);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new FornecJuridica(rs.getInt("codfornec"));
                d.setCnpj(rs.getString("cnpj"));
                d.setRazaoSocial(rs.getString("razaosocial"));
                d.setNomeFan(rs.getString("nomefant"));
                d.setInscEst(rs.getString("inscestadual"));
                
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
