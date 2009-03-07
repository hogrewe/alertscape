/**
 * 
 */
package com.alertscape.app.license;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.alertscape.app.license.LicenseHelper;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;

import junit.framework.TestCase;

/**
 * @author josh
 *
 */
public class LicenseHelperTest extends TestCase {
  private LicenseHelper helper;
  
  protected void setUp() throws Exception {
    helper = new LicenseHelper();
  }

  /**
   * Test method for {@link com.alertscape.app.license.LicenseHelper#verify(java.lang.String)}.
   */
  public void testVerify() {
    try {
      helper.install("src/test/resources/as.lic");
      LicenseContent license = helper.verify();
      System.out.println(ToStringBuilder.reflectionToString(license));
      System.out.println(ToStringBuilder.reflectionToString(license.getExtra()));
    } catch (Exception e) {
      e.printStackTrace();
      fail("Couldn't verify");
    }
  }

  public void testExpiredVerify() {
    try {
      helper.install("src/test/resources/asexpired.lic");
      LicenseContent license = helper.verify();
      System.out.println(ToStringBuilder.reflectionToString(license));
      System.out.println(ToStringBuilder.reflectionToString(license.getExtra()));
    } catch (LicenseContentException e) {
      System.out.println("Successfully failed at verifying: " + e.getLocalizedMessage());
      return;
    } catch (Exception e) {
      e.printStackTrace();
      fail("Couldn't not verify");
    }
    fail("License passed when it should have failed");
  }

}
