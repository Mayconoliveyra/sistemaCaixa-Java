
package Modelo;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ModeloVenda {
    private String data;
    private int idVenda;
    private String nomeProduto;
    private String nomeCliente;
    private int qtdItem;
    private float valorVenda;
    private float troco;
    private String dinheiroRecebido;
    private float subTotalVenda;

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the idVenda
     */
    public int getIdVenda() {
        return idVenda;
    }

    /**
     * @param idVenda the idVenda to set
     */
    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    /**
     * @return the nomeProduto
     */
    public String getNomeProduto() {
        return nomeProduto;
    }

    /**
     * @param nomeProduto the nomeProduto to set
     */
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    /**
     * @return the nomeCliente
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     * @param nomeCliente the nomeCliente to set
     */
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    /**
     * @return the qtdItem
     */
    public int getQtdItem() {
        return qtdItem;
    }

    /**
     * @param qtdItem the qtdItem to set
     */
    public void setQtdItem(int qtdItem) {
        this.qtdItem = qtdItem;
    }

    /**
     * @return the valorVenda
     */
    public float getValorVenda() {
        return valorVenda;
    }

    /**
     * @param valorVenda the valorVenda to set
     */
    public void setValorVenda(float valorVenda) {
        this.valorVenda = valorVenda;
    }

    /**
     * @return the troco
     */

    public String getDinheiroRecebido() {
        return dinheiroRecebido;
    }

    /**
     * @param dinheiroRecebido the dinheiroRecebido to set
     */
    public void setDinheiroRecebido(String dinheiroRecebido) {
        this.dinheiroRecebido = dinheiroRecebido;
    }

    /**
     * @return the troco
     */
    public float getTroco() {
        return troco;
    }

    /**
     * @param troco the troco to set
     */
    public void setTroco(float troco) {
        this.troco = troco;
    }

    /**
     * @return the subTotalVenda
     */
    public float getSubTotalVenda() {
        return subTotalVenda;
    }

    /**
     * @param subTotalVenda the subTotalVenda to set
     */
    public void setSubTotalVenda(float subTotalVenda) {
        this.subTotalVenda = subTotalVenda;
    }

    
    
}
