/*
 * Created on Aug 11, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.summary;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventChange;
import com.alertscape.cev.model.EventChangeListener;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.criterion.ComparisonCriterion;
import com.alertscape.cev.model.criterion.EventCriterion;
import com.alertscape.cev.model.severity.Severity;
import com.alertscape.cev.model.severity.SeverityFactory;
import com.alertscape.cev.ui.swing.panel.collection.EventCollectionPanel;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionSummaryPanel extends JPanel implements
    EventCollectionPanel, EventChangeListener
{
  private static final long serialVersionUID = 1L;
  private EventCollection collection;
  private Map<Severity, Set<Event>> severityEvents = new HashMap<Severity, Set<Event>>( );
  private EventCollectionPanel subPanel;
  private EventCollection subCollection = new EventCollection( );
  private JToggleButton[] sevButtons;
  private byte[] updateLock = new byte[0];

  public EventCollectionSummaryPanel( )
  {
    init( );
  }

  public void setCollection(EventCollection collection)
  {
    this.collection = collection;
    collection.addEventChangeListener(this);
  }

  public EventCollection getCollection( )
  {
    return collection;
  }

  public void handleChange(EventChange change)
  {
    synchronized (updateLock)
    {
      List<Event> subChanges = new ArrayList<Event>( );
      List<Event> adds = change.getAddEvents( );
      for (int i = 0, size = adds.size( ); i < size; i++)
      {
        Event event = adds.get(i);
        Severity s = event.getSeverity( );
        Set<Event> standing = severityEvents.get(s);
        standing.add(event);
        if (sevButtons[s.getLevel( )].isSelected( ))
        {
          subChanges.add(event);
        }
      }

      List<Event> removes = change.getRemoveEvents( );
      for (int i = 0, size = removes.size( ); i < size; i++)
      {
        Event event = removes.get(i);
        Severity s = event.getSeverity( );
        Set<Event> standing = severityEvents.get(s);
        standing.remove(event);
        if (sevButtons[s.getLevel( )].isSelected( ))
        {
          subChanges.add(event);
        }
      }

      SeverityFactory fact = SeverityFactory.getInstance( );
      for (int i = 0; i < sevButtons.length; i++)
      {
        Severity s = fact.getSeverity(i);
        sevButtons[i].setText(s.getName( ) + ": "
            + severityEvents.get(s).size( ));
      }
      subCollection.processEvents(subChanges);
    }
  }

  protected void init( )
  {
    SeverityFactory fact = SeverityFactory.getInstance( );
    int max = fact.getMaxSeverity( );
    sevButtons = new JToggleButton[max];
    for (int i = 0; i < max; i++)
    {
      Severity s = fact.getSeverity(i);
      severityEvents.put(s, new HashSet<Event>( ));
      JToggleButton sevButton = new JToggleButton( );
      sevButton.setForeground(s.getForegroundColor( ));
      sevButton.setBackground(s.getBackgroundColor( ));
      sevButton.setText(s.getName( ));
      sevButton.addItemListener(new SeverityItemListener(s));
      sevButton.setSelected(true);
      sevButton.setFocusPainted(true);
      add(sevButton);
      sevButtons[i] = sevButton;
    }
  }

  public EventCollectionPanel getSubPanel( )
  {
    return subPanel;
  }

  public void setSubPanel(EventCollectionPanel nextPanel)
  {
    this.subPanel = nextPanel;
    this.subPanel.setCollection(subCollection);
  }

  private class SeverityItemListener implements ItemListener
  {
    EventCriterion sevCriterion;

    public SeverityItemListener(Severity s)
    {
      sevCriterion = new ComparisonCriterion("severity", s,
          ComparisonCriterion.EQUAL);
    }

    public void itemStateChanged(ItemEvent e)
    {
      synchronized (updateLock)
      {
        // TODO: This is NOT good!
        List<Event> newEvents = new ArrayList<Event>( );
        if (collection != null)
        {
          List<Event> events = collection.getAllEvents( );
          for (int i = 0, size = events.size( ); i < size; i++)
          {
            Event event = events.get(i);
            Severity s = event.getSeverity( );
            if (sevButtons[s.getLevel( )].isSelected( ))
            {
              newEvents.add(event);
            }
          }
        }
        subCollection.clearEvents( );
        subCollection.processEvents(newEvents);
      }
    }
  }

}
