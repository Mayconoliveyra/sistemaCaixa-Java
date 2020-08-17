/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Visao.test;
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
public class TestSParseNota {
  
    public void parseChave (){
        
    }
    
    public static void main(String[] args) {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          
              try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("C:\\MartisXML.xml");

            NodeList listaDeProdutos = doc.getElementsByTagName("infNFe");  //PEGA O NOME NOME DA LISTA
            Node noChave = listaDeProdutos.item(0);
            Element elementoChave = (Element) noChave; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
            String chave= elementoChave.getAttribute("Id"); // SET ATRIBUTO (nItem = id)
                System.out.println("Chave= "+ chave);
            int tamanhoLista= listaDeProdutos.getLength(); //TAMANHO DA LISTA
            for(int i=0; i<tamanhoLista; i++){  //ANDA TODA A LISTA
            Node noItem = listaDeProdutos.item(i);  //PEGA TODOS ITENS DA LISTA PROCURANDO TODOS OS "det" (det = Nome do produto no XML)
             
            if(noItem.getNodeType()==Node.ELEMENT_NODE){ // VERIFICA SE O DET E UM ELEMENTO 
                Element elementoItem = (Element) noItem; // CONVETE DO TIPO NÓ PARA ELEMENTO (PARA TER MAIS METODOS
                
//                String id= elementoItem.getAttribute("nItem"); // SET ATRIBUTO (nItem = id)
//                jTextField1.setText(id);
//                System.out.println("ID"+ id);
                
                  NodeList listaDeFilhosItem = elementoItem.getChildNodes(); // PEGA TODOS OS NÓS FILHOS 
                  
                  int tamanhoListaFilhos = listaDeFilhosItem.getLength(); // PEGA A QUANTIDADE DE NÓS FILHOS 
                  for (int j = 0; j < tamanhoListaFilhos; j++) {
                    
                       Node noFilho = listaDeFilhosItem.item(j);  // SETA TODOS OS NO FILHOS
                       if(noFilho.getNodeType()== Node.ELEMENT_NODE){ // VERIFICA SE É ELEMENTO

                           Element elementoFilho = (Element) noFilho; // CONVETE TODOS OS NÓS EM ELEMENTO
                                
                           switch (elementoFilho.getTagName()){// getTagNome METODO QUE FUNCIONA COMO VARIAVEL 
                                
//                               case "cProd":
                               case "ide":
//                                  System.out.println();
//                                  System.out.println("ID= "+ elementoFilho.getTextContent());  //getTextContent    
                                  System.out.println("ide");
                               break;
//                               case "xProd":
                               case "dest":
//                                  System.out.println("Nome= "+ elementoFilho.getTextContent());  //getTextContent   
                                   System.out.println("Dest");
                               break;
                               case "xPed":
                                  System.out.println("CodBarra= "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "vProd":
                                  System.out.println("NCMe= "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "NCM":
                                  System.out.println("NCM= "+ elementoFilho.getTextContent());  //getTextContent    
                               break;
                               case "CEST":
                                  System.out.println("CEST= "+ elementoFilho.getTextContent());  //getTextContent    
                               break;                               
                               default:
                                   break;
                               
                           }
                       }
                }
                  
            }
        }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        
    
    
}
