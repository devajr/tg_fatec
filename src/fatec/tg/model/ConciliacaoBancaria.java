
package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class ConciliacaoBancaria {
    private int codigo;
    private String dataCon;
    private double saldoAnt;
    private double saldoAtual;
    private Banco banco;
    private Agencia agencia;
    private ContaBancaria contaBancaria;  
    private String status;

    public ConciliacaoBancaria(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDataCon() {
        return dataCon;
    }

    public void setDataCon(String dataCon) {
        this.dataCon = dataCon;
    }

    public double getSaldoAnt() {
        return saldoAnt;
    }

    public void setSaldoAnt(double saldoAnt) {
        this.saldoAnt = saldoAnt;
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(double saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
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
    
    
}
