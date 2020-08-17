/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Visao;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author NOTBOOK DE MAYCON
 */
public class test extends javax.swing.JFrame {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 
    public test() {
        initComponents();
 
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("C:\\MartisXML22.xml");
                
            NodeList noChaveId = doc.getElementsByTagName("infNFe");  //PEGA O NO PAI DA NOTA
           // METODO PARA PEGAR CHAVE DA NOTA 
            Node noDaChave = noChaveId.item(0); // "ID"
            Element elementoChave = (Element) noDaChave; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
            String chave= elementoChave.getAttribute("Id"); // SET ATRIBUTO (nItem = id)
            System.out.println("Chave= "+ chave);
             
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
                               break;
                               case "serie": 
                                   System.out.println("Serie: "+ elementoFilho.getTextContent());
                               break;
                               case "nNF":
                                  System.out.println("Número: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "dhEmi":
                                  System.out.println("Data de Emissão: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "dhSaiEnt":
                                  System.out.println("Data/Hora de Saída ou da Entrada: "+ elementoFilho.getTextContent());  //getTextContent    
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
                                  System.out.println("Emitente CNPJ: "+ elementoFilho.getTextContent());
                               break;
                               case "xNome": 
                                   System.out.println("Nome/ Razão Social: "+ elementoFilho.getTextContent());
                               break;
                               case "IE":
                                  System.out.println("Inscrição Estadual: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "CRT":
                                  System.out.println("Código de Regime Tributário: "+ elementoFilho.getTextContent());  //getTextContent    
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
                               break;
                               case "xMun": 
                                   System.out.println("Município: "+ elementoFilho.getTextContent());
                               break;
                               case "cMun":
                                  System.out.println("Número Município: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "UF":
                                  System.out.println("UF: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "xLgr":
                                  System.out.println("Endereço: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "CEP":
                                  System.out.println("CEP: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;    
                               case "fone":
                                  System.out.println("Telefone: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;    
                               case "xPais":
                                  System.out.println("País: "+ elementoFilho.getTextContent());  //getTextContent    
                               break; 
                               case "cPais":
                                  System.out.println("Número País: "+ elementoFilho.getTextContent());  //getTextContent    
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
                               break;
                               case "xNome": 
                                   System.out.println("Nome Destinario: "+ elementoFilho.getTextContent());
                               break;
                               case "IE":
                                  System.out.println("Inscrição Estadual Destinario: "+ elementoFilho.getTextContent());  //getTextContent    
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
                               break;
                               case "vICMS": 
                                   System.out.println("Valor do ICMS: "+ elementoFilho.getTextContent());
                               break;                         
                               case "vICMSDeson":
                                  System.out.println("Valor do ICMS Desonerado: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "vFCP":
                                  System.out.println("Valor Total do FCP: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "vBCST":
                                  System.out.println("Base de Cálculo ICMS ST: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "vST":
                                  System.out.println("Valor ICMS Substituição: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;    
                               case "vFCPST":
                                  System.out.println("Valor Total do FCP retido por ST: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;    
                               case "vFCPSTRet":
                                  System.out.println("Valor Total do FCP retido anteriormente por ST: "+ elementoFilho.getTextContent());  //getTextContent    
                               break; 
                               case "vProd":
                                  System.out.println("Valor Total dos Produtos: "+ elementoFilho.getTextContent());  //getTextContent    
                               break; 
                               case "vFrete":
                                  System.out.println("Valor do Frete: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;    
                               case "vSeg":
                                  System.out.println("Valor do Seguro: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;    
                               case "vDesc":
                                  System.out.println("Valor Total dos Descontos: "+ elementoFilho.getTextContent());  //getTextContent    
                               break; 
                               case "vII":
                                  System.out.println("Valor Total do II: "+ elementoFilho.getTextContent());  //getTextContent    
                               break; 
                               case "vIPI":
                                  System.out.println("Valor Total do IPI: "+ elementoFilho.getTextContent());  //getTextContent    
                               break; 
                               case "vIPIDevol":
                                  System.out.println("Valor Total do IPI Devolvido: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;    
                               case "vPIS":
                                  System.out.println("Valor do PIS: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;    
                               case "vCOFINS":
                                  System.out.println("Valor da COFINS: "+ elementoFilho.getTextContent());  //getTextContent    
                               break; 
                               case "vOutro":
                                  System.out.println("Outras Despesas Acessórias: "+ elementoFilho.getTextContent());  //getTextContent    
                               break; 
                               case "vNF":
                                  System.out.println("Valor Total da NFe: "+ elementoFilho.getTextContent());  //getTextContent    
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
                               break;
                               case "vOrig": 
                                   System.out.println("Valor Original: "+ elementoFilho.getTextContent());
                               break;
                               case "vLiq":
                                  System.out.println("Valor Líquido: "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
            
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
                               break;
                               case "dVenc": 
                                   System.out.println("Vencimento: "+ elementoFilho.getTextContent());
                               break;
                               case "vDup":
                                  System.out.println("Valor: "+ elementoFilho.getTextContent());  //getTextContent   
                                  System.out.println();
                               break;
                               
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
            
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
                                    System.out.println();
                                    System.out.println("ID= "+ elementoFilho.getTextContent());  //getTextContent
//                                    modXML.setCodProdFor_cProd(Integer.parseInt(elementoFilho.getTextContent()));
                                    break;

                                    case "xProd":
                                    System.out.println("Nome= "+ elementoFilho.getTextContent());  //getTextContent
//                                    modXML.setNomeProd_xProd(elementoFilho.getTextContent());
                                    
                                    break;

                                    case "NCM":
                                    System.out.println("NOMENCLATURA= "+ elementoFilho.getTextContent());  //getTextContent
//                                    modXML.setNcm_NCM(Integer.parseInt(elementoFilho.getTextContent()));
                                    break;

                                    case "uCom":
                                    System.out.println("UNIDADE DE MEDIDA= "+ elementoFilho.getTextContent());  //getTextContent
//                                    modXML.setUnidMedida_uCom(elementoFilho.getTextContent());
                                    break;

                                    case "qCom":
                                    System.out.println("QUANTIDADE RECEBIDA= "+ elementoFilho.getTextContent());  //getTextContent
//                                    modXML.setQtdRecebidas_qCom(Float.parseFloat(elementoFilho.getTextContent()));
                                    break;

                                    case "vUnCom":
                                    System.out.println("VALOR UNITÁRIO= "+ elementoFilho.getTextContent());  //getTextContent
//                                    modXML.setVlrUniProd_vUnCom(Float.parseFloat(elementoFilho.getTextContent()));
                                    break;

                                    case "vProd":
                                    System.out.println("VALOR TOTAL APLICADO= "+ elementoFilho.getTextContent());  //getTextContent
//                                    modXML.setVlrTotalProd_vProd(Float.parseFloat(elementoFilho.getTextContent()));
                                    break;

                                    case "xPed":
                                    System.out.println("NÚMERO DO PEDIDO= "+ elementoFilho.getTextContent());  //getTextContent
//                                    modXML.setNpcrnf_xPed(elementoFilho.getTextContent());

                                    break;

                                    default:

                                    break;
                                    
                                }

                            }

                        }
                      
                    }
//                  addProduto(modXML);
                }
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        ct_nome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        ct_nome1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        ct_nome2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        ct_nome3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ct_nome4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        ct_nome5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ct_nome.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_nome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Nome:");

        ct_nome1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_nome1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Nome:");

        ct_nome2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_nome2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Nome:");

        ct_nome3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_nome3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Nome:");

        ct_nome4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_nome4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Nome:");

        ct_nome5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ct_nome5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Nome:");

        jLayeredPane1.setLayer(ct_nome, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(ct_nome1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(ct_nome2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(ct_nome3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(ct_nome4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(ct_nome5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(ct_nome5, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(ct_nome4, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(ct_nome3, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(ct_nome2, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(ct_nome1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(ct_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ct_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ct_nome1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ct_nome2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ct_nome3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ct_nome4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ct_nome5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new test().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ct_nome;
    private javax.swing.JTextField ct_nome1;
    private javax.swing.JTextField ct_nome2;
    private javax.swing.JTextField ct_nome3;
    private javax.swing.JTextField ct_nome4;
    private javax.swing.JTextField ct_nome5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLayeredPane jLayeredPane1;
    // End of variables declaration//GEN-END:variables
}
