/*
 * Created on Aug 11, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.summary;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;

import com.alertscape.cev.model.Event;
import com.alertscape.cev.model.EventCollection;
import com.alertscape.cev.model.EventFilter;
import com.alertscape.cev.model.IndexedEventCollection;
import com.alertscape.cev.model.SeverityCounter;
import com.alertscape.cev.model.severity.Severity;
import com.alertscape.cev.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class EventCollectionSummaryPanel extends JPanel implements EventFilter
{
  private static final long serialVersionUID = 1L;
  private EventCollection subCollection;
  private JToggleButton[] sevButtons;
  private JLabel totalLabel;
  private SeverityMatcherEditor severityMatcher = new SeverityMatcherEditor( );
  private SeverityCounter counter = new SeverityCounter( );

  public EventCollectionSummaryPanel( )
  {
    init( );
  }

  public EventCollection setMasterCollection(EventCollection master)
  {
    EventList<Event> masterList = master.getEventList( );
    FilterList<Event> filterList = new FilterList<Event>(masterList,
        severityMatcher);
    subCollection = new IndexedEventCollection(filterList);
    counter.setEventCollection(master);
    counter.addPropertyChangeListener(new PropertyChangeListener( )
    {
      public void propertyChange(PropertyChangeEvent evt)
      {
        SeverityFactory sevFactory = SeverityFactory.getInstance( );
        for (int i = 0, size = sevFactory.getNumSeverities( ); i < size; i++)
        {
          Severity sev = sevFactory.getSeverity(i);
          sevButtons[i].setText(sev.getName( ) + ":" + counter.getCount(sev));
        }
        totalLabel.setText("Total: " + counter.getTotalCount( ));
      }
    });

    SeverityFactory sevFactory = SeverityFactory.getInstance( );
    for (int i = 0, size = sevFactory.getNumSeverities( ); i < size; i++)
    {
      Severity sev = sevFactory.getSeverity(i);
      sevButtons[i].setText(sev.getName( ) + ":" + counter.getCount(sev));
    }
    totalLabel.setText("Total: " + masterList.size( ));

    return subCollection;
  }

  public EventCollection getCollection( )
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
