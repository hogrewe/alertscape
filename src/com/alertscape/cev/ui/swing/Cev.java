/*
 * Created on Mar 21, 2006
 */
package com.alertscape.cev.ui.swing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.severity.SeverityFactory;
import com.alertscape.cev.ui.swing.panel.table.EventCollectionTablePanel;

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
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        EventCollectionTablePanel tablePanel = new EventCollectionTablePanel( );
        collection = new EventCollection( );
        tablePanel.setCollection(collection);
        add(tablePanel);

        Thread t = new Thread(new GenerateEvents( ));
        t.start( );
    }

    class GenerateEvents implements Runnable
    {
        private long id = 1000000;
        private SeverityFactory sevFactory = SeverityFactory.getInstance( ); 

        private Event buildNewEvent()
        {
            int sevLevel = (int) (Math.random( ) * sevFactory
                    .getMaxSeverity( ));
            Event e = new Event( );
            e.setCount((long) (Math.random( ) * 1000));
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
        
        private Event buildUpdateToExistingEvent()
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
                int status = (int) Math.random( ) * 2;
                if (status == 0)
                {
                    status = 1;
                }
                e.setStatus(status);
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
                e = buildNewEvent();
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
                        e = buildNewEvent();
                        System.out.print("N");
                    }
                    else
                    {
                        e = buildUpdateToExistingEvent();                        
                    }
                    
                    events.add(e);
                }
                collection.processEvents(events);
                System.out.println("");

                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e1)
                {
                    e1.printStackTrace( );
                }
            }
        }
    }
}
