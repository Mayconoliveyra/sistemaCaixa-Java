
package Modelo;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ModeloFatura {
    private int numeroFatura;
    private float valorOriginal;
    private float valorLiquido;
    private float valorDesconto;
    
    private int numeroParcela;
    private String dataVecimento;
    private float valorParcela;

    /**
     * @return the numeroFatura
     */
    public int getNumeroFatura() {
        return numeroFatura;
    }

    /**
     * @param numeroFatura the numeroFatura to set
     */
    public void setNumeroFatura(int numeroFatura) {
        this.numeroFatura = numeroFatura;
    }

    /**
     * @return the valorOriginal
     */
    public float getValorOriginal() {
        return valorOriginal;
    }

    /**
     * @param valorOriginal the valorOriginal to set
     */
    public void setValorOriginal(float valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    /**
     * @return the valorLiquido
     */
    public float getValorLiquido() {
        return valorLiquido;
    }

    /**
     * @param valorLiquido the valorLiquido to set
     */
    public void setValorLiquido(float valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    /**
     * @return the valorDesconto
     */
    public float getValorDesconto() {
        return valorDesconto;
    }

    /**
     * @param valorDesconto the valorDesconto to set
     */
    public void setValorDesconto(float valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    /**
     * @return the numeroParcela
     */
    public int getNumeroParcela() {
        return numeroParcela;
    }

    /**
     * @param numeroParcela the numeroParcela to set
     */
    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    /**
     * @return the dataVecimento
     */
    public String getDataVecimento() {
        return dataVecimento;
    }

    /**
     * @param dataVecimento the dataVecimento to set
     */
    public void setDataVecimento(String dataVecimento) {
        this.dataVecimento = dataVecimento;
    }

    /**
     * @return the valorParcela
     */
    public float getValorParcela() {
        return valorParcela;
    }

    /**
     * @param valorParcela the valorParcela to set
     */
    public void setValorParcela(float valorParcela) {
        this.valorParcela = valorParcela;
    }
    
    
    
}
