<jsp:useBean id="managedrawDraw" scope="session" class="fr.paris.lutece.plugins.draw.web.DrawJspBean" />
<% String strContent = managedrawDraw.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
