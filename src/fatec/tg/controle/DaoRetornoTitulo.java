
package fatec.tg.controle;


import fatec.tg.model.ArquivoRetorno;
import fatec.tg.model.RetornoTitulo;
import fatec.tg.model.TituloParcela;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Devair
 */
public class DaoRetornoTitulo {
      private Connection conn;

    public DaoRetornoTitulo(Connection conn) {
        this.conn = conn;
    }
      
      public  RetornoTitulo consultar (ArquivoRetorno arquivoRetorno, TituloParcela tituloParcela ) {
         RetornoTitulo d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from retornotitulo where " +
                                                 "codigo=? and numLanc=? and numParc=?");
            
            ps.setInt(1, arquivoRetorno.getCodigo());
            ps.setInt(2, tituloParcela.getNumLancamento());
            ps.setInt(3, tituloParcela.getNumParcela());
           
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new RetornoTitulo(arquivoRetorno,tituloParcela);
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
    public void inserir(RetornoTitulo retornoTitulo) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO retornoTitulo(codigo,numLanc,numParc,status) VALUES(?,?,?,?)");
          
            ps.setInt(1, retornoTitulo.getArquivoRetorno().getCodigo());
            ps.setInt(2, retornoTitulo.getTituloParcela().getNumLancamento());
            ps.setInt(3, retornoTitulo.getTituloParcela().getNumParcela());
            ps.setString(4, retornoTitulo.getStatus());
            ps.execute();
             
              ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     
    
}
