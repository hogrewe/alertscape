/**
 * 
 */
package com.alertscape.license.gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.alertscape.app.license.AlertscapeLicense;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * @author josh
 * 
 */
public class AlertscapeLicenseGenerator {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws Exception {
    Reader r = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(r);

    LicenseContent result = new LicenseContent();

    X500Principal issuer = new X500Principal("CN=Alertscape Technologies, L=Denver, ST=CO, O=Unknown,OU=Unknown, DC=US");
    result.setIssuer(issuer);
    result.setConsumerAmount(1);
    result.setConsumerType("User");
    Date now = new Date();
    result.setIssued(now);
    result.setSubject("AlertscapeApp");

    System.out.print("Issue to: ");
    String issueTo = br.readLine();

    X500Principal holder = new X500Principal("CN=" + issueTo);
    result.setHolder(holder);

    System.out.print("Expire on (yyyy-MM-dd): ");
    String expireAt = br.readLine();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date expireDate = null;
    try {
      expireDate = sdf.parse(expireAt);
    } catch (ParseException e1) {
      e1.printStackTrace();
      System.exit(1);
    }
    result.setNotAfter(expireDate);

    System.out.print("License file: ");
    String filename = br.readLine();

    AlertscapeLicense license = new AlertscapeLicense();

    System.out.print("Number of onramps: ");
    license.setOnramps(readInt(br));

    System.out.print("Number of offramps: ");
    license.setOfframps(readInt(br));

    System.out.print("Number of AMP users: ");
    license.setAmpUsers(readInt(br));

    result.setExtra(license);

    LicenseParam licenseParam = new AlertscapeLicenseParam("AlertscapeApp");
    LicenseManager lm = new LicenseManager(licenseParam);
    lm.store(result, new File(filename));

    System.out.println("License generated");
    System.out.println(ToStringBuilder.reflectionToString(result));
    System.out.println(ToStringBuilder.reflectionToString(result.getExtra()));
  }

  private static int readInt(BufferedReader br) throws Exception {
    String line = br.readLine();
    return Integer.parseInt(line);
  }

}
