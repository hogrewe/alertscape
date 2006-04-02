/*
 * Created on Mar 21, 2006
 */
package com.alertscape.cev;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.severity.Severity;
import com.alertscape.cev.model.severity.SeverityFactory;
import com.alertscape.cev.ui.swing.panel.table.EventCollectionTablePanel;

/**
 * @author josh
 * @version $Version: $
 */
public class CevFrame extends JFrame
{
    private static final long serialVersionUID = 1L;

    private EventCollection collection;

    public CevFrame( )
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

        public void run( )
        {
            while (true)
            {
                List<Event> events = new ArrayList<Event>( );
                for (int i = 0; i < 100; i++)
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

                    events.add(e);
                }
                collection.processEvents(events);

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
