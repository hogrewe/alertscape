package com.alertscape.browser.ui.swing.panel.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.Box;
import javax.swing.BoxLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private String version = "undefined";
	private ImageIcon logo;
	private JButton okButton;
	
	public AboutPanel(String version)
	{
		this.version = version;
		init();
	}	
	
  private void init()
  {
  	// set up the ui
  	this.setLayout(new BorderLayout());
     	
    BoxLayout mainbox = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(mainbox);

    
    URL imageUrl = getClass().getResource("/com/alertscape/images/common/alertscape_logo_about.png");
  	ImageIcon icon = new ImageIcon(imageUrl);
  	JLabel label = new JLabel(icon);
  	logo = icon;
  	JPanel logopanel = new JPanel(new BorderLayout());
  	logopanel.setBackground(Color.white);
  	logopanel.add(label, BorderLayout.CENTER);
    
  	this.add(Box.createRigidArea(new Dimension(0,5)));
    this.add(logopanel);
  	
    JPanel productNamePanel = new JPanel();
    BoxLayout productbox = new BoxLayout(productNamePanel, BoxLayout.X_AXIS);
    productNamePanel.setLayout(productbox);
    productNamePanel.add(Box.createRigidArea(new Dimension(5,0)));
    JLabel productNameLabel = new JLabel("Alertscape Management Portal (AMP)");
    productNameLabel.setFocusable(false);
    productNameLabel.setPreferredSize(new Dimension(75, 20));
    productNameLabel.setMinimumSize(new Dimension(75, 20));
    productNamePanel.add(productNameLabel);
    productNamePanel.add(Box.createRigidArea(new Dimension(5,0)));
    productNamePanel.setBackground(Color.white);
     				
    this.add(Box.createRigidArea(new Dimension(0,5)));
    this.add(productNamePanel);

    JPanel productCommentPanel = new JPanel();
    BoxLayout productCommentBox = new BoxLayout(productCommentPanel, BoxLayout.X_AXIS);
    productCommentPanel.setLayout(productCommentBox);
    productCommentPanel.add(Box.createRigidArea(new Dimension(5,0)));
    JLabel productCommentLabel = new JLabel("A component of the Alertscape Event Management System, v" + this.version );
    productCommentLabel.setFocusable(false);
    productCommentLabel.setPreferredSize(new Dimension(75, 20));
    productCommentLabel.setMinimumSize(new Dimension(75, 20));
    productCommentPanel.add(productCommentLabel);
    productCommentPanel.add(Box.createRigidArea(new Dimension(5,0)));
    productCommentPanel.setBackground(Color.white);
     				
    this.add(Box.createRigidArea(new Dimension(0,5)));
    this.add(productCommentPanel);    
        
    JPanel companyInfoPanel = new JPanel();
    BoxLayout companybox = new BoxLayout(companyInfoPanel, BoxLayout.X_AXIS);
    companyInfoPanel.setLayout(companybox);
    companyInfoPanel.add(Box.createRigidArea(new Dimension(5,0)));
    JLabel companyInfoLabel = new JLabel("Copyright Alertscape Technologies, Inc. " + Calendar.getInstance().get(Calendar.YEAR));
    companyInfoLabel.setFocusable(false);
    companyInfoLabel.setPreferredSize(new Dimension(75, 20));
    companyInfoLabel.setMinimumSize(new Dimension(75, 20));
    companyInfoPanel.add(companyInfoLabel);
    companyInfoPanel.add(Box.createRigidArea(new Dimension(5,0)));
    companyInfoPanel.setBackground(Color.white);
    
    this.add(Box.createRigidArea(new Dimension(0,5)));
    this.add(companyInfoPanel);    
     
		JPanel buttonPanel = new JPanel();
		BoxLayout buttonbox = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(buttonbox);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));		
		buttonPanel.add(Box.createHorizontalGlue());
		okButton = new JButton("Ok");
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		buttonPanel.setBackground(Color.white);
		
		this.add(buttonPanel);
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.setBackground(Color.white);   
  }	
  
	public Dimension getBaseSize()
	{
		return new Dimension(500, 250);
	}
	
	public void associateHideListener(ActionListener listener)
	{
		okButton.addActionListener(listener);
	}
}
