/*
 * Created on Aug 11, 2006
 */
package com.alertscape.browser.ui.swing.panel.collection.summary;

import java.awt.BorderLayout;
import java.awt.Color;
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

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.util.concurrent.Lock;

import com.alertscape.browser.model.AlertFilter;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.model.BinarySortAlertCollection;
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
  private JLabel[] sevButtons;
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
    subCollection = new BinarySortAlertCollection(filterList);
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
          //sevButtons[sev.getLevel( )].setText(sev.getName( ) + ": " + severityCounts.get(sev));
          sevButtons[sev.getLevel( )].setText("" + severityCounts.get(sev));
          sevButtons[sev.getLevel( )].setToolTipText(severityCounts.get(sev) + " " + sev.getName( ) + " alerts");
        }
        totalLabel.setText(list.size( ) + "");
        totalLabel.setToolTipText(list.size( ) + " total alerts");
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
      //sevButtons[sev.getLevel( )].setText(sev.getName( ) + ":" + severityCounts.get(sev));
      sevButtons[sev.getLevel( )].setText(severityCounts.get(sev) + "");
      sevButtons[sev.getLevel( )].setToolTipText(severityCounts.get(sev) + " " + sev.getName( ) + " alerts");
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
    sevButtons = new JLabel[max];
    for (int i = 0; i < max; i++)
    {
      Severity s = fact.getSeverity(i);
      severityCounts.put(s, 0);
      
      JPanel wrapperPanel = new JPanel(new BorderLayout());      
      JToggleButton sevButton = new JToggleButton( );
//      sevButton.setForeground(s.getForegroundColor( ));
//      sevButton.setBackground(s.getBackgroundColor( ));
      sevButton.setText(s.getName( ));
      sevButton.setToolTipText("Show/Hide " + s.getName() + " alerts");
      sevButton.addItemListener(new SeverityItemListener(s));
      sevButton.setSelected(true);
      JLabel sevTotal = new JLabel();
      sevTotal.setBorder(BorderFactory.createEtchedBorder());
      sevTotal.setHorizontalAlignment(SwingConstants.CENTER);
      wrapperPanel.add(sevButton, BorderLayout.NORTH);
      wrapperPanel.add(sevTotal, BorderLayout.SOUTH);
      
      summaryPanel.add(wrapperPanel);
      sevButtons[i] = sevTotal;  
    }
    JPanel totalPanel = new JPanel(new BorderLayout());    
    JLabel totalHeader = new JLabel();
    totalHeader.setText("Total");
    totalHeader.setHorizontalAlignment(SwingConstants.CENTER);
    totalHeader.setFont(totalHeader.getFont( ).deriveFont(Font.BOLD));
    totalLabel = new JLabel( );
    totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
    totalLabel.setFont(totalLabel.getFont( ).deriveFont(Font.BOLD));
    totalLabel.setText("0");
    
    totalPanel.add(totalHeader, BorderLayout.NORTH);
    totalPanel.add(totalLabel, BorderLayout.SOUTH);
    totalPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
    summaryPanel.add(totalPanel);

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
