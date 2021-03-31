<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page trimDirectiveWhitespaces="true" %>

<div class="edoc-content">
    <div class="edoc-table">
        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTables-edoc">
            </table>
        </div>
    </div>
    <div class="edoc-table-draft">
        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTablesDraftDoc">
            </table>
        </div>
    </div>
    <div class="edoc-table-user">
        <div class="table-responsive">
            <div class="row user-import" id="user-import-excel">
                <button class="btn btn-primary import-excel-button" id="importUserFromExcel">
                    <i class="fa fa-arrow-circle-up fa-fw"></i>
                    <spring:message code="edoc.import.report"/>
                </button>
                <button class="btn btn-primary btn-report-group" id="exportUserToExcel">
                    <i class="fa fa-arrow-circle-down fa-fw"></i>
                    <spring:message code="edoc.export.report"/>
                </button>
                <button class="btn btn-success adduser-button" id="addUser">
                    <i class="glyphicon glyphicon-plus"></i>
                    <spring:message code="edoc.button.add.user"/>
                </button>
                <button class="btn btn-info sync-sso-button" id="syncUserSSO">
                    <i class="fa fa-rocket fa-fw"></i>
                    <spring:message code="edoc.button.sync.user"/>
                </button>
            </div>
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTables-user">
            </table>
        </div>
    </div>
    <div class="edoc-table-organ">
        <div class="table-responsive">
            <div class="row user-import" id="organ-import-excel">
                <button class="btn btn-primary import-excel-button" id="importOrganFromExcel">
                    <i class="fa fa-arrow-circle-up fa-fw"></i>
                    <spring:message code="edoc.import.report"/>
                </button>
                <button class="btn btn-primary btn-report-group" id="exportOrganToExcel">
                    <i class="fa fa-arrow-circle-down fa-fw"></i>
                    <spring:message code="edoc.export.report"/>
                </button>
                <button class="btn btn-success addorgan-button" id="addOrgan">
                    <i class="glyphicon glyphicon-plus"></i>
                    <spring:message code="edoc.button.add.organ"/>
                </button>
                <%--<button class="btn btn-danger deleteorgan-button" id="deleteOrgans">
                    <i class="glyphicon glyphicon-remove"></i>
                    <spring:message code="edoc.button.delete.organs"/>
                </button>--%>
            </div>
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTables-organ">
            </table>
        </div>
    </div>
    <div class="edoc-table-not-taken">
        <div class="table-responsive">
            <div class="row user-import" id="warning-document-not-taken">
                <button class="btn btn-primary import-excel-button" id="put-to-telegram">
                    <i class="fa fa-telegram"></i>
                    <spring:message code="edoc.not.taken.warning.telegram"/>
                </button>
                <%--<button class="btn btn-primary import-excel-button" id="put-to-email">
                    <i class="fa fa-envelope"></i>
                    <spring:message code="edoc.not.taken.warning.email"/>
                </button>--%>
            </div>
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTables-edoc-notTaken">
            </table>
        </div>
    </div>
    <div class="edoc-table-not-sendPCP">
        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTables-edoc-not-sendVPCP">
            </table>
        </div>
    </div>
    <div class="edoc-statistic">
        <div class="table-responsive">
            <div class="row user-import" id="search-statistic-header">
                <div class="col-md-2 col-sm-6 col-xs-12" id="search-by-year">
                    <select class="report-time form-control" id="yearPicker"></select>
                </div>
                <button class="btn btn-info btn-report-group" id="btnRunDrawChart">
                    <i class="fa fa-calculator fa-fw"></i>
                    <spring:message code="edoc.report.button"/>
                </button>
            </div>
            <div id="chart-area">
                <canvas id="canvas"></canvas>
            </div>
        </div>
    </div>
    <div class="edoc-table-statistic">
        <div class="table-responsive">
            <div class="row report-search" id="statistic-detail">
                <div class="col-md-2 col-sm-6 col-xs-12">
                    <input type="text" class="report-time form-control" readonly
                           id="fromStatDate" value="" name="fromStatDate"
                           placeholder="<spring:message code="edoc.search.from.date"/>">
                </div>
                <div class="col-md-2 col-sm-6 col-xs-12">
                    <div class="form-inline form-inline">
                        <input type="text" class="report-time form-control"
                               readonly id="toStatDate" name="toStatDate" value=""
                               placeholder="<spring:message code="edoc.search.to.date"/>">
                    </div>
                </div>
                <ul class="nav navbar-top-links report-action">
                    <li>
                        <button class="btn btn-info btn-report-group" id="btnRunStatistic">
                            <i class="fa fa-calculator fa-fw"></i>
                            <spring:message code="edoc.report.button"/>
                        </button>
                    </li>
                    <li class="dropdown">
                        <button class="btn btn-primary btn-report-group" aria-haspopup="true" aria-expanded="false" id="exportStatistic">
                            <i class="fa fa-arrow-circle-down fa-fw"></i>
                            <spring:message code="edoc.export.report"/>
                        </button>
                    </li>
                </ul>
            </div>
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTables-statistic">
            </table>
        </div>
    </div>
</div>
<div id="overlay-edoc-not-taken">
    <div class="cv-spinner">
        <span class="loading-spinner"></span>
        <span>&nbsp;Đang xử lý</span>
    </div>
</div>
