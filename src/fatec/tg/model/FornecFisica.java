package fatec.tg.model;


public class FornecFisica extends Fornecedor{
    
    private String nome;
    private String cpf;
    private String rg;
   
    public FornecFisica(int codigo) {
        super(codigo);
  
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }
    
    
    
}
