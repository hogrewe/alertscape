/*
 * Created on Aug 11, 2006
 */
package com.alertscape.browser.model.criterion;

import java.util.ArrayList;
import java.util.List;

import com.alertscape.common.model.Alert;

/**
 * @author josh
 * @version $Version: $
 */
public class CompositeCriterion implements AlertCriterion
{
  public static final CompositeType AND = CompositeType.AND;
  public static final CompositeType OR = CompositeType.OR;

  private List<AlertCriterion> criteria = new ArrayList<AlertCriterion>( );
  private CompositeType type;

  public CompositeCriterion(List<AlertCriterion> criteria, CompositeType type)
  {
    this.criteria = new ArrayList<AlertCriterion>(criteria);
    this.type = type;
  }
  
  public void addCriterion(AlertCriterion c)
  {
    criteria.add(c);
  }
  
  public void removeCriterion(AlertCriterion c)
  {
    criteria.remove(c);
  }

  public boolean matches(Alert e)
  {
    return type.matches(criteria, e);
  }

  private static abstract class CompositeType
  {
    private static CompositeType AND = new CompositeType("AND")
    {
      @Override
      boolean matches(List<AlertCriterion> criteria, Alert e)
      {
        for (int i = 0, size = criteria.size( ); i < size; i++)
        {
          AlertCriterion criterion = criteria.get(i);
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
      boolean matches(List<AlertCriterion> criteria, Alert e)
      {
        for (int i = 0, size = criteria.size( ); i < size; i++)
        {
          AlertCriterion criterion = criteria.get(i);
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

    @Override
    public String toString( )
    {
      return type;
    }

    abstract boolean matches(List<AlertCriterion> criteria, Alert e);
  }

}
