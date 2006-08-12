/*
 * Created on Mar 21, 2006
 */
package com.alertscape.cev.ui.swing;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.alertscape.cev.common.auth.Authentication;
import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.severity.SeverityFactory;
import com.alertscape.cev.ui.swing.panel.collection.summary.EventCollectionSummaryPanel;
import com.alertscape.cev.ui.swing.panel.collection.table.EventCollectionTablePanel;

/**
 * @author josh
 * @version $Version: $
 */
public class Cev extends JFrame
{
  private static final long serialVersionUID = 1L;

  private EventCollection collection;

  public Cev( )
  {
    init( );
    setVisible(true);
  }

  public void init( )
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (InstantiationException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (UnsupportedLookAndFeelException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    EventCollectionTablePanel tablePanel = new EventCollectionTablePanel( );
    collection = new EventCollection( );
    // tablePanel.setCollection(collection);
    JPanel p = new JPanel( );
    p.setLayout(new BorderLayout( ));
    EventCollectionSummaryPanel summaryPanel = new EventCollectionSummaryPanel( );
    summaryPanel.setCollection(collection);
    summaryPanel.setSubPanel(tablePanel);
    p.add(summaryPanel, BorderLayout.NORTH);
    p.add(tablePanel, BorderLayout.CENTER);
    setContentPane(p);
    
    Authentication.login("CEV", "john.doe", null);

    Thread t = new Thread(new GenerateEvents( ));
    t.start( );
  }

  class GenerateEvents implements Runnable
  {
    private long id = 1000000;
    private SeverityFactory sevFactory = SeverityFactory.getInstance( );
    private Random rand = new Random( );

    private Event buildNewEvent( )
    {
      int sevLevel = rand.nextInt(sevFactory.getMaxSeverity( ));
      Event e = new Event( );
      e.setCount(rand.nextInt(1000));
      e.setEventId(id++);
      e.setFirstOccurence(new Date( ));
      e.setItem("Some item");
      e.setItemManager("Some item manager");
      e.setItemManagerType("Some item manager type");
      e.setItemType("Some item type");
      e.setLastOccurence(new Date( ));
      e.setLongDescription("Some long desription");
      e.setSeverity(sevFactory.getSeverity(sevLevel));
      e.setShortDescription("Some short description");
      e.setSourceId(1);
      e.setStatus(Event.STANDING);
      e.setType("Some type");

      return e;
    }

    private Event buildUpdateToExistingEvent( )
    {
      Event e = new Event( );
      if (collection.getEventCount( ) > 0)
      {
        int eventIndex = (int) (Math.random( ) * (collection.getEventCount( ) - 1));
        Event old = collection.getEventAt(eventIndex);

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
        int status = rand.nextInt(2);
        e.setStatus(status + 1);
        if (status == Event.STANDING)
        {
          System.out.print("U");
        }
        else
        {
          System.out.print("C");
        }
        e.setType(old.getType( ));
      }
      else
      {
        e = buildNewEvent( );
        System.out.print("N");
      }
      return e;
    }

    public void run( )
    {
      while (true)
      {
        List<Event> events = new ArrayList<Event>( );
        for (int i = 0; i < 100; i++)
        {
          int val = (int) (Math.random( ) * 2);

          Event e = null;

          if (val == 1)
          {
            e = buildNewEvent( );
            System.out.print("N");
          }
          else
          {
            e = buildUpdateToExistingEvent( );
          }

          events.add(e);
        }
        collection.processEvents(events);
        System.out.println("");

        try
        {
          Thread.sleep(2000);
        }
        catch (InterruptedException e1)
        {
          e1.printStackTrace( );
        }
      }
    }
  }
}
