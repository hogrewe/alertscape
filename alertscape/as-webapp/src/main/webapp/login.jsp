<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html dir="ltr" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml"
	lang="en">
<head>


<title>Welcome to Alertscape | Alertscape Technologies</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="shortcut icon" href="fourseasons_favicon.png"
	type="image/x-icon" />

</head>
<body class="sidebar-right">
<div id="headline"><img src="alertscape_logo.gif" alt="" id="logo" />
<div id="site-slogan">Centralize. Simplify. Manage. Automate.</div>
</div>


<div class="content"
	style="width: 100%; text-align: center; margin-top: 4em; margin-bottom: 8em;">
<form action="<%=request.getContextPath()%>/login" method="post">
<table border="0">
	<tr>
		<td>Username:</td>
		<td><input type="text" name="username" /></td>
	</tr>
	<tr>
		<td>Password:</td>
		<td><input type="password" name="password" /></td>
	</tr>
	<tr>
		<td colspan="2">
		<%
		  if (request.getAttribute("error") != null) {
		    out.print(request.getAttribute("error"));
		  }
		%>
		</td>
	</tr>
	<tr>
		<td colspan="2"><input type="submit" name="action" value="Login" />
		</td>
	</tr>
</table>
</form>
</div>

<div id="footer-message">Copyright 2009, Alertscape
Technologies,Inc.</div>


</body>
</html>