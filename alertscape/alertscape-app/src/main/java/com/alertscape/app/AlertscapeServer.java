/**
 * 
 */
package com.alertscape.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.AlertSource;
import com.alertscape.dao.AlertSourceDao;
import com.alertscape.pump.AlertPump;
import com.alertscape.pump.onramp.AlertOnramp;
import com.alertscape.pump.onramp.sender.AlertTransport;
import com.alertscape.pump.onramp.sender.LocalAlertTransport;

/**
 * @author josh
 * 
 */
public class AlertscapeServer {
  private static final ASLogger LOG = ASLogger.getLogger(AlertscapeServer.class);
  private AlertPump pump;
  private AlertTransport transport;
  private String asHome;
  private AlertSourceDao sourceDao;
  
  public void init() {
    initOnramps();
  }
  
  public void shutdown() {
    
  }

  public void initOnramps() {
    LocalAlertTransport t = new LocalAlertTransport();
    t.setPump(pump);

    transport = t;

    List<AlertSource> sources;
    try {
      sources = sourceDao.getAllSources();
      for (AlertSource alertSource : sources) {
        if (alertSource.isActive()) {
          try {
            createOnramp(alertSource);
          } catch (Exception e) {
            LOG.error("Couldn't initialize onramp " + alertSource, e);
          }
        }
      }
    } catch (Exception e) {
      LOG.error("Couldn't initialize onramps", e);
    }
  }

  protected void createOnramp(AlertSource source) throws IOException, MappingException, MarshalException,
      ValidationException {
    XMLContext context = new XMLContext();
    Mapping mapping = new Mapping();
    URL mappingUrl = getClass().getResource("/" + source.getType().getMappingFile());
    mapping.loadMapping(mappingUrl);
    context.addMapping(mapping);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    String configXml = source.getConfigXml();
    AlertOnramp onramp = (AlertOnramp) unmarshaller.unmarshal(new BufferedReader(new StringReader(configXml)));
    onramp.setTransport(transport);
    onramp.setAsHome(asHome);
    onramp.setSource(source);

    onramp.onrampInit();
  }

  /**
   * @return the pump
   */
  public AlertPump getPump() {
    return pump;
  }

  /**
   * @param pump
   *          the pump to set
   */
  public void setPump(AlertPump pump) {
    this.pump = pump;
  }

  /**
   * @return the transport
   */
  public AlertTransport getTransport() {
    return transport;
  }

  /**
   * @param transport
   *          the transport to set
   */
  public void setTransport(AlertTransport transport) {
    this.transport = transport;
  }

  /**
   * @return the asHome
   */
  public String getAsHome() {
    return asHome;
  }

  /**
   * @param asHome
   *          the asHome to set
   */
  public void setAsHome(String asHome) {
    this.asHome = asHome;
  }

  /**
   * @return the sourceDao
   */
  public AlertSourceDao getSourceDao() {
    return sourceDao;
  }

  /**
   * @param sourceDao
   *          the sourceDao to set
   */
  public void setSourceDao(AlertSourceDao sourceDao) {
    this.sourceDao = sourceDao;
  }

}
