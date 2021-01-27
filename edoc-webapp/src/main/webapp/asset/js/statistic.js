let edocChart = {
    appSetting: {
        host: "/chart",
        mod: "release",
        debugAgent: "web",
        root: $("#main-content"),
        cookie: 1,
        pathPlugin: "/asset",
        requestTimeout: 5000, // Timeout request for ajax,
        dataTable: null,
        mode: "report"
    },
    drawChart: function() {
        let ctx = document.getElementById('canvas').getContext('2d');
        window.myBar = new Chart(ctx, {
            type: 'bar',
            data: barChartData,
            options: {
                title: {
                    display: true,
                    text: chart_message.chart_in_year + 2020
                },
                tooltips: {
                    mode: 'index',
                    intersect: false
                },
                responsive: true,
                scales: {
                    xAxes: [{
                        stacked: true,
                    }],
                    yAxes: [{
                        stacked: true
                    }]
                }
            }
        });
    },
    renderDetailStat: function () {
        let instance = this;
        instance.dataTable = $('#dataTables-statistic').DataTable({
            ajax: {
                url: "",
                type: "GET",
                dataSrc: ""
            },
            rowId: "organId",
            responsive: true,
            pageLength: 25,
            autoWidth: true,
            ordering: true,
            searching: true,
            bDestroy: true,
            processing: true,
            paging: true,
            info: false,
            columns: [
                {
                    "title": statistic_message.table_hesder_organ_name,
                    "data": null
                },
                {
                    "title": statistic_message.table_header_total,
                    "data": null,
                },
                {
                    "title": statistic_message.table_header_sent_int,
                    "data": null,
                },
                {
                    "title": statistic_message.table_header_sent_int,
                    "data": null,
                },
                {
                    "title": statistic_message.table_header_signed,
                    "data": null
                },
                {
                    "title": statistic_message.tables_header_not_signed,
                    "data": null
                }
            ],
            language: app_message.language,
            "order": [[1, "asc"]],
        });
    }
}

let fix_sent = [5443, 6718, 8593, 7811, 7674, 9347, 10296, 10221, 10807, 10743, 10592, 16599]

let fix_received = [20610, 25496, 24886, 25020, 26631, 29516, 34652, 25120, 25953, 25919, 23776, 30787]

let sent, received;

let barChartData = {
    labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
    datasets: [{
        label: chart_message.chart_sent_document,
        backgroundColor: window.chartColors.red,
        data: fix_sent
    }, {
        label: chart_message.chart_received_document,
        backgroundColor: window.chartColors.blue,
        data: fix_received
    }]

};

$(document).ready(function() {
    $("#report-menu.nav a:not(.not-click)").on("click", function (e) {
        e.preventDefault();
        let dataMode = $(this).attr("data-mode");
        if (dataMode != null) {
            edocChart.appSetting.mode = dataMode;
            if (dataMode === "viewChart") {
                $('.edoc-content > [class^=edoc-table]').hide();
                $(".edoc-table-statistic").hide();
                edocChart.drawChart();
                $(".edoc-statistic").show();
            } else if (dataMode === "viewDetail") {
                $('.edoc-content > [class^=edoc-table]').hide();
                $(".edoc-statistic").hide();
                edocChart.renderDetailStat();
                $(".edoc-table-statistic").show();
            } else {
                edocDocument.renderDatatable();
                $(".edoc-table").show();
            }
            $("#report-menu.nav a").removeClass("active");
            $(this).addClass("active");
        }
    });

    $("#btnRunDrawChart").on('click', function(e) {
        e.preventDefault();
        console.log("clicked");
        let year = $("#year-picker").val();
        let organDomain = $(this).attr("data-id");
        ajax_chart(2020, "");
    })
})

function ajax_chart(year, organDomain) {
    let url = "/public/-/statistic/chart";
    if (organDomain != "") {
        url = url + "?year=" + year + "&organDomain=" + organDomain;
    } else {
        url = url + "?year=" + year + "&organDomain=''";
    }
    console.log(url);
    $.ajax({
        type: "GET",
        url: url,
        contentType: "application/json;charset=utf-8",
        cache: false,
        success: function(data, status) {
            console.log(data);
            sent = data.sent;
            received = data.received;
        }
    })
}