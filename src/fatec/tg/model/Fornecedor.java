
package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class Fornecedor {
   private int codFornec;
   private String dataCadastro;
   private String cep;
   private String logradouro;
   private int numero;
   private String nomeLogra;
   private String complemento;
   private String bairro;
   private String cidade;
   private String estado;
   private String pais;
   private String telefone;
   private String celular;
   private String email;
   private String pessoaCont;
   private String situacao;

    public Fornecedor(int codFornec) {
        this.codFornec = codFornec;
    }

    public int getCodFornec() {
        return codFornec;
    }

    public void setCodFornec(int codFornec) {
        this.codFornec = codFornec;
    }

    public String getNomeLogra() {
        return nomeLogra;
    }

    public void setNomeLogra(String nomeLogra) {
        this.nomeLogra = nomeLogra;
    }


    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPessoaCont() {
        return pessoaCont;
    }

    public void setPessoaCont(String pessoaCont) {
        this.pessoaCont = pessoaCont;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
   
    
    
    
    
    
}
