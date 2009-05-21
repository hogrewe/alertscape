/**
 * 
 */
package com.alertscape.web.ui.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alertscape.common.logging.ASLogger;
import com.alertscape.common.model.AuthenticatedUser;

/**
 * @author josh
 * 
 */
public class AlertscapeAuthFilter implements Filter {
  private static final ASLogger LOG = ASLogger.getLogger(AlertscapeAuthFilter.class);

  private String loginUrl;
  private List<AuthorizedUrl> urls;

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
      ServletException {
    if (req instanceof HttpServletRequest) {
      HttpServletRequest httpReq = (HttpServletRequest) req;

      String servletPath = httpReq.getServletPath();

      String requiredRole = getRequiredRole(servletPath);

      if (requiredRole == null) {
        chain.doFilter(req, resp);
        return;
      }

      HttpSession session = httpReq.getSession();
      HttpServletResponse httpResp = (HttpServletResponse) resp;

      if (session == null || session.getAttribute("authUser") == null) {
        // Redirect to login
        if (session != null) {
          session.setAttribute("authNextUrl", httpReq.getRequestURI());
        }
        String redirectURL = httpResp.encodeRedirectURL(httpReq.getContextPath() + loginUrl);
        httpResp.sendRedirect(redirectURL);
      } else {
        AuthenticatedUser user = (AuthenticatedUser) session.getAttribute("authUser");

        if (!user.getRoles().contains(requiredRole)) {
          LOG.warn("User: " + user + " attempted to access " + servletPath + " but was not authorized");
          // Not authorized
          httpResp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
          // We're good, let them in
          chain.doFilter(req, resp);
        }
      }
    }
  }

  private String getRequiredRole(String servletPath) {
    for (AuthorizedUrl authUrl : urls) {
      if (authUrl.matches(servletPath)) {
        return authUrl.getRole();
      }
    }
    return null;
  }

  @Override
  public void init(FilterConfig config) throws ServletException {

    loginUrl = "/login";

    urls = new ArrayList<AuthorizedUrl>();

    urls.add(new AuthorizedUrl("/com.alertscape.web.ui.admin.AlertscapeAdmin/index.html", "admin"));
    urls.add(new AuthorizedUrl("/com.alertscape.web.ui.admin.AlertscapeAdmin/adminService.gwt", "admin"));
  }
}
