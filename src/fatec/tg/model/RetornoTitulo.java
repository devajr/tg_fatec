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
public class RetornoTitulo {
    
    private String status;
    private ArquivoRetorno arquivoRetorno;
    private TituloParcela tituloParcela;

    public RetornoTitulo(ArquivoRetorno arquivoRetorno, TituloParcela tituloParcela) {
        this.arquivoRetorno = arquivoRetorno;
        this.tituloParcela = tituloParcela;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArquivoRetorno getArquivoRetorno() {
        return arquivoRetorno;
    }

    public void setArquivoRetorno(ArquivoRetorno arquivoRetorno) {
        this.arquivoRetorno = arquivoRetorno;
    }

    public TituloParcela getTituloParcela() {
        return tituloParcela;
    }

    public void setTituloParcela(TituloParcela tituloParcela) {
        this.tituloParcela = tituloParcela;
    }
    
    
    
}
