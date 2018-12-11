
package fatec.tg.controle;

import fatec.tg.model.Banco;
import fatec.tg.model.Subconta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoBanco {
       private ArrayList<Banco> bancos;
       private Connection conn;

    public DaoBanco(Connection conn) {
        this.conn = conn;
    }
    
      public void inserir(Banco banco) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO banco(codBanco, nome, situacao) VALUES(?,?,?)");
            ps.setInt(1, banco.getCodBanco());
            ps.setString(2, banco.getNome());
            ps.setString(3, banco.getSituacao());
           
            ps.execute();
           
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
       public void alterar(Banco banco) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE banco set nome=?,situacao=?" +
                                                 "where codbanco = ?");
            
            
            
                ps.setString(1, banco.getNome());
                ps.setString(2,banco.getSituacao());
                ps.setInt(3, banco.getCodBanco());

                ps.execute();
                
                ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
        public  Banco consultar (int codBanco) {
        Banco d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from banco where " +
                                                 "codBanco = ?");
            
            ps.setInt(1, codBanco);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new Banco (rs.getInt("codBanco"));
                d.setNome(rs.getString("nome"));
                d.setSituacao(rs.getString("situacao"));
                
            }
            rs.close();
            ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return d;
    }    
        public  Banco consultarNome (String nome) {
        Banco d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from banco where " +
                                                 "nome = ?");
            
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new Banco (rs.getInt("codBanco"));
                d.setNome(rs.getString("nome"));
                d.setSituacao(rs.getString("situacao"));
                
            }
            rs.close();
            ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return d;
    }    
        public void excluir(Banco banco) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM banco where codbanco = ?");
            
            ps.setInt(1, banco.getCodBanco());
                      
            ps.execute();
            
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
       
     public ArrayList<Banco> listarBancos(){
        Banco i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT * from banco"); 
            ResultSet rs = ps.executeQuery();
            bancos = new ArrayList<Banco>();

            while(rs.next() == true){
                        i = new Banco(rs.getInt("codbanco"));
                        i.setNome(rs.getString("nome"));
                        i.setSituacao(rs.getString("situacao"));
                bancos.add(i);
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return bancos;
    }  
}
