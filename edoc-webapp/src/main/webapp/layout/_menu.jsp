<%@ page import="com.bkav.edoc.web.auth.CookieUtil" %>
<%@ page import="com.bkav.edoc.web.OAuth2Constants" %>
<%@ page import="com.bkav.edoc.service.kernel.util.Base64" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.bkav.edoc.service.database.util.UserRoleServiceUtil" %>
<%@ page import="com.bkav.edoc.service.database.entity.User" %>
<%@ page import="com.bkav.edoc.service.database.entity.UserRole" %>
<%@ page import="com.bkav.edoc.service.database.entity.Role" %>
<%@ page import="com.bkav.edoc.service.database.util.RoleServiceUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String userCookie = CookieUtil.getValue(request, OAuth2Constants.USER_LOGIN);
    String userLog = new String(Base64.decode(userCookie), StandardCharsets.UTF_8);
    User user = new Gson().fromJson(userLog, User.class);
    UserRole userRole = UserRoleServiceUtil.getUserRoleByUserId(user.getUserId());
    String roleKey = "USER";
    /*if(userRole != null){
        long roleId= userRole.getRoleId();
        Role role = RoleServiceUtil.getRole(roleId);
        roleKey = role.getRoleKey();
    }*/
%>
<div class="edoc-action">
    <button class="btn btn-lg btn-create-edoc">
        <i class="fa fa-edit fa-fw"></i>
        <span class="menu-title"><spring:message code="button.create.document"/></span>
    </button>
</div>
<ul class="nav nav-tabs" id="edoc-tab">
    <li class="active edoc-document-tab">
        <a href="javascript:void(0)" id="mainTab">
            <i class="fa fa-file-text-o fa-fw"></i>
            <span class="menu-title"><spring:message code="edoc.document.tab.title"/></span>
        </a>
    </li>
    <li class="edoc-report-tab">
        <a href="javascript:void(0)" id="reportTab">
            <i class="fa fa-bar-chart-o fa-fw"></i>
            <span class="menu-title"><spring:message code="edoc.report.tab.title"/></span>
        </a>
    </li>
</ul>
<nav class="navbar-default sidebar fade in tab-container" id="mainTabC">
    <ul class="nav edoc-menu" id="side-menu">
        <li>
            <a href="javascript:void(0)" data-mode="inbox" class="active not-click">
                <i class="fa fa-inbox fa-fw"></i>
                <span class="menu-title">
                            <spring:message code="menu.receiver.document"/>
                        </span>
                <span class="fa arrow"></span>
            </a>
            <ul class="nav nav-second-level">
                <li>
                    <a href="javascript:void(0)" data-mode="inboxReceived"><spring:message
                            code="menu.received.document"/></a>
                </li>
                <li>
                    <a href="javascript:void(0)" data-mode="inboxNotReceived"><spring:message
                            code="menu.not.received.document"/></a>
                </li>
            </ul>
            <!-- /.nav-second-level -->
        </li>
        <li>
            <a href="javascript:void(0)" data-mode="outbox" id="outbox-menu">
                <i class="fa fa-check fa-fw"></i>
                <span class="menu-title"><spring:message code="menu.document.send"/></span>
            </a>
        </li>
        <li>
            <a href="javascript:void(0)" class="draft-menu" data-mode="draft" id="draft-menu">
                <i class="fa fa-tasks fa-fw"></i>
                <span class="menu-title"><spring:message code="menu.document.draft"/></span>
            </a>
        </li>
        <%
            if(roleKey.equals("ADMIN") || roleKey.equals("SUPER ADMIN")) {
        %>
        <li>
            <a href="javascript:void(0)" data-mode="system" class="system-management-menu not-click">
                <i class="fa fa-cogs fa-fw"></i>
                <span class="menu-title"><spring:message code="menu.system.management"/></span>
                <span class="fa arrow"></span>
            </a>
            <ul class="nav nav-second-level">
                <li>
                    <a href="javascript:void(0)" data-mode="userManage" id="user-menu"><spring:message
                            code="menu.system.management.user"/></a>
                </li>
                <li>
                    <a href="javascript:void(0)" data-mode="organManage" id="organ-menu"><spring:message
                            code="menu.system.management.organ"/></a>
                </li>
                <li>
                    <a href="javascript:void(0)" class="email-template-menu" data-mode="draft" id="email-template-menu">
                        <spring:message code="menu.email.template.manage"/>
                    </a>
                </li>
            </ul>
            <!-- /.nav-second-level -->
        </li>
        <%
            }
        %>
    </ul>
</nav>
<nav id="reportTabC" class="navbar-default sidebar fade in tab-container">
    <ul class="nav edoc-menu" id="report-menu">
        <li>
            <a href="javascript:void(0)" data-mode="report" class="report-menu not-click">
                <i class="fa fa-table fa-fw"></i>
                <span class="menu-title"><spring:message code="edoc.report.statistic"/></span>
                <span class="fa arrow"></span>
            </a>
            <ul class="nav nav-second-level">
                <li>
                    <a href="javascript:void(0)" data-mode="viewChart" id="repot-chart"><spring:message
                            code="edoc.report.chart"/></a>
                </li>
                <%
                    if(roleKey.equals("ADMIN") || roleKey.equals("SUPER ADMIN")) {
                %>
                <li>
                    <a href="javascript:void(0)" data-mode="viewDetail" id="report-detail"><spring:message
                            code="edoc.report.view.detail"/></a>
                </li>
                <%
                    }
                %>
            </ul>
        </li>
    </ul>
</nav>
