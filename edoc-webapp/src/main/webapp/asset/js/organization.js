// let organButtons = '<form class="form-horizontal" id="formImportUser" method="POST" enctype="multipart/form-data">\n' +
//     '                <div class="row user-import" id="organ-import">\n' +
//     '                    <a class="btn btn-primary import-excel-button">\n' +
//     '                        <i class="fa fa-arrow-circle-up fa-fw"></i>\n' +
//     '                        <spring:message code="edoc.import.report"/>\n' +
//     '                        <input type="file" name="importExcel" id="importExcel" class="form-control">\n' +
//     '                    </a>\n' +
//     '                    <button class="btn btn-success adduser-button" id="addOrgan">\n' +
//     '                        <i class="glyphicon glyphicon-plus"></i>\n' +
//     '                        <spring:message code="edoc.button.add.organ"/>\n' +
//     '                    </button>\n' +
//     '                </div>\n' +
//     '            </form>';

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
            serverSide: true,
            processing: true,
            pageLength: 25,
            ajax: {
                url: "/contact/-/document/contacts",
                dataSrc: "",
                type: "GET"
            },
            drawCallback: function () {
                $(this).contextMenu({
                    selector: 'tr td',
                    callback: function (key, options) {
                        let id = options.$trigger[0].parentElement.id;
                        let m = "clicked: " + key + ' ' + id;
                        console.log(m);
                        switch (key) {
                            case "delete":
                                instance.deleteOrgan(id);
                                break;
                            case "edit":
                                break;
                        }
                    },
                    items: {
                        "edit": {name: organ_message.manage_edit_organ, icon: "edit"},
                        /*"cut": {name: "Cut", icon: "cut"},
                        copy: {name: "Copy", icon: "copy"},
                        "paste": {name: "Paste", icon: "paste"},*/
                        "delete": {name: organ_message.manage_remove_organ, icon: "delete"}
                        /*"sep1": "---------",*/
                        /*"quit": {name: "Quit", icon: function(){
                                return 'context-menu-icon context-menu-icon-quit';
                            }}*/
                    }
                });
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
    },
    deleteOrgan: function (organId) {
        if (organId !== null && organId !== "") {
            $.ajax({
                url: "/public/-/organ/delete/" + organId,
                type: "DELETE",
                statusCode: {
                    200: function (response) {
                        $.notify("Delete organ success", "success");
                    },
                    400: function (response) {
                        $.notify("Delete organ error", "error");
                    },
                    500: function (response) {
                        $.notify("Delete organ error", "error");
                    }
                }
            })
            $('#organ-menu').click();
        }
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

$(document).on("change", "#importOrganFromExcel", function (e) {
    //stop submit the form, we will post it manually.
    e.preventDefault();
    let form = $('#formImportOrgan')[0];
    let data = new FormData(form);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/public/-/organ/import",
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        success: function (response) {
            console.log(response);
            if (response === "OK")
                $.notify(organ_message.organ_import_from_excel_success, "success");
            else if (response === "BAD_REQUEST")
                $.notify(organ_message.organ_import_invalid_format_file, "error");
            else if (response === "NOT_ACCEPTABLE")
                $.notify(organ_message.organ_import_from_excel_invalid_column, "error");
        },
        error: (e) => {
            $.notify(organ_message.organ_import_from_excel_fail, "error");
        }
    });
});

$(document).on('click', '#exportOrganToExcel', function (e) {
    e.preventDefault();
    $.ajax({
        type: "POST",
        url: "/public/-/organ/export",
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        success: function (response) {
            console.log(response);
            $.notify(organ_message.organ_export_to_excel_success, "success");
        },
        error: (e) => {
            $.notify(organ_message.organ_export_to_excel_fail, "error");
        }
    })
})

$(document).on("contextmenu", "#dataTables-organ>tbody>tr", function (event) {
    event.preventDefault();
    console.log("right click to end document");
});
