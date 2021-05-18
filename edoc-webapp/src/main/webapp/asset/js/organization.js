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
    init: function () {
        let instance = this;

        if (typeof jQuery === 'undefined') {
            edocDocument.log("Can not load jQuery environment");
            return;
        }
        instance.renderOrganDatatable();
    },
    renderOrganDatatable: function () {
        let instance = this;
        instance.organSetting.dataTable = $('#dataTables-organ').DataTable({
            serverSide: true,
            processing: true,
            pageLength: 25,
            ajax: {
                url: "/contact/-/document/contacts",
                type: "POST"
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
            autoWidth: false,
            ordering: true,
            bDestroy: true,
            searching: true,
            lengthChange: false,
            paging: true,
            info: false,
            columns: [
                {
                    "name": "name",
                    "title": organ_message.table_header_name,
                    "data": null,
                    "render": function (data) {
                        return $('#organNameTemplate').tmpl(data).html();
                    }
                },
                {
                    "name": "domain",
                    "title": organ_message.table_header_domain,
                    "data": "domain",
                },
                {
                    "name": "email",
                    "title": organ_message.table_header_email,
                    "data": null,
                    "render": function (data) {
                        return $('#organEmailTemplate').tmpl(data).html();
                    }
                },
                {
                    "name": "status",
                    "title": organ_message.organ_status,
                    "data": null,
                    "render": function (data) {
                        if (data.status === true) {
                            return organ_message.organ_status_active;
                        } else {
                            return organ_message.organ_status_deactive;
                        }
                    }
                },
                {
                    "name": "agency",
                    "title": organ_message.organ_table_agency,
                    "data": null,
                    "render": function (data) {
                        if (data.isAgency === true) {
                            return organ_message.organ_agency;
                        } else {
                            return organ_message.organ_not_agency;
                        }
                    }
                },
            ],
            language: app_message.language,
            order: [[3, "desc"]]
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

    createOrgan: function(e) {
        let instance = this;

        let name = $("#name").val();
        let domain = $("#domain").val();
        let address = $("#address").val();
        let email = $("#email").val();
        let inCharge = $("#inCharge").val();
        let telephone = $("#telephone").val();
        let agency = checkAgencySelected();
        let receiveNotify = checkReceivedNotify();
        let sendToVPCP = checkSendToVPCP();

        if (validateOrgan(name, domain, inCharge, address, email)) {
            //console.log(app_message.edoc_validate_document_request_fail);
            //e.preventDefault();
        } else {
            let contactRequest = {
                "name": name,
                "domain": domain,
                "address": address,
                "email": email,
                "inCharge": inCharge,
                "telephone": telephone,
                "agency": agency,
                "receiveNotify": receiveNotify,
                "sendToVPCP": sendToVPCP
            };
            $.ajax({
                type: "POST",
                contentType: "application/json;charset=utf-8",
                url: "/public/-/organ/create",
                data: JSON.stringify(contactRequest),
                cache: false,
                success: function (response) {
                    if (response.code === 200) {
                        $.notify(organ_message.organ_add_new_success, "success");
                        $.notify(response.message, "success");
                        $('#addNewOrgan').trigger("reset");
                        $('#formAddOrgan').modal('toggle');
                        organManage.renderOrganDatatable();
                    } else {
                        $.notify(organ_message.organ_add_new_fail, "error");
                    }
                },
                error: function (error) {
                    $.notify(error.responseJSON.errors, "error");
                }
            });
        }
    },

    editOrgan: function(id) {
        let instance = this;

        let name = $("#editName").val();
        let domain = $("#editDomain").val();
        let address = $("#editAddress").val();
        let email = $("#editEmail").val();
        let inCharge = $("#editInCharge").val();
        let telephone = $("#editTelephone").val();
        let agency = checkAgencySelected();
        let receiveNotify = checkReceivedNotify();
        let sendToVPCP = checkSendToVPCP();
        let integratorCenter = checkIntegratorCenter();
        let contactRequest = {
            "id": id,
            "name": name,
            "domain": domain,
            "address": address,
            "email": email,
            "inCharge": inCharge,
            "telephone": telephone,
            "agency": agency,
            "receiveNotify": receiveNotify,
            "sendToVPCP": sendToVPCP,
            "integratorCenter": integratorCenter
        };
        $.ajax({
            type: "PUT",
            contentType: "application/json;charset=utf-8",
            url: "/contact/-/update/contact",
            data: JSON.stringify(contactRequest),
            cache: false,
            beforeSend: function () {
                $("#overlay").show();
            },
            success: function (response) {
                if (response.code === 200) {
                    $.notify(organ_message.organ_edit_success, "success");
                    $.notify(response.message, "success");
                } else {
                    $.notify(organ_message.organ_edit_fail, "error");
                }
            },
            error: function (error) {
                $.notify(organ_message.organ_edit_fail, "error");
            }
        }).done(function () {
            $("#overlay").hide();
            $('#formEditOrgan').modal('toggle');
            organManage.renderOrganDatatable();
        })
    },
    deleteOrgan: function (organId) {
        let instance = this;
        if (organId !== null && organId !== "") {
            $.ajax({
                url: "/public/-/organ/delete/" + organId,
                type: "DELETE",
                statusCode: {
                    200: function (response) {
                        $.notify(organ_message.organ_delete_success, "success");
                        $("#" + organId).remove();
                        organManage.renderOrganDatatable();
                    },
                    400: function (response) {
                        $.notify(organ_message.organ_delete_fail, "error");
                    },
                    500: function (response) {
                        $.notify(organ_message.organ_delete_fail, "error");
                    }
                }
            })
        }
    }
}
$(document).ready(function () {
    $("#dataTables-organ").on('click', 'tbody>tr', function (e) {
        let organId = $(this).attr("id");
        /*let $cell = $(e.target).closest('td');
        if ($cell.index() > 0) {*/
            $.get("/contact/-/document/contacts/" + organId, function (data) {
                $('#organ-detail').empty();
                $('#organDetailTemplate').tmpl(data).appendTo('#organ-detail');
            });
            $('#organDetail').modal({
                backdrop: 'static',
                keyboard: false
            });
        //}
    });

    $("#deleteOrgans").on('click', function() {
        /*$.each($("input[name='checkBox[]']:checked").closest("td").next("td"), function () {
            values.push($(this).text());
        });*/
        let data = organManage.organSetting.dataTable.columns.checkboxes.selected();
        console.log(data);
    })

    $("#addOrgan").on('click', function (e) {
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

$(document).on("click", "#importOrganFromExcel", function (e) {
    //stop submit the form, we will post it manually.
    e.preventDefault();
    Swal.fire({
        title: 'Chọn tệp tải lên',
        input: 'file',
        showCancelButton: true,
        confirmButtonText: 'Tải lên',
        cancelButtonText: 'Hủy bỏ',
        onBeforeOpen: () => {
            $(".swal2-file").change(function () {
                var reader = new FileReader();
                reader.readAsDataURL(this.files[0]);
            });
        },
        inputAttributes: {
            'accept': "application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            'aria-label': 'Upload your profile picture'
        },
        html: '<a href="/public/-/organ/export/sample"><u>hoặc tải về tệp mẫu</u></a>'
    }).then((file) => {
        if (file.value) {
            let formData = new FormData();
            let file = $('.swal2-file')[0].files[0];
            formData.append("fileOrganToUpload", file);
            $.ajax({
                type: "POST",
                enctype: 'multipart/form-data',
                url: "/public/-/organ/import",
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                willOpen: function () {
                    $("#overlay").show();
                },
                success: (response) => {
                    let successOptions = {
                        autoHideDelay: 200000,
                        showAnimation: "fadeIn",
                        autoHide: false,
                        clickToHide: true,
                        hideAnimation: "fadeOut",
                        hideDuration: 700,
                        arrowShow: false
                    };
                    if (response.code === 400) {
                        successOptions.className = "error";
                        if (response.errors.length > 0) {
                            response.errors.forEach(function (obj) {
                                $.notify(obj, successOptions);
                            });
                        }
                        $.notify(response.message, successOptions);
                    } else if (response.code === 200) {
                        successOptions.className = "success";
                        $.notify(response.message, successOptions);
                    } else {
                        successOptions.className = "error";
                        $.notify(response.message, "error", successOptions);
                    }
                },
                error: (e) => {
                    $.notify(organ_message.organ_import_from_excel_fail, "error");
                },
            }).done(function () {
                $("#overlay").hide();
            });
        }
    });
});

$(document).on('click', '#exportOrganToExcel', function (e) {
    e.preventDefault();
    let url = "/public/-/organ/export"
    $.ajax({
        type: "GET",
        url: url,
        processData: false,
        contentType: false,
        beforeSend: function () {
            $("#overlay").show();
        },
        success: function () {
            let link = document.createElement('a');
            let href = url;
            link.style.display = 'none';
            link.setAttribute('href', href);
            link.click();
            $.notify(organ_message.organ_export_to_excel_success, "success");
        },
        error: (e) => {
            $.notify(organ_message.organ_export_to_excel_fail, "error");
        }
    }).done(function () {
        $("#overlay").hide();
    });
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
$(document).on('click', '#btn-edit-organ-confirm', function (e) {
    e.preventDefault();
    let organId = $(this).attr("data-id");
    organManage.editOrgan(organId);
})
$(document).on('click', '#btn-edit-organ-cancel', function (e) {
    e.preventDefault();
    $("#formEditOrgan").modal('toggle');
})

function checkAgencySelected() {
    if ($("#agencySelected").is(":checked") ||
        $("#agencySelectedEdit").is(":checked")) {
        return true;
    } else {
        return false;
    }
}

function checkReceivedNotify() {
    if ($("#receiveNotifySelected").is(":checked") ||
        $("#receiveNotifySelectedEdit").is(":checked")) {
        return true;
    } else {
        return false;
    }
}

function checkSendToVPCP() {
    if ($("#sendToVPCP").is(":checked") ||
        $("#sendToVPCPEdit").is(":checked")) {
        return true;
    } else {
        return false;
    }
}

function checkIntegratorCenter() {
    if ($("#integratorCenterEdit").is(":checked") ||
        $("#integratorCenterEdit").is(":checked")) {
        return true;
    } else {
        return false;
    }
}

function editOrganClick(organId) {
    $.get("/contact/-/document/contacts/" + organId, function (data) {
        console.log(data);
        $('#edoc-edit-organ').empty();
        $('#editOrganTemplate').tmpl(data).appendTo('#edoc-edit-organ');
    });
    $('#formEditOrgan').modal({
        backdrop: 'static',
        keyboard: false
    });
}

function validateOrgan(name, domain, inCharge, address, email) {
    let result = false;
    let emailRegex = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    if (name === "") {
        $("#name").notify(
            "Tên đơn vị không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (domain === "") {
        $("#domain").notify(
            "Mã định danh không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (inCharge === "") {
        $("#inCharge").notify(
            "Đơn vị phụ trách không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (address === "") {
        $("#address").notify(
            "Địa chỉ không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (email === "") {
        $("#email").notify(
            "Địa chỉ thư điện tử không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    } else {
        if (!emailRegex.test(email)) {
            $("#email").notify(
                "Địa chỉ thư điện tử không đúng định dạng!",
                {position: "right"}
            );
            result = true;
            return result;
        }
    }
}