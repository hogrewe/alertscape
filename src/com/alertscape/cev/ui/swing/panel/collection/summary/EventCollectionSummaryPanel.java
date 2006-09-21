/*
 * Created on Aug 11, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.summary;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventChange;
import com.alertscape.cev.model.EventChangeListener;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.EventFilter;
import com.alertscape.cev.model.EventChange.EventChangeType;
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
    EventCollectionPanel, EventChangeListener, EventFilter
{
  private static final long serialVersionUID = 1L;
  private EventCollection collection;
  private Map<Severity, Set<Event>> severityEvents = new HashMap<Severity, Set<Event>>( );
  private EventCollection subCollection;
  private JToggleButton[] sevButtons;
  private JLabel totalLabel;
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

  public void setMasterCollection(EventCollection master)
  {
    setCollection(master);
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
      List<Event> changes = change.getEvents( );
      for (int i = 0, size = changes.size( ); i < size; i++)
      {
        Event event = changes.get(i);
        Severity s = event.getSeverity( );
        Set<Event> standing = severityEvents.get(s);
        EventChangeType type = change.getType( );
        if (type == EventChange.EventChangeType.INSERT)
        {
          standing.add(event);
        }
        else if (type == EventChange.EventChangeType.UPDATE)
        {
          // Not sure, do we do anything?
        }
        else if (type == EventChange.EventChangeType.REMOVE)
        {
          standing.remove(event);
        }
        if (sevButtons[s.getLevel( )].isSelected( ))
        {
          subChanges.add(event);
        }
      }

      SeverityFactory fact = SeverityFactory.getInstance( );
      int total = 0;
      for (int i = 0; i < sevButtons.length; i++)
      {
        Severity s = fact.getSeverity(i);
        int size = severityEvents.get(s).size( );
        total += size;
        sevButtons[i].setText(s.getName( ) + ": " + size);
      }
      totalLabel.setText("Total: " + total);
      subCollection.processEvents(subChanges);
    }
  }

  protected void init( )
  {
    // setLayout(null);
    setLayout(new GridLayout( ));
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
      add(sevButton);
      sevButtons[i] = sevButton;
    }
    totalLabel = new JLabel( );
    totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
    totalLabel.setFont(totalLabel.getFont( ).deriveFont(Font.BOLD));
    totalLabel.setText("Total: ");
    add(totalLabel);
  }

  private class SeverityItemListener implements ItemListener
  {
    EventCriterion sevCriterion;

    public SeverityItemListener(Severity s)
    {
      sevCriterion = new ComparisonCriterion("severity", s,
          ComparisonCriterion.ComparisonType.EQUAL);
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
        if (subCollection != null)
        {
          subCollection.clearEvents( );
          subCollection.processEvents(newEvents);
        }
      }
    }
  }

  public void setSubCollection(EventCollection sub)
  {
    subCollection = sub;
  }

}
