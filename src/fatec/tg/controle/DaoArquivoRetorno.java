/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;


import fatec.tg.model.ArquivoRemessa;
import fatec.tg.model.ArquivoRetorno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoArquivoRetorno {
     private ArrayList<ArquivoRetorno> arquivosRet;
     String sql="Select codigo,padrao,to_char(dataretorno,'dd/mm/yyyy'),codbanco,agencia,contabancaria,status from retorno"; 
     private Connection conn;

    public DaoArquivoRetorno(Connection conn) {
        this.conn = conn;
    }
    public int obterCodArqRet(){
         PreparedStatement ps = null;
         int id=0;
         try{
           ps = conn.prepareStatement("select MAX(codigo) from retorno");
            
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
    public  ArquivoRetorno consultar (int codigo) {
         ArquivoRetorno d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql+" where " +
                                                 "codigo=?");
            
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new ArquivoRetorno(codigo);
                d.setDataRetorno(rs.getString("to_char(dataretorno,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
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
    public void inserir(ArquivoRetorno arquivoRetorno) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO retorno(codigo,padrao,dataretorno, codbanco, agencia,contabancaria,status) VALUES(?,?,?,?,?,?,?)");
          
            ps.setInt(1, arquivoRetorno.getCodigo());
            ps.setInt(2, arquivoRetorno.getPadraCnab());
            ps.setString(3, arquivoRetorno.getDataRetorno());
            ps.setInt(4, arquivoRetorno.getBanco().getCodBanco());
            ps.setInt(5, arquivoRetorno.getAgencia().getNumAgencia());
            ps.setInt(6, arquivoRetorno.getContaBancaria().getNumConta());
            ps.setString(7, arquivoRetorno.getStatus());
            ps.execute();
             
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     public ArrayList<ArquivoRetorno> listarArqRet(){
        ArquivoRetorno d=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql + " order by codigo desc"); 
            
            
            ResultSet rs = ps.executeQuery();
            arquivosRet = new ArrayList<ArquivoRetorno>();

            while(rs.next() == true){
                d = new ArquivoRetorno(rs.getInt("codigo"));
                d.setDataRetorno(rs.getString("to_char(dataretorno,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
                d.setStatus(rs.getString("status"));
                        
                arquivosRet.add(d);
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return arquivosRet;    
}
     public void conciliar(ArquivoRetorno arquivoRetorno) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE retorno set  status=? "+
                                                 "where codigo = ? " );
            
      
            ps.setString(1, arquivoRetorno.getStatus());
            ps.setInt(2, arquivoRetorno.getCodigo());
            

            ps.execute();
               
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
   
     public  ArrayList<ArquivoRetorno> listarCod(int codigo) {
         ArquivoRetorno d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql+" where " +
                                                 "codigo=? ");
            
            ps.setInt(1, codigo);
         
            ResultSet rs = ps.executeQuery();
           arquivosRet = new ArrayList<ArquivoRetorno>();
            while(rs.next() == true) {
                d = new ArquivoRetorno(codigo);
                d.setDataRetorno(rs.getString("to_char(dataretorno,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
                d.setStatus(rs.getString("status"));
                 
                arquivosRet.add(d);
            }
           
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return arquivosRet;
    }   
     public  ArrayList<ArquivoRetorno> listarCodBanco(int codBanco) {
         ArquivoRetorno d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql+" where " +
                                                 "codbanco=? order by codigo desc");
            
            ps.setInt(1, codBanco);
         
            ResultSet rs = ps.executeQuery();
           arquivosRet = new ArrayList<ArquivoRetorno>();
            while(rs.next() == true) {
                d = new ArquivoRetorno(rs.getInt("codigo"));
                d.setDataRetorno(rs.getString("to_char(dataretorno,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
                d.setStatus(rs.getString("status"));
            
                arquivosRet.add(d);
            }
           
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (arquivosRet);
    }    
     
public  ArrayList<ArquivoRetorno> listarSituacao(int situacao) {
         ArquivoRetorno d = null;
       
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
           arquivosRet = new ArrayList<ArquivoRetorno>();
            while(rs.next() == true) {
                d = new ArquivoRetorno(rs.getInt("codigo"));
                d.setDataRetorno(rs.getString("to_char(dataretorno,'dd/mm/yyyy')"));
                d.setPadraCnab(rs.getInt("padrao"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codbanco"),rs.getInt("agencia")));
                d.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
                d.setStatus(rs.getString("status"));
            
                arquivosRet.add(d);
            }
           
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (arquivosRet);
    }    

     
    
}
