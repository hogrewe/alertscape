/*
 * Created on Sep 30, 2006
 */
package com.alertscape.cev.model;

import java.util.Date;
import java.util.Random;

import com.alertscape.cev.model.Event.EventStatus;
import com.alertscape.cev.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class FakeEventGenerator
{
  private static final Random rand = new Random( );
  private static final SeverityFactory sevFactory = SeverityFactory
      .getInstance( );

  private static long id = 10000;

  public static Event buildNewEvent( )
  {
    int sevLevel = rand.nextInt(sevFactory.getNumSeverities( ));
    Event e = new Event( );
    e.setCount(rand.nextInt(1000));
    e.setEventId(id++);
    e.setFirstOccurence(new Date( ));
    e.setItem("Item " + rand.nextInt(500));
    e.setItemManager("Item manager " + rand.nextInt(25));
    e.setItemManagerType("Item manager type " + rand.nextInt(5));
    e.setItemType("Item type " + rand.nextInt(20));
    e.setLastOccurence(new Date( ));
    e.setLongDescription("Some long desription");
    e.setSeverity(sevFactory.getSeverity(sevLevel));
    e.setShortDescription("Some short description");
    e.setSourceId(1);
    e.setStatus(Event.EventStatus.STANDING);
    e.setType("Type " + rand.nextInt(10));

    return e;
  }

  public static Event buildUpdateToExistingEvent(Event old)
  {
    Event e = new Event( );

    e.setCount(old.getCount( ) + 1);
    e.setEventId(old.getEventId( ));
    e.setFirstOccurence(old.getFirstOccurence( ));
    e.setItem(old.getItem( ));
    e.setItemManager(old.getItemManager( ));
    e.setItemManagerType(old.getItemManagerType( ));
    e.setItemType(old.getItemType( ));
    e.setLastOccurence(new Date( ));
    e.setLongDescription(old.getLongDescription( ));
    e.setSeverity(old.getSeverity( ));
    e.setShortDescription(old.getShortDescription( ));
    e.setSourceId(old.getSourceId( ));
    if (rand.nextBoolean( ))
    {
      e.setStatus(EventStatus.STANDING);
    }
    else
    {
      e.setStatus(EventStatus.CLEARED);
    }
    e.setType(old.getType( ));
    
    return e;
  }
}
