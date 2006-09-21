/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.model;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.util.GetterHelper;

/**
 * @author josh
 * @version $Version: $
 */
public class SortedEventCollection extends EventCollection implements
    EventChangeListener
{
  private static final Object emptyArgs = new Object[0];

  private List<Event> sortedEvents;
  private EventComparator<Event> comparator;
  private byte[] lock = new byte[0];
  private EventCollection collection;

  public SortedEventCollection(EventCollection collection)
  {
    this.collection = collection;
    this.collection.addEventChangeListener(this);
  }

  public void handleChange(EventChange change)
  {
    // if(change.getChangeType() == EventChange.FULL)
    // {
    // clearEvents();
    // }
    //        
    // removeEvents(change.getRemoveEvents());
    // removeEvents(change.getAddEvents());
    // addEvents(change.getAddEvents());
    //        
    // // Iterator<Event> eventIter = change.getEvents().iterator();
    // // while(eventIter.hasNext())
    // // {
    // // Event e = eventIter.next();
    // //
    // // Event alreadyStanding = collection.getEvent(e.getEventId());
    // // sortedEvents.remove(alreadyStanding);
    // //
    // // if(e.isStanding())
    // // {
    // // sortedEvents.add(e);
    // // }
    // // }
    // sort();
    // fireEventChange(change.getAddEvents(), change.getRemoveEvents(),
    // change.getRemoveIndexes());
  }

  private void addEvents(List<Event> events)
  {
    Iterator<Event> eventIter = events.iterator( );
    while (eventIter.hasNext( ))
    {
      Event e = eventIter.next( );

      sortedEvents.add(e);
    }
  }

  private void removeEvents(List<Event> events)
  {
    Iterator<Event> eventIter = events.iterator( );
    while (eventIter.hasNext( ))
    {
      Event e = eventIter.next( );

      Event alreadyStanding = collection.getEvent(e.getEventId( ));
      sortedEvents.remove(alreadyStanding);
    }
  }

  public void setSortedField(String fieldName, boolean desc)
  {
    synchronized (lock)
    {
      if (comparator != null && comparator.getCompareField( ).equals(fieldName))
      {
        if (comparator.isInverted( ) != desc)
        {
          comparator.setInverted(desc);
          sort( );
        }
      }
      else
      {
        comparator = new EventComparator<Event>(fieldName, desc);
        sort( );
      }
    }
  }

  @Override
  public Event getEventAt(int index)
  {
    synchronized (lock)
    {
      return sortedEvents.get(index);
    }
  }

  @Override
  public void clearEvents( )
  {
    sortedEvents.clear( );
    // fireFullEventChange(new ArrayList<Event>());
  }

  @Override
  public List<Event> getAllEvents( )
  {
    return collection.getAllEvents( );
  }

  @Override
  public Event getEvent(long id)
  {
    return collection.getEvent(id);
  }

  @Override
  public int getEventCount( )
  {
    return collection.getEventCount( );
  }

  @Override
  public void processEvents(List<Event> events)
  {
    collection.processEvents(events);
  }

  protected void sort( )
  {
    if (comparator != null)
    {
      Collections.sort(sortedEvents, comparator);
    }
  }

  class EventComparator<E> implements Comparator<E>
  {
    private String compareField;
    private Method fieldGetter;
    private boolean inverted = false;

    public EventComparator(String compareField, boolean inverted)
    {
      setInverted(inverted);
      setCompareField(compareField);
    }

    @SuppressWarnings("unchecked")
    public int compare(E e1, E e2)
    {
      int compare = 0;
      try
      {
        Object f1 = fieldGetter.invoke(e1, emptyArgs);
        Object f2 = fieldGetter.invoke(e2, emptyArgs);
        if (f1 == null && f2 == null)
        {
          compare = 0;
        }
        else if (f1 != null && f1 instanceof Comparable)
        {
          compare = ((Comparable) f1).compareTo(f2);
        }
        else if (f1 != null)
        {
          compare = f1.toString( ).compareTo(f2.toString( ));
        }
        else
        {
          compare = f2.toString( ).compareTo(f1.toString( ));
        }

      }
      catch (Exception e)
      {
        String mesg = "Couldn't compare objects " + e1 + " and " + e2;
        ASLogger.error(mesg, e);
      }
      return inverted ? -compare : compare;
    }

    public String getCompareField( )
    {
      return compareField;
    }

    protected void setCompareField(String compareField)
    {
      fieldGetter = GetterHelper.makeEventGetter(compareField);
      this.compareField = compareField;
    }

    public boolean isInverted( )
    {
      return inverted;
    }

    public void setInverted(boolean inverted)
    {
      this.inverted = inverted;
    }
  }

}
