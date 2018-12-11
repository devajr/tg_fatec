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
public class TituloRecorrente {
    
    private Subconta subconta;
    private Fornecedor fornecedor;
    private Banco banco;
    private int qtdParcelas;
    private int frequencia;
    private int diasProtesto;
    private double juros;
    private double multa;
    private String historico;

    public TituloRecorrente(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Subconta getSubconta() {
        return subconta;
    }

    public void setSubconta(Subconta subconta) {
        this.subconta = subconta;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }



    public int getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(int qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public int getDiasProtesto() {
        return diasProtesto;
    }

    public void setDiasProtesto(int diasProtesto) {
        this.diasProtesto = diasProtesto;
    }

    public double getJuros() {
        return juros;
    }

    public void setJuros(double juros) {
        this.juros = juros;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
    
}
