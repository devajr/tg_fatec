/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import fatec.tg.model.ArquivoRemessa;
import fatec.tg.model.Banco;
import fatec.tg.model.ConciliacaoBancaria;
import fatec.tg.model.ContaBancaria;
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
public class DaoConciliacao {
      private Connection conn;
      private ArrayList<ConciliacaoBancaria> conciliacoes;
      private String sql="select codigo,codbanco,agencia,contabancaria,saldoatual," +
"saldoanterior, to_char(dataconciliacao,'dd/mm/yyyy'), status from conciliacao ";
    public DaoConciliacao(Connection conn) {
        this.conn = conn;
    }
      public int obterCodConc(){
         PreparedStatement ps = null;
         int id=0;
         try{
           ps = conn.prepareStatement("select MAX(codigo) from conciliacao");
            
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
        public void inserir(ConciliacaoBancaria conciliacaoBancaria) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO conciliacao(codigo,codbanco,saldoatual,saldoanterior,agencia,contabancaria, dataconciliacao,status) VALUES(?,?,?,?,?,?,?,?)");
          
            ps.setInt(1, conciliacaoBancaria.getCodigo());
            ps.setInt(2, conciliacaoBancaria.getBanco().getCodBanco());
            ps.setDouble(3, conciliacaoBancaria.getSaldoAtual());
            ps.setDouble(4, conciliacaoBancaria.getSaldoAnt());
            ps.setInt(5, conciliacaoBancaria.getAgencia().getNumAgencia());
            ps.setInt(6, conciliacaoBancaria.getContaBancaria().getNumConta());
            ps.setString(7, conciliacaoBancaria.getDataCon());
            ps.setString(8, conciliacaoBancaria.getStatus());
            ps.execute();
             
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
         public void atualiza(ConciliacaoBancaria conciliacaoBancaria) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("Update conciliacao set status = ? where codigo = ?");
          
            ps.setString(1, conciliacaoBancaria.getStatus());
            ps.setInt(2, conciliacaoBancaria.getCodigo());
            
            ps.execute();
             
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
        public ArrayList<ConciliacaoBancaria> listarConc(){
        ConciliacaoBancaria i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql+" order by codigo desc"); 
            ResultSet rs = ps.executeQuery();
            conciliacoes = new ArrayList<ConciliacaoBancaria>();

            while(rs.next() == true){
                       
                conciliacoes.add(completaConc(rs));
                
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return conciliacoes;
    }  
       public ConciliacaoBancaria consultarCod(int codigo){
        ConciliacaoBancaria i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql+" where codigo = ?"); 
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            

            while(rs.next() == true){
                       
               i=completaConc(rs);
                
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return i;
    }  
              public ArrayList<ConciliacaoBancaria> listarConcBanco(int codBanco){
        ConciliacaoBancaria i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql+" where codbanco = ? order by dataconciliacao desc"); 
            ps.setInt(1, codBanco);
            ResultSet rs = ps.executeQuery();
            conciliacoes = new ArrayList<ConciliacaoBancaria>();

            while(rs.next() == true){
                     conciliacoes.add(completaConc(rs));
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return conciliacoes;
    }  
      public ArrayList<ConciliacaoBancaria>listarConcCod(int codigo){
        ConciliacaoBancaria i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql+" where  codigo = ? "); 
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
             conciliacoes = new ArrayList<ConciliacaoBancaria>();

            while(rs.next() == true){
              conciliacoes.add(completaConc(rs));
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return conciliacoes;
    }  
            public ArrayList<ConciliacaoBancaria> listarConcSituacao(int situacao){
        ConciliacaoBancaria i=null;
        PreparedStatement ps=null;
        String status = null;
        if(situacao==1)
            status="D";
        else if(situacao==2)
            status="I";
        else if(situacao==3)
            status="P";
        try{
            ps = conn.prepareStatement(sql+" where status = ? order by dataconciliacao desc"); 
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            conciliacoes = new ArrayList<ConciliacaoBancaria>();

            while(rs.next() == true){
                       conciliacoes.add(completaConc(rs));
                
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return conciliacoes;
    }          
    
     public ConciliacaoBancaria completaConc(ResultSet rs) throws SQLException{
        ConciliacaoBancaria i=null;
        i = new ConciliacaoBancaria(rs.getInt("codigo"));
        i.setSaldoAnt(rs.getDouble("saldoAnterior"));
        i.setSaldoAtual(rs.getDouble("saldoAtual"));
        i.setDataCon(rs.getString("to_char(dataconciliacao,'dd/mm/yyyy')"));
        i.setBanco(new DaoBanco(conn).consultar(rs.getInt("codBanco")));
        i.setAgencia(new DaoAgencia(conn).consultar(rs.getInt("codBanco"),rs.getInt("agencia")));
        i.setContaBancaria(new DaoContaBancaria(conn).consultar(rs.getInt("codbanco"), rs.getInt("agencia"), rs.getInt("contabancaria")));
        i.setStatus(rs.getString("status"));

        return i;     
    }
       
        //ListarConc
        //ListarConcBanco
        //ListarConcCod
        //ListarSituacaco
      
}
