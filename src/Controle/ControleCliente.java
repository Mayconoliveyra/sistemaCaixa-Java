
package Controle;

import Modelo.ModeloCliente;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleCliente {
    ConectaBanco conecBanco = new ConectaBanco();
    ModeloCliente modCli = new ModeloCliente();
    int codBairro,codCidade,codTel;
    String Bairro, Cidade, Telefone;
    
     public void Gravar(ModeloCliente mod){ // METODO PARA SALVAR UM NOVO ARQUIVO NO BANCO
        buscaCodBairro(mod.getBairro());
        conecBanco.conexaoBanco();
           try {
        PreparedStatement pst = conecBanco.conn.prepareStatement("insert into clientes (nome_cliente, endereco_cliente, rg_cliente, cpf_cliente, id_bairro, apelido_cliente, telefone_1, telefone_2, referencia)values (?,?,?,?,?,?,?,?,?)");   
        pst.setString(1, mod.getNome());
        pst.setString(2, mod.getEndereco());
        pst.setString(3, mod.getRg());
        pst.setString(4, mod.getCpf());
        pst.setInt(5, codBairro);
        pst.setString(6, mod.getApelido());
        pst.setString(7, mod.getTelefone1());
        pst.setString(8, mod.getTelefone2());
        pst.setString(9, mod.getReferencia());
        pst.execute();
        
        JOptionPane.showMessageDialog(null,"Salvo com sucesso!");   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Criar Cliente!"+ex);
        }
         conecBanco.desconectar();
    }
     
     
     public void buscaCodBairro(String Bairro){
         conecBanco.conexaoBanco();
        try {
            
            conecBanco.executaSQL("select * from bairro where nome_bairro='"+ Bairro +"'");
            conecBanco.rs.first();
            codBairro = conecBanco.rs.getInt("id_bairro");
  
        } catch (SQLException ex) {
            
        }
        conecBanco.desconectar();
     }
     
  
  public void Editar(ModeloCliente mod){
         buscaCodBairro(mod.getBairro());
              conecBanco.conexaoBanco();
            try {
                PreparedStatement  pst = conecBanco.conn.prepareStatement("update clientes set nome_cliente=?, endereco_cliente=?, rg_cliente=?, cpf_cliente=?, id_bairro=?, apelido_cliente=?, telefone_1=?, telefone_2=?, referencia=? where id_cliente=?");
                pst.setString(1, mod.getNome());
                pst.setString(2, mod.getEndereco());
                pst.setString(3, mod.getRg());
                pst.setString(4, mod.getCpf());
                pst.setInt(5, codBairro);
                pst.setString(6, mod.getApelido());
                pst.setString(7, mod.getTelefone1());
                pst.setString(8, mod.getTelefone2());
                pst.setString(9, mod.getReferencia());
                pst.setInt(10, mod.getId());
                pst.execute();
                JOptionPane.showMessageDialog(null,"Alterado com sucesso!");
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao Alterar Cliente!"+ex);
            }
            conecBanco.desconectar(); 
    }
  
  public void Excluir(ModeloCliente modelo){
        conecBanco.conexaoBanco();
            try {
                //excluir dados da tabela cliente
               PreparedStatement  pst = conecBanco.conn.prepareStatement("delete from clientes where id_cliente=?");
                pst.setInt(1, modelo.getId());
                pst.execute();
                
                JOptionPane.showMessageDialog(null,"Cliente excluido!");
            } catch (SQLException ex) {
               JOptionPane.showMessageDialog(null,"Erro na Exclusão"+ex);
            }
        conecBanco.desconectar();
    }
    
}
