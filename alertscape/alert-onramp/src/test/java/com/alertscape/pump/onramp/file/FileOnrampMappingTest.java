/**
 * 
 */
package com.alertscape.pump.onramp.file;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.TestCase;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;

import com.alertscape.pump.onramp.AlertOnramp;

/**
 * @author josh
 * 
 */
public class FileOnrampMappingTest extends TestCase {

  public void testMapping() throws Exception {
    XMLContext context = new XMLContext();
    Mapping mapping = new Mapping();
    URL mappingUrl = getClass().getResource("/fileOnrampMapping.xml");
    mapping.loadMapping(mappingUrl);
    context.addMapping(mapping);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    InputStream is = getClass().getResourceAsStream("/fileOnrampConfig.xml");
    AlertOnramp onramp = (AlertOnramp) unmarshaller.unmarshal(new BufferedReader(new InputStreamReader(is)));
    System.out.println(onramp);
  }
}
