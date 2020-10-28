let organManage = {
    organSetting: {
        host: "/contact/-/document/contacts",
        mod: "release",
        debugAgent: "web",
        root: $("#main-content"),
        cookie: 1,
        pathPlugin: "/asset",
        requestTimeout: 5000, // Timeout request for ajax,
        dataTable: null,
        mode: "system"
    },
    renderOrganDatatable: function () {
        let instance = this;
        instance.dataTable = $('#dataTables-organ').DataTable({
            ajax: {
                url: "/contact/-/document/contacts",
                dataSrc: ""
            },
            rowId: "id",
            responsive: true,
            pageLength: 25,
            autoWidth: true,
            bDestroy: true,
            processing: true,
            paging: true,
            info: false,
            columns: [
                {
                    "title": organ_message.table_header_name,
                    "data": null,
                    "render": function (data) {
                        return $('#organNameTemplate').tmpl(data).html();
                    }
                },
                {
                    "title": organ_message.table_header_domain,
                    "data": "domain",
                },
                {
                    "title": organ_message.table_header_email,
                    "data": "email",

                },
                {
                    "title": organ_message.organ_status,
                    "data": null,
                    "render": function (data) {
                        if (data.status === true) {
                            return organ_message.organ_status_active;
                        } else {
                            return organ_message.organ_status_deactive;
                        }
                    }
                }
            ],
            language: app_message.language,
            "order": [[0, "asc"]]
        });
    },
    reGenerateToken: function (organId) {
        $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: "/contact/-/document/contact/token/" + organId,
            cache: false,
            success: function (response) {
                console.log(response);
                $("#token").val(response.token);
            },
            error: function (error) {
                $.notify(error.responseText, "error");
            }
        });
    }
}
$(document).ready(function () {

    $("#dataTables-organ").on('click', 'tbody>tr', function () {
        let organId = $(this).attr("id");
        console.log(organId);
        $.get("/contact/-/document/contacts/" + organId, function (data) {
            console.log(data);
            $('#organ-detail').empty();
            $('#organDetailTemplate').tmpl(data).appendTo('#organ-detail');
        });
        $('#organDetail').modal({
            backdrop: 'static',
            keyboard: false
        });
    });
});
$(document).on("click", ".organ-change-token", function (e) {
    e.preventDefault();
    let organId = $(this).attr("data-id");

    if (organId !== "") {
        organManage.reGenerateToken(organId);
    }
});
$(document).on("click", ".organ-view-token", function (e) {
    e.preventDefault();
    if ($(".input-token").attr('password-shown') === 'false') {

        $(".input-token").removeAttr('type');
        $(".input-token").attr('type', 'text');

        $(".input-token").removeAttr('password-shown');
        $(".input-token").attr('password-shown', 'true');
    } else {
        $(".input-token").removeAttr('type');
        $(".input-token").attr('type', 'password');

        $(".input-token").removeAttr('password-shown');
        $(".input-token").attr('password-shown', 'false');
    }
});

