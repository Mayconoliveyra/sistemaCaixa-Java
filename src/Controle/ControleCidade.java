package Controle;

import Modelo.ModeloCidade;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleCidade {
        ConectaBanco conneCidade = new ConectaBanco(); // ESTANCIA UMA CONEXÃO COM O BANCO
        
    public void InserirCidade(ModeloCidade modelo){ // METODO PARA INSERRIR CIDADE
        conneCidade.conexaoBanco(); //CONECTA AO BANCO

            try {
             PreparedStatement pst = conneCidade.conn.prepareStatement("insert into cidade(nome_cidades, id_estado)values(?,?)");
             pst.setString(1,modelo.getNome()); // 1 = NOME_CIDADES       
             pst.setInt(2,modelo.getCod_estado());  // 2 = ID_ESTADO  // MODELO = VEM DA FrmCidade
             pst.execute(); // EXECUTA AS FUNÇOES 
             JOptionPane.showMessageDialog(null,"Salvo com sucesso!");
             
            } catch (SQLException ex) {
                  JOptionPane.showMessageDialog(null,"Erro na inserção de cidades"+ ex);
            }
        conneCidade.desconectar();
    } 
    
    public void excluirCidade(ModeloCidade modelo){
        conneCidade.conexaoBanco();
            try {
                PreparedStatement pst = conneCidade.conn.prepareStatement("delete from cidade where id_cidade=?");
                pst.setInt(1, modelo.getCod());
                pst.execute();
                JOptionPane.showMessageDialog(null,"Excluido com sucesso");
            } catch (SQLException ex) {
               JOptionPane.showMessageDialog(null,"Erro na exclusão"+ex);
            }
        conneCidade.desconectar();
    }
    
    public void AlterarCidade(ModeloCidade modelo){
            conneCidade.conexaoBanco();
            try {
                PreparedStatement pst = conneCidade.conn.prepareStatement("update cidade set nome_cidades = ?, id_estado = ? where id_cidade =?");
                pst.setString(1, modelo.getNome());
                pst.setInt(2, modelo.getCod_estado());
                pst.setInt(3, modelo.getCod());
                pst.execute();
                JOptionPane.showMessageDialog(null,"Alterado com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERRO NA EDIÇÃO"+ex);
                
            }
            conneCidade.desconectar();
    }
    
   
}
