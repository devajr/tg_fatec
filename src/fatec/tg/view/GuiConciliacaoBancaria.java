/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.view;

import fatec.tg.controle.Admin;
import fatec.tg.controle.Conexao;
import fatec.tg.controle.DaoAgencia;
import fatec.tg.controle.DaoBanco;
import fatec.tg.controle.DaoConciliacao;
import fatec.tg.controle.DaoContaBancaria;
import fatec.tg.model.Agencia;
import fatec.tg.model.Banco;
import fatec.tg.model.ConciliacaoBancaria;
import fatec.tg.model.ContaBancaria;
import fatec.tg.model.TituloParcela;
import java.awt.Color;
import java.awt.Component;
import java.awt.List;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import oracle.jrockit.jfr.tools.ConCatRepository;

/**
 *
 * @author Bia Delmont
 */
public class GuiConciliacaoBancaria extends javax.swing.JInternalFrame {

  private static GuiConciliacaoBancaria guiConciliacaoBancaria;

   
      public  static GuiConciliacaoBancaria getInstancia(){
      if(guiConciliacaoBancaria== null){
          guiConciliacaoBancaria= new GuiConciliacaoBancaria();
      }
      return guiConciliacaoBancaria;
  }
    /**
     * Creates new form GuiConciliacaoBancarias
     */
    public GuiConciliacaoBancaria() {
        initComponents();
    }
    public DefaultTableModel montaTabela(){
      DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Codigo");
            modelo.addColumn("Data");
            modelo.addColumn("Saldo Anterior");
            modelo.addColumn("Saldo Atual");
            modelo.addColumn("Banco");
            modelo.addColumn("Agencia");
            modelo.addColumn("Conta");
            modelo.addColumn("Status");

     return (modelo);
 }
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
       DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
        String estado = table.getModel().getValueAt(row,7).toString();
      
      
        if (estado.equals("Pendente")) {
            c.setForeground(Color.RED);
        } 
        else
        {
            c.setForeground(Color.BLACK);  
        }
        return c;
        
    }
};
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
       public void Exibir(ArrayList<ConciliacaoBancaria>conciliacoes) throws ParseException{
     conexao = new Conexao();

        DefaultTableModel modelo=montaTabela();
        if(conciliacoes.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhuma conciliação foi encontrada");
            
        }else{
            
          for(int i =0; i< conciliacoes.size(); i++){
            
                modelo.addRow(new Object[]{String.valueOf(conciliacoes.get(i).getCodigo()),conciliacoes.get(i).getDataCon()
                        ,String.format("%.2f", conciliacoes.get(i).getSaldoAnt()).replace(".",","),
                        String.format("%.2f",conciliacoes.get(i).getSaldoAtual()).replace(".",","),conciliacoes.get(i).getBanco().getNome(),
                        conciliacoes.get(i).getAgencia().getNumAgencia(),conciliacoes.get(i).getContaBancaria().getNumConta(),Admin.formataStatusCon(conciliacoes.get(i).getStatus())});
            

              
            }
        }
           TabelaConciliacao.setModel(modelo);
           TableRowSorter<TableModel> sorter =new TableRowSorter<TableModel>(modelo);
           sorter.setComparator(1, comparator);
           sorter.setComparator(2, comparatorDouble);
           sorter.setComparator(3, comparatorDouble);
           TabelaConciliacao.setRowSorter(sorter);
           TabelaConciliacao.setDefaultEditor(Object.class, null);
           TabelaConciliacao.getColumnModel().getColumn(7).setCellRenderer(renderer);
           conexao.fecharConexao();
 }

    public void lerArquivo(String caminho) throws FileNotFoundException, ParseException{
        
   
        int numAgencia=0;
        int numConta=0;
        Scanner s = new Scanner(new File(caminho));
        while(s.hasNextLine()){
            String linha=s.nextLine();
            if(linha.contains("CLTID")){
                conciliacaoBancaria= new ConciliacaoBancaria(daoConciliacao.obterCodConc()+1);
                conciliacaoBancaria.setStatus("P");
                System.out.println(getConteudo(linha,"CLTID"));
                numAgencia=Integer.parseInt(getConteudo(linha,"CLTID").substring(0,4));
                numConta=Integer.parseInt(getConteudo(linha,"CLTID").substring(4,10));
                contaBancaria=daoContaBancaria.consultarBanco(numAgencia, numConta);
                
                System.out.println(getConteudo(linha,"CLTID").substring(0,4));
                
                System.out.println(getConteudo(linha,"CLTID").substring(4,10));
                if(contaBancaria==null){
                 JOptionPane.showMessageDialog(this, "Agencia ou Conta Bancaria não encontrada!");
                }
                else
                {  
                    
                   
                   
                    btnConciliar.requestFocus();
                    banco=daoBanco.consultar(contaBancaria.getCodBanco());
                    conciliacaoBancaria.setBanco(banco);
                    conciliacaoBancaria.setAgencia(new DaoAgencia(conexao.conectar()).consultar(contaBancaria.getCodBanco(), numAgencia));
                    conciliacaoBancaria.setContaBancaria(contaBancaria);
                    conciliacaoBancaria.setSaldoAnt(contaBancaria.getSaldo());
                    jTxtSaldoAnt.setText(String.valueOf(contaBancaria.getSaldo()).replace(".",","));  
                }   
            }
            else if(linha.contains("DTEND"))
            {
                        System.out.println(getConteudo(linha,"DTEND"));
                        Date data=Admin.formataData1(getConteudo(linha,"DTEND"));
                        SimpleDateFormat formatador = new SimpleDateFormat("ddMMyyyy");
                        String dataFormatada = formatador.format(data);
                        conciliacaoBancaria.setDataCon(dataFormatada);
                        
                        System.out.println("data formatada: "+dataFormatada);
                        
                        jTxtDataC.setText(dataFormatada);

            }
            else if(linha.contains("LEDGER"))
            {
                        //System.out.println(conciliacaoBancaria.getAgencia().getNumAgencia());
                        System.out.println(getConteudo(linha,"LEDGER"));
                      
                        conciliacaoBancaria.setSaldoAtual(Double.parseDouble(getConteudo(linha,"LEDGER")));
                        jTxtSaldoAtual.setText(getConteudo(linha,"LEDGER"));
                        jTxtSaldoDiferenca.setText(String.format("%.2f",conciliacaoBancaria.getSaldoAtual()- Math.abs(contaBancaria.getSaldo())).replace(".",","));
            }
            
            
           
        }
              daoConciliacao.inserir(conciliacaoBancaria);
              
              listar();
              
              TabelaConciliacao.setColumnSelectionInterval(0, 0);  
              TabelaConciliacao.setRowSelectionInterval(0, 0); 
              jTabProcurar.setSelectedIndex(1);
    }
    
    public String getConteudo(String linha,String tag){
        return linha.substring(linha.indexOf("<"+tag+">")+tag.length()+2,linha.length());
    }
    public void listarPorCodigo() throws ParseException{
        conciliacoes=daoConciliacao.listarConcCod(Integer.parseInt(txtCodigo.getText()));
        Exibir(conciliacoes);
    }
     public void listar() throws ParseException{
        conciliacoes=daoConciliacao.listarConc();
        Exibir(conciliacoes);
    }
     

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnConciliar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTabProcurar = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txtCaminhoArquivo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnEscolherArquivo = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbxBanco = new javax.swing.JComboBox<>();
        btnExibirTodos = new javax.swing.JButton();
        btnFiltrar = new javax.swing.JButton();
        cbxSituacao = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaConciliacao = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTxtSaldoAnt = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jTxtSaldoAtual = new javax.swing.JFormattedTextField();
        jTxtSaldoDiferenca = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTxtDataC = new javax.swing.JFormattedTextField();
        btnImportar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Conciliação Bancaria");
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

        btnConciliar.setBackground(new java.awt.Color(255, 255, 255));
        btnConciliar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/dollar-sign-and-piles-of-coins.png"))); // NOI18N
        btnConciliar.setText("Conciliar");
        btnConciliar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConciliarActionPerformed(evt);
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

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Conciliação Bancária");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/resources/logopp.png"))); // NOI18N

        jTabProcurar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabProcurarStateChanged(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtCaminhoArquivo.setEditable(false);
        txtCaminhoArquivo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCaminhoArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCaminhoArquivoActionPerformed(evt);
            }
        });

        jLabel3.setText("Local arquivo:");

        btnEscolherArquivo.setBackground(new java.awt.Color(255, 255, 255));
        btnEscolherArquivo.setText("<html>Procurar arquivo</html>");
        btnEscolherArquivo.setBorder(null);
        btnEscolherArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscolherArquivoActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filtro"));

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

        jLabel7.setText("Codigo:");

        jLabel2.setText("Banco:");

        cbxBanco.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbxBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxBancoActionPerformed(evt);
            }
        });

        btnExibirTodos.setBackground(new java.awt.Color(255, 255, 255));
        btnExibirTodos.setText("Exibir Tudo");
        btnExibirTodos.setBorder(null);
        btnExibirTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExibirTodosActionPerformed(evt);
            }
        });

        btnFiltrar.setBackground(new java.awt.Color(255, 255, 255));
        btnFiltrar.setText("Filtrar");
        btnFiltrar.setBorder(null);
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        cbxSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Deferido ", "Indeferido", "Pendente" }));
        cbxSituacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSituacaoActionPerformed(evt);
            }
        });

        jLabel9.setText("Situação:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 259, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cbxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExibirTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExibirTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Arquivos"));

        TabelaConciliacao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Código:", "Data:", "Saldo Anterior", "Saldo Atual", "Banco", "Agencia", "Conta Bancaria", "Status"
            }
        ));
        TabelaConciliacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelaConciliacaoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelaConciliacao);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCaminhoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEscolherArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCaminhoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEscolherArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jTabProcurar.addTab("Procurar", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setText("Saldo Anterior:    ");

        jTxtSaldoAnt.setEditable(false);
        jTxtSaldoAnt.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTxtSaldoAnt.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jTxtSaldoAnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtSaldoAntActionPerformed(evt);
            }
        });

        jLabel5.setText("Saldo do extrato:");

        jTxtSaldoAtual.setEditable(false);
        jTxtSaldoAtual.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTxtSaldoAtual.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jTxtSaldoAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtSaldoAtualActionPerformed(evt);
            }
        });

        jTxtSaldoDiferenca.setEditable(false);
        jTxtSaldoDiferenca.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTxtSaldoDiferenca.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jTxtSaldoDiferenca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtSaldoDiferencaActionPerformed(evt);
            }
        });

        jLabel6.setText("Diferença:");

        btnCancelar.setBackground(new java.awt.Color(255, 255, 255));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-cancelar-30.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel8.setText("Data Conciliação:");

        jTxtDataC.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jTxtDataC.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jTxtDataC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtDataCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar)
                .addGap(21, 21, 21))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtSaldoAnt))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTxtSaldoDiferenca, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTxtSaldoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxtDataC, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(540, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTxtDataC, jTxtSaldoAnt, jTxtSaldoAtual, jTxtSaldoDiferenca});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTxtDataC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTxtSaldoAnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTxtSaldoAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTxtSaldoDiferenca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 246, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jTxtDataC, jTxtSaldoAnt, jTxtSaldoAtual, jTxtSaldoDiferenca});

        jTabProcurar.addTab("Conciliação", jPanel2);

        btnImportar.setBackground(new java.awt.Color(255, 255, 255));
        btnImportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-baixar-26.png"))); // NOI18N
        btnImportar.setText("Importar");
        btnImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnConciliar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnImportar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTabProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 751, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(96, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnConciliar, btnImportar, btnSair});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnConciliar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabProcurar)
                        .addGap(38, 38, 38))))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnConciliar, btnImportar, btnSair});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConciliarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConciliarActionPerformed
        if(TabelaConciliacao.getSelectedRowCount()==0){
              jTabProcurar.setSelectedIndex(0);
              Object[]options = { "OK"};
              JOptionPane.showOptionDialog(null, "Selecione um arquivo a conciliar ", "Aviso",
              JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
              null, options, options[0]);
        }
        else
        {   int indiceLinha = TabelaConciliacao.getSelectedRow(); 
            int cod=Integer.parseInt(""+TabelaConciliacao.getValueAt(indiceLinha, 0));
            conciliacaoBancaria=daoConciliacao.consultarCod(cod);
            
            if( conciliacaoBancaria.getStatus().equals("P"))
            {
                
                Icon figura = new ImageIcon (getToolkit().createImage(getClass().getResource("/fatec/tg/icon/chat.png")));
                Object[]options = { "Sim","Não"};
                //System.out.println(contaBancaria.getCodBanco());
                if(JOptionPane.showOptionDialog(null, "Deseja considerar a atualização do saldo bancario para o valor do extrato "+"\n"+
                       "Banco: "+ conciliacaoBancaria.getBanco().getNome()+"\n"+
                       "Agencia: "+ conciliacaoBancaria.getAgencia().getNumAgencia()+"\n"+
                       "Conta: "+ conciliacaoBancaria.getContaBancaria().getNumConta(), "Aviso de Conciliação",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        figura, options, options[0])==0)
                {
//                    System.out.println(contaBancaria.getSaldo()+ conciliacaoBancaria.getSaldoAtual());
                    contaBancaria=conciliacaoBancaria.getContaBancaria();
                    contaBancaria.setSaldo(conciliacaoBancaria.getSaldoAtual());
                    daoContaBancaria.AtualizarSaldo(contaBancaria);
                    System.out.println("passei denovo");
                    conciliacaoBancaria.setStatus("D");

                    jTabProcurar.setSelectedIndex(0);
                    

                }
                else{
                    conciliacaoBancaria.setStatus("I");
                }
                if(daoConciliacao.obterCodConc()<conciliacaoBancaria.getCodigo()){
                    daoConciliacao.inserir(conciliacaoBancaria);
                }
                else{
                    daoConciliacao.atualiza(conciliacaoBancaria);
                }
                   
                try {
                    listar();

                } catch (ParseException ex) {
                    Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Esta conciliação já foi efetuada!");
                jTabProcurar.setSelectedIndex(0);
            }
            
        }
        
      
    }//GEN-LAST:event_btnConciliarActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
      dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void cbxBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxBancoActionPerformed
        if(cbxBanco.getSelectedIndex()>0){
            Banco banco=daoBanco.consultarNome((String) cbxBanco.getSelectedItem());
            conciliacoes=daoConciliacao.listarConcBanco(banco.getCodBanco());
            try {
                  Exibir(conciliacoes);
            } catch (ParseException ex) {
                  Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
    }//GEN-LAST:event_cbxBancoActionPerformed

    private void txtCaminhoArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCaminhoArquivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCaminhoArquivoActionPerformed

    private void btnEscolherArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscolherArquivoActionPerformed
        
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter= new FileNameExtensionFilter("ofc","ofc");
        //fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);
         /* JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Procurar Diretorio");
                //fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //FileNameExtensionFilter filter= new FileNameExtensionFilter("imagem", "")
              //  fileChooser.setFileFilter(filter);
                fileChooser.showOpenDialog(this);*/
        fc.showOpenDialog(this);
        File f = null;
        f  = fc.getSelectedFile();
        if(f!=null){
         txtCaminhoArquivo.setText(f.getPath());
        }
        
    }//GEN-LAST:event_btnEscolherArquivoActionPerformed

    private void jTxtSaldoAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtSaldoAtualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtSaldoAtualActionPerformed

    private void jTxtSaldoAntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtSaldoAntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtSaldoAntActionPerformed

    private void jTxtSaldoDiferencaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtSaldoDiferencaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtSaldoDiferencaActionPerformed

    private void btnImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportarActionPerformed
      if(txtCaminhoArquivo.getText().isEmpty()){
           JOptionPane.showMessageDialog(this, "Escolha um arquivo ofc a ser importado!");
      }
      else{
          try 
           {
                try 
                {
                    lerArquivo(txtCaminhoArquivo.getText());
                } 
                catch (ParseException ex) 
                {
                    Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            catch (FileNotFoundException ex) 
            {
                Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
          txtCaminhoArquivo.setText("");
      }
        
    }//GEN-LAST:event_btnImportarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        jTabProcurar.setSelectedIndex(0);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
       
        conexao = new Conexao();
        daoAgencia=new DaoAgencia(conexao.conectar());
        daoBanco =  new DaoBanco(conexao.conectar());
        daoContaBancaria = new DaoContaBancaria(conexao.conectar());
        daoConciliacao = new  DaoConciliacao(conexao.conectar());
        ArrayList<Banco> bancos = daoBanco.listarBancos();
        for (int x=0; x<bancos.size(); x++){
           cbxBanco.addItem(bancos.get(x).getNome()); 
        }  
        conciliacoes=daoConciliacao.listarConc();
        try {
            Exibir(conciliacoes);
        } catch (ParseException ex) {
            Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnExibirTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirTodosActionPerformed
       conciliacoes=daoConciliacao.listarConc();
        try {
            Exibir(conciliacoes);
        } catch (ParseException ex) {
            Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }//GEN-LAST:event_btnExibirTodosActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        if( txtCodigo.getText().isEmpty() ){
            JOptionPane.showMessageDialog(this, "O campo codigo deve ser preenchido!");
        }
        else{
            try {
                listarPorCodigo();
            } catch (ParseException ex) {
                Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void jTxtDataCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtDataCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTxtDataCActionPerformed

    private void cbxSituacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSituacaoActionPerformed
        if(cbxSituacao.getSelectedIndex()>0){
            conciliacoes=daoConciliacao.listarConcSituacao(cbxSituacao.getSelectedIndex());
      try {
          Exibir(conciliacoes);
      } catch (ParseException ex) {
          Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
      }
        }
        
    }//GEN-LAST:event_cbxSituacaoActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
         if(evt.getKeyCode() == KeyEvent.VK_ENTER){
           try {
               listarPorCodigo();
           } catch (ParseException ex) {
               Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void jTabProcurarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabProcurarStateChanged
         if(jTabProcurar.getSelectedIndex()==1 ){
            if(TabelaConciliacao.getSelectedRowCount()==0){
              jTabProcurar.setSelectedIndex(0);
              Object[]options = { "OK"};
              JOptionPane.showOptionDialog(null, "Selecione uma conciliacao a consultar ", "Aviso",
              JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
              null, options, options[0]);
          }
            else{
                int indiceLinha = TabelaConciliacao.getSelectedRow(); 
                int cod=Integer.parseInt(""+TabelaConciliacao.getValueAt(indiceLinha, 0));
                conciliacaoBancaria=daoConciliacao.consultarCod(cod);
                
                jTxtDataC.setText(conciliacaoBancaria.getDataCon());
                jTxtSaldoAnt.setText(String.format("%.2f",conciliacaoBancaria.getSaldoAnt()).replace(".",","));
                jTxtSaldoAtual.setText(String.format("%.2f",conciliacaoBancaria.getSaldoAtual()).replace(".",","));
                jTxtSaldoDiferenca.setText(String.format("%.2f",(conciliacaoBancaria.getSaldoAnt()- conciliacaoBancaria.getSaldoAtual())*(-1)).replace(".",","));
                
            }
       }
    }//GEN-LAST:event_jTabProcurarStateChanged

    private void TabelaConciliacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelaConciliacaoMouseClicked
        jTabProcurar.setSelectedIndex(1);
    }//GEN-LAST:event_TabelaConciliacaoMouseClicked

    
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
            java.util.logging.Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiConciliacaoBancaria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelaConciliacao;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConciliar;
    private javax.swing.JButton btnEscolherArquivo;
    private javax.swing.JButton btnExibirTodos;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImportar;
    private javax.swing.JButton btnSair;
    private javax.swing.JComboBox<String> cbxBanco;
    private javax.swing.JComboBox<String> cbxSituacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabProcurar;
    private javax.swing.JFormattedTextField jTxtDataC;
    private javax.swing.JFormattedTextField jTxtSaldoAnt;
    private javax.swing.JFormattedTextField jTxtSaldoAtual;
    private javax.swing.JFormattedTextField jTxtSaldoDiferenca;
    private javax.swing.JTextField txtCaminhoArquivo;
    private javax.swing.JTextField txtCodigo;
    // End of variables declaration//GEN-END:variables
    private DaoBanco daoBanco=null;
    private DaoAgencia daoAgencia=null;
    private Agencia agencia=null;
    private DaoContaBancaria daoContaBancaria=null;
    private ContaBancaria contaBancaria=null;
    private Banco banco=null;
    private Conexao conexao=null;
    private DaoConciliacao daoConciliacao=null;
    public ConciliacaoBancaria conciliacaoBancaria=null;
    public ArrayList<ConciliacaoBancaria> conciliacoes=null;

}
