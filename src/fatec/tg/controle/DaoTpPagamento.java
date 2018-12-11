package fatec.tg.controle;

import fatec.tg.model.Agencia;
import fatec.tg.model.Fornecedor;
import fatec.tg.model.TipoPagamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoTpPagamento {
    private ArrayList<TipoPagamento> tipoPagamentos;
     private Connection conn;

    public DaoTpPagamento(Connection conn) {
        this.conn = conn;
    }
     public int obterTpPag(){
         PreparedStatement ps = null;
         int id=0;
         try{
           ps = conn.prepareStatement("select MAX(codTipoPag) from TipoPagamento");
            
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                id= rs.getInt("max(codTipoPag)");
            }
            
        
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        return (id);
    }
     public void inserir(TipoPagamento tipoPagamento) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO TipoPagamento(codTipoPag,descricao,situacao) VALUES(?,?,?)");
          
            ps.setInt(1, tipoPagamento.getCodigo());
            ps.setString(2, tipoPagamento.getDescricao());
            ps.setString(3, tipoPagamento.getSituacao());
          
          
            ps.execute();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
      public void alterar(TipoPagamento tipoPagamento) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE Tipopagamento set descricao = ?,situacao = ?" +
                                                 "where codTipoPag = ?" );
            
                ps.setString(1, tipoPagamento.getDescricao());
                ps.setString(2, tipoPagamento.getSituacao());
                ps.setInt(3, tipoPagamento.getCodigo());
                
                
                ps.execute();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
      public void excluir(TipoPagamento tipoPagamento) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM TipoPagamento where codTipoPag = ?");
            
            ps.setInt(1, tipoPagamento.getCodigo());

            ps.execute();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
      public ArrayList<TipoPagamento> listarTpPagSituacao(String situacao){
        TipoPagamento i=null;
        PreparedStatement ps=null;
        String aux=null;
        if(situacao == "Ativo")
            aux="A";
        else
            aux="I";
        try{
            ps = conn.prepareStatement("SELECT * from TipoPagamento where situacao = ?");
            
            ps.setString(1, aux);
            ResultSet rs = ps.executeQuery();
           
            tipoPagamentos = new ArrayList<TipoPagamento>();
            
            
              while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from TipoPagamento where " +
                                                 "codTipoPag = ?");
                
                 ps.setString(1, rs.getString("codTipoPag"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new TipoPagamento(rs.getInt("codTipoPag"));
                        i.setDescricao(r.getString("descricao"));
                        i.setSituacao(r.getString("situacao"));
                    }
                tipoPagamentos.add(i);
                
                //crisndo array list para listar os curso
            }
                //crisndo array list para listar os curso
            
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return tipoPagamentos;
    }   
    
         public  TipoPagamento consultar (int codTipoPag) {
        TipoPagamento d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from TipoPagamento where " +
                                                 "codTipoPag = ?");
            
            ps.setInt(1, codTipoPag);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new TipoPagamento(codTipoPag);
                d.setDescricao(rs.getString("descricao"));
                d.setSituacao(rs.getString("situacao"));
            }
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    } 
        public  TipoPagamento consultarDescricao (String descricao) {
        TipoPagamento d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from TipoPagamento where " +
                                                 "descricao = ?");
            
            ps.setString(1, descricao);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new TipoPagamento(rs.getInt("codTipoPag"));
                d.setDescricao(rs.getString("descricao"));
                d.setSituacao(rs.getString("situacao"));
            }
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    } 
     
    public ArrayList<TipoPagamento> listarTpPagamentoDescricao(String descricao){
        TipoPagamento i=null;
        PreparedStatement ps=null;
      
        try{
            ps = conn.prepareStatement("SELECT * from TipoPagamento where descricao = ?");
            
            ps.setString(1, descricao);
            ResultSet rs = ps.executeQuery();
           
            tipoPagamentos = new ArrayList<TipoPagamento>();
            
            
              while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from TipoPagamento where " +
                                                 "codTipoPag = ?");
                
                 ps.setString(1, rs.getString("codTipoPag"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new TipoPagamento(rs.getInt("codTipoPag"));
                        i.setDescricao(r.getString("descricao"));
                        i.setSituacao(r.getString("situacao"));
                    }
                tipoPagamentos.add(i);
                
                //crisndo array list para listar os curso
            }
                //crisndo array list para listar os curso
            
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return tipoPagamentos;
    }   
    public ArrayList<TipoPagamento> listarTpPagamento(){
        TipoPagamento i=null;
        PreparedStatement ps=null;
      
        try{
            ps = conn.prepareStatement("SELECT codTipoPag from TipoPagamento where situacao='A'");
            
            ResultSet rs = ps.executeQuery();
           
            tipoPagamentos = new ArrayList<TipoPagamento>();
            
            
              while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from TipoPagamento where " +
                                                 "codTipoPag = ?");
                
                 ps.setString(1, rs.getString("codTipoPag"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new TipoPagamento(rs.getInt("codTipoPag"));
                        i.setDescricao(r.getString("descricao"));
                        i.setSituacao(r.getString("situacao"));
                    }
                tipoPagamentos.add(i);
                
                //crisndo array list para listar os curso
            }
                //crisndo array list para listar os curso
            
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return tipoPagamentos;
    }   
        public ArrayList<TipoPagamento> listarTodosTpPagamento(){
        TipoPagamento i=null;
        PreparedStatement ps=null;
      
        try{
            ps = conn.prepareStatement("SELECT * from TipoPagamento");
            
            ResultSet rs = ps.executeQuery();
           
            tipoPagamentos = new ArrayList<TipoPagamento>();
            
            
              while(rs.next() == true){

                        i = new TipoPagamento(rs.getInt("codTipoPag"));
                        i.setDescricao(rs.getString("descricao"));
                        i.setSituacao(rs.getString("situacao"));
               
                tipoPagamentos.add(i);
                
                //crisndo array list para listar os curso
            }
                //crisndo array list para listar os curso
            
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return tipoPagamentos;
    }   

    
}
