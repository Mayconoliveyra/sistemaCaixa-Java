/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.ModeloFatura;
import Modelo.ModeloMostraDadosNotaFiscal;
import Modelo.ModeloImportNotaFiscalDadosInicial;
import Modelo.ModelolImportProdutoNotaFiscal;
import Visao.test;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class ControleImportNotaFiscalXML {
    ModelolImportProdutoNotaFiscal modXML = new ModelolImportProdutoNotaFiscal();
    ModeloImportNotaFiscalDadosInicial modNotaInicio= new ModeloImportNotaFiscalDadosInicial();
    ModeloMostraDadosNotaFiscal modMostra= new ModeloMostraDadosNotaFiscal(); 
    ModeloFatura modFat = new ModeloFatura();
    ConectaBanco conecBan = new ConectaBanco();
    StringUtils StringUtil = new StringUtils();
    
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // IMPORTA CML
    String chaveEcessoVeriBanc;
    String chaveEcessoNota;
    String cnpjForneVeriBanc;
    String cnpjForneNota;
   
    int idForcenedor;
    int idNotaInicio;
    int idNotaDadosCobranca;
    
    int opCodCean;
    String codXprodCriaCodCean;
    String cnpjForneCriarCodCean="";
 
      SimpleDateFormat forma = new SimpleDateFormat("dd/MM/yyyy");
      Date hoje = new Date();
      
    public void parseNota(String caminho){
            try { // INICIO DO COMANDO PARA IMPORTA NOTA XML
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(caminho);
           // METODO PARA PEGAR CHAVE DA NOTA 
            NodeList noChaveId = doc.getElementsByTagName("infNFe");  //PEGA O NO PAI DA NOTA
            Node noDaChave = noChaveId.item(0); // "ID"
            Element elementoChave = (Element) noDaChave; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
            String chave= elementoChave.getAttribute("Id"); // SET CHAVE DA NOTA
            chaveEcessoNota= chave.replaceAll("[^0-9]", "");
            System.out.println("Chave= "+ chave);
            modMostra.setChaveAcesso(chaveEcessoNota);
 
            // METODO PARA PEGAR CNPJ FORNECEDOR
            NodeList noCnpjForn = doc.getElementsByTagName("emit");  //PEGA O NO PAI DA NOTA
            Node noDoForn = noCnpjForn.item(0); // "ID"
            Element elementoForn = (Element) noDoForn; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
            NodeList listaDeFilhosCnpj = elementoForn.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
            Node noFilhoForn = listaDeFilhosCnpj.item(0);
            String cnpjForn= noFilhoForn.getTextContent(); // SET ATRIBUTO (nItem = id)
            cnpjForneNota= cnpjForn.replaceAll("[^0-9]", ""); // FORMATA PARA APENAS APENAS NÚMEROS
            verificaSeANotaNaoJaEstaCadastradaNoBanco();
            verificarSeExisteFornecedorCadastrado();

            if(!chaveEcessoNota.equals(chaveEcessoVeriBanc)){
                if(cnpjForneNota.equals(cnpjForneVeriBanc)){
           modNotaInicio.setChaveDeAcesso(chaveEcessoNota);
           modNotaInicio.setDataImport(""+forma.format(hoje));
           modNotaInicio.setId_fornecedor(idForcenedor);
            NodeList noPaiIde = doc.getElementsByTagName("ide");  //PEGA O NO PAI DA NOTA   
            //METODO PARA PERCORRE A LISTA    
            int tamanhoDoNoPai= noPaiIde.getLength(); //TAMANHO DA LISTA
              System.out.println();
              System.out.println("Tamanho Ide: "+ tamanhoDoNoPai);
              
            for(int i=0; i<tamanhoDoNoPai; i++){  //ANDA TODA A LISTA
            Node noIde = noPaiIde.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)
             
            if(noIde.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO 
                Element elementoIde = (Element) noIde; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
 
                  NodeList listaDeFilhosIde = elementoIde.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
                  
                  int tamanhoListaFilhos = listaDeFilhosIde.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS 
                  for (int j = 0; j < tamanhoListaFilhos; j++) {
                    
                       Node noFilho = listaDeFilhosIde.item(j);  // SETA TODOS OS NO FILHOS
                       if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                           Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO
                                
                           switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL 
                                
                               case "mod":
                                  System.out.println("Mod: "+ elementoFilho.getTextContent());
                                  modMostra.setModelo(Integer.parseInt(elementoFilho.getTextContent()));
                               break;
                               case "serie": 
                                   System.out.println("Serie: "+ elementoFilho.getTextContent());
                                   modMostra.setSerie(Integer.parseInt(elementoFilho.getTextContent()));
                               break;
                               case "nNF":
                                  System.out.println("Número: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modNotaInicio.setNumero(elementoFilho.getTextContent());
                                  modMostra.setNumero(Integer.parseInt(elementoFilho.getTextContent()));
                               break;
                               case "dhEmi":
                                  System.out.println("Data de Emissão: "+ elementoFilho.getTextContent());  //getTextContent
                                  String convertData=elementoFilho.getTextContent();
                                  convertData=convertData.replaceAll("[^0-9]", "");
                                  convertData =convertData.substring(0,8);
                                  modNotaInicio.setDataEmissao(convertData);
                                  modMostra.setDataEmissao(elementoFilho.getTextContent());
                               break;
                               case "dhSaiEnt":
                                  System.out.println("Data/Hora de Saída ou da Entrada: "+ elementoFilho.getTextContent());  //getTextContent    
                                  modMostra.setDataSaida(elementoFilho.getTextContent());
                               break;
 
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
             NodeList noPaiEmit = doc.getElementsByTagName("emit");  //PEGA O NO PAI DA NOTA   
            //METODO PARA PERCORRE A LISTA    
            int tamanhoDoNoPaiEmit= noPaiEmit.getLength(); //TAMANHO DA LISTA
            System.out.println();
            System.out.println("Tamanho Emitente: "+ tamanhoDoNoPaiEmit);
              
            for(int i=0; i<tamanhoDoNoPaiEmit; i++){  //ANDA TODA A LISTA
            Node noEmit = noPaiEmit.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)
             
            if(noEmit.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO 
                Element elementoEmit = (Element) noEmit; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
 
                  NodeList listaDeFilhosEmit = elementoEmit.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
                  
                  int tamanhoListaFilhos = listaDeFilhosEmit.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS 
                  for (int j = 0; j < tamanhoListaFilhos; j++) {
                    
                       Node noFilho = listaDeFilhosEmit.item(j);  // SETA TODOS OS NO FILHOS
                       if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                           Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO
                                
                           switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL 
                                
                               case "CNPJ":
                                  cnpjForneCriarCodCean= elementoFilho.getTextContent();
                                  System.out.println("Emitente CNPJ: "+ elementoFilho.getTextContent());
                                  modNotaInicio.setCnpjEmitente(elementoFilho.getTextContent());
                                   modMostra.setCnpjEmit(elementoFilho.getTextContent());
                               break;
                               case "xNome": 
                                   System.out.println("Nome/ Razão Social: "+ elementoFilho.getTextContent());
                                  modNotaInicio.setNomeEmitente(elementoFilho.getTextContent());
                                   modMostra.setNomeFatasiaEmit(elementoFilho.getTextContent());
                               break;
                               case "IE":
                                  System.out.println("Inscrição Estadual: "+ elementoFilho.getTextContent());  //getTextContent  
                                   modMostra.setInscriçãoEstadualEmit(elementoFilho.getTextContent());
                               break;
                               case "CRT":
                                  System.out.println("Código de Regime Tributário: "+ elementoFilho.getTextContent());  //getTextContent 
                                   modMostra.setCodRegimeTribuEmit(elementoFilho.getTextContent());
                               break;
          
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
            
            //SETA ENDEREÇO EMITENTE
            NodeList noPaiEmitEnder = doc.getElementsByTagName("enderEmit");  //PEGA O NO PAI DA NOTA   
            //METODO PARA PERCORRE A LISTA    
            int tamanhoDoNoPaiEmitEnder= noPaiEmitEnder.getLength(); //TAMANHO DA LISTA
            System.out.println();
            System.out.println("Tamanho EmitenteEnder: "+ tamanhoDoNoPaiEmitEnder);
              
            for(int i=0; i<tamanhoDoNoPaiEmitEnder; i++){  //ANDA TODA A LISTA
            Node noEmitEnder = noPaiEmitEnder.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)
             
            if(noEmitEnder.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO 
                Element elementoEmitEnder = (Element) noEmitEnder; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
 
                  NodeList listaDeFilhosEmitEnder = elementoEmitEnder.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
                  
                  int tamanhoListaFilhos = listaDeFilhosEmitEnder.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS 
                  for (int j = 0; j < tamanhoListaFilhos; j++) {
                    
                       Node noFilho = listaDeFilhosEmitEnder.item(j);  // SETA TODOS OS NO FILHOS
                       if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                           Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO
                                
                           switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL 
                                
                               case "xBairro":
                                  System.out.println("Bairro / Distrito: "+ elementoFilho.getTextContent());
                                  modMostra.setBairroEmit(elementoFilho.getTextContent());
                               break;
                               case "xMun": 
                                   System.out.println("Município: "+ elementoFilho.getTextContent());
                                   modMostra.setMunicipioEmit(elementoFilho.getTextContent());
                               break;
                               case "cMun":
                                  System.out.println("Número Município: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modMostra.setNumeroMunicioEmit(Integer.parseInt(elementoFilho.getTextContent()));
                               break;
                               case "UF":
                                  System.out.println("UF: "+ elementoFilho.getTextContent());  //getTextContent   
                                  modMostra.setUfEmit(elementoFilho.getTextContent());
                               break;
                               case "xLgr":
                                  System.out.println("Endereço: "+ elementoFilho.getTextContent());  //getTextContent 
                                 modMostra.setEderecoEmit(elementoFilho.getTextContent());
                               break;
                               case "CEP":
                                  System.out.println("CEP: "+ elementoFilho.getTextContent());  //getTextContent   
                                  modMostra.setCepEmit(elementoFilho.getTextContent());
                               break;    
                               case "fone":
                                  System.out.println("Telefone: "+ elementoFilho.getTextContent());  //getTextContent    
                                  modMostra.setTelefoneEmit(elementoFilho.getTextContent());
                               break;    
                               case "xPais":
                                  System.out.println("País: "+ elementoFilho.getTextContent());  //getTextContent  
                                  modMostra.setPaisEmit(elementoFilho.getTextContent());
                               break; 
                               case "cPais":
                                  System.out.println("Número País: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modMostra.setNumeroPaisEmit(Integer.parseInt(elementoFilho.getTextContent()));
                               break; 
    
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
        
             //SETA DADOS DESTINARIO
            NodeList noPaiDest = doc.getElementsByTagName("dest");  //PEGA O NO PAI DA NOTA   
            //METODO PARA PERCORRE A LISTA    
            int tamanhoDoNoPaiDest= noPaiDest.getLength(); //TAMANHO DA LISTA
            System.out.println();
            System.out.println("Tamanho Destinario: "+ tamanhoDoNoPaiDest);
              
            for(int i=0; i<tamanhoDoNoPaiDest; i++){  //ANDA TODA A LISTA
            Node noDest = noPaiDest.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)
             
            if(noDest.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO 
                Element elementoDest = (Element) noDest; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
 
                  NodeList listaDeFilhosDest = elementoDest.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
                  
                  int tamanhoListaFilhos = listaDeFilhosDest.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS 
                  for (int j = 0; j < tamanhoListaFilhos; j++) {
                    
                       Node noFilho = listaDeFilhosDest.item(j);  // SETA TODOS OS NO FILHOS
                       if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                           Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO
                                
                           switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL 
                                
                               case "xCNPJ":
                                  System.out.println("CNPJ Destinario: "+ elementoFilho.getTextContent());
                                  modMostra.setCnpjDestinario(elementoFilho.getTextContent());
                               break;
                               case "xNome": 
                                   System.out.println("Nome Destinario: "+ elementoFilho.getTextContent());
                                    modMostra.setNomeRazaoDestinario(elementoFilho.getTextContent());
                               break;
                               case "IE":
                                  System.out.println("Inscrição Estadual Destinario: "+ elementoFilho.getTextContent());  //getTextContent
                                   modMostra.setInscricaoEstadualDestinario(Integer.parseInt(elementoFilho.getTextContent()));
                               break;

                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
            
           //SETA VALORES DE IMPOSTO
            NodeList noPaiTotaisValores = doc.getElementsByTagName("ICMSTot");  //PEGA O NO PAI DA NOTA   
            //METODO PARA PERCORRE A LISTA    
            int tamanhoDoNoPaiTotaisValores= noPaiTotaisValores.getLength(); //TAMANHO DA LISTA
            System.out.println();
            System.out.println("Tamanho Totais: "+ tamanhoDoNoPaiTotaisValores);
              
            for(int i=0; i<tamanhoDoNoPaiTotaisValores; i++){  //ANDA TODA A LISTA
            Node nodeNo = noPaiTotaisValores.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)
             
            if(nodeNo.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO 
                Element elementoConvertNode = (Element) nodeNo; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
 
                  NodeList listaDeFilhosDoNode = elementoConvertNode.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
                  
                  int tamanhoListaFilhos = listaDeFilhosDoNode.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS 
                  for (int j = 0; j < tamanhoListaFilhos; j++) {
                    
                       Node noFilho = listaDeFilhosDoNode.item(j);  // SETA TODOS OS NO FILHOS
                       if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                           Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO
                                
                           switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL 
                                
                               case "vBC":
                                  System.out.println("Base de Cálculo ICMS: "+ elementoFilho.getTextContent());
                                  modMostra.setBaseCalculoICMS(Float.parseFloat(elementoFilho.getTextContent()));
                               break;
                               case "vICMS": 
                                   System.out.println("Valor do ICMS: "+ elementoFilho.getTextContent());
                                   modMostra.setValorIcms(Float.parseFloat(elementoFilho.getTextContent()));
                               break;                         
                               case "vICMSDeson":
                                  System.out.println("Valor do ICMS Desonerado: "+ elementoFilho.getTextContent());  //getTextContent  
                                  modMostra.setValorIcmsDesonerado(Float.parseFloat(elementoFilho.getTextContent()));
                               break;
                               case "vFCP":
                                  System.out.println("Valor Total do FCP: "+ elementoFilho.getTextContent());  //getTextContent  
                                  modMostra.setValorTotalFCP(Float.parseFloat(elementoFilho.getTextContent()));
                               break;
                               case "vBCST":
                                  System.out.println("Base de Cálculo ICMS ST: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modMostra.setBaseCalICMSSt(Float.parseFloat(elementoFilho.getTextContent()));
                               break;
                               case "vST":
                                  System.out.println("Valor ICMS Substituição: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modMostra.setValorTotalICMsSub(Float.parseFloat(elementoFilho.getTextContent()));
                               break;    
                               case "vFCPST":
                                  System.out.println("Valor Total do FCP retido por ST: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modMostra.setValorTFCPST(Float.parseFloat(elementoFilho.getTextContent()));
                               break;    
                               case "vFCPSTRet":
                                  System.out.println("Valor Total do FCP retido anteriormente por ST: "+ elementoFilho.getTextContent());  //getTextContent
                                  modMostra.setVTFRAST(Float.parseFloat(elementoFilho.getTextContent()));
                               break; 
                               case "vProd":
                                  System.out.println("Valor Total dos Produtos: "+ elementoFilho.getTextContent());  //getTextContent  
                                  modMostra.setVTProdutos(Float.parseFloat(elementoFilho.getTextContent()));
                               break; 
                               case "vFrete":
                                  System.out.println("Valor do Frete: "+ elementoFilho.getTextContent());  //getTextContent   
                                  modMostra.setValorFrete(Float.parseFloat(elementoFilho.getTextContent()));
                               break;    
                               case "vSeg":
                                  System.out.println("Valor do Seguro: "+ elementoFilho.getTextContent());  //getTextContent    
                                  modMostra.setValorSeguro(Float.parseFloat(elementoFilho.getTextContent()));
                               break;    
                               case "vDesc":
                                  System.out.println("Valor Total dos Descontos: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modMostra.setValorTotalDesconto(Float.parseFloat(elementoFilho.getTextContent()));
                               break; 
                               case "vII":
                                  System.out.println("Valor Total do II: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modMostra.setValorTotalII(Float.parseFloat(elementoFilho.getTextContent()));
                               break; 
                               case "vIPI":
                                  System.out.println("Valor Total do IPI: "+ elementoFilho.getTextContent());  //getTextContent   
                                  modMostra.setValorTotalIPI(Float.parseFloat(elementoFilho.getTextContent()));
                               break; 
                               case "vIPIDevol":
                                  System.out.println("Valor Total do IPI Devolvido: "+ elementoFilho.getTextContent());  //getTextContent  
                                  modMostra.setValorTotalDev(Float.parseFloat(elementoFilho.getTextContent()));
                               break;    
                               case "vPIS":
                                  System.out.println("Valor do PIS: "+ elementoFilho.getTextContent());  //getTextContent  
                                  modMostra.setValorPIS(Float.parseFloat(elementoFilho.getTextContent()));
                               break;    
                               case "vCOFINS":
                                  System.out.println("Valor da COFINS: "+ elementoFilho.getTextContent());  //getTextContent 
                                  modMostra.setValorConfins(Float.parseFloat(elementoFilho.getTextContent()));
                               break; 
                               case "vOutro":
                                  System.out.println("Outras Despesas Acessórias: "+ elementoFilho.getTextContent());  //getTextContent   
                                  modMostra.setOutrasDescpAces(Float.parseFloat(elementoFilho.getTextContent()));
                               break; 
                               case "vNF":
                                  System.out.println("Valor Total da NFe: "+ elementoFilho.getTextContent());  //getTextContent    
                                  modNotaInicio.setValorTotalDaNota(Float.parseFloat(elementoFilho.getTextContent()));
                                  modMostra.setValorTotalNotaFiscal(Float.parseFloat(elementoFilho.getTextContent()));
                               break;    
  
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
          
            
             //SETA VALORES DA FATURA
            NodeList noPaiFat = doc.getElementsByTagName("fat");  //PEGA O NO PAI DA NOTA   
            //METODO PARA PERCORRE A LISTA    
            int tamanhoDoNoPaiFat= noPaiFat.getLength(); //TAMANHO DA LISTA
            System.out.println();
            System.out.println("Tamanho Fat: "+ tamanhoDoNoPaiFat);
              
            for(int i=0; i<tamanhoDoNoPaiFat; i++){  //ANDA TODA A LISTA
            Node nodeNo = noPaiFat.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)
             
            if(nodeNo.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO 
                Element elementoConvertNode = (Element) nodeNo; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
 
                  NodeList listaDeFilhosDoNode = elementoConvertNode.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
                  
                  int tamanhoListaFilhos = listaDeFilhosDoNode.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS 
                  for (int j = 0; j < tamanhoListaFilhos; j++) {
                    
                       Node noFilho = listaDeFilhosDoNode.item(j);  // SETA TODOS OS NO FILHOS
                       if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                           Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO
                                
                           switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL 
                                
                               case "nFat":
                                  System.out.println("Número: "+ elementoFilho.getTextContent());
                                  modFat.setNumeroFatura(Integer.parseInt(elementoFilho.getTextContent()));
                                  modMostra.setNumeroCob(Integer.parseInt(elementoFilho.getTextContent()));
                               break;
                               case "vOrig": 
                                   System.out.println("Valor Original: "+ elementoFilho.getTextContent());
                                   modFat.setValorOriginal(Float.parseFloat(elementoFilho.getTextContent()));
                                   modMostra.setValorOriCob(Float.parseFloat(elementoFilho.getTextContent()));
                               break;
                               case "vLiq":
                                  System.out.println("Valor Líquido: "+ elementoFilho.getTextContent());  //getTextContent  
                                  modFat.setValorLiquido(Float.parseFloat(elementoFilho.getTextContent()));
                                  modMostra.setValorLiquidoCob(Float.parseFloat(elementoFilho.getTextContent()));
                               break;
                               
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
       addInicio(modNotaInicio);
       addDadosCobranca(modFat);   
         
           //SETA VALORES DA PARCELA
            NodeList noPaiDup = doc.getElementsByTagName("dup");  //PEGA O NO PAI DA NOTA   
            //METODO PARA PERCORRE A LISTA    
            int tamanhoDoNoPaiDup= noPaiDup.getLength(); //TAMANHO DA LISTA
            System.out.println();
            System.out.println("Tamanho Dup: "+ tamanhoDoNoPaiDup);
              
            for(int i=0; i<tamanhoDoNoPaiDup; i++){  //ANDA TODA A LISTA
            Node nodeNo = noPaiDup.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)
             
            if(nodeNo.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO 
                Element elementoConvertNode = (Element) nodeNo; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
 
                  NodeList listaDeFilhosDoNode = elementoConvertNode.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
                  
                  int tamanhoListaFilhos = listaDeFilhosDoNode.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS 
                  for (int j = 0; j < tamanhoListaFilhos; j++) {
                    
                       Node noFilho = listaDeFilhosDoNode.item(j);  // SETA TODOS OS NO FILHOS
                       if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                           Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO
                                
                           switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL 
                                
                               case "nDup":
                                  System.out.println("Número: "+ elementoFilho.getTextContent());
                                  modFat.setNumeroParcela(Integer.parseInt(elementoFilho.getTextContent()));
                               break;
                               case "dVenc": 
                                  System.out.println("Vencimento: "+ elementoFilho.getTextContent());
                                  modFat.setDataVecimento(elementoFilho.getTextContent());
                               break;
                               case "vDup":
                                  System.out.println("Valor: "+ elementoFilho.getTextContent());  //getTextContent  
                                  modFat.setValorParcela(Float.parseFloat(elementoFilho.getTextContent()));
                                  System.out.println();
                               break;
                               
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
//           addInicio(modNotaInicio);
//          addDadosCobranca(modFat);    
         addParcelas(modFat);
        }
//addInicio(modNotaInicio);
  
            NodeList listaDeProdutos = doc.getElementsByTagName("prod");  //PEGA O NOME NOME DA LISTA
            int tamanhoLista= listaDeProdutos.getLength(); //TAMANHO DA LISTA
            for(int i=0; i<tamanhoLista; i++){  //ANDA TODA A LISTA
                Node noItem = listaDeProdutos.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)

                if(noItem.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO
                    Element elementoItem = (Element) noItem; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS

                        String id= elementoItem.getAttribute("nItem"); // SET ATRIBUTO (nItem = id)
                        //                jTextField1.setText(id);
//                                        System.out.println("ID"+ id);

                        NodeList listaDeFilhosItem = elementoItem.getChildNodes(); // PEGA TODOS OS NÓS FILHOS

                        int tamanhoListaFilhos = listaDeFilhosItem.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS
                        for (int j = 0; j < tamanhoListaFilhos; j++) {

                            Node noFilho = listaDeFilhosItem.item(j);  // SETA TODOS OS NO FILHOS
                            if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                                Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO

                                switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL
     
                                    //
                                    case "cProd":
                                    codXprodCriaCodCean=elementoFilho.getTextContent();
                                    System.out.println();
                                    System.out.println("ID do produto no banco do fornecedor= "+ elementoFilho.getTextContent());  //getTextContent
                                    modXML.setCodProdFor_cProd(Integer.parseInt(elementoFilho.getTextContent()));
                                    break;

                                    case "xProd":
                                    System.out.println("Descrição do produto= "+ elementoFilho.getTextContent());  //getTextContent
                                    modXML.setNomeProd_xProd(elementoFilho.getTextContent());
                                    
                                    break;

                                    case "NCM":
                                    System.out.println("NCM/ SH= "+ elementoFilho.getTextContent());  //getTextContent
                                    modXML.setNcm_NCM(Integer.parseInt(elementoFilho.getTextContent()));
                                    break;

                                    case "uCom":
                                    System.out.println("UNIDADE DE MEDIDA= "+ elementoFilho.getTextContent());  //getTextContent
                                    modXML.setUnidMedida_uCom(elementoFilho.getTextContent());
                                    break;

                                    case "qCom":
                                    System.out.println("QUANTIDADE RECEBIDA= "+ elementoFilho.getTextContent());  //getTextContent
                                    modXML.setQtdRecebidas_qCom(Float.parseFloat(elementoFilho.getTextContent()));
                                    break;

                                    case "vUnCom":
                                    System.out.println("VALOR UNITÁRIO= "+ elementoFilho.getTextContent());  //getTextContent
                                    modXML.setVlrUniProd_vUnCom(Float.parseFloat(elementoFilho.getTextContent()));
                                    break;

                                    case "vProd":
                                    System.out.println("VALOR TOTAL APLICADO= "+ elementoFilho.getTextContent());  //getTextContent
                                    modXML.setVlrTotalProd_vProd(Float.parseFloat(elementoFilho.getTextContent()));
                                    break;

                                    case "cEANTrib":
                                    System.out.println("CÓDIGO cEANTrib= "+ elementoFilho.getTextContent());  //getTextContent
                                    modXML.setCod_prod_cEANTrib(elementoFilho.getTextContent());
                                    String codCeanTrib= elementoFilho.getTextContent();
                                    
                                    if(codCeanTrib.length()>=12){
                                      modXML.setCod_prod_cEANTrib(elementoFilho.getTextContent());
                                      opCodCean=1;
                                    }else{
                                     opCodCean=2;
                                    }
                                    break;

                                    default:
                                    break;
                                    
                                }

                            }

                        }
                      
                    }
                   if(opCodCean==2){
                      String gerarCodBarra;
                      int idFornecedorCriaCodCean = 0;
                    try {
                        conecBan.executaSQL("select * from fornecedores where cnpj_fornecedor="+cnpjForneCriarCodCean+"'");
                        conecBan.rs.first();
                        idFornecedorCriaCodCean= conecBan.rs.getInt("id_fornecedor");
                    } catch (SQLException ex) {
                        
                    }
                      gerarCodBarra= codXprodCriaCodCean+idFornecedorCriaCodCean; // Cprod+IdDoFornecedor
                       String codCeanGeradoCprodIdforn;
                       codCeanGeradoCprodIdforn= StringUtil.leftPad(""+gerarCodBarra,13,"0");  //COMPLETA O RESTO COM 0 
                      modXML.setCod_prod_cEANTrib(codCeanGeradoCprodIdforn);
                   }else{
                       
                   }
                  addProduto(modXML);
                }
//             addInicio(modNotaInicio);
//               addDadosCobranca(modFat);
                }else{
                JOptionPane.showConfirmDialog(null, "Fornecedor Não Cadastrado!");  
                }
            }else{
              JOptionPane.showMessageDialog(null, "ERRO! \n Nota Já Cadastrada! \n Click em 'OK' e Tente Novamente ");
            }
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
            } // FINAL DO COMANDO PARA IMPORTA XMP

    }
    
    
   public void addDadosCobranca(ModeloFatura mod){
       acharIdNotaInicio();
       conecBan.conexaoBanco();
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("insert into nota_dados_cobranca (numero_fat, valor_liquido, valor_original, valor_desconto, id_nota_inicio ) values(?,?,?,?,?)");
            pst.setInt(1, mod.getNumeroFatura());
            pst.setFloat(2, mod.getValorLiquido());
            pst.setFloat(3, mod.getValorOriginal());
            pst.setFloat(4, mod.getValorDesconto());
            pst.setInt(5, idNotaInicio);
            pst.execute();
        } catch (SQLException ex) {
       JOptionPane.showMessageDialog(null, "Erro ao Adicionar Dados da Cobrança! \n Erro:" +ex);
        }
        conecBan.desconectar();
   }
   
   public void acharIdFatura(){
       conecBan.conexaoBanco();
        try {
            conecBan.executaSQL("select * from nota_dados_cobranca order by id_fatura");
            conecBan.rs.last();
            idNotaDadosCobranca= conecBan.rs.getInt("id_fatura");
            
        } catch (SQLException ex) {
       JOptionPane.showMessageDialog(null, "Erro ao Achar Id Fatura! \n Erro:" +ex);
        }
       conecBan.desconectar();
   }
   
   
   public void addParcelas(ModeloFatura mod){
       acharIdFatura();
       conecBan.conexaoBanco();
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("insert into nota_faturas (numero_parc, vencimento, vlr_fatura, id_dados_cob) values(?,?,?,?)");
            pst.setInt(1, mod.getNumeroParcela());
            pst.setString(2, mod.getDataVecimento());
            pst.setFloat(3, mod.getValorParcela());
            pst.setInt(4, idNotaDadosCobranca);
            pst.execute();
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, "Erro ao Adicionar Dados da Parcela! \n Erro:" +ex);
        }
       conecBan.desconectar();
   }
   
   
   public void addInicio(ModeloImportNotaFiscalDadosInicial mod){
       conecBan.conexaoBanco();
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("insert into nota_inicio (chave, cnpj_emit, nome_emit, numero_nota, id_fornecedor, data_import, data_emis, valor_total_nota) values(?,?,?,?,?,?,?,?)");
            pst.setString(1, mod.getChaveDeAcesso());
            pst.setString(2, mod.getCnpjEmitente());
            pst.setString(3, mod.getNomeEmitente());
            pst.setString(4, mod.getNumero());
            pst.setInt(5, mod.getId_fornecedor());
            pst.setString(6, mod.getDataImport());
            pst.setString(7, mod.getDataEmissao());
            pst.setFloat(8, mod.getValorTotalDaNota());
            pst.execute();
                    } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, "Erro ao Adicionar Dados Iniciais nota XML \nErro: "+ex);
        }
       conecBan.desconectar();
   } 
    
    public void addProduto(ModelolImportProdutoNotaFiscal mod){
       acharIdNotaInicio();
       conecBan.conexaoBanco();
        try {
            PreparedStatement pst = conecBan.conn.prepareStatement("insert into nota_produto (cod_prod, nome_prod,"
                    + " ncm, unid_medida, qtd_recebida, vlr_unit_prod, vlr_total_prod, id_nota_inicio, cod_cean_trib) values(?,?,?,?,?,?,?,?,?)");
            
            pst.setInt(1, mod.getCodProdFor_cProd());
            pst.setString(2, mod.getNomeProd_xProd());
            pst.setInt(3, mod.getNcm_NCM());
            pst.setString(4, mod.getUnidMedida_uCom());
            pst.setFloat(5, mod.getQtdRecebidas_qCom());
            pst.setFloat(6, mod.getVlrUniProd_vUnCom());
            pst.setFloat(7, mod.getVlrTotalProd_vProd());
            pst.setInt(8,idNotaInicio);
            pst.setString(9,mod.getCod_prod_cEANTrib());
            pst.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao adicionar produto! \nErro: "+ ex);
        }
       conecBan.desconectar();
    }
    
    public void acharIdNotaInicio(){
        conecBan.conexaoBanco();
        try {
            conecBan.executaSQL("select * from nota_inicio where chave='"+chaveEcessoNota+"'");
            conecBan.rs.first();
            idNotaInicio= conecBan.rs.getInt("id_nota");
        } catch (SQLException ex) {
        }
        conecBan.desconectar();
    }
    
    public void verificarSeExisteFornecedorCadastrado(){
            conecBan.conexaoBanco();
        try {
            conecBan.executaSQL("select * from fornecedores where cnpj_fornecedor='"+cnpjForneNota+"'");
            conecBan.rs.first();
            idForcenedor = conecBan.rs.getInt("id_fornecedor");
            cnpjForneVeriBanc= conecBan.rs.getString("cnpj_fornecedor");
        } catch (SQLException ex) {
        }    
           conecBan.desconectar();
    }
    
    public void verificaSeANotaNaoJaEstaCadastradaNoBanco(){
                conecBan.conexaoBanco();
                try {
                 conecBan.executaSQL("select * from nota_inicio where chave='"+chaveEcessoNota+"'");
                 conecBan.rs.first();
                 chaveEcessoVeriBanc = conecBan.rs.getString("chave");
                } catch (SQLException ex) {
                }
            conecBan.desconectar();
    }
    
   

}
