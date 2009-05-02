package com.alertscape.browser.ui.swing.panel.collection.chart;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValueDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.alertscape.browser.localramp.firstparty.preferences.UserPreferencesPanel;
import com.alertscape.browser.ui.swing.panel.collection.AlertCollectionPanel;
import com.alertscape.common.model.Alert;
import com.alertscape.common.model.AlertCollection;
import com.alertscape.common.util.AlertUtility;

public class AlertBarChartPanel extends JPanel implements AlertCollectionPanel, UserPreferencesPanel
{
	private static final long serialVersionUID = 1L;
  private AlertCollection collection;
  private String chartedField;
  private List<Alert> chartedAlerts;
  private String title;
  private String tooltip;
  
  public AlertBarChartPanel(List<Alert> alerts, String alertField, String title, String tooltip)
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
  	
  	DefaultCategoryDataset dataset = new DefaultCategoryDataset();

  	// sort the map
  	HashMap sortedMap = sortHashMapByValues((HashMap) fieldVals, false);
  	
  	// build the chart dataset
  	Iterator<String> it = sortedMap.keySet().iterator();
  	while (it.hasNext())
  	{
  		String nextkey = it.next();
  		Integer nextKeyCount = (Integer) sortedMap.get(nextkey);  		
  		dataset.setValue(nextKeyCount, nextkey, nextkey);
  	}  	
  	
  	
  	
  	// build the chart
  	this.setLayout(new BorderLayout());
  	 	
  	boolean showlabels = (dataset.getRowCount() < 30);
  	JFreeChart chart3 = ChartFactory.createBarChart3D(title, chartedField, "Total Alerts", dataset, PlotOrientation.VERTICAL, false, true, false);  	
  	CategoryPlot p = chart3.getCategoryPlot(); 
	  p.setForegroundAlpha(0.6f);    

    // rotate the labels of the bar chart so they are readable
    CategoryPlot plot = (CategoryPlot)chart3.getPlot();
    CategoryAxis xAxis = (CategoryAxis)plot.getDomainAxis();
    xAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
    xAxis.setMaximumCategoryLabelLines(1);
    xAxis.setTickLabelsVisible(showlabels);
	  
    ChartPanel chartpanel = new ChartPanel(chart3);
   
    chartpanel.addChartMouseListener(new ChartMouseListener()
    		{
					@Override
					public void chartMouseClicked(ChartMouseEvent arg0)
					{
						ChartEntity entity = arg0.getEntity();
						if (entity instanceof CategoryItemEntity)
						{
							CategoryItemEntity piepiece = (CategoryItemEntity)entity;
							piepiece.getDataset();
							String key = piepiece.getColumnKey().toString();
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

	
	public LinkedHashMap sortHashMapByValues(HashMap passedMap, boolean ascending) {

	  List mapKeys = new ArrayList(passedMap.keySet());
	List mapValues = new ArrayList(passedMap.values());
	Collections.sort(mapValues);
	Collections.sort(mapKeys);

	if (!ascending)
	Collections.reverse(mapValues);

	LinkedHashMap someMap = new LinkedHashMap();
	Iterator valueIt = mapValues.iterator();
	while (valueIt.hasNext()) {
	Object val = valueIt.next();
	Iterator keyIt = mapKeys.iterator();
	while (keyIt.hasNext()) {
	Object key = keyIt.next();
	if (passedMap.get(key).toString().equals(val.toString())) {
	passedMap.remove(key);
	mapKeys.remove(key);
	someMap.put(key, val);
	break;
	}
	}
	}
	return someMap;
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
