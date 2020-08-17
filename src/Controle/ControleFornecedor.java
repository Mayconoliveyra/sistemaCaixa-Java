
package Controle;

import Modelo.ModeloFornecedor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleFornecedor {
    ConectaBanco conecBan = new ConectaBanco();
    ModeloFornecedor mod = new ModeloFornecedor();
    int codBairro;
    
    public void gravar(ModeloFornecedor mod){
        achaBairro(mod.getBairro());
        conecBan.conexaoBanco();
        
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("insert into fornecedores (nome_fornecedor, endereco, id_bairro, cnpj_fornecedor, telefone_1, telefone_2, nome_fantasia)values(?,?,?,?,?,?,?)");
            pst.setString(1, mod.getNome());
            pst.setString(2, mod.getEndereco());
            pst.setInt(3, codBairro);
            pst.setString(4, mod.getCNPJ());
            pst.setString(5, mod.getTelefone1());
            pst.setString(6, mod.getTelefone2());
            pst.setString(7, mod.getNomeFatasia());
            pst.execute();
             JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Inserção do fornecedor"+ex);
        }
        conecBan.desconectar();
    }
    
    public void Excluir(ModeloFornecedor mod){
         conecBan.conexaoBanco();
        try {
            PreparedStatement pst= conecBan.conn.prepareStatement("delete from fornecedores where id_fornecedor=?");
            pst.setInt(1, mod.getId());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Excluido com sucesso!"); 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Exclusão!!"+ex);
        }
         conecBan.desconectar();
         
     }
    
    public void Alterar(ModeloFornecedor mod){
        achaBairro(mod.getBairro());
        conecBan.conexaoBanco();
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("update fornecedores set nome_fornecedor=?, endereco=?, id_bairro=?, cnpj_fornecedor=? , telefone_1=?, telefone_2=?, nome_fantasia=? where id_fornecedor=?");
            pst.setString(1, mod.getNome());
            pst.setString(2, mod.getEndereco());
            pst.setInt(3, codBairro);
            pst.setString(4, mod.getCNPJ());
            pst.setString(5, mod.getTelefone1());
            pst.setString(6, mod.getTelefone2());
            pst.setString(7, mod.getNomeFatasia());
            pst.setInt(8, mod.getId());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Alterado com sucesso!");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Alteração de Fornecedor"+ex);
        }
        conecBan.desconectar();
        
    }
    
    public void achaBairro(String bairro){
        conecBan.conexaoBanco();
        try {
            conecBan.executaSQL("select * from bairro where nome_bairro='"+bairro+"'");
            conecBan.rs.first();
            codBairro = conecBan.rs.getInt("id_bairro");
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Erro ao buscar codigo do bairro"+ex);
        }
       conecBan.desconectar();
    }
    
}
