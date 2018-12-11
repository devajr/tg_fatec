package fatec.tg.controle;

import fatec.tg.model.FornecFisica;
import fatec.tg.model.Fornecedor;
import fatec.tg.model.PlanoContas;
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
public class DaoSubconta {
    private ArrayList<Subconta> subcontas;
    private Connection conn;

    public DaoSubconta(Connection conn) {
        this.conn = conn;
    }
    
     public  Subconta consultar (int codSubconta ) {
        Subconta d = null;
   
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from subconta where " +
                                                 "codsubconta = ? ");
            
            ps.setInt(1, codSubconta);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new Subconta(codSubconta);
                d.setNome(rs.getString("nomeSubconta"));
                d.setPlanoContas(new DaoPlanodeConta(conn).consultar(rs.getInt("codPlano")));
                    
            }
            rs.close();
            ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    } 
     public  Subconta consultarNome (String nome ) {
        Subconta d = null;
   
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from subconta where " +
                                                 "nomeSubconta = ? ");
            
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new Subconta(rs.getInt("codsubconta"));
                d.setNome(rs.getString("nomeSubconta"));
                d.setPlanoContas(new DaoPlanodeConta(conn).consultar(rs.getInt("codPlano")));
                    
            }
            rs.close();
            ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    } 
     public ArrayList<Subconta> listarSubcontas(){
        Subconta i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT * from subconta"); 
            ResultSet rs = ps.executeQuery();
            subcontas = new ArrayList<Subconta>();

            while(rs.next() == true){
                        i = new Subconta(rs.getInt("codsubconta"));
                        i.setNome(rs.getString("nomesubconta"));
                        i.setPlanoContas(new DaoPlanodeConta(conn).consultar(rs.getInt("codPlano")));
                subcontas.add(i);
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return subcontas;
    }  
    
}
