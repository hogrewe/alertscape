/**
 * 
 */
package com.alertscape.web.ui.admin.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alertscape.common.model.AlertAttributeDefinition;
import com.alertscape.common.model.AlertSource;
import com.alertscape.common.model.AlertSourceType;
import com.alertscape.common.model.AuthenticatedUser;
import com.alertscape.web.service.AdminService;
import com.alertscape.web.ui.admin.client.AdminGwtService;
import com.alertscape.web.ui.admin.client.model.AttributeDefinition;
import com.alertscape.web.ui.admin.client.model.OnrampDefinition;
import com.alertscape.web.ui.admin.client.model.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author josh
 * 
 */
public class AdminGwtServiceServlet extends RemoteServiceServlet implements AdminGwtService {

  private static final long serialVersionUID = -6766700577200718416L;
  private AdminService adminService;

  public List<OnrampDefinition> getOnramps() {
    List<AlertSource> onramps = adminService.getOnramps();
    List<OnrampDefinition> results = new ArrayList<OnrampDefinition>(onramps.size());
    for (AlertSource source : onramps) {
      results.add(translate(source));
    }
    return results;
  }

  public String getTreeDefinition() {
    return adminService.getTreeDefinition();
  }

  public List<User> getUsers() {
    List<AuthenticatedUser> users = adminService.getUsers();
    List<User> results = new ArrayList<User>();

    for (AuthenticatedUser user : users) {
      results.add(translate(user));
    }
    return results;
  }

  public List<AttributeDefinition> getAttributeDefinitions() {
    List<AlertAttributeDefinition> definitions = adminService.getAttributeDefinitions();
    List<AttributeDefinition> results = new ArrayList<AttributeDefinition>(definitions.size());

    for (AlertAttributeDefinition definition : definitions) {
      results.add(translate(definition));
    }

    return results;
  }

  public void saveOnramp(OnrampDefinition onramp) {
    adminService.saveOnramp(translate(onramp));
  }

  public void saveTreeDefinition(String treeDefinition) {
    adminService.saveTreeDefinition(treeDefinition);
  }

  public void saveUser(User user, char[] password) {

    if (password == null || password.length < 1) {
      password = null;
    }

    adminService.saveUser(translate(user), password);
  }

  public void saveAttributeDefinition(AttributeDefinition definition) {
    adminService.saveAttributeDefinition(translate(definition));
  }

  public List<Map<String, String>> regexTest(String regex, Map<String, String> replacements, String[] testStrings) {
    List<Map<String, String>> results = new ArrayList<Map<String, String>>();

    Pattern p = Pattern.compile(regex);

    for (String testString : testStrings) {
      Matcher matcher = p.matcher(testString);
      if (matcher.matches()) {
        Map<String,String> a = new HashMap<String, String>();
        for (String fieldName : replacements.keySet()) {
          try {
            String value = matcher.replaceAll(replacements.get(fieldName));
            matcher.reset();
            a.put(fieldName, value);
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't replace text " + fieldName);
          }
          results.add(a);
        }
      } else {
        results.add(null);
      }
    }

    return results;
  }

  @Override
  public void init() throws ServletException {
    super.init();
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/admin-beans.xml");
    adminService = (AdminService) context.getBean("adminService");
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/admin-beans.xml");
    adminService = (AdminService) context.getBean("adminService");
  }

  protected OnrampDefinition translate(AlertSource source) {
    OnrampDefinition to = new OnrampDefinition();
    to.setId(source.getSourceId());
    to.setConfiguration(source.getConfigXml());
    to.setName(source.getSourceName());
    to.setType(source.getType().getType());
    return to;
  }

  protected AlertSource translate(OnrampDefinition source) {
    AlertSource to = new AlertSource(source.getId(), source.getName());
    to.setConfigXml(source.getConfiguration());
    AlertSourceType sourceType = new AlertSourceType();
    sourceType.setType(source.getType());
    to.setType(sourceType);

    return to;
  }

  protected User translate(AuthenticatedUser user) {
    User to = new User();
    to.setEmail(user.getEmail());
    to.setFullname(user.getFullName());
    to.setId(user.getUserId());
    to.setUsername(user.getUsername());

    return to;
  }

  protected AuthenticatedUser translate(User user) {
    AuthenticatedUser to = new AuthenticatedUser();
    to.setEmail(user.getEmail());
    to.setFullName(user.getFullname());
    to.setUserId(user.getId());
    to.setUsername(user.getUsername());

    return to;
  }

  protected AlertAttributeDefinition translate(AttributeDefinition definition) {
    AlertAttributeDefinition to = new AlertAttributeDefinition();
    to.setAttributeDefinitionId(definition.getId());
    to.setDisplayName(definition.getDisplayName());
    to.setMaxSize(definition.getSize());
    to.setName(definition.getName());

    return to;
  }

  protected AttributeDefinition translate(AlertAttributeDefinition definition) {
    AttributeDefinition to = new AttributeDefinition();
    to.setDisplayName(definition.getDisplayName());
    to.setId(definition.getAttributeDefinitionId());
    to.setName(definition.getName());
    to.setSize(definition.getMaxSize());

    return to;
  }

}
