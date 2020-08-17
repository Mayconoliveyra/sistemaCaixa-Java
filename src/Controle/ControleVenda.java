
package Controle;

import Modelo.ModeloVenda;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleVenda {
    int codProduto, codCliente;

    ConectaBanco conecBanco = new ConectaBanco();
    
    public void adicionaItem(ModeloVenda mod){
        acharIdProduto(mod.getNomeProduto());
        conecBanco.conexaoBanco();
        try {
            PreparedStatement pst = conecBanco.conn.prepareStatement("insert into itens_venda_produto(id_venda, id_produto, quantidade_produto)values(?,?,?)");
            pst.setInt(1, mod.getIdVenda());
            pst.setInt(2, codProduto);
            pst.setInt(3, mod.getQtdItem());
            pst.execute();
          //BAIXA DE ESTOQUE
             int quant=0, resul =0;
            conecBanco.executaSQL("select * from produto where nome_produto='"+mod.getNomeProduto()+"'");
            conecBanco.rs.first();
            quant = conecBanco.rs.getInt("quantidade");
            resul = quant - mod.getQtdItem();
            pst = conecBanco.conn.prepareStatement("update produto set quantidade=? where nome_produto=?");
            pst.setInt(1, resul);
            pst.setString(2, mod.getNomeProduto());
            pst.execute();
//            JOptionPane.showMessageDialog(null,"Produto Adicionado");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Realizar a venda"+ ex);
        }
        conecBanco.desconectar();      
    }
    
    public void acharIdProduto(String nome){
        conecBanco.conexaoBanco();
        conecBanco.executaSQL("select * from produto where nome_produto='"+nome+"'");
        try {
            conecBanco.rs.first();
            codProduto = conecBanco.rs.getInt("id_produto");
            conecBanco.desconectar();
        } catch (SQLException ex) {
            conecBanco.desconectar();
            JOptionPane.showMessageDialog(null,"Erro ao AchaR ID do Produto"+ ex);
        }
        
        conecBanco.desconectar();
    }
    
    public void fecharVenda(ModeloVenda mod){
        acharCliente(mod.getNomeCliente());
        conecBanco.conexaoBanco();
        
        try {
            PreparedStatement pst = conecBanco.conn.prepareStatement("update venda set data_venda=?, valor_venda=?, dinheiro_recebido=?, troco=?, sub_total_venda=?, id_cliente=? where id_venda=?");
            pst.setString(1, mod.getData());
            pst.setFloat(2, mod.getValorVenda());
            pst.setString(3, mod.getDinheiroRecebido());
            pst.setFloat(4, mod.getTroco());
            pst.setFloat(5, mod.getSubTotalVenda());
            pst.setInt(6, codCliente);
            pst.setInt(7, mod.getIdVenda());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Venda Finalizada");
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null,"Erro ao Fechar Venda!! "+ ex);
        }
        conecBanco.desconectar(); 
    }
    
    public void acharCliente(String nome){
        conecBanco.conexaoBanco();
        
        try {
            conecBanco.executaSQL("select * from clientes where nome_cliente='"+nome+"'");
            conecBanco.rs.first();
            codCliente= conecBanco.rs.getInt("id_cliente");
        } catch (SQLException ex) {
         JOptionPane.showMessageDialog(null,"Erro ao Achar Nome do Cliente"+ ex);
        }
        conecBanco.desconectar(); 
    }
    
    public void cancelarVenda(){
         conecBanco.conexaoBanco();
         PreparedStatement pst;
        conecBanco.executaSQL("select * from venda inner join itens_venda_produto on venda.id_venda = itens_venda_produto.id_venda "
                + "inner Join produto on itens_venda_produto.id_produto = produto.id_produto where valor_venda=0");
        try {
            conecBanco.rs.first();
            do{
              int qtdEstoque = conecBanco.rs.getInt("quantidade");
              int qtdVendida = conecBanco.rs.getInt("quantidade_produto");
              int soma = qtdEstoque + qtdVendida;
              
              pst = conecBanco.conn.prepareStatement("update produto set quantidade=? where id_produto =?");
              pst.setInt(1, soma);
              pst.setInt(2, conecBanco.rs.getInt("id_produto"));
              pst.execute();
             pst = conecBanco.conn.prepareStatement("delete from itens_venda_produto where id_venda=?");
             pst.setInt(1, conecBanco.rs.getInt("id_venda"));
             pst.execute();
            }while(conecBanco.rs.next());
           pst = conecBanco.conn.prepareStatement("delete from venda where valor_venda=?");   
           pst.setInt(1, 0);
           pst.execute();
        } catch (SQLException ex) {
//         JOptionPane.showMessageDialog(null,"Erro ao Cancelar Venda"+ ex);
        }
      conecBanco.desconectar();
    }
    
}
