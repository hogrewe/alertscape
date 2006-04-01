/*
 * Created on Apr 1, 2006
 */
package com.alertscape.cev.model;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.alertscape.util.GetterHelper;

/**
 * @author josh
 * @version $Version: $
 */
public class SortedEventCollection extends EventCollection
{
    private static Logger logger = Logger
            .getLogger(SortedEventCollection.class);
    private static final Object emptyArgs = new Object[0];

    private List<Event> sortedEvents;
    private EventComparator<Event> comparator;
    private byte[] lock = new byte[0];

    public void setSortedField(String fieldName, boolean desc)
    {
        synchronized (lock)
        {
            if (comparator != null
                    && comparator.getCompareField( ).equals(fieldName))
            {
                if (comparator.isInverted( ) != desc)
                {
                    comparator.setInverted(desc);
                    sort( );
                }
            } else
            {
                comparator = new EventComparator<Event>(fieldName, desc);
                sort( );
            }
        }
    }

    @Override
    public void processEvents(List<Event> events)
    {
        synchronized (lock)
        {
            for (int i = 0; i < events.size( ); i++)
            {
                Event e = events.get(i);
                if (e.isStanding( ))
                {
                    sortedEvents.add(e);
                } else
                {
                    Event alreadyThere = getEvent(e.getEventId( ));
                    sortedEvents.remove(alreadyThere);
                }
            }
            sort( );
        }
        super.processEvents(events);
    }

    public Event getEventAt(int index)
    {
        synchronized (lock)
        {
            return sortedEvents.get(index);
        }
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
                } else if (f1 != null && f1 instanceof Comparable)
                {
                    compare = ((Comparable) f1).compareTo(f2);
                } else if (f1 != null)
                {
                    compare = f1.toString( ).compareTo(f2.toString( ));
                } else
                {
                    compare = f2.toString( ).compareTo(f1.toString( ));
                }

            } catch (Exception e)
            {
                String mesg = "Couldn't compare objects " + e1 + " and " + e2;
                logger.error(mesg, e);
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
