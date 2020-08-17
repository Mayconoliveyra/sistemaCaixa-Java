/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao;

import Controle.ConectaBanco;
import Controle.ControleProdutoSemNota;
import Controle.ModeloTabela;
import Modelo.ModeloProdutoSemNota;
import com.lowagie.text.Font;
import java.awt.Color;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class VsProdutos extends javax.swing.JFrame {
     NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
         NumberFormat numberFormat2 = NumberFormat.getNumberInstance(new Locale("en", "US"));
         
     ConectaBanco conecBanc = new ConectaBanco();
     ConectaBanco conecPesquisa = new ConectaBanco();
     ModeloProdutoSemNota mod = new ModeloProdutoSemNota();
     ControleProdutoSemNota control= new ControleProdutoSemNota();
      
     String modoNavegar;
   NumberFormat formant;
   
    public VsProdutos() {
        initComponents();
            
      numberFormat2.setMaximumFractionDigits(2);
      numberFormat2.setMinimumFractionDigits(2);
        preecherTabela("select *from produto");
    /////////////////////////////////////////////////////////////////////////////////////////

//        float con = 36;
//        String a = numberFormat.format(con);
//        
//        JOptionPane.showMessageDialog(rootPane,a);
    //////////////////////////////////////////////////////////////////////         preencheCombo();
        
      numberFormat2.setMaximumFractionDigits(2);
      numberFormat2.setMinimumFractionDigits(2);
        preecherTabela("select *from produto");
    /////////////////////////////////////////////////////////////////////////////////////////

//        float con = 36;
//        String a = numberFormat.format(con);
//        
//        JOptionPane.showMessageDialog(rootPane,a);
    //////////////////////////////////////////////////////////////////////  
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_produtos = new javax.swing.JTable();
        ct_pesquisar = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        ct_nome = new javax.swing.JTextField();
        ct_preco = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ct_desconto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_sair = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbl_produtos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        tbl_produtos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_produtos.setIntercellSpacing(new java.awt.Dimension(0, 4));
        tbl_produtos.setOpaque(false);
        tbl_produtos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_produtosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_produtos);

        ct_pesquisar.setBackground(new java.awt.Color(0, 238, 255));
        ct_pesquisar.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ct_pesquisar.setText("Pesquisar produto");
        ct_pesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ct_pesquisarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ct_pesquisarMouseEntered(evt);
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

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ct_nome.setEditable(false);
        ct_nome.setBackground(new java.awt.Color(0, 238, 255));
        ct_nome.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        ct_preco.setEditable(false);
        ct_preco.setBackground(new java.awt.Color(0, 238, 255));
        ct_preco.setFont(new java.awt.Font("Arial Black", 1, 20)); // NOI18N
        ct_preco.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ct_preco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ct_precoActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PREÇO UNITÁRIO");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 102));
        jLabel4.setText("4% DESCONTO");

        ct_desconto.setEditable(false);
        ct_desconto.setBackground(new java.awt.Color(0, 238, 255));
        ct_desconto.setFont(new java.awt.Font("Arial Black", 1, 20)); // NOI18N
        ct_desconto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NOME");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ct_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ct_preco, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(ct_desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel2)
                        .addGap(99, 99, 99)
                        .addComponent(jLabel4)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ct_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ct_preco, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(ct_desconto))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(ct_pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ct_pesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LISTA DE PRODUTOS");
        jLabel1.setToolTipText("");

        btn_sair.setBackground(new java.awt.Color(255, 255, 255));
        btn_sair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/AAfechaABA.png"))); // NOI18N
        btn_sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_sair, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_sair, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(340, 230, 1017, 528);
    }// </editor-fold>//GEN-END:initComponents

    
        public void preecherTabela(String SQL){ // METODO PARA PREENCHER TABELA NA INTERFACE
        ArrayList dados = new ArrayList();
        
        String [] Colunas  = new String []{"Cod. Barra","Produto","Pre. Compra","Pre. Venda","QTD"};
        conecBanc.conexaoBanco();
        conecBanc.executaSQL(SQL);
        
      try {
          conecBanc.rs.first();
          do {
              dados.add(new Object[]{conecBanc.rs.getString("codbarra"), conecBanc.rs.getString("nome_produto"),(numberFormat.format(conecBanc.rs.getFloat("preco_compra"))),(numberFormat.format(conecBanc.rs.getFloat("preco_venda"))),conecBanc.rs.getInt("quantidade")});
              
          }while (conecBanc.rs.next());
          
      } catch (SQLException ex) {
//         JOptionPane.showMessageDialog(rootPane, "Erro ao Preencher o ArrayList! \n"+ ex);
      }
      
       ModeloTabela modelo = new ModeloTabela(dados,Colunas);
       
        tbl_produtos.setModel(modelo);
        DefaultTableCellRenderer headerRenderer2 = new DefaultTableCellRenderer(); 
        headerRenderer2.setOpaque(true); 
        headerRenderer2.setBackground(new Color(0,0,51));
        headerRenderer2.setFont(new java.awt.Font("Arial Black", Font.BOLD, 23));
        headerRenderer2.setForeground(new Color(255,255,255));
        for (int i = 0; i < tbl_produtos.getModel().getColumnCount(); 
         i++) { tbl_produtos.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer2); }

          tbl_produtos.setRowHeight(40); //  TAMANHO DA JANELA PRA CADA ITEM
        tbl_produtos.getColumnModel().getColumn(0).setPreferredWidth(120);
        tbl_produtos.getColumnModel().getColumn(0).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(1).setPreferredWidth(400);
        tbl_produtos.getColumnModel().getColumn(1).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbl_produtos.getColumnModel().getColumn(2).setResizable(false); 
        tbl_produtos.getColumnModel().getColumn(3).setPreferredWidth(80);
        tbl_produtos.getColumnModel().getColumn(3).setResizable(false);
        tbl_produtos.getColumnModel().getColumn(4).setPreferredWidth(50);
        tbl_produtos.getColumnModel().getColumn(4).setResizable(false);
        tbl_produtos.getTableHeader().setReorderingAllowed(false);
//        tbl_produtos.setAutoResizeMode(tbl_produtos.AUTO_RESIZE_OFF);
        tbl_produtos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conecBanc.desconectar();
    }//  FINAL METODO PARA IMPRIMIR TABELA NA INTERFACE
        
    private void tbl_produtosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_produtosMouseClicked
                 float desconto=0;
                String nome_produto =""+ tbl_produtos.getValueAt(tbl_produtos.getSelectedRow(), 1);
                conecBanc.conexaoBanco();
                conecBanc.executaSQL("select * from produto where nome_produto='" +nome_produto+"'");
                try {
                        conecBanc.rs.first();
                        ct_nome.setText(conecBanc.rs.getString("nome_produto"));
                        ct_preco.setText((String.valueOf(numberFormat.format(conecBanc.rs.getFloat("preco_venda")))));
                        
                       float valorVenda= conecBanc.rs.getFloat("preco_venda");
                       float div = (float) 4/100;
                       
                       desconto = valorVenda-(div * valorVenda);
 
                        ct_desconto.setText(String.valueOf(numberFormat.format(desconto)));
                        conecBanc.desconectar();

                    } catch (SQLException ex) {
                       
                    }
    }//GEN-LAST:event_tbl_produtosMouseClicked

    private void ct_pesquisarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_pesquisarMouseEntered

    }//GEN-LAST:event_ct_pesquisarMouseEntered

    private void ct_pesquisarComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_ct_pesquisarComponentResized

    }//GEN-LAST:event_ct_pesquisarComponentResized

    private void ct_pesquisarComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_ct_pesquisarComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisarComponentShown

    private void ct_pesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_pesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_pesquisarActionPerformed

    private void ct_pesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ct_pesquisarKeyReleased
        modoNavegar = "Navegar";
        //        manipularInterface();
        preecherTabela("select * from produto where nome_produto like '%" + ct_pesquisar.getText() + "%' or codbarra like '%"+ct_pesquisar.getText()+"'");
    }//GEN-LAST:event_ct_pesquisarKeyReleased

    private void btn_sairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sairActionPerformed

                dispose();
    }//GEN-LAST:event_btn_sairActionPerformed

    private void ct_precoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ct_precoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ct_precoActionPerformed

    private void ct_pesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ct_pesquisarMouseClicked
       ct_pesquisar.selectAll();
       
    }//GEN-LAST:event_ct_pesquisarMouseClicked

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
            java.util.logging.Logger.getLogger(VsProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VsProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VsProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VsProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VsProdutos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_sair;
    private javax.swing.JTextField ct_desconto;
    private javax.swing.JTextField ct_nome;
    private javax.swing.JTextField ct_pesquisar;
    private javax.swing.JTextField ct_preco;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_produtos;
    // End of variables declaration//GEN-END:variables
}
