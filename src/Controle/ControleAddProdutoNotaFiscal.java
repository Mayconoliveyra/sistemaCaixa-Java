/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloParseTblNotaParaTblProduto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleAddProdutoNotaFiscal {
    ConectaBanco conecBanco = new ConectaBanco();
    ConectaBanco conecPesquisa = new ConectaBanco();
    ModeloParseTblNotaParaTblProduto modEstoq = new ModeloParseTblNotaParaTblProduto();
    ControleParseTblNotaParaTblProduto controEstoq= new ControleParseTblNotaParaTblProduto();
    
    SimpleDateFormat forma = new SimpleDateFormat("dd/MM/yyyy");
    Date hoje = new Date();
    StringUtils StringUtil = new StringUtils();   
    
    String codCeanImportTblNotaVerifica; //CODIGO DE IMPORTAÇÃO DO PRODUTO NA TABELA NOTA_PRODUTO/ CEANTRIB_CEANIMPORT
    String codCeanImportProdutoTblProd; //CODIGO DE IMPORTAÇÃO DO PRODUTO NA TABELA PRODUTO/ CEANTRIB_CEANIMPORT
    String unidadeMedidaNotaProd; //UNIDADE DE MEDIDA DO PRODUTO NA NOTA PRODUTO
    int idUnidade_medida_convert; // ID DA CONVERSÃO NA TABELA UNIDADE MEDIDA CONVERT
    int idFornecedor; //ID DO FORNECEDOR
    public void addProdutoNovo(){
        conecBanco.conexaoBanco();
        conecPesquisa.conexaoBanco();
        
        try {
           conecBanco.executaSQL("select * from nota_inicio");
           conecBanco.rs.last();
           int idUltimaNotaProd= conecBanco.rs.getInt("id_nota");
           idFornecedor=conecBanco.rs.getInt("id_fornecedor");
           conecBanco.executaSQL("select * from nota_produto where id_nota_inicio='"+idUltimaNotaProd+"'");
           conecBanco.rs.first();
           
                do{
                    String codCeanTribTblNotaProd= conecBanco.rs.getString("cod_cean_trib"); //SALVA O CODIGO CEAN_TRIB DO PRODUTO DA TABELA NOTA_PRODUTO
                    int codProdTblNotaProd = conecBanco.rs.getInt("id_produto"); //ID DO PRODUTO NA TABELA NOTA_PRODUTO

                    //VERIFICA SE JÁ ESTA CADASTRADO PELO CODIGO DE IMPORTAÇÃO 'cod_cean_trib' NA TABELA 'ceanTrib_ceanImport'. Retorna= codCeanImportTblNotaVerifica
                    verificarSeOProdutoJaTemCadastro(codCeanTribTblNotaProd);

                    if(codCeanTribTblNotaProd.equals(codCeanImportTblNotaVerifica)){ //VERIFICA SE O PRODUTO JA TA CADASTRADO
                        unidadeMedidaNotaProd= conecBanco.rs.getString("unid_medida");
                        verificaSeUnidadeMedidaJaTaCadastrada(codCeanImportProdutoTblProd);
                            if(idUnidade_medida_convert==0){ // VERIFICA SE O PRODUTO JA FOI IMPORTADO COM ESSA UNIDADE DE MEDIDA (=0 NÃO FOI)
                               atualizaListaItensImportadoRecebe3(codProdTblNotaProd); //ADICIONA A TABELA DE CONVERSÃO
                            }else{
                               importaProdutoUnidadeMedidaJaCadastrada(codProdTblNotaProd, idUnidade_medida_convert, idFornecedor);
                               atualizaListaItensImportadoRecebe1(codProdTblNotaProd);
                            }
 
                    }else{
                        atualizaListaItensImportadoRecebe2(codProdTblNotaProd); //ADICIONA A TABELA 'NOVOS'
                    }
                    
                }while (conecBanco.rs.next());
    
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar um novo produto!\n Erro: "+ex);
        }
        conecBanco.desconectar();
        conecPesquisa.desconectar();
    }
    
    //VERIFICA SE O ITEM JA ESTA CADASTRO PELO CODIGO 'cod_cean_trib' NA TABELA 'ceantrib_ceanimport'
    public void verificarSeOProdutoJaTemCadastro(String codCeanTrib){ 
        codCeanImportTblNotaVerifica="limpa"; //limpa a string
        codCeanImportProdutoTblProd="limpa";  //limpa a string
        conecPesquisa.conexaoBanco();
        try {
            conecPesquisa.executaSQL("select * from ceantrib_ceanimport where cod_ceantrib_nota='"+codCeanTrib+"'");
            conecPesquisa.rs.first();
            codCeanImportTblNotaVerifica= conecPesquisa.rs.getString("cod_ceantrib_nota"); //SALVA O CODIGO DE IMPORTAÇÃO COD_CEANTRIB TBL ceantrib_ceanimport
            codCeanImportProdutoTblProd= conecPesquisa.rs.getString("cod_cean_import_tblprod");
        } catch (SQLException ex) {
        }
        conecPesquisa.desconectar();
    }
    
    public void verificaSeUnidadeMedidaJaTaCadastrada(String codceanimport){
        idUnidade_medida_convert=0; // 0 = NÃO LIGAÇÃO COM A MESMA UNIDADE DE MEDIDA
        conecPesquisa.conexaoBanco();
        try {
            conecPesquisa.executaSQL("select * from unidade_medida_convert where cod_ceanimport='"+codceanimport+"'");
            conecPesquisa.rs.first();
            do{
            String unidMedidaV= conecPesquisa.rs.getString("unidade_medida");
            
                if(unidMedidaV.equals(unidadeMedidaNotaProd)){
                idUnidade_medida_convert=conecPesquisa.rs.getInt("id_unidade_medida");
                }     
            
            }while(conecPesquisa.rs.next());
            
        } catch (SQLException ex) {
        }
        
        conecPesquisa.desconectar();
    }
    
    
    public void atualizaListaItensImportadoRecebe1(int idProduto){ //FINALIZA IMPORTAÇÃO DO PRODUTO
        conecBanco.conexaoBanco();
        try {
            PreparedStatement pst = conecBanco.conn.prepareStatement("update nota_produto set  importado1_novo2_converte3=?  where id_produto=?");
            pst.setInt(1, 1);
            pst.setInt(2,idProduto);
            pst.execute();
        } catch (SQLException ex) {
         JOptionPane.showMessageDialog(null, "Erro ao atualizar produto para status 1= Importado\n Erro: "+ex);
        }
        conecBanco.desconectar();
        
    }
    
    public void atualizaListaItensImportadoRecebe2(int idProduto){ //ADICIONA O PRODUTO A LISTA DE NOVOS
        conecBanco.conexaoBanco();
        try {
            PreparedStatement pst = conecBanco.conn.prepareStatement("update nota_produto set  importado1_novo2_converte3=?  where id_produto=?");
            pst.setInt(1, 2);
            pst.setInt(2,idProduto);
            pst.execute();
        } catch (SQLException ex) {
         JOptionPane.showMessageDialog(null, "Erro ao atualizar produto para status 2= novo\n Erro: "+ex);
        }
        conecBanco.desconectar();
    }
    
    public void atualizaListaItensImportadoRecebe3(int idProduto){ //ADICIONA O PRODUTO A LISTA DE CONVERSÃO
        conecBanco.conexaoBanco();
        try {
            PreparedStatement pst = conecBanco.conn.prepareStatement("update nota_produto set  importado1_novo2_converte3=?  where id_produto=?");
            pst.setInt(1, 3);
            pst.setInt(2,idProduto);
            pst.execute();
        } catch (SQLException ex) {
         JOptionPane.showMessageDialog(null, "Erro ao atualizar produto para status 3= converte\n Erro: "+ex);
        }
        conecBanco.desconectar(); 
    }
    
    public void atualizaListaItensImportadoRecebe5(int idProduto){ //ADICIONA O PRODUTO A LISTA DE PESQUISA
        conecBanco.conexaoBanco();
        try {
            PreparedStatement pst = conecBanco.conn.prepareStatement("update nota_produto set  importado1_novo2_converte3=?  where id_produto=?");
            pst.setInt(1, 5);
            pst.setInt(2,idProduto);
            pst.execute();
        } catch (SQLException ex) {
         JOptionPane.showMessageDialog(null, "Erro ao atualizar produto para status 5= semcodigo\n Erro: "+ex);
        }
        conecBanco.desconectar();   
    }
    
    public void importaProdutoUnidadeMedidaJaCadastrada(int idProdutoTblNota, int idUnidadeMedidaConvert, int idFornecedorMe){
        int quantidadeReturn=0;
        float precoCustoReturn=0;
        String codCeanImportTblUnidadeMedida="";
        
        conecPesquisa.conexaoBanco();
        try {
            conecPesquisa.executaSQL("select * from nota_produto where id_produto='"+idProdutoTblNota+"'");
            conecPesquisa.rs.first();
            float precoCustoTblNota= conecPesquisa.rs.getFloat("vlr_unit_prod"); //PREÇO CUSTO
            int qtdRecebidaTblNota= conecPesquisa.rs.getInt("qtd_recebida");     //QUANTIDADE 
            
            conecPesquisa.executaSQL("select * from unidade_medida_convert where id_unidade_medida='"+idUnidadeMedidaConvert+"'");
            conecPesquisa.rs.first();
            int quantidadeTblUnidadeMedida = conecPesquisa.rs.getInt("quantidade");
            int igual1_div2_mult3TblUnidadeMedida= conecPesquisa.rs.getInt("igual1_div2_mult3");
            codCeanImportTblUnidadeMedida= conecPesquisa.rs.getString("cod_ceanimport"); //PRODUTO QUE VAI RECEBER TEM ESSE CODIGO DE CEAN IMPORT
            
            if(igual1_div2_mult3TblUnidadeMedida==1){
               quantidadeReturn=qtdRecebidaTblNota;
               precoCustoReturn=precoCustoTblNota;
            }
            
            if(igual1_div2_mult3TblUnidadeMedida==2){
               quantidadeReturn=(qtdRecebidaTblNota/quantidadeTblUnidadeMedida);
               precoCustoReturn=(precoCustoTblNota*quantidadeTblUnidadeMedida);
               
            }
            
            if(igual1_div2_mult3TblUnidadeMedida==3){
               quantidadeReturn=(qtdRecebidaTblNota*quantidadeTblUnidadeMedida);
               precoCustoReturn=(precoCustoTblNota/quantidadeTblUnidadeMedida);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao importa produto metodo 'importaProdutoUnidadeMedidaJaCadastrada)"+ ex);
        }
        conecPesquisa.desconectar();
        atualizaProdutoRecebendoQuantidadeEPreco(codCeanImportTblUnidadeMedida, quantidadeReturn, precoCustoReturn, idFornecedorMe);
    }
    
    public void atualizaProdutoRecebendoQuantidadeEPreco(String codCeanimport, int quantidadeAdd, float precoCustoNovo, int IdFornecedor){
        conecPesquisa.conexaoBanco();
        try {
        conecPesquisa.executaSQL("select * from produto where cod_cean_import='"+codCeanimport+"'");
        conecPesquisa.rs.first();
        int quantidadeAtualProd= conecPesquisa.rs.getInt("quantidade");
        float Percentual= conecPesquisa.rs.getFloat("percentual");
        conecPesquisa.desconectar();
        
        int ResultadoDasomaQuantidade= (quantidadeAtualProd + quantidadeAdd); //SOMA QUANTIDADE ATUAL + NOVA QUANTIDADE A SER ADICIONADA
        Float novoPrecoVenda= (precoCustoNovo * Percentual) + precoCustoNovo; //NOVO PREÇO DE VENDA
        Float novoVlrGanho=(precoCustoNovo * Percentual); //VALOR GANHO 
        
        conecBanco.conexaoBanco();
            PreparedStatement pst = conecBanco.conn.prepareStatement("update produto set preco_compra=?, preco_venda=?, vlr_ganho=?, quantidade=?, id_fornecedor=? where cod_cean_import=?");
            pst.setFloat(1, precoCustoNovo);
            pst.setFloat(2, novoPrecoVenda);
            pst.setFloat(3, novoVlrGanho);
            pst.setInt(4, ResultadoDasomaQuantidade);
            pst.setInt(5, IdFornecedor);
            pst.setString(6, codCeanimport);
            pst.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao atualizar prododuto metodo (atualizaProdutoRecebendoQuantidadeEPreco)"+ ex);
        }
        conecBanco.desconectar();
    }
    
    
}
