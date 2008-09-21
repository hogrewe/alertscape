/**
 * 
 */
package com.alertscape.tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.Alert.AlertStatus;
import com.alertscape.common.model.severity.SeverityFactory;

public class GenerateEvents implements Runnable {
  private static final int NUM_EVENTS_TO_CACHE = 30000;
  private long id = 1000000;
  private SeverityFactory sevFactory = SeverityFactory.getInstance();
  private Random rand = new Random();
  private AlertSource source = new AlertSource(1, "Source 1");
  private AlertCollection c;
  private List<Alert> newEvents = new ArrayList<Alert>(NUM_EVENTS_TO_CACHE);
  private List<String> words;
  private int wordCount;
  private AlertGenerator[] generators;

  private String[] itemLookup;
  private String[] itemManagerLookup;
  private String[] itemManagerTypeLookup;
  private String[] itemTypeLookup;
  private String[] longDescriptionLookup;
  private String[] shortDescriptionLookup;
  private String[] typeLookup;

  public GenerateEvents(AlertCollection collection) {
    c = collection;
    generators = new AlertGenerator[3];
    generators[0] = new RandomTransportAlertGenerator();
    generators[1] = new IPAlertGenerator();
    generators[2] = new NagiosAlertGenerator();
    // initLookupTable();
  }

  public Alert buildNewEvent() {
    Alert a = null;
    AlertGenerator gen = generators[rand.nextInt(generators.length)];
    try {
      a = gen.readAlert();
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    if (a == null) {
      a = buildRandomEvent();
    }

    int sevLevel = rand.nextInt(sevFactory.getNumSeverities());
    a.setCount(rand.nextInt(1000));
    a.setAlertId(id++);
    a.setFirstOccurence(new Date());
    a.setLastOccurence(new Date());
    a.setSeverity(sevFactory.getSeverity(sevLevel));
    a.setSource(source);
    a.setStatus(Alert.AlertStatus.STANDING);

    return a;
  }

  private Alert buildRandomEvent() {
    int sevLevel = rand.nextInt(sevFactory.getNumSeverities());
    Alert e = new Alert();
    e.setCount(rand.nextInt(1000));
    e.setAlertId(id++);
    e.setFirstOccurence(new Date());
    e.setItem(itemLookup[rand.nextInt(itemLookup.length)]);
    e.setItemManager(itemManagerLookup[rand.nextInt(itemManagerLookup.length)]);
    e.setItemManagerType(itemManagerTypeLookup[rand.nextInt(itemManagerTypeLookup.length)]);
    e.setItemType(itemTypeLookup[rand.nextInt(itemTypeLookup.length)]);
    e.setLastOccurence(new Date());
    e.setLongDescription(longDescriptionLookup[rand.nextInt(longDescriptionLookup.length)]);
    e.setSeverity(sevFactory.getSeverity(sevLevel));
    e.setShortDescription(shortDescriptionLookup[rand.nextInt(shortDescriptionLookup.length)]);
    e.setSource(source);
    e.setStatus(Alert.AlertStatus.STANDING);
    e.setType(typeLookup[rand.nextInt(typeLookup.length)]);

    return e;
  }

  private Alert buildUpdateToExistingEvent(List<Alert> events) {
    Alert e = new Alert();
    if (events.size() > 1) {
      int eventIndex = rand.nextInt(events.size() - 1);
      Alert old = events.get(eventIndex);

      e.setCount(old.getCount() + 1);
      e.setAlertId(old.getAlertId());
      e.setFirstOccurence(old.getFirstOccurence());
      e.setItem(old.getItem());
      e.setItemManager(old.getItemManager());
      e.setItemManagerType(old.getItemManagerType());
      e.setItemType(old.getItemType());
      e.setLastOccurence(new Date());
      e.setLongDescription(old.getLongDescription());
      e.setSeverity(old.getSeverity());
      e.setShortDescription(old.getShortDescription());
      e.setSource(old.getSource());
      if (rand.nextBoolean()) {
        e.setStatus(AlertStatus.STANDING);
        System.out.print("U");
      } else {
        e.setStatus(AlertStatus.CLEARED);
        System.out.print("C");
      }
      e.setType(old.getType());
    } else {
      e = buildNewEvent();
      System.out.print("N");
    }
    return e;
  }

  public void run() {
    Random rand = new Random();
    List<Alert> allEvents = new ArrayList<Alert>(1000);
    allEvents.addAll(c.getEventList());
    for (int i = 0; i < NUM_EVENTS_TO_CACHE; i++) {
      boolean val = rand.nextBoolean();

      Alert e = null;

      if (val) {
        e = buildNewEvent();
        System.out.print("N");
      } else {
        e = buildUpdateToExistingEvent(allEvents);
      }

      newEvents.add(e);
      allEvents.add(e);
    }

    // The place we are in the event queue
    int newEventPointer = 0;
    while (true) {
      int groupSize = rand.nextInt(100);
      List<Alert> events = new ArrayList<Alert>();
      for (int i = 0; i < groupSize; i++) {
        if (newEventPointer + i + 1 >= newEvents.size()) {
          return;
        }
        events.add(newEvents.get(newEventPointer + i));
      }
      newEventPointer += groupSize;
      c.processAlerts(events);
      System.out.println("Processed: " + events.size() + " events");

      try {
        Thread.sleep(2000);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
      Runtime runtime = Runtime.getRuntime();
      long totalMemory = runtime.totalMemory() / (1024 * 1024);
      long freeMemory = runtime.freeMemory() / (1024 * 1024);

      System.out.println("Memory: " + totalMemory + "MB, Free: " + freeMemory + "MB");
    }
  }

  private String createRandomPhrase(int length) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < length; i++) {
      String word = words.get(rand.nextInt(wordCount));
      builder.append(word);
      builder.append(" ");
    }

    return builder.toString();
  }

  private String[] createPhraseLookup(int phraseLength, int wordLength) {
    String[] phrases = new String[phraseLength];

    for (int i = 0; i < phraseLength; i++) {
      phrases[i] = createRandomPhrase(wordLength);
    }

    return phrases;
  }

  @SuppressWarnings("unused")
  private void initLookupTable() {
    initWordList();

    itemLookup = createPhraseLookup(100, 2);
    itemManagerLookup = createPhraseLookup(100, 2);
    itemManagerTypeLookup = createPhraseLookup(100, 2);
    itemTypeLookup = createPhraseLookup(100, 2);
    longDescriptionLookup = createPhraseLookup(50, 15);
    shortDescriptionLookup = createPhraseLookup(100, 5);
    typeLookup = createPhraseLookup(100, 2);

    words = null;
    System.gc();
  }

  private void initWordList() {
    words = new ArrayList<String>(250000);
    InputStream is = getClass().getResourceAsStream("/words");
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader br = new BufferedReader(isr);

    String word;
    try {
      while ((word = br.readLine()) != null) {
        words.add(word);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    wordCount = words.size() - 1;
  }
}
