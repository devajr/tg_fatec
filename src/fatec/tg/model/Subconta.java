
package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class Subconta {
    private String nome;
    private int codigo;
    private PlanoContas planoContas;

    public PlanoContas getPlanoContas() {
        return planoContas;
    }

    public void setPlanoContas(PlanoContas planoContas) {
        this.planoContas = planoContas;
    }
    
    public Subconta(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    
}
