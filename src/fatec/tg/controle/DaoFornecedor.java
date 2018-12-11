/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;


import fatec.tg.model.Fornecedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devair
 */
public class DaoFornecedor {

     private ArrayList<Fornecedor> fornecedores;
       private Connection conn;

    public DaoFornecedor(Connection conn) {
        this.conn = conn;
    }
    
    public int obterCodFornec(){
         PreparedStatement ps = null;
         int id=0;
         try{
           ps = conn.prepareStatement("select MAX(codFornec) from fornecedor");
            
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) {
                id= rs.getInt("max(codFornec)");
            }
            
           rs.close();
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        return (id);
    }
    public void inserir(Fornecedor fornecedor) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO fornecedor(codfornec, datacadastro, cep,"
                    + "logradouro,nomeLogra,numero,complemento,bairro,cidade,estado,pais,telefone,"
                    + "celular,email,pessoaCont,situacao ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, fornecedor.getCodFornec());
            ps.setString(2, fornecedor.getDataCadastro());
            ps.setString(3, fornecedor.getCep());
            ps.setString(4, fornecedor.getLogradouro());
            ps.setString(5, fornecedor.getNomeLogra());
            ps.setInt(6, fornecedor.getNumero());
            ps.setString(7, fornecedor.getComplemento());
            ps.setString(8, fornecedor.getBairro());
            ps.setString(9, fornecedor.getCidade());
            ps.setString(10, fornecedor.getEstado());
            ps.setString(11, fornecedor.getPais());
            ps.setString(12, fornecedor.getTelefone());
            ps.setString(13, fornecedor.getCelular());
            ps.setString(14, fornecedor.getEmail());
            ps.setString(15, fornecedor.getPessoaCont());
            ps.setString(16, fornecedor.getSituacao());

            ps.execute();
            
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
    public void alterar(Fornecedor fornecedor) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE fornecedor set cep=?,logradouro=?,nomeLogra=?,"
                    + "numero=?,complemento=?,bairro=?,cidade=?,estado=?,pais=?,telefone=?,"
                    + "celular=?,email=?,pessoaCont=?,situacao=?" +
                                                 "where codfornec = ?");
            
            
                ps.setString(1, fornecedor.getCep());
                ps.setString(2,fornecedor.getLogradouro());
                ps.setString(3, fornecedor.getNomeLogra());
                ps.setInt(4, fornecedor.getNumero());
                ps.setString(5, fornecedor.getComplemento());
                ps.setString(6, fornecedor.getBairro());
                ps.setString(7, fornecedor.getCidade());
                ps.setString(8, fornecedor.getEstado());
                ps.setString(9, fornecedor.getPais());
                ps.setString(10, fornecedor.getTelefone());
                ps.setString(11, fornecedor.getCelular());
                ps.setString(12, fornecedor.getEmail());
                ps.setString(13, fornecedor.getPessoaCont());
                ps.setString(14, fornecedor.getSituacao());
                ps.setInt(15,fornecedor.getCodFornec());

                ps.execute();
                
                ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
        
    }
    
     public void excluir(Fornecedor fornecedor) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM fornecedor where codFornec = ?");
            
            ps.setInt(1, fornecedor.getCodFornec());
                      
            ps.execute();
            
            ps.close();
        } catch (SQLException ex) {
             System.out.println(ex.toString());   
        }
    }
     
     public ArrayList<Fornecedor> listarFornecedores(){
         Fornecedor i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT codFornec from Fornecedor");
            
            ResultSet rs = ps.executeQuery();
           
            fornecedores = new ArrayList<Fornecedor>();
            
            
            while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from fornecedor where " +
                                                 "codfornec = ?");
                
                 ps.setString(1, rs.getString("codfornec"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new Fornecedor (rs.getInt("codfornec"));
                        i.setDataCadastro(r.getString("dataCadastro"));
                        i.setCep(r.getString("cep"));
                        i.setLogradouro(r.getString("logradouro"));
                        i.setNomeLogra(r.getString("nomeLogra"));
                        i.setNumero(r.getInt("numero"));
                        i.setComplemento(r.getString("complemento"));
                        i.setBairro(r.getString("bairro"));
                        i.setCidade(r.getString("cidade"));
                        i.setEstado(r.getString("estado"));
                        i.setPais(r.getString("pais"));
                        i.setTelefone(r.getString("telefone"));
                        i.setCelular(r.getString("celular"));
                        i.setPessoaCont(r.getString("pessoaCont"));
                        i.setEmail(r.getString("email"));
                        i.setSituacao(r.getString("situacao"));

                    }
                fornecedores.add(i);
                r.close();
                //crisndo array list para listar os curso
            }
            rs.close();
            
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return fornecedores;
    }    
     public  Fornecedor consultar (int codigo) {
        Fornecedor d = null;
       
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * from Fornecedor where " +
                                                 "codFornec = ?");
            
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
           
            if (rs.next() == true) {
                d = new Fornecedor (rs.getInt("codfornec"));
                d.setDataCadastro(rs.getString("dataCadastro"));
                d.setCep(rs.getString("cep"));
                d.setLogradouro(rs.getString("logradouro"));
                d.setNomeLogra(rs.getString("nomeLogra"));
                d.setNumero(rs.getInt("numero"));
                d.setComplemento(rs.getString("complemento"));
                d.setBairro(rs.getString("bairro"));
                d.setCidade(rs.getString("cidade"));
                d.setEstado(rs.getString("estado"));
                d.setPais(rs.getString("pais"));
                d.setTelefone(rs.getString("telefone"));
                d.setCelular(rs.getString("celular"));
                d.setPessoaCont(rs.getString("pessoaCont"));
                d.setEmail(rs.getString("email"));
                d.setSituacao(rs.getString("situacao"));
                
            }
            rs.close();
            ps.close();
        }
        catch (SQLException ex) { 
             System.out.println(ex.toString());   
        }
        return (d);
    } 
     public boolean verifica(int cod) {
         boolean resp = false;
         PreparedStatement ps = null;
          try{
         ps = conn.prepareStatement("SELECT * from titulo where " +
                                                 "codFornec = ?");
          
            ps.setInt(1, cod);
            ResultSet rs = ps.executeQuery();
            if (rs.next() == true) 
                resp = true;
            else
                resp= false;
          }catch (SQLException ex){
            System.out.println(ex.toString());
        }
          return resp;
     }
      public ArrayList<Fornecedor> listarNomeFornecedores(){
        Fornecedor i=null;
        PreparedStatement ps=null;
        try{
            ps = conn.prepareStatement("SELECT codFornec from Fornecedor");
            
            ResultSet rs = ps.executeQuery();
           
            fornecedores = new ArrayList<Fornecedor>();
            
            
            while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from fornecedor where " +
                                                 "codfornec = ?");
                
                 ps.setString(1, rs.getString("codfornec"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new Fornecedor (rs.getInt("codfornec"));
                        i.setDataCadastro(r.getString("dataCadastro"));
                        i.setCep(r.getString("cep"));
                        i.setLogradouro(r.getString("logradouro"));
                        i.setNomeLogra(r.getString("nomeLogra"));
                        i.setNumero(r.getInt("numero"));
                        i.setComplemento(r.getString("complemento"));
                        i.setBairro(r.getString("bairro"));
                        i.setCidade(r.getString("cidade"));
                        i.setEstado(r.getString("estado"));
                        i.setPais(r.getString("pais"));
                        i.setTelefone(r.getString("telefone"));
                        i.setCelular(r.getString("celular"));
                        i.setPessoaCont(r.getString("pessoaCont"));
                        i.setEmail(r.getString("email"));
                        i.setSituacao(r.getString("situacao"));



                    }
                fornecedores.add(i);
                 r.close();
                //crisndo array list para listar os curso
            }
            rs.close();
            ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return fornecedores;
    }   
     
    public ArrayList<Fornecedor> listarFornecedoresSituacao(String situacao){
        Fornecedor i=null;
        PreparedStatement ps=null;
        String aux=null;
        if(situacao == "Ativo")
            aux="A";
        else
            aux="I";
        try{
            ps = conn.prepareStatement("SELECT * from Fornecedor where situacao = ?");
            
            ps.setString(1, aux);
            ResultSet rs = ps.executeQuery();
           
            fornecedores = new ArrayList<Fornecedor>();
            
            
              while(rs.next() == true){
                
                ps = conn.prepareStatement("SELECT * from fornecedor where " +
                                                 "codfornec = ?");
                
                 ps.setString(1, rs.getString("codfornec"));
                 ResultSet r = ps.executeQuery();
           
                    if (r.next() == true) {
                        i = new Fornecedor (rs.getInt("codfornec"));
                        i.setDataCadastro(r.getString("dataCadastro"));
                        i.setCep(r.getString("cep"));
                        i.setLogradouro(r.getString("logradouro"));
                        i.setNomeLogra(r.getString("nomeLogra"));
                        i.setNumero(r.getInt("numero"));
                        i.setComplemento(r.getString("complemento"));
                        i.setBairro(r.getString("bairro"));
                        i.setCidade(r.getString("cidade"));
                        i.setEstado(r.getString("estado"));
                        i.setPais(r.getString("pais"));
                        i.setTelefone(r.getString("telefone"));
                        i.setCelular(r.getString("celular"));
                        i.setPessoaCont(r.getString("pessoaCont"));
                        i.setEmail(r.getString("email"));
                        i.setSituacao(r.getString("situacao"));

                    }
                fornecedores.add(i);
                 r.close();
                //crisndo array list para listar os curso
            }
                //crisndo array list para listar os curso
          rs.close();  
          ps.close();
        }catch (SQLException ex){
            System.out.println(ex.toString());
        }
        return fornecedores;
    }   
     
    
}
