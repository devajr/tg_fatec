package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class TituloParcela extends Titulo{
    
   private int numParcela;
   private String nossoNum;
   private double valorParcela;
   private double valorPago;
   private String dataVenc;
   private String dataPag;
   private String chequePara;
   private String statusPag;
   private String numCheque;
   private String transfBanc;
   private TipoPagamento tipoPagamento;
   
    public TituloParcela(int numLancamento ) {
        super(numLancamento);
       
    }

    public int getNumParcela() {
        return numParcela;
    }

    public void setNumParcela(int numParcela) {
        this.numParcela = numParcela;
    }

    public String getNossoNum() {
        return nossoNum;
    }

    public void setNossoNum(String nossoNum) {
        this.nossoNum = nossoNum;
    }

   

    public double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getDataVenc() {
        return dataVenc;
    }

    public void setDataVenc(String dataVenc) {
        this.dataVenc = dataVenc;
    }

    public String getDataPag() {
        return dataPag;
    }

    public void setDataPag(String dataPag) {
        this.dataPag = dataPag;
    }

    public String getChequePara() {
        return chequePara;
    }

    public void setChequePara(String chequePara) {
        this.chequePara = chequePara;
    }

    public String getStatusPag() {
        return statusPag;
    }

    public void setStatusPag(String statusPag) {
        this.statusPag = statusPag;
    }

    public String getNumCheque() {
        return numCheque;
    }

    public void setNumCheque(String numCheque) {
        this.numCheque = numCheque;
    }

    public String getTransfBanc() {
        return transfBanc;
    }

    public void setTransfBanc(String transfBanc) {
        this.transfBanc = transfBanc;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    
          
    
    
}
