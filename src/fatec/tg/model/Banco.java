package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class Banco {
    private int codBanco;
    private String nome;
    private String situacao;

    public Banco(int codBanco) {
        this.codBanco = codBanco;
    }

    public int getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(int codBanco) {
        this.codBanco = codBanco;
    }

   
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
    
    
}
