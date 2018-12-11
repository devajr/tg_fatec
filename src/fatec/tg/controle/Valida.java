/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.controle;

import java.util.InputMismatchException;
/**
 *
 * @author Devair
 */
public class Valida {
    
        public static boolean Verifica_Cpf(String cpf ){
        int d1=0;
        int d2=0;
        int resp=0;
        int cont =0;
        int aux=0;
        int i;
        cpf=cpf.replace(".","").replace("-","").replace(" ","");

        if(cpf.length()<11||cpf.equals("00000000000") || cpf.equals("11111111111") ||
            cpf.equals("22222222222") || cpf.equals("33333333333") ||
            cpf.equals("44444444444") || cpf.equals("55555555555") ||
            cpf.equals("66666666666") || cpf.equals("77777777777") ||
            cpf.equals("88888888888") || cpf.equals("99999999999") )
            return false;

        else{

                for(i=1;i<10;i++){
                    aux=Integer.valueOf(cpf.substring(i-1,i)).intValue();
                    d1 = d1 + ( 11 - i ) * aux;
                    d2 = d2 + ( 12 - i ) * aux;
                }

                d1=d1%11;

                if(d1<2)
                    d1=0;
                else
                    d1=11-d1;

                d2+=2*d1;
                d2=d2%11;
                if(d2<2)
                    d2=0;
                else
                    d2=11-d2;

                if(d1==(int)cpf.charAt(9)-48 && d2==(int)cpf.charAt(10)-48 )
                    return true;

            }

        return false;
    }
    public static boolean VerificaCnpj(String cnpj) {
        
        cnpj=cnpj.replace(".","").replace("-","").replace(" ","").replace("/", "");
        System.out.println(cnpj);
// considera-se erro cnpj's formados por uma sequencia de numeros iguais
    if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
        cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
        cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
        cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
        cnpj.equals("88888888888888") || cnpj.equals("99999999999999") ||
       (cnpj.length() != 14))
       return(false);
 
    char dig13, dig14;
    int sm, i, r, num, peso;
 
// "try" - protege o código para eventuais erros de conversao de tipo (int)
    try {
// Calculo do 1o. Digito Verificador
      sm = 0;
      peso = 2;
      for (i=11; i>=0; i--) {
// converte o i-ésimo caractere do cnpj em um número:
// por exemplo, transforma o caractere '0' no inteiro 0
// (48 eh a posição de '0' na tabela ASCII)
        num = (int)(cnpj.charAt(i) - 48);
        sm = sm + (num * peso);
        peso = peso + 1;
        if (peso == 10)
           peso = 2;
      }
 
      r = sm % 11;
      if ((r == 0) || (r == 1))
         dig13 = '0';
      else dig13 = (char)((11-r) + 48);
 
// Calculo do 2o. Digito Verificador
      sm = 0;
      peso = 2;
      for (i=12; i>=0; i--) {
        num = (int)(cnpj.charAt(i)- 48);
        sm = sm + (num * peso);
        peso = peso + 1;
        if (peso == 10)
           peso = 2;
      }
 
      r = sm % 11;
      if ((r == 0) || (r == 1))
         dig14 = '0';
      else dig14 = (char)((11-r) + 48);
 
// Verifica se os dígitos calculados conferem com os dígitos informados.
      if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13)))
         return(true);
      else return(false);
    } catch (InputMismatchException erro) {
        return(false);
    }
  }
}
