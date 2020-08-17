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
public class ModeloMostraDadosNotaFiscal {
    //Dados Nf-E
    private String chaveAcesso;
    private int numero;
    private int modelo;
    private int serie;
    private String dataEmissao;
    private String dataSaida;
    private float valorTotalNotaFiscal;

    //DADOS DESTINADO
    private String CnpjDestinario;
    private String nomeRazaoDestinario;
    private int inscricaoEstadualDestinario;
    private String UfDestinario;
    
    // Dados do Emitente
    private String CnpjEmit;
    private String nomeFatasiaEmit;
    private String ederecoEmit;
    private String bairroEmit;
    private String cepEmit;
    private String telefoneEmit;
    private String paisEmit;
    private String municipioEmit;
    private String cnaeFiscalEmit;
    private String iestEmit;
    private String mofIcmsEmit;
    private String inscriçãoEstadualEmit;
    private String codRegimeTribuEmit;
    private String inscriçãoEstualEmit;
    private int numeroMunicioEmit;
    private String UfEmit;
    private int numeroPaisEmit;
    //Totais
    private float baseCalculoICMS;
    private float valorIcms;
    private float valorIcmsDesonerado;
    private float valorTotalFCP;
    private float outrasDescpAces;
    private float valorTotalICMsSub;
    private float valorTFCPST;
    private float VTFRAST;
    private float VTProdutos;
    private float vAproxDosTributos;
    private float valorFrete;
    private float valorSeguro;
    private float valorTotalDesconto;
    private float valorConfins;
    private float valorTotalII;
    private float valorTotalIPI;
    private float valorTotalDev;
    private float valorPIS;
    private float baseCalICMSSt;
     
    
    // Dados da cobrança
   private int numeroCob;
   private float valorOriCob;
   private float valorLiquidoCob;
   private float valorDescontoCob;
    
    
    
    
    /**
     * @return the chaveAcesso
     */
    public String getChaveAcesso() {
        return chaveAcesso;
    }

    /**
     * @param chaveAcesso the chaveAcesso to set
     */
    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * @return the modelo
     */
    public int getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    /**
     * @return the serie
     */
    public int getSerie() {
        return serie;
    }

    /**
     * @param serie the serie to set
     */
    public void setSerie(int serie) {
        this.serie = serie;
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
     * @return the dataSaida
     */
    public String getDataSaida() {
        return dataSaida;
    }

    /**
     * @param dataSaida the dataSaida to set
     */
    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    /**
     * @return the valorTotalNotaFiscal
     */
    public float getValorTotalNotaFiscal() {
        return valorTotalNotaFiscal;
    }

    /**
     * @param valorTotalNotaFiscal the valorTotalNotaFiscal to set
     */
    public void setValorTotalNotaFiscal(float valorTotalNotaFiscal) {
        this.valorTotalNotaFiscal = valorTotalNotaFiscal;
    }

    /**
     * @return the CnpjDestinario
     */
    public String getCnpjDestinario() {
        return CnpjDestinario;
    }

    /**
     * @param CnpjDestinario the CnpjDestinario to set
     */
    public void setCnpjDestinario(String CnpjDestinario) {
        this.CnpjDestinario = CnpjDestinario;
    }

    /**
     * @return the nomeRazaoDestinario
     */
    public String getNomeRazaoDestinario() {
        return nomeRazaoDestinario;
    }

    /**
     * @param nomeRazaoDestinario the nomeRazaoDestinario to set
     */
    public void setNomeRazaoDestinario(String nomeRazaoDestinario) {
        this.nomeRazaoDestinario = nomeRazaoDestinario;
    }

    /**
     * @return the inscricaoEstadualDestinario
     */
    public int getInscricaoEstadualDestinario() {
        return inscricaoEstadualDestinario;
    }

    /**
     * @param inscricaoEstadualDestinario the inscricaoEstadualDestinario to set
     */
    public void setInscricaoEstadualDestinario(int inscricaoEstadualDestinario) {
        this.inscricaoEstadualDestinario = inscricaoEstadualDestinario;
    }

    /**
     * @return the UfDestinario
     */
    public String getUfDestinario() {
        return UfDestinario;
    }

    /**
     * @param UfDestinario the UfDestinario to set
     */
    public void setUfDestinario(String UfDestinario) {
        this.UfDestinario = UfDestinario;
    }

    /**
     * @return the CnpjEmit
     */
    public String getCnpjEmit() {
        return CnpjEmit;
    }

    /**
     * @param CnpjEmit the CnpjEmit to set
     */
    public void setCnpjEmit(String CnpjEmit) {
        this.CnpjEmit = CnpjEmit;
    }

    /**
     * @return the nomeFatasiaEmit
     */
    public String getNomeFatasiaEmit() {
        return nomeFatasiaEmit;
    }

    /**
     * @param nomeFatasiaEmit the nomeFatasiaEmit to set
     */
    public void setNomeFatasiaEmit(String nomeFatasiaEmit) {
        this.nomeFatasiaEmit = nomeFatasiaEmit;
    }

    /**
     * @return the ederecoEmit
     */
    public String getEderecoEmit() {
        return ederecoEmit;
    }

    /**
     * @param ederecoEmit the ederecoEmit to set
     */
    public void setEderecoEmit(String ederecoEmit) {
        this.ederecoEmit = ederecoEmit;
    }

    /**
     * @return the bairroEmit
     */
    public String getBairroEmit() {
        return bairroEmit;
    }

    /**
     * @param bairroEmit the bairroEmit to set
     */
    public void setBairroEmit(String bairroEmit) {
        this.bairroEmit = bairroEmit;
    }

    /**
     * @return the cepEmit
     */
    public String getCepEmit() {
        return cepEmit;
    }

    /**
     * @param cepEmit the cepEmit to set
     */
    public void setCepEmit(String cepEmit) {
        this.cepEmit = cepEmit;
    }

    /**
     * @return the telefoneEmit
     */
    public String getTelefoneEmit() {
        return telefoneEmit;
    }

    /**
     * @param telefoneEmit the telefoneEmit to set
     */
    public void setTelefoneEmit(String telefoneEmit) {
        this.telefoneEmit = telefoneEmit;
    }

    /**
     * @return the paisEmit
     */
    public String getPaisEmit() {
        return paisEmit;
    }

    /**
     * @param paisEmit the paisEmit to set
     */
    public void setPaisEmit(String paisEmit) {
        this.paisEmit = paisEmit;
    }

    /**
     * @return the municipioEmit
     */
    public String getMunicipioEmit() {
        return municipioEmit;
    }

    /**
     * @param municipioEmit the municipioEmit to set
     */
    public void setMunicipioEmit(String municipioEmit) {
        this.municipioEmit = municipioEmit;
    }

    /**
     * @return the cnaeFiscalEmit
     */
    public String getCnaeFiscalEmit() {
        return cnaeFiscalEmit;
    }

    /**
     * @param cnaeFiscalEmit the cnaeFiscalEmit to set
     */
    public void setCnaeFiscalEmit(String cnaeFiscalEmit) {
        this.cnaeFiscalEmit = cnaeFiscalEmit;
    }

    /**
     * @return the iestEmit
     */
    public String getIestEmit() {
        return iestEmit;
    }

    /**
     * @param iestEmit the iestEmit to set
     */
    public void setIestEmit(String iestEmit) {
        this.iestEmit = iestEmit;
    }

    /**
     * @return the mofIcmsEmit
     */
    public String getMofIcmsEmit() {
        return mofIcmsEmit;
    }

    /**
     * @param mofIcmsEmit the mofIcmsEmit to set
     */
    public void setMofIcmsEmit(String mofIcmsEmit) {
        this.mofIcmsEmit = mofIcmsEmit;
    }

    /**
     * @return the inscriçãoEstadualEmit
     */
    public String getInscriçãoEstadualEmit() {
        return inscriçãoEstadualEmit;
    }

    /**
     * @param inscriçãoEstadualEmit the inscriçãoEstadualEmit to set
     */
    public void setInscriçãoEstadualEmit(String inscriçãoEstadualEmit) {
        this.inscriçãoEstadualEmit = inscriçãoEstadualEmit;
    }

    /**
     * @return the codRegimeTribuEmit
     */
    public String getCodRegimeTribuEmit() {
        return codRegimeTribuEmit;
    }

    /**
     * @param codRegimeTribuEmit the codRegimeTribuEmit to set
     */
    public void setCodRegimeTribuEmit(String codRegimeTribuEmit) {
        this.codRegimeTribuEmit = codRegimeTribuEmit;
    }

    /**
     * @return the inscriçãoEstualEmit
     */
    public String getInscriçãoEstualEmit() {
        return inscriçãoEstualEmit;
    }

    /**
     * @param inscriçãoEstualEmit the inscriçãoEstualEmit to set
     */
    public void setInscriçãoEstualEmit(String inscriçãoEstualEmit) {
        this.inscriçãoEstualEmit = inscriçãoEstualEmit;
    }

    /**
     * @return the baseCalculoICMS
     */
    public float getBaseCalculoICMS() {
        return baseCalculoICMS;
    }

    /**
     * @param baseCalculoICMS the baseCalculoICMS to set
     */
    public void setBaseCalculoICMS(float baseCalculoICMS) {
        this.baseCalculoICMS = baseCalculoICMS;
    }

    /**
     * @return the valorIcms
     */
    public float getValorIcms() {
        return valorIcms;
    }

    /**
     * @param valorIcms the valorIcms to set
     */
    public void setValorIcms(float valorIcms) {
        this.valorIcms = valorIcms;
    }

    /**
     * @return the valorIcmsDesonerado
     */
    public float getValorIcmsDesonerado() {
        return valorIcmsDesonerado;
    }

    /**
     * @param valorIcmsDesonerado the valorIcmsDesonerado to set
     */
    public void setValorIcmsDesonerado(float valorIcmsDesonerado) {
        this.valorIcmsDesonerado = valorIcmsDesonerado;
    }

    /**
     * @return the valorTotalFCP
     */
    public float getValorTotalFCP() {
        return valorTotalFCP;
    }

    /**
     * @param valorTotalFCP the valorTotalFCP to set
     */
    public void setValorTotalFCP(float valorTotalFCP) {
        this.valorTotalFCP = valorTotalFCP;
    }

    /**
     * @return the outrasDescpAces
     */
    public float getOutrasDescpAces() {
        return outrasDescpAces;
    }

    /**
     * @param outrasDescpAces the outrasDescpAces to set
     */
    public void setOutrasDescpAces(float outrasDescpAces) {
        this.outrasDescpAces = outrasDescpAces;
    }

    /**
     * @return the valorTotalICMsSub
     */
    public float getValorTotalICMsSub() {
        return valorTotalICMsSub;
    }

    /**
     * @param valorTotalICMsSub the valorTotalICMsSub to set
     */
    public void setValorTotalICMsSub(float valorTotalICMsSub) {
        this.valorTotalICMsSub = valorTotalICMsSub;
    }

    /**
     * @return the valorTFCPST
     */
    public float getValorTFCPST() {
        return valorTFCPST;
    }

    /**
     * @param valorTFCPST the valorTFCPST to set
     */
    public void setValorTFCPST(float valorTFCPST) {
        this.valorTFCPST = valorTFCPST;
    }

    /**
     * @return the VTFRAST
     */
    public float getVTFRAST() {
        return VTFRAST;
    }

    /**
     * @param VTFRAST the VTFRAST to set
     */
    public void setVTFRAST(float VTFRAST) {
        this.VTFRAST = VTFRAST;
    }

    /**
     * @return the VTProdutos
     */
    public float getVTProdutos() {
        return VTProdutos;
    }

    /**
     * @param VTProdutos the VTProdutos to set
     */
    public void setVTProdutos(float VTProdutos) {
        this.VTProdutos = VTProdutos;
    }

    /**
     * @return the vAproxDosTributos
     */
    public float getvAproxDosTributos() {
        return vAproxDosTributos;
    }

    /**
     * @param vAproxDosTributos the vAproxDosTributos to set
     */
    public void setvAproxDosTributos(float vAproxDosTributos) {
        this.vAproxDosTributos = vAproxDosTributos;
    }

    /**
     * @return the valorFrete
     */
    public float getValorFrete() {
        return valorFrete;
    }

    /**
     * @param valorFrete the valorFrete to set
     */
    public void setValorFrete(float valorFrete) {
        this.valorFrete = valorFrete;
    }

    /**
     * @return the valorSeguro
     */
    public float getValorSeguro() {
        return valorSeguro;
    }

    /**
     * @param valorSeguro the valorSeguro to set
     */
    public void setValorSeguro(float valorSeguro) {
        this.valorSeguro = valorSeguro;
    }

    /**
     * @return the valorTotalDesconto
     */
    public float getValorTotalDesconto() {
        return valorTotalDesconto;
    }

    /**
     * @param valorTotalDesconto the valorTotalDesconto to set
     */
    public void setValorTotalDesconto(float valorTotalDesconto) {
        this.valorTotalDesconto = valorTotalDesconto;
    }

    /**
     * @return the valorConfins
     */
    public float getValorConfins() {
        return valorConfins;
    }

    /**
     * @param valorConfins the valorConfins to set
     */
    public void setValorConfins(float valorConfins) {
        this.valorConfins = valorConfins;
    }

    /**
     * @return the valorTotalII
     */
    public float getValorTotalII() {
        return valorTotalII;
    }

    /**
     * @param valorTotalII the valorTotalII to set
     */
    public void setValorTotalII(float valorTotalII) {
        this.valorTotalII = valorTotalII;
    }

    /**
     * @return the valorTotalIPI
     */
    public float getValorTotalIPI() {
        return valorTotalIPI;
    }

    /**
     * @param valorTotalIPI the valorTotalIPI to set
     */
    public void setValorTotalIPI(float valorTotalIPI) {
        this.valorTotalIPI = valorTotalIPI;
    }

    /**
     * @return the valorTotalDev
     */
    public float getValorTotalDev() {
        return valorTotalDev;
    }

    /**
     * @param valorTotalDev the valorTotalDev to set
     */
    public void setValorTotalDev(float valorTotalDev) {
        this.valorTotalDev = valorTotalDev;
    }

    /**
     * @return the valorPIS
     */
    public float getValorPIS() {
        return valorPIS;
    }

    /**
     * @param valorPIS the valorPIS to set
     */
    public void setValorPIS(float valorPIS) {
        this.valorPIS = valorPIS;
    }

    /**
     * @return the numeroMunicioEmit
     */
    public int getNumeroMunicioEmit() {
        return numeroMunicioEmit;
    }

    /**
     * @param numeroMunicio the numeroMunicioEmit to set
     */
    public void setNumeroMunicioEmit(int numeroMunicio) {
        this.numeroMunicioEmit = numeroMunicio;
    }

    /**
     * @return the UfEmit
     */
    public String getUfEmit() {
        return UfEmit;
    }

    /**
     * @param UfEmit the UfEmit to set
     */
    public void setUfEmit(String UfEmit) {
        this.UfEmit = UfEmit;
    }

    /**
     * @return the numeroPaisEmit
     */
    public int getNumeroPaisEmit() {
        return numeroPaisEmit;
    }

    /**
     * @param numeroPaisEmit the numeroPaisEmit to set
     */
    public void setNumeroPaisEmit(int numeroPaisEmit) {
        this.numeroPaisEmit = numeroPaisEmit;
    }

    /**
     * @return the baseCalICMSSt
     */
    public float getBaseCalICMSSt() {
        return baseCalICMSSt;
    }

    /**
     * @param baseCalICMSSt the baseCalICMSSt to set
     */
    public void setBaseCalICMSSt(float baseCalICMSSt) {
        this.baseCalICMSSt = baseCalICMSSt;
    }

    /**
     * @return the numeroCob
     */
    public int getNumeroCob() {
        return numeroCob;
    }

    /**
     * @param numeroCob the numeroCob to set
     */
    public void setNumeroCob(int numeroCob) {
        this.numeroCob = numeroCob;
    }

    /**
     * @return the valorOriCob
     */
    public float getValorOriCob() {
        return valorOriCob;
    }

    /**
     * @param valorOriCob the valorOriCob to set
     */
    public void setValorOriCob(float valorOriCob) {
        this.valorOriCob = valorOriCob;
    }

    /**
     * @return the valorLiquidoCob
     */
    public float getValorLiquidoCob() {
        return valorLiquidoCob;
    }

    /**
     * @param valorLiquidoCob the valorLiquidoCob to set
     */
    public void setValorLiquidoCob(float valorLiquidoCob) {
        this.valorLiquidoCob = valorLiquidoCob;
    }

    /**
     * @return the valorDescontoCob
     */
    public float getValorDescontoCob() {
        return valorDescontoCob;
    }

    /**
     * @param valorDescontoCob the valorDescontoCob to set
     */
    public void setValorDescontoCob(float valorDescontoCob) {
        this.valorDescontoCob = valorDescontoCob;
    }

    
    
    
    
    
    
    
}  