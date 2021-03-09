<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<header class="public-header">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-11 col-sm-6 col-xs-12">
                <h3>
                    <span class="header-title">
                        <spring:message code="edoc.report.title"/>
                    </span>
                    <small id="updateTime"><spring:message code="edoc.report.update.time"/>
                        <fmt:formatDate type="both" pattern="dd/MM/yyyy HH:mm:ss" value="${now}"/> ) </small>
                </h3>
            </div>
            <div class="col-md-1 col-sm-6 col-xs-12">
                <h3 class="public-stat-total">
                    <small>
                        <span class="label label-success pull-right">
                            <spring:message code="edoc.report.total"/>
                            <span id="totalReport">
                            </span>
                        </span>
                    </small>
                </h3>
            </div>
        </div>
    </div>
</header>
<div class="report-main">
    <div class="edoc-report-table">
        <div class="row report-search">
            <div class="col-md-2 col-sm-6 col-xs-12">
                <input type="text" class="report-time form-control" readonly
                       id="fromDate" value="" name="fromDate"
                       placeholder="<spring:message code="edoc.search.from.date"/>">
            </div>
            <div class="col-md-2 col-sm-6 col-xs-12">
                <div class="form-inline form-inline">
                    <input type="text" class="report-time form-control"
                           readonly id="toDate" name="toDate" value=""
                           placeholder="<spring:message code="edoc.search.to.date"/>">
                </div>
            </div>
            <div class="col-md-2 col-sm-6 col-xs-12">
                <input class="form-control" type="search" id="statDetailSearch" value="" placeholder="Nhập từ khóa...">
            </div>
            <ul class="nav navbar-top-links report-action">
                <li>
                    <button class="btn btn-info btn-report-group" id="btnSearchReport">
                        <i class="fa fa-calculator fa-fw"></i>
                        <spring:message code="edoc.report.button"/>
                    </button>
                </li>
                <li class="dropdown">
                    <button class="btn btn-primary btn-report-group" aria-haspopup="true" aria-expanded="false" id="exportReport">
                        <i class="fa fa-arrow-circle-down fa-fw"></i>
                        <spring:message code="edoc.export.report"/>
                    </button>
<%--                    <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">--%>
<%--                        <li><a class="dropdown-item" id="exportReportPDF" href="javascript:void(0)">PDF</a></li>--%>
<%--                        <li><a class="dropdown-item" id="exportReportExcel" href="javascript:void(0)">Excel</a></li>--%>
<%--                    </ul>--%>
                </li>
            </ul>
        </div>
        <div class="row message-filter">
            <div class="col-md-12 col-xs-12 col-sm-12">
                <p id="filterLabel"></p>
            </div>
        </div>
        <div class="row">
            <div class="table-responsive">
                <table class="table table-striped table-bordered table-hover custom-datatable" id="edocReportTable">
                </table>
            </div>
        </div>
    </div>
</div>