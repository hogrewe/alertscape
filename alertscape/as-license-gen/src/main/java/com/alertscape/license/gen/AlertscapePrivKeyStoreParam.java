/**
 * 
 */
package com.alertscape.license.gen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.schlichtherle.license.KeyStoreParam;

/**
 * @author josh
 * 
 */
public class AlertscapePrivKeyStoreParam implements KeyStoreParam {

  @Override
  public String getAlias() {
    return "as-license-privatekey";
  }

  @Override
  public String getKeyPwd() {
    return "A13r7$c4p3R0x0rs";
  }

  @Override
  public String getStorePwd() {
    return "A13r7$c4p3R0x0rs";
  }

  @Override
  public InputStream getStream() throws IOException {
    final String resourceName = "/asPrivateKeys.store";
    final InputStream in = getClass().getResourceAsStream(resourceName);
    if (in == null) {
      throw new FileNotFoundException(resourceName);
    }
    return in;
  }
}
