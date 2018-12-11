/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.view;

import fatec.tg.controle.Admin;

import fatec.tg.controle.Conexao;
import fatec.tg.controle.DaoBanco;
import fatec.tg.controle.DaoFornecPF;
import fatec.tg.controle.DaoFornecPJ;
import fatec.tg.controle.DaoFornecedor;
import fatec.tg.controle.DaoRecorrente;
import fatec.tg.controle.DaoSubconta;
import fatec.tg.controle.DaoTitulo;
import fatec.tg.controle.DaoTituloParcela;
import fatec.tg.model.Banco;
import fatec.tg.model.FornecFisica;
import fatec.tg.model.FornecJuridica;
import fatec.tg.model.Fornecedor;
import fatec.tg.model.Subconta;
import fatec.tg.model.Titulo;
import fatec.tg.model.TituloParcela;
import fatec.tg.model.TituloRecorrente;
import static fatec.tg.view.GuiMenu.painelMenu;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Devair
 */
public class GuiContasAPagar extends javax.swing.JInternalFrame {
    private static GuiContasAPagar guiContasAPagar;
    GuiExcluirTitulo passarParcela;
    GuiPagarTitulo pagarParcela;
  
    public GuiContasAPagar() {
        initComponents();
          
    }
    public  ArrayList<TituloParcela> titulosP;
    
    public static GuiContasAPagar getInstancia(){
        if(guiContasAPagar==null)
            guiContasAPagar=new GuiContasAPagar();
        return guiContasAPagar;
    }
     public  String buscaFornec(int cod){
        fornecFisica=daoFornecPF.consultar(cod);
        if(fornecFisica==null){
            fornecJuridica=daoFornecPJ.consultar(cod);
            return fornecJuridica.getNomeFan();
        }
        else{
             return fornecFisica.getNome();
           
        }
        
     }
    
     public double converte(String arg) throws ParseException{
		//obtem um NumberFormat para o Locale default (BR)
                
                    NumberFormat nf = NumberFormat.getNumberInstance();
                    double number = nf.parse(arg).doubleValue();
		return number;
                
		//converte um número com vírgulas ex: 2,56 para double
        
		
	}
      Comparator<String> comparator = new Comparator<String>() {  
        public int compare(String as_dataprimaria, String as_datasecundaria) {  
            Date dataPrimaria = null;  
            Date dataSecundaria = null;  
            try {  
                dataPrimaria   = Admin.formataData(as_dataprimaria);
                dataSecundaria = Admin.formataData(as_datasecundaria);
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return dataPrimaria.compareTo(dataSecundaria);
        }  
    };
    Comparator<String> comparatorDouble = new Comparator<String>() {
    public int compare(String s1, String s2) {
        Double valor1 = null;
        Double valor2 = null;
        try {
            valor1 = Admin.converte(s1);
            valor2 = Admin.converte(s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valor1.compareTo(valor2);
    }
};
     public void AtualizaParcela(){
         if(titulosP != null){
            jtxtDtVencParcela2.setText(titulosP.get(cbxNumParcela2.getSelectedIndex()).getDataVenc());
            jtxtValorParcela2.setText(String.valueOf(titulosP.get(cbxNumParcela2.getSelectedIndex()).getValorParcela()));
            txtCodBarras2.setText(titulosP.get(cbxNumParcela2.getSelectedIndex()).getNossoNum());
         }
         
       
     }
     
     public int buscaCodFornec(String nome){
        
         fornecFisica=daoFornecPF.buscaFornecPF(nome);
        if(fornecFisica==null){
            fornecJuridica=daoFornecPJ.buscaFornecPJ(nome);
            return fornecJuridica.getCodFornec();
        }
        else{
             return fornecFisica.getCodFornec();
        }
     }
 public DefaultTableModel montaTabela(){
      DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("N° Lançamento");
            modelo.addColumn("N° Parcela");
            modelo.addColumn("Forncecedor");
            modelo.addColumn("Vencimento");
            modelo.addColumn("Banco");
            modelo.addColumn("Valor");
           
            modelo.addColumn("Historico");

     return (modelo);
 }
 public void Exibir(ArrayList<TituloParcela>titulosParcela) throws ParseException{
     conexao = new Conexao();
     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
     int cont=0;
     double total=0.0;
     double totalVencido=0;
     Date hoje =new Date();
        DefaultTableModel modelo=montaTabela();
        if(titulosParcela.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhum Titulo encontrado");

        }else{
            
          for(int i =0; i< titulosParcela.size(); i++){
               
                modelo.addRow(new Object[]{String.valueOf(titulosParcela.get(i).getNumLancamento()),titulosParcela.get(i).getNumParcela()
                         ,titulosParcela.get(i).getFornecFisica()==null? titulosParcela.get(i).getFornecJuridica().getNomeFan(): titulosParcela.get(i).getFornecFisica().getNome(),
                        titulosParcela.get(i).getDataVenc(),titulosParcela.get(i).getBanco()==null? " " : titulosParcela.get(i).getBanco().getNome(),
                        String.valueOf(titulosParcela.get(i).getValorParcela()),titulosParcela.get(i).getHistorico()});
            
                cont++;
              total=total+titulosParcela.get(i).getValorParcela();
              Date datavenc = Admin.formataData((String)titulosParcela.get(i).getDataVenc());
              if(datavenc.before(hoje)){
                  totalVencido=totalVencido+titulosParcela.get(i).getValorParcela();
              }
             jtxtTotalVencido.setText(String.valueOf(totalVencido));
            }
        }
         TabelaContasAPagar.setModel(modelo);
          TableRowSorter<TableModel> sorter =new TableRowSorter<TableModel>(modelo);
           sorter.setComparator(3, comparator);
           sorter.setComparator(5, comparatorDouble);
           
         TabelaContasAPagar.setRowSorter(sorter);
         TabelaContasAPagar.setDefaultRenderer(Object.class,renderer);
         TabelaContasAPagar.setDefaultEditor(Object.class, null);
         TabelaContasAPagar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         txtTotalLancamentos.setText(String.valueOf(cont));
         jtxtTotalPagar.setText(String.format("%.2f",total).replace(".",","));
         jtxtTotalPagar.requestFocus();
         System.out.println(String.valueOf(total));
         conexao.fecharConexao();
 }
 public void listaPorCodigo(){
     titulosParcela  = daoTituloParcela.listarTitulosCod(Integer.parseInt(txtNumLancamento.getText()),"N");
            try {
                Exibir(titulosParcela);
                txtNumLancamento.setText("");
            } catch (ParseException ex) {
                Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
 }
 public void listaPorFornec(){

        
        try {
            
            titulosParcela = daoTituloParcela.listarTitulosFornec(buscaCodFornec((String) cbxFornecedor.getSelectedItem()),"N");
            Exibir(titulosParcela);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
 }

 DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
        String estado = table.getModel().getValueAt(row,3).toString();
      
        Date hoje =new Date();
        Date str = null;
        try {
            str = Admin.formataData((String) estado);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (str.before(hoje)) {
            c.setForeground(Color.RED);
            
        } else {
            c.setForeground(Color.BLACK);
        }
        
        return c;
        
    }
};
 public void HabilitaBotao(Boolean b){
      btnAlterar.setEnabled(b);
      btnExcluir.setEnabled(b);
      btnImprimir.setEnabled(b);
      btnNovo.setEnabled(b);
      btnPagar.setEnabled(b);
      btnSimular.setEnabled(b);
      btnSair.setEnabled(b);
            
 }
 
 public void HabilitaLbValidacao(Boolean b){
     jLabelFreq.setVisible(b);
        jLabelFornec.setVisible(b);
        jLabelSubconta.setVisible(b);
        jLabelQtdParc.setVisible(b);
        jLabelValorP.setVisible(b);
        jLabelDtVenc.setVisible(b);
        jLabelDtEmissao.setVisible(b);
 }
 public void LimpaCampos(){
     cbxFornecedor1.setSelectedIndex(0);
     cbxBancoTitular.setSelectedIndex(0);
     cbxSubconta.setSelectedIndex(0);
     cbxFrequencia.setSelectedIndex(0);
     //cbxNumParcela2=null;
     txtParcelas.setText("");
     txtDiasProtesto.setText("");
     jtxtJuros.setText("");
     jtxtMulta.setText("");
     txtHistorico.setText("");
     jtxtDtEmissao.setText("");
     jtxtDtVencParcela2.setText("");
     jtxtValorParcela2.setText("");
     txtCodBarras2.setText("");
    
 }
  public String buscaNomeSubconta(int cod){
      subconta=daoSubconta.consultar(cod);
      return(subconta.getNome());
  }
    public String buscaNomeBanco(int cod){
      banco=daoBanco.consultar(cod);
      return(banco.getNome());
  }
  public void Consulta(){
     cbxFornecedor1();
     cbxSubconta();
     cbxBanco();
     
     int indiceLinha = TabelaContasAPagar.getSelectedRow(); 
     int cod=0,parc=0;
     jTabProcurar.setSelectedIndex(1);
     cod=Integer.parseInt(""+TabelaContasAPagar.getValueAt(indiceLinha, 0));
     parc=Integer.parseInt(""+TabelaContasAPagar.getValueAt(indiceLinha, 1));

     titulo=daoTitulo.consultar(cod);
     tituloParcela=daoTituloParcela.consultar(cod,parc);
     txtNumLancamento1.setText(String.valueOf(titulo.getNumLancamento()));
     cbxFornecedor1.setSelectedItem(buscaFornec(titulo.getFornecedor().getCodFornec()));
     cbxBancoTitular.setSelectedItem(buscaNomeBanco(titulo.getBanco().getCodBanco()));
     cbxSubconta.setSelectedItem(buscaNomeSubconta(titulo.getSubconta().getCodigo()));
     txtParcelas.setText("");
     txtParcelas.setEnabled(false);
     cbxFrequencia.setEnabled(false);
     txtDiasProtesto.setText(String.valueOf(titulo.getNdProtesto()));
     jtxtJuros.setText(String.valueOf(titulo.getJuros()).replace(".", ","));
     jtxtMulta.setText(String.valueOf(titulo.getMulta()).replace(".", ","));
     txtHistorico.setText(titulo.getHistorico());
     jtxtDtEmissao.setText(titulo.getDtEmissao());
     jtxtDtVencParcela2.setText(tituloParcela.getDataVenc());
     System.out.println("data emissao");
     System.out.println(titulo.getDtEmissao());
     jtxtValorParcela2.setText(String.valueOf(tituloParcela.getValorParcela()).replace(".", ","));
     txtCodBarras2.setText(tituloParcela.getNossoNum());
     cbxNumParcela2.addItem(String.valueOf(parc));
    tituloRecorrente=daoRecorrente.consultar(titulo.getFornecedor().getCodFornec());
            if(tituloRecorrente!=null){
               ckbContaCorrente.setSelected(true);
            }
    
 }
 public void Cadastrar(){
     
     HabilitaLbValidacao(false);
     jPanelTituloParcela.setEnabled(false);
     HabilitarTituloParcela(false);
     LimpaCampos();
     txtNumLancamento1.setText(String.valueOf(daoTitulo.obterNumLancamento()+1));
  
 }
 public void cbxFornecedor1(){
        ArrayList<FornecJuridica> fornecedoresPJ = daoFornecPJ.listarFornecedoresPJ();
        ArrayList<FornecFisica> fornecedoresPF = daoFornecPF.listarFornecedoresPF();
        for (int x=0; x<fornecedoresPJ.size(); x++){
           cbxFornecedor1.addItem(fornecedoresPJ.get(x).getNomeFan()); 
        }  
        for (int x=0; x<fornecedoresPF.size(); x++){ 
           cbxFornecedor1.addItem(fornecedoresPF.get(x).getNome()); 
        }  
 }
 public void cbxFornecedor(){
        ArrayList<FornecJuridica> fornecedoresPJ = daoFornecPJ.listarFornecedoresPJ();
        ArrayList<FornecFisica> fornecedoresPF = daoFornecPF.listarFornecedoresPF();
        for (int x=0; x<fornecedoresPJ.size(); x++){
           cbxFornecedor.addItem(fornecedoresPJ.get(x).getNomeFan()); 
          
        }  
        for (int x=0; x<fornecedoresPF.size(); x++){
           cbxFornecedor.addItem(fornecedoresPF.get(x).getNome()); 
        }  
 }
  public void cbxBanco(){
        ArrayList<Banco> bancos = daoBanco.listarBancos();
        for (int x=0; x<bancos.size(); x++){
           cbxBancoTitular.addItem(bancos.get(x).getNome()); 
        }   
 }
   public void cbxSubconta(){
        ArrayList<Subconta> subcontas = daoSubconta.listarSubcontas();
        for (int x=0; x<subcontas.size(); x++){
           cbxSubconta.addItem(subcontas.get(x).getNome()); 
        }   
 }
   
   public  ArrayList<TituloParcela> CriaParcelas(){
         ArrayList<TituloParcela> titulos = null;
        TituloParcela i = null;
         for (int x=0; x<Integer.parseInt(txtParcelas.getText()); x++){
            i= new TituloParcela(Integer.parseInt(txtNumLancamento1.getText()));
            i.setNumParcela(x+1);         
        }  
         titulos.add(i);
         return titulos;
   }
   public void HabilitarTituloParcela(Boolean b){
       jtxtDtVencParcela2.setEnabled(b);
      jtxtDtVencParcela2.setEnabled(b);
      cbxNumParcela2.setEnabled(b);
      txtCodBarras2.setEnabled(b);
      jPanelTituloParcela.setEnabled(b);
   }
     public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);   
    }
       public void AtualizaTitulos(){
    titulosParcela  = daoTituloParcela.listarTitulos("N");
        try {
             Exibir(titulosParcela);
        } catch (ParseException ex) {
              Logger.getLogger(GuiPagarTitulo.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnPagar = new javax.swing.JButton();
        btnSimular = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnNovo = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jTabProcurar = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelaContasAPagar = new javax.swing.JTable();
        txtTotalLancamentos = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbxFornecedor = new javax.swing.JComboBox<>();
        cbxOrdenar = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNumLancamento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtDataInicio = new javax.swing.JFormattedTextField();
        jtxtDataFim = new javax.swing.JFormattedTextField();
        btnFiltrar = new javax.swing.JButton();
        btnExibir = new javax.swing.JButton();
        jtxtTotalVencido = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        jtxtTotalPagar = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        txtParcelas = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbxFrequencia = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtNumLancamento1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jtxtDtEmissao = new javax.swing.JFormattedTextField();
        cbxFornecedor1 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jPanelTituloParcela = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jtxtDtVencParcela2 = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cbxNumParcela2 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        txtCodBarras2 = new javax.swing.JTextField();
        jtxtValorParcela2 = new javax.swing.JFormattedTextField();
        jLabelDtVenc = new javax.swing.JLabel();
        jLabelValorP = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cbxBancoTitular = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtDiasProtesto = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cbxSubconta = new javax.swing.JComboBox<>();
        txtHistorico = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        btnGravar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        ckbContaCorrente = new javax.swing.JCheckBox();
        jLabelFornec = new javax.swing.JLabel();
        jLabelSubconta = new javax.swing.JLabel();
        jLabelQtdParc = new javax.swing.JLabel();
        jLabelFreq = new javax.swing.JLabel();
        jLabelDtEmissao = new javax.swing.JLabel();
        jtxtJuros = new javax.swing.JFormattedTextField();
        jtxtMulta = new javax.swing.JFormattedTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setForeground(java.awt.Color.white);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Contas a Pagar");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
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

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

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

        btnPagar.setBackground(new java.awt.Color(255, 255, 255));
        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-dinheiro-32.png"))); // NOI18N
        btnPagar.setText("Pagar");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        btnSimular.setBackground(new java.awt.Color(255, 255, 255));
        btnSimular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-gráfico-combinado-24.png"))); // NOI18N
        btnSimular.setText("Simular");
        btnSimular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimularActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/resources/logopp.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Contas a Pagar");

        btnNovo.setBackground(new java.awt.Color(255, 255, 255));
        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-arquivo-26.png"))); // NOI18N
        btnNovo.setText("Novo");
        btnNovo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNovoMouseClicked(evt);
            }
        });
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
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

        btnImprimir.setBackground(new java.awt.Color(255, 255, 255));
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-impressão-24.png"))); // NOI18N
        btnImprimir.setText("<html>Gerar<br>Relatório</html>");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        jTabProcurar.setBackground(new java.awt.Color(245, 255, 255));
        jTabProcurar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabProcurarStateChanged(evt);
            }
        });
        jTabProcurar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabProcurarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTabProcurarMouseEntered(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jScrollPane2FocusGained(evt);
            }
        });

        TabelaContasAPagar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TabelaContasAPagar.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nº Lançamento", "Nº da parcela", "Fornecedor", "Vencimento", "Banco", "Valor", "Histórico"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TabelaContasAPagar);

        txtTotalLancamentos.setEditable(false);
        txtTotalLancamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalLancamentosActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Nº Lançamentos");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Total a Pagar");

        jLabel2.setText("Fornecedor:");

        cbxFornecedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbxFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxFornecedorActionPerformed(evt);
            }
        });

        cbxOrdenar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Vencimento", "N°Lançamento" }));
        cbxOrdenar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOrdenarActionPerformed(evt);
            }
        });

        jLabel5.setText("Ordenar por:");

        jLabel3.setText("Nº do Lançamento:");

        txtNumLancamento.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtNumLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumLancamentoActionPerformed(evt);
            }
        });
        txtNumLancamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumLancamentoKeyPressed(evt);
            }
        });

        jLabel4.setText("Contas a Pagar entre:");

        jtxtDataInicio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtDataInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jtxtDataFim.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtDataFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

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

        jtxtTotalVencido.setEditable(false);
        jtxtTotalVencido.setBackground(new java.awt.Color(255, 204, 204));
        jtxtTotalVencido.setForeground(new java.awt.Color(255, 0, 0));
        jtxtTotalVencido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jtxtTotalVencido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtTotalVencidoActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("Total Vencido");

        jtxtTotalPagar.setEditable(false);
        jtxtTotalPagar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jtxtTotalPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtTotalPagarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(85, 85, 85)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNumLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jtxtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnFiltrar)
                                .addGap(18, 18, 18)
                                .addComponent(btnExibir))
                            .addComponent(jLabel4))
                        .addGap(0, 23, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalLancamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtTotalVencido, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnExibir, btnFiltrar});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtxtTotalPagar, txtTotalLancamentos});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExibir)
                    .addComponent(cbxFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar)
                    .addComponent(txtNumLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtTotalLancamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jtxtTotalVencido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jtxtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jtxtDataFim, jtxtDataInicio, txtNumLancamento});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jtxtTotalPagar, txtTotalLancamentos});

        jTabProcurar.addTab("Procurar", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtParcelas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtParcelas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtParcelasFocusLost(evt);
            }
        });
        txtParcelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParcelasActionPerformed(evt);
            }
        });

        jLabel7.setText("Frequência");

        cbxFrequencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Semanal", "Quinzenal", "Mensal", "Bimestral", "Anual" }));

        jLabel8.setText("Nº Lançamento");

        txtNumLancamento1.setEditable(false);
        txtNumLancamento1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtNumLancamento1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumLancamento1ActionPerformed(evt);
            }
        });

        jLabel11.setText("Data Emissão");

        jLabel12.setText("Fornecedor");

        jtxtDtEmissao.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtDtEmissao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDtEmissao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtDtEmissaoFocusLost(evt);
            }
        });

        cbxFornecedor1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbxFornecedor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxFornecedor1ActionPerformed(evt);
            }
        });

        jLabel13.setText("Qtd Parcelas");

        jPanelTituloParcela.setBackground(new java.awt.Color(255, 255, 255));
        jPanelTituloParcela.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Título Parcela"));

        jLabel22.setText("Data vencimento parcela");

        jtxtDtVencParcela2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtDtVencParcela2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtxtDtVencParcela2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtDtVencParcela2FocusLost(evt);
            }
        });

        jLabel23.setText("Valor parcela");

        jLabel24.setText("Nº da Parcela");

        cbxNumParcela2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxNumParcela2ItemStateChanged(evt);
            }
        });
        cbxNumParcela2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxNumParcela2MouseClicked(evt);
            }
        });
        cbxNumParcela2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNumParcela2ActionPerformed(evt);
            }
        });

        jLabel25.setText("Nosso Numero");

        txtCodBarras2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCodBarras2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodBarras2FocusLost(evt);
            }
        });

        jtxtValorParcela2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jtxtValorParcela2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jtxtValorParcela2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtValorParcela2FocusLost(evt);
            }
        });

        jLabelDtVenc.setForeground(new java.awt.Color(255, 0, 0));
        jLabelDtVenc.setText("*Campo Obrigatório");

        jLabelValorP.setForeground(new java.awt.Color(255, 0, 0));
        jLabelValorP.setText("*Campo Obrigatório");

        javax.swing.GroupLayout jPanelTituloParcelaLayout = new javax.swing.GroupLayout(jPanelTituloParcela);
        jPanelTituloParcela.setLayout(jPanelTituloParcelaLayout);
        jPanelTituloParcelaLayout.setHorizontalGroup(
            jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloParcelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodBarras2, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addGroup(jPanelTituloParcelaLayout.createSequentialGroup()
                        .addGroup(jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelTituloParcelaLayout.createSequentialGroup()
                                .addGroup(jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(jtxtDtVencParcela2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanelTituloParcelaLayout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addGap(67, 67, 67))
                                    .addGroup(jPanelTituloParcelaLayout.createSequentialGroup()
                                        .addComponent(jtxtValorParcela2)
                                        .addGap(34, 34, 34))))
                            .addGroup(jPanelTituloParcelaLayout.createSequentialGroup()
                                .addComponent(jLabelDtVenc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelValorP)
                                .addGap(34, 34, 34)))
                        .addGroup(jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(cbxNumParcela2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 20, Short.MAX_VALUE))
        );
        jPanelTituloParcelaLayout.setVerticalGroup(
            jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloParcelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtDtVencParcela2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxNumParcela2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtValorParcela2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTituloParcelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDtVenc)
                    .addComponent(jLabelValorP))
                .addGap(10, 10, 10)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodBarras2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel27.setText("Banco titular");

        cbxBancoTitular.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));

        jLabel28.setText("Juros % ao dia");

        jLabel29.setText("Multa");

        txtDiasProtesto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtDiasProtesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiasProtestoActionPerformed(evt);
            }
        });

        jLabel30.setText("Dias protesto");

        jLabel31.setText("Subconta");

        cbxSubconta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));

        txtHistorico.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, java.awt.Color.lightGray));
        txtHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHistoricoActionPerformed(evt);
            }
        });

        jLabel33.setText("Histórico");

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

        ckbContaCorrente.setText("Conta Recorrente");
        ckbContaCorrente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbContaCorrenteActionPerformed(evt);
            }
        });

        jLabelFornec.setForeground(new java.awt.Color(255, 0, 0));
        jLabelFornec.setText("*Campo Obrigatório");

        jLabelSubconta.setForeground(new java.awt.Color(255, 0, 0));
        jLabelSubconta.setText("*Campo Obrigatório");

        jLabelQtdParc.setForeground(new java.awt.Color(255, 0, 0));
        jLabelQtdParc.setText("*Campo Obrigatório");

        jLabelFreq.setForeground(new java.awt.Color(255, 0, 0));
        jLabelFreq.setText("*Campo Obrigatório");

        jLabelDtEmissao.setForeground(new java.awt.Color(255, 0, 0));
        jLabelDtEmissao.setText("*Campo Obrigatório");

        jtxtJuros.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jtxtJuros.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jtxtJuros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtJurosActionPerformed(evt);
            }
        });

        jtxtMulta.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jtxtMulta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jtxtMulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtMultaActionPerformed(evt);
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
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(cbxFrequencia, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel31)
                                        .addComponent(cbxSubconta, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(42, 42, 42)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cbxFornecedor1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbxBancoTitular, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel27)
                                        .addComponent(jLabel12)
                                        .addComponent(jLabelFornec))
                                    .addGap(63, 63, 63)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel13))
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(jLabelQtdParc)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabelFreq))
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                            .addComponent(txtDiasProtesto, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                            .addComponent(jLabel30)
                                                            .addGap(41, 41, 41)))
                                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                            .addComponent(jLabel28)
                                                            .addGap(18, 18, 18)
                                                            .addComponent(jLabel29))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                            .addComponent(jtxtJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                            .addComponent(jtxtMulta, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                            .addGap(5, 5, 5))))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(txtNumLancamento1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(3, 3, 3))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelTituloParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ckbContaCorrente, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelSubconta))
                                .addGap(42, 42, 42)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jtxtDtEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelDtEmissao))
                                .addGap(154, 154, 154)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33)
                                    .addComponent(txtHistorico))))
                        .addGap(227, 227, 227))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 692, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGravar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCancelar, btnGravar});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(jLabel12)
                                .addComponent(jLabel8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxFornecedor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumLancamento1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(cbxFrequencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelFornec)
                            .addComponent(jLabelQtdParc))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbxBancoTitular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxSubconta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel28)
                                            .addComponent(jLabel29))
                                        .addComponent(jLabel30))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel31)
                                        .addComponent(jLabel27)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtDiasProtesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabelFreq))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSubconta)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel33)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtDtEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDtEmissao))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(ckbContaCorrente)
                        .addGap(20, 20, 20)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTituloParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGravar)
                    .addComponent(btnCancelar))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnCancelar, btnGravar});

        jTabProcurar.addTab("Lançamentos", jPanel2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPagar, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(btnSimular, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSair, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(23, 23, 23)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTabProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 967, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 107, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPagar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSimular)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSair)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImprimir)))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnImprimir, btnNovo, btnPagar, btnSair, btnSimular});

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAlterar, btnExcluir});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirActionPerformed
         conexao.conectar();
         titulosParcela  = daoTituloParcela.listarTitulos("N");
        try {
            Exibir(titulosParcela);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
       conexao.fecharConexao();
        
    }//GEN-LAST:event_btnExibirActionPerformed

    private void txtNumLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumLancamentoActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        String src="C:/Users/Devair/Documents/NetBeansProjects/prjContasaPagar/"
                + "src/fatec/Relatorios/RelatorioContasAPagar.jasper";
        JasperPrint jasperPrint=null;
        try {
            jasperPrint=JasperFillManager.fillReport(src, null,new JRBeanCollectionDataSource(titulosParcela));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao Gerar Relatorio\n"+ex);
        }
        JasperViewer view =new JasperViewer(jasperPrint,false);
        view.setTitle("Relatório Contas a Pagar");
         URL url = this.getClass().getResource("/fatec/resources/logo2.png");  
         Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);  
         view.setIconImage(iconeTitulo);
          view.setVisible((true));
        
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        conexao = new Conexao();
        jTabProcurar.setSelectedIndex(1);
        txtParcelas.setEnabled(true);
        cbxFrequencia.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnGravar.setEnabled(true);
        Cadastrar();       
       
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
      
          if(TabelaContasAPagar.getSelectedRowCount()==0){
            
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Título a Excluir ", "Aviso de Exclusão",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        else
        {   
            int indiceLinha = TabelaContasAPagar.getSelectedRow();
            int cod=0,parc=0;
            cod=Integer.parseInt(""+TabelaContasAPagar.getValueAt(indiceLinha, 0));
            parc=Integer.parseInt(""+TabelaContasAPagar.getValueAt(indiceLinha, 1));
            
            passarParcela = new GuiExcluirTitulo();
            passarParcela.RecebeParcela(cod,parc);
            guiMenu.painelMenu.add(passarParcela);
            passarParcela.show();
            passarParcela.setPosicao();
            
            
            //guiMenu.setEnabled(false);
           
            
        }
      
        
        
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        conexao.fecharConexao();
        dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
       
        if(TabelaContasAPagar.getSelectedRowCount()==0){
            
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Título a Pagar ", "Aviso de Pagamento",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        else
        {   
            int indiceLinha = TabelaContasAPagar.getSelectedRow();
            int cod=0,parc=0;
            cod=Integer.parseInt(""+TabelaContasAPagar.getValueAt(indiceLinha, 0));
            parc=Integer.parseInt(""+TabelaContasAPagar.getValueAt(indiceLinha, 1));
            
            pagarParcela= new GuiPagarTitulo();
            pagarParcela.RecebeParcela(cod,parc);
            guiMenu.painelMenu.add(pagarParcela);
            pagarParcela.show();
            pagarParcela.setPosicao();
            //guiMenu.setEnabled(false);
           
            
        }
      
        
    }//GEN-LAST:event_btnPagarActionPerformed

    private void btnSimularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimularActionPerformed
        // TODO add your handling code here:
        GuiSimulacaoContasAPagar f1=new GuiSimulacaoContasAPagar();
           painelMenu.add(f1);
           f1.show();
           f1.setPosicao();
    }//GEN-LAST:event_btnSimularActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        
        conexao = new Conexao();
        daoFornecedor = new DaoFornecedor(conexao.conectar());
        daoFornecPF = new DaoFornecPF(conexao.conectar());
        daoFornecPJ = new DaoFornecPJ(conexao.conectar());
        daoTitulo =new DaoTitulo(conexao.conectar());
        daoTituloParcela=new DaoTituloParcela(conexao.conectar());
        daoSubconta=new DaoSubconta(conexao.conectar());
        daoBanco=new DaoBanco(conexao.conectar());
        daoRecorrente=new DaoRecorrente(conexao.conectar());
        admin=new Admin();
  
        cbxFornecedor();
        cbxFornecedor1();
        cbxSubconta();
        cbxBanco();
         titulosParcela = daoTituloParcela.listarTitulos("N");
        try {
            Exibir(titulosParcela);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        conexao.fecharConexao();
       
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtNumLancamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumLancamentoKeyPressed
        conexao.conectar();
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
          listaPorCodigo();
        }
        conexao.fecharConexao();
        
    }//GEN-LAST:event_txtNumLancamentoKeyPressed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        
        conexao.conectar();
        if(txtNumLancamento.getText().isEmpty() && 
                jtxtDataInicio.getText().replace("/","").replace(" ","").length()!=0 && 
                jtxtDataFim.getText().replace("/","").replace(" ","").length()!=0)
        {
            
            try {
                    Date dataInicio=Admin.formataData(jtxtDataInicio.getText());
                    Date dataFinal=Admin.formataData(jtxtDataFim.getText());

                    if(dataInicio==null || dataFinal==null){
                         JOptionPane.showMessageDialog(this, "Insira uma data válida!");

                    }
                    else{
                        if(dataInicio.compareTo(dataFinal)>0){
                            JOptionPane.showMessageDialog(this, "A data Final deve ser maior ou igual a data inicio");
                        }
                        else{

                            titulosParcela = daoTituloParcela.listarTitulosData(jtxtDataInicio.getText(),jtxtDataFim.getText(),"N");
                             Exibir(titulosParcela); 

                        }
                    }
    
                } 
                catch (ParseException ex) {
                    Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
                 }
     
        }
        else if(txtNumLancamento.getText().isEmpty()==false 
                && jtxtDataInicio.getText().replace("/","").replace(" ","").length()==0 && 
                jtxtDataFim.getText().replace("/","").replace(" ","").length()==0){
           listaPorCodigo();
            
        }
        else if(txtNumLancamento.getText().isEmpty() 
                && jtxtDataInicio.getText().replace("/","").replace(" ","").length()==0 &&
                jtxtDataFim.getText().replace("/","").replace(" ","").length()==0){
            JOptionPane.showMessageDialog(this, "Preencha um campo de filtro");
        }
        else{
            JOptionPane.showMessageDialog(this, "Apenas um campo de filtro deve ser preenchido");
        }
            
        
       
       txtNumLancamento.setText("");
       jtxtDataFim.setText("");
       jtxtDataInicio.setText("");
       conexao.fecharConexao();
      
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void cbxFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxFornecedorActionPerformed
       conexao.conectar();
        if(cbxFornecedor.getSelectedIndex()!=0)
        listaPorFornec();
        conexao.fecharConexao();
    }//GEN-LAST:event_cbxFornecedorActionPerformed

    private void cbxOrdenarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOrdenarActionPerformed
        // TODO add your handling code here:
        conexao.conectar();
        ArrayList<TituloParcela> titulosParcela;
           System.out.println(cbxOrdenar.getSelectedIndex());
        try {
            if(cbxOrdenar.getSelectedIndex()==1){
             
                titulosParcela = daoTituloParcela.listarTitulosOrder(1,"N");
                Exibir(titulosParcela);
            }        
            else if(cbxOrdenar.getSelectedIndex()==2){
                titulosParcela = daoTituloParcela.listarTitulosOrder(2,"N");
                Exibir(titulosParcela);
            }
        }
        catch (ParseException ex) {
                Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
        conexao.fecharConexao();
    }//GEN-LAST:event_cbxOrdenarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
          conexao = new Conexao();
         if(TabelaContasAPagar.getSelectedRowCount()==0){
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Título a ser Alterado ", "Aviso",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
         else{
             Consulta();
             HabilitaLbValidacao(false);
             HabilitaBotao(false);
             btnGravar.setEnabled(true);
             btnCancelar.setEnabled(true);
         }
        
        
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void jtxtTotalPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtTotalPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtTotalPagarActionPerformed

    private void txtTotalLancamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalLancamentosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalLancamentosActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        LimpaCampos();
        HabilitaBotao(true);
        titulosP=null;
        cbxNumParcela2.removeAllItems();
        jTabProcurar.setSelectedIndex(0);
        conexao.fecharConexao();
        
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHistoricoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHistoricoActionPerformed

    private void txtDiasProtestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiasProtestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiasProtestoActionPerformed

    private void txtNumLancamento1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumLancamento1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumLancamento1ActionPerformed

    private void txtParcelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParcelasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParcelasActionPerformed

    private void ckbContaCorrenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbContaCorrenteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ckbContaCorrenteActionPerformed

    private void txtParcelasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtParcelasFocusLost
        // TODO add your handling code here:
        if(daoTitulo.obterNumLancamento()<(Integer.parseInt(txtNumLancamento1.getText()))){
            
        if(txtParcelas.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Qtd de Parcelas é um campo obrigatório");
        }
        else if(Integer.parseInt(txtParcelas.getText())>0){
            TituloParcela i = null;
            ArrayList<TituloParcela>titulos=null;
            titulos = new ArrayList<TituloParcela>();
            titulosP=new ArrayList<TituloParcela>();
            for (int x=0; x<Integer.parseInt(txtParcelas.getText()); x++){
               
               i= new TituloParcela(Integer.parseInt(txtNumLancamento1.getText()));
               i.setNumParcela(x+1);
               System.out.println(i.getNumLancamento());
               System.out.println(i.getNumParcela());       
               titulos.add(i);
               
            } 
            
            titulosP=titulos;
            if(cbxNumParcela2.getItemCount()==0)
             cbxNumParcela2.addItem(String.valueOf(1));
             
           jPanelTituloParcela.setEnabled(true);
           HabilitarTituloParcela(true);
      }
        else
             JOptionPane.showMessageDialog(this, "Qtd de Parcelas não pode ser 0");
        }
    }//GEN-LAST:event_txtParcelasFocusLost

    private void jtxtDtVencParcela2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtDtVencParcela2FocusLost
           if(daoTitulo.obterNumLancamento()<Integer.parseInt(txtNumLancamento1.getText())){
                if(flag==0)
                {
                    try 
                    {
                        //ArrayList<TituloParcela> titulos=CriaParcelas();
                        Date data= Admin.formataData(jtxtDtVencParcela2.getText());
                        if(data==null){
                             JOptionPane.showMessageDialog(this, "Insira uma data válida!");
                        }
                        else
                        {   flag++;  
                            Calendar c = Calendar.getInstance();
                            c.setTime(data);
                            SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy");  
                            //String result = out.format(new Date());
                            if(cbxFrequencia.getSelectedIndex()!=0)
                            {

                                System.out.println("passei");
                                for(int x=0; x<Integer.parseInt(txtParcelas.getText());x++)
                                {
                                     if(x>0)
                                            cbxNumParcela2.addItem(String.valueOf(x+1));
                                    if(x>0){
                                        if( cbxFrequencia.getSelectedIndex()==3){
                                            c.add(Calendar.DATE, +30);

                                            }
                                        else if(cbxFrequencia.getSelectedIndex()==1){
                                            c.add(Calendar.DATE, +7);
                                        }
                                        else if(cbxFrequencia.getSelectedIndex()==2){
                                            c.add(Calendar.DATE, +15);
                                        }
                                        else if(cbxFrequencia.getSelectedIndex()==4){
                                            c.add(Calendar.DATE, +60);
                                        }
                                        else if(cbxFrequencia.getSelectedIndex()==5){
                                            c.add(Calendar.YEAR, +1);
                                        }
                                        data=c.getTime();
                                        System.out.println(data);
                                        titulosP.get(x).setDataVenc(formata.format(data));
                                    }
                                    else{
                                        System.out.println(data);
                                        titulosP.get(x).setDataVenc(formata.format(data));
                                    }

                                }

                            }

                        }


                    }

                    catch (ParseException ex) 
                    {
                        Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                else
                {
                    titulosP.get(cbxNumParcela2.getSelectedIndex()).setDataVenc(jtxtDtVencParcela2.getText());
                }
                System.out.println("entreeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
       }
           
           
           
    }//GEN-LAST:event_jtxtDtVencParcela2FocusLost

    private void cbxNumParcela2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNumParcela2ActionPerformed
    
     //jtxtValorParcela2.requestFocus();
        
    }//GEN-LAST:event_cbxNumParcela2ActionPerformed

    private void txtCodBarras2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodBarras2FocusLost
        if(daoTitulo.obterNumLancamento()<Integer.parseInt(txtNumLancamento1.getText()))
        titulosP.get(cbxNumParcela2.getSelectedIndex()).setNossoNum(txtCodBarras2.getText());
    }//GEN-LAST:event_txtCodBarras2FocusLost

    private void jtxtValorParcela2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtValorParcela2FocusLost
       if(daoTitulo.obterNumLancamento()<Integer.parseInt(txtNumLancamento1.getText())){
        if(flagVparcela==0){
            System.out.println("pasei no flag");
              for(int x=0; x<Integer.parseInt(txtParcelas.getText());x++){
                try {
                    titulosP.get(x).setValorParcela(converte(jtxtValorParcela2.getText()));
                   
                } catch (ParseException ex) {
                    Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
                }
              }
              if(titulosP.get(0).getDataVenc()!=null)
              flagVparcela++;     
        }
        else{
            try {
                titulosP.get(cbxNumParcela2.getSelectedIndex()).setValorParcela(converte(jtxtValorParcela2.getText()));
            } catch (ParseException ex) {
                Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
       }
    }//GEN-LAST:event_jtxtValorParcela2FocusLost

    private void btnGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGravarActionPerformed
        conexao.conectar();
        if(cbxFornecedor1.getSelectedIndex()==0){
            jLabelFornec.setVisible(true);
        }
        else
             jLabelFornec.setVisible(false);
        
        if(cbxSubconta.getSelectedIndex()==0){
            jLabelSubconta.setVisible(true);
        }
        else
            jLabelSubconta.setVisible(false);
            
        
         
         
         if(jtxtDtVencParcela2.getText().replace("/", "").replace(" ", "").isEmpty()){
             jLabelDtVenc.setVisible(true);
         }
         else
             jLabelDtVenc.setVisible(false);
         
          if(jtxtValorParcela2.getText().isEmpty()){
             jLabelValorP.setVisible(true);
         }
         else
             jLabelValorP.setVisible(false);
       
          if(jtxtDtEmissao.getText().replace("/", "").replace(" ", "").isEmpty()){
             jLabelDtEmissao.setVisible(true);
         }
         else
             jLabelDtEmissao.setVisible(false);
       
         
         if(daoTitulo.obterNumLancamento()<Integer.parseInt(txtNumLancamento1.getText())){
               if(txtParcelas.getText().isEmpty()){
                   System.out.println("passeicadastrar");
                   jLabelQtdParc.setVisible(true);
                 }
                else
                      jLabelQtdParc.setVisible(false);
                if(cbxFrequencia.getSelectedIndex()==0){
                    jLabelFreq.setVisible(true);
                }
        else
            jLabelFreq.setVisible(false);
            System.out.println("pseei no cadastrar");
             if(cbxFornecedor1.getSelectedIndex()==0||cbxSubconta.getSelectedIndex()==0|| cbxFrequencia.getSelectedIndex()==0
                 || txtParcelas.getText().isEmpty() || jtxtValorParcela2.getText().isEmpty() || jtxtDtVencParcela2.getText()
                         .replace("/", "").replace(" ", "").isEmpty()||jtxtDtEmissao.getText()
                         .replace("/", "").replace(" ", "").isEmpty()){
             JOptionPane.showMessageDialog(this, "Confira os campos Obrigatórios!!");
         }
         else{
                fornecedor=new Fornecedor(buscaCodFornec((String) cbxFornecedor1.getSelectedItem()));
                Object[] options = { "OK", "CANCELAR" };
                 tituloRecorrente=daoRecorrente.consultar(fornecedor.getCodFornec());
                if(ckbContaCorrente.isSelected()){
                    if(tituloRecorrente==null){
                        if(JOptionPane.showOptionDialog(null, "Clique em OK para confirmar a inclusao do Fornecedor : "+ 
                                buscaFornec(fornecedor.getCodFornec()) +" como Titulo Recorrente",
                                "Aviso Titulo Recorrente",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, options, options[0])==0) {
                                tituloRecorrente=new  TituloRecorrente(fornecedor);
                                tituloRecorrente.setSubconta(daoSubconta.consultarNome((String) cbxSubconta.getSelectedItem()));

                                tituloRecorrente.setBanco(daoBanco.consultarNome((String) cbxBancoTitular.getSelectedItem()));
                                tituloRecorrente.setQtdParcelas(Integer.parseInt(txtParcelas.getText()));
                                try {
                                    tituloRecorrente.setJuros(converte(jtxtJuros.getText()));
                                    tituloRecorrente.setMulta(converte(jtxtMulta.getText()));
                                } catch (ParseException ex) {
                                    Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                tituloRecorrente.setHistorico(txtHistorico.getText());
                                tituloRecorrente.setDiasProtesto(Integer.parseInt(txtDiasProtesto.getText()));
                                tituloRecorrente.setFrequencia(cbxFrequencia.getSelectedIndex());
                                daoRecorrente.inserir(tituloRecorrente);
                        }
                    }
                    
                        
                }
                else if (ckbContaCorrente.isSelected()==false){
                    System.out.println("passei no chek do recorrente");
                    if(tituloRecorrente!=null){
                        if(JOptionPane.showOptionDialog(null, "Clique em OK para confirmar a exclusao do Fornecedor : "+ 
                                buscaFornec(fornecedor.getCodFornec()) +" como Titulo Recorrente",
                                "Aviso Titulo Recorrente",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, options, options[0])==0) {
                                daoRecorrente.excluir(tituloRecorrente);
                        }
                     }
                }
                titulo=new Titulo(Integer.parseInt(txtNumLancamento1.getText()));
                
                titulo.setFornecedor(fornecedor);
                titulo.setSubconta(daoSubconta.consultarNome((String) cbxSubconta.getSelectedItem()));
                if(cbxBancoTitular.getSelectedIndex()!=0)
                titulo.setBanco(daoBanco.consultarNome((String) cbxBancoTitular.getSelectedItem()));
                titulo.setDtEmissao(jtxtDtEmissao.getText());
                titulo.setQtdParcela(Integer.parseInt(txtParcelas.getText()));
            try {
                if(jtxtJuros.getText().isEmpty()==false)
                titulo.setJuros(converte(jtxtJuros.getText()));
                if(jtxtMulta.getText().isEmpty()==false)
                titulo.setMulta(converte(jtxtMulta.getText()));
            } catch (ParseException ex) {
                Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                if(txtDiasProtesto.getText().isEmpty()==false)
                titulo.setNdProtesto(Integer.parseInt(txtDiasProtesto.getText()));
               
             daoTitulo.inserir(titulo);
             for(int x=0;x<titulo.getQtdParcela();x++){
                 titulosP.get(x).setStatusPag("N");
                daoTituloParcela.inserir(titulosP.get(x));
             }
             JOptionPane.showMessageDialog(this, "Titulo Cadastrado com Sucesso!!");
             
             Cadastrar();
             titulosP=null;
             cbxNumParcela2.removeAllItems();
             flag=0;
            }
             
             
         }
         else{
             
             if(cbxFornecedor1.getSelectedIndex()==0||cbxSubconta.getSelectedIndex()==0
                 || jtxtValorParcela2.getText().isEmpty() || jtxtDtVencParcela2.getText()
                         .replace("/", "").replace(" ", "").isEmpty()||jtxtDtEmissao.getText()
                         .replace("/", "").replace(" ", "").isEmpty()){
             JOptionPane.showMessageDialog(this, "Confira os campos Obrigatórios!!");
         }
         else{
                fornecedor=new Fornecedor(buscaCodFornec((String) cbxFornecedor1.getSelectedItem()));
                Object[] options = { "OK", "CANCELAR" };
                 tituloRecorrente=daoRecorrente.consultar(fornecedor.getCodFornec());
                if(ckbContaCorrente.isSelected()){
                    if(tituloRecorrente==null){
                        if(JOptionPane.showOptionDialog(null, "Clique em OK para confirmar a inclusao do Fornecedor : "+ 
                                buscaFornec(fornecedor.getCodFornec()) +" como Titulo Recorrente",
                                "Aviso Titulo Recorrente",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, options, options[0])==0) {
                                tituloRecorrente=new  TituloRecorrente(fornecedor);
                                tituloRecorrente.setSubconta(daoSubconta.consultarNome((String) cbxSubconta.getSelectedItem()));
                                tituloRecorrente.setBanco(daoBanco.consultarNome((String) cbxBancoTitular.getSelectedItem()));
                                tituloRecorrente.setQtdParcelas(Integer.parseInt(txtParcelas.getText()));
                                try {
                                    tituloRecorrente.setJuros(converte(jtxtJuros.getText()));
                                    tituloRecorrente.setMulta(converte(jtxtMulta.getText()));
                                } catch (ParseException ex) {
                                    Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                tituloRecorrente.setHistorico(txtHistorico.getText());
                                tituloRecorrente.setDiasProtesto(Integer.parseInt(txtDiasProtesto.getText()));
                                tituloRecorrente.setFrequencia(cbxFrequencia.getSelectedIndex());
                                daoRecorrente.inserir(tituloRecorrente);
                        }
                    }
                    
                        
                }
                else if (ckbContaCorrente.isSelected()==false){
                    System.out.println("passei no chek do recorrente");
                    if(tituloRecorrente!=null){
                        if(JOptionPane.showOptionDialog(null, "Clique em OK para confirmar a exclusao do Fornecedor : "+ 
                                buscaFornec(fornecedor.getCodFornec()) +" como Titulo Recorrente",
                                "Aviso Titulo Recorrente",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, options, options[0])==0) {
                                daoRecorrente.excluir(tituloRecorrente);
                        }
                     }
                }
               
                titulo.setFornecedor(fornecedor);
                titulo.setSubconta(daoSubconta.consultarNome((String) cbxSubconta.getSelectedItem()));
                titulo.setBanco(daoBanco.consultarNome((String) cbxBancoTitular.getSelectedItem()));
                titulo.setDtEmissao(jtxtDtEmissao.getText());
                
            try {
                titulo.setJuros(converte(jtxtJuros.getText()));
                titulo.setMulta(converte(jtxtMulta.getText()));
            } catch (ParseException ex) {
                Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                
                titulo.setNdProtesto(Integer.parseInt(txtDiasProtesto.getText()));
                titulo.setHistorico(txtHistorico.getText());
               
             daoTitulo.alterar(titulo);
             tituloParcela.setNumLancamento(titulo.getNumLancamento());
             tituloParcela.setNumParcela(Integer.parseInt((String) cbxNumParcela2.getSelectedItem()) );
             tituloParcela.setDataVenc(jtxtDtVencParcela2.getText());
                 try {
                     tituloParcela.setValorParcela(converte(jtxtValorParcela2.getText()));
                 } catch (ParseException ex) {
                     Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
                 }
             tituloParcela.setNossoNum(txtCodBarras2.getText());
             daoTituloParcela.alterar(tituloParcela);
             JOptionPane.showMessageDialog(this, "Titulo Alterado com Sucesso!!");
             
             LimpaCampos();
             HabilitaBotao(true);
             jTabProcurar.setSelectedIndex(0);
             titulosP=null;
             cbxNumParcela2.removeAllItems();
            
            }
             
         }
           titulosParcela = daoTituloParcela.listarTitulosOrder(1,"N");
        try {
            Exibir(titulosParcela);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
         
     conexao.fecharConexao();
    }//GEN-LAST:event_btnGravarActionPerformed

    private void cbxFornecedor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxFornecedor1ActionPerformed
        // TODO add your handling code here:
        if(cbxFornecedor1.getSelectedIndex()>0){
              
            fornecedor=new Fornecedor(buscaCodFornec((String) cbxFornecedor1.getSelectedItem()));
            tituloRecorrente=daoRecorrente.consultar(fornecedor.getCodFornec());
            if(tituloRecorrente!=null){
               cbxSubconta.setSelectedItem(tituloRecorrente.getSubconta().getNome());
               cbxBancoTitular.setSelectedItem(tituloRecorrente.getBanco().getNome());
               txtParcelas.setText(String.valueOf(tituloRecorrente.getQtdParcelas()));
               txtDiasProtesto.setText(String.valueOf(tituloRecorrente.getDiasProtesto()));
               jtxtJuros.setText(String.valueOf(tituloRecorrente.getJuros()));
               jtxtMulta.setText(String.valueOf(tituloRecorrente.getMulta()));
               txtHistorico.setText(tituloRecorrente.getHistorico());
               cbxFrequencia.setSelectedIndex(tituloRecorrente.getFrequencia());
               ckbContaCorrente.setSelected(true);
                HabilitarTituloParcela(true);
                txtParcelas.requestFocus();
            }
        }
            
      
        
    }//GEN-LAST:event_cbxFornecedor1ActionPerformed

    private void jtxtJurosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtJurosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtJurosActionPerformed

    private void jtxtMultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtMultaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtMultaActionPerformed

    private void jTabProcurarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabProcurarMouseClicked
        // TODO add your handling code here:
       
         
    }//GEN-LAST:event_jTabProcurarMouseClicked

    private void btnNovoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNovoMouseClicked
        // TODO add your handling code here:
     
    }//GEN-LAST:event_btnNovoMouseClicked

    private void jTabProcurarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabProcurarStateChanged
        // TODO add your handling code here:
       if(jTabProcurar.getSelectedIndex()==1 && btnNovo.hasFocus()==false){
          if(TabelaContasAPagar.getSelectedRowCount()==0){
            jTabProcurar.setSelectedIndex(0);
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Título a consultar ", "Aviso",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        else
        {
            Consulta();
            HabilitaLbValidacao(false);
            btnGravar.setEnabled(false);
            btnCancelar.setEnabled(false);
            
        }
      }
    }//GEN-LAST:event_jTabProcurarStateChanged

    private void jtxtTotalVencidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtTotalVencidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtTotalVencidoActionPerformed

    private void jTabProcurarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabProcurarMouseEntered
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTabProcurarMouseEntered

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        AtualizaTitulos();
    }//GEN-LAST:event_formFocusGained

    private void jScrollPane2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jScrollPane2FocusGained
        // TODO add your handling code here:
        AtualizaTitulos();
    }//GEN-LAST:event_jScrollPane2FocusGained

    private void jtxtDtEmissaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtDtEmissaoFocusLost
        // TODO add your handling code here:
        Date data;
        try {
            data = Admin.formataData(jtxtDtEmissao.getText());
             if(data==null){
                JOptionPane.showMessageDialog(this, "Insira uma data válida!");
              }
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jtxtDtEmissaoFocusLost

    private void cbxNumParcela2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxNumParcela2MouseClicked
      
       
    }//GEN-LAST:event_cbxNumParcela2MouseClicked

    private void cbxNumParcela2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxNumParcela2ItemStateChanged
          if(daoTitulo.obterNumLancamento()<Integer.parseInt(txtNumLancamento1.getText())){
           AtualizaParcela();
      }
    }//GEN-LAST:event_cbxNumParcela2ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelaContasAPagar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    public javax.swing.JButton btnExibir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnGravar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnSimular;
    private javax.swing.JComboBox<String> cbxBancoTitular;
    private javax.swing.JComboBox<String> cbxFornecedor;
    private javax.swing.JComboBox<String> cbxFornecedor1;
    private javax.swing.JComboBox<String> cbxFrequencia;
    private javax.swing.JComboBox<String> cbxNumParcela2;
    private javax.swing.JComboBox<String> cbxOrdenar;
    private javax.swing.JComboBox<String> cbxSubconta;
    private javax.swing.JCheckBox ckbContaCorrente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDtEmissao;
    private javax.swing.JLabel jLabelDtVenc;
    private javax.swing.JLabel jLabelFornec;
    private javax.swing.JLabel jLabelFreq;
    private javax.swing.JLabel jLabelQtdParc;
    private javax.swing.JLabel jLabelSubconta;
    private javax.swing.JLabel jLabelValorP;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelTituloParcela;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabProcurar;
    private javax.swing.JFormattedTextField jtxtDataFim;
    private javax.swing.JFormattedTextField jtxtDataInicio;
    private javax.swing.JFormattedTextField jtxtDtEmissao;
    private javax.swing.JFormattedTextField jtxtDtVencParcela2;
    private javax.swing.JFormattedTextField jtxtJuros;
    private javax.swing.JFormattedTextField jtxtMulta;
    private javax.swing.JFormattedTextField jtxtTotalPagar;
    private javax.swing.JFormattedTextField jtxtTotalVencido;
    private javax.swing.JFormattedTextField jtxtValorParcela2;
    private javax.swing.JTextField txtCodBarras2;
    private javax.swing.JTextField txtDiasProtesto;
    private javax.swing.JTextField txtHistorico;
    private javax.swing.JTextField txtNumLancamento;
    private javax.swing.JTextField txtNumLancamento1;
    private javax.swing.JTextField txtParcelas;
    private javax.swing.JTextField txtTotalLancamentos;
    // End of variables declaration//GEN-END:variables
    private Conexao conexao=null;
    private Titulo titulo=null;
    private TituloParcela tituloParcela=null;
    private FornecFisica fornecFisica =null;
    private FornecJuridica fornecJuridica=null;
    private DaoFornecPF daoFornecPF=null;
    private DaoFornecPJ daoFornecPJ=null;
    private DaoFornecedor daoFornecedor=null;
    private Fornecedor fornecedor=null;
    private DaoTitulo daoTitulo=null;
    private DaoTituloParcela daoTituloParcela=null;
    private DaoBanco daoBanco=null;
    private Banco banco=null;
    private DaoSubconta  daoSubconta=null;
    private Subconta subconta=null;
    public int flag=0;
    public int flagVparcela=0;
    private Admin admin=null;
    private TituloRecorrente tituloRecorrente=null;
    private DaoRecorrente daoRecorrente=null;
    private GuiMenu guiMenu;
    private ArrayList<TituloParcela> titulosParcela;
}
