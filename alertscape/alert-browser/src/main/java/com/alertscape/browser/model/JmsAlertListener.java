/**
 * 
 */
package com.alertscape.browser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
  private static final ASLogger LOG = ASLogger.getLogger(JmsAlertListener.class);

  private AlertCollection collection;
  private ConnectionFactory factory;
  private Connection connection;
  private Session session;
  private Destination topic;
  private MessageConsumer consumer;
  private MessageListener listener;
  private List<Alert> alertQueue = new ArrayList<Alert>();
  private AlertProcessor processor;

  public void init() throws AlertscapeException {
    try {
      processor = new AlertProcessor();
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

  public void startProcessing() throws AlertscapeException {
    processor.start();
  }

  public void stopProcessing() throws AlertscapeException {
    processor.setRunning(false);
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
            synchronized (alertQueue) {
              alertQueue.add(alert);
            }
          }
        } catch (JMSException e) {
          LOG.error("Couldn't get object from JMS message", e);
        }
      }
    }
  }

  private final class AlertProcessor extends Thread {
    private boolean running;

    @Override
    public void run() {
      running = true;
      while (running) {
        synchronized (alertQueue) {
          if (!alertQueue.isEmpty()) {
            collection.processAlerts(alertQueue);
            alertQueue.clear();
          }
        }

        synchronized (this) {
          try {
            wait(3000);
          } catch (InterruptedException e) {
          }
        }
      }
    }

    public void setRunning(boolean running) {
      this.running = running;
      synchronized (this) {
        notifyAll();
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
}
