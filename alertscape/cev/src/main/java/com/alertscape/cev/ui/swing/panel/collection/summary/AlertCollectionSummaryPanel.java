/*
 * Created on Aug 11, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.summary;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.util.concurrent.Lock;

import com.alertscape.cev.model.AlertFilter;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.IndexedAlertCollection;
import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class AlertCollectionSummaryPanel extends JPanel implements AlertFilter
{
  private static final long serialVersionUID = 1L;
  private AlertCollection subCollection;
  private JToggleButton[] sevButtons;
  private JLabel totalLabel;
  private SeverityMatcherEditor severityMatcher = new SeverityMatcherEditor( );
  private Map<Severity, Integer> severityCounts;
  private List<Alert> existingEvents = new ArrayList<Alert>(70000);

  public AlertCollectionSummaryPanel( )
  {
    severityCounts = new HashMap<Severity, Integer>(SeverityFactory
        .getInstance( ).getNumSeverities( ));
    init( );
  }

  public AlertCollection setMasterCollection(AlertCollection master)
  {
    EventList<Alert> masterList = master.getEventList( );
    FilterList<Alert> filterList = new FilterList<Alert>(masterList,
        severityMatcher);
    subCollection = new IndexedAlertCollection(filterList);
    existingEvents.clear( );
    existingEvents.addAll(masterList);

    masterList.addListEventListener(new ListEventListener<Alert>( )
    {
      public void listChanged(ListEvent<Alert> listChanges)
      {
        EventList<Alert> list = listChanges.getSourceList( );
        Lock lock = list.getReadWriteLock( ).readLock( );
        lock.lock( );
        Alert e;
        Severity s;
        int count;
        while (listChanges.next( ))
        {
          int index = listChanges.getIndex( );
          switch (listChanges.getType( ))
          {
            case ListEvent.INSERT:
              e = list.get(index);
              existingEvents.add(index, e);
              s = e.getSeverity( );
              count = severityCounts.get(s);
              count++;
              severityCounts.put(s, count);
              break;
            case ListEvent.DELETE:
              e = existingEvents.remove(index);
              s = e.getSeverity( );
              count = severityCounts.get(s);
              count--;
              severityCounts.put(s, count);
              break;
            case ListEvent.UPDATE:
              e = existingEvents.get(index);
              Alert newEvent = list.get(index);
              existingEvents.set(index, newEvent);
              if(newEvent.getSeverity( ) != e.getSeverity( ))
              {
                // Decrement the old severity
                count = severityCounts.get(e.getSeverity( ));
                count--;
                severityCounts.put(e.getSeverity( ), count);
                
                // Increment the new severity
                count = severityCounts.get(newEvent.getSeverity( ));
                count++;
                severityCounts.put(newEvent.getSeverity( ), count);
              }
              break;
          }

        }
        
        lock.unlock( );

        for (Severity sev : severityCounts.keySet( ))
        {
          sevButtons[sev.getLevel( )].setText(sev.getName( ) + ": "
              + severityCounts.get(sev));
        }
        totalLabel.setText("Total: " + list.size( ));
      }
    });

    // Initialize the counts to the current counts in the collection
    Lock lock = getCollection( ).getEventList( ).getReadWriteLock( ).readLock( );
    lock.lock( );
    for (Alert event : getCollection( ).getEventList( ))
    {
      int count = severityCounts.get(event.getSeverity( ));
      count++;
      severityCounts.put(event.getSeverity( ), count);
    }
    lock.unlock( );

    for (Severity sev : severityCounts.keySet( ))
    {
      sevButtons[sev.getLevel( )].setText(sev.getName( ) + ":"
          + severityCounts.get(sev));
    }

    return subCollection;
  }

  public AlertCollection getCollection( )
  {
    return subCollection;
  }

  protected void init( )
  {
    // setLayout(null);
    JPanel summaryPanel = new JPanel( );
    summaryPanel.setLayout(new GridLayout( ));
    SeverityFactory fact = SeverityFactory.getInstance( );
    int max = fact.getNumSeverities( );
    sevButtons = new JToggleButton[max];
    for (int i = 0; i < max; i++)
    {
      Severity s = fact.getSeverity(i);
      severityCounts.put(s, 0);
      JToggleButton sevButton = new JToggleButton( );
      sevButton.setForeground(s.getForegroundColor( ));
      sevButton.setBackground(s.getBackgroundColor( ));
      sevButton.setText(s.getName( ));
      sevButton.addItemListener(new SeverityItemListener(s));
      sevButton.setSelected(true);
      summaryPanel.add(sevButton);
      sevButtons[i] = sevButton;
    }
    totalLabel = new JLabel( );
    totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
    totalLabel.setFont(totalLabel.getFont( ).deriveFont(Font.BOLD));
    totalLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    totalLabel.setText("Total: ");
    summaryPanel.add(totalLabel);

    setLayout(new BorderLayout( ));
    // add(headerPanel, BorderLayout.NORTH);
    add(summaryPanel, BorderLayout.CENTER);

    severityMatcher.addAllSeverities( );
  }

  private class SeverityItemListener implements ItemListener
  {
    private Severity s;

    public SeverityItemListener(Severity s)
    {
      this.s = s;
    }

    public void itemStateChanged(ItemEvent e)
    {
      if (e.getStateChange( ) == ItemEvent.DESELECTED)
      {
        severityMatcher.removeSeverity(s);
      }
      else
      {
        severityMatcher.addSeverity(s);
      }
    }
  }
}
