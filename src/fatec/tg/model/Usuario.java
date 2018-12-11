package fatec.tg.model;

/**
 *
 * @author Devair
 */
public class Usuario {
    
    private int codUsuario;
    private String usuario;
    private String senha;

    public Usuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

   
     
    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
   

    

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    
}
