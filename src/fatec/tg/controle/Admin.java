/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import fatec.tg.model.FornecFisica;
import fatec.tg.model.FornecJuridica;
import fatec.tg.model.Fornecedor;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Devair
 */
public class Admin {
    
    public String formataSituacao(String ent){
        if(ent.equals("I"))
           return "Inativo";
        else 
           return "Ativo";
        
    }
       public static String formataStatus(String ent){
        if(ent.equals("A"))
           return "Aberto";
        else 
           return "Fechado";
        
    }
         public static String formataStatusPag(String ent){
        if(ent.equals("A"))
           return "Aguardando Aprovação";
        else if(ent.equals("P"))
             return "Pago";
        else 
           return "Não Pago";
        
    }
          public static String formataStatusCon(String ent){
        if(ent.equals("P"))
           return "Pendente";
        else if(ent.equals("D"))
             return "Deferido";
        else 
           return "Indeferido";
        
    }
    public static String formataStatusArqRet(String ent){
        
        if(ent.equals("P"))
           return "Pendente";
        else 
            return "Conciliado";
    }
      public static String formataStatusRetTitulo(String ent){
        
        int tam=ent.length();
        String resp=null;
        if(tam<=2){
            resp="Baixa efetuada: ";
        }else
            resp="Baixa rejeitada cod(s) ocorrencias: ";
	
            return resp;
    }
    
    public  static Date  formataData(String data) throws ParseException { 
	if (data == null || data.equals(""))
            return null;
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        try{
            date =(Date) formatter.parse(data);

        }
        catch(ParseException ex){
            
        }
        return date;
	}
    public  static Date  formataData1(String data) throws ParseException { 
	if (data == null || data.equals(""))
            return null;
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setLenient(false);
        try{
            date =(Date) formatter.parse(data);

        }
        catch(ParseException ex){
            
        }
        return date;
	}
      public  static Date  formataData2(String data) throws ParseException { 
	if (data == null || data.equals(""))
            return null;
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
      //  SimpleDateFormat yy = new SimpleDateFormat( "MM/dd/yy" );
        formatter.setLenient(false);
        try{
            System.out.println("teste"+formatter.parse(data));
            date =(Date) formatter.parse(data);

        }
        catch(ParseException ex){
            
        }
        return date;
	}
     public static double converte(String arg) throws ParseException{
		//obtem um NumberFormat para o Locale default (BR)
		NumberFormat nf = NumberFormat.getNumberInstance();
		//converte um número com vírgulas ex: 2,56 para double
		double number = nf.parse(arg).doubleValue();
		return number;
	}
       public static String preencheCom(String linha_a_preencher, String letra, int tamanho, int direcao){

        //Checa se Linha a preencher é nula ou branco

        if (linha_a_preencher == null || linha_a_preencher.trim() == "" ) {linha_a_preencher = "";}

       

        //Enquanto Linha a preencher possuir 2 espaços em branco seguidos, substitui por 1 espaço apenas

        //while (linha_a_preencher.contains(" ")) {linha_a_preencher = linha_a_preencher.replaceAll(" "," ").trim();}

       

        //Retira caracteres estranhos

        linha_a_preencher = linha_a_preencher.replaceAll("[./-]","");

        StringBuffer sb = new StringBuffer(linha_a_preencher);

        if (direcao==1){ //a Esquerda

            for (int i=sb.length() ; i<tamanho ; i++){

                sb.insert(0,letra);

            }

        } else if (direcao==2) {//a Direita

            for (int i=sb.length() ; i<tamanho ; i++){

                sb.append(letra);

            }

        }

        return sb.toString();

    }

    
}
