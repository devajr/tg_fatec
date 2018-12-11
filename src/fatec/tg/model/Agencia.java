package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class Agencia extends Banco{
    
    private int numAgencia;
    private String situacao;
    private String telefone;
    private String endereco;

    public Agencia(int codBanco, int numAgencia) {
        super(codBanco);
        this.numAgencia = numAgencia;
    }

    public int getNumAgencia() {
        return numAgencia;
    }

    public void setNumAgencia(int numAgencia) {
        this.numAgencia = numAgencia;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    
    
    
}
