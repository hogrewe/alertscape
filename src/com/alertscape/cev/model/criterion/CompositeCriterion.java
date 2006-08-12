/*
 * Created on Aug 11, 2006
 */
package com.alertscape.cev.model.criterion;

import java.util.ArrayList;
import java.util.List;

import com.alertscape.cev.model.Event;

/**
 * @author josh
 * @version $Version: $
 */
public class CompositeCriterion implements EventCriterion
{
  public static final CompositeType AND = CompositeType.AND;
  public static final CompositeType OR = CompositeType.OR;

  private List<EventCriterion> criteria = new ArrayList<EventCriterion>( );
  private CompositeType type;

  public CompositeCriterion(List<EventCriterion> criteria, CompositeType type)
  {
    this.criteria = new ArrayList<EventCriterion>(criteria);
    this.type = type;
  }
  
  public void addCriterion(EventCriterion c)
  {
    criteria.add(c);
  }
  
  public void removeCriterion(EventCriterion c)
  {
    criteria.remove(c);
  }

  public boolean matches(Event e)
  {
    return type.matches(criteria, e);
  }

  private static abstract class CompositeType
  {
    private static CompositeType AND = new CompositeType("AND")
    {
      @Override
      boolean matches(List<EventCriterion> criteria, Event e)
      {
        for (int i = 0, size = criteria.size( ); i < size; i++)
        {
          EventCriterion criterion = criteria.get(i);
          // If any of the criteria don't match, return false
          if (!criterion.matches(e))
          {
            return false;
          }
        }

        // If nothing didn't match, return true
        return true;
      }
    };
    private static CompositeType OR = new CompositeType("OR")
    {
      @Override
      boolean matches(List<EventCriterion> criteria, Event e)
      {
        for (int i = 0, size = criteria.size( ); i < size; i++)
        {
          EventCriterion criterion = criteria.get(i);
          // If any of the criteria match, return true
          if (criterion.matches(e))
          {
            return true;
          }
        }
        // If we don't find any matching criteria, return false
        return false;
      }
    };

    private String type;

    private CompositeType(String type)
    {
      this.type = type;
    }

    public String toString( )
    {
      return type;
    }

    abstract boolean matches(List<EventCriterion> criteria, Event e);
  }

}
