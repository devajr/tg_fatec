
package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class ArquivoRetorno {
    private int codigo;
    private int padraCnab;
    private String dataRetorno;
    private Banco banco;
    private Agencia agencia;
    private ContaBancaria contaBancaria;
    private String status;
    
    
    public ArquivoRetorno(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getPadraCnab() {
        return padraCnab;
    }

    public void setPadraCnab(int padraCnab) {
        this.padraCnab = padraCnab;
    }

    public String getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(String dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
    
    
}
