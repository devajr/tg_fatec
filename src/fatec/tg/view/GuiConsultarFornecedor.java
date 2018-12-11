/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.view;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import fatec.tg.controle.Conexao;
import fatec.tg.controle.DaoFornecPF;
import fatec.tg.controle.DaoFornecPJ;
import fatec.tg.controle.DaoFornecedor;
import fatec.tg.controle.Valida;
import fatec.tg.model.FornecFisica;
import fatec.tg.model.FornecJuridica;
import fatec.tg.model.Fornecedor;
import fatec.tg.model.TipoPagamento;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.scene.control.ButtonType.OK;
import javax.swing.ListSelectionModel;
import org.eclipse.persistence.exceptions.EntityManagerSetupException;
/**
/**
 *
 * @author Devair
 */
public class GuiConsultarFornecedor extends javax.swing.JInternalFrame {

private GuiMenu guiMenu;
private static GuiConsultarFornecedor guiConsultarFornecedor;
public static GuiConsultarFornecedor getInstancia(){
    if(guiConsultarFornecedor==null){
        guiConsultarFornecedor= new GuiConsultarFornecedor();
       
    }
    return guiConsultarFornecedor;
}
    public GuiConsultarFornecedor() {
        initComponents();
    }
      public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);   
    }
      public void listarFornec(){
          ArrayList<Fornecedor> fornecedores = daoFornecedor.listarFornecedores();
            Exibir(fornecedores);
      }
    
 public String formataSituacao(String ent){
    
        if(ent.equals("I"))
           return "Inativo";
        else 
           return "Ativo";
        
    }
 public DefaultTableModel montaTabela(){
      DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Código");
            modelo.addColumn("Nome Fornecedor");
            modelo.addColumn("Cpf/Cnpj");
            modelo.addColumn("Rg/IE");
            modelo.addColumn("Telefone");
            modelo.addColumn("Cidade");
            modelo.addColumn("Situacão");
     return (modelo);
 }
 public void Exibir(ArrayList<Fornecedor>fornecedores){
   
        DefaultTableModel modelo=montaTabela();
        if(fornecedores.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhum fornecedor encontrado");
        }else{
            
            for(int i =0; i< fornecedores.size(); i++){
                fornecFisica=daoFornecPF.consultar(fornecedores.get(i).getCodFornec());
                fornecJuridica=daoFornecPJ.consultar(fornecedores.get(i).getCodFornec());
                //System.err.println(formataSituacao(fornecedores.get(i).getSituacao()));
                if(fornecFisica!=null)
                modelo.addRow(new Object[]{String.valueOf(fornecedores.get(i).getCodFornec()),fornecFisica.getNome(),fornecFisica.getCpf(),fornecFisica.getRg(),fornecedores.get(i).getTelefone(),fornecedores.get(i).getCidade(),formataSituacao(fornecedores.get(i).getSituacao())});
                else
                modelo.addRow(new Object[]{String.valueOf(fornecedores.get(i).getCodFornec()),fornecJuridica.getNomeFan(),fornecJuridica.getCnpj(),fornecJuridica.getInscEst(),fornecedores.get(i).getTelefone(),fornecedores.get(i).getCidade(),formataSituacao(fornecedores.get(i).getSituacao())});

            }
        }
        TabelaFornecedor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TabelaFornecedor.setModel(modelo);
        TabelaFornecedor.setDefaultEditor(Object.class, null);
 }
    public void ExibirPeloNome(ArrayList<FornecFisica> fornecPF,ArrayList<FornecJuridica> fornecPJ ){
        DefaultTableModel modelo=montaTabela();
            if(fornecPJ.isEmpty() && fornecPF.isEmpty()){
                JOptionPane.showMessageDialog(this, "Nenhum fornecedor encontrado");
            }else{
                for(int i =0; i< fornecPJ.size(); i++){
                    fornecJuridica=daoFornecPJ.consultar(fornecPJ.get(i).getCodFornec());
                    fornecedor=daoFornecedor.consultar(fornecJuridica.getCodFornec());
                    modelo.addRow(new Object[]{String.valueOf(fornecJuridica.getCodFornec()),fornecJuridica.getNomeFan(),fornecedor.getTelefone(),fornecedor.getCidade(),formataSituacao(fornecedor.getSituacao()) });

                }
                for(int i =0; i< fornecPF.size(); i++){
                    fornecFisica=daoFornecPF.consultar(fornecPF.get(i).getCodFornec());
                    fornecedor=daoFornecedor.consultar(fornecFisica.getCodFornec());
                    modelo.addRow(new Object[]{String.valueOf(fornecFisica.getCodFornec()),fornecFisica.getNome(),fornecFisica.getCpf(),fornecFisica.getRg(),fornecedor.getTelefone(),fornecedor.getCidade(),formataSituacao(fornecedor.getSituacao()) });

                }

            }

            TabelaFornecedor.setModel(modelo);
            TabelaFornecedor.setDefaultEditor(Object.class, null);
    }
    public void habilitaBotoes(boolean x){
            btnAlterar.setEnabled(x);
            btnSair.setEnabled(x);
            btnExcluir.setEnabled(x);
            btnNovo.setEnabled(x);
    }
    public void Consulta(){
       
            jTabProcurar.setSelectedIndex(1);
            int indiceLinha = TabelaFornecedor.getSelectedRow();
            fornecedor=null;
            fornecedor=daoFornecedor.consultar(Integer.parseInt((String) TabelaFornecedor.getValueAt(indiceLinha, 0)));
            txtDatacadastro.setText(fornecedor.getDataCadastro());
            txtCodigo1.setText(String.valueOf(fornecedor.getCodFornec()));
            txtBairro.setText(fornecedor.getBairro());
            txtComplemento.setText(fornecedor.getComplemento());
            txtEmail.setText(fornecedor.getEmail());
            txtMunicipio.setText(fornecedor.getCidade());  
            txtNomeContato.setText(fornecedor.getPessoaCont());
            txtNomeLogradouro.setText(fornecedor.getNomeLogra());
            txtNumero.setText(String.valueOf(fornecedor.getNumero()));
            txtPais.setText(fornecedor.getPais());        
            jtxtCEP.setText(fornecedor.getCep());              
            jtxtCelular.setText(fornecedor.getCelular());             
            jtxtTelefone.setText(fornecedor.getTelefone());       
            fornecFisica=daoFornecPF.consultar(Integer.parseInt((String) TabelaFornecedor.getValueAt(indiceLinha, 0)));
            fornecJuridica=daoFornecPJ.consultar(Integer.parseInt((String) TabelaFornecedor.getValueAt(indiceLinha, 0)));
            cbxLogradouro.setToolTipText(fornecedor.getLogradouro());
            cbxUF.setToolTipText(fornecedor.getEstado());
            cbxTipoPessoa.setEnabled(false);

            if(fornecedor.getSituacao().equals("A"))
             rbAtivo.setSelected(true);
            else
              rbInativo.setSelected(true);


            if(fornecFisica==null){
                txtNome.setEnabled(true);
                jtxtRG.setEnabled(true);
                jtxtCPF.setEnabled(true);
                cbxTipoPessoa.setSelectedIndex(2);

                jtxtCNPJ.setText(fornecJuridica.getCnpj());
                txtRazaoSocial.setText(fornecJuridica.getRazaoSocial());
                txtNomeFantasia.setText(fornecJuridica.getNomeFan());
                jtxtIE.setText(fornecJuridica.getInscEst());
            }
            else{
                txtNomeFantasia.setEnabled(true);
                txtRazaoSocial.setEnabled(true);
                jtxtCNPJ.setEnabled(true);
                jtxtIE.setEnabled(true);
                cbxTipoPessoa.setSelectedIndex(1);

                txtNome.setText(fornecFisica.getNome());
                jtxtCPF.setText(fornecFisica.getCpf());
                jtxtRG.setText(fornecFisica.getRg());

            }

            
    }
    public void habilitaLabelObrigatorio(boolean i){
        jLabelCnpj.setVisible(i);
        jLabelSituacao.setVisible(i);
        jLabelCpf.setVisible(i);
    }
    public void LimpaCampos(){
         txtBairro.setText("");
            txtComplemento.setText("");
            txtEmail.setText("");
            txtMunicipio.setText("");  
            txtNomeContato.setText("");
            txtNomeLogradouro.setText("");
            txtNumero.setText("");
            txtPais.setText("");        
            jtxtCEP.setText("");              
            jtxtCelular.setText("");             
            jtxtTelefone.setText("");
            cbxLogradouro.setSelectedIndex(0);
            cbxUF.setSelectedIndex(0);
            cbxTipoPessoa.setSelectedIndex(0);
            jtxtCNPJ.setText("");
            txtRazaoSocial.setText("");
            txtNomeFantasia.setText("");
            jtxtIE.setText("");
            txtNome.setText("");
            jtxtCPF.setText("");
            jtxtRG.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pteste = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTabProcurar = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNomeFornecedor = new javax.swing.JTextField();
        cbxSituaçao = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnFiltrar = new javax.swing.JButton();
        btnExibir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaFornecedor = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtCodigo1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cbxTipoPessoa = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        txtDatacadastro = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        rbAtivo = new javax.swing.JRadioButton();
        rbInativo = new javax.swing.JRadioButton();
        jLabelSituacao = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jtxtRG = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jtxtCPF = new javax.swing.JFormattedTextField();
        jLabelCpf = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtRazaoSocial = new javax.swing.JTextField();
        txtNomeFantasia = new javax.swing.JTextField();
        jtxtCNPJ = new javax.swing.JFormattedTextField();
        jtxtIE = new javax.swing.JFormattedTextField();
        jLabelCnpj = new javax.swing.JLabel();
        txtNomeContato = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtNomeLogradouro = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        txtPais = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        cbxUF = new javax.swing.JComboBox<>();
        cbxLogradouro = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jtxtTelefone = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jtxtCelular = new javax.swing.JFormattedTextField();
        jtxtCEP = new javax.swing.JFormattedTextField();
        jLabel27 = new javax.swing.JLabel();
        txtComplemento = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtMunicipio = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnGravar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Fornecedor");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        pteste.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Fornecedor");

        btnNovo.setBackground(new java.awt.Color(255, 255, 255));
        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-arquivo-26.png"))); // NOI18N
        btnNovo.setText("Novo");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });

        btnAlterar.setBackground(new java.awt.Color(255, 255, 255));
        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-editar-arquivo-26.png"))); // NOI18N
        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setBackground(new java.awt.Color(255, 255, 255));
        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-apagar-arquivo-26.png"))); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnSair.setBackground(new java.awt.Color(255, 255, 255));
        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-sair-26.png"))); // NOI18N
        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\Users\\Devair\\Google Drive\\fatec\\5 semestre\\TG\\logopp.png")); // NOI18N

        jTabProcurar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabProcurarStateChanged(evt);
            }
        });
        jTabProcurar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabProcurarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTabProcurarFocusLost(evt);
            }
        });
        jTabProcurar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabProcurarMouseClicked(evt);
            }
        });
        jTabProcurar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabProcurarKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtCodigo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });

        jLabel2.setText("Código");

        jLabel4.setText("Nome do Fornecedor");

        txtNomeFornecedor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtNomeFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeFornecedorKeyPressed(evt);
            }
        });

        cbxSituaçao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Ativo", "Inativo" }));
        cbxSituaçao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSituaçaoActionPerformed(evt);
            }
        });

        jLabel3.setText("Situação");

        btnFiltrar.setBackground(new java.awt.Color(255, 255, 255));
        btnFiltrar.setText("Filtrar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        btnExibir.setBackground(new java.awt.Color(255, 255, 255));
        btnExibir.setText("Exibir tudo");
        btnExibir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExibirActionPerformed(evt);
            }
        });

        TabelaFornecedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nome Fornecedor", "Cpf/Cnpj", "Rg/IE", "Telefone", "Cidade", "Situação"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelaFornecedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelaFornecedorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelaFornecedor);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbxSituaçao, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3))
                        .addGap(37, 37, 37)
                        .addComponent(btnExibir)))
                .addGap(22, 22, 22))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnExibir, btnFiltrar, cbxSituaçao});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxSituaçao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExibir)
                    .addComponent(txtNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnExibir, btnFiltrar, cbxSituaçao});

        jTabProcurar.addTab("Procurar", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setText("Código:");

        txtCodigo1.setEditable(false);
        txtCodigo1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCodigo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigo1ActionPerformed(evt);
            }
        });

        jLabel14.setText("Tipo Pessoa:");

        cbxTipoPessoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Física", "Jurídica" }));
        cbxTipoPessoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTipoPessoaActionPerformed(evt);
            }
        });

        jLabel25.setText("Data Cadastro:");

        txtDatacadastro.setEditable(false);
        txtDatacadastro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtDatacadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDatacadastroActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Situação"));
        jPanel5.setEnabled(false);

        rbAtivo.setText("Ativo");

        rbInativo.setText("Inativo");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(rbAtivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbInativo)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbAtivo)
                    .addComponent(rbInativo)))
        );

        jLabelSituacao.setForeground(java.awt.Color.red);
        jLabelSituacao.setText("*Campo Obrigatório");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));

        jLabel6.setText("Nome:");

        jLabel7.setText("RG:");

        txtNome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtNome.setEnabled(false);
        txtNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomeFocusLost(evt);
            }
        });

        jtxtRG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtRG.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtRG.setEnabled(false);
        jtxtRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtRGActionPerformed(evt);
            }
        });

        jLabel8.setText("CPF:");

        jtxtCPF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtCPF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCPF.setEnabled(false);
        jtxtCPF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtCPFFocusLost(evt);
            }
        });
        jtxtCPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCPFActionPerformed(evt);
            }
        });

        jLabelCpf.setForeground(java.awt.Color.red);
        jLabelCpf.setText("*Campo Obrigatório");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jtxtRG, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCpf)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(jtxtCPF)))))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtRG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCpf)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));

        jLabel10.setText("Razão Social:");

        jLabel11.setText("Nome Fantasia:");

        jLabel12.setText("IE:");

        jLabel13.setText("CNPJ:");

        txtRazaoSocial.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtRazaoSocial.setEnabled(false);
        txtRazaoSocial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRazaoSocialActionPerformed(evt);
            }
        });

        txtNomeFantasia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtNomeFantasia.setEnabled(false);
        txtNomeFantasia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeFantasiaActionPerformed(evt);
            }
        });

        jtxtCNPJ.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtCNPJ.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtCNPJ.setEnabled(false);
        jtxtCNPJ.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtCNPJFocusLost(evt);
            }
        });

        jtxtIE.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtIE.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###.###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtIE.setEnabled(false);

        jLabelCnpj.setForeground(java.awt.Color.red);
        jLabelCnpj.setText("*Campo Obrigatório");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRazaoSocial)
                            .addComponent(jLabel13)
                            .addComponent(jtxtCNPJ, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(txtNomeFantasia)
                            .addComponent(jtxtIE, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)))
                    .addComponent(jLabelCnpj))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRazaoSocial)
                    .addComponent(txtNomeFantasia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtIE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCNPJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCnpj)
                .addGap(23, 23, 23))
        );

        txtNomeContato.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtNomeContato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeContatoActionPerformed(evt);
            }
        });

        jLabel23.setText("Nome contato:");

        txtEmail.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        jLabel19.setText("E-mail:");

        txtNomeLogradouro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtNomeLogradouro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeLogradouroActionPerformed(evt);
            }
        });

        jLabel18.setText("Nome Logradouro:");

        txtBairro.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtPais.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setText("País:");

        jLabel24.setText("Bairro:");

        jLabel28.setText("UF:");

        cbxUF.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        cbxLogradouro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Avenida", "Rua", "Estrada", "Rodovia" }));
        cbxLogradouro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLogradouroActionPerformed(evt);
            }
        });

        jLabel15.setText("Logradouro:");

        jLabel20.setText("Número:");

        txtNumero.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel21.setText("Telefone:");

        jtxtTelefone.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel22.setText("Celular:");

        jtxtCelular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtCelular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jtxtCEP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtCEP.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel27.setText("CEP:");

        txtComplemento.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel26.setText("Complemento:");

        txtMunicipio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtMunicipio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMunicipioActionPerformed(evt);
            }
        });

        jLabel17.setText("Município:");

        btnGravar.setBackground(new java.awt.Color(255, 255, 255));
        btnGravar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-descarregar-da-nuvem-26.png"))); // NOI18N
        btnGravar.setText("Gravar");
        btnGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGravarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-cancelar-30.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDatacadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel25))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelSituacao))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtPais, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNomeLogradouro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel18)
                                .addComponent(jLabel19)
                                .addComponent(jLabel23)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNomeContato, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel16)
                            .addComponent(jLabel24))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(85, 85, 85)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15)
                                    .addComponent(cbxUF, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel26)
                                    .addComponent(txtMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtCEP, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jtxtTelefone)
                            .addComponent(jtxtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 48, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGravar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar)
                .addGap(35, 35, 35))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCancelar, btnGravar});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel14)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDatacadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSituacao)
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel28)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(jLabel15)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel20)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNomeLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtCEP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNomeContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnGravar))
                .addGap(41, 41, 41))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnCancelar, btnGravar});

        jTabProcurar.addTab("Cadastro", jPanel2);

        javax.swing.GroupLayout ptesteLayout = new javax.swing.GroupLayout(pteste);
        pteste.setLayout(ptesteLayout);
        ptesteLayout.setHorizontalGroup(
            ptesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ptesteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ptesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSair, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(btnNovo, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ptesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTabProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 1106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        ptesteLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAlterar, btnExcluir, btnNovo, btnSair});

        ptesteLayout.setVerticalGroup(
            ptesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ptesteLayout.createSequentialGroup()
                .addGroup(ptesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ptesteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addGap(15, 15, 15)
                        .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ptesteLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTabProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ptesteLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAlterar, btnExcluir, btnSair});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pteste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pteste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        
        conexao = new Conexao();

        daoFornecedor = new DaoFornecedor(conexao.conectar());
        daoFornecPF = new DaoFornecPF(conexao.conectar());
        daoFornecPJ = new DaoFornecPJ(conexao.conectar());
        ArrayList<Fornecedor> fornecedores = daoFornecedor.listarFornecedores();
        Exibir(fornecedores);
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
   
        conexao.fecharConexao();
        dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed

     

        if(TabelaFornecedor.getSelectedRowCount()==0){
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Fornecedor a ser excluido ", "Aviso de Exclusão",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        else
        {
            int indiceLinha = TabelaFornecedor.getSelectedRow();  
            fornecedor=daoFornecedor.consultar(Integer.parseInt((String) TabelaFornecedor.getValueAt(indiceLinha, 0)));
            fornecFisica=daoFornecPF.consultar((fornecedor.getCodFornec()));
            fornecJuridica=daoFornecPJ.consultar(fornecedor.getCodFornec());
            Object[] options = { "OK", "CANCELAR" };
             Object[] options1 = { "OK" };
            if(daoFornecedor.verifica(fornecedor.getCodFornec())==true)
            { 
                JOptionPane.showOptionDialog(null, "Este fornecedor não pode ser excluido por estar relacionado a titulos já cadastrados!", "Aviso de Exclusão",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options1, options[0]);
                jTabProcurar.setSelectedIndex(0);
            } 
            else
            {
                 if(fornecFisica==null)
                 {

                    if(JOptionPane.showOptionDialog(null, "Clique em OK para confirmar a exclusao do Fornecedor Pessoa Juridica: "+"\n"+
                      "Codigo: " + fornecedor.getCodFornec() + "\n" +
                      "Razao Social: " + fornecJuridica.getRazaoSocial() + "\n" + 
                      "Cnpj: " + fornecJuridica.getCnpj() + "\n" + 
                      "Inscrição Estadual: " + fornecJuridica.getInscEst(), 
                      "Aviso de Exclusão",
                      JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                      null, options, options[0])==0) 
                    {
                        daoFornecPJ.excluir(fornecJuridica);
                        daoFornecedor.excluir(fornecedor);
                         btnExibir.setSelected(true);
                    }
                    else
                        jTabProcurar.setSelectedIndex(0);
                }
                else
                 {
                    if(JOptionPane.showOptionDialog(null, "Clique em OK para confirmar a exclusao do Fornecedor Pessoa Fisica: " + "\n" +
                     "Codigo :" + fornecedor.getCodFornec() + "\n" +
                      "Nome: " + fornecFisica.getNome()+ "\n" +       
                      "Rg: " + fornecFisica.getRg() + "\n" + 
                      "Cpf: " + fornecFisica.getCpf(),
                      "Aviso de Exclusão",
                      JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                      null, options, options[0])==0)
                    {
                        daoFornecPF.excluir(fornecFisica);
                        daoFornecedor.excluir(fornecedor);
                         btnExibir.setSelected(true);
                    }
                    else
                        jTabProcurar.setSelectedIndex(0);
                }


            }
        
        }
       
        
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        
          if(TabelaFornecedor.getSelectedRowCount()==0){
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Fornecedor a ser Alterado ", "Aviso",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        else
        {
            Consulta();
            habilitaBotoes(false);
            btnGravar.setEnabled(true);
            btnCancelar.setEnabled(true);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        //new GuiCadastrarFornecedor().setVisible(true);
        txtCodigo1.setText(String.valueOf(daoFornecedor.obterCodFornec()+1));
        rbAtivo.setSelected(true);
        cbxTipoPessoa.setEnabled(true);
        jLabelSituacao.setVisible(false);
        jLabelCpf.setVisible(false);
        jLabelCnpj.setVisible(false);
        Date data = new Date(System.currentTimeMillis());  
        SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy"); 
        txtDatacadastro.setText(formatarDate.format(data));
        LimpaCampos();
        jTabProcurar.setSelectedIndex(1);
        
        habilitaBotoes(false);
        btnGravar.setEnabled(true);
        btnCancelar.setEnabled(true);        
        
        
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        // TODO add your handling code here:

        if( txtCodigo.getText().isEmpty()== false && txtNomeFornecedor.getText().isEmpty() && cbxSituaçao.getSelectedIndex()==0){
            fornecedor = null;
            fornecedor = daoFornecedor.consultar(Integer.parseInt(txtCodigo.getText()));
           DefaultTableModel modelo=montaTabela();
            if(fornecedor==null){
                JOptionPane.showMessageDialog(this, "Nenhum fornecedor encontrado");
            }else{
                fornecFisica=daoFornecPF.consultar(fornecedor.getCodFornec());
                fornecJuridica=daoFornecPJ.consultar(fornecedor.getCodFornec());
                if(fornecFisica!=null)
                modelo.addRow(new Object[]{String.valueOf(fornecedor.getCodFornec()),fornecFisica.getNome(),fornecFisica.getCpf(),fornecFisica.getRg(),fornecedor.getTelefone(),fornecedor.getCidade(),formataSituacao(fornecedor.getSituacao())});
                else
                modelo.addRow(new Object[]{String.valueOf(fornecedor.getCodFornec()),fornecJuridica.getNomeFan(),fornecJuridica.getCnpj(),fornecJuridica.getInscEst(),fornecedor.getTelefone(),fornecedor.getCidade(),formataSituacao(fornecedor.getSituacao())});

            }
            TabelaFornecedor.setModel(modelo);
            TabelaFornecedor.setDefaultEditor(Object.class, null);
        }
        else if(cbxSituaçao.getSelectedItem() != "Selecione" && txtCodigo.getText().isEmpty() && txtNomeFornecedor.getText().isEmpty())
        {
          ArrayList<Fornecedor> fornecedores = daoFornecedor.listarFornecedoresSituacao((String) cbxSituaçao.getSelectedItem());
            Exibir(fornecedores);
        }
        else if(txtCodigo.getText().isEmpty() && txtNomeFornecedor.getText().isEmpty()==false && cbxSituaçao.getSelectedItem() == "Selecione")
        {
            ArrayList<FornecJuridica> fornecPJ = daoFornecPJ.listarNomeFornecedores(txtNomeFornecedor.getText());
            ArrayList<FornecFisica> fornecPF = daoFornecPF.listarNomeFornecedores(txtNomeFornecedor.getText());
            ExibirPeloNome(fornecPF, fornecPJ);
            
        }
        else if(txtCodigo.getText().isEmpty() && txtNomeFornecedor.getText().isEmpty() && cbxSituaçao.getSelectedIndex()==0)
        JOptionPane.showMessageDialog(this, "Preencha um campo de filtro");
        else
        JOptionPane.showMessageDialog(this, "Apenas um campo de filtro deve ser preenchido");
        
        txtCodigo.setText("");
        txtNomeFornecedor.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnExibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirActionPerformed
       ArrayList<Fornecedor> fornecedores = daoFornecedor.listarFornecedores();
        Exibir(fornecedores);
    }//GEN-LAST:event_btnExibirActionPerformed

    private void cbxSituaçaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSituaçaoActionPerformed
         ArrayList<Fornecedor> fornecedores = daoFornecedor.listarFornecedoresSituacao((String) cbxSituaçao.getSelectedItem());
         Exibir(fornecedores);
         cbxSituaçao.setSelectedIndex(0);
    }//GEN-LAST:event_cbxSituaçaoActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void TabelaFornecedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelaFornecedorMouseClicked
        jTabProcurar.setSelectedIndex(1);
    }//GEN-LAST:event_TabelaFornecedorMouseClicked

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
          fornecedor = null;
            fornecedor = daoFornecedor.consultar(Integer.parseInt(txtCodigo.getText()));
           DefaultTableModel modelo=montaTabela();
            if(fornecedor==null){
                JOptionPane.showMessageDialog(this, "Nenhum fornecedor encontrado");
            }else{
                fornecFisica=daoFornecPF.consultar(fornecedor.getCodFornec());
                fornecJuridica=daoFornecPJ.consultar(fornecedor.getCodFornec());
                if(fornecFisica!=null)
                modelo.addRow(new Object[]{String.valueOf(fornecedor.getCodFornec()),fornecFisica.getNome(),fornecFisica.getCpf(),fornecFisica.getRg(),fornecedor.getTelefone(),fornecedor.getCidade(),formataSituacao(fornecedor.getSituacao())});
                else
                modelo.addRow(new Object[]{String.valueOf(fornecedor.getCodFornec()),fornecJuridica.getNomeFan(),fornecJuridica.getCnpj(),fornecJuridica.getInscEst(),fornecedor.getTelefone(),fornecedor.getCidade(),formataSituacao(fornecedor.getSituacao())});

            }
            TabelaFornecedor.setModel(modelo);
            TabelaFornecedor.setDefaultEditor(Object.class, null);
       }
       
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void txtNomeFornecedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeFornecedorKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            ArrayList<FornecJuridica> fornecPJ = daoFornecPJ.listarNomeFornecedores(txtNomeFornecedor.getText());
            ArrayList<FornecFisica> fornecPF = daoFornecPF.listarNomeFornecedores(txtNomeFornecedor.getText());
            ExibirPeloNome(fornecPF, fornecPJ);
        }
    }//GEN-LAST:event_txtNomeFornecedorKeyPressed

    private void txtMunicipioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMunicipioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMunicipioActionPerformed

    private void txtNomeContatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeContatoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeContatoActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtNomeLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeLogradouroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeLogradouroActionPerformed

    private void cbxLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLogradouroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxLogradouroActionPerformed

    private void txtDatacadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDatacadastroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDatacadastroActionPerformed

    private void jtxtCNPJFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCNPJFocusLost
        if(Valida.VerificaCnpj(jtxtCNPJ.getText())==false){
            JOptionPane.showMessageDialog(null, "Cnpj invalido");
            jtxtCNPJ.requestFocus();
        }
    }//GEN-LAST:event_jtxtCNPJFocusLost

    private void txtNomeFantasiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeFantasiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeFantasiaActionPerformed

    private void txtRazaoSocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRazaoSocialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRazaoSocialActionPerformed

    private void txtCodigo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigo1ActionPerformed

    private void jtxtCPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCPFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCPFActionPerformed

    private void jtxtCPFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCPFFocusLost
        if(Valida.Verifica_Cpf(jtxtCPF.getText())==false){
            JOptionPane.showMessageDialog(null, "Cpf invalido");
            jtxtCPF.requestFocus();
        }
    }//GEN-LAST:event_jtxtCPFFocusLost

    private void jtxtRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtRGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtRGActionPerformed

    private void txtNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeFocusLost

    private void cbxTipoPessoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTipoPessoaActionPerformed
        // TODO add your handling code here:
        if(cbxTipoPessoa.getSelectedIndex()==1){
            txtNome.setEnabled(true);
            jtxtRG.setEnabled(true);
            jtxtCPF.setEnabled(true);

            txtNomeFantasia.setEnabled(false);
            txtRazaoSocial.setEnabled(false);
            jtxtCNPJ.setEnabled(false);
            jtxtIE.setEnabled(false);

            txtNomeFantasia.setText("");
            txtRazaoSocial.setText("");
            jtxtCNPJ.setText("");
            jtxtIE.setText("");
            jLabelCnpj.setVisible(false);
        }
        else if(cbxTipoPessoa.getSelectedIndex()==2){

            txtNomeFantasia.setEnabled(true);
            txtRazaoSocial.setEnabled(true);
            jtxtCNPJ.setEnabled(true);
            jtxtIE.setEnabled(true);

            txtNome.setEnabled(false);
            jtxtRG.setEnabled(false);
            jtxtCPF.setEnabled(false);

            txtNome.setText("");
            jtxtCPF.setText("");
            jtxtRG.setText("");
            jLabelCpf.setVisible(false);
        }
    }//GEN-LAST:event_cbxTipoPessoaActionPerformed

    private void btnGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarActionPerformed
        // TODO add your handling code here:
        //String novo="Fornecedor Cadastrado com sucesso!",alterar="Fornecedor Alterado com sucesso!";
        
        if(daoFornecedor.obterCodFornec()<Integer.parseInt(txtCodigo1.getText())){
            fornecFisica=null;
            fornecJuridica=null;
            fornecedor= new Fornecedor(Integer.parseInt(txtCodigo1.getText()));
            fornecedor.setPais(txtPais.getText());
            fornecedor.setEstado((String) cbxUF.getSelectedItem());
            fornecedor.setCidade(txtMunicipio.getText());
            fornecedor.setBairro(txtBairro.getText());
            fornecedor.setCep(jtxtCEP.getText().replace("-", ""));
            fornecedor.setNomeLogra(txtNomeLogradouro.getText());
            fornecedor.setLogradouro(String.valueOf(cbxLogradouro.getSelectedItem()));
            if(txtNumero.getText().equals("")==false){
                fornecedor.setNumero(Integer.parseInt(txtNumero.getText()));
            }
            fornecedor.setComplemento(txtComplemento.getText());
            fornecedor.setTelefone(jtxtTelefone.getText().replace("(", "").replace(")", "").replace("-", ""));
            fornecedor.setCelular(jtxtCelular.getText().replace("(", "").replace(")", "").replace("-", ""));
            fornecedor.setEmail(txtEmail.getText());
            fornecedor.setDataCadastro(txtDatacadastro.getText().replace("/", ""));
            fornecedor.setPessoaCont(txtNomeContato.getText());

            if(rbAtivo.isSelected())
            fornecedor.setSituacao("A");
            else if(rbInativo.isSelected())
            fornecedor.setSituacao("I");

            if(cbxTipoPessoa.getSelectedIndex()==0)
            jLabelSituacao.setVisible(true);
            else{
                if(cbxTipoPessoa.getSelectedIndex()==1){
                    if(jtxtCPF.getText().isEmpty()==false)
                    jLabelCpf.setVisible(true);
                    else
                    jLabelCpf.setVisible(false);
                }
                else if(cbxTipoPessoa.getSelectedIndex()==2){
                    if(jtxtCNPJ.getText().isEmpty()==false)
                    jLabelCnpj.setVisible(true);
                    else
                    jLabelCnpj.setVisible(false);
                }

                jLabelSituacao.setVisible(false);
            }

            if(cbxTipoPessoa.getSelectedIndex()==1){
                fornecFisica= new FornecFisica(fornecedor.getCodFornec());
                fornecFisica.setCpf(jtxtCPF.getText().replace(".", "").replace("-","").replace(" ",""));
                fornecFisica.setNome(txtNome.getText());
                fornecFisica.setRg(jtxtRG.getText().replace(".", "").replace("-", ""));

            }
            else if(cbxTipoPessoa.getSelectedIndex()==2){
                fornecJuridica=new FornecJuridica(fornecedor.getCodFornec());
                fornecJuridica.setCnpj(jtxtCNPJ.getText().replace(".", "").replace("-","").replace("/","").replace(" ",""));
                fornecJuridica.setInscEst(jtxtIE.getText());
                fornecJuridica.setNomeFan(txtNomeFantasia.getText());
                fornecJuridica.setRazaoSocial(txtRazaoSocial.getText());

            }

            if(cbxTipoPessoa.getSelectedIndex()!= 0 && (jtxtCNPJ.getText().replace(".", "").replace("-","").replace("/","").replace(" ","").length()!=0|| jtxtCPF.getText().replace(".", "").replace("-","").replace(" ","").length()!=0 ))
            {
                System.out.println(jtxtCPF.getText().replace(".", "").replace("-","").replace(" ","").length());
                daoFornecedor.inserir(fornecedor);
                if(cbxTipoPessoa.getSelectedIndex()==1)
                daoFornecPF.inserir(fornecFisica);
                else if(cbxTipoPessoa.getSelectedIndex()==2)
                daoFornecPJ.inserir(fornecJuridica);
                JOptionPane.showMessageDialog(this, "Fornecedor Cadastrado com sucesso!");
                txtBairro.setText("");
                txtCodigo.setText("");
                txtComplemento.setText("");
                txtEmail.setText("");
                txtMunicipio.setText("");
                txtNome.setText("");
                txtNomeContato.setText("");
                txtNomeFantasia.setText("");
                txtNomeLogradouro.setText("");
                txtNumero.setText("");
                txtPais.setText("");
                txtRazaoSocial.setText("");
                jtxtCEP.setText("");
                jtxtCNPJ.setText("");
                jtxtCPF.setText("");
                jtxtCelular.setText("");
                jtxtIE.setText("");
                jtxtRG.setText("");
                jtxtTelefone.setText("");
                cbxLogradouro.setSelectedIndex(0);
                cbxTipoPessoa.setSelectedIndex(0);
                cbxUF.setSelectedIndex(0);
                rbAtivo.setSelected(false);
                rbInativo.setSelected(false);
                jTabProcurar.setSelectedIndex(0);
                habilitaBotoes(true);
                listarFornec();
            }
            else{
                JOptionPane.showMessageDialog(this, "Confira os campos Obrigatórios!!");
            }
        }
        else{
                fornecFisica=null;
                fornecJuridica=null;
                fornecedor= new Fornecedor(Integer.parseInt(txtCodigo1.getText()));
                fornecedor.setPais(txtPais.getText());
                fornecedor.setEstado((String) cbxUF.getSelectedItem());
                fornecedor.setCidade(txtMunicipio.getText());
                fornecedor.setBairro(txtBairro.getText());
                fornecedor.setCep(jtxtCEP.getText().replace("-", ""));
                fornecedor.setNomeLogra(txtNomeLogradouro.getText());
                fornecedor.setLogradouro(String.valueOf(cbxLogradouro.getSelectedItem()));
                if(txtNumero.getText().equals("")==false){
                    fornecedor.setNumero(Integer.parseInt(txtNumero.getText()));
                }
                fornecedor.setComplemento(txtComplemento.getText());
                fornecedor.setTelefone(jtxtTelefone.getText().replace("(", "").replace(")", "").replace("-", ""));
                fornecedor.setCelular(jtxtCelular.getText().replace("(", "").replace(")", "").replace("-", ""));
                fornecedor.setEmail(txtEmail.getText());
                fornecedor.setDataCadastro(txtDatacadastro.getText().replace("/", ""));
                fornecedor.setPessoaCont(txtNomeContato.getText());

                if(rbAtivo.isSelected())
                fornecedor.setSituacao("A");
                else if(rbInativo.isSelected())
                fornecedor.setSituacao("I");
                daoFornecedor.alterar(fornecedor);    
                    if(cbxTipoPessoa.getSelectedIndex()==1){
                    fornecFisica= new FornecFisica(fornecedor.getCodFornec());
                    fornecFisica.setCpf(jtxtCPF.getText().replace(".", "").replace("-","").replace(" ",""));
                    fornecFisica.setNome(txtNome.getText());
                    fornecFisica.setRg(jtxtRG.getText().replace(".", "").replace("-", ""));
                    daoFornecPF.alterar(fornecFisica);
                    }
                    else if(cbxTipoPessoa.getSelectedIndex()==2){
                    fornecJuridica=new FornecJuridica(fornecedor.getCodFornec());
                    fornecJuridica.setCnpj(jtxtCNPJ.getText().replace(".", "").replace("-","").replace("/","").replace(" ",""));
                    fornecJuridica.setInscEst(jtxtIE.getText());
                    fornecJuridica.setNomeFan(txtNomeFantasia.getText());
                    fornecJuridica.setRazaoSocial(txtRazaoSocial.getText());
                    daoFornecPJ.alterar(fornecJuridica);
                    }   

                   JOptionPane.showMessageDialog(this, "Fornecedor Alterado com sucesso!");
                   
            jTabProcurar.setSelectedIndex(0);
            ArrayList<Fornecedor> fornecedores = daoFornecedor.listarFornecedores();
            Exibir(fornecedores);
            habilitaBotoes(true);
            
        }
      LimpaCampos();

    }//GEN-LAST:event_btnGravarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
            jTabProcurar.setSelectedIndex(0);
            LimpaCampos();
            habilitaBotoes(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void jTabProcurarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabProcurarFocusGained
   
    }//GEN-LAST:event_jTabProcurarFocusGained

    private void jTabProcurarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabProcurarFocusLost
      
    }//GEN-LAST:event_jTabProcurarFocusLost

    private void jTabProcurarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabProcurarMouseClicked
       
    }//GEN-LAST:event_jTabProcurarMouseClicked

    private void jTabProcurarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabProcurarStateChanged
         if(jTabProcurar.getSelectedIndex()==1 && btnNovo.hasFocus()==false){
          if(TabelaFornecedor.getSelectedRowCount()==0){
            jTabProcurar.setSelectedIndex(0);
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Título a consultar ", "Aviso",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        else
        {
            Consulta();
            habilitaLabelObrigatorio(false);
            btnGravar.setEnabled(false);
            btnCancelar.setEnabled(false);
            
        }
      }
         
    }//GEN-LAST:event_jTabProcurarStateChanged

    private void jTabProcurarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabProcurarKeyPressed
        // TODO add your handling code here:
          
    }//GEN-LAST:event_jTabProcurarKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelaFornecedor;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnExibir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnGravar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSair;
    private javax.swing.JComboBox<String> cbxLogradouro;
    private javax.swing.JComboBox<String> cbxSituaçao;
    private javax.swing.JComboBox<String> cbxTipoPessoa;
    private javax.swing.JComboBox<String> cbxUF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCnpj;
    private javax.swing.JLabel jLabelCpf;
    private javax.swing.JLabel jLabelSituacao;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabProcurar;
    private javax.swing.JFormattedTextField jtxtCEP;
    private javax.swing.JFormattedTextField jtxtCNPJ;
    private javax.swing.JFormattedTextField jtxtCPF;
    private javax.swing.JFormattedTextField jtxtCelular;
    private javax.swing.JFormattedTextField jtxtIE;
    private javax.swing.JFormattedTextField jtxtRG;
    private javax.swing.JFormattedTextField jtxtTelefone;
    private javax.swing.JPanel pteste;
    private javax.swing.JRadioButton rbAtivo;
    private javax.swing.JRadioButton rbInativo;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCodigo1;
    private javax.swing.JTextField txtComplemento;
    private javax.swing.JFormattedTextField txtDatacadastro;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMunicipio;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNomeContato;
    private javax.swing.JTextField txtNomeFantasia;
    private javax.swing.JTextField txtNomeFornecedor;
    private javax.swing.JTextField txtNomeLogradouro;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtPais;
    private javax.swing.JTextField txtRazaoSocial;
    // End of variables declaration//GEN-END:variables

    private Conexao conexao = null;
    private DaoFornecedor daoFornecedor = null;
    private  Fornecedor fornecedor = null;
    private DaoFornecPF daoFornecPF = null;
    private FornecFisica fornecFisica = null;
    private FornecJuridica fornecJuridica = null;
    private DaoFornecPJ daoFornecPJ = null;

}
