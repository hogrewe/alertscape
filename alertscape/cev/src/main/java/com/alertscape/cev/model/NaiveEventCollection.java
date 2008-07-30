/*
 * Created on Oct 17, 2006
 */
package com.alertscape.cev.model;

import ca.odell.glazedlists.util.concurrent.Lock;

/**
 * @author josh
 * @version $Version: $
 */
public class NaiveEventCollection extends AbstractEventCollection
{
  @Override
  protected void processSingleEvent(Event event)
  {
    int existingEventIndex = indexOf(event);
    if (event.isStanding( ))
    {
      // A new event, never seen
      if (existingEventIndex < 0)
      {
        System.out.print("N");
        events.add(event);
      }
      // We already have it, update it
      else
      {
        System.out.print("U");
        events.set(existingEventIndex, event);
      }
    }
    else
    {
      assert (existingEventIndex >= 0);
      System.out.print("C");

      events.remove(event);
      events.remove(existingEventIndex);
    }
  }

  protected int indexOf(Event event)
  {
    Lock lock = events.getReadWriteLock( ).readLock( );
    try
    {
      lock.lock( );
      for (int i = 0, size = events.size( ); i < size; i++)
      {
        Event e = events.get(i);
        if (e.getEventId( ) == event.getEventId( ))
        {
          return i;
        }
      }
    }
    finally
    {
      lock.unlock( );
    }

    return -1;
  }
}
