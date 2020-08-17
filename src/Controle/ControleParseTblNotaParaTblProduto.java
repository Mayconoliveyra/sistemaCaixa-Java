/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloParseTblNotaParaTblProduto;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleParseTblNotaParaTblProduto {
    ConectaBanco conecBanc = new ConectaBanco();
    ConectaBanco conecEstoq = new ConectaBanco();
      
//    ModeloParseTblNotaParaTblProduto modEstoq = new ModeloParseTblNotaParaTblProduto();
    
    public void produtoJaCadastradoTudoOk(ModeloParseTblNotaParaTblProduto mod){
         conecBanc.conexaoBanco();
         conecEstoq.conexaoBanco();
        try {
            PreparedStatement pst = conecBanc.conn.prepareStatement("update produto set quantidade=? where id_produto=?");
            pst.setInt(1,mod.getTotalRestado());
            pst.setInt(2, mod.getIdProduto());
            pst.execute();
            
            pst = conecEstoq.conn.prepareStatement("update nota_produto set  importado1_novo2_converte3=?  where id_produto=?");
            pst.setInt(1, mod.getStatusOpc());
            pst.setInt(2, mod.getIdProdutoNota_produto());
            pst.execute();
        } catch (SQLException ex) {
        }
        conecBanc.desconectar();
        conecEstoq.desconectar();
    }

    
}