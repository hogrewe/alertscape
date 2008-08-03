/*
 * Created on Oct 17, 2006
 */
package com.alertscape.common.model;

import java.util.Collections;
import java.util.Comparator;


/**
 * @author josh
 * @version $Version: $
 */
public class BinarySortEventCollection extends AbstractEventCollection
{
  private static final Comparator<Event> comparator = new EventComparator( );

  @Override
  protected void processSingleEvent(Event event)
  {
    int index = indexOf(event);
    if (event.isStanding( ))
    {
      // It's a new event
      if (index < 0)
      {
        System.out.print("N");
        events.add(-index - 1, event);
      }
      else
      {
        System.out.print("U");
        events.set(index, event);
      }
    }
    else
    {
      if (index < 0)
      {
        System.out.println("Doh, index is " + index + " on a delete");
      }
      else
      {
        System.out.print("C");
        events.remove(index);
      }
    }
  }

  @Override
  public Event getEvent(long id)
  {
    Event temp = new Event( );
    temp.setEventId(id);
    int index = indexOf(temp);
    if (index >= 0)
    {
      return events.get(index);
    }
    else
    {
      return null;
    }
  }

  protected int indexOf(Event event)
  {
    return Collections.binarySearch(events, event, comparator);
  }

  private static class EventComparator implements Comparator<Event>
  {
    public int compare(Event o1, Event o2)
    {
      long e1 = o1.getEventId( );
      long e2 = o2.getEventId( );
      return (e1 == e2 ? 0 : (e1 < e2) ? -1 : 1);
    }
  }
}
