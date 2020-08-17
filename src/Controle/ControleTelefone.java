
package Controle;

import Modelo.ModeloTelefone;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleTelefone {
    ConectaBanco conecBanco = new ConectaBanco();
    ModeloTelefone modelo = new ModeloTelefone();
    
    
  public void Gravar(ModeloTelefone modelo){ // METODO PARA SALVAR UM NOVO ARQUIVO NO BANCO
        conecBanco.conexaoBanco();
        
           try {
        PreparedStatement pst = conecBanco.conn.prepareStatement("insert into telefone (numero_tel, numero_whats)values (?,?)");   
        pst.setString(1, modelo.getTel());
        pst.setString(2, modelo.getWhats());
        pst.execute();
               JOptionPane.showMessageDialog(null,"Dados inseridos com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao inserir os dados!"+ex);
        }
         conecBanco.desconectar();
    }
  
  public void Editar(ModeloTelefone modelo){
              conecBanco.conexaoBanco();
             
            try {
                PreparedStatement pst = conecBanco.conn.prepareStatement("update telefone set numero_tel = ?, numero_whats=?  where id_telefone=?");
                pst.setString(1, modelo.getTel());
                pst.setString(2, modelo.getWhats());
                pst.setInt(3, modelo.getCod());
                pst.execute();
                JOptionPane.showMessageDialog(null,"Dados Alterados Com Sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERRO NA EDIÇÃO"+ex);
            }
            conecBanco.desconectar();
           
    }
  
  public void Excluir(ModeloTelefone modelo){
        conecBanco.conexaoBanco();
            try {
                PreparedStatement pst = conecBanco.conn.prepareStatement("delete from telefone where id_telefone=?");
                pst.setInt(1, modelo.getCod());
                pst.execute();
                JOptionPane.showMessageDialog(null,"Dados excluidos com Sucesso");
            } catch (SQLException ex) {
               JOptionPane.showMessageDialog(null,"Erro na exclusão"+ex);
            }
           conecBanco.desconectar();
    }
    
  
    public ModeloTelefone Primeiro(){
        conecBanco.conexaoBanco();
        conecBanco.executaSQL("select * from telefone");
        try {
            conecBanco.rs.first();
            modelo.setCod(conecBanco.rs.getInt("id_telefone"));
            modelo.setTel(conecBanco.rs.getString("numero_tel"));

        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao mostrar dados!"+ex);
        }
           conecBanco.desconectar();
        return modelo;
    }
    
     public ModeloTelefone Ultimo(){
        conecBanco.conexaoBanco();
        conecBanco.executaSQL("select * from telefone");
        try {
            conecBanco.rs.last();
            modelo.setCod(conecBanco.rs.getInt("id_telefone"));
            modelo.setTel(conecBanco.rs.getString("numero_tel"));

        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao mostrar dados!"+ex);
        }
           conecBanco.desconectar();
        return modelo;
    }
    
    public ModeloTelefone Anterior(){
        conecBanco.conexaoBanco();
       // connBanco.executaSql("select * from telefone");
        try {
            conecBanco.rs.previous();
            modelo.setCod(conecBanco.rs.getInt("id_telefone"));
            modelo.setTel(conecBanco.rs.getString("numero_tel"));

        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao mostrar dados!"+ex);
        }
           conecBanco.desconectar();
        return modelo;
    }
    
    public ModeloTelefone Proximo(){
        conecBanco.conexaoBanco();
       // connBanco.executaSql("select * from telefone");
        try {
            conecBanco.rs.next();
            modelo.setCod(conecBanco.rs.getInt("id_telefone"));
            modelo.setTel(conecBanco.rs.getString("numero_tel"));

        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao mostrar dados!"+ex);
        }
           conecBanco.desconectar();
        return modelo;
    }
  
    
}



