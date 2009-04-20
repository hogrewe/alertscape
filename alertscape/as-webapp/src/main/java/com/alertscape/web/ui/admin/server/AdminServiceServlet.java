/**
 * 
 */
package com.alertscape.web.ui.admin.server;

import java.util.List;

import com.alertscape.web.ui.admin.client.AdminService;
import com.alertscape.web.ui.admin.client.model.OnrampDefinition;
import com.alertscape.web.ui.admin.client.model.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author josh
 * 
 */
public class AdminServiceServlet extends RemoteServiceServlet implements AdminService {

  private static final long serialVersionUID = -6766700577200718416L;

  public List<OnrampDefinition> getOnramps() {
    return null;
  }

  public String getTreeDefinition() {
    // TODO Auto-generated method stub
    return null;
  }

  public List<User> getUsers() {
    // TODO Auto-generated method stub
    return null;
  }

  public void saveOnramp(OnrampDefinition onramp) {
    // TODO Auto-generated method stub

  }

  public void saveTreeDefinition(String treeDefinition) {
    // TODO Auto-generated method stub

  }

  public void saveUser(User user) {
    // TODO Auto-generated method stub

  }

}
