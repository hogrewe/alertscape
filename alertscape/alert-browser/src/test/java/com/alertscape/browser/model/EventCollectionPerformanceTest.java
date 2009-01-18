/*
 * Created on Sep 30, 2006
 */
package com.alertscape.browser.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.BinarySortAlertCollection;
import com.alertscape.common.performance.PerformanceRun;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionPerformanceTest extends TestCase {
  private static final ASLogger LOG = ASLogger.getLogger(EventCollectionPerformanceTest.class);
  public static final int NUM_EVENTS = 200;
  public static final int BLOCK_SIZE = 500;

  private static final Random rand = new Random();

  // public void testInsertsCollection( )
  // {
  // EventCollection c = new IndexedEventCollection( );
  //
  // List<Event> events = buildEventList(NUM_EVENTS, false, null);
  //
  // PerformanceRun run = new PerformanceRun( );
  // run.setNumOperations(NUM_EVENTS);
  //
  // run.start( );
  //
  // sendEvents(events, c);
  //
  // run.end( );
  // LOG
  // .info("Performance: " + run.getOperationsPerMilli( ) + " events/ms");
  // }

  public void testSteadyStateCollection() {
//    AlertCollection c = new IndexedAlertCollection();
     AlertCollection c = new BinarySortAlertCollection( );

    // Send the inserts to get it built up
    List<Alert> events = buildEventList(NUM_EVENTS, false, null);

    PerformanceRun run = new PerformanceRun();
    run.setNumOperations(NUM_EVENTS);

    run.start();

    sendEvents(events, c);

    run.end();
    LOG.info("Insert performance: " + run.getOperationsPerMilli() + " events/ms");

    // Now send some updates, insert, removes
    events = buildEventList(NUM_EVENTS, true, c);
    run = new PerformanceRun();
    run.setNumOperations(NUM_EVENTS);

    run.start();

    sendEvents(events, c);

    run.end();
    LOG.info("Steady state performance: " + run.getOperationsPerMilli() + " events/ms");
  }

  private void sendEvents(List<Alert> events, AlertCollection c) {
    List<Alert> eventsToSend = new ArrayList<Alert>(BLOCK_SIZE);
    for (int i = 0; i < NUM_EVENTS; i++) {
      eventsToSend.add(events.get(i));
      // If we have BLOCK_SIZE number of events, send them and clear them out
      if (eventsToSend.size() >= BLOCK_SIZE) {
        c.processAlerts(eventsToSend);
        eventsToSend.clear();
      }
    }

    // Check to see if we have any leftover events to send
    if (eventsToSend.size() > 0) {
      c.processAlerts(eventsToSend);
    }

  }

  private List<Alert> buildEventList(int size, boolean includeUpdates, AlertCollection c) {
    List<Alert> events = new ArrayList<Alert>(size);

    while (events.size() < size) {
      Alert e = null;
      if (includeUpdates) {
        if (rand.nextBoolean() == true && c.getAlertCount() > 0) {
          Alert old = c.getAlertAt(rand.nextInt(c.getAlertCount()));
          e = FakeEventGenerator.buildUpdateToExistingEvent(old);
        } else {
          e = FakeEventGenerator.buildNewEvent();
        }
      } else {
        e = FakeEventGenerator.buildNewEvent();
      }
      events.add(e);
    }

    return events;
  }
}
