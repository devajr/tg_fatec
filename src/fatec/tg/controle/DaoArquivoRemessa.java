/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import fatec.tg.model.ArquivoRemessa;
import fatec.tg.model.ContaBancaria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Devair
 */
public class DaoArquivoRemessa {
     private ArrayList<ArquivoRemessa> arquivosRem;
     String sql="Select codigo,padrao,valortotal,to_char(dataremessa,'dd/MM/yyyy'),codbanco,agencia,contabancaria,status from remessa"; 
     private Connection conn;

    public DaoArquivoRemessa(Connection conn) {
        this.conn = conn;
    }
     public int obterCodArqRem(){
         PreparedStatement ps = null;
         int id=0;
         try{
           ps = conn.prepareStatement("select MAX(codigo) from Remessa");
            
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
        return (id);
    }
     
     public  ArquivoRemessa consultar (int codigo) {
         ArquivoRemessa d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql+" where " +
                                                 "codigo=?");
            
            ps.setInt(1, codigo);
         
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new ArquivoRemessa(codigo);
                d.setDataRemessa(rs.getString("to_char(dataremessa,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setValorTotal(rs.getDouble("valortotal"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
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
       public  ArquivoRemessa consultarHj (int banco,int agencia, int conta,String hoje) {
        ArquivoRemessa d = null;
        //int codigo=idArqRem();
        PreparedStatement ps = null;
       
        try {
            ps = conn.prepareStatement(sql+" where " +
                                                 "codbanco=? and agencia=? and contabancaria=? and to_char(dataremessa,'dd/MM/yyyy')=? and status='A'");
            
            ps.setInt(1, banco);
            ps.setInt(2, agencia);
            ps.setInt(3, conta);
            ps.setString(4, hoje);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new ArquivoRemessa(rs.getInt("codigo"));
                d.setDataRemessa(rs.getString("to_char(dataremessa,'dd/MM/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setValorTotal(rs.getDouble("valortotal"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
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
     public void inserir(ArquivoRemessa arquivoRemessa) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO remessa(codigo,padrao,dataremessa,valortotal, codbanco, agencia,contabancaria,status) VALUES(?,?,?,?,?,?,?,?)");
          
            ps.setInt(1, arquivoRemessa.getCodigo());
            ps.setInt(2, arquivoRemessa.getPadraCnab());
            ps.setString(3, arquivoRemessa.getDataRemessa());
            ps.setDouble(4, arquivoRemessa.getValorTotal());
            ps.setInt(5, arquivoRemessa.getBanco().getCodBanco());
            ps.setInt(6, arquivoRemessa.getAgencia().getNumAgencia());
            ps.setInt(7, arquivoRemessa.getContaBancaria().getNumConta());
            ps.setString(8, arquivoRemessa.getStatus());
            ps.execute();
             
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     
     public void alterar(ArquivoRemessa arquivoRemessa) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE remessa set padrao = ?,valorTotal = ?,dataremessa = ?, codbanco=?,agencia=?,contabancaria=?status=?" +
                                                 "where codigo = ? " );
            
            
            
            
            ps.setInt(1, arquivoRemessa.getPadraCnab());
            ps.setString(2, arquivoRemessa.getDataRemessa());
            ps.setDouble(3, arquivoRemessa.getValorTotal());
            ps.setInt(4, arquivoRemessa.getBanco().getCodBanco());
            ps.setInt(5, arquivoRemessa.getAgencia().getNumAgencia());
            ps.setInt(6, arquivoRemessa.getContaBancaria().getNumConta());
            ps.setInt(7, arquivoRemessa.getCodigo());
            ps.setString(8, arquivoRemessa.getStatus());

                ps.execute();
               
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     public ArrayList<ArquivoRemessa> listarArqRem(){
        ArquivoRemessa d=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql + " order by codigo desc"); 
            
            
            ResultSet rs = ps.executeQuery();
            arquivosRem = new ArrayList<ArquivoRemessa>();

            while(rs.next() == true){
                d = new ArquivoRemessa(rs.getInt("codigo"));
                d.setDataRemessa(rs.getString("to_char(dataremessa,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setValorTotal(rs.getDouble("valortotal"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
                d.setStatus(rs.getString("status"));
                        
                arquivosRem.add(d);
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return arquivosRem;    
}
     public void fechar(ArquivoRemessa arquivoRemessa) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE remessa set padrao = ?, status=?, valortotal=?"+
                                                 "where codigo = ? " );
            

            ps.setInt(1, arquivoRemessa.getPadraCnab());
            ps.setString(2, arquivoRemessa.getStatus());
            ps.setDouble(3, arquivoRemessa.getValorTotal());
            ps.setInt(4, arquivoRemessa.getCodigo());
            

            ps.execute();
               
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
public  ArrayList<ArquivoRemessa> listarCod(int codigo) {
         ArquivoRemessa d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql+" where " +
                                                 "codigo=? ");
            
            ps.setInt(1, codigo);
         
            ResultSet rs = ps.executeQuery();
           arquivosRem = new ArrayList<ArquivoRemessa>();
            while(rs.next() == true) {
                d = new ArquivoRemessa(codigo);
                d.setDataRemessa(rs.getString("to_char(dataremessa,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setValorTotal(rs.getDouble("valortotal"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
                d.setStatus(rs.getString("status"));
                 
                arquivosRem.add(d);
            }
           
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return arquivosRem;
    }    

public  ArrayList<ArquivoRemessa> listarCodBanco(int codBanco) {
         ArquivoRemessa d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql+" where " +
                                                 "codbanco=? order by codigo desc");
            
            ps.setInt(1, codBanco);
         
            ResultSet rs = ps.executeQuery();
           arquivosRem = new ArrayList<ArquivoRemessa>();
            while(rs.next() == true) {
                d = new ArquivoRemessa(rs.getInt("codigo"));
                d.setDataRemessa(rs.getString("to_char(dataremessa,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setValorTotal(rs.getDouble("valortotal"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
                d.setStatus(rs.getString("status"));
            
                arquivosRem.add(d);
            }
           
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (arquivosRem);
    }    


public  ArrayList<ArquivoRemessa> listarSituacao(int situacao) {
         ArquivoRemessa d = null;
       
        PreparedStatement ps = null;
        String status=null;
        if(situacao==1)
            status="A";
        else if(situacao==2)
            status="F";
        try {
            ps = conn.prepareStatement(sql+" where " +
                                                 "status=? order by codigo desc");
            
            ps.setString(1, status);
         
            ResultSet rs = ps.executeQuery();
           arquivosRem = new ArrayList<ArquivoRemessa>();
            while(rs.next() == true) {
                d = new ArquivoRemessa(rs.getInt("codigo"));
                d.setDataRemessa(rs.getString("to_char(dataremessa,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setValorTotal(rs.getDouble("valortotal"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
                d.setStatus(rs.getString("status"));
            
                arquivosRem.add(d);
            }
           
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (arquivosRem);
    }    


}
