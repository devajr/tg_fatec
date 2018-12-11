
package fatec.tg.controle;

import fatec.tg.model.TipoPagamento;
import fatec.tg.model.Titulo;
import fatec.tg.model.TituloParcela;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Devair
 */
public class DaoTituloParcela {
     private Connection conn;
     private ArrayList<TituloParcela> titulos;
     String sql= "Select tituloparcela.numLanc,tituloparcela.numParc,tituloparcela.nossoNum,tituloparcela.valorparcela,tituloparcela.valorpago,"
              +"to_char(tituloparcela.datavenc,'dd/mm/yyyy'),to_char(tituloparcela.datapag,'dd/mm/yyyy'),"
            +"to_char(tituloparcela.datacheque,'dd/mm/yyyy'),tituloparcela.numcheque,tituloparcela.transfbanc,tituloparcela.status,"
           +"tituloparcela.tipopagamento,to_char(dataemissao,'dd/mm/yyyy'),titulo.qtdparcelas,"
            +"titulo.codfornec,titulo.juros,titulo.ndprotesto,titulo.multa,titulo.codbanco,titulo.codsubconta,titulo.historico "
            +"from tituloparcela,titulo "
            +"where titulo.numlanc=tituloParcela.numlanc and "
             + ""
             + "";
    public DaoTituloParcela(Connection conn) {
        this.conn = conn;
    }
     public void inserir(TituloParcela tituloParcela) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO TituloParcela (numlanc, numParc,nossoNum,valorParcela,"
                    + "ValorPago,dataVenc,dataPag,dataCheque,numCheque,transfBanc,status"
                    + ") VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, tituloParcela.getNumLancamento());
            System.out.println(tituloParcela.getNumLancamento());
            ps.setInt(2, tituloParcela.getNumParcela());
            System.out.println(tituloParcela.getNumParcela());
            ps.setString(3, tituloParcela.getNossoNum());
            
            ps.setDouble(4, tituloParcela.getValorParcela());
            System.out.println(tituloParcela.getValorParcela());
            ps.setDouble(5, tituloParcela.getValorPago());
            System.out.println(tituloParcela.getValorPago());
            ps.setString(6, tituloParcela.getDataVenc());
            System.out.println(tituloParcela.getDataVenc());
            ps.setString(7, tituloParcela.getDataPag());
            System.out.println(tituloParcela.getDataPag());
            ps.setString(8, tituloParcela.getChequePara());
            System.out.println(tituloParcela.getChequePara());
            ps.setString(9, tituloParcela.getNumCheque());
            System.out.println(tituloParcela.getNumCheque());
            ps.setString(10, tituloParcela.getTransfBanc());
            System.out.println(tituloParcela.getTransfBanc());
            ps.setString(11, tituloParcela.getStatusPag());
            System.out.println(tituloParcela.getStatusPag());

            ps.execute();
            
            ps.close();
        
            
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     public void alterar(TituloParcela tituloParcela) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE TituloParcela set nossoNum = ?,valorParcela = ?,"
                    + "dataVenc = ?"
                   + "where numLanc = ? and numParc = ?");
            
                    ps.setString(1, tituloParcela.getNossoNum());
                    ps.setDouble(2, tituloParcela.getValorParcela());
                    ps.setString(3, tituloParcela.getDataVenc());
                    ps.setInt(4, tituloParcela.getNumLancamento());
                    ps.setInt(5, tituloParcela.getNumParcela());
               

                ps.execute();
                
                ps.close();
         
                
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
    }
        public  TituloParcela consultar (int numLanc, int numParc) {
        TituloParcela d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement( sql+  " tituloparcela.numLanc = ? and tituloparcela.numParc =? ");
            
            ps.setInt(1, numLanc);
            ps.setInt(2,numParc);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
               d=completacTituloParcela(rs);
                
            }
                rs.close();
                ps.close();
            
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    }    
     
     public void excluir(TituloParcela tituloParcela) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM tituloParcela where tituloparcela.numlanc = ? and tituloparcela.numParc = ?");
            
            ps.setInt(1, tituloParcela.getNumLancamento());
            ps.setInt(2, tituloParcela.getNumParcela());
                      
            ps.execute();
            ps.close();

                
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     public ArrayList<TituloParcela> listarTitulos(String status){
       
        TituloParcela i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql +"status = ?");
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
           
            titulos = new ArrayList<TituloParcela>();
            
            while(rs.next() == true){

                titulos.add(completacTituloParcela(rs));
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
      
                
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        
        return titulos;
    }   
     
     public ArrayList<TituloParcela> listarTitulosCod(int numLanc,String status){
       
        TituloParcela i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql+" tituloparcela.numLanc = ? and tituloparcela.status = ?");
            
            ps.setInt(1,numLanc); 
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            titulos = new ArrayList<TituloParcela>();
            
            
            while(rs.next() == true){
   
                  titulos.add(completacTituloParcela(rs));

                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
         
               
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return titulos;
    }   
      public ArrayList<TituloParcela> listarTitulosData(String d1,String d2,String status ) throws ParseException{
       
        TituloParcela i=null;
        PreparedStatement ps=null;
        Date dataBd = null,dataInicio = null,dataFinal = null;
       
        try{
            ps = conn.prepareStatement(sql+" status = ?");
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            titulos = new ArrayList<TituloParcela>();

            while(rs.next() == true){

                        dataInicio=Admin.formataData(d1);
                        System.out.println(dataInicio);
                        dataFinal=Admin.formataData(d2);
                        System.out.println(dataInicio);
                        if(status.equals("P"))
                        dataBd=Admin.formataData(rs.getString("to_char(tituloparcela.datapag,'dd/mm/yyyy')"));    
                        if(status.equals("N"))    
                        dataBd=Admin.formataData(rs.getString("to_char(tituloparcela.datavenc,'dd/mm/yyyy')"));
                        if(dataBd.compareTo(dataInicio)>=0 && dataBd.compareTo(dataFinal)<=0){
                         i=completacTituloParcela(rs);
                         titulos.add(i);
                        }
                        
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
           
            
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return titulos;
    }   
    
    /* public TituloParcela completa(TituloParcela tituloParcela) {
        PreparedStatement ps = null;
        try {
                ps = conn.prepareStatement("SELECT * from titulo where " +
                                                 "numLanc = ?");
                
                ps.setInt(1, tituloParcela.getNumLancamento());
                ResultSet rs = ps.executeQuery();
           
                    if (rs.next() == true) {
                       
                        tituloParcela.setDtEmissao(rs.getString("dataemissao"));                      
                        tituloParcela.setQtdParcela(rs.getInt("qtdparcelas"));                      
                        tituloParcela.setFornecedor(new DaoFornecedor(conn).consultar(rs.getInt("codfornec")));                      
                        tituloParcela.setJuros(rs.getDouble("juros"));                        
                        tituloParcela.setMulta(rs.getDouble("multa"));                       
                        tituloParcela.setBanco(new DaoBanco(conn).consultar(rs.getInt("codBanco")));                       
                        tituloParcela.setSubconta(new DaoSubconta(conn).consultar(rs.getInt("codSubconta")) );                       
                        tituloParcela.setHistorico(rs.getString("historico"));                      
                        tituloParcela.setNdProtesto(rs.getInt("ndprotesto"));                     
                        tituloParcela.setTituloRecorrente(rs.getString("titulorecorrente"));                      
                        tituloParcela.setBanco(new DaoBanco(conn).consultar(rs.getInt("codBanco")));
                    }

                ps.execute();
                rs.close();
                ps.close();
              
               
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        return(tituloParcela);
     } 
*/
      public ArrayList<TituloParcela> listarTitulosFornec(int codFornec, String status){
       
        TituloParcela i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(sql+
                " status=? and tituloparcela.numLanc in(Select numLanc from titulo where codfornec = ?)");
            ps.setString(1, status);
            ps.setInt(2,codFornec);
            
            ResultSet rs = ps.executeQuery();
            titulos = new ArrayList<TituloParcela>();
            while(rs.next() == true){
          
                 titulos.add(completacTituloParcela(rs));
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
           
                
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return titulos;
    }   
      public ArrayList<TituloParcela> listarTitulosOrder(int cod,String status){
       
        TituloParcela i=null;
        PreparedStatement ps=null;
        
        try{
            if(cod==1)
               ps = conn.prepareStatement(sql + "status=? order by datavenc");
            if(cod==2)
                ps = conn.prepareStatement(sql + "status=? order by tituloparcela.numLanc");
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            titulos = new ArrayList<TituloParcela>();
            
            while(rs.next() == true){
                
                titulos.add(completacTituloParcela(rs));
                
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
           
                
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return titulos;
    }   
      public TituloParcela completacTituloParcela(ResultSet rs) throws SQLException{
        TituloParcela i=null;
                    i = new TituloParcela(rs.getInt("numLanc"));
                    i.setNumParcela(rs.getInt("numParc"));
                    i.setValorParcela(rs.getDouble("valorParcela"));
                    i.setValorPago(rs.getDouble("valorPago"));
                    i.setDataVenc(rs.getString("to_char(tituloparcela.datavenc,'dd/mm/yyyy')"));
                    i.setDataPag(rs.getString("to_char(tituloparcela.datapag,'dd/mm/yyyy')"));
                    i.setChequePara(rs.getString("to_char(tituloparcela.datacheque,'dd/mm/yyyy')"));
                    i.setNumCheque(rs.getString("numCheque"));
                    i.setNossoNum(rs.getString("nossoNum"));
                    i.setStatusPag(rs.getString("status"));
                    i.setTransfBanc(rs.getString("transfbanc"));
                    i.setTipoPagamento(new DaoTpPagamento(conn).consultar(rs.getInt("tipoPagamento")));
                    //System.out.println("tipopagamento: "+rs.getInt("tipoPagamento"));
                        i.setDtEmissao(rs.getString("to_char(dataemissao,'dd/mm/yyyy')"));                      
                        i.setQtdParcela(rs.getInt("qtdparcelas"));                      
                        i.setFornecedor(new DaoFornecedor(conn).consultar(rs.getInt("codfornec")));  
                        i.setFornecFisica(new DaoFornecPF(conn).consultar(rs.getInt("codfornec")));
                        i.setFornecJuridica(new DaoFornecPJ(conn).consultar(rs.getInt("codfornec")));
                        i.setJuros(rs.getDouble("juros"));                        
                        i.setMulta(rs.getDouble("multa"));                       
                        i.setBanco(new DaoBanco(conn).consultar(rs.getInt("codBanco")));                       
                        i.setSubconta(new DaoSubconta(conn).consultar(rs.getInt("codSubconta")) );                       
                        i.setHistorico(rs.getString("historico"));                      
                        i.setNdProtesto(rs.getInt("ndprotesto"));                     
                        //i.setTituloRecorrente(rs.getString("titulorecorrente"));                      
                        
                    
              
            return i;     
      }
      public  void PagarDinheiro(TituloParcela tituloParcela){
          PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE TituloParcela set dataPag = ?,valorpago = ?,"
                    + "status = ?,tipopagamento = ? "
                   + "where numLanc = ? and numParc = ? ");
            
                    ps.setString(1, tituloParcela.getDataPag());
                    ps.setDouble(2, tituloParcela.getValorPago());
                    ps.setString(3, tituloParcela.getStatusPag());
                    ps.setInt(4, tituloParcela.getTipoPagamento().getCodigo());
                    ps.setInt(5, tituloParcela.getNumLancamento());
                    ps.setInt(6, tituloParcela.getNumParcela());
                    

                ps.execute();
                
                ps.close();
         
                
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
      }
       public  void PagarTransBanc(TituloParcela tituloParcela){
          PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE TituloParcela set dataPag = ?,valorpago = ?,"
                    + "status = ?, transfbanc = ?,tipopagamento = ? "
                   + "where numLanc = ? and numParc = ?");
            
                    ps.setString(1, tituloParcela.getDataPag());
                    ps.setDouble(2, tituloParcela.getValorPago());
                    ps.setString(3, tituloParcela.getStatusPag());
                    ps.setString(4,tituloParcela.getTransfBanc());
                    ps.setInt(5, tituloParcela.getTipoPagamento().getCodigo());
                    ps.setInt(6, tituloParcela.getNumLancamento());
                    ps.setInt(7, tituloParcela.getNumParcela());
                   

                ps.execute();
                
                ps.close();
         
                
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
      }
       public  void PagarCheque(TituloParcela tituloParcela){
          PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE TituloParcela set dataPag = ?,valorpago = ?,"
                    + "status = ?,  datacheque = ?, numcheque=?,tipopagamento = ? "
                   + "where numLanc = ? and numParc = ?");
            
                    ps.setString(1, tituloParcela.getDataPag());
                    ps.setDouble(2, tituloParcela.getValorPago());
                    ps.setString(3, tituloParcela.getStatusPag());
                    ps.setString(4,tituloParcela.getChequePara());
                    ps.setString(5, tituloParcela.getNumCheque());
                    ps.setInt(6, tituloParcela.getTipoPagamento().getCodigo());
                    ps.setInt(7, tituloParcela.getNumLancamento());
                    ps.setInt(8, tituloParcela.getNumParcela());
                   

                ps.execute();
                
                ps.close();
         
                
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
      }
     public ArrayList<TituloParcela> listarTitulosRemessa(int codRemessa){
       String query="select tituloparcela.numLanc,tituloparcela.numParc,tituloparcela.valorparcela,tituloparcela.valorpago," +
        " to_char(tituloparcela.datavenc,'dd/mm/yyyy'),to_char(tituloparcela.datapag,'dd/mm/yyyy'),to_char(titulo.dataemissao,'dd/mm/yyyy')," +
        "titulo.codfornec,titulo.juros,titulo.multa,tituloparcela.status, tituloparcela.nossonum " +
        "from tituloparcela,remessa,remessatitulo,titulo " +
        "where remessa.codigo=remessatitulo.codremessa and remessatitulo.numparc=tituloparcela.numparc " +
        "and remessatitulo.numlanc=tituloparcela.numlanc and titulo.numlanc=tituloParcela.numlanc and remessa.codigo=?";
        TituloParcela i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, codRemessa);
           
            
            ResultSet rs = ps.executeQuery();
            titulos = new ArrayList<TituloParcela>();
            while(rs.next() == true){
          
                    i = new TituloParcela(rs.getInt("numLanc"));
                    System.out.println(rs.getInt("numLanc"));
                    i.setNumParcela(rs.getInt("numParc"));
                    System.out.println(rs.getInt("numParc"));
                    i.setValorParcela(rs.getDouble("valorParcela"));
                    i.setValorPago(rs.getDouble("valorPago"));
                    i.setDataVenc(rs.getString("to_char(tituloparcela.datavenc,'dd/mm/yyyy')"));
                    i.setDataPag(rs.getString("to_char(tituloparcela.datapag,'dd/mm/yyyy')"));
                    i.setDtEmissao(rs.getString("to_char(titulo.dataemissao,'dd/mm/yyyy')"));
                    i.setFornecedor(new DaoFornecedor(conn).consultar(rs.getInt("codfornec"))); 
                    i.setFornecFisica(new DaoFornecPF(conn).consultar(rs.getInt("codfornec")));
                    i.setFornecJuridica(new DaoFornecPJ(conn).consultar(rs.getInt("codfornec")));
                    i.setNossoNum(rs.getString("nossonum"));
                    i.setJuros(rs.getDouble("juros"));                        
                    i.setMulta(rs.getDouble("multa")); 
                    i.setStatusPag(rs.getString("status"));
                   
                 
                titulos.add(i);
                //crisndo array list para listar os curso
            }
            
            rs.close();
            ps.close();
           
                
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return titulos;
    }  
           public  void PagarRemessa(TituloParcela tituloParcela){
          PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE TituloParcela set dataPag = ?,valorpago = ?,"
                    + "status = ?,tipopagamento = ? "
                   + "where numLanc = ? and numParc = ?");
            
                    ps.setString(1, tituloParcela.getDataPag());
                    ps.setDouble(2, tituloParcela.getValorPago());
                    ps.setString(3, tituloParcela.getStatusPag());
                    ps.setInt(4, tituloParcela.getTipoPagamento().getCodigo());
                    ps.setInt(5, tituloParcela.getNumLancamento());
                    ps.setInt(6, tituloParcela.getNumParcela());
                   

                ps.execute();
                
                ps.close();
         
                
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
      }
            public ArrayList<TituloParcela> listarTitulosRetorno(int codRetorno){
       String query="select tituloparcela.numLanc,tituloparcela.numParc,tituloparcela.valorparcela,tituloparcela.valorpago," +
        " to_char(tituloparcela.datavenc,'dd/mm/yyyy'),to_char(tituloparcela.datapag,'dd/mm/yyyy'),to_char(titulo.dataemissao,'dd/mm/yyyy')," +
        "titulo.codfornec,titulo.juros,titulo.multa,tituloparcela.status, tituloparcela.nossonum, tituloparcela.tipopagamento " +
        "from tituloparcela,retorno,retornotitulo,titulo " +
        "where retorno.codigo=retornotitulo.codigo and retornotitulo.numparc=tituloparcela.numparc " +
        "and retornotitulo.numlanc=tituloparcela.numlanc and titulo.numlanc=tituloParcela.numlanc and retorno.codigo=?";
        TituloParcela i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, codRetorno);
           
            
            ResultSet rs = ps.executeQuery();
            titulos = new ArrayList<TituloParcela>();
            while(rs.next() == true){
          
                    i = new TituloParcela(rs.getInt("numLanc"));
                    System.out.println(rs.getInt("numLanc"));
                    i.setNumParcela(rs.getInt("numParc"));
                    System.out.println(rs.getInt("numParc"));
                    i.setValorParcela(rs.getDouble("valorParcela"));
                    i.setValorPago(rs.getDouble("valorPago"));
                    i.setDataVenc(rs.getString("to_char(tituloparcela.datavenc,'dd/mm/yyyy')"));
                    i.setDataPag(rs.getString("to_char(tituloparcela.datapag,'dd/mm/yyyy')"));
                    i.setDtEmissao(rs.getString("to_char(titulo.dataemissao,'dd/mm/yyyy')"));
                    i.setFornecedor(new DaoFornecedor(conn).consultar(rs.getInt("codfornec"))); 
                    i.setFornecFisica(new DaoFornecPF(conn).consultar(rs.getInt("codfornec")));
                    i.setFornecJuridica(new DaoFornecPJ(conn).consultar(rs.getInt("codfornec")));
                    i.setNossoNum(rs.getString("nossonum"));
                    i.setJuros(rs.getDouble("juros"));                        
                    i.setMulta(rs.getDouble("multa")); 
                    i.setStatusPag(rs.getString("status"));
                    i.setTipoPagamento(new DaoTpPagamento(conn).consultar(rs.getInt("tipopagamento")));
                   
                 
                titulos.add(i);
                //crisndo array list para listar os curso
            }
            
            rs.close();
            ps.close();
           
                
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return titulos;
    }  
    public  void cancelarPag(TituloParcela tituloParcela){
          TipoPagamento tipoPagamento=null;
          PreparedStatement ps = null;
          tituloParcela.setDataPag("");
          tituloParcela.setValorPago(0);
          tituloParcela.setStatusPag("N");
          tituloParcela.setTipoPagmento( tipoPagamento);
        try {
            ps = conn.prepareStatement("UPDATE TituloParcela set dataPag = ?,valorpago = ?,"
                    + "status = ?,tipopagamento = ? "
                   + "where numLanc = ? and numParc = ?");
            
                    ps.setString(1, tituloParcela.getDataPag());
                    ps.setDouble(2, tituloParcela.getValorPago());
                    ps.setString(3, tituloParcela.getStatusPag());
                    if(tituloParcela.getTipoPagamento()==null)
                        ps.setString(4, "");
                    else
                    ps.setInt(4, tituloParcela.getTipoPagamento().getCodigo());
                    ps.setInt(5, tituloParcela.getNumLancamento());
                    ps.setInt(6, tituloParcela.getNumParcela());
                   

                ps.execute();
                
                ps.close();
         
                
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
      }
}
