package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class Titulo {
    
    
    private String dtEmissao;
    private int qtdParcelas;
    private String tituloRecorrente;
    private String frequencia;
    private double juros;
    private int ndProtesto;
    private double multa;
    private String historico;
    private int numLancamento;
    private int qtdParcela;
    private Banco banco;
    private Banco titularBanco;
    private FornecFisica fornecFisica;
    private FornecJuridica fornecJuridica;
    private TipoPagamento tipoPagmento;
    private Subconta subconta;
    private Fornecedor fornecedor;
   
   
    public String getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(String dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public int getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(int qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public String getTituloRecorrente() {
        return tituloRecorrente;
    }

    public void setTituloRecorrente(String tituloRecorrente) {
        this.tituloRecorrente = tituloRecorrente;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public double getJuros() {
        return juros;
    }

    public void setJuros(double juros) {
        this.juros = juros;
    }

    public int getNdProtesto() {
        return ndProtesto;
    }

    public void setNdProtesto(int ndProtesto) {
        this.ndProtesto = ndProtesto;
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

    public int getNumLancamento() {
        return numLancamento;
    }

    public void setNumLancamento(int numLancamento) {
        this.numLancamento = numLancamento;
    }

    public int getQtdParcela() {
        return qtdParcela;
    }

    public void setQtdParcela(int qtdParcela) {
        this.qtdParcela = qtdParcela;
    }

    public Titulo(int numLancamento) {
        this.numLancamento = numLancamento;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Banco getTitularBanco() {
        return titularBanco;
    }

    public void setTitularBanco(Banco titularBanco) {
        this.titularBanco = titularBanco;
    }

    public FornecFisica getFornecFisica() {
        return fornecFisica;
    }

    public void setFornecFisica(FornecFisica fornecFisica) {
        this.fornecFisica = fornecFisica;
    }

    public FornecJuridica getFornecJuridica() {
        return fornecJuridica;
    }

    public void setFornecJuridica(FornecJuridica fornecJuridica) {
        this.fornecJuridica = fornecJuridica;
    }

    public TipoPagamento getTipoPagmento() {
        return tipoPagmento;
    }

    public void setTipoPagmento(TipoPagamento tipoPagmento) {
        this.tipoPagmento = tipoPagmento;
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
    
    
    
    
}
