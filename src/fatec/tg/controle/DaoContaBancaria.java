
package fatec.tg.controle;

import fatec.tg.model.Agencia;
import fatec.tg.model.ContaBancaria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoContaBancaria {
       private ArrayList<ContaBancaria> contasB;
       private Connection conn;

    public DaoContaBancaria(Connection conn) {
        this.conn = conn;
    }
    
    public void inserir(ContaBancaria contaBanc) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO contaBancaria(codBanco,numAgencia,numConta, situacao, saldo,limite) VALUES(?,?,?,?,?,?)");
          
            ps.setInt(1, contaBanc.getCodBanco());
            ps.setInt(2, contaBanc.getNumAgencia());
            ps.setInt(3, contaBanc.getNumConta());
            ps.setString(4, contaBanc.getSituacao());
            ps.setDouble(5, contaBanc.getSaldo());
            ps.setDouble(6, contaBanc.getLimite());
          
            ps.execute();
             
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
        public void alterar(ContaBancaria contaBanc) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE contaBancaria set saldo = ?,situacao = ?,limite = ?" +
                                                 "where codbanco = ? and  numAgencia = ? and numConta = ?" );
            
            
            
                ps.setDouble(1, contaBanc.getSaldo());
                ps.setString(2, contaBanc.getSituacao());
                ps.setDouble(3, contaBanc.getLimite());
                ps.setInt(4, contaBanc.getCodBanco());
                ps.setInt(5, contaBanc.getNumAgencia());
                ps.setInt(6, contaBanc.getNumConta());

                ps.execute();
               
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
        public  ContaBancaria consultar (int codBanco,int numAgencia,int numConta) {
        ContaBancaria d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from contaBancaria where " +
                                                 "codBanco = ? and numAgencia = ? and numConta = ?");
            
            ps.setInt(1, codBanco);
            ps.setInt(2,numAgencia);
            ps.setInt(3, numConta);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new ContaBancaria(codBanco,numAgencia,numConta);
                d.setLimite(rs.getDouble("limite"));
                d.setSituacao(rs.getString("situacao"));
                d.setSaldo(rs.getDouble("saldo"));
                
            }
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }    
        public void excluir(ContaBancaria contaBanc) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM contaBancaria where codbanco = ? and numAgencia = ? and numConta = ?");
            
            ps.setInt(1, contaBanc.getCodBanco());
            ps.setInt(1, contaBanc.getNumAgencia());
            ps.setInt(1, contaBanc.getNumConta());
           

            ps.execute();
            
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
      public ArrayList<ContaBancaria> listarContaBancaria(int codBanco, int numAgencia){
        ContaBancaria i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT * from ContaBancaria where codbanco=? and numAgencia=? and situacao='A'"); 
            
            ps.setInt(1, codBanco);
            ps.setInt(2, numAgencia);
            ResultSet rs = ps.executeQuery();
            contasB = new ArrayList<ContaBancaria>();

            while(rs.next() == true){
                        i = new ContaBancaria(codBanco,numAgencia,rs.getInt("numConta"));
                        i.setSaldo(rs.getDouble("saldo"));
                        i.setLimite(rs.getDouble("Limite"));
                        i.setSituacao(rs.getString("situacao"));
                        
                contasB.add(i);
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return contasB;    
}
            public  ContaBancaria consultarBanco (int numAgencia,int numConta) {
        ContaBancaria d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from contaBancaria where " +
                                                 "numAgencia = ? and numConta = ?");
            
            
            ps.setInt(1,numAgencia);
            ps.setInt(2, numConta);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new ContaBancaria(rs.getInt("codBanco"),numAgencia,numConta);
                d.setLimite(rs.getDouble("limite"));
                d.setSituacao(rs.getString("situacao"));
                d.setSaldo(rs.getDouble("saldo"));
                
            }
             rs.close();
              ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }  
              public void AtualizarSaldo(ContaBancaria contaBanc) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE contaBancaria set saldo = ?" +
                                                 "where codbanco = ? and  numAgencia = ? and numConta = ?" );
            
            
            
                ps.setDouble(1, contaBanc.getSaldo());
                System.out.println("passei "+contaBanc.getSaldo());
                ps.setInt(2, contaBanc.getCodBanco());
                ps.setInt(3, contaBanc.getNumAgencia());
                ps.setInt(4, contaBanc.getNumConta());

                ps.execute();
               
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
}
