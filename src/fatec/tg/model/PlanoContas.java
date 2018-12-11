
package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class PlanoContas {
   private int codigo;
   private String nome;

    public PlanoContas(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
   
    
}
