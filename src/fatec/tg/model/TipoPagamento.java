
package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class TipoPagamento {
    
    private int codigo;
    private String  descricao;
    private String situacao;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public TipoPagamento(int codigo) {
        this.codigo = codigo;
    }
    
    
}
