/**
 * 
 */
package com.alertscape.web.ui.auth;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alertscape.common.model.AuthenticatedUser;
import com.alertscape.dao.AuthenticatedUserDao;

/**
 * @author josh
 * 
 */
public class AlertscapeLoginServlet extends HttpServlet {

  private static final long serialVersionUID = 5106299198520067578L;
  private String loginJsp = "/login.jsp";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    process(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    process(req, resp);
  }

  /**
   * @param req
   * @param resp
   * @throws IOException
   * @throws ServletException
   */
  private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String action = req.getParameter("action");
    if (action == null) {
      RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(loginJsp);
      dispatcher.forward(req, resp);
    } else {
      String username = req.getParameter("username");
      String password = req.getParameter("password");
      AuthenticatedUserDao userDao = (AuthenticatedUserDao) getServletContext().getAttribute("authenticatedUserDao");
      AuthenticatedUser user;
      try {
        user = userDao.authenticate(username, password.toCharArray());

        if (user != null) {
          req.getSession().setAttribute("authUser", user);
          String forward = (String) req.getSession().getAttribute("authNextUrl");
          if(forward == null || forward.isEmpty()) {
            forward=req.getContextPath();
          }
          resp.sendRedirect(resp.encodeRedirectURL(forward));
        } else {
          req.setAttribute("error", "No user");
          RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(loginJsp);
          dispatcher.forward(req, resp);
        }
      } catch (Exception e) {
        req.setAttribute("error", "Incorrect username/password");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(loginJsp);
        dispatcher.forward(req, resp);
      }
    }
  }
}
