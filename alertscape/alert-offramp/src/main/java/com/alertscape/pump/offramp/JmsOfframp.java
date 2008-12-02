/**
 * 
 */
package com.alertscape.pump.offramp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import com.alertscape.AlertscapeException;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;

/**
 * @author josh
 * 
 */
public class JmsOfframp implements AlertOfframp {
  private ConnectionFactory factory;
  private Topic topic;
  private Connection connection;
  private Session session;
  private MessageProducer producer;

  public void init() throws AlertscapeException {
    try {
      connection = factory.createConnection();
      connection.start();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      producer = session.createProducer(topic);
    } catch (JMSException e) {
      throw new AlertscapeException("Couldn't initialize JMS", e);
    }

  }

  public void shutdown() throws AlertscapeException {
    try {
      connection.close();
    } catch (JMSException e) {
      ASLogger.error("Couldn't close JMS connection", e);
    }
  }

  public void processAlert(Alert alert) throws AlertscapeException {
    try {
      ObjectMessage message = session.createObjectMessage(alert);

      producer.send(message);
    } catch (JMSException e) {
      shutdown();
      init();
      try {
        ObjectMessage message = session.createObjectMessage(alert);

        producer.send(message);
      } catch (JMSException e1) {
        throw new AlertscapeException("Couldn't send alert after attempted reconnect", e);
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
   * @param factory
   *          the factory to set
   */
  public void setFactory(ConnectionFactory factory) {
    this.factory = factory;
  }

  /**
   * @return the topic
   */
  public Topic getTopic() {
    return topic;
  }

  /**
   * @param topic
   *          the topic to set
   */
  public void setTopic(Topic topic) {
    this.topic = topic;
  }
}
