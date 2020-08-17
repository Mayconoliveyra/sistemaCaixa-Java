package Controle;

import Modelo.ModeloBairro;
import com.lowagie.text.Font;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;


/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleBairro {
    ConectaBanco connBairro = new ConectaBanco();
    ConectaBanco connPesqui = new ConectaBanco();
    int codCid;
    String cidade;
    

    
    public void Gravar(ModeloBairro modelo){ // METODO PARA SALVAR UM NOVO ARQUIVO NO BANCO
        connBairro.conexaoBanco();
         try {
             connBairro.executaSQL("select * from cidade where nome_cidades='"+modelo.getCidade()+ "'");
             connBairro.rs.first();
             codCid = connBairro.rs.getInt("id_cidade");
             PreparedStatement pst = connBairro.conn.prepareStatement("insert into bairro (nome_bairro, id_cidade)values (?,?)");
             pst.setString(1, modelo.getNome());
             pst.setInt(2, codCid);
             pst.execute();
                JOptionPane.showMessageDialog(null,"Salvo com sucesso!");
         } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Erro ao inseridos dados!"+ex);
         }
        connBairro.desconectar();
    }
    
    
    public void Editar(ModeloBairro modelo){
              connBairro.conexaoBanco();
              connPesqui.conexaoBanco();
            try {
                connPesqui.executaSQL("select * from cidade where nome_cidades='" +modelo.getCidade()+"'");
                connPesqui.rs.first();
                codCid = connPesqui.rs.getInt("id_cidade");
                PreparedStatement pst = connBairro.conn.prepareStatement("update bairro set nome_bairro=?, id_cidade=? where id_bairro=?");
                pst.setString(1, modelo.getNome());
                pst.setInt(2,codCid);
                pst.setInt(3, modelo.getCod());
                pst.execute();
                JOptionPane.showMessageDialog(null,"Alterado com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERRO NA EDIÇÃO"+ex);
                
            }
            connBairro.desconectar();
            connPesqui.desconectar();
    }
    
    
    public void Excluir(ModeloBairro modelo){
        connBairro.conexaoBanco();
            try {
                PreparedStatement pst = connBairro.conn.prepareStatement("delete from bairro where id_bairro=?");
                pst.setInt(1, modelo.getCod());
                pst.execute();
                JOptionPane.showMessageDialog(null,"Excluido com sucesso");
            } catch (SQLException ex) {
               JOptionPane.showMessageDialog(null,"Erro na exclusão"+ex);
            }
            connBairro.desconectar();
    }
    
  
    
  }
