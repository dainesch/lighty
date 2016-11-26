<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <meta name="description" content="Lighty Control">
        <meta name="author" content="Dainesch">
        <link rel="icon" href="../../favicon.ico">

        <title>${site.title}</title>

        <c:forEach var="file" items="${site.cssFiles}">
            <link href="<c:url value="${file}"/>" rel="stylesheet">
        </c:forEach>

    </head>

    <body>

        <nav class="navbar navbar-fixed-top navbar-dark bg-inverse">
            <a class="navbar-brand" href="<c:url value="/site/index"/>">Lighty</a>
            <ul class="nav navbar-nav">
                <li class="nav-item <c:if test="${'Home' eq site.activeMenu}">active</c:if>">
                    <a class="nav-link" href="<c:url value="/site/index"/>">Image mode</a>
                </li>
                <li class="nav-item <c:if test="${'Audio' eq site.activeMenu}">active</c:if>">
                    <a class="nav-link" href="<c:url value="/site/audio"/>">Audio Mode</a>
                </li>
                <li class="nav-item <c:if test="${'Promo' eq site.activeMenu}">active</c:if>">
                    <a class="nav-link" href="<c:url value="/site/promo"/>">Pro Mode</a>
                </li>
            </ul>
            <ul class="nav navbar-nav pull-xs-right">
                <c:choose>
                    <c:when test="${userMod.loggedIn}">
                        <li class="nav-item active">
                            <a class="nav-link" href="<c:url value="/site/user/login"/>"><c:out value="${userMod.name}"/></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item active">
                            <a class="nav-link" href="<c:url value="/site/user/login"/>">Login</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>

        <div id="mainCont" class="container-fluid">

            <jsp:doBody/>

        </div>



        <c:forEach var="file" items="${site.jsFiles}">
            <script src="<c:url value="${file}"/>"></script>
        </c:forEach>

    </body>
</html>
