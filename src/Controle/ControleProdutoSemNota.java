
package Controle;

import Modelo.ModeloProdutoSemNota;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleProdutoSemNota {
    ModeloProdutoSemNota mod = new ModeloProdutoSemNota();
    ConectaBanco conecBan= new ConectaBanco();
    ConectaBanco conecFornec = new ConectaBanco();
    int codFornecedor, codGrupoProd, codMarca;
    String nomeFornecedor;
    
    public void inserirProdutoSemNota(ModeloProdutoSemNota mod){
        buscarCodigoFor(mod.getFornecedorSN());
        buscarCodigoGrupo(mod.getGrupoSN());
        buscarCodigoMarca(mod.getMarcaSN());
        conecBan.conexaoBanco();
        try {
            
            PreparedStatement pst = conecBan.conn.prepareStatement("insert into produto (nome_produto, preco_compra, preco_venda, quantidade, codbarra, id_fornecedor, percentual, vlr_ganho,id_marca, id_grupo,unidade, qtd_minima_estoque, cod_cean_import)values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mod.getDescricaoSN());
            pst.setFloat(2, mod.getPrecoCompraSN());
            pst.setFloat(3, mod.getPrecoVendaSN());
            pst.setInt(4, mod.getQuantidadeCN());
            pst.setString(5, mod.getCodBarrasSN());
            pst.setInt(6, codFornecedor);
            pst.setFloat(7, mod.getPercentualItemSN());
            pst.setFloat(8, mod.getVlrGanhoItemSN());
            pst.setInt(9, codMarca);
            pst.setInt(10, codGrupoProd);
            pst.setString(11, mod.getUnidadeMedidaSN());
            pst.setInt(12, mod.getQtdMinimaEstoqSN());
            pst.setString(13, mod.getCodCeanImportSN());
            pst.execute();
            JOptionPane.showMessageDialog(null,"Salvo com sucesso!"); 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao inserir o produto!"+ex);
        }
        conecBan.desconectar(); 
    }
    
    public void editarProdutoSemNota(ModeloProdutoSemNota mod){
         buscarCodigoFor(mod.getFornecedorSN());
         buscarCodigoMarca(mod.getMarcaSN());
         buscarCodigoGrupo(mod.getGrupoSN());
        conecBan.conexaoBanco();
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("update produto set nome_produto=?, preco_compra=?, preco_venda=?, quantidade=?, codbarra=?, id_fornecedor=?, percentual=?, vlr_ganho=?,id_marca=?, id_grupo=?, unidade=?, qtd_minima_estoque=? where id_produto=?");
            pst.setString(1, mod.getDescricaoSN());
            pst.setFloat(2, mod.getPrecoCompraSN());
            pst.setFloat(3, mod.getPrecoVendaSN());
            pst.setInt(4, mod.getQuantidadeCN());
            pst.setString(5, mod.getCodBarrasSN());
            pst.setInt(6, codFornecedor);
            pst.setFloat(7, mod.getPercentualItemSN()); 
            pst.setFloat(8, mod.getVlrGanhoItemSN()); 
            pst.setInt(9, codMarca);
            pst.setInt(10, codGrupoProd);
            pst.setString(11, mod.getUnidadeMedidaSN());
            pst.setInt(12, mod.getQtdMinimaEstoqSN());
            pst.setInt(13, mod.getIdProdutoSN());
            pst.execute();
            JOptionPane.showMessageDialog(null,"Alterado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao alterar produto!"+ex);
        }
    }
    
    public void excluirProdutoSemNota(ModeloProdutoSemNota mod){
           conecBan.conexaoBanco();
        try {
            // EXCLUIR PRODUTO
            PreparedStatement pst = conecBan.conn.prepareStatement("delete from produto where id_produto=?");
            pst.setInt(1, mod.getIdProdutoSN());
            pst.execute();
            JOptionPane.showMessageDialog(null,"Excluido com sucesso!");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao  excluir produto!"+ex);
        }
            conecBan.desconectar(); 
    }
    
    
    public void buscarCodigoFor(String nomeForn ){
        conecBan.conexaoBanco();
        conecBan.executaSQL("select * from fornecedores where nome_fornecedor='"+nomeForn+"'");
        try {
         conecBan.rs.first();
            codFornecedor = conecBan.rs.getInt("id_fornecedor");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao Procurar Fornecedor!"+ex);
        }
        conecBan.desconectar();
    }
    
    public void buscarCodigoGrupo(String nomeGrupo ){
        conecBan.conexaoBanco();
        conecBan.executaSQL("select * from grupo_produto where nome_grupo='"+nomeGrupo+"'");
        try {
         conecBan.rs.first();
            codGrupoProd = conecBan.rs.getInt("id_grupo");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao Procurar Grupo!"+ex);
        }
        conecBan.desconectar();
    }
    
    public void buscarCodigoMarca(String nomeMarca ){
        conecBan.conexaoBanco();
        conecBan.executaSQL("select * from marca where nome_marca='"+nomeMarca+"'");
        try {
         conecBan.rs.first();
            codMarca = conecBan.rs.getInt("id_marca");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao Procurar Marca!"+ex);
        }
        conecBan.desconectar();
    }
    
    public void buscarNomeFornecedor(int cod){
        conecFornec.conexaoBanco();
        conecFornec.executaSQL("select * from fornecedores where id_fornecedor='"+cod+"'");
        try {
            conecFornec.rs.first();
            nomeFornecedor = conecFornec.rs.getString("nome_fornecedor");
            
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao Bunscar Codigo de Fornecedor!"+ ex);
        }
        conecFornec.desconectar();
    }
    

    
}
