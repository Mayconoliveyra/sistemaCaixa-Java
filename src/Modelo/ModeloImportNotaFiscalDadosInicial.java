
package Modelo;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ModeloImportNotaFiscalDadosInicial {
    private String chaveDeAcesso;
    private String numero;
    private String nomeEmitente;
    private String cnpjEmitente;
    private float valorTotalDaNota;
    private String dataEmissao;
    private String dataImport;

    private int id_fornecedor;
    
    /**
     * @return the chaveDeAcesso
     */
    public String getChaveDeAcesso() {
        return chaveDeAcesso;
    }

    /**
     * @param chaveDeAcesso the chaveDeAcesso to set
     */
    public void setChaveDeAcesso(String chaveDeAcesso) {
        this.chaveDeAcesso = chaveDeAcesso;
    }

    /**
     * @return the nomeEmitente
     */
    public String getNomeEmitente() {
        return nomeEmitente;
    }

    /**
     * @param nomeEmitente the nomeEmitente to set
     */
    public void setNomeEmitente(String nomeEmitente) {
        this.nomeEmitente = nomeEmitente;
    }


    /**
     * @return the valorTotalDaNota
     */
    public float getValorTotalDaNota() {
        return valorTotalDaNota;
    }

    /**
     * @param valorTotalDaNota the valorTotalDaNota to set
     */
    public void setValorTotalDaNota(float valorTotalDaNota) {
        this.valorTotalDaNota = valorTotalDaNota;
    }

    /**
     * @return the dataEmissao
     */
    public String getDataEmissao() {
        return dataEmissao;
    }

    /**
     * @param dataEmissao the dataEmissao to set
     */
    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

  
 
    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the cnpjEmitente
     */
    public String getCnpjEmitente() {
        return cnpjEmitente;
    }

    /**
     * @param cnpjEmitente the cnpjEmitente to set
     */
    public void setCnpjEmitente(String cnpjEmitente) {
        this.cnpjEmitente = cnpjEmitente;
    }

    /**
     * @return the id_fornecedor
     */
    public int getId_fornecedor() {
        return id_fornecedor;
    }

    /**
     * @param id_fornecedor the id_fornecedor to set
     */
    public void setId_fornecedor(int id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }

    /**
     * @return the dataImport
     */
    public String getDataImport() {
        return dataImport;
    }

    /**
     * @param dataImport the dataImport to set
     */
    public void setDataImport(String dataImport) {
        this.dataImport = dataImport;
    }
    
  
}
