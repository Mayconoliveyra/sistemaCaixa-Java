/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao;

import Controle.ConectaBanco;
import Controle.ControleAddProdutoNotaFiscal;
import Controle.ControleCeantribCeanimport;
import Controle.ControleImportNotaFiscalXML;
import Controle.ControleProdutoComNota;
import Controle.ControleProdutoSemNota;
import Controle.ControleUnidadeMedida;
import Controle.ModeloTabela;
import Modelo.ModeloCeantribCeanImport;
import Modelo.ModeloProdutoComNota;
import Modelo.ModeloProdutoSemNota;
import Modelo.ModeloUnidadeMedida;
import com.lowagie.text.Font;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public final class FrmProduto extends javax.swing.JInternalFrame {

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    StringUtils StringUtil = new StringUtils();

    ConectaBanco conecBanc = new ConectaBanco();
    ConectaBanco conecPesquisa = new ConectaBanco();
    ConectaBanco conecBanTblProd = new ConectaBanco();

    ModeloProdutoSemNota modProdSemNota = new ModeloProdutoSemNota();
    ControleProdutoSemNota controlProdSemNota = new ControleProdutoSemNota();

    ControleImportNotaFiscalXML controlNotaFiscal = new ControleImportNotaFiscalXML();
    ControleAddProdutoNotaFiscal controlAddProdNota = new ControleAddProdutoNotaFiscal();

    ControleCeantribCeanimport controleCeanTribCeanImport = new ControleCeantribCeanimport();
    ModeloCeantribCeanImport modCeanTribCeanImport = new ModeloCeantribCeanImport();

    ModeloProdutoComNota modProdComNota = new ModeloProdutoComNota();
    ControleProdutoComNota controlProdComNota = new ControleProdutoComNota();
    
    ModeloUnidadeMedida modUnidMed= new ModeloUnidadeMedida();
    ControleUnidadeMedida contrUnidMed= new ControleUnidadeMedida();

    String modoNavegar;
    String codCeanImport; //CODIGO DE IMPORTAÇAO TABELA NOTA PRODUTO
    String codBarrasVerifica;
    String unidadeMedidaNotaProd;
    int idProduto;
    int opMarcaSemNota, opGrupoSemNota, opFornecedorSemNota; //atualiza combobox =1
    int opMarcaComNota, opGrupoComNota, opFornecedorComNota; //atualiza combobox =1

    //VARIAVEIS PRODUTOS SEM CÓDIGOS
    String codBarraTblProd;

    //VARIAVEIS DE TRATA OS VALORES DOS PRODUTO NOVOS COM NOTA
    float vlrganhoItemNovos; //SALVA O VALOR GANHO 
    float preVendaNovos;     //SALVA O VALOR DE VENDA   
    float precoCustoNovos;   //SALVA O PRECO DE COMPRA
    int quantidadeNovos;     //SALVA A QUANTIDADE 
    int igual1_div2_mult3Novos;
        float precoCustoReturnNovos;
        int quantidadeReturnNovos;
        int quantidadeSetRNovos;

    //VARIAVEIS DE TRATA OS VALORES DOS PRODUTO SEM NOTA
    float vlrganhoItem; //SALVA O VALOR GANHO PARA CADA ITEM 
    float preVenda;     // SALVA O VALOR DE VENDA DE CADA ITEM
    float preCompra;    // SALVA O PRECO DE COMPRA DO ITEM

    //VARIAVEIS PARA KEYRELESSE ITENS FALTANDO CONVERTE
    float vlrCompraConv;
    float vlrVendaConv;
    String codCeanImportProdConvert;
    float vlrGanhoConv;
    int idProdutoConv;
    ////////
    String nomeFornecedor;

    //PEGA DATA DO SISTEMA
    NumberFormat formant;
    SimpleDateFormat forma = new SimpleDateFormat("dd/MM/yyyy");
    Date hoje = new Date();

    public FrmProduto() {
        initComponents();
        Locale.setDefault(new Locale("pt", "BR"));
        UIManager.put("OptionPane.messageFont", new FontUIResource("Arial", Font.NORMAL, 17));
        UIManager.put("OptionPane.yesButtonText", "Confirma");
        UIManager.put("OptionPane.noButtonText", "Cancelar");

        preencheComboFornecedor();
        preencheComboMarca();
        preencheComboGrupo();

        preecherTabela("select * from produto order by nome_produto");

        modoNavegar = "Inicializar";
        manipularInterface();

        conecBanc.conexaoBanco();
        try {
            conecBanc.executaSQL("select * from nota_produto where importado1_novo2_converte3=2 or importado1_novo2_converte3=3 or importado1_novo2_converte3=5");
            conecBanc.rs.first();
            int opcItensImport = conecBanc.rs.getInt("importado1_novo2_converte3");

            if (opcItensImport == 2 | opcItensImport == 3 | opcItensImport == 5) {
                preecherTabelaProdutoPesquisa("select * from produto order by nome_produto");
                preecherTabelaItensImportadosPesquisar("select * from nota_produto where importado1_novo2_converte3=5 order by id_produto");
                preecherTabelaItensImportadosNovo("select * from nota_produto where importado1_novo2_converte3 =2 order by id_produto");
                preecherTabelaItensImportadosConverte("select * from nota_produto where importado1_novo2_converte3 =3 order by id_produto");
                jt_importarProdutos.setVisible(true);
            }
        } catch (SQLException ex) {
            jt_importarProdutos.setVisible(false);
        }
        conecBanc.desconectar();

        cbox_unidadeMedida_itensConvertTblNota.setSelectedItem(null);
        cbox_unidadeMedida_itensSem.setSelectedItem(null);
        cbox_grupo_itensSem.setSelectedItem(null);
        cbox_marca_itensSem.setSelectedItem(null);
        btn_novoItensSemCodigo.setEnabled(false);

        btn_gerarCodBarraComNota.setVisible(false);
        btn_gerarCodBarraComNota.setEnabled(false);

        jPanel76.setVisible(false);
        btn_cancelarConvert.setVisible(false);
        btn_salvarConvert.setVisible(false);

        btn_cancelarProdutoNaoCadastrado.setEnabled(false);
        btn_salvarProdutoNaoCadastrado.setEnabled(false);
        btn_ConverteProdutoNaoCadastrado.setEnabled(false);
        btn_PesquisarProdutoNaoCadastrado.setEnabled(false);
        btn_addMarcaComNota.setEnabled(false);
        btn_addGrupoComNota.setEnabled(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        try {
            jDialogConverte =(javax.swing.JDialog)java.beans.Beans.instantiate(getClass().getClassLoader(), "Visao.FrmProduto_jDialogConverte");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        jDesktopPane7 = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        btn_multConvet = new javax.swing.JButton();
        btn_igualConvert = new javax.swing.JButton();
        btn_divConvert = new javax.swing.JButton();
        jPanel116 = new javax.swing.JPanel();
        ct_quantidade2Convert = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel120 = new javax.swing.JPanel();
        ct_quantidadeResulConvert = new javax.swing.JTextField();
        jPanel121 = new javax.swing.JPanel();
        ct_precoCustoConvertResul = new javax.swing.JTextField();
        jPanel117 = new javax.swing.JPanel();
        ct_quantidadeConvertSet = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_cancelaJanelaConvert = new javax.swing.JButton();
        btn_okConvert = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel118 = new javax.swing.JPanel();
        ct_codigoConvert = new javax.swing.JTextField();
        jPanel119 = new javax.swing.JPanel();
        ct_descricaoConvert = new javax.swing.JTextField();
        jPanel87 = new javax.swing.JPanel();
        ct_vlrCustoConvert = new javax.swing.JTextField();
        jPanel80 = new javax.swing.JPanel();
        ct_quantidadeConvert = new javax.swing.JTextField();
        a1 = new javax.swing.JPanel();
        cbox_unidadeMedidaConvert = new javax.swing.JComboBox<>();
        jDesktopPane9 = new javax.swing.JDesktopPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        ct_pesquisar = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btn_novo = new javax.swing.JButton();
        btn_salvar = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_excluir = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel102 = new javax.swing.JPanel();
        ct_codBarra = new javax.swing.JTextField();
        jPanel106 = new javax.swing.JPanel();
        ct_preco_venda = new javax.swing.JTextField();
        jPanel111 = new javax.swing.JPanel();
        cbox_grupo = new javax.swing.JComboBox<>();
        jPanel105 = new javax.swing.JPanel();
        ct_vlr_ganho = new javax.swing.JTextField();
        btn_gerarCodBarraSemNota = new javax.swing.JButton();
        jPanel109 = new javax.swing.JPanel();
        ct_qtdMinEstq = new javax.swing.JTextField();
        jPanel103 = new javax.swing.JPanel();
        ct_preco_compra = new javax.swing.JTextField();
        jPanel100 = new javax.swing.JPanel();
        ct_cod = new javax.swing.JTextField();
        btn_addFornecedorSemNota = new javax.swing.JButton();
        jPanel108 = new javax.swing.JPanel();
        ct_quantidade = new javax.swing.JTextField();
        jPanel104 = new javax.swing.JPanel();
        ct_percentual = new javax.swing.JTextField();
        jPanel97 = new javax.swing.JPanel();
        ct_produto = new javax.swing.JTextField();
        jPanel110 = new javax.swing.JPanel();
        cbox_marca = new javax.swing.JComboBox<>();
        btn_addMarcaSemNota = new javax.swing.JButton();
        jPanel107 = new javax.swing.JPanel();
        cbox_unidade = new javax.swing.JComboBox<>();
        jPanel112 = new javax.swing.JPanel();
        cbox_fornecedor = new javax.swing.JComboBox<>();
        btn_addGrupoSemNota = new javax.swing.JButton();
        jPanel56 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_produtos = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        btn_ler_nota_fiscal = new javax.swing.JButton();
        ct_chaveNota = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jDesktopPane3 = new javax.swing.JDesktopPane();
        jt_importarProdutos = new javax.swing.JTabbedPane();
        jp_novos = new javax.swing.JDesktopPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_itensSemCadastro = new javax.swing.JTable();
        jDesktopPane5 = new javax.swing.JDesktopPane();
        jPanel72 = new javax.swing.JPanel();
        ct_descricao_itensSem = new javax.swing.JTextField();
        s = new javax.swing.JPanel();
        cbox_marca_itensSem = new javax.swing.JComboBox<>();
        btn_addGrupoComNota = new javax.swing.JButton();
        jPanel82 = new javax.swing.JPanel();
        cbox_grupo_itensSem = new javax.swing.JComboBox<>();
        btn_gerarCodBarraComNota = new javax.swing.JButton();
        a = new javax.swing.JPanel();
        cbox_unidadeMedida_itensSem = new javax.swing.JComboBox<>();
        jPanel75 = new javax.swing.JPanel();
        ct_vlrVenda_itensSem = new javax.swing.JTextField();
        jPanel77 = new javax.swing.JPanel();
        ct_margeDeLucro = new javax.swing.JTextField();
        jPanel74 = new javax.swing.JPanel();
        ct_vlrCusto_itensSem = new javax.swing.JTextField();
        jPanel81 = new javax.swing.JPanel();
        ct_estMinimo_itensSem = new javax.swing.JTextField();
        jPanel79 = new javax.swing.JPanel();
        ct_qtdInicial_itensSem = new javax.swing.JTextField();
        jPanel113 = new javax.swing.JPanel();
        ct_codigo = new javax.swing.JTextField();
        jPanel73 = new javax.swing.JPanel();
        ct_codBarra_itensSem = new javax.swing.JTextField();
        btn_addMarcaComNota = new javax.swing.JButton();
        btn_PesquisarProdutoNaoCadastrado = new javax.swing.JButton();
        btn_ConverteProdutoNaoCadastrado = new javax.swing.JButton();
        btn_salvarProdutoNaoCadastrado = new javax.swing.JButton();
        btn_cancelarProdutoNaoCadastrado = new javax.swing.JButton();
        jp_conversão = new javax.swing.JDesktopPane();
        jDesktopPane6 = new javax.swing.JDesktopPane();
        jPanel90 = new javax.swing.JPanel();
        jPanel96 = new javax.swing.JPanel();
        ct_quantidadeConvertNota = new javax.swing.JTextField();
        a4 = new javax.swing.JPanel();
        cbox_unidadeMedida_itensConvertTblNota = new javax.swing.JComboBox<>();
        jPanel83 = new javax.swing.JPanel();
        ct_descricao_itensConvertTblNota = new javax.swing.JTextField();
        jPanel93 = new javax.swing.JPanel();
        ct_vlrCusto_itensConvertTblNota = new javax.swing.JTextField();
        jPanel76 = new javax.swing.JPanel();
        jPanel78 = new javax.swing.JPanel();
        ct_descricao_itensConvertTblProd = new javax.swing.JTextField();
        jPanel85 = new javax.swing.JPanel();
        ct_vlrVenda_itensConvertTblProd = new javax.swing.JTextField();
        jPanel86 = new javax.swing.JPanel();
        ct_lucro_itensConvertTblProd = new javax.swing.JTextField();
        a5 = new javax.swing.JPanel();
        ct_unidMedBan = new javax.swing.JTextField();
        jPanel88 = new javax.swing.JPanel();
        ct_codBarra_convert = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_itensConvete = new javax.swing.JTable();
        btn_cancelarConvert = new javax.swing.JButton();
        btn_salvarConvert = new javax.swing.JButton();
        jp_sem_codigo_barra = new javax.swing.JDesktopPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel98 = new javax.swing.JPanel();
        btn_novoItensSemCodigo = new javax.swing.JButton();
        btn_cadastradoItensSemCodigo = new javax.swing.JButton();
        btn_cancelarProdutosSemCodigo = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbl_itensSemCodBarra = new javax.swing.JTable();
        jPanel99 = new javax.swing.JPanel();
        ct_pesquisar1 = new javax.swing.JTextField();
        jPanel55 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tbm_itensCadastradosProdSemCodBarra = new javax.swing.JTable();
        jDesktopPane8 = new javax.swing.JDesktopPane();
        jPanel91 = new javax.swing.JPanel();
        ct_codBarraSemcodigoBarra = new javax.swing.JTextField();
        jPanel89 = new javax.swing.JPanel();
        ct_descricao_itensSemCodigo = new javax.swing.JTextField();
        jPanel92 = new javax.swing.JPanel();
        ct_numItemItensSemCodigo = new javax.swing.JTextField();
        btn_colarCodBarraitensSemCodBarra = new javax.swing.JButton();

        jDesktopPane7.setBackground(new java.awt.Color(51, 102, 255));
        jDesktopPane7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDesktopPane7.setMaximumSize(new java.awt.Dimension(633, 369));
        jDesktopPane7.setMinimumSize(new java.awt.Dimension(633, 369));

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel1.setOpaque(false);

        btn_multConvet.setBackground(new java.awt.Color(51, 51, 51));
        btn_multConvet.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btn_multConvet.setForeground(new java.awt.Color(0, 0, 153));
        btn_multConvet.setText("Mult(x)");
        btn_multConvet.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_multConvet.setEnabled(false);
        btn_multConvet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_multConvet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_multConvetActionPerformed(evt);
            }
        });

        btn_igualConvert.setBackground(new java.awt.Color(51, 51, 51));
        btn_igualConvert.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btn_igualConvert.setForeground(new java.awt.Color(0, 0, 153));
        btn_igualConvert.setText("Igual(=)");
        btn_igualConvert.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_igualConvert.setEnabled(false);
        btn_igualConvert.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_igualConvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_igualConvertActionPerformed(evt);
            }
        });

        btn_divConvert.setBackground(new java.awt.Color(51, 51, 51));
        btn_divConvert.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btn_divConvert.setForeground(new java.awt.Color(0, 0, 153));
        btn_divConvert.setText("Div(/)");
        btn_divConvert.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_divConvert.setEnabled(false);
        btn_divConvert.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_divConvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_divConvertActionPerformed(evt);
            }
        });

        jPanel116.setBackground(new java.awt.Color(197, 196, 195));
        jPanel116.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quantidade", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel116.setOpaque(false);

        ct_quantidade2Convert.setEditable(false);
        ct_quantidade2Convert.setBackground(new java.awt.Color(204, 204, 204));
        ct_quantidade2Convert.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        ct_quantidade2Convert.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_quantidade2Convert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_quantidade2Convert.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_quantidade2ConvertKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel116Layout = new javax.swing.GroupLayout(jPanel116);
        jPanel116.setLayout(jPanel116Layout);
        jPanel116Layout.setHorizontalGroup(
            jPanel116Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidade2Convert, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        );
        jPanel116Layout.setVerticalGroup(
            jPanel116Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidade2Convert)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true), "Resultado", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16))); // NOI18N
        jPanel5.setOpaque(false);

        jPanel120.setBackground(new java.awt.Color(197, 196, 195));
        jPanel120.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Quantidade", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel120.setOpaque(false);

        ct_quantidadeResulConvert.setEditable(false);
        ct_quantidadeResulConvert.setBackground(new java.awt.Color(204, 204, 204));
        ct_quantidadeResulConvert.setFont(new java.awt.Font("Arial", 1, 23)); // NOI18N
        ct_quantidadeResulConvert.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_quantidadeResulConvert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_quantidadeResulConvert.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_quantidadeResulConvert.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_quantidadeResulConvertKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel120Layout = new javax.swing.GroupLayout(jPanel120);
        jPanel120.setLayout(jPanel120Layout);
        jPanel120Layout.setHorizontalGroup(
            jPanel120Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidadeResulConvert)
        );
        jPanel120Layout.setVerticalGroup(
            jPanel120Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidadeResulConvert, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        jPanel121.setBackground(new java.awt.Color(197, 196, 195));
        jPanel121.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Preço Custo(R$)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel121.setOpaque(false);

        ct_precoCustoConvertResul.setEditable(false);
        ct_precoCustoConvertResul.setBackground(new java.awt.Color(204, 204, 204));
        ct_precoCustoConvertResul.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        ct_precoCustoConvertResul.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_precoCustoConvertResul.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_precoCustoConvertResul.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_precoCustoConvertResul.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_precoCustoConvertResulKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel121Layout = new javax.swing.GroupLayout(jPanel121);
        jPanel121.setLayout(jPanel121Layout);
        jPanel121Layout.setHorizontalGroup(
            jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_precoCustoConvertResul, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
        );
        jPanel121Layout.setVerticalGroup(
            jPanel121Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_precoCustoConvertResul, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel121, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel120, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel120, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel121, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel117.setBackground(new java.awt.Color(197, 196, 195));
        jPanel117.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "??????", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel117.setOpaque(false);

        ct_quantidadeConvertSet.setBackground(new java.awt.Color(255, 255, 102));
        ct_quantidadeConvertSet.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        ct_quantidadeConvertSet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_quantidadeConvertSet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_quantidadeConvertSet.setEnabled(false);
        ct_quantidadeConvertSet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_quantidadeConvertSetKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel117Layout = new javax.swing.GroupLayout(jPanel117);
        jPanel117.setLayout(jPanel117Layout);
        jPanel117Layout.setHorizontalGroup(
            jPanel117Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidadeConvertSet, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        );
        jPanel117Layout.setVerticalGroup(
            jPanel117Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidadeConvertSet)
        );

        jLabel1.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel1.setText("=");

        btn_cancelaJanelaConvert.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_cancelaJanelaConvert.setText("Cancelar");
        btn_cancelaJanelaConvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelaJanelaConvertActionPerformed(evt);
            }
        });

        btn_okConvert.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btn_okConvert.setText("Salvar");
        btn_okConvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_okConvertActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel116, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_multConvet, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btn_divConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_igualConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel117, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_cancelaJanelaConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(btn_okConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(106, 106, 106)))
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btn_multConvet, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_igualConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_divConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jPanel117, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jPanel116, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_cancelaJanelaConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_okConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel9.setOpaque(false);

        jPanel118.setBackground(new java.awt.Color(197, 196, 195));
        jPanel118.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Código", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel118.setOpaque(false);

        ct_codigoConvert.setEditable(false);
        ct_codigoConvert.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        ct_codigoConvert.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_codigoConvert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_codigoConvert.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        ct_codigoConvert.setEnabled(false);

        javax.swing.GroupLayout jPanel118Layout = new javax.swing.GroupLayout(jPanel118);
        jPanel118.setLayout(jPanel118Layout);
        jPanel118Layout.setHorizontalGroup(
            jPanel118Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_codigoConvert, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
        );
        jPanel118Layout.setVerticalGroup(
            jPanel118Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel118Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_codigoConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel119.setBackground(new java.awt.Color(197, 196, 195));
        jPanel119.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Descrição do Produto", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel119.setOpaque(false);

        ct_descricaoConvert.setEditable(false);
        ct_descricaoConvert.setBackground(new java.awt.Color(204, 204, 204));
        ct_descricaoConvert.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_descricaoConvert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_descricaoConvert.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_descricaoConvertKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel119Layout = new javax.swing.GroupLayout(jPanel119);
        jPanel119.setLayout(jPanel119Layout);
        jPanel119Layout.setHorizontalGroup(
            jPanel119Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_descricaoConvert)
        );
        jPanel119Layout.setVerticalGroup(
            jPanel119Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel119Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_descricaoConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel87.setBackground(new java.awt.Color(197, 196, 195));
        jPanel87.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Preço Custo(R$)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel87.setOpaque(false);

        ct_vlrCustoConvert.setEditable(false);
        ct_vlrCustoConvert.setBackground(new java.awt.Color(204, 204, 204));
        ct_vlrCustoConvert.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_vlrCustoConvert.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_vlrCustoConvert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_vlrCustoConvert.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_vlrCustoConvert.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ct_vlrCustoConvertMousePressed(evt);
            }
        });
        ct_vlrCustoConvert.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_vlrCustoConvertKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel87Layout = new javax.swing.GroupLayout(jPanel87);
        jPanel87.setLayout(jPanel87Layout);
        jPanel87Layout.setHorizontalGroup(
            jPanel87Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrCustoConvert, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        );
        jPanel87Layout.setVerticalGroup(
            jPanel87Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrCustoConvert)
        );

        jPanel80.setBackground(new java.awt.Color(197, 196, 195));
        jPanel80.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Quantidade", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel80.setOpaque(false);

        ct_quantidadeConvert.setEditable(false);
        ct_quantidadeConvert.setBackground(new java.awt.Color(204, 204, 204));
        ct_quantidadeConvert.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ct_quantidadeConvert.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_quantidadeConvert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_quantidadeConvert.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_quantidadeConvertKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel80Layout = new javax.swing.GroupLayout(jPanel80);
        jPanel80.setLayout(jPanel80Layout);
        jPanel80Layout.setHorizontalGroup(
            jPanel80Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidadeConvert, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
        );
        jPanel80Layout.setVerticalGroup(
            jPanel80Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidadeConvert)
        );

        a1.setBackground(new java.awt.Color(197, 196, 195));
        a1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Unid. Medida", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        a1.setOpaque(false);

        cbox_unidadeMedidaConvert.setBackground(new java.awt.Color(252, 252, 146));
        cbox_unidadeMedidaConvert.setFont(new java.awt.Font("Arial Black", 1, 16)); // NOI18N
        cbox_unidadeMedidaConvert.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M²", "M", "LT", "SC", "CM", "UN", "CT", "CX", "PA", "PC", "PT", "RL", "KG", "G", "M²", "MILH", "LAT", "CAÇ", "X" }));
        cbox_unidadeMedidaConvert.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cbox_unidadeMedidaConvert.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbox_unidadeMedidaConvert.setEnabled(false);

        javax.swing.GroupLayout a1Layout = new javax.swing.GroupLayout(a1);
        a1.setLayout(a1Layout);
        a1Layout.setHorizontalGroup(
            a1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_unidadeMedidaConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        a1Layout.setVerticalGroup(
            a1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_unidadeMedidaConvert, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel118, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel119, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(jPanel80, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel87, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(a1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel118, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel119, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(a1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDesktopPane7.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane7.setLayer(jPanel9, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane7Layout = new javax.swing.GroupLayout(jDesktopPane7);
        jDesktopPane7.setLayout(jDesktopPane7Layout);
        jDesktopPane7Layout.setHorizontalGroup(
            jDesktopPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDesktopPane7Layout.setVerticalGroup(
            jDesktopPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout jDialogConverteLayout = new javax.swing.GroupLayout(jDialogConverte.getContentPane());
        jDialogConverte.getContentPane().setLayout(jDialogConverteLayout);
        jDialogConverteLayout.setHorizontalGroup(
            jDialogConverteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialogConverteLayout.setVerticalGroup(
            jDialogConverteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(197, 196, 195));
        setClosable(true);
        setTitle("FORMULÁRIO DE CADASTRO DE PRODUTOS");
        setPreferredSize(new java.awt.Dimension(700, 300));

        jTabbedPane1.setBackground(new java.awt.Color(197, 196, 195));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N

        jDesktopPane2.setBackground(new java.awt.Color(0, 51, 204));

        ct_pesquisar.setBackground(new java.awt.Color(0, 238, 255));
        ct_pesquisar.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        ct_pesquisar.setText(" Caixa de Pesquisa...");
        ct_pesquisar.setToolTipText("Digite o nome do produto ou código de barra");
        ct_pesquisar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 3, true));
        ct_pesquisar.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_pesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ct_pesquisarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ct_pesquisarMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ct_pesquisarMousePressed(evt);
            }
        });
        ct_pesquisar.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ct_pesquisarComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                ct_pesquisarComponentShown(evt);
            }
        });
        ct_pesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_pesquisarActionPerformed(evt);
            }
        });
        ct_pesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_pesquisarKeyReleased(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 4));
        jPanel6.setForeground(new java.awt.Color(102, 102, 102));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        btn_novo.setBackground(new java.awt.Color(255, 255, 255));
        btn_novo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_novo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/NOVO.png"))); // NOI18N
        btn_novo.setToolTipText("NOVO");
        btn_novo.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        btn_novo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_novoActionPerformed(evt);
            }
        });

        btn_salvar.setBackground(new java.awt.Color(255, 255, 255));
        btn_salvar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_salvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/salvar.png"))); // NOI18N
        btn_salvar.setToolTipText("SALVAR");
        btn_salvar.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        btn_salvar.setEnabled(false);
        btn_salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salvarActionPerformed(evt);
            }
        });

        btn_editar.setBackground(new java.awt.Color(255, 255, 255));
        btn_editar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_editar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/EDITAR.png"))); // NOI18N
        btn_editar.setToolTipText("EDITAR");
        btn_editar.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        btn_editar.setEnabled(false);
        btn_editar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btn_editarMouseReleased(evt);
            }
        });
        btn_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarActionPerformed(evt);
            }
        });

        btn_excluir.setBackground(new java.awt.Color(255, 255, 255));
        btn_excluir.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_excluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/excluir.png"))); // NOI18N
        btn_excluir.setToolTipText("EXCLUIR");
        btn_excluir.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        btn_excluir.setEnabled(false);
        btn_excluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_excluirActionPerformed(evt);
            }
        });

        btn_cancelar.setBackground(new java.awt.Color(255, 255, 255));
        btn_cancelar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/cancelar.png"))); // NOI18N
        btn_cancelar.setToolTipText("CANCELAR");
        btn_cancelar.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        btn_cancelar.setEnabled(false);
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_novo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_salvar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_excluir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(btn_novo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_salvar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_editar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_excluir, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        jDesktopPane1.setBackground(new java.awt.Color(0, 51, 153));
        jDesktopPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        jPanel102.setBackground(new java.awt.Color(197, 196, 195));
        jPanel102.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Código de Barras", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel102.setForeground(new java.awt.Color(255, 255, 255));
        jPanel102.setOpaque(false);

        ct_codBarra.setBackground(new java.awt.Color(255, 255, 102));
        ct_codBarra.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_codBarra.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_codBarra.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_codBarra.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        ct_codBarra.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_codBarra.setEnabled(false);
        ct_codBarra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_codBarraKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel102Layout = new javax.swing.GroupLayout(jPanel102);
        jPanel102.setLayout(jPanel102Layout);
        jPanel102Layout.setHorizontalGroup(
            jPanel102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_codBarra)
        );
        jPanel102Layout.setVerticalGroup(
            jPanel102Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_codBarra, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel106.setBackground(new java.awt.Color(197, 196, 195));
        jPanel106.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Preço Venda(R$)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel106.setOpaque(false);

        ct_preco_venda.setEditable(false);
        ct_preco_venda.setBackground(new java.awt.Color(255, 255, 102));
        ct_preco_venda.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_preco_venda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_preco_venda.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_preco_venda.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_preco_venda.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_preco_venda.setEnabled(false);

        javax.swing.GroupLayout jPanel106Layout = new javax.swing.GroupLayout(jPanel106);
        jPanel106.setLayout(jPanel106Layout);
        jPanel106Layout.setHorizontalGroup(
            jPanel106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_preco_venda, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
        );
        jPanel106Layout.setVerticalGroup(
            jPanel106Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_preco_venda, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel111.setBackground(new java.awt.Color(197, 196, 195));
        jPanel111.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Grupo ", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel111.setOpaque(false);
        jPanel111.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel111MouseEntered(evt);
            }
        });

        cbox_grupo.setBackground(new java.awt.Color(252, 252, 146));
        cbox_grupo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbox_grupo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cbox_grupo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbox_grupoMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel111Layout = new javax.swing.GroupLayout(jPanel111);
        jPanel111.setLayout(jPanel111Layout);
        jPanel111Layout.setHorizontalGroup(
            jPanel111Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_grupo, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel111Layout.setVerticalGroup(
            jPanel111Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_grupo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel105.setBackground(new java.awt.Color(197, 196, 195));
        jPanel105.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Lucro(R$)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel105.setOpaque(false);

        ct_vlr_ganho.setEditable(false);
        ct_vlr_ganho.setBackground(new java.awt.Color(255, 255, 102));
        ct_vlr_ganho.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_vlr_ganho.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_vlr_ganho.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_vlr_ganho.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_vlr_ganho.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_vlr_ganho.setEnabled(false);

        javax.swing.GroupLayout jPanel105Layout = new javax.swing.GroupLayout(jPanel105);
        jPanel105.setLayout(jPanel105Layout);
        jPanel105Layout.setHorizontalGroup(
            jPanel105Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlr_ganho, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );
        jPanel105Layout.setVerticalGroup(
            jPanel105Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlr_ganho)
        );

        btn_gerarCodBarraSemNota.setBackground(new java.awt.Color(255, 255, 255));
        btn_gerarCodBarraSemNota.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_gerarCodBarraSemNota.setText("GERAR");
        btn_gerarCodBarraSemNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gerarCodBarraSemNotaActionPerformed(evt);
            }
        });

        jPanel109.setBackground(new java.awt.Color(197, 196, 195));
        jPanel109.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Qtd. Minima", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel109.setOpaque(false);

        ct_qtdMinEstq.setBackground(new java.awt.Color(255, 255, 102));
        ct_qtdMinEstq.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_qtdMinEstq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_qtdMinEstq.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_qtdMinEstq.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_qtdMinEstq.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        ct_qtdMinEstq.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_qtdMinEstq.setEnabled(false);
        ct_qtdMinEstq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_qtdMinEstqKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel109Layout = new javax.swing.GroupLayout(jPanel109);
        jPanel109.setLayout(jPanel109Layout);
        jPanel109Layout.setHorizontalGroup(
            jPanel109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_qtdMinEstq, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        jPanel109Layout.setVerticalGroup(
            jPanel109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_qtdMinEstq, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel103.setBackground(new java.awt.Color(197, 196, 195));
        jPanel103.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Preço Custo (R$)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel103.setOpaque(false);

        ct_preco_compra.setBackground(new java.awt.Color(255, 255, 102));
        ct_preco_compra.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_preco_compra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_preco_compra.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_preco_compra.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_preco_compra.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_preco_compra.setEnabled(false);
        ct_preco_compra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ct_preco_compraMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ct_preco_compraMousePressed(evt);
            }
        });
        ct_preco_compra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_preco_compraActionPerformed(evt);
            }
        });
        ct_preco_compra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_preco_compraKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel103Layout = new javax.swing.GroupLayout(jPanel103);
        jPanel103.setLayout(jPanel103Layout);
        jPanel103Layout.setHorizontalGroup(
            jPanel103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_preco_compra, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        );
        jPanel103Layout.setVerticalGroup(
            jPanel103Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_preco_compra, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel100.setBackground(new java.awt.Color(197, 196, 195));
        jPanel100.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Código", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel100.setOpaque(false);

        ct_cod.setEditable(false);
        ct_cod.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        ct_cod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_cod.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_cod.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        ct_cod.setEnabled(false);

        javax.swing.GroupLayout jPanel100Layout = new javax.swing.GroupLayout(jPanel100);
        jPanel100.setLayout(jPanel100Layout);
        jPanel100Layout.setHorizontalGroup(
            jPanel100Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_cod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel100Layout.setVerticalGroup(
            jPanel100Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_cod, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btn_addFornecedorSemNota.setBackground(new java.awt.Color(204, 204, 204));
        btn_addFornecedorSemNota.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btn_addFornecedorSemNota.setForeground(new java.awt.Color(51, 204, 0));
        btn_addFornecedorSemNota.setText("NOVO FORNECEDOR");
        btn_addFornecedorSemNota.setEnabled(false);
        btn_addFornecedorSemNota.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btn_addFornecedorSemNotaMouseMoved(evt);
            }
        });
        btn_addFornecedorSemNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addFornecedorSemNotaActionPerformed(evt);
            }
        });

        jPanel108.setBackground(new java.awt.Color(197, 196, 195));
        jPanel108.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Quantidade", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel108.setOpaque(false);

        ct_quantidade.setBackground(new java.awt.Color(255, 255, 102));
        ct_quantidade.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_quantidade.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_quantidade.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_quantidade.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_quantidade.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        ct_quantidade.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_quantidade.setEnabled(false);
        ct_quantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_quantidadeKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel108Layout = new javax.swing.GroupLayout(jPanel108);
        jPanel108.setLayout(jPanel108Layout);
        jPanel108Layout.setHorizontalGroup(
            jPanel108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
        );
        jPanel108Layout.setVerticalGroup(
            jPanel108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidade, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jPanel104.setBackground(new java.awt.Color(197, 196, 195));
        jPanel104.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Margem (%)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel104.setOpaque(false);

        ct_percentual.setBackground(new java.awt.Color(255, 255, 102));
        ct_percentual.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_percentual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_percentual.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_percentual.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_percentual.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_percentual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_percentualKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel104Layout = new javax.swing.GroupLayout(jPanel104);
        jPanel104.setLayout(jPanel104Layout);
        jPanel104Layout.setHorizontalGroup(
            jPanel104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_percentual, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
        );
        jPanel104Layout.setVerticalGroup(
            jPanel104Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_percentual, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel97.setBackground(new java.awt.Color(197, 196, 195));
        jPanel97.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Descrição do Produto", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel97.setOpaque(false);

        ct_produto.setBackground(new java.awt.Color(255, 255, 102));
        ct_produto.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_produto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
        ct_produto.setCaretColor(new java.awt.Color(255, 0, 0));
        ct_produto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_produto.setEnabled(false);
        ct_produto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_produtoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel97Layout = new javax.swing.GroupLayout(jPanel97);
        jPanel97.setLayout(jPanel97Layout);
        jPanel97Layout.setHorizontalGroup(
            jPanel97Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_produto, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );
        jPanel97Layout.setVerticalGroup(
            jPanel97Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel97Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_produto, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
        );

        jPanel110.setBackground(new java.awt.Color(197, 196, 195));
        jPanel110.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Marca", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel110.setOpaque(false);
        jPanel110.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel110MouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel110MousePressed(evt);
            }
        });

        cbox_marca.setBackground(new java.awt.Color(252, 252, 146));
        cbox_marca.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbox_marca.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cbox_marca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbox_marcaMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel110Layout = new javax.swing.GroupLayout(jPanel110);
        jPanel110.setLayout(jPanel110Layout);
        jPanel110Layout.setHorizontalGroup(
            jPanel110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_marca, 0, 220, Short.MAX_VALUE)
        );
        jPanel110Layout.setVerticalGroup(
            jPanel110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel110Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cbox_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btn_addMarcaSemNota.setBackground(new java.awt.Color(204, 204, 204));
        btn_addMarcaSemNota.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btn_addMarcaSemNota.setForeground(new java.awt.Color(51, 204, 0));
        btn_addMarcaSemNota.setText("NOVA MARCA");
        btn_addMarcaSemNota.setEnabled(false);
        btn_addMarcaSemNota.setOpaque(false);
        btn_addMarcaSemNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addMarcaSemNotaActionPerformed(evt);
            }
        });

        jPanel107.setBackground(new java.awt.Color(197, 196, 195));
        jPanel107.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Unid. Medida", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel107.setOpaque(false);

        cbox_unidade.setBackground(new java.awt.Color(252, 252, 146));
        cbox_unidade.setFont(new java.awt.Font("Arial Black", 1, 16)); // NOI18N
        cbox_unidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M² ", "M ", "LT", "SC", "CM", "UN", "CT ", "CX ", "PA", "PC", "PT", "RL ", "KG ", "G ", "M²", "MILH", "LAT", "CAÇ", "X " }));
        cbox_unidade.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout jPanel107Layout = new javax.swing.GroupLayout(jPanel107);
        jPanel107.setLayout(jPanel107Layout);
        jPanel107Layout.setHorizontalGroup(
            jPanel107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_unidade, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel107Layout.setVerticalGroup(
            jPanel107Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_unidade, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel112.setBackground(new java.awt.Color(197, 196, 195));
        jPanel112.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Fornecedor", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel112.setOpaque(false);
        jPanel112.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel112MouseEntered(evt);
            }
        });

        cbox_fornecedor.setBackground(new java.awt.Color(252, 252, 146));
        cbox_fornecedor.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbox_fornecedor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cbox_fornecedor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbox_fornecedor.setEnabled(false);

        javax.swing.GroupLayout jPanel112Layout = new javax.swing.GroupLayout(jPanel112);
        jPanel112.setLayout(jPanel112Layout);
        jPanel112Layout.setHorizontalGroup(
            jPanel112Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_fornecedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel112Layout.setVerticalGroup(
            jPanel112Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel112Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cbox_fornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btn_addGrupoSemNota.setBackground(new java.awt.Color(204, 204, 204));
        btn_addGrupoSemNota.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btn_addGrupoSemNota.setForeground(new java.awt.Color(51, 204, 0));
        btn_addGrupoSemNota.setText("NOVO GRUPO");
        btn_addGrupoSemNota.setEnabled(false);
        btn_addGrupoSemNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addGrupoSemNotaActionPerformed(evt);
            }
        });

        jDesktopPane1.setLayer(jPanel102, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel106, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel111, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel105, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btn_gerarCodBarraSemNota, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel109, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel103, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel100, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btn_addFornecedorSemNota, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel108, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel104, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel97, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel110, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btn_addMarcaSemNota, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel107, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jPanel112, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btn_addGrupoSemNota, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addComponent(jPanel107, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel110, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_addMarcaSemNota))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel111, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_addGrupoSemNota))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel112, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addGap(0, 99, Short.MAX_VALUE)
                                .addComponent(btn_addFornecedorSemNota))))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(jPanel100, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel97, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_gerarCodBarraSemNota)))
                .addContainerGap())
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel108, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel109, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel103, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel104, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel105, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel106, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel100, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel97, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel102, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_gerarCodBarraSemNota)
                        .addGap(13, 13, 13)))
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel107, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel111, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel112, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel110, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_addMarcaSemNota)
                    .addComponent(btn_addGrupoSemNota)
                    .addComponent(btn_addFornecedorSemNota))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel109, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel103, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel104, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel105, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel106, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel108, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel56.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true), "Lista de Produto", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 17))); // NOI18N
        jPanel56.setOpaque(false);

        tbl_produtos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbl_produtos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        tbl_produtos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_produtos.setIntercellSpacing(new java.awt.Dimension(0, 4));
        tbl_produtos.setOpaque(false);
        tbl_produtos.setSelectionBackground(new java.awt.Color(255, 255, 102));
        tbl_produtos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_produtos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_produtosMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbl_produtosMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_produtos);

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jDesktopPane2.setLayer(ct_pesquisar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jPanel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jDesktopPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jPanel56, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane2Layout = new javax.swing.GroupLayout(jDesktopPane2);
        jDesktopPane2.setLayout(jDesktopPane2Layout);
        jDesktopPane2Layout.setHorizontalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel56, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane2Layout.createSequentialGroup()
                        .addGap(0, 29, Short.MAX_VALUE)
                        .addComponent(ct_pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDesktopPane2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jDesktopPane1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jDesktopPane2Layout.setVerticalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDesktopPane2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDesktopPane2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ct_pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(6, 6, 6))
        );

        jTabbedPane1.addTab("     Sem Nota     ", jDesktopPane2);

        jPanel4.setOpaque(false);

        jPanel11.setBackground(new java.awt.Color(197, 196, 195));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        btn_ler_nota_fiscal.setBackground(new java.awt.Color(255, 255, 255));
        btn_ler_nota_fiscal.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_ler_nota_fiscal.setText("Importa");
        btn_ler_nota_fiscal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_ler_nota_fiscalMousePressed(evt);
            }
        });
        btn_ler_nota_fiscal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ler_nota_fiscalActionPerformed(evt);
            }
        });

        ct_chaveNota.setBackground(new java.awt.Color(240, 236, 237));
        ct_chaveNota.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        ct_chaveNota.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true));
        ct_chaveNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_chaveNotaActionPerformed(evt);
            }
        });
        ct_chaveNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_chaveNotaKeyReleased(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("procurar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ct_chaveNota, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ler_nota_fiscal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(ct_chaveNota, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_ler_nota_fiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jt_importarProdutos.setBackground(new java.awt.Color(197, 196, 195));
        jt_importarProdutos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jt_importarProdutos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jp_novos.setBackground(new java.awt.Color(0, 51, 255));
        jp_novos.setOpaque(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Sem Cadastro", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16))); // NOI18N
        jPanel2.setOpaque(false);

        tbl_itensSemCadastro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbl_itensSemCadastro.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        tbl_itensSemCadastro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_itensSemCadastro.setIntercellSpacing(new java.awt.Dimension(2, 2));
        tbl_itensSemCadastro.setOpaque(false);
        tbl_itensSemCadastro.setSelectionBackground(new java.awt.Color(255, 255, 102));
        tbl_itensSemCadastro.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_itensSemCadastro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_itensSemCadastroMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tbl_itensSemCadastroMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbl_itensSemCadastroMousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(tbl_itensSemCadastro);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
        );

        jDesktopPane5.setBackground(new java.awt.Color(0, 51, 153));
        jDesktopPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jPanel72.setBackground(new java.awt.Color(197, 196, 195));
        jPanel72.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Descrição do Produto", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel72.setOpaque(false);

        ct_descricao_itensSem.setBackground(new java.awt.Color(255, 255, 102));
        ct_descricao_itensSem.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_descricao_itensSem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_descricao_itensSem.setEnabled(false);
        ct_descricao_itensSem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_descricao_itensSemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
        jPanel72.setLayout(jPanel72Layout);
        jPanel72Layout.setHorizontalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_descricao_itensSem, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
        );
        jPanel72Layout.setVerticalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_descricao_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        s.setBackground(new java.awt.Color(197, 196, 195));
        s.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Marca", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        s.setOpaque(false);
        s.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sMouseEntered(evt);
            }
        });

        cbox_marca_itensSem.setBackground(new java.awt.Color(255, 255, 102));
        cbox_marca_itensSem.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cbox_marca_itensSem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cbox_marca_itensSem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbox_marca_itensSem.setEnabled(false);
        cbox_marca_itensSem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbox_marca_itensSemMousePressed(evt);
            }
        });

        javax.swing.GroupLayout sLayout = new javax.swing.GroupLayout(s);
        s.setLayout(sLayout);
        sLayout.setHorizontalGroup(
            sLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_marca_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        sLayout.setVerticalGroup(
            sLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_marca_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btn_addGrupoComNota.setBackground(new java.awt.Color(204, 204, 204));
        btn_addGrupoComNota.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btn_addGrupoComNota.setForeground(new java.awt.Color(51, 204, 0));
        btn_addGrupoComNota.setText("NOVO GRUPO");
        btn_addGrupoComNota.setEnabled(false);
        btn_addGrupoComNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addGrupoComNotaActionPerformed(evt);
            }
        });

        jPanel82.setBackground(new java.awt.Color(197, 196, 195));
        jPanel82.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Grupo", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel82.setOpaque(false);
        jPanel82.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel82MouseEntered(evt);
            }
        });

        cbox_grupo_itensSem.setBackground(new java.awt.Color(252, 252, 146));
        cbox_grupo_itensSem.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cbox_grupo_itensSem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cbox_grupo_itensSem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbox_grupo_itensSem.setEnabled(false);
        cbox_grupo_itensSem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbox_grupo_itensSemMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cbox_grupo_itensSemMouseReleased(evt);
            }
        });
        cbox_grupo_itensSem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbox_grupo_itensSemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel82Layout = new javax.swing.GroupLayout(jPanel82);
        jPanel82.setLayout(jPanel82Layout);
        jPanel82Layout.setHorizontalGroup(
            jPanel82Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_grupo_itensSem, 0, 213, Short.MAX_VALUE)
        );
        jPanel82Layout.setVerticalGroup(
            jPanel82Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_grupo_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btn_gerarCodBarraComNota.setBackground(new java.awt.Color(255, 255, 255));
        btn_gerarCodBarraComNota.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_gerarCodBarraComNota.setText("GERAR");
        btn_gerarCodBarraComNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gerarCodBarraComNotaActionPerformed(evt);
            }
        });

        a.setBackground(new java.awt.Color(197, 196, 195));
        a.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Unid. Medida", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        a.setOpaque(false);

        cbox_unidadeMedida_itensSem.setBackground(new java.awt.Color(252, 252, 146));
        cbox_unidadeMedida_itensSem.setFont(new java.awt.Font("Arial Black", 1, 16)); // NOI18N
        cbox_unidadeMedida_itensSem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M²", "M", "LT", "SC", "CM", "UN", "CT", "CX", "PA", "PC", "PT", "RL", "KG", "G", "M²", "MILH", "LAT", "CAÇ", "X" }));
        cbox_unidadeMedida_itensSem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        cbox_unidadeMedida_itensSem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbox_unidadeMedida_itensSem.setEnabled(false);

        javax.swing.GroupLayout aLayout = new javax.swing.GroupLayout(a);
        a.setLayout(aLayout);
        aLayout.setHorizontalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_unidadeMedida_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        aLayout.setVerticalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aLayout.createSequentialGroup()
                .addComponent(cbox_unidadeMedida_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel75.setBackground(new java.awt.Color(197, 196, 195));
        jPanel75.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Preço Venda(R$)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel75.setOpaque(false);

        ct_vlrVenda_itensSem.setEditable(false);
        ct_vlrVenda_itensSem.setBackground(new java.awt.Color(204, 204, 204));
        ct_vlrVenda_itensSem.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_vlrVenda_itensSem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_vlrVenda_itensSem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_vlrVenda_itensSem.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_vlrVenda_itensSem.setEnabled(false);

        javax.swing.GroupLayout jPanel75Layout = new javax.swing.GroupLayout(jPanel75);
        jPanel75.setLayout(jPanel75Layout);
        jPanel75Layout.setHorizontalGroup(
            jPanel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrVenda_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel75Layout.setVerticalGroup(
            jPanel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrVenda_itensSem)
        );

        jPanel77.setBackground(new java.awt.Color(197, 196, 195));
        jPanel77.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Margem(%)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel77.setOpaque(false);

        ct_margeDeLucro.setBackground(new java.awt.Color(255, 255, 102));
        ct_margeDeLucro.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_margeDeLucro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_margeDeLucro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_margeDeLucro.setEnabled(false);
        ct_margeDeLucro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_margeDeLucroKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel77Layout = new javax.swing.GroupLayout(jPanel77);
        jPanel77.setLayout(jPanel77Layout);
        jPanel77Layout.setHorizontalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_margeDeLucro, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel77Layout.setVerticalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_margeDeLucro)
        );

        jPanel74.setBackground(new java.awt.Color(197, 196, 195));
        jPanel74.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Preço Custo(R$)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel74.setOpaque(false);

        ct_vlrCusto_itensSem.setEditable(false);
        ct_vlrCusto_itensSem.setBackground(new java.awt.Color(204, 204, 204));
        ct_vlrCusto_itensSem.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_vlrCusto_itensSem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_vlrCusto_itensSem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_vlrCusto_itensSem.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_vlrCusto_itensSem.setEnabled(false);
        ct_vlrCusto_itensSem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ct_vlrCusto_itensSemMousePressed(evt);
            }
        });
        ct_vlrCusto_itensSem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_vlrCusto_itensSemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel74Layout = new javax.swing.GroupLayout(jPanel74);
        jPanel74.setLayout(jPanel74Layout);
        jPanel74Layout.setHorizontalGroup(
            jPanel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrCusto_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel74Layout.setVerticalGroup(
            jPanel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrCusto_itensSem)
        );

        jPanel81.setBackground(new java.awt.Color(197, 196, 195));
        jPanel81.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Qtd. Minima", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel81.setOpaque(false);

        ct_estMinimo_itensSem.setBackground(new java.awt.Color(255, 255, 102));
        ct_estMinimo_itensSem.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_estMinimo_itensSem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_estMinimo_itensSem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_estMinimo_itensSem.setEnabled(false);
        ct_estMinimo_itensSem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_estMinimo_itensSemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel81Layout = new javax.swing.GroupLayout(jPanel81);
        jPanel81.setLayout(jPanel81Layout);
        jPanel81Layout.setHorizontalGroup(
            jPanel81Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_estMinimo_itensSem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel81Layout.setVerticalGroup(
            jPanel81Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_estMinimo_itensSem)
        );

        jPanel79.setBackground(new java.awt.Color(197, 196, 195));
        jPanel79.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Quantidade", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel79.setOpaque(false);

        ct_qtdInicial_itensSem.setEditable(false);
        ct_qtdInicial_itensSem.setBackground(new java.awt.Color(204, 204, 204));
        ct_qtdInicial_itensSem.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_qtdInicial_itensSem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_qtdInicial_itensSem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_qtdInicial_itensSem.setEnabled(false);
        ct_qtdInicial_itensSem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_qtdInicial_itensSemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel79Layout = new javax.swing.GroupLayout(jPanel79);
        jPanel79.setLayout(jPanel79Layout);
        jPanel79Layout.setHorizontalGroup(
            jPanel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_qtdInicial_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel79Layout.setVerticalGroup(
            jPanel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_qtdInicial_itensSem)
        );

        jPanel113.setBackground(new java.awt.Color(197, 196, 195));
        jPanel113.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Código", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel113.setOpaque(false);

        ct_codigo.setEditable(false);
        ct_codigo.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        ct_codigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_codigo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_codigo.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        ct_codigo.setEnabled(false);

        javax.swing.GroupLayout jPanel113Layout = new javax.swing.GroupLayout(jPanel113);
        jPanel113.setLayout(jPanel113Layout);
        jPanel113Layout.setHorizontalGroup(
            jPanel113Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_codigo, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
        );
        jPanel113Layout.setVerticalGroup(
            jPanel113Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel113Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel73.setBackground(new java.awt.Color(197, 196, 195));
        jPanel73.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Código de Barras", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel73.setOpaque(false);

        ct_codBarra_itensSem.setBackground(new java.awt.Color(255, 255, 102));
        ct_codBarra_itensSem.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        ct_codBarra_itensSem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_codBarra_itensSem.setEnabled(false);
        ct_codBarra_itensSem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_codBarra_itensSemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel73Layout = new javax.swing.GroupLayout(jPanel73);
        jPanel73.setLayout(jPanel73Layout);
        jPanel73Layout.setHorizontalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_codBarra_itensSem)
        );
        jPanel73Layout.setVerticalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel73Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_codBarra_itensSem, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btn_addMarcaComNota.setBackground(new java.awt.Color(204, 204, 204));
        btn_addMarcaComNota.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btn_addMarcaComNota.setForeground(new java.awt.Color(51, 204, 0));
        btn_addMarcaComNota.setText(" NOVA MARCA");
        btn_addMarcaComNota.setEnabled(false);
        btn_addMarcaComNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addMarcaComNotaActionPerformed(evt);
            }
        });

        btn_PesquisarProdutoNaoCadastrado.setBackground(new java.awt.Color(255, 255, 255));
        btn_PesquisarProdutoNaoCadastrado.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btn_PesquisarProdutoNaoCadastrado.setForeground(new java.awt.Color(0, 102, 255));
        btn_PesquisarProdutoNaoCadastrado.setText("PESQUISAR");
        btn_PesquisarProdutoNaoCadastrado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_PesquisarProdutoNaoCadastrado.setEnabled(false);
        btn_PesquisarProdutoNaoCadastrado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_PesquisarProdutoNaoCadastradoMousePressed(evt);
            }
        });
        btn_PesquisarProdutoNaoCadastrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PesquisarProdutoNaoCadastradoActionPerformed(evt);
            }
        });

        btn_ConverteProdutoNaoCadastrado.setBackground(new java.awt.Color(255, 255, 255));
        btn_ConverteProdutoNaoCadastrado.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btn_ConverteProdutoNaoCadastrado.setForeground(new java.awt.Color(0, 102, 255));
        btn_ConverteProdutoNaoCadastrado.setText("CONVERTE");
        btn_ConverteProdutoNaoCadastrado.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_ConverteProdutoNaoCadastrado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_ConverteProdutoNaoCadastradoMousePressed(evt);
            }
        });
        btn_ConverteProdutoNaoCadastrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ConverteProdutoNaoCadastradoActionPerformed(evt);
            }
        });

        jDesktopPane5.setLayer(jPanel72, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(s, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(btn_addGrupoComNota, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(jPanel82, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(btn_gerarCodBarraComNota, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(a, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(jPanel75, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(jPanel77, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(jPanel74, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(jPanel81, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(jPanel79, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(jPanel113, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(jPanel73, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(btn_addMarcaComNota, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(btn_PesquisarProdutoNaoCadastrado, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane5.setLayer(btn_ConverteProdutoNaoCadastrado, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane5Layout = new javax.swing.GroupLayout(jDesktopPane5);
        jDesktopPane5.setLayout(jDesktopPane5Layout);
        jDesktopPane5Layout.setHorizontalGroup(
            jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane5Layout.createSequentialGroup()
                .addComponent(jPanel113, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_gerarCodBarraComNota, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jDesktopPane5Layout.createSequentialGroup()
                .addGroup(jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jDesktopPane5Layout.createSequentialGroup()
                        .addComponent(jPanel79, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel81, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel74, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(s, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDesktopPane5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btn_ConverteProdutoNaoCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_PesquisarProdutoNaoCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_addMarcaComNota)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDesktopPane5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_addGrupoComNota))))
        );
        jDesktopPane5Layout.setVerticalGroup(
            jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane5Layout.createSequentialGroup()
                .addGroup(jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel113, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDesktopPane5Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(btn_gerarCodBarraComNota)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(s, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane5Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_addMarcaComNota)
                            .addComponent(btn_addGrupoComNota)))
                    .addGroup(jDesktopPane5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDesktopPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_ConverteProdutoNaoCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_PesquisarProdutoNaoCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_salvarProdutoNaoCadastrado.setBackground(new java.awt.Color(255, 255, 255));
        btn_salvarProdutoNaoCadastrado.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btn_salvarProdutoNaoCadastrado.setText("SALVAR");
        btn_salvarProdutoNaoCadastrado.setEnabled(false);
        btn_salvarProdutoNaoCadastrado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_salvarProdutoNaoCadastradoMousePressed(evt);
            }
        });
        btn_salvarProdutoNaoCadastrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salvarProdutoNaoCadastradoActionPerformed(evt);
            }
        });

        btn_cancelarProdutoNaoCadastrado.setBackground(new java.awt.Color(255, 255, 255));
        btn_cancelarProdutoNaoCadastrado.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btn_cancelarProdutoNaoCadastrado.setText("CANCELAR");
        btn_cancelarProdutoNaoCadastrado.setEnabled(false);
        btn_cancelarProdutoNaoCadastrado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_cancelarProdutoNaoCadastradoMousePressed(evt);
            }
        });
        btn_cancelarProdutoNaoCadastrado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarProdutoNaoCadastradoActionPerformed(evt);
            }
        });

        jp_novos.setLayer(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jp_novos.setLayer(jDesktopPane5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jp_novos.setLayer(btn_salvarProdutoNaoCadastrado, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jp_novos.setLayer(btn_cancelarProdutoNaoCadastrado, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jp_novosLayout = new javax.swing.GroupLayout(jp_novos);
        jp_novos.setLayout(jp_novosLayout);
        jp_novosLayout.setHorizontalGroup(
            jp_novosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane5)
            .addGroup(jp_novosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_novosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_cancelarProdutoNaoCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btn_salvarProdutoNaoCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(352, 352, 352))
        );
        jp_novosLayout.setVerticalGroup(
            jp_novosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_novosLayout.createSequentialGroup()
                .addComponent(jDesktopPane5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_novosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_salvarProdutoNaoCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cancelarProdutoNaoCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jt_importarProdutos.addTab("       Novo       ", jp_novos);

        jp_conversão.setOpaque(false);

        jDesktopPane6.setBackground(new java.awt.Color(0, 51, 153));
        jDesktopPane6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jPanel90.setBackground(new java.awt.Color(197, 196, 195));
        jPanel90.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "Selecionado", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel90.setOpaque(false);

        jPanel96.setBackground(new java.awt.Color(197, 196, 195));
        jPanel96.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Quantidade", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel96.setOpaque(false);

        ct_quantidadeConvertNota.setBackground(new java.awt.Color(255, 255, 102));
        ct_quantidadeConvertNota.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_quantidadeConvertNota.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_quantidadeConvertNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_quantidadeConvertNota.setEnabled(false);
        ct_quantidadeConvertNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_quantidadeConvertNotaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel96Layout = new javax.swing.GroupLayout(jPanel96);
        jPanel96.setLayout(jPanel96Layout);
        jPanel96Layout.setHorizontalGroup(
            jPanel96Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_quantidadeConvertNota, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
        );
        jPanel96Layout.setVerticalGroup(
            jPanel96Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel96Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_quantidadeConvertNota, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        a4.setBackground(new java.awt.Color(197, 196, 195));
        a4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Unid. Medida", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        a4.setOpaque(false);

        cbox_unidadeMedida_itensConvertTblNota.setBackground(new java.awt.Color(255, 255, 102));
        cbox_unidadeMedida_itensConvertTblNota.setFont(new java.awt.Font("Arial Black", 1, 15)); // NOI18N
        cbox_unidadeMedida_itensConvertTblNota.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M² ", "M ", "LT", "SC", "CM", "UN", "CT ", "CX ", "PA", "PC", "PT", "RL ", "KG ", "G ", "M²", "MILH", "LAT", "CAÇ", "X " }));
        cbox_unidadeMedida_itensConvertTblNota.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cbox_unidadeMedida_itensConvertTblNota.setEnabled(false);

        javax.swing.GroupLayout a4Layout = new javax.swing.GroupLayout(a4);
        a4.setLayout(a4Layout);
        a4Layout.setHorizontalGroup(
            a4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_unidadeMedida_itensConvertTblNota, 0, 94, Short.MAX_VALUE)
        );
        a4Layout.setVerticalGroup(
            a4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbox_unidadeMedida_itensConvertTblNota, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel83.setBackground(new java.awt.Color(197, 196, 195));
        jPanel83.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Descrição do Produto", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel83.setOpaque(false);

        ct_descricao_itensConvertTblNota.setEditable(false);
        ct_descricao_itensConvertTblNota.setBackground(new java.awt.Color(255, 255, 153));
        ct_descricao_itensConvertTblNota.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        ct_descricao_itensConvertTblNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_descricao_itensConvertTblNota.setEnabled(false);

        javax.swing.GroupLayout jPanel83Layout = new javax.swing.GroupLayout(jPanel83);
        jPanel83.setLayout(jPanel83Layout);
        jPanel83Layout.setHorizontalGroup(
            jPanel83Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_descricao_itensConvertTblNota, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
        );
        jPanel83Layout.setVerticalGroup(
            jPanel83Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel83Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_descricao_itensConvertTblNota, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel93.setBackground(new java.awt.Color(197, 196, 195));
        jPanel93.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Preço Custo(R$)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel93.setOpaque(false);

        ct_vlrCusto_itensConvertTblNota.setBackground(new java.awt.Color(255, 255, 102));
        ct_vlrCusto_itensConvertTblNota.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_vlrCusto_itensConvertTblNota.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_vlrCusto_itensConvertTblNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_vlrCusto_itensConvertTblNota.setEnabled(false);
        ct_vlrCusto_itensConvertTblNota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ct_vlrCusto_itensConvertTblNotaMousePressed(evt);
            }
        });
        ct_vlrCusto_itensConvertTblNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_vlrCusto_itensConvertTblNotaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel93Layout = new javax.swing.GroupLayout(jPanel93);
        jPanel93.setLayout(jPanel93Layout);
        jPanel93Layout.setHorizontalGroup(
            jPanel93Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrCusto_itensConvertTblNota, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
        );
        jPanel93Layout.setVerticalGroup(
            jPanel93Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel93Layout.createSequentialGroup()
                .addGap(0, 31, Short.MAX_VALUE)
                .addComponent(ct_vlrCusto_itensConvertTblNota, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel90Layout = new javax.swing.GroupLayout(jPanel90);
        jPanel90.setLayout(jPanel90Layout);
        jPanel90Layout.setHorizontalGroup(
            jPanel90Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel90Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel93, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(239, 239, 239)
                .addComponent(jPanel96, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(a4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel90Layout.setVerticalGroup(
            jPanel90Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel90Layout.createSequentialGroup()
                .addGroup(jPanel90Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel90Layout.createSequentialGroup()
                        .addComponent(a4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 28, Short.MAX_VALUE))
                    .addComponent(jPanel96, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel83, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel90Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel93, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jDesktopPane6.setLayer(jPanel90, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane6Layout = new javax.swing.GroupLayout(jDesktopPane6);
        jDesktopPane6.setLayout(jDesktopPane6Layout);
        jDesktopPane6Layout.setHorizontalGroup(
            jDesktopPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDesktopPane6Layout.setVerticalGroup(
            jDesktopPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane6Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel76.setBackground(new java.awt.Color(204, 255, 204));
        jPanel76.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Cadastrado", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14))); // NOI18N

        jPanel78.setBackground(new java.awt.Color(204, 255, 204));
        jPanel78.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Descrição do Produto", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13))); // NOI18N

        ct_descricao_itensConvertTblProd.setEditable(false);
        ct_descricao_itensConvertTblProd.setBackground(new java.awt.Color(255, 255, 102));
        ct_descricao_itensConvertTblProd.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        ct_descricao_itensConvertTblProd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_descricao_itensConvertTblProd.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_descricao_itensConvertTblProd.setEnabled(false);
        ct_descricao_itensConvertTblProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_descricao_itensConvertTblProdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel78Layout = new javax.swing.GroupLayout(jPanel78);
        jPanel78.setLayout(jPanel78Layout);
        jPanel78Layout.setHorizontalGroup(
            jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_descricao_itensConvertTblProd, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
        );
        jPanel78Layout.setVerticalGroup(
            jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_descricao_itensConvertTblProd)
        );

        jPanel85.setBackground(new java.awt.Color(204, 255, 204));
        jPanel85.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Preço Venda(R$)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13))); // NOI18N

        ct_vlrVenda_itensConvertTblProd.setEditable(false);
        ct_vlrVenda_itensConvertTblProd.setBackground(new java.awt.Color(255, 255, 102));
        ct_vlrVenda_itensConvertTblProd.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_vlrVenda_itensConvertTblProd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_vlrVenda_itensConvertTblProd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_vlrVenda_itensConvertTblProd.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_vlrVenda_itensConvertTblProd.setEnabled(false);

        javax.swing.GroupLayout jPanel85Layout = new javax.swing.GroupLayout(jPanel85);
        jPanel85.setLayout(jPanel85Layout);
        jPanel85Layout.setHorizontalGroup(
            jPanel85Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrVenda_itensConvertTblProd, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        );
        jPanel85Layout.setVerticalGroup(
            jPanel85Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_vlrVenda_itensConvertTblProd)
        );

        jPanel86.setBackground(new java.awt.Color(204, 255, 204));
        jPanel86.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Margem (%)", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13))); // NOI18N

        ct_lucro_itensConvertTblProd.setEditable(false);
        ct_lucro_itensConvertTblProd.setBackground(new java.awt.Color(255, 255, 102));
        ct_lucro_itensConvertTblProd.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_lucro_itensConvertTblProd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_lucro_itensConvertTblProd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_lucro_itensConvertTblProd.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_lucro_itensConvertTblProd.setEnabled(false);
        ct_lucro_itensConvertTblProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_lucro_itensConvertTblProdKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel86Layout = new javax.swing.GroupLayout(jPanel86);
        jPanel86.setLayout(jPanel86Layout);
        jPanel86Layout.setHorizontalGroup(
            jPanel86Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_lucro_itensConvertTblProd, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
        );
        jPanel86Layout.setVerticalGroup(
            jPanel86Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_lucro_itensConvertTblProd)
        );

        a5.setBackground(new java.awt.Color(204, 255, 204));
        a5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Unid. Medida", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13))); // NOI18N

        ct_unidMedBan.setEditable(false);
        ct_unidMedBan.setBackground(new java.awt.Color(102, 51, 255));
        ct_unidMedBan.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_unidMedBan.setForeground(new java.awt.Color(255, 255, 0));
        ct_unidMedBan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_unidMedBan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_unidMedBan.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_unidMedBan.setEnabled(false);

        javax.swing.GroupLayout a5Layout = new javax.swing.GroupLayout(a5);
        a5.setLayout(a5Layout);
        a5Layout.setHorizontalGroup(
            a5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_unidMedBan, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
        );
        a5Layout.setVerticalGroup(
            a5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_unidMedBan)
        );

        jPanel88.setBackground(new java.awt.Color(197, 196, 195));
        jPanel88.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Código de Barras", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14))); // NOI18N
        jPanel88.setOpaque(false);

        ct_codBarra_convert.setBackground(new java.awt.Color(255, 255, 102));
        ct_codBarra_convert.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        ct_codBarra_convert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_codBarra_convert.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_codBarra_convert.setEnabled(false);
        ct_codBarra_convert.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_codBarra_convertKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel88Layout = new javax.swing.GroupLayout(jPanel88);
        jPanel88.setLayout(jPanel88Layout);
        jPanel88Layout.setHorizontalGroup(
            jPanel88Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_codBarra_convert)
        );
        jPanel88Layout.setVerticalGroup(
            jPanel88Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel88Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ct_codBarra_convert, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel76Layout = new javax.swing.GroupLayout(jPanel76);
        jPanel76.setLayout(jPanel76Layout);
        jPanel76Layout.setHorizontalGroup(
            jPanel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel76Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel88, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel78, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel86, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel85, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel76Layout.setVerticalGroup(
            jPanel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel86, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(a5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel76Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel88, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Lista de Conversão", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16))); // NOI18N
        jPanel3.setOpaque(false);

        tbl_itensConvete.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbl_itensConvete.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        tbl_itensConvete.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_itensConvete.setIntercellSpacing(new java.awt.Dimension(2, 2));
        tbl_itensConvete.setOpaque(false);
        tbl_itensConvete.setSelectionBackground(new java.awt.Color(255, 255, 102));
        tbl_itensConvete.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_itensConvete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_itensConveteMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbl_itensConveteMousePressed(evt);
            }
        });
        jScrollPane5.setViewportView(tbl_itensConvete);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
        );

        btn_cancelarConvert.setBackground(new java.awt.Color(255, 255, 255));
        btn_cancelarConvert.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_cancelarConvert.setText("Cancelar");
        btn_cancelarConvert.setEnabled(false);
        btn_cancelarConvert.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_cancelarConvertMousePressed(evt);
            }
        });
        btn_cancelarConvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarConvertActionPerformed(evt);
            }
        });

        btn_salvarConvert.setBackground(new java.awt.Color(255, 255, 255));
        btn_salvarConvert.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_salvarConvert.setText("Salvar");
        btn_salvarConvert.setEnabled(false);
        btn_salvarConvert.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_salvarConvertMousePressed(evt);
            }
        });
        btn_salvarConvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salvarConvertActionPerformed(evt);
            }
        });

        jp_conversão.setLayer(jDesktopPane6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jp_conversão.setLayer(jPanel76, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jp_conversão.setLayer(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jp_conversão.setLayer(btn_cancelarConvert, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jp_conversão.setLayer(btn_salvarConvert, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jp_conversãoLayout = new javax.swing.GroupLayout(jp_conversão);
        jp_conversão.setLayout(jp_conversãoLayout);
        jp_conversãoLayout.setHorizontalGroup(
            jp_conversãoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane6)
            .addComponent(jPanel76, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jp_conversãoLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_conversãoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_cancelarConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_salvarConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(386, 386, 386))
        );
        jp_conversãoLayout.setVerticalGroup(
            jp_conversãoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_conversãoLayout.createSequentialGroup()
                .addComponent(jPanel76, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDesktopPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jp_conversãoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_cancelarConvert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_salvarConvert, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jt_importarProdutos.addTab(" Conversão ", jp_conversão);

        jp_sem_codigo_barra.setBackground(new java.awt.Color(0, 51, 204));

        jTabbedPane2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jPanel98.setBackground(new java.awt.Color(197, 196, 195));
        jPanel98.setOpaque(false);

        btn_novoItensSemCodigo.setBackground(new java.awt.Color(255, 255, 255));
        btn_novoItensSemCodigo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_novoItensSemCodigo.setText("NOVO");
        btn_novoItensSemCodigo.setEnabled(false);
        btn_novoItensSemCodigo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_novoItensSemCodigoMousePressed(evt);
            }
        });
        btn_novoItensSemCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_novoItensSemCodigoActionPerformed(evt);
            }
        });

        btn_cadastradoItensSemCodigo.setBackground(new java.awt.Color(255, 255, 255));
        btn_cadastradoItensSemCodigo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_cadastradoItensSemCodigo.setText("CADASTRADO");
        btn_cadastradoItensSemCodigo.setEnabled(false);
        btn_cadastradoItensSemCodigo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_cadastradoItensSemCodigoMousePressed(evt);
            }
        });
        btn_cadastradoItensSemCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cadastradoItensSemCodigoActionPerformed(evt);
            }
        });

        btn_cancelarProdutosSemCodigo.setBackground(new java.awt.Color(255, 255, 255));
        btn_cancelarProdutosSemCodigo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_cancelarProdutosSemCodigo.setText("CANCELAR");
        btn_cancelarProdutosSemCodigo.setEnabled(false);
        btn_cancelarProdutosSemCodigo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_cancelarProdutosSemCodigoMousePressed(evt);
            }
        });
        btn_cancelarProdutosSemCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarProdutosSemCodigoActionPerformed(evt);
            }
        });

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Lista de pesquisa", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16))); // NOI18N
        jPanel8.setOpaque(false);

        tbl_itensSemCodBarra.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbl_itensSemCodBarra.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        tbl_itensSemCodBarra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_itensSemCodBarra.setIntercellSpacing(new java.awt.Dimension(4, 4));
        tbl_itensSemCodBarra.setOpaque(false);
        tbl_itensSemCodBarra.setSelectionBackground(new java.awt.Color(255, 255, 102));
        tbl_itensSemCodBarra.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_itensSemCodBarra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_itensSemCodBarraMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbl_itensSemCodBarraMousePressed(evt);
            }
        });
        jScrollPane7.setViewportView(tbl_itensSemCodBarra);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel98Layout = new javax.swing.GroupLayout(jPanel98);
        jPanel98.setLayout(jPanel98Layout);
        jPanel98Layout.setHorizontalGroup(
            jPanel98Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel98Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel98Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_cancelarProdutosSemCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_novoItensSemCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_cadastradoItensSemCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(285, 285, 285))
        );
        jPanel98Layout.setVerticalGroup(
            jPanel98Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel98Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel98Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cancelarProdutosSemCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_cadastradoItensSemCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_novoItensSemCodigo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("        ITENS DE PESQUISA        ", jPanel98);

        jPanel99.setBackground(new java.awt.Color(197, 196, 195));
        jPanel99.setOpaque(false);

        ct_pesquisar1.setBackground(new java.awt.Color(0, 238, 255));
        ct_pesquisar1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        ct_pesquisar1.setText("Caixa de Pesquisa...");
        ct_pesquisar1.setToolTipText("Digite o nome do produto ou código de barra");
        ct_pesquisar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ct_pesquisar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ct_pesquisar1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ct_pesquisar1MouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ct_pesquisar1MousePressed(evt);
            }
        });
        ct_pesquisar1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ct_pesquisar1ComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                ct_pesquisar1ComponentShown(evt);
            }
        });
        ct_pesquisar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_pesquisar1ActionPerformed(evt);
            }
        });
        ct_pesquisar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_pesquisar1KeyReleased(evt);
            }
        });

        jPanel55.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Lista de Produtos", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 16))); // NOI18N
        jPanel55.setOpaque(false);

        tbm_itensCadastradosProdSemCodBarra.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbm_itensCadastradosProdSemCodBarra.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        tbm_itensCadastradosProdSemCodBarra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbm_itensCadastradosProdSemCodBarra.setIntercellSpacing(new java.awt.Dimension(4, 4));
        tbm_itensCadastradosProdSemCodBarra.setOpaque(false);
        tbm_itensCadastradosProdSemCodBarra.setSelectionBackground(new java.awt.Color(255, 255, 102));
        tbm_itensCadastradosProdSemCodBarra.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbm_itensCadastradosProdSemCodBarra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbm_itensCadastradosProdSemCodBarraMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbm_itensCadastradosProdSemCodBarraMousePressed(evt);
            }
        });
        jScrollPane8.setViewportView(tbm_itensCadastradosProdSemCodBarra);

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8)
                .addContainerGap())
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel99Layout = new javax.swing.GroupLayout(jPanel99);
        jPanel99.setLayout(jPanel99Layout);
        jPanel99Layout.setHorizontalGroup(
            jPanel99Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_pesquisar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1035, Short.MAX_VALUE)
            .addGroup(jPanel99Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel99Layout.setVerticalGroup(
            jPanel99Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel99Layout.createSequentialGroup()
                .addComponent(ct_pesquisar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("PRODUTOS CADASTRADOS", jPanel99);

        jDesktopPane8.setBackground(new java.awt.Color(0, 51, 255));
        jDesktopPane8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "Selecionado", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jPanel91.setBackground(new java.awt.Color(204, 255, 204));
        jPanel91.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Código de Barras", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel91.setOpaque(false);

        ct_codBarraSemcodigoBarra.setEditable(false);
        ct_codBarraSemcodigoBarra.setBackground(new java.awt.Color(252, 252, 146));
        ct_codBarraSemcodigoBarra.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        ct_codBarraSemcodigoBarra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_codBarraSemcodigoBarra.setEnabled(false);
        ct_codBarraSemcodigoBarra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_codBarraSemcodigoBarraActionPerformed(evt);
            }
        });
        ct_codBarraSemcodigoBarra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ct_codBarraSemcodigoBarraKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel91Layout = new javax.swing.GroupLayout(jPanel91);
        jPanel91.setLayout(jPanel91Layout);
        jPanel91Layout.setHorizontalGroup(
            jPanel91Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_codBarraSemcodigoBarra, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
        );
        jPanel91Layout.setVerticalGroup(
            jPanel91Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_codBarraSemcodigoBarra)
        );

        jPanel89.setBackground(new java.awt.Color(204, 255, 204));
        jPanel89.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Descrição do Produto", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel89.setOpaque(false);

        ct_descricao_itensSemCodigo.setEditable(false);
        ct_descricao_itensSemCodigo.setBackground(new java.awt.Color(204, 204, 255));
        ct_descricao_itensSemCodigo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_descricao_itensSemCodigo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_descricao_itensSemCodigo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ct_descricao_itensSemCodigo.setEnabled(false);
        ct_descricao_itensSemCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_descricao_itensSemCodigoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel89Layout = new javax.swing.GroupLayout(jPanel89);
        jPanel89.setLayout(jPanel89Layout);
        jPanel89Layout.setHorizontalGroup(
            jPanel89Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_descricao_itensSemCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
        );
        jPanel89Layout.setVerticalGroup(
            jPanel89Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_descricao_itensSemCodigo)
        );

        jPanel92.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Código", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel92.setOpaque(false);

        ct_numItemItensSemCodigo.setEditable(false);
        ct_numItemItensSemCodigo.setBackground(new java.awt.Color(204, 204, 255));
        ct_numItemItensSemCodigo.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        ct_numItemItensSemCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_numItemItensSemCodigo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        ct_numItemItensSemCodigo.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        ct_numItemItensSemCodigo.setEnabled(false);
        ct_numItemItensSemCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_numItemItensSemCodigoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel92Layout = new javax.swing.GroupLayout(jPanel92);
        jPanel92.setLayout(jPanel92Layout);
        jPanel92Layout.setHorizontalGroup(
            jPanel92Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_numItemItensSemCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel92Layout.setVerticalGroup(
            jPanel92Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ct_numItemItensSemCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        btn_colarCodBarraitensSemCodBarra.setBackground(new java.awt.Color(255, 255, 255));
        btn_colarCodBarraitensSemCodBarra.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btn_colarCodBarraitensSemCodBarra.setText("Colar");
        btn_colarCodBarraitensSemCodBarra.setEnabled(false);
        btn_colarCodBarraitensSemCodBarra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_colarCodBarraitensSemCodBarraMousePressed(evt);
            }
        });
        btn_colarCodBarraitensSemCodBarra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_colarCodBarraitensSemCodBarraActionPerformed(evt);
            }
        });

        jDesktopPane8.setLayer(jPanel91, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane8.setLayer(jPanel89, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane8.setLayer(jPanel92, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane8.setLayer(btn_colarCodBarraitensSemCodBarra, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane8Layout = new javax.swing.GroupLayout(jDesktopPane8);
        jDesktopPane8.setLayout(jDesktopPane8Layout);
        jDesktopPane8Layout.setHorizontalGroup(
            jDesktopPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane8Layout.createSequentialGroup()
                .addComponent(jPanel92, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel89, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jPanel91, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_colarCodBarraitensSemCodBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jDesktopPane8Layout.setVerticalGroup(
            jDesktopPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDesktopPane8Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(btn_colarCodBarraitensSemCodBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel92, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel91, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jp_sem_codigo_barra.setLayer(jTabbedPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jp_sem_codigo_barra.setLayer(jDesktopPane8, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jp_sem_codigo_barraLayout = new javax.swing.GroupLayout(jp_sem_codigo_barra);
        jp_sem_codigo_barra.setLayout(jp_sem_codigo_barraLayout);
        jp_sem_codigo_barraLayout.setHorizontalGroup(
            jp_sem_codigo_barraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jDesktopPane8)
        );
        jp_sem_codigo_barraLayout.setVerticalGroup(
            jp_sem_codigo_barraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_sem_codigo_barraLayout.createSequentialGroup()
                .addComponent(jDesktopPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jt_importarProdutos.addTab("  Pesquisar  ", jp_sem_codigo_barra);

        jDesktopPane3.setLayer(jt_importarProdutos, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane3Layout = new javax.swing.GroupLayout(jDesktopPane3);
        jDesktopPane3.setLayout(jDesktopPane3Layout);
        jDesktopPane3Layout.setHorizontalGroup(
            jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jt_importarProdutos)
        );
        jDesktopPane3Layout.setVerticalGroup(
            jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jt_importarProdutos, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jDesktopPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDesktopPane3))
        );

        jTabbedPane1.addTab("     Com Nota    ", jPanel4);

        jDesktopPane9.setLayer(jTabbedPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane9Layout = new javax.swing.GroupLayout(jDesktopPane9);
        jDesktopPane9.setLayout(jDesktopPane9Layout);
        jDesktopPane9Layout.setHorizontalGroup(
            jDesktopPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jDesktopPane9Layout.setVerticalGroup(
            jDesktopPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane9)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jDesktopPane9, javax.swing.GroupLayout.Alignment.LEADING)
        );

        setSize(new java.awt.Dimension(1068, 572));
    }// </editor-fold>//GEN-END:initComponents

    public void manipularInterface() { //MANIPULA A NAVEGAÇÃO 
        switch (modoNavegar) {

            case "Inicializar":
                ct_cod.setText("");
                ct_produto.setText("");
                ct_quantidade.setText("");
                ct_preco_compra.setText("");
                ct_preco_venda.setText("");
                ct_codBarra.setText("");
                ct_percentual.setText("");
                ct_vlr_ganho.setText("");
                ct_qtdMinEstq.setText("");
                cbox_marca.setSelectedItem(null);
                cbox_grupo.setSelectedItem(null);
                cbox_unidade.setSelectedItem(null);

                ct_produto.setBackground(new Color(255, 255, 102));
                ct_codBarra.setBackground(new Color(255, 255, 102));
                cbox_unidade.setBorder(new LineBorder(null));
                cbox_marca.setBorder(new LineBorder(null));
                cbox_grupo.setBorder(new LineBorder(null));
                cbox_fornecedor.setBorder(new LineBorder(null));
                ct_quantidade.setBackground(new Color(255, 255, 102));
                ct_qtdMinEstq.setBackground(new Color(255, 255, 102));
                ct_preco_compra.setBackground(new Color(255, 255, 102));
                ct_percentual.setBackground(new Color(255, 255, 102));
                ct_vlr_ganho.setBackground(new Color(255, 255, 102));
                ct_preco_venda.setBackground(new Color(255, 255, 102));

                btn_addMarcaSemNota.setVisible(false);
                btn_addGrupoSemNota.setVisible(false);
                btn_addFornecedorSemNota.setVisible(false);
                btn_gerarCodBarraSemNota.setVisible(false);

                btn_addMarcaSemNota.setEnabled(false);
                btn_addGrupoSemNota.setEnabled(false);
                btn_addFornecedorSemNota.setEnabled(false);
                btn_gerarCodBarraSemNota.setEnabled(false);

                ct_qtdMinEstq.setEnabled(false);
                cbox_unidade.setEnabled(false);
                cbox_marca.setEnabled(false);
                cbox_grupo.setEnabled(false);
                ct_percentual.setEnabled(false);
                cbox_fornecedor.setSelectedItem(null);
                ct_codBarra.setEnabled(false);
                ct_cod.setEnabled(false);
                ct_produto.setEnabled(false);
                ct_quantidade.setEnabled(false);
                ct_preco_compra.setEnabled(false);
                ct_preco_venda.setEnabled(false);
                cbox_fornecedor.setEnabled(false);

                btn_novo.setEnabled(true);
                ;
                btn_salvar.setEnabled(false);
                btn_editar.setEnabled(false);
                btn_excluir.setEnabled(false);
                btn_cancelar.setEnabled(false);
                break;

            case "Navegar":
                ct_cod.setText("");
                ct_produto.setText("");
                ct_quantidade.setText("");
                ct_preco_compra.setText("");
                ct_preco_venda.setText("");
                ct_codBarra.setText("");
                ct_percentual.setText("");
                ct_vlr_ganho.setText("");
                ct_qtdMinEstq.setText("");
                cbox_marca.setSelectedItem(null);
                cbox_grupo.setSelectedItem(null);
                cbox_unidade.setSelectedItem(null);

                btn_addMarcaSemNota.setVisible(false);
                btn_addGrupoSemNota.setVisible(false);
                btn_addFornecedorSemNota.setVisible(false);
                btn_gerarCodBarraSemNota.setVisible(false);

                btn_addMarcaSemNota.setEnabled(false);
                btn_addGrupoSemNota.setEnabled(false);
                btn_addFornecedorSemNota.setEnabled(false);
                btn_gerarCodBarraSemNota.setEnabled(false);

                ct_produto.setBackground(new Color(255, 255, 102));
                ct_codBarra.setBackground(new Color(255, 255, 102));
                cbox_unidade.setBorder(new LineBorder(null));
                cbox_marca.setBorder(new LineBorder(null));
                cbox_grupo.setBorder(new LineBorder(null));
                cbox_fornecedor.setBorder(new LineBorder(null));
                ct_quantidade.setBackground(new Color(255, 255, 102));
                ct_qtdMinEstq.setBackground(new Color(255, 255, 102));
                ct_preco_compra.setBackground(new Color(255, 255, 102));
                ct_percentual.setBackground(new Color(255, 255, 102));
                ct_vlr_ganho.setBackground(new Color(255, 255, 102));
                ct_preco_venda.setBackground(new Color(255, 255, 102));

                cbox_unidade.setEnabled(false);
                cbox_marca.setEnabled(false);
                cbox_grupo.setEnabled(false);

                ct_qtdMinEstq.setEnabled(false);
                ct_codBarra.setEnabled(false);
                ct_cod.setEnabled(false);
                ct_produto.setEnabled(false);
                ct_quantidade.setEnabled(false);
                ct_preco_compra.setEnabled(false);
                ct_preco_venda.setEnabled(false);
                cbox_fornecedor.setEnabled(false);
                cbox_fornecedor.setSelectedItem(null);
                btn_novo.setEnabled(true);
                ;
                btn_salvar.setEnabled(false);
                btn_editar.setEnabled(true);
                btn_excluir.setEnabled(true);
                btn_cancelar.setEnabled(true);
                ct_percentual.setEnabled(false);
                break;

            case "Novo":
                ct_cod.setText("");
                ct_produto.setText("");
                ct_quantidade.setText("");
                ct_preco_compra.setText("");
                ct_preco_venda.setText("");
                ct_codBarra.setText("");
                ct_percentual.setText("");
                ct_vlr_ganho.setText("");
                ct_qtdMinEstq.setText("");
                cbox_fornecedor.setSelectedItem(null);
                cbox_marca.setSelectedItem(null);
                cbox_grupo.setSelectedItem(null);
                cbox_unidade.setSelectedItem(null);
                ct_qtdMinEstq.setEnabled(true);
                cbox_unidade.setEnabled(true);
                cbox_marca.setEnabled(true);
                cbox_grupo.setEnabled(true);
                ct_percentual.setEnabled(true);
                ct_codBarra.setEnabled(true);
                ct_cod.setEnabled(false);
                ct_produto.setEnabled(true);
                ct_quantidade.setEnabled(true);
                ct_preco_compra.setEnabled(true);
                ct_preco_venda.setEnabled(false);
                cbox_fornecedor.setEnabled(true);

                btn_addMarcaSemNota.setVisible(true);
                btn_addGrupoSemNota.setVisible(true);
                btn_addFornecedorSemNota.setVisible(true);
                btn_gerarCodBarraSemNota.setVisible(true);

                ct_produto.setBackground(new Color(255, 255, 102));
                ct_codBarra.setBackground(new Color(255, 255, 102));
                cbox_unidade.setBorder(new LineBorder(null));
                cbox_marca.setBorder(new LineBorder(null));
                cbox_grupo.setBorder(new LineBorder(null));
                cbox_fornecedor.setBorder(new LineBorder(null));
                ct_quantidade.setBackground(new Color(255, 255, 102));
                ct_qtdMinEstq.setBackground(new Color(255, 255, 102));
                ct_preco_compra.setBackground(new Color(255, 255, 102));
                ct_percentual.setBackground(new Color(255, 255, 102));
                ct_vlr_ganho.setBackground(new Color(255, 255, 102));
                ct_preco_venda.setBackground(new Color(255, 255, 102));

                btn_addMarcaSemNota.setEnabled(true);
                btn_addGrupoSemNota.setEnabled(true);
                btn_addFornecedorSemNota.setEnabled(true);
                btn_gerarCodBarraSemNota.setEnabled(true);

                btn_novo.setEnabled(false);
                ;
                btn_salvar.setEnabled(true);
                btn_editar.setEnabled(false);
                btn_excluir.setEnabled(false);
                btn_cancelar.setEnabled(true);
                break;

            case "Editar":
                ct_qtdMinEstq.setEnabled(true);
                cbox_unidade.setEnabled(true);
                cbox_marca.setEnabled(true);
                cbox_grupo.setEnabled(true);
                ct_percentual.setEnabled(true);
                ct_codBarra.setEnabled(true);
                ct_cod.setEnabled(false);
                ct_produto.setEnabled(true);
                ct_quantidade.setEnabled(false);
                ct_preco_compra.setEnabled(true);
                ct_preco_venda.setEnabled(false);
                cbox_fornecedor.setEnabled(true);

                ct_produto.setBackground(new Color(255, 255, 102));
                ct_codBarra.setBackground(new Color(255, 255, 102));
                cbox_unidade.setBorder(new LineBorder(null));
                cbox_marca.setBorder(new LineBorder(null));
                cbox_grupo.setBorder(new LineBorder(null));
                cbox_fornecedor.setBorder(new LineBorder(null));
                ct_quantidade.setBackground(new Color(255, 255, 102));
                ct_qtdMinEstq.setBackground(new Color(255, 255, 102));
                ct_preco_compra.setBackground(new Color(255, 255, 102));
                ct_percentual.setBackground(new Color(255, 255, 102));
                ct_vlr_ganho.setBackground(new Color(255, 255, 102));
                ct_preco_venda.setBackground(new Color(255, 255, 102));

                btn_addMarcaSemNota.setVisible(true);
                btn_addGrupoSemNota.setVisible(true);
                btn_addFornecedorSemNota.setVisible(true);
                btn_gerarCodBarraSemNota.setVisible(true);

                btn_addMarcaSemNota.setEnabled(true);
                btn_addGrupoSemNota.setEnabled(true);
                btn_addFornecedorSemNota.setEnabled(true);
                btn_gerarCodBarraSemNota.setEnabled(true);

                btn_novo.setEnabled(false);
                ;
                btn_salvar.setEnabled(true);
                btn_editar.setEnabled(false);
                btn_excluir.setEnabled(true);
                btn_cancelar.setEnabled(true);
                break;

            default:
                break;
        }
    }

    public void preencheComboFornecedor() {
        conecBanc.conexaoBanco();
        conecBanc.executaSQL("select * from fornecedores order by nome_fornecedor");
        try {
            conecBanc.rs.first();
            cbox_fornecedor.removeAllItems();
            do {
                cbox_fornecedor.addItem(conecBanc.rs.getString("nome_fornecedor"));
            } while (conecBanc.rs.next());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao preencher combobox" + ex);
        }
        conecBanc.desconectar();
    }

    public void preencheComboMarca() {
        conecBanc.conexaoBanco();
        conecBanc.executaSQL("select * from marca order by nome_marca");
        try {
            conecBanc.rs.first();
            cbox_marca.removeAllItems();
            cbox_marca_itensSem.removeAllItems();
            do {
                cbox_marca.addItem(conecBanc.rs.getString("nome_marca"));
                cbox_marca_itensSem.addItem(conecBanc.rs.getString("nome_marca"));
            } while (conecBanc.rs.next());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao preencher combobox" + ex);
        }
        conecBanc.desconectar();
    }

    public void preencheComboGrupo() {
        conecBanc.conexaoBanco();
        conecBanc.executaSQL("select * from grupo_produto order by nome_grupo");
        try {
            conecBanc.rs.first();
            cbox_grupo.removeAllItems();
            cbox_grupo_itensSem.removeAllItems();
            do {
                cbox_grupo.addItem(conecBanc.rs.getString("nome_grupo"));
                cbox_grupo_itensSem.addItem(conecBanc.rs.getString("nome_grupo"));
            } while (conecBanc.rs.next());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao preencher combobox" + ex);
        }
        conecBanc.desconectar();
    }

    public void preecherTabelaItensImportadosNovo(String SQL) { // METODO PARA PREENCHER TABELA NA INTERFACE
        ArrayList dados = new ArrayList();
        String[] Colunas = new String[]{"Código ", "Descrição", "Qtd.", "Uni. Comercial", "Valor Unit.(R$)", "Valor Total(R$)"};
        conecBanc.conexaoBanco();
        conecPesquisa.conexaoBanco();
        conecBanc.executaSQL(SQL);
        try {
            conecBanc.rs.first();
            do {
                dados.add(new Object[]{conecBanc.rs.getString("id_produto"), conecBanc.rs.getString("nome_prod"), conecBanc.rs.getInt("qtd_recebida"), conecBanc.rs.getString("unid_medida"), numberFormat.format(conecBanc.rs.getFloat("vlr_unit_prod")), numberFormat.format(conecBanc.rs.getFloat("vlr_total_prod"))});

            } while (conecBanc.rs.next());

        } catch (SQLException ex) {
        }

        ModeloTabela modelo = new ModeloTabela(dados, Colunas);
        tbl_itensSemCadastro.setModel(modelo);
        DefaultTableCellRenderer headerRenderer2 = new DefaultTableCellRenderer();
        headerRenderer2.setOpaque(true);
        headerRenderer2.setBackground(new Color(0, 0, 51));
        headerRenderer2.setFont(new java.awt.Font("Arial Black", Font.BOLD, 23));
        headerRenderer2.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < tbl_itensSemCadastro.getModel().getColumnCount();
                i++) {
            tbl_itensSemCadastro.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer2);
        }

        tbl_itensSemCadastro.setRowHeight(32); //  TAMANHO DA JANELA PRA CADA ITEM

        tbl_itensSemCadastro.getColumnModel().getColumn(0).setPreferredWidth(70);
        tbl_itensSemCadastro.getColumnModel().getColumn(0).setResizable(false);
        tbl_itensSemCadastro.getColumnModel().getColumn(1).setPreferredWidth(435);
        tbl_itensSemCadastro.getColumnModel().getColumn(1).setResizable(false);
        tbl_itensSemCadastro.getColumnModel().getColumn(2).setPreferredWidth(90);
        tbl_itensSemCadastro.getColumnModel().getColumn(2).setResizable(false);
        tbl_itensSemCadastro.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbl_itensSemCadastro.getColumnModel().getColumn(3).setResizable(false);
        tbl_itensSemCadastro.getColumnModel().getColumn(4).setPreferredWidth(120);
        tbl_itensSemCadastro.getColumnModel().getColumn(4).setResizable(false);
        tbl_itensSemCadastro.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbl_itensSemCadastro.getColumnModel().getColumn(5).setResizable(false);
//        tbl_itensSemCadastro.setAutoResizeMode(tbl_itensImportados.AUTO_RESIZE_OFF);
        tbl_itensSemCadastro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conecBanc.desconectar();
        conecPesquisa.desconectar();
    }//  FINAL METODO PARA IMPRIMIR TABELA NA INTERFACE

    public void preecherTabelaItensImportadosPesquisar(String SQL) { // METODO PARA PREENCHER TABELA NA INTERFACE
        ArrayList dados = new ArrayList();
        String[] Colunas = new String[]{"Código ", "Descrição", "Qtd.", "Uni. Comercial", "Valor Unit.(R$)", "Valor Total(R$)"};
        conecBanc.conexaoBanco();
        conecPesquisa.conexaoBanco();
        conecBanc.executaSQL(SQL);
        try {
            conecBanc.rs.first();

            do {
                dados.add(new Object[]{conecBanc.rs.getString("id_produto"), conecBanc.rs.getString("nome_prod"), conecBanc.rs.getInt("qtd_recebida"), conecBanc.rs.getString("unid_medida"), numberFormat.format(conecBanc.rs.getFloat("vlr_unit_prod")), numberFormat.format(conecBanc.rs.getFloat("vlr_total_prod"))});

            } while (conecBanc.rs.next());
        } catch (SQLException ex) {
        }

        ModeloTabela modelo = new ModeloTabela(dados, Colunas);
        tbl_itensSemCodBarra.setModel(modelo);
        DefaultTableCellRenderer headerRenderer2 = new DefaultTableCellRenderer();
        headerRenderer2.setOpaque(true);
        headerRenderer2.setBackground(new Color(0, 0, 51));
        headerRenderer2.setFont(new java.awt.Font("Arial Black", Font.BOLD, 23));
        headerRenderer2.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < tbl_itensSemCodBarra.getModel().getColumnCount();
                i++) {
            tbl_itensSemCodBarra.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer2);
        }

        tbl_itensSemCodBarra.setRowHeight(40); //  TAMANHO DA JANELA PRA CADA ITEM

        tbl_itensSemCodBarra.getColumnModel().getColumn(0).setPreferredWidth(70);
        tbl_itensSemCodBarra.getColumnModel().getColumn(0).setResizable(false);
        tbl_itensSemCodBarra.getColumnModel().getColumn(1).setPreferredWidth(435);
        tbl_itensSemCodBarra.getColumnModel().getColumn(1).setResizable(false);
        tbl_itensSemCodBarra.getColumnModel().getColumn(2).setPreferredWidth(90);
        tbl_itensSemCodBarra.getColumnModel().getColumn(2).setResizable(false);
        tbl_itensSemCodBarra.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbl_itensSemCodBarra.getColumnModel().getColumn(3).setResizable(false);
        tbl_itensSemCodBarra.getColumnModel().getColumn(4).setPreferredWidth(120);
        tbl_itensSemCodBarra.getColumnModel().getColumn(4).setResizable(false);
        tbl_itensSemCodBarra.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbl_itensSemCodBarra.getColumnModel().getColumn(5).setResizable(false);
//        tbl_itensSemCadastro.setAutoResizeMode(tbl_itensImportados.AUTO_RESIZE_OFF);
        tbl_itensSemCodBarra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conecBanc.desconectar();
        conecPesquisa.desconectar();
    }//  FINAL METODO PARA IMPRIMIR TABELA NA INTERFACE

    public void preecherTabelaItensImportadosConverte(String SQL) { // METODO PARA PREENCHER TABELA NA INTERFACE
        ArrayList dados = new ArrayList();
        String[] Colunas = new String[]{"Código ", "Descrição", "Qtd.", "Uni. Comercial", "Vlr. Unit.(R$)", "Vlr. Total(R$)"};
        conecBanc.conexaoBanco();
        conecPesquisa.conexaoBanco();
        conecBanc.executaSQL(SQL);
        try {
            conecBanc.rs.first();

            do {
                dados.add(new Object[]{conecBanc.rs.getInt("id_produto"), conecBanc.rs.getString("nome_prod"), conecBanc.rs.getInt("qtd_recebida"), conecBanc.rs.getString("unid_medida"), numberFormat.format(conecBanc.rs.getFloat("vlr_unit_prod")), numberFormat.format(conecBanc.rs.getFloat("vlr_total_prod"))});

            } while (conecBanc.rs.next());

        } catch (SQLException ex) {
        }

        ModeloTabela modelo = new ModeloTabela(dados, Colunas);
        tbl_itensConvete.setModel(modelo);
        DefaultTableCellRenderer headerRenderer2 = new DefaultTableCellRenderer();
        headerRenderer2.setOpaque(true);
        headerRenderer2.setBackground(new Color(0, 0, 51));
        headerRenderer2.setFont(new java.awt.Font("Arial Black", Font.BOLD, 23));
        headerRenderer2.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < tbl_itensConvete.getModel().getColumnCount();
                i++) {
            tbl_itensConvete.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer2);
        }

        tbl_itensConvete.setRowHeight(32); //  TAMANHO DA JANELA PRA CADA ITEM

        tbl_itensConvete.getColumnModel().getColumn(0).setPreferredWidth(70);
        tbl_itensConvete.getColumnModel().getColumn(0).setResizable(false);
        tbl_itensConvete.getColumnModel().getColumn(1).setPreferredWidth(445);
        tbl_itensConvete.getColumnModel().getColumn(1).setResizable(false);
        tbl_itensConvete.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbl_itensConvete.getColumnModel().getColumn(2).setResizable(false);
        tbl_itensConvete.getColumnModel().getColumn(3).setPreferredWidth(130);
        tbl_itensConvete.getColumnModel().getColumn(3).setResizable(false);
        tbl_itensConvete.getColumnModel().getColumn(4).setPreferredWidth(120);
        tbl_itensConvete.getColumnModel().getColumn(4).setResizable(false);
        tbl_itensConvete.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbl_itensConvete.getColumnModel().getColumn(5).setResizable(false);
//        tbl_itensConveteNecassario.setAutoResizeMode(tbl_itensImportados.AUTO_RESIZE_OFF);
        tbl_itensConvete.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conecBanc.desconectar();
        conecPesquisa.desconectar();
    }//  FINAL METODO PARA IMPRIMIR TABELA NA INTERFACE

    public void preecherTabela(String SQL) { // METODO PARA PREENCHER TABELA NA INTERFACE
        ArrayList dados = new ArrayList();
        String[] Colunas = new String[]{"Código Barras", "Descrição", "Marca", "UND.", "Qtd", "P. Compra", "P. Venda"};
        conecBanc.conexaoBanco();
        conecPesquisa.conexaoBanco();
        conecBanc.executaSQL(SQL);
        try {
            conecBanc.rs.first();
            do {
                conecPesquisa.executaSQL("select * from marca where id_marca='" + conecBanc.rs.getInt("id_marca") + "'");
                conecPesquisa.rs.first();
                String nomeMarca = conecPesquisa.rs.getString("nome_marca");
                dados.add(new Object[]{conecBanc.rs.getString("codbarra"), conecBanc.rs.getString("nome_produto"), (nomeMarca), conecBanc.rs.getString("unidade"), conecBanc.rs.getInt("quantidade"), (numberFormat.format(conecBanc.rs.getFloat("preco_compra"))), (numberFormat.format(conecBanc.rs.getFloat("preco_venda")))});

            } while (conecBanc.rs.next());

        } catch (SQLException ex) {
        }

        ModeloTabela modelo = new ModeloTabela(dados, Colunas);
        tbl_produtos.setModel(modelo);
        DefaultTableCellRenderer headerRenderer2 = new DefaultTableCellRenderer();
        headerRenderer2.setOpaque(true);
        headerRenderer2.setBackground(new Color(0, 0, 51));
        headerRenderer2.setFont(new java.awt.Font("Arial Black", Font.BOLD, 23));
        headerRenderer2.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < tbl_produtos.getModel().getColumnCount();
                i++) {
            tbl_produtos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer2);
        }

        tbl_produtos.setRowHeight(35); //  TAMANHO DA JANELA PRA CADA ITEM

        tbl_produtos.getColumnModel().getColumn(0).setPreferredWidth(120); //COD. BARRA
        tbl_produtos.getColumnModel().getColumn(0).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(1).setPreferredWidth(317); //DESCRIÇÃO
        tbl_produtos.getColumnModel().getColumn(1).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(2).setPreferredWidth(130);  //MARCA
        tbl_produtos.getColumnModel().getColumn(2).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(3).setPreferredWidth(57);  //UNIDADE
        tbl_produtos.getColumnModel().getColumn(3).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(4).setPreferredWidth(57);  //QUANTIDADE
        tbl_produtos.getColumnModel().getColumn(4).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(5).setPreferredWidth(90);  //PREÇO COMPRA
        tbl_produtos.getColumnModel().getColumn(5).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(6).setPreferredWidth(90);  //PREÇO VENDA
        tbl_produtos.getColumnModel().getColumn(6).setResizable(false);
        tbl_produtos.getTableHeader().setReorderingAllowed(false);
//        tbl_produtos.setAutoResizeMode(tbl_produtos.AUTO_RESIZE_OFF);
        tbl_produtos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conecBanc.desconectar();
        conecPesquisa.desconectar();
    }//  FINAL METODO PARA IMPRIMIR TABELA NA INTERFACE

    public void preecherTabelaProdutoPesquisa(String SQL) { // METODO PARA PREENCHER TABELA NA INTERFACE
        ArrayList dados = new ArrayList();
        String[] Colunas = new String[]{"Código Barras", "Descrição", "Marca", "UND", "Qtd", "P. Compra", "P. Venda"};
        conecBanc.conexaoBanco();
        conecPesquisa.conexaoBanco();
        conecBanc.executaSQL(SQL);

        try {
            conecBanc.rs.first();
            do {
                conecPesquisa.executaSQL("select * from marca where id_marca='" + conecBanc.rs.getInt("id_marca") + "'");
                conecPesquisa.rs.first();
                String nomeMarca = conecPesquisa.rs.getString("nome_marca");
                dados.add(new Object[]{conecBanc.rs.getString("codbarra"), conecBanc.rs.getString("nome_produto"), (nomeMarca), conecBanc.rs.getString("unidade"), conecBanc.rs.getInt("quantidade"), (numberFormat.format(conecBanc.rs.getFloat("preco_compra"))), (numberFormat.format(conecBanc.rs.getFloat("preco_venda")))});

            } while (conecBanc.rs.next());

        } catch (SQLException ex) {
        }

        ModeloTabela modelo = new ModeloTabela(dados, Colunas);
        tbm_itensCadastradosProdSemCodBarra.setModel(modelo);
        DefaultTableCellRenderer headerRenderer2 = new DefaultTableCellRenderer();
        headerRenderer2.setOpaque(true);
        headerRenderer2.setBackground(new Color(0, 0, 51));
        headerRenderer2.setFont(new java.awt.Font("Arial Black", Font.BOLD, 23));
        headerRenderer2.setForeground(new Color(255, 255, 255));
        for (int i = 0; i < tbm_itensCadastradosProdSemCodBarra.getModel().getColumnCount();
                i++) {
            tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer2);
        }

        tbm_itensCadastradosProdSemCodBarra.setRowHeight(33); //  TAMANHO DA JANELA PRA CADA ITEM

        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(0).setPreferredWidth(120); //COD. BARRA
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(0).setResizable(false);
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(1).setPreferredWidth(317); //DESCRIÇÃO
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(1).setResizable(false);
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(2).setPreferredWidth(130);  //MARCA
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(2).setResizable(false);
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(3).setPreferredWidth(57);  //UNIDADE
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(3).setResizable(false);
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(4).setPreferredWidth(57);  //QUANTIDADE
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(4).setResizable(false);
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(5).setPreferredWidth(90);  //PREÇO COMPRA
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(5).setResizable(false);
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(6).setPreferredWidth(90);  //PREÇO VENDA
        tbm_itensCadastradosProdSemCodBarra.getColumnModel().getColumn(6).setResizable(false);
        tbm_itensCadastradosProdSemCodBarra.getTableHeader().setReorderingAllowed(false);
//        tbl_produtos.setAutoResizeMode(tbl_produtos.AUTO_RESIZE_OFF);
        tbm_itensCadastradosProdSemCodBarra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conecBanc.desconectar();
        conecPesquisa.desconectar();
    }//  FINAL METODO PARA IMPRIMIR TABELA NA INTERFACE

    private void btn_novoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_novoActionPerformed
        modoNavegar = "Novo";
        manipularInterface();
        vlrganhoItem = 0; //SALVA O VALOR GANHO PARA CADA ITEM 
        preVenda = 0;     // SALVA O VALOR DE VENDA DE CADA ITEM
        preCompra = 0;    // SALVA O PRECO DE COMPRA DO ITEM

    }//GEN-LAST:event_btn_novoActionPerformed

    private void btn_salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salvarActionPerformed
        String codbarraVeri = "";
        int op1, op2, op3, op4, op5, op6, op7, op8, op9, op10, op11, op12;

        conecBanTblProd.conexaoBanco();
        try {
            conecBanTblProd.executaSQL("select * from produto where codbarra='" + ct_codBarra.getText() + "'");
            conecBanTblProd.rs.first();
            codbarraVeri = conecBanTblProd.rs.getString("codBarra");
        } catch (SQLException ex) {
        }
        conecBanTblProd.desconectar();

        if (ct_produto.getText().equals("")) {
            ct_produto.setBackground(new Color(246, 170, 170));
            op1 = 1;
        } else {
            ct_produto.setBackground(new Color(255, 255, 102));
            op1 = 2;
        }

        if (cbox_unidade.getSelectedIndex() == -1) {
            cbox_unidade.setBorder(new LineBorder(new Color(255, 0, 0)));
            op3 = 1;
        } else {
            cbox_unidade.setBorder(new LineBorder(null));
            op3 = 2;
        }

        if (cbox_marca.getSelectedIndex() == -1) {
            cbox_marca.setBorder(new LineBorder(new Color(255, 0, 0)));
            op4 = 1;
        } else {
            cbox_marca.setBorder(new LineBorder(null));
            op4 = 2;
        }

        if (cbox_grupo.getSelectedIndex() == -1) {
            cbox_grupo.setBorder(new LineBorder(new Color(255, 0, 0)));
            op5 = 1;
        } else {
            cbox_grupo.setBorder(new LineBorder(null));
            op5 = 2;
        }

        if (cbox_fornecedor.getSelectedIndex() == -1) {
            cbox_fornecedor.setBorder(new LineBorder(new Color(255, 0, 0)));
            op6 = 1;
        } else {
            cbox_fornecedor.setBorder(new LineBorder(null));
            op6 = 2;
        }

        if (ct_quantidade.getText().equals("")) {
            ct_quantidade.setBackground(new Color(246, 170, 170));
            op7 = 1;
        } else {
            ct_quantidade.setBackground(new Color(255, 255, 102));
            op7 = 2;
        }

        if (ct_qtdMinEstq.getText().equals("")) {
            ct_qtdMinEstq.setBackground(new Color(246, 170, 170));
            op8 = 1;
        } else {
            ct_qtdMinEstq.setBackground(new Color(255, 255, 102));
            op8 = 2;
        }

        if (ct_preco_compra.getText().equals("")) {
            ct_preco_compra.setBackground(new Color(246, 170, 170));
            op9 = 1;
        } else {
            ct_preco_compra.setBackground(new Color(255, 255, 102));
            op9 = 2;
        }

        if (ct_percentual.getText().equals("")) {
            ct_percentual.setBackground(new Color(246, 170, 170));
            op10 = 1;
        } else {
            ct_percentual.setBackground(new Color(255, 255, 102));
            op10 = 2;
        }

        if (ct_codBarra.getText().equals("")) {
            ct_codBarra.setBackground(new Color(246, 170, 170));
            op2 = 1;
        } else {
            ct_codBarra.setBackground(new Color(255, 255, 102));
            op2 = 2;
        }

        if (codbarraVeri.equals(ct_codBarra.getText())) {
            op11 = 1;
        } else {
            op11 = 2;
        }

        if (ct_codBarra.getText().equals(codBarrasVerifica)) {
            op12 = 2;
        } else {
            if (codbarraVeri.equals(ct_codBarra.getText())) {
                op12 = 1;
            } else {
                op12 = 2;
            }
        }

        if (op1 == 2 && op2 == 2 && op3 == 2 && op4 == 2 && op5 == 2 && op6 == 2 && op7 == 2 && op8 == 2 && op9 == 2 && op10 == 2) {
            if (modoNavegar.equals("Novo")) {
                if (op11 == 1) {
                    JOptionPane.showMessageDialog(null, "Código de barras já cadastrado! \nTente outro código de barras.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    int resposta = JOptionPane.showConfirmDialog(null, "Salvar o novo produto?", "Novo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resposta == JOptionPane.YES_NO_OPTION) {
                        modProdSemNota.setFornecedorSN("" + cbox_fornecedor.getSelectedItem());
                        modProdSemNota.setGrupoSN("" + cbox_grupo.getSelectedItem());
                        modProdSemNota.setMarcaSN("" + cbox_marca.getSelectedItem());
                        modProdSemNota.setUnidadeMedidaSN("" + cbox_unidade.getSelectedItem());
                        modProdSemNota.setCodBarrasSN(ct_codBarra.getText());
                        modProdSemNota.setDescricaoSN(ct_produto.getText());
                        modProdSemNota.setPrecoVendaSN(preVenda);
                        modProdSemNota.setPrecoCompraSN(preCompra);
                        modProdSemNota.setQuantidadeCN(Integer.parseInt(ct_quantidade.getText()));
                        modProdSemNota.setVlrGanhoItemSN(vlrganhoItem);
                        modProdSemNota.setPercentualItemSN(Float.parseFloat(ct_percentual.getText()));
                        modProdSemNota.setQtdMinimaEstoqSN(Integer.parseInt(ct_qtdMinEstq.getText()));
                        modProdSemNota.setCodCeanImportSN(ct_codBarra.getText());
                        controlProdSemNota.inserirProdutoSemNota(modProdSemNota);

                        //CRIA LICAÇÃO DE IMPORTAÇÃO, PARA IMPORTAÇÃO AUTOMATICA COM NOTA FISCAL
                        modCeanTribCeanImport.setCodCeanTblProd(ct_codBarra.getText());
                        modCeanTribCeanImport.setCodCeanTribTblNota(ct_codBarra.getText());
                        controleCeanTribCeanImport.addNovaLigacaoCeanTribComCeanImport(modCeanTribCeanImport);

                        modoNavegar = "Inicializar";
                        manipularInterface();
                        vlrganhoItem = 0; //SALVA O VALOR GANHO PARA CADA ITEM 
                        preVenda = 0;     // SALVA O VALOR DE VENDA DE CADA ITEM
                        preCompra = 0;    // SALVA O PRECO DE COMPRA DO ITEM
                    }
                }
            }

            if (modoNavegar.equals("Editar")) {
                if (op12 == 1) {
                    JOptionPane.showMessageDialog(null, "Código de barras já cadastrado! \nTente outro código de barras.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    int resposta = JOptionPane.showConfirmDialog(null, "Deseja salvar alterações?", "Alteração", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resposta == JOptionPane.YES_NO_OPTION) {
                        modProdSemNota.setIdProdutoSN(Integer.parseInt(ct_cod.getText()));
                        modProdSemNota.setCodBarrasSN(ct_codBarra.getText());
                        modProdSemNota.setFornecedorSN("" + cbox_fornecedor.getSelectedItem());
                        modProdSemNota.setDescricaoSN(ct_produto.getText());
                        modProdSemNota.setPrecoVendaSN(preVenda);
                        modProdSemNota.setPrecoCompraSN(preCompra);
                        modProdSemNota.setQuantidadeCN(Integer.parseInt(ct_quantidade.getText()));
                        modProdSemNota.setPercentualItemSN(Float.parseFloat(ct_percentual.getText()));
                        modProdSemNota.setVlrGanhoItemSN(vlrganhoItem);
                        modProdSemNota.setMarcaSN("" + cbox_marca.getSelectedItem());
                        modProdSemNota.setGrupoSN("" + cbox_grupo.getSelectedItem());
                        modProdSemNota.setUnidadeMedidaSN("" + cbox_unidade.getSelectedItem());
                        modProdSemNota.setQtdMinimaEstoqSN(Integer.parseInt(ct_qtdMinEstq.getText()));
                        controlProdSemNota.editarProdutoSemNota(modProdSemNota);

                        modoNavegar = "Inicializar";
                        manipularInterface();
                        vlrganhoItem = 0; //SALVA O VALOR GANHO PARA CADA ITEM 
                        preVenda = 0;     // SALVA O VALOR DE VENDA DE CADA ITEM
                        preCompra = 0;    // SALVA O PRECO DE COMPRA DO ITEM
                    }
                }
            }
            preecherTabela("select * from produto order by nome_produto");
        } else {
            JOptionPane.showMessageDialog(null, "Campo obrigatório não preenchido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_salvarActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        modoNavegar = "Editar";
        manipularInterface();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_excluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_excluirActionPerformed

        int respost = JOptionPane.showConfirmDialog(null, "Deseja excluir o produto?", "Excluir", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respost == JOptionPane.YES_NO_OPTION) {

            //EXCLUI A LIGAÇÃO DE IMPORTAÇÃO AUTOMATICA COM NOTA
            modCeanTribCeanImport.setIdProdutoTblProd(Integer.parseInt(ct_cod.getText()));
            controleCeanTribCeanImport.excluirLigacaoCeanTribComCeanImport(modCeanTribCeanImport);
            //EXCLUI O PRODUTO
            modProdSemNota.setIdProdutoSN(Integer.parseInt(ct_cod.getText()));
            controlProdSemNota.excluirProdutoSemNota(modProdSemNota);

            preecherTabela("select * from produto order by nome_produto");
            modoNavegar = "Inicializar";
            manipularInterface();

            vlrganhoItem = 0; //SALVA O VALOR GANHO PARA CADA ITEM 
            preVenda = 0;     // SALVA O VALOR DE VENDA DE CADA ITEM
            preCompra = 0;    // SALVA O PRECO DE COMPRA DO ITEM
        }

    }//GEN-LAST:event_btn_excluirActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        modoNavegar = "Inicializar";
        manipularInterface();
        vlrganhoItem = 0; //SALVA O VALOR GANHO PARA CADA ITEM 
        preVenda = 0;     // SALVA O VALOR DE VENDA DE CADA ITEM
        preCompra = 0;    // SALVA O PRECO DE COMPRA DO ITEM
    }//GEN-LAST:event_btn_cancelarActionPerformed
    public void metodoSomaValoresAbaSemNota() {
        vlrganhoItem = preCompra * Float.parseFloat(ct_percentual.getText());
        ct_vlr_ganho.setText(String.valueOf(numberFormat.format(vlrganhoItem)));
        preVenda = preCompra + vlrganhoItem;
        ct_preco_venda.setText(String.valueOf(numberFormat.format(preVenda)));
    }

    private void ct_percentualKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_percentualKeyReleased
        ct_percentual.setText(ct_percentual.getText().replaceAll("[^0-9.]", ""));
        metodoSomaValoresAbaSemNota();

    }//GEN-LAST:event_ct_percentualKeyReleased

    private void btn_ler_nota_fiscalMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ler_nota_fiscalMousePressed

    }//GEN-LAST:event_btn_ler_nota_fiscalMousePressed

    private void ct_pesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_pesquisarKeyReleased
        if (ct_pesquisar.getText().equals("")) {
            preecherTabela("select * from produto order by nome_produto");
        } else {
            modoNavegar = "Navegar";
            manipularInterface();
            conecBanc.conexaoBanco();
            preecherTabela("select * from produto where nome_produto like '%" + ct_pesquisar.getText() + "%' or codbarra like '%" + ct_pesquisar.getText() + "%'" + "");
            conecBanc.desconectar();
        }
    }//GEN-LAST:event_ct_pesquisarKeyReleased

    private void ct_pesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_pesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisarActionPerformed

    private void ct_pesquisarComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_ct_pesquisarComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisarComponentShown

    private void ct_pesquisarComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_ct_pesquisarComponentResized

    }//GEN-LAST:event_ct_pesquisarComponentResized

    private void ct_pesquisarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_pesquisarMouseEntered

    }//GEN-LAST:event_ct_pesquisarMouseEntered

    private void ct_pesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_pesquisarMouseClicked

    }//GEN-LAST:event_ct_pesquisarMouseClicked

    private void tbl_produtosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_produtosMouseClicked

    }//GEN-LAST:event_tbl_produtosMouseClicked

    private void btn_ler_nota_fiscalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ler_nota_fiscalActionPerformed
        if (ct_chaveNota.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Nota não encontrada! \nSelecione um arquivo clicando em 'procurar'", "ERRO", JOptionPane.INFORMATION_MESSAGE);
        } else {
            conecBanc.conexaoBanco();
            int idUltimoNotaImportada = 0;

            try {
                conecBanc.executaSQL("select * from nota_inicio");
                conecBanc.rs.last();
                idUltimoNotaImportada = conecBanc.rs.getInt("id_nota");

            } catch (SQLException ex) {
            }

            controlNotaFiscal.parseNota(ct_chaveNota.getText());
            try {
                conecBanc.executaSQL("select * from nota_inicio");
                conecBanc.rs.last();

                if (idUltimoNotaImportada != conecBanc.rs.getInt("id_nota")) {

                    controlAddProdNota.addProdutoNovo();
                    cbox_unidadeMedida_itensConvertTblNota.setSelectedItem(null);
                    cbox_unidadeMedida_itensSem.setSelectedItem(null);
                    cbox_grupo_itensSem.setSelectedItem(null);
                    cbox_marca_itensSem.setSelectedItem(null);
                    jt_importarProdutos.setVisible(true);

                    preecherTabelaProdutoPesquisa("select * from produto order by nome_produto");
                    preecherTabelaItensImportadosPesquisar("select * from nota_produto where importado1_novo2_converte3=5 order by id_produto");
                    preecherTabelaItensImportadosNovo("select * from nota_produto where importado1_novo2_converte3=2 order by id_produto ");
                    preecherTabelaItensImportadosConverte("select * from nota_produto where importado1_novo2_converte3=3 order by id_produto");
                    preecherTabela("select * from produto order by nome_produto");
                    JOptionPane.showMessageDialog(null, "Produtos importados! \nClick em 'OK'");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao importar! " + ex, "Erro", JOptionPane.ERROR_MESSAGE);
            }
            conecBanc.desconectar();
        }
    }//GEN-LAST:event_btn_ler_nota_fiscalActionPerformed

    private void ct_pesquisarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_pesquisarMousePressed

    }//GEN-LAST:event_ct_pesquisarMousePressed

    private void btn_editarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_editarMouseReleased

    }//GEN-LAST:event_btn_editarMouseReleased

    private void tbl_produtosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_produtosMousePressed
        preVenda = 0;
        vlrganhoItem = 0;
        preCompra = 0;
        modoNavegar = "Navegar";
        manipularInterface();

        String codBarraClick = "" + tbl_produtos.getValueAt(tbl_produtos.getSelectedRow(), 0);
        conecBanc.conexaoBanco();
        conecPesquisa.conexaoBanco();

        try {
            conecBanc.executaSQL("select * from produto where codbarra='" + codBarraClick + "'");
            conecBanc.rs.first();
            preVenda = conecBanc.rs.getFloat("preco_venda"); // GUARDA VALOR DE VENDA DO PRODUTO SELECIONADO
            vlrganhoItem = conecBanc.rs.getFloat("vlr_ganho"); // GUARDA O VALOR DE GANHO DO ITEM SELECIONADO
            preCompra = conecBanc.rs.getFloat("preco_compra");
            idProduto = conecBanc.rs.getInt("id_produto");
            ct_cod.setText(String.valueOf(conecBanc.rs.getInt("id_produto")));
            ct_codBarra.setText(conecBanc.rs.getString("codbarra"));
            codBarrasVerifica = conecBanc.rs.getString("codbarra");
            ct_produto.setText(conecBanc.rs.getString("nome_produto"));
            ct_quantidade.setText(String.valueOf(conecBanc.rs.getInt("quantidade")));
            ct_preco_compra.setText((String.valueOf(numberFormat.format(conecBanc.rs.getFloat("preco_compra")))));
            ct_preco_venda.setText(String.valueOf(numberFormat.format(conecBanc.rs.getFloat("preco_venda"))));
            ct_percentual.setText(conecBanc.rs.getString("percentual"));
            ct_vlr_ganho.setText(String.valueOf(numberFormat.format(conecBanc.rs.getFloat("vlr_ganho"))));

            conecPesquisa.executaSQL("select * from fornecedores where id_fornecedor=" + conecBanc.rs.getInt("id_fornecedor"));
            conecPesquisa.rs.first();
            cbox_fornecedor.setSelectedItem(conecPesquisa.rs.getString("nome_fornecedor"));
            cbox_unidade.removeAll();
            cbox_unidade.setSelectedItem(conecBanc.rs.getString("unidade"));

            conecPesquisa.executaSQL("select * from marca where id_marca=" + conecBanc.rs.getInt("id_marca"));
            conecPesquisa.rs.first();
            cbox_marca.setSelectedItem(conecPesquisa.rs.getString("nome_marca"));
            ct_qtdMinEstq.setText("" + conecBanc.rs.getInt("qtd_minima_estoque"));

            conecPesquisa.executaSQL("select * from grupo_produto where id_grupo=" + conecBanc.rs.getInt("id_grupo"));
            conecPesquisa.rs.first();
            cbox_grupo.setSelectedItem(conecPesquisa.rs.getString("nome_grupo"));

            conecPesquisa.desconectar();
            conecBanc.desconectar();
        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_tbl_produtosMousePressed

    private void ct_preco_compraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_preco_compraKeyReleased
        ct_preco_compra.setText(ct_preco_compra.getText().replaceAll("[^0-9.]", ""));
        preCompra = Float.parseFloat(ct_preco_compra.getText());
        metodoSomaValoresAbaSemNota();
    }//GEN-LAST:event_ct_preco_compraKeyReleased

    private void ct_preco_compraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_preco_compraMousePressed
        ct_preco_compra.setText(ct_preco_compra.getText().replaceAll("[^0-9.,]", ""));
        ct_preco_compra.setText(ct_preco_compra.getText().replace(",", "."));

    }//GEN-LAST:event_ct_preco_compraMousePressed

    private void ct_preco_compraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_preco_compraActionPerformed

    }//GEN-LAST:event_ct_preco_compraActionPerformed

    private void ct_preco_compraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_preco_compraMouseClicked

    }//GEN-LAST:event_ct_preco_compraMouseClicked

    private void btn_gerarCodBarraSemNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gerarCodBarraSemNotaActionPerformed
        conecBanTblProd.conexaoBanco();
        try {
            conecBanTblProd.executaSQL("select * from produto order by id_produto");
            conecBanTblProd.rs.last();
            int codUltimoProd = conecBanTblProd.rs.getInt("id_produto");
            codUltimoProd = codUltimoProd + 1;
            String formatCodUltimo;
            formatCodUltimo = StringUtils.leftPad("" + codUltimoProd, 4, "0");  // EXEMPLO 190+1 = 191
            String gerarCodBarraBtnGerarSemNota = "123456789" + formatCodUltimo;
            ct_codBarra.setText(gerarCodBarraBtnGerarSemNota);
        } catch (SQLException ex) {

        }
        conecBanTblProd.conexaoBanco();
    }//GEN-LAST:event_btn_gerarCodBarraSemNotaActionPerformed

    private void ct_quantidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_quantidadeKeyReleased
        ct_quantidade.setText(ct_quantidade.getText().replaceAll("[^0-9]", ""));
    }//GEN-LAST:event_ct_quantidadeKeyReleased

    private void ct_qtdMinEstqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_qtdMinEstqKeyReleased
        ct_qtdMinEstq.setText(ct_qtdMinEstq.getText().replaceAll("[^0-9]", ""));
    }//GEN-LAST:event_ct_qtdMinEstqKeyReleased

    private void ct_codBarraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_codBarraKeyReleased
        ct_codBarra.setText(ct_codBarra.getText().replaceAll("[^0-9]", ""));
        if (ct_codBarra.getText().length() >= 15) {
            ct_codBarra.setText(ct_codBarra.getText().substring(0, 15));
        } else {
        }
    }//GEN-LAST:event_ct_codBarraKeyReleased

    private void btn_addMarcaSemNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addMarcaSemNotaActionPerformed
        opMarcaSemNota = 1;
        FrmAddMarca dialog = new FrmAddMarca(new javax.swing.JFrame(), true);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_addMarcaSemNotaActionPerformed

    private void btn_addGrupoSemNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addGrupoSemNotaActionPerformed
        opGrupoSemNota = 1;
        FrmAddGrupo dialog = new FrmAddGrupo(new javax.swing.JFrame(), true);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_addGrupoSemNotaActionPerformed

    private void cbox_grupoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbox_grupoMousePressed

    }//GEN-LAST:event_cbox_grupoMousePressed

    private void btn_addFornecedorSemNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addFornecedorSemNotaActionPerformed
        opFornecedorSemNota = 1;
        FrmAddFornecedor dialog = new FrmAddFornecedor(new javax.swing.JFrame(), true);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_addFornecedorSemNotaActionPerformed

    private void btn_addFornecedorSemNotaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_addFornecedorSemNotaMouseMoved

    }//GEN-LAST:event_btn_addFornecedorSemNotaMouseMoved

    private void jPanel112MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel112MouseEntered
        if (opFornecedorSemNota == 1) {
            preencheComboFornecedor();
            cbox_fornecedor.setSelectedItem(null);
            opFornecedorSemNota = 2;
        } else {
        }

    }//GEN-LAST:event_jPanel112MouseEntered

    private void jPanel111MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel111MouseEntered
        if (opGrupoSemNota == 1) {
            preencheComboGrupo();
            cbox_grupo.setSelectedItem(null);
            opGrupoSemNota = 2;
        } else {
        }
    }//GEN-LAST:event_jPanel111MouseEntered

    private void jPanel110MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel110MouseEntered
        if (opMarcaSemNota == 1) {
            preencheComboMarca();
            cbox_marca.setSelectedItem(null);
            opMarcaSemNota = 2;
        } else {
        }
    }//GEN-LAST:event_jPanel110MouseEntered

    private void jPanel110MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel110MousePressed

    }//GEN-LAST:event_jPanel110MousePressed

    private void ct_produtoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_produtoKeyReleased
        if (ct_produto.getText().length() >= 45) {
            ct_produto.setText(ct_produto.getText().substring(0, 45));
        } else {
        }
    }//GEN-LAST:event_ct_produtoKeyReleased

    private void ct_chaveNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_chaveNotaActionPerformed

    }//GEN-LAST:event_ct_chaveNotaActionPerformed

    private void ct_chaveNotaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_chaveNotaKeyReleased

    }//GEN-LAST:event_ct_chaveNotaKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser arquivo = new JFileChooser();
        FileNameExtensionFilter filtroXML = new FileNameExtensionFilter("Documento XML", "xml");
        arquivo.addChoosableFileFilter(filtroXML);
        arquivo.setAcceptAllFileFilterUsed(false);
        if (arquivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            ct_chaveNota.setText(arquivo.getSelectedFile().getAbsolutePath());
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbox_marcaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbox_marcaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbox_marcaMousePressed

    private void btn_colarCodBarraitensSemCodBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_colarCodBarraitensSemCodBarraActionPerformed
        ct_codBarraSemcodigoBarra.setText(codBarraTblProd);
        codBarraTblProd = "";
        btn_colarCodBarraitensSemCodBarra.setEnabled(false);
        btn_cadastradoItensSemCodigo.setEnabled(true);
    }//GEN-LAST:event_btn_colarCodBarraitensSemCodBarraActionPerformed

    private void btn_colarCodBarraitensSemCodBarraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_colarCodBarraitensSemCodBarraMousePressed

    }//GEN-LAST:event_btn_colarCodBarraitensSemCodBarraMousePressed

    private void ct_numItemItensSemCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_numItemItensSemCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_numItemItensSemCodigoActionPerformed

    private void ct_descricao_itensSemCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_descricao_itensSemCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_descricao_itensSemCodigoActionPerformed

    private void ct_codBarraSemcodigoBarraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_codBarraSemcodigoBarraKeyReleased
        ct_codBarraSemcodigoBarra.setText(ct_codBarraSemcodigoBarra.getText().replaceAll("[^0-9]", ""));
        if (ct_codBarraSemcodigoBarra.getText().length() >= 15) {
            ct_codBarraSemcodigoBarra.setText(ct_codBarraSemcodigoBarra.getText().substring(0, 15));
        } else {
        }
        if (ct_codBarraSemcodigoBarra.getText().equals("")) {
            btn_cadastradoItensSemCodigo.setEnabled(false);
        } else {
            btn_cadastradoItensSemCodigo.setEnabled(true);
        }
    }//GEN-LAST:event_ct_codBarraSemcodigoBarraKeyReleased

    private void ct_codBarraSemcodigoBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_codBarraSemcodigoBarraActionPerformed

    }//GEN-LAST:event_ct_codBarraSemcodigoBarraActionPerformed

    private void tbm_itensCadastradosProdSemCodBarraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbm_itensCadastradosProdSemCodBarraMousePressed
        String codbarras = "" + tbm_itensCadastradosProdSemCodBarra.getValueAt(tbm_itensCadastradosProdSemCodBarra.getSelectedRow(), 0);
        ct_codBarraSemcodigoBarra.setText("");
        if (!ct_descricao_itensSemCodigo.getText().equals("")) {
            btn_colarCodBarraitensSemCodBarra.setEnabled(true);
            ct_codBarraSemcodigoBarra.setEnabled(true);
        }
        btn_cadastradoItensSemCodigo.setEnabled(false);

        conecBanTblProd.conexaoBanco();
        try {
            conecBanTblProd.executaSQL("select * from produto where codbarra='" + codbarras + "'");
            conecBanTblProd.rs.first();
            codBarraTblProd = conecBanTblProd.rs.getString("codbarra");
        } catch (SQLException ex) {
        }
        conecBanTblProd.desconectar();
    }//GEN-LAST:event_tbm_itensCadastradosProdSemCodBarraMousePressed

    private void tbm_itensCadastradosProdSemCodBarraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbm_itensCadastradosProdSemCodBarraMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbm_itensCadastradosProdSemCodBarraMouseClicked

    private void ct_pesquisar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_pesquisar1KeyReleased
        if (ct_pesquisar1.getText().equals("")) {
            preecherTabelaProdutoPesquisa("select * from produto order by nome_produto");
        } else {
            conecBanTblProd.conexaoBanco();
            preecherTabelaProdutoPesquisa("select * from produto where nome_produto like '%" + ct_pesquisar1.getText() + "%' or codbarra like '%" + ct_pesquisar1.getText() + "%'" + "");
            conecBanTblProd.desconectar();
        }
    }//GEN-LAST:event_ct_pesquisar1KeyReleased

    private void ct_pesquisar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_pesquisar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisar1ActionPerformed

    private void ct_pesquisar1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_ct_pesquisar1ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisar1ComponentShown

    private void ct_pesquisar1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_ct_pesquisar1ComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisar1ComponentResized

    private void ct_pesquisar1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_pesquisar1MousePressed

    }//GEN-LAST:event_ct_pesquisar1MousePressed

    private void ct_pesquisar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_pesquisar1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisar1MouseEntered

    private void ct_pesquisar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_pesquisar1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisar1MouseClicked

    private void tbl_itensSemCodBarraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_itensSemCodBarraMousePressed
        ct_descricao_itensSemCodigo.setText("");
        ct_codBarraSemcodigoBarra.setText("");
        ct_numItemItensSemCodigo.setText("");
        ct_codBarraSemcodigoBarra.setEnabled(false);
        ct_descricao_itensSemCodigo.setEnabled(true);
        btn_novoItensSemCodigo.setEnabled(true);
        btn_cancelarProdutosSemCodigo.setEnabled(true);
        btn_cadastradoItensSemCodigo.setEnabled(false);

        conecBanc.conexaoBanco();
        String idProdutoNota = "" + tbl_itensSemCodBarra.getValueAt(tbl_itensSemCodBarra.getSelectedRow(), 0);
        try {
            conecBanc.executaSQL("select * from nota_produto where id_produto='" + idProdutoNota + "'");
            conecBanc.rs.first();
            ct_descricao_itensSemCodigo.setText(conecBanc.rs.getString("nome_prod"));
            ct_numItemItensSemCodigo.setText("" + conecBanc.rs.getInt("id_produto"));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao selecionar item! \nErro: " + ex);
        }
        conecBanc.desconectar();
    }//GEN-LAST:event_tbl_itensSemCodBarraMousePressed

    private void tbl_itensSemCodBarraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_itensSemCodBarraMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_itensSemCodBarraMouseClicked

    private void btn_cancelarProdutosSemCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarProdutosSemCodigoActionPerformed
        ct_descricao_itensSemCodigo.setText("");
        ct_codBarraSemcodigoBarra.setText("");
        ct_numItemItensSemCodigo.setText("");
        ct_codBarraSemcodigoBarra.setEnabled(false);
        ct_descricao_itensSemCodigo.setEnabled(false);
        btn_novoItensSemCodigo.setEnabled(false);
        btn_novoItensSemCodigo.setEnabled(false);
        btn_cancelarProdutosSemCodigo.setEnabled(false);
        btn_cadastradoItensSemCodigo.setEnabled(false);
    }//GEN-LAST:event_btn_cancelarProdutosSemCodigoActionPerformed

    private void btn_cancelarProdutosSemCodigoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cancelarProdutosSemCodigoMousePressed

    }//GEN-LAST:event_btn_cancelarProdutosSemCodigoMousePressed

    private void btn_cadastradoItensSemCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cadastradoItensSemCodigoActionPerformed
        conecBanTblProd.conexaoBanco();
        String codCean = "";

        try {
            conecBanTblProd.executaSQL("select * from produto where codbarra='" + ct_codBarraSemcodigoBarra.getText() + "'");
            conecBanTblProd.rs.first();
            codCean = conecBanTblProd.rs.getString("cod_cean_import");
        } catch (SQLException ex) {
        }
        if (ct_codBarraSemcodigoBarra.getText().equals(codCean)) {
            int resposta = JOptionPane.showConfirmDialog(null, "Confirma importação?", "Importação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (resposta == JOptionPane.YES_NO_OPTION) {
                modCeanTribCeanImport.setCodBarrasProd(ct_codBarraSemcodigoBarra.getText());
                modCeanTribCeanImport.setIdProdutoTblNota(Integer.parseInt(ct_numItemItensSemCodigo.getText()));
                controleCeanTribCeanImport.addNovaLigacaoCeanTribComCeanImportSelecionado(modCeanTribCeanImport);
                preecherTabelaProdutoPesquisa("select * from produto order by nome_produto");
                preecherTabelaItensImportadosPesquisar("select * from nota_produto where importado1_novo2_converte3=5 order by id_produto");
                preecherTabelaItensImportadosNovo("select * from nota_produto where importado1_novo2_converte3 =2 order by id_produto");
                preecherTabelaItensImportadosConverte("select * from nota_produto where importado1_novo2_converte3 =3 order by id_produto");
                preecherTabela("select * from produto order by nome_produto");
                btn_cadastradoItensSemCodigo.setEnabled(false);
                btn_novoItensSemCodigo.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Código de barras não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        conecBanTblProd.desconectar();
    }//GEN-LAST:event_btn_cadastradoItensSemCodigoActionPerformed

    private void btn_cadastradoItensSemCodigoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cadastradoItensSemCodigoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cadastradoItensSemCodigoMousePressed

    private void btn_novoItensSemCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_novoItensSemCodigoActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Adicionar o item a tabela 'Novo'?", "Novo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_NO_OPTION) {
            controlAddProdNota.atualizaListaItensImportadoRecebe2(Integer.parseInt(ct_numItemItensSemCodigo.getText()));
            ct_descricao_itensSemCodigo.setText("");
            ct_codBarraSemcodigoBarra.setText("");
            ct_numItemItensSemCodigo.setText("");
            ct_codBarraSemcodigoBarra.setEnabled(false);
            ct_descricao_itensSemCodigo.setEnabled(false);
            btn_novoItensSemCodigo.setEnabled(false);
            conecBanc.desconectar();
            preecherTabelaItensImportadosNovo("select * from nota_produto where importado1_novo2_converte3=2 order by id_produto");
            preecherTabelaItensImportadosPesquisar("select * from nota_produto where importado1_novo2_converte3=5 order by id_produto");
        }
    }//GEN-LAST:event_btn_novoItensSemCodigoActionPerformed

    private void btn_novoItensSemCodigoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_novoItensSemCodigoMousePressed

    }//GEN-LAST:event_btn_novoItensSemCodigoMousePressed

    private void tbl_itensConveteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_itensConveteMousePressed
        ct_vlrCusto_itensConvertTblNota.setText("");
        cbox_unidadeMedida_itensConvertTblNota.setSelectedItem(null);
        jPanel76.setVisible(true);
        btn_cancelarConvert.setVisible(true);
        btn_salvarConvert.setVisible(true);

        ct_vlrCusto_itensConvertTblNota.setBackground(new Color(255, 255, 102));
        ct_quantidadeConvertNota.setBackground(new Color(255, 255, 102));
        cbox_unidadeMedida_itensConvertTblNota.setBorder(new LineBorder(new Color(197, 196, 195)));
        //        ct_vlrVenda_itensConvertTblNota.setBackground(new Color(255,255,102));

        String idProd = "" + tbl_itensConvete.getValueAt(tbl_itensConvete.getSelectedRow(), 0);

        conecBanc.conexaoBanco();
        conecBanTblProd.conexaoBanco();
        try {
            conecBanc.executaSQL("select * from nota_produto where id_produto='" + idProd + "'");
            conecBanc.rs.first();
            String codCeanTribNota = conecBanc.rs.getString("cod_cean_trib");
            ct_descricao_itensConvertTblNota.setText(conecBanc.rs.getString("nome_prod"));
            ct_vlrCusto_itensConvertTblNota.setText("" + numberFormat.format(conecBanc.rs.getFloat("vlr_unit_prod")));
            ct_quantidadeConvertNota.setText("" + conecBanc.rs.getInt("qtd_recebida"));
            vlrCompraConv = conecBanc.rs.getFloat("vlr_unit_prod");// VARIAVEL GLOBAL COM VALOR DA COMPRA DO ITENS SELECIONADO
            idProdutoConv = conecBanc.rs.getInt("id_produto");
            conecBanc.executaSQL("select * from ceantrib_ceanimport where cod_ceantrib_nota='"+codCeanTribNota+"'");
            conecBanc.rs.first();
            conecBanTblProd.executaSQL("select * from produto where cod_cean_import='"+conecBanc.rs.getString("cod_cean_import_tblprod")+"'");
            conecBanTblProd.rs.first();
            ct_descricao_itensConvertTblProd.setText(conecBanTblProd.rs.getString("nome_produto"));
            ct_lucro_itensConvertTblProd.setText("" + conecBanTblProd.rs.getFloat("percentual"));
            ct_vlrVenda_itensConvertTblProd.setText("" + numberFormat.format(conecBanTblProd.rs.getFloat("preco_venda")));
            ct_unidMedBan.setText(conecBanTblProd.rs.getString("unidade"));
            ct_codBarra_convert.setText(conecBanTblProd.rs.getString("codbarra"));
            codCeanImportProdConvert = conecBanTblProd.rs.getString("cod_cean_import");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao selecionar itens da tabela \nErro: " + ex);
        }

        ct_descricao_itensConvertTblProd.setEnabled(false);
        ct_lucro_itensConvertTblProd.setEnabled(false);
        ct_vlrVenda_itensConvertTblProd.setEnabled(false);
        ct_unidMedBan.setEnabled(false);
        ct_descricao_itensConvertTblNota.setEnabled(true);
        ct_vlrCusto_itensConvertTblNota.setEnabled(true);
        ct_quantidadeConvertNota.setEnabled(true);
        cbox_unidadeMedida_itensConvertTblNota.setEnabled(true);
        btn_salvarConvert.setEnabled(true);
        btn_cancelarConvert.setEnabled(true);

        conecBanTblProd.desconectar();
        conecBanc.desconectar();
    }//GEN-LAST:event_tbl_itensConveteMousePressed

    private void tbl_itensConveteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_itensConveteMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_itensConveteMouseClicked

    private void ct_lucro_itensConvertTblProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_lucro_itensConvertTblProdKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_lucro_itensConvertTblProdKeyReleased

    private void ct_descricao_itensConvertTblProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_descricao_itensConvertTblProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_descricao_itensConvertTblProdActionPerformed

    private void btn_salvarConvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salvarConvertActionPerformed
        int op1, op2, op4, op5, op6;

        String unidMedTblProd = "" + ct_unidMedBan.getText();
        String unidMedTblNota = "" + cbox_unidadeMedida_itensConvertTblNota.getSelectedItem();

        if (ct_vlrCusto_itensConvertTblNota.getText().equals("")) {
            ct_vlrCusto_itensConvertTblNota.setBackground(new Color(246, 170, 170));
            op1 = 1;
        } else {
            ct_vlrCusto_itensConvertTblNota.setBackground(new Color(255, 255, 102));
            op1 = 2;
        }

        if (ct_quantidadeConvertNota.getText().equals("")) {
            ct_quantidadeConvertNota.setBackground(new Color(246, 170, 170));
            op4 = 1;
        } else {
            ct_quantidadeConvertNota.setBackground(new Color(255, 255, 102));
            op4 = 2;
        }

        if (cbox_unidadeMedida_itensConvertTblNota.getSelectedIndex() == -1) {
            cbox_unidadeMedida_itensConvertTblNota.setBorder(new LineBorder(new Color(255, 0, 0)));
            op5 = 1;
        } else {
            cbox_unidadeMedida_itensConvertTblNota.setBorder(new LineBorder(new Color(197, 196, 195)));
            op5 = 2;
        }

        if (!unidMedTblProd.equals(unidMedTblNota)) {
            cbox_unidadeMedida_itensConvertTblNota.setBorder(new LineBorder(new Color(255, 0, 0)));
            op6 = 1;
        } else {
            cbox_unidadeMedida_itensConvertTblNota.setBorder(new LineBorder(new Color(197, 196, 195)));
            op6 = 2;
        }

        if (op1 == 2 && op4 == 2 && op5 == 2) {
            if (op6 == 2) {
                int resposta = JOptionPane.showConfirmDialog(null, "Finalizar importação?", "Importação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resposta == JOptionPane.YES_NO_OPTION) {
                    conecBanTblProd.conexaoBanco();
                    conecBanc.conexaoBanco();
                    try {
                        conecBanTblProd.executaSQL("select * from produto where cod_cean_import='" + codCeanImportProdConvert + "'");
                        conecBanTblProd.rs.first();
                        int quantidadeAtualEstoque = conecBanTblProd.rs.getInt("quantidade");
                        int quantidadeAtualizada = quantidadeAtualEstoque + Integer.parseInt(ct_quantidadeConvertNota.getText());
                        PreparedStatement pst = conecBanTblProd.conn.prepareStatement("update produto set quantidade=?, preco_compra=?, preco_venda=?, vlr_ganho=? where cod_cean_import=?");
                        pst.setInt(1, quantidadeAtualizada);
                        pst.setFloat(2, vlrCompraConv);
                        pst.setFloat(3, vlrVendaConv);
                        pst.setFloat(4, vlrGanhoConv);
                        pst.setString(5, codCeanImportProdConvert);
                        pst.execute();

                        PreparedStatement pst1 = conecBanc.conn.prepareStatement("update nota_produto set importado1_novo2_converte3=? where id_produto=?");
                        pst1.setInt(1, 1);
                        pst1.setInt(2, idProdutoConv);
                        pst1.execute();
                        JOptionPane.showMessageDialog(null, "Sucesso!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar estoque converte \n Erro:" + ex);
                    }
                    preecherTabelaItensImportadosConverte("select * from nota_produto where importado1_novo2_converte3 =3 order by id_produto");
                    preecherTabela("select * from produto order by nome_produto");
                    conecBanTblProd.desconectar();
                    ct_descricao_itensConvertTblProd.setText("");
                    ct_lucro_itensConvertTblProd.setText("");
                    ct_vlrVenda_itensConvertTblProd.setText("");
                    ct_unidMedBan.setText("");
                    ct_descricao_itensConvertTblNota.setText("");
                    ct_vlrCusto_itensConvertTblNota.setText("");
                    ct_quantidadeConvertNota.setText("");
                    cbox_unidadeMedida_itensConvertTblNota.setSelectedItem(null);

                    ct_descricao_itensConvertTblProd.setEnabled(false);
                    ct_lucro_itensConvertTblProd.setEnabled(false);
                    ct_vlrVenda_itensConvertTblProd.setEnabled(false);
                    ct_unidMedBan.setEnabled(false);
                    ct_descricao_itensConvertTblNota.setEnabled(false);
                    ct_vlrCusto_itensConvertTblNota.setEnabled(false);
                    ct_quantidadeConvertNota.setEnabled(false);
                    cbox_unidadeMedida_itensConvertTblNota.setEnabled(false);
                    btn_salvarConvert.setEnabled(false);
                    btn_cancelarConvert.setEnabled(false);
                    btn_cancelarConvert.setVisible(false);
                    btn_salvarConvert.setVisible(false);
                    jPanel76.setVisible(false);
                    ct_vlrCusto_itensConvertTblNota.setBackground(new Color(255, 255, 102));
                    ct_quantidadeConvertNota.setBackground(new Color(255, 255, 102));
                    cbox_unidadeMedida_itensConvertTblNota.setBorder(new LineBorder(new Color(197, 196, 195)));
                    //        ct_vlrVenda_itensConvertTblNota.setBackground(new Color(255,255,102));
                    conecBanTblProd.desconectar();
                    conecBanc.desconectar();
                } else {

                }
            } else {
                JOptionPane.showMessageDialog(null, "Unidade de medida inválida! \nVerifique se a unidade de medida coresponde.", "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Campo obrigatório não preenchido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_salvarConvertActionPerformed

    private void btn_salvarConvertMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_salvarConvertMousePressed

    }//GEN-LAST:event_btn_salvarConvertMousePressed

    private void btn_cancelarConvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarConvertActionPerformed
        ct_vlrCusto_itensConvertTblNota.setText("");
        cbox_unidadeMedida_itensConvertTblNota.setSelectedItem(null);
        ct_descricao_itensConvertTblNota.setText("");
        ct_descricao_itensConvertTblProd.setText("");
        ct_quantidadeConvertNota.setText("");
        ct_unidMedBan.setText("");
        ct_lucro_itensConvertTblProd.setText("");
        ct_vlrVenda_itensConvertTblProd.setText("");
        jPanel76.setVisible(false);
        btn_cancelarConvert.setVisible(false);
        btn_salvarConvert.setVisible(false);

        ct_descricao_itensConvertTblProd.setEnabled(false);
        ct_lucro_itensConvertTblProd.setEnabled(false);
        ct_vlrVenda_itensConvertTblProd.setEnabled(false);
        ct_unidMedBan.setEnabled(false);
        ct_descricao_itensConvertTblNota.setEnabled(false);
        ct_vlrCusto_itensConvertTblNota.setEnabled(false);
        ct_quantidadeConvertNota.setEnabled(false);
        cbox_unidadeMedida_itensConvertTblNota.setEnabled(false);
        btn_salvarConvert.setEnabled(false);
        btn_cancelarConvert.setEnabled(false);

        ct_vlrCusto_itensConvertTblNota.setBackground(new Color(255, 255, 102));
        ct_quantidadeConvertNota.setBackground(new Color(255, 255, 102));
        cbox_unidadeMedida_itensConvertTblNota.setBorder(new LineBorder(new Color(197, 196, 195)));
        //        ct_vlrVenda_itensConvertTblNota.setBackground(new Color(255,255,102));
    }//GEN-LAST:event_btn_cancelarConvertActionPerformed

    private void btn_cancelarConvertMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cancelarConvertMousePressed

    }//GEN-LAST:event_btn_cancelarConvertMousePressed

    private void ct_vlrCusto_itensConvertTblNotaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_vlrCusto_itensConvertTblNotaKeyReleased
        ct_vlrCusto_itensConvertTblNota.setText(ct_vlrCusto_itensConvertTblNota.getText().replaceAll("[^0-9.]", ""));
        vlrCompraConv = Float.parseFloat(ct_vlrCusto_itensConvertTblNota.getText());
    }//GEN-LAST:event_ct_vlrCusto_itensConvertTblNotaKeyReleased

    private void ct_vlrCusto_itensConvertTblNotaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_vlrCusto_itensConvertTblNotaMousePressed
        ct_vlrCusto_itensConvertTblNota.setText(ct_vlrCusto_itensConvertTblNota.getText().replaceAll("[^0-9.,]", ""));
        ct_vlrCusto_itensConvertTblNota.setText(ct_vlrCusto_itensConvertTblNota.getText().replace(",", "."));
    }//GEN-LAST:event_ct_vlrCusto_itensConvertTblNotaMousePressed

    private void ct_quantidadeConvertNotaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_quantidadeConvertNotaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_quantidadeConvertNotaKeyReleased

    private void btn_PesquisarProdutoNaoCadastradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PesquisarProdutoNaoCadastradoActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Adicionar o item a tabela 'Pesquisa'?", "Pesquisa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (resposta == JOptionPane.YES_NO_OPTION) {
            controlAddProdNota.atualizaListaItensImportadoRecebe5(Integer.parseInt(ct_codigo.getText()));
            preecherTabelaItensImportadosNovo("select * from nota_produto where importado1_novo2_converte3 =2 order by id_produto");
            preecherTabelaProdutoPesquisa("select * from produto order by nome_produto");
            preecherTabelaItensImportadosPesquisar("select * from nota_produto where importado1_novo2_converte3=5 order by id_produto");

            ct_descricao_itensSem.setText("");
            ct_codBarra_itensSem.setText("");
            cbox_unidadeMedida_itensSem.setSelectedItem(null);
            ct_vlrCusto_itensSem.setText("");
            ct_margeDeLucro.setText("");
            ct_vlrVenda_itensSem.setText("");
            ct_qtdInicial_itensSem.setText("");
            ct_estMinimo_itensSem.setText("");
            cbox_grupo_itensSem.setSelectedItem(null);
            cbox_marca_itensSem.setSelectedItem(null);

            ct_descricao_itensSem.setEnabled(false);
            ct_codBarra_itensSem.setEnabled(false);
            cbox_unidadeMedida_itensSem.setEnabled(false);
            ct_vlrCusto_itensSem.setEnabled(false);
            ct_margeDeLucro.setEnabled(false);
            ct_vlrVenda_itensSem.setEnabled(false);
            ct_qtdInicial_itensSem.setEnabled(false);
            ct_estMinimo_itensSem.setEnabled(false);
            cbox_grupo_itensSem.setEnabled(false);
            cbox_marca_itensSem.setEnabled(false);
            btn_cancelarProdutoNaoCadastrado.setEnabled(false);
            btn_salvarProdutoNaoCadastrado.setEnabled(false);

            btn_cancelarProdutoNaoCadastrado.setEnabled(false);
            btn_salvarProdutoNaoCadastrado.setEnabled(false);
            btn_addMarcaComNota.setEnabled(false);
            btn_addGrupoComNota.setEnabled(false);
            btn_PesquisarProdutoNaoCadastrado.setEnabled(false);
            ct_codigo.setText("");
        }
    }//GEN-LAST:event_btn_PesquisarProdutoNaoCadastradoActionPerformed

    private void btn_PesquisarProdutoNaoCadastradoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_PesquisarProdutoNaoCadastradoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_PesquisarProdutoNaoCadastradoMousePressed

    private void ct_codBarra_itensSemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_codBarra_itensSemKeyReleased
        ct_codBarra_itensSem.setText(ct_codBarra_itensSem.getText().replaceAll("[^0-9]", ""));

        if (ct_codBarra_itensSem.getText().length() >= 15) {
            ct_codBarra_itensSem.setText(ct_codBarra_itensSem.getText().substring(0, 15));
        } else {
        }
    }//GEN-LAST:event_ct_codBarra_itensSemKeyReleased

    private void ct_qtdInicial_itensSemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_qtdInicial_itensSemKeyReleased
        ct_qtdInicial_itensSem.setText(ct_qtdInicial_itensSem.getText().replaceAll("[^0-9]", ""));
    }//GEN-LAST:event_ct_qtdInicial_itensSemKeyReleased

    private void ct_estMinimo_itensSemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_estMinimo_itensSemKeyReleased
        ct_estMinimo_itensSem.setText(ct_estMinimo_itensSem.getText().replaceAll("[^0-9]", ""));
    }//GEN-LAST:event_ct_estMinimo_itensSemKeyReleased

    private void ct_vlrCusto_itensSemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_vlrCusto_itensSemKeyReleased

    }//GEN-LAST:event_ct_vlrCusto_itensSemKeyReleased

    private void ct_vlrCusto_itensSemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_vlrCusto_itensSemMousePressed

    }//GEN-LAST:event_ct_vlrCusto_itensSemMousePressed

    private void ct_margeDeLucroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_margeDeLucroKeyReleased
        ct_margeDeLucro.setText(ct_margeDeLucro.getText().replaceAll("[^0-9.]", ""));
        calculaValoresComNota(precoCustoNovos, Float.parseFloat(ct_margeDeLucro.getText()));
    }//GEN-LAST:event_ct_margeDeLucroKeyReleased

    private void btn_gerarCodBarraComNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gerarCodBarraComNotaActionPerformed
        conecBanTblProd.conexaoBanco();
        try {
            conecBanTblProd.executaSQL("select * from produto order by id_produto");
            conecBanTblProd.rs.last();
            int codUltimoProd = conecBanTblProd.rs.getInt("id_produto");
            codUltimoProd = codUltimoProd + 1;
            String formatCodUltimo;
            formatCodUltimo = StringUtils.leftPad("" + codUltimoProd, 4, "0");  //leftPad = PEGA UM NÚMERO COM TAMANHO '4' E PRENCHE COM '0' A ESQUERDA
            String gerarCodBarraBtnGerarSemNota = "123456789" + formatCodUltimo;
            ct_codBarra_itensSem.setText(gerarCodBarraBtnGerarSemNota);
        } catch (SQLException ex) {

        }
        conecBanTblProd.conexaoBanco();
    }//GEN-LAST:event_btn_gerarCodBarraComNotaActionPerformed

    private void jPanel82MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel82MouseEntered
        if (opGrupoComNota == 1) {
            preencheComboGrupo();
            cbox_grupo_itensSem.setSelectedItem(null);
            opGrupoComNota = 2;
        } else {
        }
    }//GEN-LAST:event_jPanel82MouseEntered

    private void cbox_grupo_itensSemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbox_grupo_itensSemActionPerformed

    }//GEN-LAST:event_cbox_grupo_itensSemActionPerformed

    private void cbox_grupo_itensSemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbox_grupo_itensSemMouseReleased

    }//GEN-LAST:event_cbox_grupo_itensSemMouseReleased

    private void cbox_grupo_itensSemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbox_grupo_itensSemMousePressed

    }//GEN-LAST:event_cbox_grupo_itensSemMousePressed

    private void btn_addGrupoComNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addGrupoComNotaActionPerformed
        FrmAddGrupo dialog = new FrmAddGrupo(new javax.swing.JFrame(), true);
        dialog.setVisible(true);
        opGrupoComNota = 1;
    }//GEN-LAST:event_btn_addGrupoComNotaActionPerformed

    private void btn_addMarcaComNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addMarcaComNotaActionPerformed
        FrmAddMarca dialog = new FrmAddMarca(new javax.swing.JFrame(), true);
        dialog.setVisible(true);
        opMarcaComNota = 1;
    }//GEN-LAST:event_btn_addMarcaComNotaActionPerformed

    private void sMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sMouseEntered
        if (opMarcaComNota == 1) {
            preencheComboMarca();
            cbox_marca_itensSem.setSelectedItem(null);
            opMarcaComNota = 2;
        } else {
        }
    }//GEN-LAST:event_sMouseEntered

    private void cbox_marca_itensSemMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbox_marca_itensSemMousePressed

    }//GEN-LAST:event_cbox_marca_itensSemMousePressed

    private void btn_cancelarProdutoNaoCadastradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarProdutoNaoCadastradoActionPerformed
        ct_descricao_itensSem.setText("");
        ct_codBarra_itensSem.setText("");
        cbox_unidadeMedida_itensSem.setSelectedItem(null);
        ct_vlrCusto_itensSem.setText("");
        ct_margeDeLucro.setText("");
        ct_vlrVenda_itensSem.setText("");
        ct_qtdInicial_itensSem.setText("");
        ct_estMinimo_itensSem.setText("");
        cbox_grupo_itensSem.setSelectedItem(null);
        cbox_marca_itensSem.setSelectedItem(null);

        ct_descricao_itensSem.setEnabled(false);
        ct_codBarra_itensSem.setEnabled(false);
        cbox_unidadeMedida_itensSem.setEnabled(false);
        ct_vlrCusto_itensSem.setEnabled(false);
        ct_margeDeLucro.setEnabled(false);
        ct_vlrVenda_itensSem.setEnabled(false);
        ct_qtdInicial_itensSem.setEnabled(false);
        ct_estMinimo_itensSem.setEnabled(false);
        cbox_grupo_itensSem.setEnabled(false);
        cbox_marca_itensSem.setEnabled(false);
        ct_codigo.setText("");
        btn_gerarCodBarraComNota.setVisible(false);

        ct_descricao_itensSem.setBackground(new Color(252, 252, 146));
        ct_codBarra_itensSem.setBackground(new Color(252, 252, 146));
        ct_qtdInicial_itensSem.setBackground(new Color(252, 252, 146));
        ct_estMinimo_itensSem.setBackground(new Color(252, 252, 146));
        ct_vlrCusto_itensSem.setBackground(new Color(252, 252, 146));
        ct_margeDeLucro.setBackground(new Color(252, 252, 146));
        ct_vlrVenda_itensSem.setBackground(new Color(252, 252, 146));
        cbox_unidadeMedida_itensSem.setBorder(new LineBorder(new Color(197, 196, 195)));
        cbox_marca_itensSem.setBorder(new LineBorder(new Color(197, 196, 195)));
        cbox_grupo_itensSem.setBorder(new LineBorder(new Color(197, 196, 195)));

        btn_cancelarProdutoNaoCadastrado.setEnabled(false);
        btn_salvarProdutoNaoCadastrado.setEnabled(false);
        btn_ConverteProdutoNaoCadastrado.setEnabled(false);
        btn_PesquisarProdutoNaoCadastrado.setEnabled(false);
        btn_addMarcaComNota.setEnabled(false);
        btn_addGrupoComNota.setEnabled(false);

    }//GEN-LAST:event_btn_cancelarProdutoNaoCadastradoActionPerformed

    private void btn_cancelarProdutoNaoCadastradoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_cancelarProdutoNaoCadastradoMousePressed

    }//GEN-LAST:event_btn_cancelarProdutoNaoCadastradoMousePressed

    private void btn_salvarProdutoNaoCadastradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salvarProdutoNaoCadastradoActionPerformed
        int op1, op2,op4,op6, op7, op8, op9, op10, op11;
        JOptionPane.showMessageDialog(null,quantidadeSetRNovos);
        JOptionPane.showMessageDialog(null,igual1_div2_mult3Novos);
        String codBarraVeriNaoCadas = "";
        conecBanTblProd.conexaoBanco();
        try {
            conecBanTblProd.executaSQL("select * from produto where codbarra='" + ct_codBarra_itensSem.getText() + "'");
            conecBanTblProd.rs.first();
            codBarraVeriNaoCadas = conecBanTblProd.rs.getString("codbarra");
        } catch (SQLException ex) {
        }
        conecBanTblProd.desconectar();

        if (ct_descricao_itensSem.getText().equals("")) {
            ct_descricao_itensSem.setBackground(new Color(246, 170, 170));
            op1 = 1;
        } else {
            ct_descricao_itensSem.setBackground(new Color(255, 255, 102));
            op1 = 2;
        }

        if (ct_codBarra_itensSem.getText().equals("")) {
            ct_codBarra_itensSem.setBackground(new Color(246, 170, 170));
            op2 = 1;
        } else {
            ct_codBarra_itensSem.setBackground(new Color(255, 255, 102));
            op2 = 2;
        }

        if (ct_estMinimo_itensSem.getText().equals("")) {
            ct_estMinimo_itensSem.setBackground(new Color(246, 170, 170));
            op4 = 1;
        } else {
            ct_estMinimo_itensSem.setBackground(new Color(255, 255, 102));
            op4 = 2;
        }

        if (ct_margeDeLucro.getText().equals("")) {
            ct_margeDeLucro.setBackground(new Color(246, 170, 170));
            op6 = 1;
        } else {
            ct_margeDeLucro.setBackground(new Color(255, 255, 102));
            op6 = 2;
        }

        if (cbox_marca_itensSem.getSelectedIndex() == -1) {
            cbox_marca_itensSem.setBorder(new LineBorder(new Color(255, 0, 0)));
            op7 = 1;
        } else {
            cbox_marca_itensSem.setBorder(new LineBorder(null));
            op7 = 2;
        }

        if (cbox_grupo_itensSem.getSelectedIndex() == -1) {
            cbox_grupo_itensSem.setBorder(new LineBorder(new Color(255, 0, 0)));
            op8 = 1;
        } else {
            cbox_grupo_itensSem.setBorder(new LineBorder(null));
            op8 = 2;
        }

        if (cbox_unidadeMedida_itensSem.getSelectedIndex() == -1) {
            cbox_unidadeMedida_itensSem.setBorder(new LineBorder(new Color(255, 0, 0)));
            op9 = 1;
        } else {
            cbox_unidadeMedida_itensSem.setBorder(new LineBorder(null));
            op9 = 2;
        }

        if (ct_codBarra_itensSem.getText().equals(codBarraVeriNaoCadas)) {
            op10 = 1;
        } else {
            op10 = 2;
        }

        if (ct_codBarra_itensSem.getText().length() < 13) {
            op11 = 1;
        } else {
            op11 = 2;
        }

        if (op1 == 2 && op2 == 2 && op4 == 2 && op6 == 2 && op7 == 2 && op8 == 2 && op9 == 2) {
            if (op11 == 1) {
                JOptionPane.showMessageDialog(null, "Código de barras muito curto(mínimo 13 caracteres)! \nTente outro código de barras.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                if (op10 == 1) {
                    JOptionPane.showMessageDialog(null, "Código de barras já cadastrado! \nTente outro código de barras.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    int resposta = JOptionPane.showConfirmDialog(null, "Salvar novo produto?", "Novo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resposta == JOptionPane.YES_NO_OPTION) {
                        modProdSemNota.setDescricaoSN(ct_descricao_itensSem.getText());
                        modProdSemNota.setCodBarrasSN(ct_codBarra_itensSem.getText());
                        modProdSemNota.setPrecoCompraSN(precoCustoNovos);
                        modProdSemNota.setPrecoVendaSN(preVendaNovos);
                        modProdSemNota.setPercentualItemSN(Float.parseFloat(ct_margeDeLucro.getText()));
                        modProdSemNota.setQuantidadeCN(Integer.parseInt(ct_qtdInicial_itensSem.getText()));
                        modProdSemNota.setUnidadeMedidaSN("" + cbox_unidadeMedida_itensSem.getSelectedItem());
                        modProdSemNota.setGrupoSN("" + cbox_grupo_itensSem.getSelectedItem());
                        modProdSemNota.setMarcaSN("" + cbox_marca_itensSem.getSelectedItem());
                        modProdSemNota.setFornecedorSN(nomeFornecedor);
                        modProdSemNota.setVlrGanhoItemSN(vlrganhoItemNovos);
                        modProdSemNota.setQtdMinimaEstoqSN(Integer.parseInt(ct_estMinimo_itensSem.getText()));
                        modProdSemNota.setCodCeanImportSN(codCeanImport);
                        controlProdSemNota.inserirProdutoSemNota(modProdSemNota);

                        //CRIA LIGAÇÃO DE IMPORTAÇÃO
                        modCeanTribCeanImport.setCodCeanTblProd(codCeanImport);
                        modCeanTribCeanImport.setCodCeanTribTblNota(codCeanImport);
                        controleCeanTribCeanImport.addNovaLigacaoCeanTribComCeanImport(modCeanTribCeanImport);
                        
                        //CRIA LIGAÇÃO UNIDADEMEDIDA
                        modUnidMed.setCodCeanTribUM(codCeanImport);
                        modUnidMed.setIgual1_div2_mult3UM(igual1_div2_mult3Novos);
                        modUnidMed.setQuantidadeUM(quantidadeSetRNovos);
                        modUnidMed.setUnidMedidaNotaUM(unidadeMedidaNotaProd);
                        contrUnidMed.criaLigacaoUnidadeMedida(modUnidMed);
                        
                        //ATUALIZA STATUS DO PRODUTO PARA 1= IMPORTAÇÃO FINALIZADA
                        controlAddProdNota.atualizaListaItensImportadoRecebe1(Integer.parseInt(ct_codigo.getText()));
                        
                        preecherTabelaItensImportadosNovo("select * from nota_produto where importado1_novo2_converte3 =2 order by id_produto");
                        preecherTabelaItensImportadosConverte("select * from nota_produto where importado1_novo2_converte3 =3 order by id_produto");
                        preecherTabelaProdutoPesquisa("select * from produto order by nome_produto");
                        preecherTabela("select * from produto order by nome_produto");

                        ct_descricao_itensSem.setText("");
                        ct_codBarra_itensSem.setText("");
                        cbox_unidadeMedida_itensSem.setSelectedItem(null);
                        ct_vlrCusto_itensSem.setText("");
                        ct_margeDeLucro.setText("");
                        ct_vlrVenda_itensSem.setText("");
                        ct_qtdInicial_itensSem.setText("");
                        ct_estMinimo_itensSem.setText("");
                        cbox_grupo_itensSem.setSelectedItem(null);
                        cbox_marca_itensSem.setSelectedItem(null);

                        ct_descricao_itensSem.setEnabled(false);
                        ct_codBarra_itensSem.setEnabled(false);
                        cbox_unidadeMedida_itensSem.setEnabled(false);
                        ct_vlrCusto_itensSem.setEnabled(false);
                        ct_margeDeLucro.setEnabled(false);
                        ct_vlrVenda_itensSem.setEnabled(false);
                        ct_qtdInicial_itensSem.setEnabled(false);
                        ct_estMinimo_itensSem.setEnabled(false);
                        cbox_grupo_itensSem.setEnabled(false);
                        cbox_marca_itensSem.setEnabled(false);

                        btn_gerarCodBarraComNota.setVisible(false);
//                        vlrganhoItemNovos=0; //SALVA O VALOR GANHO PARA CADA ITEM
//                        preVendaNovos=0;     // SALVA O VALOR DE VENDA DE CADA ITEM
//                        precoCustoNovos=0;    // SALVA O PRECO DE COMPRA DO ITEM

                        ct_codigo.setText("");
                        btn_cancelarProdutoNaoCadastrado.setEnabled(false);
                        btn_salvarProdutoNaoCadastrado.setEnabled(false);
                        btn_ConverteProdutoNaoCadastrado.setEnabled(false);
                        btn_PesquisarProdutoNaoCadastrado.setEnabled(false);
                        btn_addMarcaComNota.setEnabled(false);
                        btn_addGrupoComNota.setEnabled(false);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Campo obrigatório não preenchido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btn_salvarProdutoNaoCadastradoActionPerformed

    private void btn_salvarProdutoNaoCadastradoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_salvarProdutoNaoCadastradoMousePressed

    }//GEN-LAST:event_btn_salvarProdutoNaoCadastradoMousePressed

    private void ct_descricao_itensSemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_descricao_itensSemKeyReleased
        if (ct_descricao_itensSem.getText().length() >= 45) {
            ct_descricao_itensSem.setText(ct_descricao_itensSem.getText().substring(0, 45));
        } else {
        }
    }//GEN-LAST:event_ct_descricao_itensSemKeyReleased

    private void tbl_itensSemCadastroMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_itensSemCadastroMousePressed
        btn_cancelarProdutoNaoCadastrado.setEnabled(true);
        btn_salvarProdutoNaoCadastrado.setEnabled(true);
        btn_ConverteProdutoNaoCadastrado.setEnabled(true);
        btn_PesquisarProdutoNaoCadastrado.setEnabled(true);
        btn_addMarcaComNota.setEnabled(true);
        btn_addGrupoComNota.setEnabled(true);

        ct_descricao_itensSem.setText("");
        ct_codBarra_itensSem.setText("");
        cbox_unidadeMedida_itensSem.setSelectedItem(null);
        ct_vlrCusto_itensSem.setText("");
        ct_margeDeLucro.setText("");
        ct_vlrVenda_itensSem.setText("");
        ct_qtdInicial_itensSem.setText("");
        ct_estMinimo_itensSem.setText("");
        cbox_grupo_itensSem.setSelectedItem(null);
        cbox_marca_itensSem.setSelectedItem(null);

        ct_descricao_itensSem.setEnabled(true);
        ct_codBarra_itensSem.setEnabled(true);
        cbox_unidadeMedida_itensSem.setEnabled(true);
        ct_vlrCusto_itensSem.setEnabled(true);
        ct_margeDeLucro.setEnabled(true);
        ct_vlrVenda_itensSem.setEnabled(false);
        ct_qtdInicial_itensSem.setEnabled(true);
        ct_estMinimo_itensSem.setEnabled(true);
        cbox_grupo_itensSem.setEnabled(true);
        cbox_marca_itensSem.setEnabled(true);
        btn_cancelarProdutoNaoCadastrado.setEnabled(true);
        btn_salvarProdutoNaoCadastrado.setEnabled(true);

        ct_codigo.setText("");
        ct_descricao_itensSem.setBackground(new Color(255, 255, 102));
        ct_codBarra_itensSem.setBackground(new Color(255, 255, 102));
        ct_estMinimo_itensSem.setBackground(new Color(255, 255, 102));
        ct_margeDeLucro.setBackground(new Color(255, 255, 102));
        cbox_unidadeMedida_itensSem.setBorder(new LineBorder(new Color(197, 196, 195)));
        cbox_marca_itensSem.setBorder(new LineBorder(new Color(197, 196, 195)));
        cbox_grupo_itensSem.setBorder(new LineBorder(new Color(197, 196, 195)));

        conecBanc.conexaoBanco();
        String idProdutoNota = "" + tbl_itensSemCadastro.getValueAt(tbl_itensSemCadastro.getSelectedRow(), 0);
        try {
            conecBanc.executaSQL("select * from nota_produto where id_produto='" + idProdutoNota + "'");
            conecBanc.rs.first();
            ct_descricao_itensSem.setText(conecBanc.rs.getString("nome_prod"));
            codCeanImport = conecBanc.rs.getString("cod_cean_trib");
            ct_codigo.setText("" + conecBanc.rs.getInt("id_produto"));
            cbox_unidadeMedida_itensSem.setSelectedItem("" + conecBanc.rs.getString("unid_medida"));
            unidadeMedidaNotaProd=conecBanc.rs.getString("unid_medida");
            conecPesquisa.conexaoBanco();
            conecPesquisa.executaSQL("select * from nota_inicio where id_nota='" + conecBanc.rs.getInt("id_nota_inicio") + "'");
            conecPesquisa.rs.first();
            String verifSeEItemSemCodBarra = conecBanc.rs.getString("cod_prod") + conecPesquisa.rs.getString("id_fornecedor");
            String codCeanGeradoCprodIdforn = StringUtils.leftPad("" + verifSeEItemSemCodBarra, 13, "0");  //COMPLETA O RESTO COM 0
            conecPesquisa.desconectar();
            if (codCeanImport.equals(codCeanGeradoCprodIdforn)) {
                ct_codBarra_itensSem.setText("SEM CÓDIGO");
                btn_gerarCodBarraComNota.setVisible(true);
                btn_gerarCodBarraComNota.setEnabled(true);
            } else {
                ct_codBarra_itensSem.setText(conecBanc.rs.getString("cod_cean_trib"));
                btn_gerarCodBarraComNota.setVisible(false);
                btn_gerarCodBarraComNota.setEnabled(false);
            }

            //SETA VALORES PARA CALCULO
            quantidadeNovos = conecBanc.rs.getInt("qtd_recebida");
            precoCustoNovos = conecBanc.rs.getFloat("vlr_unit_prod");
            convertProdutoComNota(quantidadeNovos, precoCustoNovos, 1, 1);
            igual1_div2_mult3Novos=1;

            //PREENCHE CAMPO DA JANELA CONVERT
            ct_codigoConvert.setText("" + conecBanc.rs.getInt("id_produto"));
            ct_descricaoConvert.setText(conecBanc.rs.getString("nome_prod"));
            ct_quantidadeConvert.setText("" + quantidadeNovos);
            ct_vlrCustoConvert.setText(numberFormat.format(precoCustoNovos));
            cbox_unidadeMedidaConvert.setSelectedItem("" + conecBanc.rs.getString("unid_medida"));
            ct_quantidade2Convert.setText("" + quantidadeNovos);

            //SETA NOME DO FORNECEDOR
            conecPesquisa.conexaoBanco();
            conecPesquisa.executaSQL("select * from nota_inicio where id_nota='" + conecBanc.rs.getInt("id_nota_inicio") + "'");
            conecPesquisa.rs.first();
            conecBanc.executaSQL("select * from fornecedores where id_fornecedor='" + conecPesquisa.rs.getInt("id_fornecedor") + "'");
            conecBanc.rs.first();
            nomeFornecedor = conecBanc.rs.getString("nome_fornecedor");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao selecionar item! \nErro: " + ex);
        }
        conecBanc.desconectar();
        conecPesquisa.desconectar();
    }//GEN-LAST:event_tbl_itensSemCadastroMousePressed

    private void tbl_itensSemCadastroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_itensSemCadastroMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_itensSemCadastroMouseExited

    private void tbl_itensSemCadastroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_itensSemCadastroMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_itensSemCadastroMouseClicked

    private void btn_ConverteProdutoNaoCadastradoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ConverteProdutoNaoCadastradoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_ConverteProdutoNaoCadastradoMousePressed

    private void btn_ConverteProdutoNaoCadastradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ConverteProdutoNaoCadastradoActionPerformed
        jDialogConverte.setLocationRelativeTo(null);
        jDialogConverte.setVisible(true);
    }//GEN-LAST:event_btn_ConverteProdutoNaoCadastradoActionPerformed

    private void btn_divConvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_divConvertActionPerformed
        igual1_div2_mult3Novos=2;
        ct_quantidadeConvertSet.setText("");
        btn_multConvet.setEnabled(true);
        btn_igualConvert.setEnabled(true);
        btn_divConvert.setEnabled(false);
        ct_quantidadeConvertSet.setEnabled(true);       
        convertProdutoComNota(quantidadeNovos, precoCustoNovos, 2, Integer.parseInt(ct_quantidadeConvertSet.getText()));
    }//GEN-LAST:event_btn_divConvertActionPerformed

    private void btn_multConvetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_multConvetActionPerformed
        igual1_div2_mult3Novos=3;
        ct_quantidadeConvertSet.setText("");
        btn_multConvet.setEnabled(false);
        btn_igualConvert.setEnabled(true);
        btn_divConvert.setEnabled(true);
        ct_quantidadeConvertSet.setEnabled(true);
        convertProdutoComNota(quantidadeNovos, precoCustoNovos, 3, Integer.parseInt(ct_quantidadeConvertSet.getText()));
    }//GEN-LAST:event_btn_multConvetActionPerformed

    private void ct_quantidadeConvertKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_quantidadeConvertKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_quantidadeConvertKeyReleased

    private void ct_vlrCustoConvertMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_vlrCustoConvertMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_vlrCustoConvertMousePressed

    private void ct_vlrCustoConvertKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_vlrCustoConvertKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_vlrCustoConvertKeyReleased

    private void ct_quantidade2ConvertKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_quantidade2ConvertKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_quantidade2ConvertKeyReleased

    private void ct_descricaoConvertKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_descricaoConvertKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_descricaoConvertKeyReleased

    private void ct_quantidadeResulConvertKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_quantidadeResulConvertKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_quantidadeResulConvertKeyReleased

    private void ct_precoCustoConvertResulKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_precoCustoConvertResulKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_precoCustoConvertResulKeyReleased

    private void ct_quantidadeConvertSetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_quantidadeConvertSetKeyReleased
        ct_quantidadeConvertSet.setText(ct_quantidadeConvertSet.getText().replaceAll("[^0-9]", ""));
        if(igual1_div2_mult3Novos==3){
          convertProdutoComNota(quantidadeNovos, precoCustoNovos,3 , Integer.parseInt(ct_quantidadeConvertSet.getText()));   
        }
        if(igual1_div2_mult3Novos==2){
          convertProdutoComNota(quantidadeNovos, precoCustoNovos,2 , Integer.parseInt(ct_quantidadeConvertSet.getText()));   
        }
    }//GEN-LAST:event_ct_quantidadeConvertSetKeyReleased

    private void btn_igualConvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_igualConvertActionPerformed
        igual1_div2_mult3Novos=1;    
        convertProdutoComNota(quantidadeNovos, precoCustoNovos,1 , 1);
    }//GEN-LAST:event_btn_igualConvertActionPerformed

    private void btn_cancelaJanelaConvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelaJanelaConvertActionPerformed
        igual1_div2_mult3Novos=1;    
        convertProdutoComNota(quantidadeNovos, precoCustoNovos,1 , 1);
        jDialogConverte.dispose();
    }//GEN-LAST:event_btn_cancelaJanelaConvertActionPerformed

    private void btn_okConvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_okConvertActionPerformed
    int resposta = JOptionPane.showConfirmDialog(null, "Confirma conversão?", "Conversão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (resposta == JOptionPane.YES_NO_OPTION) {
       precoCustoNovos= precoCustoReturnNovos;
       quantidadeNovos = quantidadeReturnNovos;
       jDialogConverte.dispose();
       btn_ConverteProdutoNaoCadastrado.setEnabled(false);
    }
    }//GEN-LAST:event_btn_okConvertActionPerformed

    private void ct_codBarra_convertKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_codBarra_convertKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_codBarra_convertKeyReleased
    
    public void calculaValoresComNota(float precoCusto, float margem){
        preVendaNovos= (precoCusto*margem) + precoCusto;
        vlrganhoItemNovos= precoCusto*margem;
        ct_vlrVenda_itensSem.setText(numberFormat.format(preVendaNovos));
    }
    
    public void convertProdutoComNota(int quantidade, float precoCusto, int igual1_div2_mult3, int quantidadeSet) {
   
        if (igual1_div2_mult3 == 1) { //IGUAL
        ct_quantidadeConvertSet.setText("");
        btn_multConvet.setEnabled(true);
        btn_igualConvert.setEnabled(false);
        btn_divConvert.setEnabled(true);
        ct_quantidadeConvertSet.setEnabled(false); 
        
            quantidadeSetRNovos= quantidadeSet;
            precoCustoReturnNovos = precoCusto;
            quantidadeReturnNovos = quantidade;
            ct_quantidadeResulConvert.setText("" + quantidadeReturnNovos);
            ct_precoCustoConvertResul.setText("" + numberFormat.format(precoCustoReturnNovos));
            ct_qtdInicial_itensSem.setText("" + quantidadeReturnNovos);
            ct_vlrCusto_itensSem.setText("" + numberFormat.format(precoCustoReturnNovos));
        }

        if (igual1_div2_mult3 == 2) { //DIVIDIR
            quantidadeSetRNovos= quantidadeSet;
            precoCustoReturnNovos = (precoCusto * quantidadeSet);
            quantidadeReturnNovos = (quantidade / quantidadeSet);
            ct_quantidadeResulConvert.setText("" + quantidadeReturnNovos);
            ct_precoCustoConvertResul.setText("" + numberFormat.format(precoCustoReturnNovos));
            ct_qtdInicial_itensSem.setText("" + quantidadeReturnNovos);
            ct_vlrCusto_itensSem.setText("" + numberFormat.format(precoCustoReturnNovos));

        }

        if (igual1_div2_mult3 == 3) { //MULTIPLICAR       
            quantidadeSetRNovos= quantidadeSet;
            precoCustoReturnNovos = (precoCusto / quantidadeSet);
            quantidadeReturnNovos = (quantidade * quantidadeSet);
            ct_quantidadeResulConvert.setText(""+ quantidadeReturnNovos);
            ct_precoCustoConvertResul.setText(""+ numberFormat.format(precoCustoReturnNovos));
            ct_qtdInicial_itensSem.setText("" + quantidadeReturnNovos);
            ct_vlrCusto_itensSem.setText("" + numberFormat.format(precoCustoReturnNovos));
            
        }

    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JPanel a1;
    private javax.swing.JPanel a4;
    private javax.swing.JPanel a5;
    private javax.swing.JButton btn_ConverteProdutoNaoCadastrado;
    private javax.swing.JButton btn_PesquisarProdutoNaoCadastrado;
    private javax.swing.JButton btn_addFornecedorSemNota;
    private javax.swing.JButton btn_addGrupoComNota;
    private javax.swing.JButton btn_addGrupoSemNota;
    private javax.swing.JButton btn_addMarcaComNota;
    private javax.swing.JButton btn_addMarcaSemNota;
    private javax.swing.JButton btn_cadastradoItensSemCodigo;
    private javax.swing.JButton btn_cancelaJanelaConvert;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_cancelarConvert;
    private javax.swing.JButton btn_cancelarProdutoNaoCadastrado;
    private javax.swing.JButton btn_cancelarProdutosSemCodigo;
    private javax.swing.JButton btn_colarCodBarraitensSemCodBarra;
    private javax.swing.JButton btn_divConvert;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_excluir;
    private javax.swing.JButton btn_gerarCodBarraComNota;
    private javax.swing.JButton btn_gerarCodBarraSemNota;
    private javax.swing.JButton btn_igualConvert;
    private javax.swing.JButton btn_ler_nota_fiscal;
    private javax.swing.JButton btn_multConvet;
    private javax.swing.JButton btn_novo;
    private javax.swing.JButton btn_novoItensSemCodigo;
    private javax.swing.JButton btn_okConvert;
    private javax.swing.JButton btn_salvar;
    private javax.swing.JButton btn_salvarConvert;
    private javax.swing.JButton btn_salvarProdutoNaoCadastrado;
    private javax.swing.JComboBox<String> cbox_fornecedor;
    private javax.swing.JComboBox<String> cbox_grupo;
    private javax.swing.JComboBox<String> cbox_grupo_itensSem;
    private javax.swing.JComboBox<String> cbox_marca;
    private javax.swing.JComboBox<String> cbox_marca_itensSem;
    private javax.swing.JComboBox<String> cbox_unidade;
    private javax.swing.JComboBox<String> cbox_unidadeMedidaConvert;
    private javax.swing.JComboBox<String> cbox_unidadeMedida_itensConvertTblNota;
    private javax.swing.JComboBox<String> cbox_unidadeMedida_itensSem;
    private javax.swing.JTextField ct_chaveNota;
    private javax.swing.JTextField ct_cod;
    private javax.swing.JTextField ct_codBarra;
    private javax.swing.JTextField ct_codBarraSemcodigoBarra;
    private javax.swing.JTextField ct_codBarra_convert;
    private javax.swing.JTextField ct_codBarra_itensSem;
    private javax.swing.JTextField ct_codigo;
    private javax.swing.JTextField ct_codigoConvert;
    private javax.swing.JTextField ct_descricaoConvert;
    private javax.swing.JTextField ct_descricao_itensConvertTblNota;
    private javax.swing.JTextField ct_descricao_itensConvertTblProd;
    private javax.swing.JTextField ct_descricao_itensSem;
    private javax.swing.JTextField ct_descricao_itensSemCodigo;
    private javax.swing.JTextField ct_estMinimo_itensSem;
    private javax.swing.JTextField ct_lucro_itensConvertTblProd;
    private javax.swing.JTextField ct_margeDeLucro;
    private javax.swing.JTextField ct_numItemItensSemCodigo;
    private javax.swing.JTextField ct_percentual;
    private javax.swing.JTextField ct_pesquisar;
    private javax.swing.JTextField ct_pesquisar1;
    private javax.swing.JTextField ct_precoCustoConvertResul;
    private javax.swing.JTextField ct_preco_compra;
    private javax.swing.JTextField ct_preco_venda;
    private javax.swing.JTextField ct_produto;
    private javax.swing.JTextField ct_qtdInicial_itensSem;
    private javax.swing.JTextField ct_qtdMinEstq;
    private javax.swing.JTextField ct_quantidade;
    private javax.swing.JTextField ct_quantidade2Convert;
    private javax.swing.JTextField ct_quantidadeConvert;
    private javax.swing.JTextField ct_quantidadeConvertNota;
    private javax.swing.JTextField ct_quantidadeConvertSet;
    private javax.swing.JTextField ct_quantidadeResulConvert;
    private javax.swing.JTextField ct_unidMedBan;
    private javax.swing.JTextField ct_vlrCustoConvert;
    private javax.swing.JTextField ct_vlrCusto_itensConvertTblNota;
    private javax.swing.JTextField ct_vlrCusto_itensSem;
    private javax.swing.JTextField ct_vlrVenda_itensConvertTblProd;
    private javax.swing.JTextField ct_vlrVenda_itensSem;
    private javax.swing.JTextField ct_vlr_ganho;
    private javax.swing.JButton jButton1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JDesktopPane jDesktopPane3;
    private javax.swing.JDesktopPane jDesktopPane5;
    private javax.swing.JDesktopPane jDesktopPane6;
    private javax.swing.JDesktopPane jDesktopPane7;
    private javax.swing.JDesktopPane jDesktopPane8;
    private javax.swing.JDesktopPane jDesktopPane9;
    private javax.swing.JDialog jDialogConverte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel100;
    private javax.swing.JPanel jPanel102;
    private javax.swing.JPanel jPanel103;
    private javax.swing.JPanel jPanel104;
    private javax.swing.JPanel jPanel105;
    private javax.swing.JPanel jPanel106;
    private javax.swing.JPanel jPanel107;
    private javax.swing.JPanel jPanel108;
    private javax.swing.JPanel jPanel109;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel110;
    private javax.swing.JPanel jPanel111;
    private javax.swing.JPanel jPanel112;
    private javax.swing.JPanel jPanel113;
    private javax.swing.JPanel jPanel116;
    private javax.swing.JPanel jPanel117;
    private javax.swing.JPanel jPanel118;
    private javax.swing.JPanel jPanel119;
    private javax.swing.JPanel jPanel120;
    private javax.swing.JPanel jPanel121;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel79;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel80;
    private javax.swing.JPanel jPanel81;
    private javax.swing.JPanel jPanel82;
    private javax.swing.JPanel jPanel83;
    private javax.swing.JPanel jPanel85;
    private javax.swing.JPanel jPanel86;
    private javax.swing.JPanel jPanel87;
    private javax.swing.JPanel jPanel88;
    private javax.swing.JPanel jPanel89;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel90;
    private javax.swing.JPanel jPanel91;
    private javax.swing.JPanel jPanel92;
    private javax.swing.JPanel jPanel93;
    private javax.swing.JPanel jPanel96;
    private javax.swing.JPanel jPanel97;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JDesktopPane jp_conversão;
    private javax.swing.JDesktopPane jp_novos;
    private javax.swing.JDesktopPane jp_sem_codigo_barra;
    private javax.swing.JTabbedPane jt_importarProdutos;
    private javax.swing.JPanel s;
    private javax.swing.JTable tbl_itensConvete;
    private javax.swing.JTable tbl_itensSemCadastro;
    private javax.swing.JTable tbl_itensSemCodBarra;
    private javax.swing.JTable tbl_produtos;
    private javax.swing.JTable tbm_itensCadastradosProdSemCodBarra;
    // End of variables declaration//GEN-END:variables
}
