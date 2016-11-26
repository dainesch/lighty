<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<t:siteTemplate>
    <jsp:body>
        <div class="row">
            <div class="col-xs-6 offset-xs-3">
                <div class="card card-block text-xs-center">
                    <h5 class="card-title">Login</h5>
                    <form method="POST">
                        <div class="form-group row">
                            <label for="user" class="col-xs-4 col-form-label">Username</label>
                            <div class="col-xs-4">
                                <input class="form-control" type="text" value="<c:out value="${userMod.name}"/>" id="user" name="user" placeholder="Anonymous" required>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="pass" class="col-xs-4 col-form-label">Admin pass</label>
                            <div class="col-xs-4">
                                <input class="form-control" type="password" id="pass" name="pass">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-xs-12 text-xs-right">
                                <button type="submit" class="btn btn-primary">Login</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:siteTemplate>


