package fatec.tg.controle;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;


public class Conexao {

   private Connection connection=null;

   
   public Connection conectar() {
        
	   if (connection == null){
	      try {
                 Class.forName("oracle.jdbc.driver.OracleDriver");
            
                 connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "tg_contas_a_pagar", "123");               
	         System.out.println("Conexao OK");
              }catch (Exception ex) {
                  System.out.println("Falha na Conexao");
                  System.out.println(ex.toString() + ex.getMessage());
          }
	   }
       
	   return connection;
   }
   
   public void fecharConexao(){
        if (connection != null){
	   try {
                  connection.close();
           } catch (SQLException ex) {
                   System.out.println(ex.toString());    
           }
        }   
    }
}