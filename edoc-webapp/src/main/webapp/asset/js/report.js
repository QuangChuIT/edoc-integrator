let keyword = null;
let edocReport = {
    appSetting: {
        host: "/van-ban/-/",
        mod: "release",
        debugAgent: "web",
        root: $("#main-content"),
        cookie: 1,
        pathPlugin: "/asset",
        requestTimeout: 5000, // Timeout request for ajax,
        dataTable: null,
        mode: "inbox"
    },
    init: function () {
        let instance = this;
        appSettings = instance.appSetting;

        if (typeof jQuery === 'undefined') {
            edocReport.log("Can not load jQuery environment");
            return;
        }
        instance.renderStat();
        instance.renderReportTable("", "", keyword);
    },
    renderStat: function () {
        let instance = this;
        $.get("/public/-/document/stat", function (data, status) {
            $("#publicContent").empty();
            $('#edocPublicStatTmpl').tmpl(data).appendTo('#publicContent');
            $("#totalReport").text(data.total);
        });
    },
    renderReportTable: function (fromDate, toDate, keyword) {
        let instance = this;
        let url = "/public/-/stat/detail";
        if (keyword !== null) {
            url = url + "?keyword=" + keyword;
            if (fromDate !== "" && toDate !== "")
                url = url + "&fromDate=" + fromDate + "&toDate=" + toDate;
        } else {
            if (fromDate !== "" && toDate !== "")
                url = url + "?fromDate=" + fromDate + "&toDate=" + toDate;
        }

        let jsonData = null;
        $.ajax({
            url: url,
            type: "GET",
            dataSrc: "",
            async: false,
            success: function (data) {
                jsonData = data;
            }
        });

        instance.appSetting.dataTable = $('#edocReportTable').ejTreeGrid({
            dataSource: jsonData,
            treeColumnIndex: 0,
            isResponsive: true,
            allowSorting: true,
            allowMultiSorting:true,
            allowPaging: true,
            pageSettings: {
                pageCount: 5,
                pageSizeMode: "all",
                pageSize: "23",
            },
            allowColumnResize: true,
            //enableCollapseAll: true,
            childMapping: "childOrgan",
            sortSettings: {
                sortedColumns: [
                    { field: "total", direction: ej.sortOrder.Descending }
                ]
            },
            columns: [
                { field: "organName", headerText: app_message.edoc_organ_name, width: "550px" },
                { field: "sent", headerText: app_message.edoc_organ_sent },
                { field: "received", headerText: app_message.edoc_organ_received },
                { field: "total", headerText: app_message.edoc_organ_total}
            ],
        });
        if (fromDate !== "" && toDate !== "") {
            $("#filterLabel").html(app_message.edoc_report_filter + "<span class='time-filter'>" + fromDate + " - " + toDate + "</span>");
            $("#fromDate").val("");
            $("#toDate").val("");
        } else {
            let beginDate = new Date(new Date().getFullYear() + 1, 0, 1).formatDate();
            let currentDate = new Date().formatDate();
            $("#filterLabel").html(app_message.edoc_report_filter + "<span class='time-filter'>" + beginDate + " - " + currentDate + "</span>");
        }
    },
    exportExcel: function (fromDate, toDate) {
        let url = "/public/-/stat/export/excel";
        if (fromDate !== null && toDate !== null) {
            url = url + "?fromDate=" + fromDate + "&toDate=" + toDate;
        }
        $.ajax({
            type: "GET",
            url: url,
            beforeSend: function () {
                $("#overlay-public").show();
            },
            success: function () {
                let link = document.createElement('a');
                let href = url;
                link.style.display = 'none';
                link.setAttribute('href', href);
                link.click();
                $.notify(app_message.edoc_export_success, "success");
            },
            error: (e) => {
                $.notify(app_message.edoc_message_error_export, "error");
            }
        }).done(function () {
            $("#overlay-public").hide();
        });
    }
}
$(document).ready(function () {
    edocReport.init();
    $.datetimepicker.setLocale('vi');
    $("#fromDate").datetimepicker({
        i18n: {
            vi: {
                months: [
                    'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4',
                    'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8',
                    'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12',
                ],
                dayOfWeek: [
                    "CN", "T2", "T3", "T4",
                    "T5", "T6", "T7",
                ]
            }
        },
        timepicker: false,
        format: 'd/m/Y'
    });
    $("#toDate").datetimepicker({
        i18n: {
            vi: {
                months: [
                    'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4',
                    'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8',
                    'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12',
                ],
                dayOfWeek: [
                    "CN", "T2", "T3", "T4",
                    "T5", "T6", "T7",
                ]
            }
        },
        timepicker: false,
        format: 'd/m/Y'
    });

    $("#btnSearchReport").on("click", function (event) {
        event.preventDefault();
        let fromDate = $("#fromDate").val();
        let toDate = $("#toDate").val();
        keyword = ($("#statDetailSearch").val() === "" ? null : $("#statDetailSearch").val());
        let fromDateValue = new Date(fromDate);
        let toDateValue = new Date(toDate);
        if (fromDateValue > toDateValue) {
            $.notify(app_message.edoc_message_error_report_date, "error");
        } else {
            localStorage.removeItem("fromDateReport");
            localStorage.removeItem("toDateReport");
            localStorage.setItem("fromDateReport", fromDate);
            localStorage.setItem("toDateReport", toDate);
            edocReport.renderReportTable(fromDate, toDate, keyword);
        }
    });

    $("#exportReport").on("click", function (e) {
        e.preventDefault();
        let fromDate = localStorage.getItem("fromDateReport");
        let toDate = localStorage.getItem("toDateReport");
        edocReport.exportExcel(fromDate, toDate);
    })
});

String.format = function () {
    let s = arguments[0];
    for (let i = 0; i < arguments.length - 1; i++) {
        let reg = new RegExp("\\{" + i + "\\}", "gm");
        s = s.replace(reg, arguments[i + 1]);
    }
    return s;
}

function addCommas(nStr) {
    nStr += '';
    let x = nStr.split('.');
    let x1 = x[0];
    let x2 = x.length > 1 ? '.' + x[1] : '';
    let rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}
