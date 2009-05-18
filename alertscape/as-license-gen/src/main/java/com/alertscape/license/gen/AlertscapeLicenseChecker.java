/**
 * 
 */
package com.alertscape.license.gen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.alertscape.app.license.AlertscapeLicense;
import com.alertscape.app.license.LicenseHelper;

import de.schlichtherle.license.LicenseContent;

/**
 * @author josh
 *
 */
public class AlertscapeLicenseChecker {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    Reader r = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(r);
    
    System.out.println("File to check: ");
    String filename = br.readLine();
    LicenseHelper helper = new LicenseHelper();
    helper.install(filename);
    LicenseContent content = helper.verify();
    AlertscapeLicense license = helper.getLicense();
    
    System.out.println(ToStringBuilder.reflectionToString(content));
    System.out.println(ToStringBuilder.reflectionToString(license));
  }

}
