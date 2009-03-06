/**
 * 
 */
package com.alertscape.browser.model.tree;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import junit.framework.TestCase;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;

/**
 * @author josh
 *
 */
public class TreeMappingTest extends TestCase {
  public void testMapping() throws Exception {
    XMLContext context = new XMLContext();
    Mapping mapping = new Mapping();
    URL mappingUrl = getClass().getResource("/treeConfigMapping.xml");
    mapping.loadMapping(mappingUrl);
    context.addMapping(mapping);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    InputStream is = getClass().getResourceAsStream("/treeMappingTest.xml");
    AlertTreeNode node = (AlertTreeNode) unmarshaller.unmarshal(new BufferedReader(new InputStreamReader(is)));
    System.out.println(node);
  }
  
  public void testMarshalling() throws Exception {
    XMLContext context = new XMLContext();
    Mapping mapping = new Mapping();
    URL mappingUrl = getClass().getResource("/treeConfigMapping.xml");
    mapping.loadMapping(mappingUrl);
    context.addMapping(mapping);

    Marshaller m = context.createMarshaller();
    DefaultAlertTreeNode node = new DefaultAlertTreeNode("blah");
    node.setMatcher(new FieldAlertMatcher("item", "test"));
    StringWriter stringWriter = new StringWriter();
    m.setWriter(stringWriter);
    m.marshal(node);
    System.out.println(stringWriter.getBuffer());
  }
}
