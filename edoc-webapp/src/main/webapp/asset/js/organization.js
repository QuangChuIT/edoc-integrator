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
                dataSrc: "",
                type: "GET"
            },
            drawCallback: function () {
                $(this).contextMenu({
                    selector: 'tr td',
                    callback: function (key, options) {
                        let id = options.$trigger[0].parentElement.id;
                        let m = "clicked: " + key + ' ' + id;
                        switch (key) {
                            case "delete":
                                instance.deleteOrgan(id);
                                break;
                            case "edit":
                                editOrganClick(id);
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
                $("#token").val(response.token);
            },
            error: function (error) {
                $.notify(error.responseText, "error");
            }
        });
    },
    createOrgan: function() {
        let name = $("#name").val();
        let domain = $("#domain").val();
        let address = $("#address").val();
        let email = $("#email").val();
        let inCharge = $("#inCharge").val();
        let telephone = $("#telephone").val();

        if (validateOrgan(name, domain, inCharge, address, email)) {
            console.log(app_message.edoc_validate_document_request_fail);
        } else {
            let contactRequest = {
                "name": name,
                "domain": domain,
                "address": address,
                "email": email,
                "inCharge": inCharge,
                "telephone": telephone
            };
            $.ajax({
                type: "POST",
                contentType: "application/json;charset=utf-8",
                url: "/public/-/organ/create",
                data: JSON.stringify(contactRequest),
                cache: false,
                success: function (response){
                    if (response.code === 200) {
                        $.notify(organ_message.organ_add_new_success, "success");
                        $('#formAddOrgan').modal('toggle');
                        $("#organ-menu").click();
                        $('#edoc-add-organ').empty();
                    } else {
                        $.notify(organ_message.organ_add_new_fail, "error");
                    }
                },
                error: function (error) {
                    $.notify(error.responseText, "error");
                }
            });
        }
    },
    editOrgan: function(id) {
        let name = $("#editName").val();
        let domain = $("#editDomain").val();
        let address = $("#editAddress").val();
        let email = $("#editEmail").val();
        let inCharge = $("#editInCharge").val();
        let telephone = $("#editTelephone").val();

        let contactRequest = {
            "id": id,
            "name": name,
            "domain": domain,
            "address": address,
            "email": email,
            "inCharge": inCharge,
            "telephone": telephone
        };
        $.ajax({
            type: "PUT",
            contentType: "application/json;charset=utf-8",
            url: "/contact/-/update/contact",
            data: JSON.stringify(contactRequest),
            cache: false,
            success: function (response){
                if (response.code === 200) {
                    $.notify(organ_message.organ_edit_success, "success");
                } else {
                    $.notify(organ_message.organ_edit_fail, "error");
                }
            },
            error: function (error) {
                $.notify(organ_message.organ_edit_fail, "error");
            }
        });
        $('#edoc-edit-organ').empty();
        $('#formEditOrgan').modal('toggle');
        $("#organ-menu").click();
    },
    deleteOrgan: function (organId) {
        if (organId !== null && organId !== "") {
            $.ajax({
                url: "/public/-/organ/delete/" + organId,
                type: "DELETE",
                statusCode: {
                    200: function (response) {
                        $.notify(organ_message.organ_delete_success, "success");
                    },
                    400: function (response) {
                        $.notify(organ_message.organ_delete_fail, "error");
                    },
                    500: function (response) {
                        $.notify(organ_message.organ_delete_fail, "error");
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
        $.get("/contact/-/document/contacts/" + organId, function (data) {
            $('#organ-detail').empty();
            $('#organDetailTemplate').tmpl(data).appendTo('#organ-detail');
        });
        $('#organDetail').modal({
            backdrop: 'static',
            keyboard: false
        });
    });

    $("#addOrgan").on('click', function(e) {
        e.preventDefault();
        $("#formAddOrgan").modal({
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
            $.notify(organ_message.organ_export_to_excel_success, "success");
        },
        error: (e) => {
            $.notify(organ_message.organ_export_to_excel_fail, "error");
        }
    })
})

$(document).on("contextmenu", "#dataTables-organ>tbody>tr", function (event) {
    event.preventDefault();
});
// Buttons in Add new organ form
$(document).on("click", "#btn-addOrgan-confirm", function (event) {
    event.preventDefault();
    organManage.createOrgan();
});
$(document).on("click", "#btn-addOrgan-cancel", function (event) {
    event.preventDefault();
    $("#formAddOrgan").modal('toggle');
});

// Button in Edit organ form
$(document).on('click', '#btn-edit-organ-confirm', function(e) {
    e.preventDefault();
    let organId = $(this).attr("data-id");

    organManage.editOrgan(organId);
})
$(document).on('click', '#btn-edit-organ-cancel', function(e) {
    e.preventDefault();
    $("#formEditOrgan").toggle();
})

function editOrganClick(organId) {
    $.get("/contact/-/document/contacts/" + organId, function (data) {
        $('#edoc-edit-organ').empty();
        $('#editOrganTemplate').tmpl(data).appendTo('#edoc-edit-organ');
    });
    $('#formEditOrgan').modal({
        backdrop: 'static',
        keyboard: false
    });
}

function validateOrgan(name, domain, inCharge, address, email) {
    let emailRegex = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    if (name === "") {
        $("#name").notify(
            "Tên đơn vị không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (domain === "") {
        $("#domain").notify(
            "Mã định danh không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (inCharge === "") {
        $("#inCharge").notify(
            "Đơn vị phụ trách không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (address === "") {
        $("#address").notify(
            "Địa chỉ không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (email === "") {
        $("#email").notify(
            "Địa chỉ thư điện tử không được để trống !",
            {position: "right"}
        );
        return true;
    } else {
        if (!emailRegex.test(email)) {
            $("#email").notify(
                "Địa chỉ thư điện tử không đúng định dạng!",
                {position: "right"}
            );
            return true;
        }
    }
}