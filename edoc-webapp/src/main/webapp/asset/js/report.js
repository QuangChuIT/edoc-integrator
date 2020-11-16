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
        instance.renderReportTable("", "");
    },
    renderStat: function () {
        let instance = this;
        $.get("/public/-/document/stat", function (data, status) {
            console.log(status);
            $("#publicContent").empty();
            $('#edocPublicStatTmpl').tmpl(data).appendTo('#publicContent');
            $("#totalReport").text(data.total);
        });
    },
    renderReportTable: function (fromDate, toDate) {
        let instance = this;
        let url = "/public/-/stat/detail";
        if (fromDate !== "" && toDate !== "") {
            url = url + "?fromDate=" + fromDate + "&toDate=" + toDate;
        }
        instance.appSetting.dataTable = $('#edocReportTable').DataTable({
            ajax: {
                url: url,
                dataSrc: ""
            },
            responsive: true,
            pageLength: 20,
            searching: true,
            lengthChange: false,
            autoWidth: true,
            bDestroy: true,
            paging: true,
            info: false,
            columns: [
                {
                    "title": app_message.edoc_organ_name,
                    "data": null,
                    "render": function (data) {
                        return "<span class='organ-name'>" + data.organName + "</span>";
                    }
                },
                {
                    "title": app_message.edoc_organ_sent,
                    "data": "sent",
                },
                {
                    "title": app_message.edoc_organ_received,
                    "data": "received",
                },
                {
                    "title": app_message.edoc_organ_total,
                    "data": "total",
                }
            ],
            language: app_message.language,
            "order": [[3, "desc"]]
        });
        if (fromDate !== "" && toDate !== "") {
            $("#filterLabel").html(app_message.edoc_report_filter + "<span class='time-filter'>" + fromDate + " - " + toDate + "</span>");
            $("#fromDate").val("");
            $("#toDate").val("");
        } else {
            let currentDate = new Date().formatDate();
            $("#filterLabel").html(app_message.edoc_report_default_filter + "<span class='time-filter'>" + currentDate + "</span>");
        }

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
        let fromDateValue = new Date(fromDate);
        let toDateValue = new Date(toDate);
        if (fromDateValue > toDateValue) {
            $.notify(app_message.edoc_message_error_report_date, "error");
        } else {
            edocReport.renderReportTable(fromDate, toDate);
        }
    });
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