package iaws.covoiturage.services.impl;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.io.ClassPathResource;

public class XmlHelper {
	
	/**
     * Return the dom root element of an xml file
     * @param filePathInClassPath  the file path relative to the classpath
     * @return  the root element
	 * @throws JDOMException 
     * @throws IOException
     */
    public static Element getRootElementFromFileInClasspath(String filePathInClassPath) throws JDOMException, IOException{    	
    	SAXBuilder builder = new SAXBuilder();
    	
    	Document document = (Document) builder.build(new ClassPathResource(filePathInClassPath).getInputStream());
    	return document.getRootElement();
    }

}
