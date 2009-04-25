package com.alertscape.browser.ui.swing.panel.collection.chart;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.alertscape.browser.localramp.firstparty.preferences.UserPreferencesPanel;
import com.alertscape.browser.ui.swing.panel.collection.AlertCollectionPanel;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.util.AlertUtility;

public class AlertPieChartPanel extends JPanel implements AlertCollectionPanel, UserPreferencesPanel
{
	private static final long serialVersionUID = 1L;
  private AlertCollection collection;
  private String chartedField;
  private List<Alert> chartedAlerts;
  private String title;
  private String tooltip;
  
  public AlertPieChartPanel(List<Alert> alerts, String alertField, String title, String tooltip)
  {
  	this.chartedAlerts = alerts;
  	this.chartedField = alertField;
  	this.title = title;
  	this.tooltip = tooltip;
  	init();
  }
  
  private void init()
  {
  	// set up the ui
  	
  	// roll up all of the counts for the given field
  	Map<String, Map> data = AlertUtility.countAlertFields(chartedAlerts, chartedField);
  	
  	// strip out the counts vs the alerts that are in the counts
  	Map<String,Integer> fieldVals = data.get("values");
  	final Map<String,List<Alert>> fieldAlerts = data.get("alerts");
  	
  	// determine percentages
  	Map<String, Double> fieldPcts = new HashMap<String, Double>(fieldVals.size());
  	Iterator<String> it = fieldVals.keySet().iterator();
  	while (it.hasNext())
  	{
  		String nextkey = it.next();
  		Integer nextKeyCount = fieldVals.get(nextkey);
  		double pct = nextKeyCount.doubleValue() / ((double)chartedAlerts.size());
  		fieldPcts.put(nextkey, pct);
  	}  	
  	
  	// build the chart
  	this.setLayout(new BorderLayout());
  	DefaultPieDataset dataset = new DefaultPieDataset();
  	
  	Iterator<String> it2 = fieldPcts.keySet().iterator();
  	double otherpct = 0.0;
  	List<Alert> otherAlerts = new ArrayList<Alert>(0);  // a list to collect all of the alerts marked as other, for later drilling
  	while (it2.hasNext())
  	{
  		String nextkey = it2.next();
  		double nextval = fieldPcts.get(nextkey);
  		if (nextval > 0.01) // if it is greater than 1% then show it on it's own, otherwise sum it into an 'other' pie
  		{
  			String visibleLabel = nextkey + " (" + fieldAlerts.get(nextkey).size() +")";
  			dataset.setValue(visibleLabel, nextval);
  			fieldAlerts.put(visibleLabel, fieldAlerts.get(nextkey));
  		}
  		else
  		{
  			otherpct = otherpct + nextval;
  			otherAlerts.addAll((fieldAlerts.get(nextkey)));
  		}
  	}
  	
  	if (otherpct > 0.0)
  	{
  		String otherlabel = "Other" + " (" + otherAlerts.size() +")";
  		dataset.setValue(otherlabel, otherpct);
  		fieldAlerts.put(otherlabel, otherAlerts);
  	}
  	
  	JFreeChart chart3 = ChartFactory.createPieChart3D(title, dataset, false, false, false);
		PiePlot3D plot3 = (PiePlot3D) chart3.getPlot();
	  plot3.setForegroundAlpha(0.6f);
    plot3.setCircular(true);
    
    ChartPanel chartpanel = new ChartPanel(chart3);
    
    chartpanel.addChartMouseListener(new ChartMouseListener()
    		{
					@Override
					public void chartMouseClicked(ChartMouseEvent arg0)
					{
						ChartEntity entity = arg0.getEntity();
						if (entity instanceof PieSectionEntity)
						{
							PieSectionEntity piepiece = (PieSectionEntity)entity;
							piepiece.getDataset();
							String key = piepiece.getSectionKey().toString();
							List<Alert> associatedAlerts = fieldAlerts.get(key);
							if (associatedAlerts != null)
							{							
								// Now you can drill baby!
								CreateChartPanelAction action = new CreateChartPanelAction();
								action.actionPerformed(new ActionEvent(associatedAlerts, ActionEvent.ACTION_FIRST, key));
							}
							else
							{
								System.out.println("the associated alerts are null");
							}
						}
					}

					@Override
					public void chartMouseMoved(ChartMouseEvent arg0)
					{
						// TODO Auto-generated method stub						
					}
    		});
    
    this.add(chartpanel, BorderLayout.CENTER);
    this.setPreferredSize(new Dimension(800, 600));
    
    
    // set up listeners on the clicking of the chart
//    chart3.handleClick(x, y, info);
//    ChartRenderingInfo info;
    
  }
  
	public AlertCollection getCollection()
	{
		// TODO Auto-generated method stub
		return collection;
	}

	public Map getUserPreferences()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setUserPreferences(Map preferences)
	{
		// TODO Auto-generated method stub
	}

	
	
	
	
	
	
	
	
// Tutorial code	
//  /**
//   * Creates a new demo instance.
//   * 
//   * @param title  the frame title.
//   */
//  public AlertChartPanel(String title) {
//
//      super(title);
//      JPanel panel = new JPanel(new GridLayout(2, 2));
//      DefaultPieDataset dataset = new DefaultPieDataset();
//      dataset.setValue("Section 1", 23.3);
//      dataset.setValue("Section 2", 56.5);
//      dataset.setValue("Section 3", 43.3);
//      dataset.setValue("Section 4", 11.1);
//      
//      JFreeChart chart1 = ChartFactory.createPieChart("Chart 1", dataset, false, false, false);
//      JFreeChart chart2 = ChartFactory.createPieChart("Chart 2", dataset, false, false, false);
//      PiePlot plot2 = (PiePlot) chart2.getPlot();
//      plot2.setCircular(false);
//      JFreeChart chart3 = ChartFactory.createPieChart3D("Chart 3", dataset, false, false, false);
//      PiePlot3D plot3 = (PiePlot3D) chart3.getPlot();
//      plot3.setForegroundAlpha(0.6f);
//      plot3.setCircular(true);
//      JFreeChart chart4 = ChartFactory.createPieChart3D("Chart 4", dataset, false, false, false);
//      PiePlot3D plot4 = (PiePlot3D) chart4.getPlot();
//      plot4.setForegroundAlpha(0.6f);
//
//      panel.add(new ChartPanel(chart1));
//      panel.add(new ChartPanel(chart2));
//      panel.add(new ChartPanel(chart3));
//      panel.add(new ChartPanel(chart4));
//
//      panel.setPreferredSize(new Dimension(800, 600));
//      setContentPane(panel);
//
//  }
//
//  /**
//   * The starting point for the demo.
//   * 
//   * @param args  ignored.
//   */
//  public static void main(String[] args) {
//  	AlertChartPanel demo = new AlertChartPanel("Pie Chart Demo 7");
//      demo.pack();
//      RefineryUtilities.centerFrameOnScreen(demo);
//      demo.setVisible(true);
//  }
}
