/*
 * Created on Oct 21, 2006
 */
package com.alertscape.common.model;

import java.util.HashMap;
import java.util.Map;


import ca.odell.glazedlists.EventList;

/**
 * @author josh
 * @version $Version: $
 */
public class IndexedEventCollection extends AbstractEventCollection
{
  private Map<Long, Integer> idToIndex = new HashMap<Long, Integer>( );
  
  public IndexedEventCollection()
  {
  }
  
  public IndexedEventCollection(EventList<Event> eventList)
  {
    super(eventList);
  }

  @Override
  protected void processSingleEvent(Event event)
  {
    Integer index = idToIndex.get(event.getEventId( ));
    if (event.isStanding( ))
    {
      // Is it new?
      if (index == null)
      {
        System.out.print("N");
        events.add(event);
        idToIndex.put(event.getEventId( ), events.size( )-1);
      }
      // It's already in the list, just update it
      else
      {
        System.out.print("U");
        events.set(index, event);
      }
    }
    else
    {
      if (index != null)
      {
        System.out.print("C");
        events.remove(index);
        // Ok, we removed the item at index, so we have to update the map for
        // everything that comes after index
        for (int i = index; i < events.size( ); i++)
        {
          Event e = events.get(i);
          int nextIndex = idToIndex.get(e.getEventId( ));
          nextIndex--;
          idToIndex.put(e.getEventId( ), nextIndex);
        }
      }
      else
      {
        System.out.println("Doh, index is null on a delete");
      }
    }
  }

  @Override
  public void clearEvents( )
  {
    super.clearEvents( );
    idToIndex.clear( );
  }

  @Override
  public Event getEvent(long id)
  {
    Event e = null;
    Integer index = idToIndex.get(id);
    if (index != null)
    {
      e = events.get(index);
    }

    return e;
  }
}
