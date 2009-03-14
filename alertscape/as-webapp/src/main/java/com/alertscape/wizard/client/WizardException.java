/**
 * 
 */
package com.alertscape.wizard.client;

import java.io.Serializable;

/**
 * @author josh
 * 
 */
public class WizardException extends Exception implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public WizardException() {
    
  }

  public WizardException(String s) {
    super(s);
  }
}
