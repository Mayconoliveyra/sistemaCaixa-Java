/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloCeantribCeanImport;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleCeantribCeanimport {
    ConectaBanco conecBanc= new ConectaBanco();
    ConectaBanco conecPesquisa= new ConectaBanco();
    String codCenImportTblNota;
    String codCenImportTblProduto;
    
    
    //ADICIONA UM UMA LIGAÇAO CEAN_IMPORT COM CEAN_TRIB NOVO PRODUTO
    public void addNovaLigacaoCeanTribComCeanImport(ModeloCeantribCeanImport mod){
        conecBanc.conexaoBanco();
        try {
            PreparedStatement pst = conecBanc.conn.prepareStatement("insert into ceantrib_ceanimport(cod_cean_import_tblprod, cod_ceantrib_nota)values(?,?)");
            pst.setString(1, mod.getCodCeanTblProd());
            pst.setString(2, mod.getCodCeanTribTblNota());
            pst.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao adicionar na tabela ceantrib_ceanimport\nErro: "+ex);
        }
        conecBanc.desconectar();
    }
    
    //ADICIONA UM UMA LIGAÇAO CEAN_IMPORT COM CEAN_TRIB, PARA ITENS QUE EU SELECIONO
    public void addNovaLigacaoCeanTribComCeanImportSelecionado(ModeloCeantribCeanImport mod){
        procurarCodCeanImportTblNota(mod.getIdProdutoTblNota());
        procurarCodCeanImportTblProdPassandoCodbarras(mod.getCodBarrasProd());
        conecBanc.conexaoBanco();
        try {
            PreparedStatement pst = conecBanc.conn.prepareStatement("insert into ceantrib_ceanimport(cod_cean_import_tblprod, cod_ceantrib_nota)values(?,?)");
            pst.setString(1, codCenImportTblProduto);
            pst.setString(2, codCenImportTblNota);
            pst.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao adicionar na tabela ceantrib_ceanimport\nErro: "+ex);
        }
        conecBanc.desconectar();
    }
    
    //EXCLUI A LIGAÇÃO "CEAN_IMPORT COM CEAN_TRIB, ESSA LIGAÇÃO E FEITA PARA IMPORTA UM AUTOMATICO
    public void excluirLigacaoCeanTribComCeanImport(ModeloCeantribCeanImport mod){
        procurarCodCeanImportTblProdPassandoIdProd(mod.getIdProdutoTblProd());
        conecBanc.conexaoBanco();
        try {
            PreparedStatement pst = conecBanc.conn.prepareStatement("delete from ceantrib_ceanimport where cod_cean_import_tblprod=?");
            pst.setString(1, codCenImportTblProduto);
            pst.execute();
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao excluir ligação ceanimport com ceantrib\nErro: "+ex);
        }
        conecBanc.desconectar();
    }
    
    //METODO PARA PROCURAR O CODIGO CEAN_TRIB DO PRODUTO NA TABELA NOTA_INICIO, PASSANDO O 'ID_PRODUTO'
    public void procurarCodCeanImportTblNota(int idProdutoTblNota){
        conecPesquisa.conexaoBanco();
            try {
                conecPesquisa.executaSQL("select * from nota_produto where id_produto='"+idProdutoTblNota+"'");
                conecPesquisa.rs.first();
                codCenImportTblNota= conecPesquisa.rs.getString("cod_cean_trib");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Erro ao procurar cod_cean_trib na tabela nota_inicio\nErro: "+ex);
            }
        conecPesquisa.desconectar();   
    }
    
    //METODO PARA PROCURAR O CODIGO CEAN_IMPORT DO PRODUTO NA TABELA PRODUTO PASSANDO 'CODIGO DE BARRAS'.
    public void procurarCodCeanImportTblProdPassandoCodbarras(String codbarras){ 
        conecPesquisa.conexaoBanco();
            try {
                conecPesquisa.executaSQL("select * from produto where codbarra='"+codbarras+"'");
                conecPesquisa.rs.first();
                codCenImportTblProduto= conecPesquisa.rs.getString("cod_cean_import");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Erro ao procurar cod_cean_import tbl produto\nErro: "+ex);
            }
        conecPesquisa.desconectar();   
    }   

    //METODO PARA PROCURAR O CODIGO CEAN_IMPORT DO PRODUTO NA TABELA PRODUTO PASSANDO 'ID_PRODUTO'.
    public void procurarCodCeanImportTblProdPassandoIdProd(int idProdutoTblProd){ 
        conecPesquisa.conexaoBanco();
            try {
                conecPesquisa.executaSQL("select * from produto where id_produto='"+idProdutoTblProd+"'");
                conecPesquisa.rs.first();
                codCenImportTblProduto= conecPesquisa.rs.getString("cod_cean_import");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Erro ao procurar cod_cean_import tbl produto\nErro: "+ex);
            }
        conecPesquisa.desconectar();   
    }  
    
        
}