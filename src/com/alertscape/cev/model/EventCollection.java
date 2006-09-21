/*
 * Created on Mar 26, 2006
 */
package com.alertscape.cev.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alertscape.cev.model.EventChange.EventChangeType;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollection
{
  private byte[] lock = new byte[0];
  private Map<Long, Event> eventMap = new HashMap<Long, Event>( );
  private List<Event> events = new ArrayList<Event>( );
  private EventChangeSupport support = new EventChangeSupport( );

  public void processEvents(List<Event> newEvents)
  {
    List<Event> inserts = new ArrayList<Event>(newEvents.size( ));
    List<Event> updates = new ArrayList<Event>(newEvents.size( ));
    List<Event> removes = new ArrayList<Event>(newEvents.size( ));
    List<Integer> insertIndices = new ArrayList<Integer>(newEvents.size( ));
    List<Integer> updateIndices = new ArrayList<Integer>(newEvents.size( ));
    List<Integer> removeIndices = new ArrayList<Integer>(newEvents.size( ));

    synchronized (lock)
    {
      // We have to loop thrice because we have to do removes first, then
      // updates and then inserts so our indices are correct
      for (int i = 0, size = newEvents.size( ); i < size; i++)
      {
        Event e = newEvents.get(i);
        // First loop, we only care about removes
        if (!e.isStanding( ))
        {
          Event current = eventMap.remove(e.getEventId( ));
          if (current != null)
          {
            int index = events.indexOf(current);
            events.remove(index);
            removes.add(e);
            removeIndices.add(index);
          }
        }
      }
      // Fire off the removes
      if (!removes.isEmpty( ))
      {
        fireEventChange(EventChange.EventChangeType.REMOVE, removes, removeIndices);
      }

      for (int i = 0, size = newEvents.size( ); i < size; i++)
      {
        Event e = newEvents.get(i);
        // Second loop, we only care about updates
        if (e.isStanding( ))
        {
          // Check to see if we already have it
          Event current = eventMap.get(e.getEventId( ));
          // If we already have it, this is an update
          if (current != null)
          {
            // Pull the index that we need to update and set it to the new event
            int index = events.indexOf(current);
            events.set(index, e);
            updates.add(e);
            updateIndices.add(index);
            eventMap.put(e.getEventId( ), e);
          }
        }
      }
      // Fire off the updates
      if (!updates.isEmpty( ))
      {
        fireEventChange(EventChange.EventChangeType.UPDATE, updates, updateIndices);
      }

      for (int i = 0, size = newEvents.size( ); i < size; i++)
      {
        Event e = newEvents.get(i);
        // Third loop, we only care about inserts
        if (e.isStanding( ))
        {
          // Check to see if we already have it
          Event current = eventMap.get(e.getEventId( ));
          // If we don't already have it, this is an insert
          if (current == null)
          {
            int index = events.size( );
            events.add(e);
            inserts.add(e);
            insertIndices.add(index);
            eventMap.put(e.getEventId( ), e);
          }
        }
      }
      // Fire off the updates
      if (!inserts.isEmpty( ))
      {
        fireEventChange(EventChange.EventChangeType.INSERT, inserts, insertIndices);
      }
    }
  }

  public List<Event> getAllEvents( )
  {
    List<Event> all = null;
    synchronized (lock)
    {
      all = new ArrayList<Event>(eventMap.values( ));
    }
    return all;
  }

  public int getEventCount( )
  {
    return events.size( );
  }

  public Event getEvent(long id)
  {
    Event e = null;
    synchronized (lock)
    {
      e = eventMap.get(id);
    }
    return e;
  }

  public Event getEventAt(int index)
  {
    return events.get(index);
  }

  public void clearEvents( )
  {
    List<Event> removed = null;
    List<Integer> indices = null;
    synchronized (lock)
    {
      eventMap.clear( );
      removed = new ArrayList<Event>(events.size( ));
      indices = new ArrayList<Integer>(events.size( ));
      for (int i = 0, size = events.size( ); i < size; i++)
      {
        removed.add(events.get(i));
        indices.add(i);
      }
      events.clear( );
    }
    fireEventChange(EventChange.EventChangeType.REMOVE, removed, indices);
  }

  public void addEventChangeListener(EventChangeListener l)
  {
    support.addListener(l);
  }

  public void removeEventChangeListener(EventChangeListener l)
  {
    support.removeListener(l);
  }

  protected void fireEventChange(EventChangeType type, List<Event> events,
      List<Integer> indices)
  {
    EventChange change = new EventChange(type, events, indices, this);
    support.fireEventChange(change);
  }

  protected int addEvent(Event e)
  {
    eventMap.put(e.getEventId( ), e);
    // TODO: this may be slow
    int index = events.indexOf(e);
    if (index >= 0)
    {
      events.set(index, e);
    }
    else
    {
      events.add(e);
      index = events.size( ) - 1;
    }
    return index;
  }

  protected int removeEvent(Event e)
  {
    eventMap.remove(e.getEventId( ));
    // TODO: this may be slow
    int index = events.indexOf(e);
    if (index >= 0)
    {
      events.remove(index);
    }
    return index;
  }
}
