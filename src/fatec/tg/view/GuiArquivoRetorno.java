/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.tg.view;

import fatec.tg.controle.Admin;
import fatec.tg.controle.Conexao;
import fatec.tg.controle.DaoAgencia;
import fatec.tg.controle.DaoArquivoRemessa;
import fatec.tg.controle.DaoArquivoRetorno;
import fatec.tg.controle.DaoBanco;
import fatec.tg.controle.DaoContaBancaria;
import fatec.tg.controle.DaoFornecPF;
import fatec.tg.controle.DaoFornecPJ;
import fatec.tg.controle.DaoFornecedor;
import fatec.tg.controle.DaoRemessaTitulo;
import fatec.tg.controle.DaoRetornoTitulo;
import fatec.tg.controle.DaoTitulo;
import fatec.tg.controle.DaoTituloParcela;
import fatec.tg.controle.DaoTpPagamento;
import fatec.tg.model.Agencia;
import fatec.tg.model.ArquivoRemessa;
import fatec.tg.model.ArquivoRetorno;
import fatec.tg.model.Banco;
import fatec.tg.model.ConciliacaoBancaria;
import fatec.tg.model.ContaBancaria;
import fatec.tg.model.FornecFisica;
import fatec.tg.model.FornecJuridica;
import fatec.tg.model.Fornecedor;
import fatec.tg.model.RemessaTitulo;
import fatec.tg.model.RetornoTitulo;
import fatec.tg.model.TipoPagamento;
import fatec.tg.model.Titulo;
import fatec.tg.model.TituloParcela;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileNotFoundException;
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

/**
 *
 * @author Bia Delmont
 */
public class GuiArquivoRetorno extends javax.swing.JInternalFrame {
 private static GuiArquivoRetorno guiArquivoRetorno;
    public static GuiArquivoRetorno getInstancia(){
        if(guiArquivoRetorno==null){
            guiArquivoRetorno = new GuiArquivoRetorno();
           
        }
        return guiArquivoRetorno;
    }
   
    public GuiArquivoRetorno() {
        initComponents();
    }
     public void listarPorCodigo() throws ParseException{
        arquivosRet=daoArquivoRetorno.listarCod(Integer.parseInt(txtCodigo.getText()));
        ExibirRet(arquivosRet);
    }
    public DefaultTableModel montaTabela(){
      DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Codigo");
            modelo.addColumn("Data");
            modelo.addColumn("Padrão");
            modelo.addColumn("Banco");
            modelo.addColumn("Agencia");
            modelo.addColumn("Conta");
            modelo.addColumn("Status");

     return (modelo);
 }
        public DefaultTableModel montaTabelaRetTitulos(){
      DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("N°Item");
            modelo.addColumn("N° Lançamento");
            modelo.addColumn("N° Parcela");
            modelo.addColumn("Fornececedor");
            modelo.addColumn("Nosso Num");
            modelo.addColumn("Vencimento");
            modelo.addColumn("Data Pag");
            modelo.addColumn("Valor Titulo");
            modelo.addColumn("Valor Pago");
            modelo.addColumn("Status");
            

     return (modelo);
 }
     DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
        String estado = table.getModel().getValueAt(row,6).toString();
      
      
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
      DefaultTableCellRenderer rendererTitulo = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
        String estado = table.getModel().getValueAt(row,9).toString();
      
      
        if (estado.contains("rejeitada")) {
            c.setForeground(Color.RED);
        } 
        else
        {
            c.setForeground(new Color(60,179,113));  
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
       public void Exibir(ArrayList<TituloParcela>titulosParcela) throws ParseException{
     conexao = new Conexao();
     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
     int cont=0;
     double total=0.0;
     double totalPago=0;
    
        DefaultTableModel modelo=montaTabelaRetTitulos();
        if(titulosParcela.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhum Titulo encontrado");

        }else{
            
          for(int i =0; i< titulosParcela.size(); i++){
              retornoTitulo=daoRetornoTitulo.consultar(arquivoRetorno, titulosParcela.get(i));
              
                Date hoje = null;
                if(titulosParcela.get(i).getDataPag()!=null){
                        hoje =Admin.formataData(titulosParcela.get(i).getDataPag());
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
                }
               if(Admin.formataStatusRetTitulo(retornoTitulo.getStatus()).contains("rejeitada")){
                   titulosParcela.get(i).setDataPag("");
                   titulosParcela.get(i).setValorPago(0);
                   
               }
                 
                modelo.addRow(new Object[]{i+1,String.valueOf(titulosParcela.get(i).getNumLancamento()),titulosParcela.get(i).getNumParcela()
                        ,titulosParcela.get(i).getFornecFisica()==null? titulosParcela.get(i).getFornecJuridica().getNomeFan(): titulosParcela.get(i).getFornecFisica().getNome(),
                        titulosParcela.get(i).getNossoNum(),titulosParcela.get(i).getDataVenc(),titulosParcela.get(i).getDataPag()==""? "        -":titulosParcela.get(i).getDataPag(),
                        titulosParcela.get(i).getValorParcela(),titulosParcela.get(i).getValorPago()==0? "       -":titulosParcela.get(i).getValorPago(),Admin.formataStatusRetTitulo(retornoTitulo.getStatus())+retornoTitulo.getStatus()});
               
                cont++;
              total=total+totalPago;
              
            }
        }
         TabelaRetornoTitulo.setModel(modelo);
          TableRowSorter<TableModel> sorter =new TableRowSorter<TableModel>(modelo);
           sorter.setComparator(4, comparator);
           sorter.setComparator(5, comparator);
           sorter.setComparator(6, comparatorDouble);
           sorter.setComparator(9, comparatorDouble);
           TabelaRetornoTitulo.setRowSorter(sorter);
           TabelaRetornoTitulo.getColumnModel().getColumn(0).setPreferredWidth(50);
           TabelaRetornoTitulo.getColumnModel().getColumn(1).setPreferredWidth(70);
           TabelaRetornoTitulo.getColumnModel().getColumn(2).setPreferredWidth(70);
           TabelaRetornoTitulo.getColumnModel().getColumn(9).setPreferredWidth(220);
         TabelaRetornoTitulo.setDefaultEditor(Object.class, null);
         TabelaRetornoTitulo.getColumnModel().getColumn(9).setCellRenderer(rendererTitulo);
         conexao.fecharConexao();
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
       public void ExibirRet(ArrayList<ArquivoRetorno>arquivosRet) throws ParseException{
     conexao = new Conexao();

        DefaultTableModel modelo=montaTabela();
        if(arquivosRet.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nenhuma arquivo retorno foi encontrado");
            
        }else{
            
          for(int i =0; i< arquivosRet.size(); i++){
            
                modelo.addRow(new Object[]{String.valueOf(arquivosRet.get(i).getCodigo()),arquivosRet.get(i).getDataRetorno(),
                        arquivosRet.get(i).getPadraCnab(),this.arquivosRet.get(i).getBanco().getNome(),
                        arquivosRet.get(i).getAgencia().getNumAgencia(),arquivosRet.get(i).getContaBancaria().getNumConta(),Admin.formataStatusArqRet(arquivosRet.get(i).getStatus())});
            

              
            }
        }
           TabelaArquivoRetorno.setModel(modelo);
           TableRowSorter<TableModel> sorter =new TableRowSorter<TableModel>(modelo);
           sorter.setComparator(1, comparator);
           TabelaArquivoRetorno.setRowSorter(sorter);
           TabelaArquivoRetorno.setDefaultEditor(Object.class, null);
           TabelaArquivoRetorno.getColumnModel().getColumn(6).setCellRenderer(renderer);
           conexao.fecharConexao();
 }
       public void listarArqRet(){
            arquivosRet=daoArquivoRetorno.listarArqRet();
        try {
            ExibirRet(arquivosRet);
        } catch (ParseException ex) {
            Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void lerArquivo(String caminho)throws FileNotFoundException, ParseException{
       
        Scanner s = new Scanner(new File(caminho));
        int i=0;
        String linha=s.nextLine();
        if(linha.length()==400 && linha.substring(2,9).equals("RETORNO")){
            while(s.hasNextLine()){
                
                System.out.println(linha.length());
                System.out.println( linha.substring(2,9));

                    if(i==0){//cabecalho
                         System.out.println( linha.substring(76,79));
                          System.out.println( linha.substring(26,30));
                           System.out.println( linha.substring(32,38));

                           contaBancaria=daoContaBancaria.consultar(Integer.parseInt(linha.substring(76,79)), Integer.parseInt(linha.substring(26,30)), Integer.parseInt(linha.substring(32,38)));
                           banco=daoBanco.consultar(Integer.parseInt(linha.substring(76,79)));
                           agencia=daoAgencia.consultar(banco.getCodBanco(),Integer.parseInt(linha.substring(26,30)));
                           System.out.println("conta"+contaBancaria.getNumConta());
                           arquivoRetorno = new ArquivoRetorno(banco.getCodBanco());
                           arquivoRetorno.setCodigo(daoArquivoRetorno.obterCodArqRet()+1);
                           arquivoRetorno.setBanco(banco);
                           arquivoRetorno.setAgencia(agencia);
                           arquivoRetorno.setContaBancaria(contaBancaria);
                           arquivoRetorno.setPadraCnab(400);
                           arquivoRetorno.setStatus("P");
                           Date data=Admin.formataData2(linha.substring(94,100));
                           SimpleDateFormat formatador = new SimpleDateFormat("ddMMYYYY");
                           arquivoRetorno.setDataRetorno(formatador.format(data));
                           daoArquivoRetorno.inserir(arquivoRetorno);

                    }
                    else if(i>0 && linha.substring( 0,1).equals("9")==false){
                       
                        
                        System.out.println("Numlanc "+linha.substring(44, 50));
                        System.out.println("Numparc "+linha.substring(57, 62));
                        int numLanc=Integer.parseInt(linha.substring(44, 50));
                        int numParc=Integer.parseInt(linha.substring(57, 62));
                        int ocorrencia=Integer.parseInt(linha.substring(108, 110));
                        tituloParcela= daoTituloParcela.consultar(numLanc, numParc);
                        retornoTitulo= new RetornoTitulo(arquivoRetorno,tituloParcela );
                        if(ocorrencia==15){
                            ArrayList ocorrencias = new ArrayList();
                            int oc1=Integer.parseInt(linha.substring(377,379));
                            int oc2=Integer.parseInt(linha.substring(379,381));
                            int oc3=Integer.parseInt(linha.substring(381,383));
                            int oc4=Integer.parseInt(linha.substring(383,385));
                            System.out.println("ocorrencia1 "+oc1);
                            System.out.println("ocorrencia2 "+oc2);
                            System.out.println("ocorrencia3 "+oc3);
                            System.out.println("ocorrencia4 "+oc4);
                            if(oc1!=0&& oc2!=0 && oc3!=0 && oc4!=0){
                                retornoTitulo.setStatus(oc1+";"+oc2+";"+oc3+";"+oc4);
                             }
  
                         }
                        else{
                            retornoTitulo.setStatus(String.valueOf(ocorrencia));
                        }
                        daoRetornoTitulo.inserir(retornoTitulo);
                        
                    }
                    i++; 
                    linha=s.nextLine();
            }
             JOptionPane.showMessageDialog(this, "Arquivo importado com sucesso!");
             listarArqRet();
        }
        else{
             JOptionPane.showMessageDialog(this, "Arquivo não suportado");
                
            }
            
          
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnConciliar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        JTabProcurar = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtLocalArquivo = new javax.swing.JTextField();
        btnProcurar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxBanco = new javax.swing.JComboBox<>();
        btnExibirTodos = new javax.swing.JButton();
        btnFiltrar = new javax.swing.JButton();
        cbxSituacao = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaArquivoRetorno = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TabelaRetornoTitulo = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        btnImportar = new javax.swing.JButton();

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
        jLabel1.setText("Arquivo Retorno");

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

        JTabProcurar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                JTabProcurarStateChanged(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Local arquivo:");

        txtLocalArquivo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnProcurar.setBackground(new java.awt.Color(255, 255, 255));
        btnProcurar.setText("Procurar arquivo ");
        btnProcurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filtro"));

        txtCodigo.setEditable(false);
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

        jLabel4.setText("Banco:");

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

        cbxSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        cbxSituacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSituacaoActionPerformed(evt);
            }
        });

        jLabel9.setText("Situação:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExibirTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExibirTodos, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFiltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Arquivos"));

        TabelaArquivoRetorno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", "", "", "", ""},
                {"", "", "", "", "", "", ""},
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
                "Codigo", "Data", "Padrao", "Banco", "Agencia", "Conta Bancaria", "Status"
            }
        ));
        TabelaArquivoRetorno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelaArquivoRetornoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelaArquivoRetorno);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(txtLocalArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(352, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLocalArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        JTabProcurar.addTab("Procurar", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Titulos Importados Arquivo Retorno"));

        TabelaRetornoTitulo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Item", "Nº Lançamento", "Nº Parcela", "Fornecedor", "Nosso Numero", "Vencimento", "Data Pagamento", "Valor título", "Valor Pago", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelaRetornoTitulo.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(TabelaRetornoTitulo);
        if (TabelaRetornoTitulo.getColumnModel().getColumnCount() > 0) {
            TabelaRetornoTitulo.getColumnModel().getColumn(0).setMinWidth(50);
            TabelaRetornoTitulo.getColumnModel().getColumn(0).setPreferredWidth(50);
            TabelaRetornoTitulo.getColumnModel().getColumn(0).setMaxWidth(50);
            TabelaRetornoTitulo.getColumnModel().getColumn(1).setMinWidth(60);
            TabelaRetornoTitulo.getColumnModel().getColumn(1).setPreferredWidth(60);
            TabelaRetornoTitulo.getColumnModel().getColumn(1).setMaxWidth(60);
            TabelaRetornoTitulo.getColumnModel().getColumn(2).setMinWidth(60);
            TabelaRetornoTitulo.getColumnModel().getColumn(2).setPreferredWidth(60);
            TabelaRetornoTitulo.getColumnModel().getColumn(2).setMaxWidth(60);
            TabelaRetornoTitulo.getColumnModel().getColumn(3).setMinWidth(70);
            TabelaRetornoTitulo.getColumnModel().getColumn(3).setPreferredWidth(70);
            TabelaRetornoTitulo.getColumnModel().getColumn(3).setMaxWidth(70);
            TabelaRetornoTitulo.getColumnModel().getColumn(4).setMinWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(4).setPreferredWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(4).setMaxWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(5).setMinWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(5).setPreferredWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(5).setMaxWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(6).setMinWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(6).setPreferredWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(6).setMaxWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(7).setMinWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(7).setPreferredWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(7).setMaxWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(8).setMinWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(8).setPreferredWidth(80);
            TabelaRetornoTitulo.getColumnModel().getColumn(8).setMaxWidth(80);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        JTabProcurar.addTab(" Retorno", jPanel2);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/resources/logopp.png"))); // NOI18N

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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel11)
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnImportar)
                            .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnConciliar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTabProcurar, javax.swing.GroupLayout.DEFAULT_SIZE, 1079, Short.MAX_VALUE))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnConciliar, btnImportar, btnSair});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnImportar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConciliar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(206, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTabProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnConciliar, btnImportar, btnSair});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConciliarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConciliarActionPerformed
        if(TabelaArquivoRetorno.getSelectedRowCount()==0){
                      JTabProcurar.setSelectedIndex(0);
                      Object[]options = { "OK"};
                      JOptionPane.showOptionDialog(null, "Selecione um arquivo retorno a conciliar ", "Aviso",
                      JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                      null, options, options[0]);
        }
        else{
             if( arquivoRetorno.getStatus().equals("P"))
            {
                Icon figura = new ImageIcon (getToolkit().createImage(getClass().getResource("/fatec/tg/icon/chat.png")));
                Object[]options = { "Sim","Não"};
                if(JOptionPane.showOptionDialog(null, "Deseja conciliar o Arquivo retorno: "+ arquivoRetorno.getCodigo(), "Aviso de Conciliação",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        figura, options, options[0])==0)
                {
                     for(int i =0; i< titulosParcela.size(); i++)
                     {
                         retornoTitulo=daoRetornoTitulo.consultar(arquivoRetorno, titulosParcela.get(i));
                        if(retornoTitulo.getStatus().equals("9")||retornoTitulo.getStatus().equals("10")){
                            titulosParcela.get(i).setStatusPag("P");
                            daoTituloParcela.PagarRemessa(titulosParcela.get(i));
                            contaBancaria=arquivoRetorno.getContaBancaria();
                            contaBancaria.setSaldo(contaBancaria.getSaldo()-titulosParcela.get(i).getValorPago());
                            daoContaBancaria.AtualizarSaldo(contaBancaria);
                        }
                        else{
                            daoTituloParcela.cancelarPag(titulosParcela.get(i));
                        }
   
                     }
                arquivoRetorno.setStatus("C");
                daoArquivoRetorno.conciliar(arquivoRetorno);
                JOptionPane.showMessageDialog(this, "Arquivo retorno conciliado com sucesso!"+"\n"+ "Obs: Titulos com baixa rejeitada será reinserido à lista de contas a pagar!");
                JTabProcurar.setSelectedIndex(0);
                listarArqRet();
                }
               
            }
             else{
                 JOptionPane.showMessageDialog(this, "Este arquivo já foi conciliado!");
                 JTabProcurar.setSelectedIndex(0);
             }
                 
             
        }
        
        
    }//GEN-LAST:event_btnConciliarActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
       dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
  
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void cbxBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxBancoActionPerformed
        if(cbxBanco.getSelectedIndex()>0){
            Banco banco=daoBanco.consultarNome((String) cbxBanco.getSelectedItem());
            arquivosRet=daoArquivoRetorno.listarCodBanco(banco.getCodBanco());
            try {
                  ExibirRet(arquivosRet);
            } catch (ParseException ex) {
                  Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cbxBancoActionPerformed

    private void btnExibirTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExibirTodosActionPerformed
        listarArqRet();
    }//GEN-LAST:event_btnExibirTodosActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void cbxSituacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSituacaoActionPerformed
         if(cbxSituacao.getSelectedIndex()>0){
            arquivosRet=daoArquivoRetorno.listarSituacao(cbxSituacao.getSelectedIndex());
      try {
          ExibirRet(arquivosRet);
      } catch (ParseException ex) {
          Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
      }
        }
    }//GEN-LAST:event_cbxSituacaoActionPerformed

    private void btnProcurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter= new FileNameExtensionFilter("Retorno","ret");
        fc.setFileFilter(filter);
        fc.showOpenDialog(this);
        File f = null;
        f  = fc.getSelectedFile();
        if(f!=null){
         txtLocalArquivo.setText(f.getPath());
        }
    }//GEN-LAST:event_btnProcurarActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        conexao = new Conexao();
        daoFornecedor = new DaoFornecedor(conexao.conectar());
        daoFornecPF = new DaoFornecPF(conexao.conectar());
        daoFornecPJ = new DaoFornecPJ(conexao.conectar());
        daoTitulo =new DaoTitulo(conexao.conectar());
        daoTituloParcela=new DaoTituloParcela(conexao.conectar());
        daoBanco=new DaoBanco(conexao.conectar());
        daoContaBancaria= new DaoContaBancaria(conexao.conectar());
        daoAgencia = new DaoAgencia(conexao.conectar());
        daotpPagamento=new DaoTpPagamento(conexao.conectar());
        daoArquivoRetorno=new DaoArquivoRetorno(conexao.conectar());
        daoRetornoTitulo= new DaoRetornoTitulo((conexao.conectar()));
        ArrayList<Banco> bancos = daoBanco.listarBancos();
        for (int x=0; x<bancos.size(); x++){
           cbxBanco.addItem(bancos.get(x).getNome()); 
        }   
        arquivosRet = daoArquivoRetorno.listarArqRet();
         
        try {
            ExibirRet(arquivosRet);
        } catch (ParseException ex) {
            Logger.getLogger(GuiConciliacaoBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportarActionPerformed
         if(txtLocalArquivo.getText().isEmpty()){
           JOptionPane.showMessageDialog(this, "Escolha um arquivo ofc a ser importado!");
      }
      else{
          try 
           {
                try 
                {
                    lerArquivo(txtLocalArquivo.getText());
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
          txtLocalArquivo.setText("");
      }
        
    }//GEN-LAST:event_btnImportarActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
          if(evt.getKeyCode() == KeyEvent.VK_ENTER){
           try {
               listarPorCodigo();
           } catch (ParseException ex) {
               Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void JTabProcurarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_JTabProcurarStateChanged
       if(JTabProcurar.getSelectedIndex()==1 ){
            if(TabelaArquivoRetorno.getSelectedRowCount()==0){
              JTabProcurar.setSelectedIndex(0);
              Object[]options = { "OK"};
              JOptionPane.showOptionDialog(null, "Selecione um arquivo retorno a consultar ", "Aviso",
              JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
              null, options, options[0]);
          }
            else{
                int indiceLinha = TabelaArquivoRetorno.getSelectedRow(); 
                int cod=Integer.parseInt(""+TabelaArquivoRetorno.getValueAt(indiceLinha, 0));
                arquivoRetorno=daoArquivoRetorno.consultar(cod);
                titulosParcela=daoTituloParcela.listarTitulosRetorno(cod);
                JTabProcurar.setSelectedIndex(1);
                 try {
                  Exibir(titulosParcela);
              } catch (ParseException ex) {
                  Logger.getLogger(GuiArquivoRemessas.class.getName()).log(Level.SEVERE, null, ex);
              }
               
            }
       }
    }//GEN-LAST:event_JTabProcurarStateChanged

    private void TabelaArquivoRetornoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelaArquivoRetornoMouseClicked
        JTabProcurar.setSelectedIndex(1);
    }//GEN-LAST:event_TabelaArquivoRetornoMouseClicked
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
            java.util.logging.Logger.getLogger(GuiArquivoRetorno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiArquivoRetorno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiArquivoRetorno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiArquivoRetorno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiArquivoRetorno().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane JTabProcurar;
    private javax.swing.JTable TabelaArquivoRetorno;
    private javax.swing.JTable TabelaRetornoTitulo;
    private javax.swing.JButton btnConciliar;
    private javax.swing.JButton btnExibirTodos;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImportar;
    private javax.swing.JButton btnProcurar;
    private javax.swing.JButton btnSair;
    private javax.swing.JComboBox<String> cbxBanco;
    private javax.swing.JComboBox<String> cbxSituacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtLocalArquivo;
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

    private int codBanco=0;
    private int numAgencia=0;
    private int contab=0;
    private TipoPagamento tipoPagamento=null;
    private DaoTpPagamento daotpPagamento=null;
    private ArquivoRetorno arquivoRetorno=null;
    private RetornoTitulo retornoTitulo=null;
    private DaoRetornoTitulo daoRetornoTitulo=null;
  
    private DaoArquivoRetorno daoArquivoRetorno=null;
    private ArrayList<ArquivoRetorno> arquivosRet=null;
    private ArrayList<TituloParcela> titulosParcela=null;
}
