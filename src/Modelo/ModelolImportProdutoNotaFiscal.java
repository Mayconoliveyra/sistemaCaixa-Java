/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

public class ModelolImportProdutoNotaFiscal {
    private int codProdFor_cProd; // CODIGO DO PRODUTO FORNECIDO PELO FORNECEDOR
    private String nomeProd_xProd; //DESCRIÇÃO DO PRODUTO
    private int ncm_NCM; // NOMENCLATURA COMM DO MERCASUL. É UM NÚMERO QUE IDENTIFICA CADA PRODUTO EXISTENTE EM DIVERSOS PAÍSES DA AMÉRICA DO SUL
    private String unidMedida_uCom; //INFORMA A UNIDADE DE MEDIDA DO PRODUTO UTILIZADA 
    private float qtdRecebidas_qCom; // QUANTIDADE RECEBIDA
    private float vlrUniProd_vUnCom; // VALOR UNITÁRIO DE CADA MODELO DE PRODUTO DESCRITO NA NOTA
    private float vlrTotalProd_vProd; // INFORMA O VALOR TOTAL APLICADA A TODOS OS PRODUTOPS DESCRITO NA NOTA FISCAL
    private String cod_prod_cEANTrib;//NÚMERO DO PEDIDO DE COMPRA RELATIVO Á NOTA FISCAL
    private int id_notaInicio;
    
    
    /**
     * @return the nomeProd_xProd
     */
    public String getNomeProd_xProd() {
        return nomeProd_xProd;
    }

    /**
     * @param nomeProd_xProd the nomeProd_xProd to set
     */
    public void setNomeProd_xProd(String nomeProd_xProd) {
        this.nomeProd_xProd = nomeProd_xProd;
    }

    /**
     * @return the codProdFor_cProd
     */
    public int getCodProdFor_cProd() {
        return codProdFor_cProd;
    }

    /**
     * @param codProdFor_cProd the codProdFor_cProd to set
     */
    public void setCodProdFor_cProd(int codProdFor_cProd) {
        this.codProdFor_cProd = codProdFor_cProd;
    }

    /**
     * @return the ncm_NCM
     */
    public int getNcm_NCM() {
        return ncm_NCM;
    }

    /**
     * @param ncm_NCM the ncm_NCM to set
     */
    public void setNcm_NCM(int ncm_NCM) {
        this.ncm_NCM = ncm_NCM;
    }

    /**
     * @return the unidMedida_uCom
     */
    public String getUnidMedida_uCom() {
        return unidMedida_uCom;
    }

    /**
     * @param unidMedida_uCom the unidMedida_uCom to set
     */
    public void setUnidMedida_uCom(String unidMedida_uCom) {
        this.unidMedida_uCom = unidMedida_uCom;
    }

    /**
     * @return the qtdRecebidas_qCom
     */
    public float getQtdRecebidas_qCom() {
        return qtdRecebidas_qCom;
    }

    /**
     * @param qtdRecebidas_qCom the qtdRecebidas_qCom to set
     */
    public void setQtdRecebidas_qCom(float qtdRecebidas_qCom) {
        this.qtdRecebidas_qCom = qtdRecebidas_qCom;
    }

    /**
     * @return the vlrUniProd_vUnCom
     */
    public float getVlrUniProd_vUnCom() {
        return vlrUniProd_vUnCom;
    }

    /**
     * @param vlrUniProd_vUnCom the vlrUniProd_vUnCom to set
     */
    public void setVlrUniProd_vUnCom(float vlrUniProd_vUnCom) {
        this.vlrUniProd_vUnCom = vlrUniProd_vUnCom;
    }

    /**
     * @return the vlrTotalProd_vProd
     */
    public float getVlrTotalProd_vProd() {
        return vlrTotalProd_vProd;
    }

    /**
     * @param vlrTotalProd_vProd the vlrTotalProd_vProd to set
     */
    public void setVlrTotalProd_vProd(float vlrTotalProd_vProd) {
        this.vlrTotalProd_vProd = vlrTotalProd_vProd;
    }

    /**
     * @return the cod_prod_cEANTrib
     */
    public String getCod_prod_cEANTrib() {
        return cod_prod_cEANTrib;
    }

    /**
     * @param cod_prod_cEANTrib the cod_prod_cEANTrib to set
     */
    public void setCod_prod_cEANTrib(String cod_prod_cEANTrib) {
        this.cod_prod_cEANTrib = cod_prod_cEANTrib;
    }


    /**
     * @return the id_notaInicio
     */
    public int getId_notaInicio() {
        return id_notaInicio;
    }

    /**
     * @param id_notaInicio the id_notaInicio to set
     */
    public void setId_notaInicio(int id_notaInicio) {
        this.id_notaInicio = id_notaInicio;
    }

 
}
