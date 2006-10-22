/*
 * Created on Oct 17, 2006
 */
package com.alertscape.cev.model;

import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FreezableList;
import ca.odell.glazedlists.util.concurrent.Lock;

/**
 * @author josh
 * @version $Version: $
 */
public abstract class AbstractEventCollection implements EventCollection
{
  protected EventList<Event> events;

  // protected FreezableList<Event> freezableEvents;

  public AbstractEventCollection( )
  {
    setEventList(new BasicEventList<Event>(70000));
  }

  public AbstractEventCollection(EventList<Event> eventList)
  {
    setEventList(eventList);
  }

  public void clearEvents( )
  {
    Lock lock = events.getReadWriteLock( ).writeLock( );
    try
    {
      lock.lock( );
      events.clear( );
    }
    finally
    {
      lock.unlock( );
    }
  }

  public Event getEvent(long id)
  {
    Lock lock = events.getReadWriteLock( ).readLock( );
    try
    {
      lock.lock( );
      for (Event e : events)
      {
        if (e.getEventId( ) == id)
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

  public Event getEventAt(int index)
  {
    Lock lock = events.getReadWriteLock( ).readLock( );
    try
    {
      lock.lock( );
      return events.get(index);
    }
    finally
    {
      lock.unlock( );
    }
  }

  public int getEventCount( )
  {
    Lock lock = events.getReadWriteLock( ).readLock( );
    try
    {
      lock.lock( );
      return events.size( );
    }
    finally
    {
      lock.unlock( );
    }
  }

  public EventList<Event> getEventList( )
  {
    // TODO: Do we need to pass back the modifiable list? We may need to wrap
    // this list in other lists and have those lists be transformable
    return events;
  }

  public void setEventList(EventList<Event> list)
  {
    // TODO: should we do some sort of defensive copying?!?
    this.events = list;
    // this.freezableEvents = new FreezableList<Event>(list);
  }

  public void processEvents(List<Event> newEvents)
  {
    System.out.println( );
    Lock lock = events.getReadWriteLock( ).writeLock( );
    lock.lock( );
    // freezableEvents.freeze( );
    for (Event event : newEvents)
    {
      processSingleEvent(event);
    }
    // freezableEvents.thaw( );
    lock.unlock( );
    System.out.println( );
  }

  protected abstract void processSingleEvent(Event event);
}
