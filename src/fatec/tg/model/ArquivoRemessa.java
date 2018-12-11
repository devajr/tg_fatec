/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.model; 

/**
 *
 * @author Devair
 */
public class ArquivoRemessa {
    
    private int codigo;
    private int padraCnab;
    private double valorTotal;
    private String dataRemessa;
    private Banco banco;
    private Agencia agencia;
    private ContaBancaria contaBancaria;
    private String status;

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

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDataRemessa() {
        return dataRemessa;
    }

    public void setDataRemessa(String dataRemessa) {
        this.dataRemessa = dataRemessa;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public ArquivoRemessa(int codigo) {
        this.codigo = codigo;
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
