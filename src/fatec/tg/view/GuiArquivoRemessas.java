/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.view;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import fatec.tg.controle.Admin;
import fatec.tg.controle.Conexao;
import fatec.tg.controle.DaoAgencia;
import fatec.tg.controle.DaoArquivoRemessa;
import fatec.tg.controle.DaoBanco;
import fatec.tg.controle.DaoContaBancaria;
import fatec.tg.controle.DaoFornecPF;
import fatec.tg.controle.DaoFornecPJ;
import fatec.tg.controle.DaoFornecedor;
import fatec.tg.controle.DaoRemessaTitulo;
import fatec.tg.controle.DaoTitulo;
import fatec.tg.controle.DaoTituloParcela;
import fatec.tg.controle.DaoTpPagamento;
import fatec.tg.model.Agencia;
import fatec.tg.model.ArquivoRemessa;
import fatec.tg.model.Banco;
import fatec.tg.model.ContaBancaria;
import fatec.tg.model.FornecFisica;
import fatec.tg.model.FornecJuridica;
import fatec.tg.model.Fornecedor;
import fatec.tg.model.RemessaTitulo;
import fatec.tg.model.TipoPagamento;
import fatec.tg.model.Titulo;
import fatec.tg.model.TituloParcela;
import static fatec.tg.view.GuiMenu.painelMenu;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.eclipse.persistence.internal.oxm.StrBuffer;

/**
 *
 * @author Bia Delmont
 */
public class GuiArquivoRemessas extends javax.swing.JInternalFrame {
    
     private static GuiArquivoRemessas guiArquivoRemessas;
     public static GuiArquivoRemessas getInstancia(){
         if(guiArquivoRemessas==null){
             guiArquivoRemessas=new GuiArquivoRemessas();
             
         }
             return guiArquivoRemessas;
         
     }
     
    public GuiArquivoRemessas() {
        initComponents();
    }
    public File GerarRemessa(){
        File file=null;
        
        
        
        
        
        
     return file;   
    }
    
   public String constroiHeader(int cod){
       
        arquivoRemessa=daoArquivoRemessa.consultar(cod);
                        
        banco=daoBanco.consultar(arquivoRemessa.getBanco().getCodBanco());
        Admin admin=new Admin();
        String nop="01";
        String op="REMESSA";
        String cob= Admin.preencheCom("01COBRANCA", " ", 17, 2);
        
        String sAgencia=String.valueOf(arquivoRemessa.getAgencia().getNumAgencia());
        String sContaB=(String.format("%08d", arquivoRemessa.getContaBancaria().getNumConta()));
        String nomeEmp=admin.preencheCom("Eleven Idiomas"," ", 30, 2);
        String sBanco=(String.format("%03d", arquivoRemessa.getBanco().getCodBanco()));
        String nomeBanco= admin.preencheCom(banco.getNome()," ", 15, 2);
        String branco="        ";
        String dataRem = arquivoRemessa.getDataRemessa().substring(0,5);
        dataRem=dataRem+arquivoRemessa.getDataRemessa().substring(8,10).replace("/", "");
        dataRem = admin.preencheCom(dataRem, " ", 300, 2);
        String codRem=(String.format("%06d", 1));
                        
        return (nop+op+cob+sAgencia+sContaB+branco+nomeEmp+sBanco+nomeBanco+dataRem+codRem);
       
       
   }
   public String constroiDetalhe(int cod,TituloParcela tituloParcela, int i){
       
        arquivoRemessa=daoArquivoRemessa.consultar(cod);
                        
        banco=daoBanco.consultar(arquivoRemessa.getBanco().getCodBanco());
        Admin admin=new Admin();
        String tipo="1";
        String codInscEm="02";
        String numInsEm="07561011000171";
        String codInscF=null;
        String numInscF=null;
        String nomeF=null;
        String sAgencia=String.valueOf(arquivoRemessa.getAgencia().getNumAgencia());
        String sContaB=(String.format("%08d", arquivoRemessa.getContaBancaria().getNumConta())+"    ");

        String codAleg="0000";
        String usoEmp="NUMLANC"+String.format("%06d", tituloParcela.getNumLancamento())+"NUMPARC"+String.format("%05d", tituloParcela.getNumParcela());
        String nossoNum=Admin.preencheCom(tituloParcela.getNossoNum(),"0", 8, 1);
        String moedaV="0000000000000";
        String numCart="109";
        String usoBanco=Admin.preencheCom(" ", " ", 21, 1);
        String codCarteira="I";
        String codOcor="01";
        String numDoc="          ";
        String datavenc = tituloParcela.getDataVenc().substring(0,5).replace("/","");
        datavenc=datavenc+tituloParcela.getDataVenc().substring(8,10).replace("/","");
        String valorTiulo=Admin.preencheCom(String.format("%.2f", tituloParcela.getValorParcela()).replace(",", ""), "0", 13, 1);
        String sBanco=(String.format("%03d", arquivoRemessa.getBanco().getCodBanco()));
        String agenciaCob="00000";
        String especie="01";
        String aceite="A";
        String dtEmisao=tituloParcela.getDtEmissao().substring(0,5).replace("/","");
        dtEmisao=dtEmisao+tituloParcela.getDtEmissao().substring(8,10).replace("/", ""); 
        String instCob="0000";
        String juros=Admin.preencheCom(String.format( "%.2f",tituloParcela.getJuros()).replace(",",""), "0", 13, 1);
        //juros=String.format("%013d", juros);
        String zero= admin.preencheCom("0", "0", 45, 2);
        if(tituloParcela.getFornecFisica()==null){
            codInscF="02";
            numInscF=Admin.preencheCom(tituloParcela.getFornecJuridica().getCnpj(), "0", 14, 1);
            nomeF=Admin.preencheCom(tituloParcela.getFornecJuridica().getNomeFan(), " ", 40, 2);
        }
        else{
            codInscF="01";
            numInscF=Admin.preencheCom(tituloParcela.getFornecFisica().getCpf(), "0", 14, 1);
            nomeF=Admin.preencheCom(tituloParcela.getFornecFisica().getNome(), " ", 40, 2);
        }
    
       String endereco=Admin.preencheCom( tituloParcela.getFornecedor().getLogradouro() +" "+
               tituloParcela.getFornecedor().getNomeLogra() +" " +
               tituloParcela.getFornecedor().getNumero() +" "+
               tituloParcela.getFornecedor().getComplemento(), " ", 40, 2);
        String bairro=Admin.preencheCom(tituloParcela.getFornecedor().getBairro(), " ", 12, 2);
        String cep= tituloParcela.getFornecedor().getCep();
        String cidade= Admin.preencheCom(tituloParcela.getFornecedor().getCidade(), " ", 15, 2);
        String uf=tituloParcela.getFornecedor().getEstado();
        String branco=Admin.preencheCom(" ", " ", 43, 2);
  
        String codSeq=(String.format("%06d", i));
                        
        
       
                   
        
        return (tipo+codInscEm+numInsEm+sAgencia+sContaB+codAleg+usoEmp+nossoNum+moedaV+numCart+usoBanco+codCarteira
                +codOcor+numDoc+datavenc+valorTiulo+sBanco+agenciaCob+especie+aceite+dtEmisao+instCob+
                juros+zero+codInscF+numInscF+nomeF+endereco+bairro+cep+cidade+uf+branco+codSeq);
        
   }
   public String constroiTrailler(int i){
       
        String registro=Admin.preencheCom("9", " ", 394, 2);
        String codSeq=String.format("%06d", i);
        return (registro+codSeq);
       
       
   }


    public void listarPorCodigo() throws ParseException{
        arquivosRem=daoArquivoRemessa.listarCod(Integer.parseInt(txtCodRemessa1.getText()));
        System.out.println(arquivosRem.size());
        ExibirRem(arquivosRem);
    }
     
       public double converte(String arg) throws ParseException{
		//obtem um NumberFormat para o Locale default (BR)
		NumberFormat nf = NumberFormat.getNumberInstance();
		//converte um número com vírgulas ex: 2,56 para double
		double number = nf.parse(arg).doubleValue();
		return number;
	}
         public int buscaCodBanco(String nome){
      banco=daoBanco.consultarNome(nome);
      return(banco.getCodBanco());
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
        public DefaultTableModel montaTabela(){
      DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("N°Item");
            modelo.addColumn("N° Lançamento");
            modelo.addColumn("N° Parcela");
            modelo.addColumn("Forncecedor");
            modelo.addColumn("Vencimento");
            modelo.addColumn("Data Pagamento");
            modelo.addColumn("Valor");
            modelo.addColumn("Juros");
            modelo.addColumn("Multa");
            modelo.addColumn("Valor Pago");
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
      
        Date hoje =new Date();
        Date str = null;
        try {
            str = Admin.formataData((String) estado);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (estado.equals("Aberto")) {
            c.setForeground(new Color(60,179,113));
            
        } else {
            c.setForeground(Color.BLACK);
        }
        
        return c;
        
    }
};
 public void Exibir(ArrayList<TituloParcela>titulosParcela) throws ParseException{
     conexao = new Conexao();
     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
     int cont=0;
     double total=0.0;
     double totalPago=0;
    
        DefaultTableModel modelo=montaTabela();
        if(titulosParcela.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhum Titulo encontrado");

        }else{
            
          for(int i =0; i< titulosParcela.size(); i++){
                Date hoje =new Date();
                 Date dtvenc = null;
                 double juros=0;
                 double multa=0;
                 try {
                     dtvenc = (Date)Admin.formataData(titulosParcela.get(i).getDataVenc());
                } catch (ParseException ex) {
                        Logger.getLogger(GuiPagarTitulo.class.getName()).log(Level.SEVERE, null, ex);
                }
                 Calendar a = Calendar.getInstance();
                a.setTime(hoje);//data maior
                Calendar b = Calendar.getInstance();
                b.setTime(dtvenc);// data menor
                if(hoje.after(dtvenc)){
                  a.add(Calendar.DATE, - b.get(Calendar.DAY_OF_MONTH));
                   juros=a.get(Calendar.DAY_OF_MONTH)*titulosParcela.get(i).getJuros();
                   multa=titulosParcela.get(i).getMulta();
                }else{
                    juros=0;
                    multa=0;
                }
                totalPago=titulosParcela.get(i).getValorParcela()+juros+multa;
                modelo.addRow(new Object[]{i+1,String.valueOf(titulosParcela.get(i).getNumLancamento()),titulosParcela.get(i).getNumParcela()
                        ,titulosParcela.get(i).getFornecFisica()==null? titulosParcela.get(i).getFornecJuridica().getNomeFan(): titulosParcela.get(i).getFornecFisica().getNome(),
                        titulosParcela.get(i).getDataVenc(),titulosParcela.get(i).getDataPag(),
                        String.valueOf(titulosParcela.get(i).getValorParcela()),juros,multa,totalPago,Admin.formataStatusPag(titulosParcela.get(i).getStatusPag())});
            
                cont++;
              total=total+totalPago;
              
            }
        }
         TabelaRemessaTitulo.setModel(modelo);
          TableRowSorter<TableModel> sorter =new TableRowSorter<TableModel>(modelo);
           sorter.setComparator(4, comparator);
           sorter.setComparator(5, comparator);
           sorter.setComparator(6, comparatorDouble);
           sorter.setComparator(9, comparatorDouble);
           TabelaRemessaTitulo.setRowSorter(sorter);
       
         TabelaRemessaTitulo.setDefaultEditor(Object.class, null);
         txtTotalLancamentos.setText(String.valueOf(cont));
         jtxtTotalPagar.setText(String.format("%.2f",total).replace(".",","));
         
        System.out.println(String.valueOf(total));
         conexao.fecharConexao();
 }

 public DefaultTableModel montaTabelaRem(){
      DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Codigo Remessa");
            modelo.addColumn("Banco");
            modelo.addColumn("Agencia");
            modelo.addColumn("Conta Bancaria");
            modelo.addColumn("Padrao");
            modelo.addColumn("Valor Total");
            modelo.addColumn("Data Remessa");
            modelo.addColumn("Status");

     return (modelo);
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
 public void ExibirRem(ArrayList<ArquivoRemessa>arquivosRem) throws ParseException{
        conexao = new Conexao();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        int cont=0;
        double total=0.0;
        double totalVencido=0;
        Date hoje =new Date();
        DefaultTableModel modelo=montaTabelaRem();
        if(arquivosRem.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhum Aqruivo encontrado");
        }else
        {
            
             for(int i =0; i< arquivosRem.size(); i++){
               
                modelo.addRow(new Object[]{String.valueOf(arquivosRem.get(i).getCodigo()),arquivosRem.get(i).getBanco().getNome(),
                arquivosRem.get(i).getAgencia().getNumAgencia(),arquivosRem.get(i).getContaBancaria().getNumConta(),
                arquivosRem.get(i).getPadraCnab()==0? "": arquivosRem.get(i).getPadraCnab(),arquivosRem.get(i).getValorTotal()==0? " ":arquivosRem.get(i).getValorTotal(),
                arquivosRem.get(i).getDataRemessa(),Admin.formataStatus(arquivosRem.get(i).getStatus())});
                cont++;
             
            }
            TabelaRemessas.setModel(modelo);
            TableRowSorter<TableModel> sorter =new TableRowSorter<TableModel>(modelo);
            sorter.setComparator(6, comparator);
            TabelaRemessas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            TabelaRemessas.setRowSorter(sorter);
            TabelaRemessas.getColumnModel().getColumn(7).setCellRenderer(renderer);
            TabelaRemessas.setDefaultEditor(Object.class, null);
         
            conexao.fecharConexao();
        }
 }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        RbPadrao = new javax.swing.ButtonGroup();
        btnGerarArquivo = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTabProcurar = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtCodRemessa1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnFiltrar = new javax.swing.JButton();
        cbxSituacao = new javax.swing.JComboBox<>();
        cbxBanco = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelaRemessas = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtCodRemessa = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtDataRemessa = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        rbCnab240 = new javax.swing.JRadioButton();
        rbCnab400 = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnSalvar = new javax.swing.JButton();
        jtxtTotalPagar = new javax.swing.JFormattedTextField();
        txtTotalLancamentos = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtAgencia = new javax.swing.JTextField();
        txtBanco = new javax.swing.JTextField();
        txtConta = new javax.swing.JTextField();
        btnRemover = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaRemessaTitulo = new javax.swing.JTable();

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

        btnGerarArquivo.setBackground(new java.awt.Color(255, 255, 255));
        btnGerarArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/office-material (1).png"))); // NOI18N
        btnGerarArquivo.setText("<html>Gerar<br>Remessa</html>");
        btnGerarArquivo.setToolTipText("");
        btnGerarArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarArquivoActionPerformed(evt);
            }
        });

        btnSair.setBackground(new java.awt.Color(255, 255, 255));
        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-sair-26.png"))); // NOI18N
        btnSair.setText("Sair");
        btnSair.setToolTipText("");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Arquivo Remessa");
        jLabel1.setToolTipText("");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/resources/logopp.png"))); // NOI18N

        jTabProcurar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabProcurarStateChanged(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setText("Cod. Remessa:");

        txtCodRemessa1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtCodRemessa1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodRemessa1KeyPressed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Exibir Todos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        cbxSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "Aberto", "Fechado" }));
        cbxSituacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSituacaoActionPerformed(evt);
            }
        });

        cbxBanco.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbxBanco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxBancoActionPerformed(evt);
            }
        });

        jLabel15.setText("Situação:");

        jLabel2.setText("Banco:");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Remessas"));

        TabelaRemessas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo Remessa", "Banco", "Agencia", "Conta Bancaria", "Padrao CNAB", "Valor Total", "Data Remessa", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TabelaRemessas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelaRemessasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TabelaRemessas);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 712, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(txtCodRemessa1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(cbxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(73, 73, 73)
                        .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(jButton1)))
                .addContainerGap(183, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnFiltrar, jButton1});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cbxBanco, cbxSituacao, txtCodRemessa1});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel15)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodRemessa1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnFiltrar, jButton1});

        jTabProcurar.addTab("Procurar", jPanel1);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setText("Cod. Remessa");

        txtCodRemessa.setEditable(false);
        txtCodRemessa.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setText("Data Remessa:");

        jtxtDataRemessa.setEditable(false);
        jtxtDataRemessa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            jtxtDataRemessa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Padrão CNAB"));

        RbPadrao.add(rbCnab240);
        rbCnab240.setText("240");
        rbCnab240.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCnab240ActionPerformed(evt);
            }
        });

        RbPadrao.add(rbCnab400);
        rbCnab400.setText("400");
        rbCnab400.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCnab400ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(rbCnab240)
                .addGap(18, 18, 18)
                .addComponent(rbCnab400)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(rbCnab240)
                .addComponent(rbCnab400))
        );

        jLabel17.setText("Banco:");

        jLabel22.setText("Agência:");

        jLabel18.setText("Nº da conta:");

        btnSalvar.setBackground(new java.awt.Color(255, 255, 255));
        btnSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/icons8-descarregar-da-nuvem-26.png"))); // NOI18N
        btnSalvar.setText("<html>Fechar<br>Remessa</html");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        jtxtTotalPagar.setEditable(false);
        jtxtTotalPagar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jtxtTotalPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtTotalPagarActionPerformed(evt);
            }
        });

        txtTotalLancamentos.setEditable(false);
        txtTotalLancamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalLancamentosActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Nº Lançamentos");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Total a Pagar");

        txtAgencia.setEditable(false);
        txtAgencia.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtBanco.setEditable(false);
        txtBanco.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtConta.setEditable(false);
        txtConta.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnRemover.setBackground(new java.awt.Color(255, 255, 255));
        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/tg/icon/minus.png"))); // NOI18N
        btnRemover.setText("<html>Remover<br>Titulo</html>");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Titulos"));

        TabelaRemessaTitulo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Item", "Nº Lançamento", "Nº Parcela", "Fornecedor", "Vencimento", "Data Pagamento", "Valor título", "Juros", "Multa", "Valor a Pagar", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelaRemessaTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelaRemessaTituloMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelaRemessaTitulo);
        if (TabelaRemessaTitulo.getColumnModel().getColumnCount() > 0) {
            TabelaRemessaTitulo.getColumnModel().getColumn(0).setMinWidth(50);
            TabelaRemessaTitulo.getColumnModel().getColumn(0).setMaxWidth(50);
            TabelaRemessaTitulo.getColumnModel().getColumn(10).setMinWidth(120);
            TabelaRemessaTitulo.getColumnModel().getColumn(10).setMaxWidth(120);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBanco)
                            .addComponent(jLabel8)
                            .addComponent(txtCodRemessa, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jtxtDataRemessa, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtAgencia, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel22))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(txtConta, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel17))
                .addGap(88, 88, 88)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(243, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTotalLancamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtxtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtxtDataRemessa, txtBanco, txtCodRemessa, txtConta});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnRemover, btnSalvar});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodRemessa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtDataRemessa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                        .addComponent(btnRemover, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel22)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtTotalLancamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(59, 59, 59))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jtxtDataRemessa, txtAgencia, txtBanco, txtCodRemessa, txtConta});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnRemover, btnSalvar});

        jTabProcurar.addTab("Remessa", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnGerarArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnGerarArquivo, btnSair});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGerarArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(273, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGerarArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarArquivoActionPerformed
        if(TabelaRemessas.getSelectedRowCount()==0){
            
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Arquivo a ser Gerado ", "Aviso",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        else{
            Admin admin= new Admin();
            int indiceLinha = TabelaRemessas.getSelectedRow(); 
            if(String.valueOf(TabelaRemessas.getValueAt(indiceLinha, 7)).equals("Aberto")){
                JOptionPane.showMessageDialog(this, "O Arquivo remessa preceisa ser fechado");
            }else
            {
               JFileChooser salvandoArquivo = new JFileChooser();
               salvandoArquivo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
               FileNameExtensionFilter filter= new FileNameExtensionFilter("Remessa","REM");
               salvandoArquivo.setFileFilter(filter);
               salvandoArquivo.setSelectedFile(new File("Remessa.REM"));
                int resultado = salvandoArquivo.showSaveDialog(this);
                if (resultado != JFileChooser.APPROVE_OPTION) 
                {
                    return;
                }   
                    File salvarArquivoEscolhido = salvandoArquivo.getSelectedFile();
                    
                    if ( salvarArquivoEscolhido.exists() == true )
                    {
                        int selecionaOpcao = JOptionPane.showConfirmDialog(null,
						" O arquivo já existe, deseja sobrescreve-lo? ", null,
						JOptionPane.OK_CANCEL_OPTION);
                        if (selecionaOpcao == JOptionPane.OK_OPTION) 
                        {
                            
                            PrintWriter pw = null;
                            try 
                            {
                                pw = new PrintWriter(new FileWriter(salvarArquivoEscolhido));

                                pw.println( constroiHeader(Integer.parseInt(""+TabelaRemessas.getValueAt(indiceLinha, 0))));
                                int i=0;
                                for( i=0;i<titulosParcela.size();i++){
                                    System.out.println(constroiDetalhe(Integer.parseInt(""+TabelaRemessas.getValueAt(indiceLinha, 0)),titulosParcela.get(i),i+2));
                                     pw.println( constroiDetalhe(Integer.parseInt(""+TabelaRemessas.getValueAt(indiceLinha, 0)),titulosParcela.get(i),i+2));
                                }
                                pw.println( constroiTrailler(i+2));
                                pw.close();
                            }    
                            catch (IOException ex) {
                                Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(null, "Arquivo gerado com  sucesso!");
                            }
       
                        }
                    }
                    else
                    {
                            
                        PrintWriter pw = null;
                        try 
                        {
                            pw = new PrintWriter(new FileWriter(salvarArquivoEscolhido));
                            pw.println( constroiHeader(Integer.parseInt(""+TabelaRemessas.getValueAt(indiceLinha, 0))));
                            int i=0;
                            for( i=0;i<titulosParcela.size();i++){
                                System.out.println(constroiDetalhe(Integer.parseInt(""+TabelaRemessas.getValueAt(indiceLinha, 0)),titulosParcela.get(i),i+2));
                                pw.println( constroiDetalhe(Integer.parseInt(""+TabelaRemessas.getValueAt(indiceLinha, 0)),titulosParcela.get(i),i+2));
                            }
                            pw.println( constroiTrailler(i+2));
                            pw.close();
                            JOptionPane.showMessageDialog(null, "Arquivo gerado com  sucesso!");
                        }    
                        catch (IOException ex) 
                        {
                            Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);          
                            JOptionPane.showMessageDialog(null, " O arquivo nao pode ser salvo. ");
                        }  
                        
                    }       
                
            }
        }       
    }//GEN-LAST:event_btnGerarArquivoActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
       dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void rbCnab240ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCnab240ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbCnab240ActionPerformed

    private void rbCnab400ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCnab400ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbCnab400ActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        conexao = new Conexao();
        daoFornecedor = new DaoFornecedor(conexao.conectar());
        daoFornecPF = new DaoFornecPF(conexao.conectar());
        daoFornecPJ = new DaoFornecPJ(conexao.conectar());
        daoTitulo =new DaoTitulo(conexao.conectar());
        daoTituloParcela=new DaoTituloParcela(conexao.conectar());
        daoBanco=new DaoBanco(conexao.conectar());
        daotpPagamento=new DaoTpPagamento(conexao.conectar());
        daoArquivoRemessa=new DaoArquivoRemessa(conexao.conectar());
        daoRemessaTitulo= new DaoRemessaTitulo((conexao.conectar()));
        ArrayList<Banco> bancos = daoBanco.listarBancos();
        for (int x=0; x<bancos.size(); x++){
           cbxBanco.addItem(bancos.get(x).getNome()); 
        }   
        arquivosRem = daoArquivoRemessa.listarArqRem();
        try {
            ExibirRem(arquivosRem);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formInternalFrameOpened

    private void jTabProcurarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabProcurarStateChanged
        if(jTabProcurar.getSelectedIndex()==1 ){
          if(TabelaRemessas.getSelectedRowCount()==0){
            jTabProcurar.setSelectedIndex(0);
            Object[]options = { "OK"};
            JOptionPane.showOptionDialog(null, "Selecione um Arquivo a consultar ", "Aviso",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        else
        {
            jTabProcurar.setSelectedIndex(1);
            int indiceLinha = TabelaRemessas.getSelectedRow(); 
            int cod=0;
            jTabProcurar.setSelectedIndex(1);
            cod=Integer.parseInt(""+TabelaRemessas.getValueAt(indiceLinha, 0));
            titulosParcela=daoTituloParcela.listarTitulosRemessa(cod);
            txtCodRemessa.setText((String) TabelaRemessas.getValueAt(indiceLinha, 0));
            jtxtDataRemessa.setText((String) TabelaRemessas.getValueAt(indiceLinha, 6));
            txtAgencia.setText(String.valueOf(TabelaRemessas.getValueAt(indiceLinha, 2)));
            txtBanco.setText(String.valueOf(TabelaRemessas.getValueAt(indiceLinha, 1)));
            txtConta.setText(String.valueOf(TabelaRemessas.getValueAt(indiceLinha, 3)));
            arquivoRemessa=daoArquivoRemessa.consultar(cod);
            if(arquivoRemessa.getPadraCnab()==240)
                rbCnab240.isSelected();
            else if(arquivoRemessa.getPadraCnab()==400)
                rbCnab400.isSelected();
            if(arquivoRemessa.getStatus().equals("F")){
                rbCnab240.setEnabled(false);
                rbCnab400.setEnabled(false);
                btnSalvar.setEnabled(false);
                btnRemover.setEnabled(false);
            }
            else{
                 rbCnab240.setEnabled(true);
                rbCnab400.setEnabled(true);
                btnSalvar.setEnabled(true);
                btnRemover.setEnabled(true);
            }
           
              try {
                  Exibir(titulosParcela);
              } catch (ParseException ex) {
                  Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
              }
            
        }
      }
    }//GEN-LAST:event_jTabProcurarStateChanged

    private void jtxtTotalPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtTotalPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtTotalPagarActionPerformed

    private void txtTotalLancamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalLancamentosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalLancamentosActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
       
        if(rbCnab240.isSelected()==false && rbCnab400.isSelected()==false){
             JOptionPane.showMessageDialog(this, "Selecione um tipo padrão CNAB!");
        }else{
            
            arquivoRemessa=daoArquivoRemessa.consultar(Integer.parseInt(txtCodRemessa.getText()));
            if(rbCnab240.isSelected())
              arquivoRemessa.setPadraCnab(240);
            else
                arquivoRemessa.setPadraCnab(400);
            try {
                arquivoRemessa.setValorTotal(converte(jtxtTotalPagar.getText()));
            } catch (ParseException ex) {
                Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
            }
                arquivoRemessa.setStatus("F");
               daoArquivoRemessa.fechar(arquivoRemessa);
            JOptionPane.showMessageDialog(this, "Arquivo Remessa Finalizado");
             jTabProcurar.setSelectedIndex(0);
              arquivosRem = daoArquivoRemessa.listarArqRem();
             try {
                  ExibirRem(arquivosRem);
              } catch (ParseException ex) {
                  Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
              }
        }
        
        
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
       Icon figura = new ImageIcon (getToolkit().createImage(getClass().getResource("/fatec/tg/icon/minusg.png")));

        if(TabelaRemessaTitulo.getSelectedRowCount()==0){
            Object[]options = { "OK","Cancelar"};
            JOptionPane.showOptionDialog(null, "Selecione um Título a ser Removido ", "Aviso de Remoção",
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            figura, options, options[0]);
        }
        else
        {   
            int indiceLinha = TabelaRemessaTitulo.getSelectedRow();
            int cod=0,parc=0;
            cod=Integer.parseInt(""+TabelaRemessaTitulo.getValueAt(indiceLinha, 1));
            parc=Integer.parseInt(""+TabelaRemessaTitulo.getValueAt(indiceLinha, 2));
            arquivoRemessa=new ArquivoRemessa(Integer.parseInt(txtCodRemessa.getText()));
            tituloParcela=new TituloParcela(cod);
            tituloParcela.setNumParcela(parc);
            tituloParcela.setDataPag("");
            tituloParcela.setValorPago(0);
            tituloParcela.setStatusPag("N");
            tituloParcela.setTipoPagamento(tipoPagamento);
            
            remessaTitulo=new RemessaTitulo(arquivoRemessa, tituloParcela);
            
            //JOptionPane.showMessageDialog(this, "Titulo Removido com Sucesso!");
             Object[]options = { "Sim","Cancelar"};
            if(JOptionPane.showOptionDialog(null, "Clique em Sim para confirmar a Remossao do TiTulo : "+ 
                                cod +" Parcela: "+parc,
                                "Aviso de Remoção",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                figura, options, options[0])==0) {
                daoRemessaTitulo.excluir(remessaTitulo);
                daoTituloParcela.PagarRemessa(tituloParcela);
                titulosParcela=daoTituloParcela.listarTitulosRemessa(Integer.parseInt(txtCodRemessa.getText()));
                try {
                    Exibir(titulosParcela);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
            
        }
      
        
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     arquivosRem = daoArquivoRemessa.listarArqRem();
        try {
            ExibirRem(arquivosRem);
        } catch (ParseException ex) {
            Logger.getLogger(GuiContasAPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCodRemessa1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodRemessa1KeyPressed
       if(evt.getKeyCode() == KeyEvent.VK_ENTER){
           try {
               listarPorCodigo();
           } catch (ParseException ex) {
               Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }//GEN-LAST:event_txtCodRemessa1KeyPressed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
       if( txtCodRemessa1.getText().isEmpty() ){
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

    private void cbxSituacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSituacaoActionPerformed
        if(cbxSituacao.getSelectedIndex()>0){
            arquivosRem=daoArquivoRemessa.listarSituacao(cbxSituacao.getSelectedIndex());
            try {
                ExibirRem(arquivosRem);
            } catch (ParseException ex) {
                Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_cbxSituacaoActionPerformed

    private void cbxBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxBancoActionPerformed
        if(cbxBanco.getSelectedIndex()>0){
            Banco banco=daoBanco.consultarNome((String) cbxBanco.getSelectedItem());
            arquivosRem=daoArquivoRemessa.listarCodBanco(banco.getCodBanco());
            try {
                ExibirRem(arquivosRem);
            } catch (ParseException ex) {
                Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_cbxBancoActionPerformed

    private void TabelaRemessaTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelaRemessaTituloMouseClicked
        
    }//GEN-LAST:event_TabelaRemessaTituloMouseClicked

    private void TabelaRemessasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelaRemessasMouseClicked
        jTabProcurar.setSelectedIndex(1);
    }//GEN-LAST:event_TabelaRemessasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup RbPadrao;
    private javax.swing.JTable TabelaRemessaTitulo;
    private javax.swing.JTable TabelaRemessas;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnGerarArquivo;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cbxBanco;
    private javax.swing.JComboBox<String> cbxSituacao;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabProcurar;
    private javax.swing.JFormattedTextField jtxtDataRemessa;
    private javax.swing.JFormattedTextField jtxtTotalPagar;
    private javax.swing.JRadioButton rbCnab240;
    private javax.swing.JRadioButton rbCnab400;
    private javax.swing.JTextField txtAgencia;
    private javax.swing.JTextField txtBanco;
    private javax.swing.JTextField txtCodRemessa;
    private javax.swing.JTextField txtCodRemessa1;
    private javax.swing.JTextField txtConta;
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
    private DaoAgencia daoAgencia=null;
    private Agencia agencia=null;
    private DaoContaBancaria daoContaBancaria=null;
    private ContaBancaria contaBancaria=null;
    private Banco banco=null;
    private int numLanc=0;
    private int numParc=0;
    private int codBanco=0;
    private int numAgencia=0;
    private int contab=0;
    private TipoPagamento tipoPagamento=null;
    private DaoTpPagamento daotpPagamento=null;
    private DaoArquivoRemessa daoArquivoRemessa=null;
    private ArquivoRemessa arquivoRemessa=null;
    private RemessaTitulo remessaTitulo=null;
    private DaoRemessaTitulo daoRemessaTitulo=null;
    private ArrayList<ArquivoRemessa> arquivosRem;
    private ArrayList<TituloParcela> titulosParcela;
}
