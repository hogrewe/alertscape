/*
 * Created on Sep 24, 2006
 */
package com.alertscape.cev.ui.swing.panel.collection.summary;

import java.util.HashSet;
import java.util.Set;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;

import com.alertscape.common.model.severity.Severity;
import com.alertscape.common.model.severity.SeverityFactory;

/**
 * @author josh
 * @version $Version: $
 */
public class SeverityMatcherEditor extends
    AbstractMatcherEditor<com.alertscape.common.model.Event>
{
  private Set<Severity> matchingSeverities = new HashSet<Severity>(
      SeverityFactory.getInstance( ).getNumSeverities( ));

  public void addSeverity(Severity s)
  {
    matchingSeverities.add(s);
    if (matchingSeverities.size( ) == SeverityFactory.getInstance( )
        .getNumSeverities( ))
    {
      fireMatchAll( );
    }
    else
    {
      SeverityMatcher m = new SeverityMatcher(matchingSeverities);
      fireRelaxed(m);
    }
  }

  public void removeSeverity(Severity s)
  {
    if (matchingSeverities.contains(s))
    {
      matchingSeverities.remove(s);
      if (matchingSeverities.isEmpty( ))
      {
        fireMatchNone( );
      }
      else
      {
        SeverityMatcher m = new SeverityMatcher(matchingSeverities);
        fireConstrained(m);
      }
    }
  }
  
  public void addAllSeverities()
  {
    SeverityFactory sf = SeverityFactory.getInstance( );
    for(int i=0, size=sf.getNumSeverities( ); i<size; i++)
    {
      matchingSeverities.add(sf.getSeverity(i));
    }
    
    fireMatchAll( );
  }

  private class SeverityMatcher implements
      Matcher<com.alertscape.common.model.Event>
  {
    private Set<Severity> sevs;

    public SeverityMatcher(Set<Severity> sevs)
    {
      this.sevs = new HashSet<Severity>(sevs);
    }

    public boolean matches(com.alertscape.common.model.Event item)
    {
      return sevs.contains(item.getSeverity( ));
    }
  }
}
