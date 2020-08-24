package com.medas.mi.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Munquasim Alam
 *
 */
public class XmlUtil {
	private static final Logger logger = Logger.getLogger(XmlUtil.class);
	public static Document doc = null;
	public static Element  setting = null;

//	public static Map<String, String> readXmlFile(String path) {
//		Map<String, String> settings = new HashMap<String, String>();
//		try {
//			// File fXmlFile = new File("settings/CobasB221.xml");
//			File fXmlFile = new File(path);
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
//			doc.getDocumentElement().normalize();
//			NodeList nList = doc.getElementsByTagName("setting");
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//				Node nNode = nList.item(temp);
//				Element eElement = (Element) nNode;
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//					settings.put("machineName", eElement.getElementsByTagName("machineName").item(0).getTextContent());
//					settings.put("machineId", eElement.getElementsByTagName("machineId").item(0).getTextContent());
//					settings.put("comPort", eElement.getElementsByTagName("comPort").item(0).getTextContent());
//					settings.put("bitsPerSecond",
//							eElement.getElementsByTagName("bitsPerSecond").item(0).getTextContent());
//					settings.put("parity", eElement.getElementsByTagName("parity").item(0).getTextContent());
//					settings.put("stopBit", eElement.getElementsByTagName("stopBit").item(0).getTextContent());
//					settings.put("dataBits", eElement.getElementsByTagName("dataBits").item(0).getTextContent());
//				}
//			}
//		} catch (Exception ex) {
//			System.out.println("readXmlFile Exception:"+ ex.getMessage());
//			logger.error("readXmlFile Exception:", ex);
//		}
//
//		return settings;
//	}
	
	public static Map<Integer, Map<String, String>> readXmlFile(String path) {
		Map<Integer, Map<String, String>> settings = new HashMap<Integer, Map<String, String>>();
		Map<String, String> setting = null;
		try {
			// File fXmlFile = new File("settings/CobasB221.xml");
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("setting");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;
				if(eElement.getElementsByTagName("machineName").item(0) != null) {
					
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					setting = new HashMap<String, String>();
					setting.put("machineName", eElement.getElementsByTagName("machineName").item(0).getTextContent());
					setting.put("machineId", eElement.getElementsByTagName("machineId").item(0).getTextContent());
					setting.put("comPort", eElement.getElementsByTagName("comPort").item(0).getTextContent());
					setting.put("bitsPerSecond",
							eElement.getElementsByTagName("bitsPerSecond").item(0).getTextContent());
					setting.put("parity", eElement.getElementsByTagName("parity").item(0).getTextContent());
					setting.put("stopBit", eElement.getElementsByTagName("stopBit").item(0).getTextContent());
					setting.put("dataBits", eElement.getElementsByTagName("dataBits").item(0).getTextContent());
					settings.put(temp,setting);
					
				}
			}
			}
		} catch (Exception ex) {
			System.out.println("readXmlFile Exception:"+ ex.getMessage());
			logger.error("readXmlFile Exception:", ex);
		}
		
		return settings;
	}

//	public static void writeXmlFile(Map<String, String> settingsMap) {
//		try {
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//			Document doc = docBuilder.newDocument();
//			Element rootElement = doc.createElement("settings");
//			doc.appendChild(rootElement);
//			Element setting = doc.createElement("setting");
//			rootElement.appendChild(setting);
//			for (Entry<String, String> entry : settingsMap.entrySet()) {
//				Element element = doc.createElement(entry.getKey());
//				element.appendChild(doc.createTextNode(entry.getValue()));
//				setting.appendChild(element);
//			}
//			// write the content into xml file
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			DOMSource source = new DOMSource(doc);
//			StreamResult result = new StreamResult(new File("settings/" + settingsMap.get("machineName") + ".xml"));
//			transformer.transform(source, result);
//			System.out.println("File saved!");
//		} catch (ParserConfigurationException pce) {
//			System.out.println("writeXmlFile ParserConfigurationException:"+ pce.getMessage());
//			logger.error("writeXmlFile ParserConfigurationException:", pce);
//	
//		} catch (TransformerException tfe) {
//			System.out.println("writeXmlFile TransformerException:"+ tfe.getMessage());
//			logger.error("writeXmlFile ParserConfigurationException:", tfe);
//	
//		}
//	}
	
	public static void writeXmlFile(Map<Integer, Map<String, String>> machineNames) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			 doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("settings");
			doc.appendChild(rootElement);
			if(machineNames.size() == 0) {
			   setting = doc.createElement("setting");
				rootElement.appendChild(setting);
			}
			
			machineNames.forEach((k,v) -> {
				System.out.println("key: " + k +" Value :" + v);
				if(v != null){
					 setting = doc.createElement("setting");
					rootElement.appendChild(setting);

					for (Entry<String, String> entry : v.entrySet()) {
						Element element = doc.createElement(entry.getKey());
						element.appendChild(doc.createTextNode(entry.getValue()));
						setting.appendChild(element);
					}
				} 
				
				if(v == null){
					 setting = doc.createElement("setting");
					rootElement.appendChild(setting);
					
			       }
				
			});
			
			
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("settings/permanentSetting.xml"));
			transformer.transform(source, result);
			System.out.println("File saved!");
			transformer = null;
			doc = null;
			source = null;
			result = null;
			
		} catch (ParserConfigurationException pce) {
			System.out.println("writeXmlFile ParserConfigurationException:"+ pce.getMessage());
			logger.error("writeXmlFile ParserConfigurationException:", pce);
	
		} catch (TransformerException tfe) {
			System.out.println("writeXmlFile TransformerException:"+ tfe.getMessage());
			logger.error("writeXmlFile ParserConfigurationException:", tfe);
	
		}
	}


	/*public static List<File> readFilesInFolder() {
		List<File> filesInFolder = null;

		try {
			filesInFolder = Files.walk(Paths.get("settings")).filter(Files::isRegularFile).map(Path::toFile)
					.collect(Collectors.toList());
		} catch (IOException ex) {
			System.out.println("writeXmlFile IOException:"+ ex.getMessage());
			logger.error("writeXmlFile IOException:", ex);
	
		}
		return filesInFolder;
	}*/

}
