<%@ page import="com.bkav.edoc.web.auth.CookieUtil" %>
<%@ page import="com.bkav.edoc.web.OAuth2Constants" %>
<%@ page import="com.bkav.edoc.service.database.cache.UserCacheEntry" %>
<%@ page import="com.bkav.edoc.service.kernel.util.Base64" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.bkav.edoc.web.util.PropsUtil" %>
<%@ page import="com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil" %>
<%@ page import="com.bkav.edoc.service.database.cache.OrganizationCacheEntry" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userLoginCookie = CookieUtil.getValue(request, OAuth2Constants.USER_LOGIN);
    String userLogin = new String(Base64.decode(userLoginCookie), StandardCharsets.UTF_8);
    UserCacheEntry user = new Gson().fromJson(userLogin, UserCacheEntry.class);
    List<OrganizationCacheEntry> organizationCacheEntries = EdocDynamicContactServiceUtil.getDyCacheEntriesByAgency(true);
%>
<nav class="navbar navbar-default nav-top-header" id="header-nav">
    <div class="navbar-header edoc-header">
        <a href="<c:url value="/"/>">
            <img src="<c:url value="/asset/img/Logo.png"/>" alt=""/>
            <span><spring:message code="edoc.header.title"/></span>
        </a>
    </div>
    <div class="search-container">
        <input class="search-input" id="search-edoc" type="search" placeholder="<spring:message code="edoc.search"/>">
        <a href="javascript:void(0)" type="button" data-toggle="modal-popover" data-placement="bottom" id="search-filter">
            <i class="fa fa-filter fa-lg" title="<spring:message code="edoc.search.filter"/>"></i>
        </a>
    </div>
    <div class="popover" id="searchFilter">
        <div class="popover-content">
            <div class="form-input">
                <label class="control-label" for="fromOrganSearch">
                    <spring:message code="edoc.search.fromOrgan"/>
                </label>
                <select class="form-control" multiple="multiple" id="fromOrganSearch">
                    <%
                        for (OrganizationCacheEntry organization: organizationCacheEntries) {
                    %>
                    <option value="<%=organization.getDomain()%>"><%=organization.getName()%></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="form-input">
                <label class="control-label" for="toOrganSearch">
                    <spring:message code="edoc.search.toOrgan"/>
                </label>
                <select class="form-control" multiple="multiple" id="toOrganSearch">
                    <%
                        for (OrganizationCacheEntry organization: organizationCacheEntries) {
                    %>
                    <option value="<%=organization.getDomain()%>"><%=organization.getName()%></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="form-input">
                <label class="control-label" for="docCodeSearch">
                    <spring:message code="edoc.search.docCode"/>
                </label>
                <input type="text" id="docCodeSearch" value="">
            </div>
            <hr>
            <div class="form-input">
                <div class="form-group" id="btn-search-filter">
                    <button class="btn btn-danger" id="btn-searchFilter-reset">
                        <spring:message code="edoc.button.search.reset"/>
                    </button>
                    <button class="btn btn-success" id="btn-searchFilter-confirm">
                        <spring:message code="edoc.button.confirm"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
    </button>
    <ul class="nav navbar-right navbar-top-links">
        <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)">
                <i class="fa fa-user fa-fw"></i><%=user.getDisplayName()%>
                <b class="caret"></b>
            </a>
            <ul class="dropdown-menu dropdown-user">
                <li>
                    <a class="user-info" href="javascript:void(0)" data-id="<%=user.getUserId()%>">
                        <i class="fa fa-user fa-fw"></i>
                        <spring:message code="user.profile"/>
                    </a>
                </li>
                <%
                    String changePassUrl = PropsUtil.get("user.change.password.url");
                %>
                <li>
                    <a class="change-password" href="<%= changePassUrl %>" target="_blank">
                        <i class="fa fa-key fa-fw"></i>
                        <spring:message code="user.change.password"/>
                    </a>
                </li>
                <li>
                    <a class="logout" href="<c:url value="/logout"/>">
                        <i class="fa fa-sign-out fa-fw"></i>
                        <spring:message code="logout"/>
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</nav>