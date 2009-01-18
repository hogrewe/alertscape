/*
 * Created on Sep 30, 2006
 */
package com.alertscape.browser.model;

import java.util.Date;
import java.util.Random;

import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.Alert.AlertStatus;
import com.alertscape.common.model.severity.SeverityFactory;

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

  public static Alert buildNewEvent( )
  {
    int sevLevel = rand.nextInt(sevFactory.getNumSeverities( ));
    Alert e = new Alert( );
    e.setCount(rand.nextInt(1000));
    e.setAlertId(id++);
    e.setFirstOccurence(new Date( ));
    e.setItem("Item " + rand.nextInt(500));
    e.setItemManager("Item manager " + rand.nextInt(25));
    e.setItemManagerType("Item manager type " + rand.nextInt(5));
    e.setItemType("Item type " + rand.nextInt(20));
    e.setLastOccurence(new Date( ));
    e.setLongDescription("Some long desription");
    e.setSeverity(sevFactory.getSeverity(sevLevel));
    e.setShortDescription("Some short description");
    e.setSource(new AlertSource(1, "Source 1"));
    e.setStatus(Alert.AlertStatus.STANDING);
    e.setType("Type " + rand.nextInt(10));

    return e;
  }

  public static Alert buildUpdateToExistingEvent(Alert old)
  {
    Alert e = new Alert( );

    e.setCount(old.getCount( ) + 1);
    e.setAlertId(old.getAlertId( ));
    e.setFirstOccurence(old.getFirstOccurence( ));
    e.setItem(old.getItem( ));
    e.setItemManager(old.getItemManager( ));
    e.setItemManagerType(old.getItemManagerType( ));
    e.setItemType(old.getItemType( ));
    e.setLastOccurence(new Date( ));
    e.setLongDescription(old.getLongDescription( ));
    e.setSeverity(old.getSeverity( ));
    e.setShortDescription(old.getShortDescription( ));
    e.setSource(old.getSource( ));
    if (rand.nextBoolean( ))
    {
      e.setStatus(AlertStatus.STANDING);
    }
    else
    {
      e.setStatus(AlertStatus.CLEARED);
    }
    e.setType(old.getType( ));
    
    return e;
  }
}
