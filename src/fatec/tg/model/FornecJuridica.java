
package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class FornecJuridica extends Fornecedor{
    
    private String razaoSocial;
    private String nomeFan;
    private String cnpj;
    private String inscEst;

    public FornecJuridica(int codigo, String cnpj) {
        super(codigo);
        this.cnpj = cnpj;
    }
    
    
    public FornecJuridica(int codigo) {
        super(codigo);
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFan() {
        return nomeFan;
    }

    public void setNomeFan(String nomeFan) {
        this.nomeFan = nomeFan;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscEst() {
        return inscEst;
    }

    public void setInscEst(String inscEst) {
        this.inscEst = inscEst;
    }
    
}
