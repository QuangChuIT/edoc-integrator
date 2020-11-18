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
            <form class="form-horizontal" id="formImportUser" method="POST" enctype="multipart/form-data">
                <div class="row user-import" id="user-import-excel">
                    <a class="btn btn-primary import-excel-button">
                        <i class="fa fa-arrow-circle-up fa-fw"></i>
                        <spring:message code="edoc.import.report"/>
                        <input type="file" name="importUserFromExcel" id="importUserFromExcel" class="form-control">
                    </a>
                    <button class="btn btn-primary btn-report-group dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false" id="exportUserToExcel">
                        <i class="fa fa-arrow-circle-down fa-fw"></i>
                        <spring:message code="edoc.export.report"/>
                    </button>
                    <button class="btn btn-success adduser-button" id="addUser">
                        <i class="glyphicon glyphicon-plus"></i>
                        <spring:message code="edoc.button.add.user"/>
                    </button>
                </div>
            </form>
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTables-user">
            </table>
        </div>
    </div>
    <div class="edoc-table-organ">
        <div class="table-responsive">
            <form class="form-horizontal" id="formImportOrgan" method="POST" enctype="multipart/form-data">
                <div class="row user-import" id="organ-import-excel">
                    <a class="btn btn-primary import-excel-button">
                        <i class="fa fa-arrow-circle-up fa-fw"></i>
                        <spring:message code="edoc.import.report"/>
                        <input type="file" name="importOrganFromExcel" id="importOrganFromExcel" class="form-control">
                    </a>
                    <button class="btn btn-primary btn-report-group dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false" id="exportOrganToExcel">
                        <i class="fa fa-arrow-circle-down fa-fw"></i>
                        <spring:message code="edoc.export.report"/>
                    </button>
<%--                    <button class="btn btn-success addorgan-button" id="addOrgan">--%>
<%--                        <i class="glyphicon glyphicon-plus"></i>--%>
<%--                        <spring:message code="edoc.button.add.organ"/>--%>
<%--                    </button>--%>
                </div>
            </form>
            <table class="table table-striped table-bordered table-hover custom-datatable" id="dataTables-organ">
            </table>
        </div>
    </div>
</div>
