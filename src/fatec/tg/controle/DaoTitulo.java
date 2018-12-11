
package fatec.tg.controle;
import fatec.tg.model.Fornecedor;
import fatec.tg.model.Titulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Devair
 */
public class DaoTitulo {
    
    private ArrayList<Titulo> titulos;
      private Connection conn;

    public DaoTitulo(Connection conn) {
        this.conn = conn;
    }
      public void inserir(Titulo titulo) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO Titulo(numlanc, dataemissao,qtdparcelas,"
                    + "codFornec,juros,ndprotesto,multa,codBanco,"
                    + "codSubconta,historico ) VALUES(?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, titulo.getNumLancamento());
            System.out.println(titulo.getNumLancamento());
            ps.setString(2, titulo.getDtEmissao());
            System.out.println(titulo.getDtEmissao());
            ps.setInt(3, titulo.getQtdParcela());
            System.out.println(titulo.getQtdParcela());
            ps.setInt(4, titulo.getFornecedor().getCodFornec());
            System.out.println(titulo.getFornecedor().getCodFornec());
            ps.setDouble(5, titulo.getJuros());
            //System.out.println(titulo.getJuros());
            ps.setInt(6, titulo.getNdProtesto());
           // System.out.println(titulo.getNdProtesto());
            ps.setDouble(7, titulo.getMulta());
            if(titulo.getBanco()==null)
                ps.setString(8, null);
            else
            ps.setInt(8, titulo.getBanco().getCodBanco());
            //System.out.println(titulo.getBanco().getCodBanco());
            ps.setInt(9, titulo.getSubconta().getCodigo());
           // System.out.println(titulo.getSubconta().getCodigo());
            ps.setString(10, titulo.getHistorico());
           // System.out.println(titulo.getHistorico());
         

            ps.execute();
          
            ps.close();
            
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
       public void alterar(Titulo titulo) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE Titulo set  dataemissao = ?,qtdparcelas = ?,"
                    + "codFornec = ?,ndprotesto = ?,juros =?,multa = ?, codBanco = ?,"
                    + "codSubconta = ?,historico = ?"
                   + "where numLanc = ?");
            
                ps.setString(1, titulo.getDtEmissao());
                ps.setInt(2, titulo.getQtdParcela());
                ps.setInt(3, titulo.getFornecedor().getCodFornec());
                ps.setInt(4, titulo.getNdProtesto());
                ps.setDouble(5, titulo.getJuros());               
                ps.setDouble(6, titulo.getMulta());
                ps.setInt(7, titulo.getBanco().getCodBanco());
                ps.setInt(8, titulo.getSubconta().getCodigo());
                ps.setString(9, titulo.getHistorico());
                ps.setInt(10, titulo.getNumLancamento());
               

                ps.execute();
                
            ps.close();
           
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
    }
        public  Titulo consultar (int numLanc ) {
        Titulo d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT to_char(dataemissao,'dd/mm/yyyy'),qtdparcelas,codfornec,juros,multa,codbanco,"
                    + "historico,ndprotesto,codsubconta from Titulo where " +
                                                 "numLanc = ? ");
            
            ps.setInt(1, numLanc);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new Titulo (numLanc);
                d.setDtEmissao(rs.getString("to_char(dataemissao,'dd/mm/yyyy')"));
                d.setQtdParcela(rs.getInt("qtdparcelas"));
                d.setFornecedor(new DaoFornecedor(conn).consultar(rs.getInt("codfornec")));
                d.setJuros(rs.getDouble("juros"));
                d.setMulta(rs.getDouble("multa"));
                d.setBanco(new DaoBanco(conn).consultar(rs.getInt("codbanco")));
                d.setHistorico(rs.getString("historico"));
                d.setNdProtesto(rs.getInt("ndprotesto"));
                d.setSubconta(new DaoSubconta(conn).consultar(rs.getInt("codsubconta")));
                //d.setTituloRecorrente(rs.getString("titulorecorrente"));
                
            }
            rs.close();
            ps.close();
           
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }    
     
     public void excluir(Titulo titulo) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM titulo where numlanc = ?");
            
            ps.setInt(1, titulo.getNumLancamento());
            
             
            ps.execute();
            ps.close();   
            
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     
   public ArrayList<Titulo> listarTitulos(){
       
        Titulo i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT numLanc from Titulo");
            
            ResultSet rs = ps.executeQuery();
           
            titulos = new ArrayList<Titulo>();
            
            
            while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from titulo where " +
                                                 "numLanc = ?");
                
                 ps.setString(1, rs.getString("numLanc"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                    i = new Titulo (r.getInt("numLanc"));
                    i.setDtEmissao(r.getString("dataemissao"));
                    i.setQtdParcela(r.getInt("qtdparcelas"));
                    i.setFornecedor(new DaoFornecedor(conn).consultar(r.getInt("codfornec")));
                    i.setJuros(r.getDouble("juros"));
                    i.setMulta(r.getDouble("multa"));
                    i.setBanco(new DaoBanco(conn).consultar(r.getInt("codbanco")));
                    i.setHistorico(r.getString("historico"));
                    i.setNdProtesto(r.getInt("ndprotesto"));
                    i.setTituloRecorrente(r.getString("titulorecorrente"));

                    }
                titulos.add(i);
                
               r.close(); 
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
            
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return titulos;
    }
   
     
     public int obterNumLancamento(){
         PreparedStatement ps = null;
         int id=0;
         try{
           ps = conn.prepareStatement("select MAX(numLanc) from Titulo");
            
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                id= rs.getInt("max(numLanc)");
            }
            
                rs.close();
                ps.close();
             
             
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        return (id);
    }
     
     
     
    
}
