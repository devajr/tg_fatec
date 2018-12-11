
package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class ContaBancaria extends Agencia{
    private double saldo;
    private double limite;
    private int numConta;
    private String situacao;

    public ContaBancaria(int codigo, int numAgencia, int numConta) {
        super(codigo, numAgencia);
        this.numConta = numConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public int getNumConta() {
        return numConta;
    }

    public void setNumConta(int numConta) {
        this.numConta = numConta;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
    
    
}
