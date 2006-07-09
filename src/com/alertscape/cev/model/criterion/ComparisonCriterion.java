/*
 * Created on Jul 6, 2006
 */
package com.alertscape.cev.model.criterion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.alertscape.cev.model.Event;
import com.alertscape.util.GetterHelper;

/**
 * @author josh
 * @version $Version: $
 */
public class ComparisonCriterion implements EventCriterion
{
  public static final int EQUAL = 0;
  public static final int NOT_EQUAL = 1;
  public static final int GREATER_THAN = 2;
  public static final int GREATER_THAN_OR_EQUAL = 3;
  public static final int LESS_THAN = 4;
  public static final int LESS_THAN_OR_EQUAL = 5;

  private static Logger logger = Logger.getLogger(ComparisonCriterion.class);
  private static final Object[] GETTER_ARGS = new Object[0];

  private String field;
  private Object value;
  private Method fieldGetter;
  private int comparisonType;

  public ComparisonCriterion(String field, Object value, int comparisonType)
  {
    setField(field);
    setValue(value);
    setComparisonType(comparisonType);
  }

  @SuppressWarnings("unchecked")
  public boolean matches(Event e)
  {
    boolean matches = false;

    if (getFieldGetter( ) != null)
    {
      try
      {
        Object fieldValue = getFieldGetter( ).invoke(e, GETTER_ARGS);
        int comparison = 0;
        if (fieldValue instanceof Comparable)
        {
          comparison = ((Comparable) fieldValue).compareTo(getValue( ));
        }
        else
        {
          String s = fieldValue.toString( );
          comparison = s.compareTo(getValue( ).toString( ));
        }

        switch (getComparisonType( ))
        {
          case EQUAL:
            matches = comparison == 0;
            break;
          case NOT_EQUAL:
            matches = comparison != 0;
            break;
          case GREATER_THAN:
            matches = comparison > 0;
            break;
          case GREATER_THAN_OR_EQUAL:
            matches = comparison >= 0;
            break;
          case LESS_THAN:
            matches = comparison < 0;
            break;
          case LESS_THAN_OR_EQUAL:
            matches = comparison <= 0;
            break;
          default:
            logger.error("The comparison type: " + getComparisonType( )
                + " is not a recognized type");
            break;
        }
      }
      catch (IllegalArgumentException e1)
      {
        logger.error("Trouble invoking getter: " + getFieldGetter( ) + " on "
            + e, e1);
      }
      catch (IllegalAccessException e1)
      {
        logger.error("Trouble invoking getter: " + getFieldGetter( ) + " on "
            + e, e1);
      }
      catch (InvocationTargetException e1)
      {
        logger.error("Trouble invoking getter: " + getFieldGetter( ) + " on "
            + e, e1);
      }
    }

    return matches;
  }

  public String getField( )
  {
    return field;
  }

  public void setField(String field)
  {
    if (this.field != field)
    {
      fieldGetter = GetterHelper.makeEventGetter(field);
    }
    this.field = field;
  }

  public Object getValue( )
  {
    return value;
  }

  /**
   * In order for the comparisons to work correctly, {@link #setField(String)}
   * should be called before calling this method so that the return type of the
   * field can be used to properly determine the expected type for the value.
   * 
   * @param value
   *          The value to compare the field value to
   */
  public void setValue(Object value)
  {
    if (value == null)
    {
      this.value = value;
      return;
    }

    if (fieldGetter != null)
    {
      // Get the string representation of the value
      String s = value.toString( );

      // Get the return type of the getter method
      Class<? extends Object> fieldType = fieldGetter.getReturnType( );

      // Check if it's any of the primitive types and convert it to that type
      // If it's none, then just leave it alone
      if (fieldType.isAssignableFrom(Double.class))
      {
        value = Double.valueOf(s);
      }
      if (fieldType.isAssignableFrom(double.class))
      {
        value = Double.parseDouble(s);
      }
      else if (fieldType.isAssignableFrom(Float.class))
      {
        value = Float.valueOf(s);
      }
      else if (fieldType.isAssignableFrom(float.class))
      {
        value = Float.parseFloat(s);
      }
      else if (fieldType.isAssignableFrom(Long.class))
      {
        value = Long.valueOf(s);
      }
      else if (fieldType.isAssignableFrom(long.class))
      {
        value = Long.parseLong(s);
      }
      else if (fieldType.isAssignableFrom(Integer.class))
      {
        value = Integer.valueOf(s);
      }
      else if (fieldType.isAssignableFrom(int.class))
      {
        value = Integer.parseInt(s);
      }
      else if (fieldType.isAssignableFrom(Short.class))
      {
        value = Short.valueOf(s);
      }
      else if (fieldType.isAssignableFrom(short.class))
      {
        value = Short.parseShort(s);
      }
      else if (fieldType.isAssignableFrom(Byte.class))
      {
        value = Byte.valueOf(s);
      }
      else if (fieldType.isAssignableFrom(byte.class))
      {
        value = Byte.parseByte(s);
      }
      else if (fieldType.isAssignableFrom(Boolean.class))
      {
        value = Boolean.valueOf(s);
      }
      else if (fieldType.isAssignableFrom(boolean.class))
      {
        value = Boolean.parseBoolean(s);
      }
    }
    this.value = value;
  }

  protected Method getFieldGetter( )
  {
    return fieldGetter;
  }

  protected void setFieldGetter(Method fieldGetter)
  {
    this.fieldGetter = fieldGetter;
  }

  protected int getComparisonType( )
  {
    return comparisonType;
  }

  protected void setComparisonType(int comparisonType)
  {
    this.comparisonType = comparisonType;
  }
}
