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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Bia Delmont
 */
public class GuiSimulacaoContasAPagar extends javax.swing.JInternalFrame {

  private static GuiSimulacaoContasAPagar guiSimulacaoContasAPagar;

    public GuiSimulacaoContasAPagar() {
        initComponents();
    }
      public  static GuiSimulacaoContasAPagar getInstancia(){
      if(guiSimulacaoContasAPagar== null){
          guiSimulacaoContasAPagar= new GuiSimulacaoContasAPagar();
      }
      return guiSimulacaoContasAPagar;
  }
     public DefaultTableModel montaTabela(){
      DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("N° Lançamento");
            modelo.addColumn("N° Parcela");
            modelo.addColumn("Forncecedor");
            modelo.addColumn("Vencimento");
            modelo.addColumn("Banco");
            modelo.addColumn("Valor");
            modelo.addColumn("Juros");
            modelo.addColumn("Multa");
            modelo.addColumn("Dias Protesto");
            modelo.addColumn("Valor Total");
            modelo.addColumn("Status");
            modelo.addColumn("Historico");

     return (modelo);
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
     public void setPosicao(){
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 2);   
    }

      public String buscaNomeSubconta(int cod){
      subconta=daoSubconta.consultar(cod);
      return(subconta.getNome());
  }
    public String buscaNomeBanco(int cod){
      banco=daoBanco.consultar(cod);
      return(banco.getNome());
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
                        ,buscaFornec(titulosParcela.get(i).getFornecedor().getCodFornec()),
                        titulosParcela.get(i).getDataVenc(),titulosParcela.get(i).getBanco()==null? " " :titulosParcela.get(i).getBanco().getNome() ,
                        titulosParcela.get(i).getValorParcela(),"a calcular","a calcular",titulosParcela.get(i).getNdProtesto(),titulosParcela.get(i).getHistorico()});
            
                cont++;
              total=total+titulosParcela.get(i).getValorParcela();
              Date datavenc = Admin.formataData((String)titulosParcela.get(i).getDataVenc());
              if(datavenc.before(hoje)){
                  totalVencido=totalVencido+titulosParcela.get(i).getValorParcela();
              }
             //jtxtTotalVencido.setText(String.valueOf(totalVencido));
            }
        }
         TabelaSimulacao1.setModel(modelo);
         TabelaSimulacao1.setDefaultRenderer(Object.class,renderer);
         TabelaSimulacao1.setDefaultEditor(Object.class, null);
         txtTotalTitulos.setText(String.format("%.2f",total).replace(".",","));
         txtTotalLancamentos.setText(String.valueOf(cont));
         System.out.println(String.valueOf(total));
         conexao.fecharConexao();
         txtTotalJuros.setText("");
         txtTotalMultas.setText("");
         txtTotalSimulado.setText("");
 }
  public void ExibirSimulacao(ArrayList<TituloParcela>titulosParcela,Date dtPag) throws ParseException{
     conexao = new Conexao();
     titulosParcelaRelatorio=null;
     titulosParcelaRelatorio = new ArrayList<TituloParcela>();
     //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
     LocalDate hoje = LocalDate.now();
     int cont=0;
     double total=0.0;
     double totalVencido=0;
     double totalJuros=0;
     double totalMulta=0;
     double totalSimulado=0;
    
        DefaultTableModel modelo=montaTabela();
        if(titulosParcela.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhum Titulo encontrado");

        }else{
            
          for(int i =0; i< titulosParcela.size(); i++){
               double juros=0;
               double multa=0;
               double totalParcela=0;
               String status="Não Protestado";
               Date datavenc = Admin.formataData((String)titulosParcela.get(i).getDataVenc());
             
               Calendar a = Calendar.getInstance();
               a.setTime(dtPag);//data maior
               Calendar b = Calendar.getInstance();
               b.setTime(datavenc);// data menor
               if(dtPag.after(datavenc)){
                   a.add(Calendar.DATE, - b.get(Calendar.DAY_OF_MONTH));
                   System.out.println(a.get(Calendar.DAY_OF_MONTH));
                   juros=a.get(Calendar.DAY_OF_MONTH)*titulosParcela.get(i).getJuros();
                   multa= titulosParcela.get(i).getMulta();
                   if(titulosParcela.get(i).getNdProtesto()>0 && titulosParcela.get(i).getNdProtesto()<a.get(Calendar.DAY_OF_MONTH)){
                   status="Protestado";
               }
                }
               else{
                    multa=0;
                    juros=0;
               }
               
               
                totalParcela=juros+multa+titulosParcela.get(i).getValorParcela();
                   
               
                modelo.addRow(new Object[]{String.valueOf(titulosParcela.get(i).getNumLancamento()),titulosParcela.get(i).getNumParcela()
                        ,buscaFornec(titulosParcela.get(i).getFornecedor().getCodFornec()),
                        titulosParcela.get(i).getDataVenc(),titulosParcela.get(i).getBanco()== null? " ":titulosParcela.get(i).getBanco().getNome(),
                        titulosParcela.get(i).getValorParcela(),juros,multa,titulosParcela.get(i).getNdProtesto(),totalParcela,status,titulosParcela.get(i).getHistorico()});
            
                cont++;
              total=total+titulosParcela.get(i).getValorParcela();
              totalJuros=totalJuros+juros;
              totalMulta=totalMulta+multa;
              totalSimulado=totalSimulado+totalParcela;
              TituloParcela t=null;
                        t = new TituloParcela(titulosParcela.get(i).getNumLancamento());
                        t.setNumParcela(titulosParcela.get(i).getNumParcela());
                        t.setValorParcela(titulosParcela.get(i).getValorParcela());
                        t.setValorPago(totalParcela);
                        t.setDataVenc(titulosParcela.get(i).getDataVenc());
                        t.setDataPag(hoje.format(f));
                        t.setNdProtesto(titulosParcela.get(i).getNdProtesto());
                        t.setStatusPag(status);
                        t.setFornecedor(titulosParcela.get(i).getFornecedor());  
                        t.setFornecFisica(titulosParcela.get(i).getFornecFisica());
                        t.setFornecJuridica(titulosParcela.get(i).getFornecJuridica());
                        t.setJuros(juros);                        
                        t.setMulta(multa);                       
                        t.setBanco(titulosParcela.get(i).getBanco());                       
                                           

                        titulosParcelaRelatorio.add(t);                       
            }
        }
         TabelaSimulacao1.setModel(modelo);
         TabelaSimulacao1.setDefaultRenderer(Object.class,renderer);
         TabelaSimulacao1.setDefaultEditor(Object.class, null);
         txtTotalTitulos.setText(String.format("%.2f",total).replace(".",","));
         txtTotalJuros.setText(String.format("%.2f",totalJuros).replace(".",","));
         txtTotalMultas.setText(String.format("%.2f",totalMulta).replace(".",","));
         txtTotalSimulado.setText(String.format("%.2f",totalSimulado).replace(".",","));
         txtTotalLancamentos.setText(String.valueOf(cont));
         conexao.fecharConexao();
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
      public void  Limpa(){
        titulosParcela=null;
        DefaultTableModel modelo=montaTabela();
        TabelaSimulacao1.setModel(modelo);
        txtTotalJuros.setText("");
        txtTotalMultas.setText("");
        txtTotalSimulado.setText("");
        txtTotalTitulos.setText("");
      }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelTabSimulacao = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelaSimulacao1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTotalSimulado = new javax.swing.JTextField();
        txtTotalMultas = new javax.swing.JTextField();
        txtTotalJuros = new javax.swing.JTextField();
        txtTotalTitulos = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTotalLancamentos = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbxFornecedor = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cbxOrdenar = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtNumLancamento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtDataInicio = new javax.swing.JFormattedTextField();
        jtxtDataFim = new javax.swing.JFormattedTextField();
        btnFiltrar = new javax.swing.JButton();
        btnExibir = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jtxtPagamento = new javax.swing.JFormattedTextField();
        btnGerarRelatorio = new javax.swing.JButton();
        btnEfetuarSimulacao = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btnSair = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Simulaçao de Contas a Pagar");
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

        panelTabSimulacao.setBackground(new java.awt.Color(255, 255, 255));

        TabelaSimulacao1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nº Lanc", "Nº Parcela", "Fornecedor", "Venc.", "Banco", "Valor", "Juros", "Multa", "Dias Protesto", "Valor Total", "Status", "Histórico"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(TabelaSimulacao1);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Total Titulos: ");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Total Juros ");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Total Multas:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Total Corrigido: ");

        txtTotalSimulado.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        txtTotalMultas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtTotalMultas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalMultasActionPerformed(evt);
            }
        });

        txtTotalJuros.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtTotalJuros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalJurosActionPerformed(evt);
            }
        });

        txtTotalTitulos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Nº Lançamentos");

        txtTotalLancamentos.setEditable(false);
        txtTotalLancamentos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtTotalLancamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalLancamentosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTabSimulacaoLayout = new javax.swing.GroupLayout(panelTabSimulacao);
        panelTabSimulacao.setLayout(panelTabSimulacaoLayout);
        panelTabSimulacaoLayout.setHorizontalGroup(
            panelTabSimulacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabSimulacaoLayout.createSequentialGroup()
                .addGroup(panelTabSimulacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelTabSimulacaoLayout.createSequentialGroup()
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
                        .addComponent(txtTotalSimulado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1052, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelTabSimulacaoLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtTotalJuros, txtTotalLancamentos, txtTotalMultas, txtTotalSimulado, txtTotalTitulos});

        panelTabSimulacaoLayout.setVerticalGroup(
            panelTabSimulacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabSimulacaoLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTabSimulacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabSimulacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(txtTotalLancamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelTabSimulacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jLabel11)
                        .addComponent(jLabel10)
                        .addComponent(jLabel7)
                        .addComponent(txtTotalSimulado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTotalMultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTotalJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTotalTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panelTabSimulacaoLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtTotalJuros, txtTotalLancamentos, txtTotalMultas, txtTotalSimulado, txtTotalTitulos});

        jTabbedPane1.addTab("Simulação", panelTabSimulacao);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Simulação de contas a Pagar");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filtrar títulos"));

        jLabel2.setText("Fornecedor");

        cbxFornecedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbxFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxFornecedorActionPerformed(evt);
            }
        });

        jLabel5.setText("Ordenar por:");

        cbxOrdenar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Vencimento", "N°Lançamento" }));
        cbxOrdenar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOrdenarActionPerformed(evt);
            }
        });

        jLabel8.setText("Nº do Lançamento:");

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

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cbxFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(txtNumLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jtxtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(btnFiltrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExibir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLimpar))
                    .addComponent(jLabel4))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnExibir, btnFiltrar, btnLimpar});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar)
                    .addComponent(btnExibir)
                    .addComponent(btnLimpar)
                    .addComponent(cbxFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Simular para"));

        jLabel3.setText("Pagamento em:");

        jtxtPagamento.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtPagamento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jtxtPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        btnGerarRelatorio.setBackground(new java.awt.Color(255, 255, 255));
        btnGerarRelatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-impressão-26.png"))); // NOI18N
        btnGerarRelatorio.setText("<html>Gerar<br>Relatório</html>");
        btnGerarRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarRelatorioActionPerformed(evt);
            }
        });

        btnEfetuarSimulacao.setBackground(new java.awt.Color(255, 255, 255));
        btnEfetuarSimulacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-gráfico-combinado-24.png"))); // NOI18N
        btnEfetuarSimulacao.setText("<html>Efetuar<br>Simulação</html>");
        btnEfetuarSimulacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEfetuarSimulacaoActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\Devair\\Google Drive\\fatec\\5 semestre\\TG\\logopp.png")); // NOI18N

        btnSair.setBackground(new java.awt.Color(255, 255, 255));
        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-sair-26.png"))); // NOI18N
        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGerarRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEfetuarSimulacao, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnEfetuarSimulacao, btnGerarRelatorio, btnSair});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEfetuarSimulacao, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGerarRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnEfetuarSimulacao, btnGerarRelatorio, btnSair});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGerarRelatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarRelatorioActionPerformed
      if(titulosParcelaRelatorio==null){
           JOptionPane.showMessageDialog(this, "Efetue a Simulação para gerar o relatorio");
      }
      else{
            String src="C:/Users/Devair/Documents/NetBeansProjects/prjContasaPagar/"
                    + "src/fatec/Relatorios/RelatorioContasSimuladas.jasper";
            JasperPrint jasperPrint=null;

            try {
                jasperPrint=JasperFillManager.fillReport(src, null,new JRBeanCollectionDataSource(titulosParcelaRelatorio));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(rootPane, "Erro ao Gerar Relatorio\n"+ex);
            }
            JasperViewer view =new JasperViewer(jasperPrint,false);
             view.setTitle("Relatório Contas Simuladas");
             URL url = this.getClass().getResource("/fatec/resources/logo2.png");  
             Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);  
             view.setIconImage(iconeTitulo);
             view.setVisible((true));
        
      }
        
        
    }//GEN-LAST:event_btnGerarRelatorioActionPerformed

    private void btnEfetuarSimulacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEfetuarSimulacaoActionPerformed
        // TODO add your handling code here:
        conexao=new Conexao();
        if(jtxtPagamento.getText().replace("/","").replace(" ", "").length()<8){
             JOptionPane.showMessageDialog(this, "Insira uma data para a Simulação de Pagamento");
             Limpa();
        }else{
            Date dtPag = null;
            LocalDate hoje = LocalDate.now();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            //Date hoje = new Date();
            
            try {
                dtPag = (Date)Admin.formataData(jtxtPagamento.getText());
           
                if(dtPag==null ){
                     JOptionPane.showMessageDialog(this, "Insira uma data válida!");
                     Limpa();
                }
                else if(dtPag.before(Admin.formataData(hoje.format(f)))){
                    JOptionPane.showMessageDialog(this, "A data de Pagamento deve ser maior ou igual a data de hoje");
                    Limpa();
                }
                else{
                     if(titulosParcela==null){
                        JOptionPane.showMessageDialog(this, "Filtre os titulos a serem simulados");
                    }else{
                        ExibirSimulacao(titulosParcela, dtPag);
                    }
                    
                   
                    
                     
                }
               
            } catch (ParseException ex) {
                Logger.getLogger(GuiSimulacaoContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
      
        }
        conexao.fecharConexao();
    }//GEN-LAST:event_btnEfetuarSimulacaoActionPerformed

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
       
         
        
       
    }//GEN-LAST:event_formInternalFrameOpened

    private void cbxFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxFornecedorActionPerformed
        conexao = new Conexao();
        if(cbxFornecedor.getSelectedIndex()!=0)
        listaPorFornec();
        conexao.fecharConexao();
    }//GEN-LAST:event_cbxFornecedorActionPerformed

    private void txtNumLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumLancamentoActionPerformed

    private void txtNumLancamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumLancamentoKeyPressed
        conexao = new Conexao();
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            listaPorCodigo();
        }
        conexao.fecharConexao();

    }//GEN-LAST:event_txtNumLancamentoKeyPressed

    private void cbxOrdenarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOrdenarActionPerformed
        // TODO add your handling code here:
        conexao = new Conexao();
        
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

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed

         conexao = new Conexao();
        if(txtNumLancamento.getText().isEmpty() &&
            jtxtDataInicio.getText().replace("/","").replace(" ","").length()!=0 &&
            jtxtDataFim.getText().replace("/","").replace(" ","").length()!=0){

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
            } catch (ParseException ex) {
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

    private void btnExibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirActionPerformed
         conexao = new Conexao();
        titulosParcela  = daoTituloParcela.listarTitulos("N");
        try {
            Exibir(titulosParcela);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        conexao.fecharConexao();

    }//GEN-LAST:event_btnExibirActionPerformed

    private void txtTotalJurosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalJurosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalJurosActionPerformed

    private void txtTotalMultasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalMultasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalMultasActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        Limpa();
        titulosParcelaRelatorio=null;
    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtTotalLancamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalLancamentosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalLancamentosActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        dispose();
        conexao.fecharConexao();
    }//GEN-LAST:event_btnSairActionPerformed
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
            java.util.logging.Logger.getLogger(GuiSimulacaoContasAPagar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiSimulacaoContasAPagar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiSimulacaoContasAPagar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiSimulacaoContasAPagar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiSimulacaoContasAPagar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelaSimulacao1;
    private javax.swing.JButton btnEfetuarSimulacao;
    public javax.swing.JButton btnExibir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnGerarRelatorio;
    private javax.swing.JButton btnLimpar;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JFormattedTextField jtxtDataFim;
    private javax.swing.JFormattedTextField jtxtDataInicio;
    private javax.swing.JFormattedTextField jtxtPagamento;
    private javax.swing.JPanel panelTabSimulacao;
    private javax.swing.JTextField txtNumLancamento;
    private javax.swing.JTextField txtTotalJuros;
    private javax.swing.JTextField txtTotalLancamentos;
    private javax.swing.JTextField txtTotalMultas;
    private javax.swing.JTextField txtTotalSimulado;
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
}
