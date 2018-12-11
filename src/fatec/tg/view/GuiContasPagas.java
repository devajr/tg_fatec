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
import fatec.tg.controle.DaoTpPagamento;
import fatec.tg.model.ArquivoRemessa;
import fatec.tg.model.Banco;
import fatec.tg.model.FornecFisica;
import fatec.tg.model.FornecJuridica;
import fatec.tg.model.Fornecedor;
import fatec.tg.model.Subconta;
import fatec.tg.model.TipoPagamento;
import fatec.tg.model.Titulo;
import fatec.tg.model.TituloParcela;
import fatec.tg.model.TituloRecorrente;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultRowSorter;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Bia Delmont
 */
public class GuiContasPagas extends javax.swing.JInternalFrame {
    private static GuiContasPagas guiContasPagas;
    public static GuiContasPagas getInstancia(){
        if(guiContasPagas==null){
            guiContasPagas = new GuiContasPagas();
           
        }
        return guiContasPagas;
    }
    /**
     * Creates new form GuiContasPagass
     */
    public GuiContasPagas() {
        initComponents();
      //  this .setPosicao();
    }
    public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);   
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
     public int buscaCodTipoPag(String nome){
        tipoPagamento=daoTpPagamento.consultarDescricao(nome);
        return (tipoPagamento.getCodigo());
     }
     public DefaultTableModel montaTabela(){
      DefaultTableModel modelo = new DefaultTableModel();
      
            modelo.addColumn("N° Lançamento");
            modelo.addColumn("N° Parcela");
            modelo.addColumn("Forncecedor");
            modelo.addColumn("Vencimento");
            modelo.addColumn("Data Pagamento");
            modelo.addColumn("Banco");
            modelo.addColumn("Valor");
            modelo.addColumn("Juros");
            modelo.addColumn("Multa");
            modelo.addColumn("Valor Pago");
            modelo.addColumn("Tipo Pagamento");
            modelo.addColumn("N° cheque");
            modelo.addColumn("Transf Bancaria");

     return (modelo);
 }
 public void Exibir(ArrayList<TituloParcela>titulosParcela) throws ParseException{
     conexao = new Conexao();
     titulosParcelaRelatorio=null;
     titulosParcelaRelatorio = new ArrayList<TituloParcela>();
     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
     int cont=0;
     double total=0.0;
     double totalVencido=0;
     double totalJuros=0;
     double totalMulta=0;
     double totalPago=0;
        DefaultTableModel modelo=montaTabela();
        //TabelaContasPagas.setRowSorter(new table);
        if(titulosParcela.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhum Titulo encontrado");

        }else{
            
          for(int i =0; i< titulosParcela.size(); i++){
              double juros=0,multa=0;
              double totalParcela=0;
              Date datavenc = Admin.formataData((String)titulosParcela.get(i).getDataVenc());
              Date dataPag = Admin.formataData((String)titulosParcela.get(i).getDataPag());
               Calendar a = Calendar.getInstance();
               a.setTime(dataPag);//data maior
               Calendar b = Calendar.getInstance();
               b.setTime(datavenc);// data menor
               if(dataPag.after(datavenc)){
                   a.add(Calendar.DATE, - b.get(Calendar.DAY_OF_MONTH));
                   System.out.println(a.get(Calendar.DAY_OF_MONTH));
                   juros=a.get(Calendar.DAY_OF_MONTH)*titulosParcela.get(i).getJuros();
                   multa= titulosParcela.get(i).getMulta();
                   
                }
               else{
                    multa=0;
                    juros=0;
               }
                totalParcela=juros+multa+titulosParcela.get(i).getValorParcela();
                modelo.addRow(new Object[]{String.valueOf(titulosParcela.get(i).getNumLancamento()),titulosParcela.get(i).getNumParcela()
                        ,titulosParcela.get(i).getFornecFisica()==null? titulosParcela.get(i).getFornecJuridica().getNomeFan(): titulosParcela.get(i).getFornecFisica().getNome(),
                        titulosParcela.get(i).getDataVenc(), titulosParcela.get(i).getDataPag(),titulosParcela.get(i).getBanco().getNome(),
                        String.valueOf(titulosParcela.get(i).getValorParcela()),String.valueOf(juros),String.valueOf(multa),String.valueOf(totalParcela),
                        titulosParcela.get(i).getTipoPagamento().getDescricao()
                        ,titulosParcela.get(i).getNumCheque(),titulosParcela.get(i).getTransfBanc()});
            
                cont++;
              total=total+titulosParcela.get(i).getValorParcela();
              totalJuros=totalJuros+juros;
              totalMulta=totalMulta+multa;
              totalPago=totalPago+totalParcela;
              
                        TituloParcela t=null;
                        t = new TituloParcela(titulosParcela.get(i).getNumLancamento());
                        t.setNumParcela(titulosParcela.get(i).getNumParcela());
                        t.setValorParcela(titulosParcela.get(i).getValorParcela());
                        t.setValorPago(totalParcela);
                        t.setDataVenc(titulosParcela.get(i).getDataVenc());
                        t.setDataPag(titulosParcela.get(i).getDataPag());
                        t.setNumCheque(titulosParcela.get(i).getNumCheque());
                        t.setTransfBanc(titulosParcela.get(i).getTransfBanc());
                        t.setTipoPagamento(titulosParcela.get(i).getTipoPagamento());               
                        t.setFornecedor(titulosParcela.get(i).getFornecedor());  
                        t.setFornecFisica(titulosParcela.get(i).getFornecFisica());
                        t.setFornecJuridica(titulosParcela.get(i).getFornecJuridica());
                        t.setJuros(juros);                        
                        t.setMulta(multa);                       
                        t.setBanco(titulosParcela.get(i).getBanco());                       
                        t.setSubconta(titulosParcela.get(i).getSubconta());                       

              titulosParcelaRelatorio.add(t);
              
            }
        }
        
           TabelaContasPagas.setModel(modelo);
           TableRowSorter<TableModel> sorter =new TableRowSorter<TableModel>(modelo);
           sorter.setComparator(3, comparator);
           sorter.setComparator(4, comparator);
           sorter.setComparator(6, comparatorDouble);
           sorter.setComparator(7, comparatorDouble);
           sorter.setComparator(8, comparatorDouble);
           sorter.setComparator(9, comparatorDouble);
           TabelaContasPagas.setRowSorter(sorter);
         
         
         TabelaContasPagas.setDefaultEditor(Object.class, null);
         txtTotalLancamentos.setText(String.valueOf(cont));
         txtTotalTitulos.setText(String.format("%.2f",total).replace(".",","));
         txtTotalJuros.setText(String.format("%.2f",totalJuros).replace(".",","));
         txtTotalMultas.setText(String.format("%.2f",totalMulta).replace(".",","));
         txtTotalPago.setText(String.format("%.2f",totalPago).replace(".",","));
         
        
         conexao.fecharConexao();
 }
 public void listaPorCodigo(){
     titulosParcela  = daoTituloParcela.listarTitulosCod(Integer.parseInt(txtNumLancamento.getText()),"P");
            try {
                Exibir(titulosParcela);
                txtNumLancamento.setText("");
            } catch (ParseException ex) {
                Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
 }
 public void listaPorFornec(){

        
        try {
            
            titulosParcela = daoTituloParcela.listarTitulosFornec(buscaCodFornec((String) cbxFornecedor.getSelectedItem()),"P");
            Exibir(titulosParcela);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        PainelProcura3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TabelaContasPagas = new javax.swing.JTable();
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
        jLabel12 = new javax.swing.JLabel();
        txtTotalLancamentos = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTotalTitulos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTotalJuros = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTotalMultas = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTotalPago = new javax.swing.JTextField();
        btnImprimir = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
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

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Contas Pagas");

        PainelProcura3.setBackground(new java.awt.Color(255, 255, 255));
        PainelProcura3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Procura"));

        TabelaContasPagas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nº Lanc", "Nº Parcela", "Fornecedor", "Venc.", "Data Pagamento", "Banco", "Valor", "Juros", "Multa", "Valor Pago", "Tipo Pagamento", "N° Cheque", "Transf Bancaria"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(TabelaContasPagas);
        if (TabelaContasPagas.getColumnModel().getColumnCount() > 0) {
            TabelaContasPagas.getColumnModel().getColumn(0).setMaxWidth(80);
            TabelaContasPagas.getColumnModel().getColumn(1).setMaxWidth(80);
        }

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

        jLabel4.setText("Contas Pagas entre:");

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

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Nº Lançamentos");

        txtTotalLancamentos.setEditable(false);
        txtTotalLancamentos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtTotalLancamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalLancamentosActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Total Titulos: ");

        txtTotalTitulos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Total Juros ");

        txtTotalJuros.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtTotalJuros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalJurosActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Total Multas:");

        txtTotalMultas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtTotalMultas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalMultasActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Total Pago");

        txtTotalPago.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout PainelProcura3Layout = new javax.swing.GroupLayout(PainelProcura3);
        PainelProcura3.setLayout(PainelProcura3Layout);
        PainelProcura3Layout.setHorizontalGroup(
            PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelProcura3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PainelProcura3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalLancamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalMultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PainelProcura3Layout.createSequentialGroup()
                        .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PainelProcura3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(85, 85, 85)
                                .addComponent(jLabel3))
                            .addGroup(PainelProcura3Layout.createSequentialGroup()
                                .addComponent(cbxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNumLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PainelProcura3Layout.createSequentialGroup()
                                .addComponent(jtxtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnFiltrar)
                                .addGap(18, 18, 18)
                                .addComponent(btnExibir))
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        PainelProcura3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtTotalJuros, txtTotalLancamentos, txtTotalMultas, txtTotalPago, txtTotalTitulos});

        PainelProcura3Layout.setVerticalGroup(
            PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelProcura3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExibir)
                    .addComponent(cbxFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar)
                    .addComponent(txtNumLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(txtTotalLancamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PainelProcura3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel11)
                        .addComponent(jLabel10)
                        .addComponent(jLabel7)
                        .addComponent(txtTotalPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTotalMultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTotalJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTotalTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        btnImprimir.setBackground(new java.awt.Color(255, 255, 255));
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-impressão-26.png"))); // NOI18N
        btnImprimir.setText("<html>Gerar<br>Relatório</html>");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
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

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/resources/logopp.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(862, Short.MAX_VALUE))
                    .addComponent(PainelProcura3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnImprimir, btnSair});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PainelProcura3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSair, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnImprimir, btnSair});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        String src="C:/Users/Devair/Documents/NetBeansProjects/prjContasaPagar/"
                + "src/fatec/Relatorios/RelatorioContasPagasr.jasper";
        JasperPrint jasperPrint=null;
        try {
            jasperPrint=JasperFillManager.fillReport(src, null,new JRBeanCollectionDataSource(titulosParcelaRelatorio));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao Gerar Relatorio\n"+ex);
        }
        JasperViewer view =new JasperViewer(jasperPrint,false);
        view.setTitle("Relatório Contas Pagas");
         URL url = this.getClass().getResource("/fatec/resources/logo2.png");  
         Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);  
         view.setIconImage(iconeTitulo);
          view.setVisible((true));
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
       dispose();
       conexao.fecharConexao();
    }//GEN-LAST:event_btnSairActionPerformed

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

                titulosParcela = daoTituloParcela.listarTitulosOrder(1,"P");
                Exibir(titulosParcela);
            }
            else if(cbxOrdenar.getSelectedIndex()==2){
                titulosParcela = daoTituloParcela.listarTitulosOrder(2,"P");
                Exibir(titulosParcela);
            }
        }
        catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        conexao.fecharConexao();
    }//GEN-LAST:event_cbxOrdenarActionPerformed

    private void txtNumLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumLancamentoActionPerformed

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

                        titulosParcela = daoTituloParcela.listarTitulosData(jtxtDataInicio.getText(),jtxtDataFim.getText(),"P");
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
       

    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnExibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirActionPerformed
        conexao.conectar();
        titulosParcela  = daoTituloParcela.listarTitulos("P");
        try {
            Exibir(titulosParcela);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        conexao.fecharConexao();

    }//GEN-LAST:event_btnExibirActionPerformed

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
        daoTpPagamento=new DaoTpPagamento(conexao.conectar());
        admin=new Admin();
  
        cbxFornecedor();
       
            
        
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtTotalLancamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalLancamentosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalLancamentosActionPerformed

    private void txtTotalJurosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalJurosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalJurosActionPerformed

    private void txtTotalMultasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalMultasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalMultasActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GuiContasPagas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiContasPagas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiContasPagas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiContasPagas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiContasPagas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PainelProcura3;
    private javax.swing.JTable TabelaContasPagas;
    public javax.swing.JButton btnExibir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnSair;
    private javax.swing.JComboBox<String> cbxFornecedor;
    private javax.swing.JComboBox<String> cbxOrdenar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JFormattedTextField jtxtDataFim;
    private javax.swing.JFormattedTextField jtxtDataInicio;
    private javax.swing.JTextField txtNumLancamento;
    private javax.swing.JTextField txtTotalJuros;
    private javax.swing.JTextField txtTotalLancamentos;
    private javax.swing.JTextField txtTotalMultas;
    private javax.swing.JTextField txtTotalPago;
    private javax.swing.JTextField txtTotalTitulos;
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
    private ArrayList<TituloParcela> titulosParcelaRelatorio;
    private DaoTpPagamento daoTpPagamento=null;
    private TipoPagamento tipoPagamento=null;
}
