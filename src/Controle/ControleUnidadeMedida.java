/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloUnidadeMedida;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleUnidadeMedida {
    ConectaBanco conecBan = new ConectaBanco();
    
    public void criaLigacaoUnidadeMedida(ModeloUnidadeMedida mod){
        conecBan.conexaoBanco();
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("insert into unidade_medida_convert (unidade_medida, cod_ceanimport, quantidade, igual1_div2_mult3)values(?,?,?,?)");
            pst.setString(1, mod.getUnidMedidaNotaUM());
            pst.setString(2, mod.getCodCeanTribUM());
            pst.setInt(3, mod.getQuantidadeUM());
            pst.setInt(4, mod.getIgual1_div2_mult3UM());
            pst.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao criar ligacao unidade de medida!\nErro: "+ex);
        }   
        conecBan.desconectar();
    }
    
    public void unidadeMedidaIgual(){
        
    }
   
}
