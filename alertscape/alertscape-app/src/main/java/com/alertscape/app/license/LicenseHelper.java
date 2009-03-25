/**
 * 
 */
package com.alertscape.app.license;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.prefs.Preferences;

import com.alertscape.app.AlertscapeServer;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * @author josh
 * 
 */
public class LicenseHelper {
  public void install(String filename) throws Exception {
    LicenseManager lm = new LicenseManager(getLicenseParam());
    File licenseFile = new File(filename);
    if (!licenseFile.exists()) {
      throw new FileNotFoundException("File " + licenseFile + " not found");
    }
    lm.install(licenseFile);
  }

  public LicenseContent verify() throws Exception {
    LicenseManager lm = new LicenseManager(getLicenseParam());
    LicenseContent content = lm.verify();
    return content;
  }

  public AL getLicense() throws Exception {
    LicenseManager lm = new LicenseManager(getLicenseParam());
    LicenseContent content = lm.verify();
    return (AL) content.getExtra();
  }

  private LicenseParam getLicenseParam() {
    CipherParam cipherParam = new DefaultCipherParam("A13r7$c4p3R0x0rs");
    KeyStoreParam keyStoreParam = new DefaultKeyStoreParam(LicenseHelper.class, "/asPublicCerts.store",
        "as-license-publiccert", "PD50$#177uk7hXBYE", null);
    LicenseParam licenseParam = new DefaultLicenseParam("AlertscapeApp",
        Preferences.userNodeForPackage(AlertscapeServer.class), keyStoreParam, cipherParam);
    return licenseParam;
  }
}
