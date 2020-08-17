/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloProdutoComNota;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleProdutoComNota {
    ConectaBanco conecBan = new ConectaBanco();
    int codFornecedor, codGrupoProd, codMarca;
    String nomeFornecedor;
    
    public void inserirProdutoComNota(ModeloProdutoComNota mod){
        buscarCodigoFor(mod.getFornecedorCN());
        buscarCodigoGrupo(mod.getGrupoProdutoCN());
        buscarCodigoMarca(mod.getMarcaProdutoCN());
        conecBan.conexaoBanco();
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("insert into produto (nome_produto, quantidade, codbarra, id_fornecedor, percentual, id_marca, id_grupo, unidade, qtd_minima_estoque, cod_cean_import)values(?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, mod.getDescricaoCN());
            pst.setInt(2, mod.getQtdProdutoCN());
            pst.setString(3, mod.getCodBarraCN());
            pst.setInt(4, codFornecedor);
            pst.setFloat(5, mod.getPercentualItemCN());
            pst.setInt(6, codMarca );
            pst.setInt(7, codGrupoProd);
            pst.setString(8, mod.getUnidadeCN());
            pst.setInt(9, mod.getQtdMinimaEstoqCN());
            pst.setString(10, mod.getCodCeanImportCN());
            pst.execute();
            JOptionPane.showMessageDialog(null,"Salvo com sucesso!"); 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao inserir o produto!"+ex);
        }
        conecBan.desconectar(); 
    }
    
    public void buscarCodigoFor(String nomeForn){
        conecBan.conexaoBanco();
        conecBan.executaSQL("select * from fornecedores where nome_fornecedor='"+nomeForn+"'");
        try {
         conecBan.rs.first();
            codFornecedor = conecBan.rs.getInt("id_fornecedor");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao procurar fornecedor!"+ex);
        }
        conecBan.desconectar();
    }
    
    public void buscarCodigoGrupo(String nomeGrupo){
        conecBan.conexaoBanco();
        conecBan.executaSQL("select * from grupo_produto where nome_grupo='"+nomeGrupo+"'");
        try {
        conecBan.rs.first();
            codGrupoProd = conecBan.rs.getInt("id_grupo");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao procurar grupo!"+ex);
        }
        conecBan.desconectar();
    }
    
    public void buscarCodigoMarca(String nomeMarca){
        conecBan.conexaoBanco();
        conecBan.executaSQL("select * from marca where nome_marca='"+nomeMarca+"'");
        try {
         conecBan.rs.first();
            codMarca = conecBan.rs.getInt("id_marca");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao procurar marca!"+ex);
        }
        conecBan.desconectar();
    }
  
 
}
