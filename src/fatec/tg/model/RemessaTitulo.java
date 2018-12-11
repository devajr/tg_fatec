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
public class RemessaTitulo {
    private ArquivoRemessa arquivoRemessa;
    private TituloParcela tituloParcela;
    private String status;

    public RemessaTitulo(ArquivoRemessa arquivoRemessa, TituloParcela tituloParcela) {
        this.arquivoRemessa = arquivoRemessa;
        this.tituloParcela = tituloParcela;
    }
    
    public ArquivoRemessa getArquivoRemessa() {
        return arquivoRemessa;
    }

    public void setArquivoRemessa(ArquivoRemessa arquivoRemessa) {
        this.arquivoRemessa = arquivoRemessa;
    }

    public TituloParcela getTituloParcela() {
        return tituloParcela;
    }

    public void setTituloParcela(TituloParcela tituloParcela) {
        this.tituloParcela = tituloParcela;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
