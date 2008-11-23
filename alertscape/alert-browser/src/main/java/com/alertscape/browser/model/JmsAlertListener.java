/**
 * 
 */
package com.alertscape.browser.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;

/**
 * @author josh
 * 
 */
public class JmsAlertListener implements AlertListener {

  private AlertCollection collection;
  private ConnectionFactory factory;
  private Connection connection;
  private Session session;
  private Destination topic;
  private MessageConsumer consumer;
  private MessageListener listener;

  public void init() throws AlertscapeException {
    try {
      connection = factory.createConnection();
      connection.start();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      
      // TODO: this should be injected
      topic = session.createTopic("com.alertscape.pump.Alerts");
      
      consumer = session.createConsumer(topic);
      listener = new AlertMessageListener();
      consumer.setMessageListener(listener);
    } catch (JMSException e) {
      throw new AlertscapeException("Couldn't initialize JMS topic", e);
    }
  }

  public void shutdown() throws AlertscapeException {
    try {
      connection.close();
    } catch (JMSException e) {
      throw new AlertscapeException("Couldn't close JMS connection", e);
    }

  }

  public void setCollection(AlertCollection collection) {
    this.collection = collection;
  }

  public void startListening() throws AlertscapeException {
    init();
  }

  /**
   * @return the topic
   */
  public Destination getTopic() {
    return topic;
  }

  /**
   * @param topic
   *          the topic to set
   */
  public void setTopic(Destination queue) {
    this.topic = queue;
  }

  /**
   * @author josh
   * 
   */
  private final class AlertMessageListener implements MessageListener {
    public void onMessage(Message message) {
      if (message instanceof ObjectMessage) {
        ObjectMessage om = (ObjectMessage) message;
        try {
          Serializable object = om.getObject();
          if (object instanceof Alert) {
            Alert alert = (Alert) object;
            if (collection != null) {
              collection.processAlerts(Arrays.asList(alert));
            }
          }
        } catch (JMSException e) {
          ASLogger.error("Couldn't get object from JMS message", e);
        }
      }
    }
  }

  /**
   * @return the factory
   */
  public ConnectionFactory getFactory() {
    return factory;
  }

  /**
   * @param factory the factory to set
   */
  public void setFactory(ConnectionFactory factory) {
    this.factory = factory;
  }
}
