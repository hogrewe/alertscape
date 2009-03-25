/**
 * 
 */
package com.alertscape.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;

import com.alertscape.app.license.AL;
import com.alertscape.app.license.LicenseHelper;
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
  private Set<AlertOnramp> onramps = new HashSet<AlertOnramp>();

  public void init() {
    LicenseHelper helper = new LicenseHelper();
    try {
      helper.install(asHome + File.separator + "alertscape.license");
      AL license = helper.getLicense();
      initOnramps(license.getOn());
    } catch (Exception e) {
      StringBuilder b = new StringBuilder();
      b.append("Inv");
      b.append("alid li");
      b.append("cen");
      b.append("se f");
      b.append("ile");
      b.append(": ");
      b.append(e.getLocalizedMessage());
      LOG.error(b);
    }
  }

  public void shutdown() {
    for (AlertOnramp onramp : onramps) {
      onramp.shutdown();
    }
  }

  public void initOnramps(int allowed) {
    LocalAlertTransport t = new LocalAlertTransport();
    t.setPump(pump);

    transport = t;

    List<AlertSource> sources;
    try {
      sources = sourceDao.getAllSources();
      int onrampsConfigured = 0;
      for (AlertSource alertSource : sources) {
        if (alertSource.isActive()) {
          onrampsConfigured++;
          if (onrampsConfigured > allowed) {
            StringBuilder b = new StringBuilder();
            b.append("Lic");
            b.append("ense all");
            b.append("ows o");
            b.append("nly " + allowed);
            b.append(" onr");
            b.append("amps b");
            b.append("ut mor");
            b.append("e are con");
            b.append("figured. ");
            b.append(" Not sta");
            b.append("rting rem");
            b.append("aining o");
            b.append("nramps");
            LOG.error(b);
            return;
          }
          try {
            AlertOnramp onramp = createOnramp(alertSource);
            onramp.onrampInit();
            onramps.add(onramp);
          } catch (Exception e) {
            LOG.error("Couldn't initialize onramp " + alertSource, e);
          }
        }
      }
    } catch (Exception e) {
      LOG.error("Couldn't initialize onramps", e);
    }
  }

  protected AlertOnramp createOnramp(AlertSource source) throws IOException, MappingException, MarshalException,
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

    return onramp;
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
