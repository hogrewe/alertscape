/*
 * Created on Oct 22, 2006
 */
package com.alertscape.cev.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.util.concurrent.Lock;

import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class SeverityCounter
{
  private Map<Severity, Integer> severityCounts = new HashMap<Severity, Integer>(
      SeverityFactory.getInstance( ).getNumSeverities( ));
  private List<Alert> existingEvents = new ArrayList<Alert>(70000);
  private AlertCollection collection;
  private ListEventListener<Alert> eventListener = new CounterEventListener( );

  // This will do for now, but we should maybe have a specialized event for this
  private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

  public SeverityCounter( )
  {
    SeverityFactory sevFactory = SeverityFactory.getInstance( );
    for (int i = 0, size = sevFactory.getNumSeverities( ); i < size; i++)
    {
      Severity sev = sevFactory.getSeverity(i);
      severityCounts.put(sev, 0);
    }

  }

  public void setEventCollection(AlertCollection collection)
  {
    if (this.collection != collection)
    {
      // Clear out the events we know about
      existingEvents.clear( );

      // If we had a collection stop listening to it
      if (this.collection != null)
      {
        this.collection.getEventList( ).removeListEventListener(eventListener);
      }

      // If the new collection is not null, initialize our counts and start
      // listening to it's events
      if (collection != null)
      {
        EventList<Alert> eventList = collection.getEventList( );
        Lock lock = eventList.getReadWriteLock( ).readLock( );
        lock.lock( );
        try
        {
          eventList.addListEventListener(eventListener);
          existingEvents.addAll(eventList);
          for (Alert event : eventList)
          {
            int count = severityCounts.get(event.getSeverity( ));
            count++;
            severityCounts.put(event.getSeverity( ), count);
          }

        }
        finally
        {
          lock.unlock( );
        }
      }
    }
    this.collection = collection;
  }

  /**
   * This is not the best way to do this, but currently, the listener should
   * just listen for any property change and query the counter for the actual
   * counts.
   * 
   * @param listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    changeSupport.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    changeSupport.removePropertyChangeListener(listener);
  }

  public int getCount(Severity s)
  {
    int count = 0;
    Integer fatCount = severityCounts.get(s);
    if (fatCount != null)
    {
      count = fatCount;
    }
    return count;
  }

  public int getTotalCount( )
  {
    return collection == null ? 0 : collection.getAlertCount( );
  }

  private class CounterEventListener implements ListEventListener<Alert>
  {
    public void listChanged(ListEvent<Alert> listChanges)
    {
      EventList<Alert> list = listChanges.getSourceList( );
      Lock lock = list.getReadWriteLock( ).readLock( );
      lock.lock( );
      Alert e;
      Severity s;
      int count;
      while (listChanges.next( ))
      {
        int index = listChanges.getIndex( );

        assert (index < list.size( ) && index >= 0);

        switch (listChanges.getType( ))
        {
          case ListEvent.INSERT:
            e = list.get(listChanges.getIndex( ));
            existingEvents.add(listChanges.getIndex( ), e);
            s = e.getSeverity( );
            count = severityCounts.get(s);
            count++;
            severityCounts.put(s, count);
            break;
          case ListEvent.DELETE:
            e = existingEvents.get(listChanges.getIndex( ));
            s = e.getSeverity( );
            count = severityCounts.get(s);
            count--;
            severityCounts.put(s, count);
            break;
          case ListEvent.UPDATE:
            e = existingEvents.get(listChanges.getIndex( ));
            Alert newEvent = list.get(listChanges.getIndex( ));
            existingEvents.set(listChanges.getIndex( ), newEvent);
            if (newEvent.getSeverity( ) != e.getSeverity( ))
            {
              // Decrement the old severity
              count = severityCounts.get(e.getSeverity( ));
              count--;
              severityCounts.put(e.getSeverity( ), count);

              // Increment the new severity
              count = severityCounts.get(newEvent.getSeverity( ));
              count++;
              severityCounts.put(newEvent.getSeverity( ), count);
            }
            break;
        }
      }
      lock.unlock( );

      changeSupport.firePropertyChange("SEVCOUNTS", false, true);
    }
  }
}
