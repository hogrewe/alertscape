/*
 * Created on Mar 21, 2006
 */
package com.alertscape.cev.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;

import com.alertscape.cev.common.auth.Authentication;
import com.alertscape.cev.ui.swing.panel.CevStatusPanel;
import com.alertscape.cev.ui.swing.panel.collection.summary.EventCollectionSummaryPanel;
import com.alertscape.cev.ui.swing.panel.collection.table.EventCollectionTablePanel;
import com.alertscape.cev.ui.swing.panel.common.ASPanelBuilder;
import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.IndexedAlertCollection;
import com.alertscape.common.model.Alert.AlertStatus;
import com.alertscape.common.model.severity.SeverityFactory;
import com.alertscape.util.ImageFinder;

/**
 * @author josh
 * @version $Version: $
 */
public class Cev extends JFrame
{
  private static final long serialVersionUID = 1L;

  private AlertCollection collection;

  public Cev( )
  {
    init( );
  }

  public void init( )
  {
    try
    {
      LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels( );
      for (LookAndFeelInfo info : installedLookAndFeels)
      {
        ASLogger.debug(info.getName( ) + ":" + info.getClassName( ));
      }
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName( ));
//      UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    }
    catch (ClassNotFoundException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace( );
    }
    catch (InstantiationException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace( );
    }
    catch (IllegalAccessException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace( );
    }
    catch (UnsupportedLookAndFeelException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace( );
    }
    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    collection = new IndexedAlertCollection( );
    JPanel p = new JPanel( );
    p.setLayout(new BorderLayout( ));
    EventCollectionSummaryPanel summaryPanel = new EventCollectionSummaryPanel( );
    AlertCollection subCollection = summaryPanel
        .setMasterCollection(collection);
    EventCollectionTablePanel tablePanel = new EventCollectionTablePanel(
        subCollection);

    Icon bgImage = ImageFinder.getInstance( ).findImage(
        "/com/alertscape/images/common/hdr_background_small.png");
    JPanel outerSummaryPanel = new JPanel( );
    outerSummaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
    outerSummaryPanel.setLayout(new GridLayout(1, 1));
    outerSummaryPanel.add(summaryPanel);
    JPanel outerTablePanel = new JPanel( );
    outerTablePanel.setLayout(new BorderLayout( ));
    JLabel hdrLabel = new JLabel( );
    hdrLabel.setForeground(Color.white);
    hdrLabel.setText("Events");
    hdrLabel.setOpaque(false);
    Font f = new Font("Dialog", Font.ITALIC, 18);
    hdrLabel.setFont(f);
    JPanel headerPanel = new JPanel( );
    headerPanel.setBorder(BorderFactory.createEmptyBorder(7, 15, 1, 3));
    headerPanel.setOpaque(false);
    headerPanel.setLayout(new GridLayout(1, 1));
    headerPanel.add(hdrLabel);
    JPanel imagePanel = ASPanelBuilder.wrapInBackgroundImage(headerPanel,
        bgImage);
    imagePanel.setBackground(Color.white);
    JPanel bgPanel = new JPanel( );
    bgPanel.setLayout(new GridLayout(1, 1));
    bgPanel.add(imagePanel);
    bgPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    outerTablePanel.add(bgPanel, BorderLayout.NORTH);
    outerTablePanel.add(tablePanel, BorderLayout.CENTER);
    p.add(outerSummaryPanel, BorderLayout.NORTH);
    p.add(outerTablePanel, BorderLayout.CENTER);
    p.add(new CevStatusPanel( ), BorderLayout.SOUTH);
    setContentPane(p);

    Authentication.login("CEV", "john.doe", null);
    setTitle("alertscape Event Browser");
    URL cevImageUrl = getClass( ).getResource(
        "/com/alertscape/images/common/as_logo2_32.png");
    ImageIcon cevImage = new ImageIcon(cevImageUrl);
    setIconImage(cevImage.getImage( ));
    setVisible(true);

    GenerateEvents gen = new GenerateEvents(collection);
    int groupSize = 1000;
    List<Alert> events = new ArrayList<Alert>(groupSize);
    for (int i = 0; i < 50000; i++)
    {
      events.add(gen.buildNewEvent( ));
      if (i % groupSize == 0)
      {
        ASLogger.debug(i);
        collection.processAlerts(events);
        events.clear( );
      }
    }
    collection.processAlerts(events);

    Thread t = new Thread(gen);
    t.start( );
  }

  public static class GenerateEvents implements Runnable
  {
    private static final int NUM_EVENTS_TO_CACHE = 50000;
    private long id = 1000000;
    private SeverityFactory sevFactory = SeverityFactory.getInstance( );
    private Random rand = new Random( );
    private AlertCollection c;
    private List<Alert> newEvents = new ArrayList<Alert>(NUM_EVENTS_TO_CACHE);

    public GenerateEvents(AlertCollection collection)
    {
      c = collection;
    }

    public Alert buildNewEvent( )
    {
      int sevLevel = rand.nextInt(sevFactory.getNumSeverities( ));
      Alert e = new Alert( );
      e.setCount(rand.nextInt(1000));
      e.setAlertId(id++);
      e.setFirstOccurence(new Date( ));
      e.setItem("Some item");
      e.setItemManager("Some item manager");
      e.setItemManagerType("Some item manager type");
      e.setItemType("Some item type");
      e.setLastOccurence(new Date( ));
      e.setLongDescription("Some long desription");
      e.setSeverity(sevFactory.getSeverity(sevLevel));
      e.setShortDescription("Some short description");
      e.setSource(new AlertSource(1, "Source 1"));
      e.setStatus(Alert.AlertStatus.STANDING);
      e.setType("Some type");

      return e;
    }

    private Alert buildUpdateToExistingEvent(List<Alert> events)
    {
      Alert e = new Alert( );
      if (events.size( ) > 1)
      {
        int eventIndex = rand.nextInt(events.size( ) - 1);
        Alert old = events.get(eventIndex);

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
          System.out.print("U");
        }
        else
        {
          e.setStatus(AlertStatus.CLEARED);
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
      Random rand = new Random();
      List<Alert> allEvents = new ArrayList<Alert>(100000);
      allEvents.addAll(c.getEventList( ));
      for(int i=0; i<NUM_EVENTS_TO_CACHE; i++)
      {
        boolean val = rand.nextBoolean( );

        Alert e = null;

        if (val)
        {
          e = buildNewEvent( );
          System.out.print("N");
        }
        else
        {
          e = buildUpdateToExistingEvent(allEvents);
        }

        newEvents.add(e);
        allEvents.add(e);
      }

      // The place we are in the event queue
      int newEventPointer = 0;
      while (true)
      {
        int groupSize = rand.nextInt(100);
        List<Alert> events = new ArrayList<Alert>( );
        for (int i = 0; i < groupSize; i++)
        {
          events.add(newEvents.get(newEventPointer + i));
        }
        newEventPointer+=groupSize;
        c.processAlerts(events);
        System.out.println("Processed: " + events.size( ) + " events");

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
