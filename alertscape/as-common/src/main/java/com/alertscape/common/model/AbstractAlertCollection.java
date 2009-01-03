/*
 * Created on Oct 17, 2006
 */
package com.alertscape.common.model;

import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.util.concurrent.Lock;


/**
 * @author josh
 * @version $Version: $
 */
public abstract class AbstractAlertCollection implements AlertCollection
{
  protected EventList<Alert> alerts;

  // protected FreezableList<Event> freezableEvents;

  public AbstractAlertCollection( )
  {
    setEventList(new BasicEventList<Alert>(70000));
  }

  public AbstractAlertCollection(EventList<Alert> eventList)
  {
    setEventList(eventList);
  }

  public void clearAlerts( )
  {
    Lock lock = alerts.getReadWriteLock( ).writeLock( );
    try
    {
      lock.lock( );
      alerts.clear( );
    }
    finally
    {
      lock.unlock( );
    }
  }

  public Alert getAlert(long id)
  {
    Lock lock = alerts.getReadWriteLock( ).readLock( );
    try
    {
      lock.lock( );
      for (Alert e : alerts)
      {
        if (e.getAlertId( ) == id)
        {
          return e;
        }
      }
    }
    finally
    {
      lock.unlock( );
    }

    return null;
  }

  public Alert getAlertAt(int index)
  {
    Lock lock = alerts.getReadWriteLock( ).readLock( );
    try
    {
      lock.lock( );
      return alerts.get(index);
    }
    finally
    {
      lock.unlock( );
    }
  }

  public int getAlertCount( )
  {
    Lock lock = alerts.getReadWriteLock( ).readLock( );
    try
    {
      lock.lock( );
      return alerts.size( );
    }
    finally
    {
      lock.unlock( );
    }
  }

  public EventList<Alert> getEventList( )
  {
    // TODO: Do we need to pass back the modifiable list? We may need to wrap
    // this list in other lists and have those lists be transformable
    return alerts;
  }

  public void setEventList(EventList<Alert> list)
  {
    // TODO: should we do some sort of defensive copying?!?
    this.alerts = list;
    // this.freezableEvents = new FreezableList<Event>(list);
  }

  public void processAlerts(List<Alert> newAlerts)
  {
    Lock lock = alerts.getReadWriteLock( ).writeLock( );
    lock.lock( );
    // freezableEvents.freeze( );
    for (Alert alert : newAlerts)
    {
      processSingleAlert(alert);
    }
    // freezableEvents.thaw( );
    lock.unlock( );
  }

  protected abstract void processSingleAlert(Alert alert);
}
