/**
 * 
 */
package com.alertscape.license.gen;

import java.util.prefs.Preferences;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseParam;

/**
 * @author josh
 *
 */
public class AlertscapeLicenseParam implements LicenseParam {
  
  private String subject;

  public AlertscapeLicenseParam(String subject) {
    this.subject = subject;
  }

  @Override
  public CipherParam getCipherParam() {
    return new AlertscapePrivCipherParam();
  }

  @Override
  public KeyStoreParam getKeyStoreParam() {
    return new AlertscapePrivKeyStoreParam();
  }

  @Override
  public Preferences getPreferences() {
//    return Preferences.userNodeForPackage(AlertscapeServer.class);
    return null;
  }

  @Override
  public String getSubject() {
    return subject;
  }
}
