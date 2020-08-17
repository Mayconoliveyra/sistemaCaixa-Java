package Controle;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ConectaBanco {
    
    public Statement stm; // RESNPOSÁVEL POR PREPARAR E REALIZAR PESQUISAS NO BANCO DE DADOS
    public ResultSet rs; // RESPOSAVÁL POR ARMAZENA O RESULTADO DE UMA PESQUISA PASSADA PARA STATEMENT 
    private String driver = "org.postgresql.Driver"; //RESPONSÁVEL POR INDENTIFICAR O SERVIÇO DE BANDO DE DADOS;
    private String caminho = "jdbc:postgresql://localhost:5432/sistemadeposito"; //RESPONSÁVEL POR SETAR O LOCAL DO BANCO DE DADOS
    private String usuario = "postgres";
    private String senha = "m8767"; 
    public Connection conn; // RESPONSÁVEL POR REALIZAR A CONEXÃO COM O BANDO DE DADOS
    
    public void conexaoBanco(){ // MÉTODO RESPOSAVEL POR REALIZAR A CONEXÃO COM O BANCO
       
        try { // TENTATIVA INICIAL "PARECIDO COM O IF" 
            System.setProperty("jdbc.Drivers", driver); // SETA A PROPRIEDADE DO DRIVE DE CONEXÃO
            conn = DriverManager.getConnection(caminho, usuario, senha); // REALIZA A CONEXAO COM O BANCO DE DADOS
            //JOptionPane.showMessageDialog(null, "Conectado com sucesso!"); // IMPRIMI CAIXA DE MESAGEM "SUCESSO"
        } catch (SQLException ex) { //SEGUNDA TENTATIVA
            JOptionPane.showMessageDialog(null, "Erro de conexão! \n Erro"+ ex.getMessage()); // IMPRIME CAIXA DE MESSAGEM "ERRO"

        }
    }
    
    public void executaSQL(String SQL){ // METODO PARA MANIPULAR INTERFACE
        try {
            stm =conn.createStatement(rs.TYPE_SCROLL_INSENSITIVE, rs.CONCUR_READ_ONLY);
            rs = stm.executeQuery(SQL);
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Erro no executaSql! \n Erro"+ ex.getMessage()); // IMPRIME CAIXA DE MESSAGEM "ERRO"
        }
    }
    
    public void desconectar(){ //   MÉTODO PARA FECHAR A CONEXÃO COM O BANCO DE DADOS
        try {
            conn.close(); // FECHA A CONEXÃO
         //   JOptionPane.showMessageDialog(null, "Desconectado com sucesso!"); // IMPRIMI CAIXA DE MESAGEM "SUCESSO"
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão! \n Erro"+ ex.getMessage()); // IMPRIME CAIXA DE MESSAGEM "ERRO"
        }
    }
    
    
    
    
}
