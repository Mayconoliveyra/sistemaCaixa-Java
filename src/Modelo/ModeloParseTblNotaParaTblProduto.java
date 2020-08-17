/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ModeloParseTblNotaParaTblProduto {
    private String dataAlteracao;
    private int qtdAdiciona;
    private int totalRestado;
    private int idProduto;
    private int idProdutoNota_produto;
    private int statusOpc;

    /**
     * @return the dataAlteracao
     */
    public String getDataAlteracao() {
        return dataAlteracao;
    }

    /**
     * @param dataAlteracao the dataAlteracao to set
     */
    public void setDataAlteracao(String dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    /**
     * @return the qtdAdiciona
     */
    public int getQtdAdiciona() {
        return qtdAdiciona;
    }

    /**
     * @param qtdAdicionada the qtdAdiciona to set
     */
    public void setQtdAdiciona(int qtdAdicionada) {
        this.qtdAdiciona = qtdAdicionada;
    }

    /**
     * @return the totalRestado
     */
    public int getTotalRestado() {
        return totalRestado;
    }

    /**
     * @param total the totalRestado to set
     */
    public void setTotalRestado(int total) {
        this.totalRestado = total;
    }

    /**
     * @return the idProduto
     */
    public int getIdProduto() {
        return idProduto;
    }

    /**
     * @param idProduto the idProduto to set
     */
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    /**
     * @return the statusOpc
     */
    public int getStatusOpc() {
        return statusOpc;
    }

    /**
     * @param statusOpc the statusOpc to set
     */
    public void setStatusOpc(int statusOpc) {
        this.statusOpc = statusOpc;
    }

    /**
     * @return the idProdutoNota_produto
     */
    public int getIdProdutoNota_produto() {
        return idProdutoNota_produto;
    }

    /**
     * @param idProdutoNota_produto the idProdutoNota_produto to set
     */
    public void setIdProdutoNota_produto(int idProdutoNota_produto) {
        this.idProdutoNota_produto = idProdutoNota_produto;
    }
    
    
}
