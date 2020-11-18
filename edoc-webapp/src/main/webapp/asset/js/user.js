let userId;
let userManage = {
    userSetting: {
        host: "/public/-/user/",
        mod: "release",
        debugAgent: "web",
        root: $("#main-content"),
        cookie: 1,
        pathPlugin: "/asset",
        requestTimeout: 5000, // Timeout request for ajax,
        dataTable: null,
        mode: "system"
    },
    renderUserDatatable: function () {
        let instance = this;
        instance.dataTable = $('#dataTables-user').DataTable({
            serverSide: true,
            processing: true,
            pageLength: 25,
            ajax: {
                url: "/public/-/users",
                type: "GET",
                dataSrc: ""
            },
            drawCallback: function () {
                $(this).contextMenu({
                    selector: 'tr',
                    callback: function (key, options) {
                        let id = options.$trigger[0].id;
                        userId = id;
                        // check id superadmin account.
                        // if (id == "") {
                        //     alert("Not edit in superadmin account!!!");
                        // }
                        let m = "clicked: " + key + ' ' + id;
                        console.log(m);
                        switch(key) {
                            case "delete":
                                instance.deleteUser(id);
                                break;
                            case "edit":
                                $('#user-menu').click();
                                break;
                            case "permission":
                                console.log(userId);
                                permissionClick(userId);
                                break;
                        }
                    },
                    items: {
                        "edit": {name: user_message.manage_edit_user, icon: "edit"},
                        "permission": {name: user_message.manage_permission_user, icon: "fa-shield"},
                        "sep1": "---------",
                        "delete": {name: user_message.manage_remove_user, icon: "delete"}
                    }
                });
            },
            rowId: "userId",
            responsive: true,
            pageLength: 25,
            autoWidth: true,
            bDestroy: true,
            processing: true,
            paging: true,
            info: false,
            columns: [
                {
                    "title": user_message.table_header_fullName,
                    "data": null,
                    "render": function (data) {
                        return $('#userFullNameTemplate').tmpl(data).html();
                    }
                },
                {
                    "title": user_message.table_header_username,
                    "data": "username",
                },
                {
                    "title": user_message.table_header_emailAddress,
                    "data": "emailAddress",
                },
                {
                    "title": user_message.table_header_organize,
                    "data": "organization.name",
                },
                {
                    "title": user_message.table_header_status,
                    "data": null,
                    "render": function (data) {
                        return $('#userStatusTemplate').tmpl(data).html();
                    }
                },
            ],
            language: app_message.language,
            "order": [[0, "asc"]],
        });
    },
    deleteUser: function (userId) {
        if (userId !== null && userId !== "") {
            $.ajax({
                url: "/public/-/user/delete/" + userId,
                type: "DELETE",
                statusCode: {
                    200: function (response) {
                        $.notify("Delete user success", "success");
                    },
                    400: function (response) {
                        $.notify("Delete user error", "error");
                    },
                    500: function (response) {
                        $.notify("Delete user error", "error");
                    }
                }
            })
            $('#user-menu').click();
        }
    },
    createUser: function () {
        //get displayName
        let addDisplayName = $("#addDisplayName").val();
        //get userName
        let addUserName = $("#addUserName").val();
        //get organization
        let addOrganDomain = $("#addOrganDomain").val();
        //get password
        let password = $("#password").val();
        //get emailAddress
        let addEmailAddress = $("#addEmailAddress").val();

        if (validateUser(addDisplayName, addUserName, addOrganDomain, password, addEmailAddress)) {
            console.log(app_message.edoc_validate_document_request_fail);
        } else {
            let addUserRequest = {
                "displayName": addDisplayName,
                "userName": addUserName,
                "organDomain": addOrganDomain,
                "password": password,
                "emailAddress": addEmailAddress
            };
            console.log(addUserRequest);
            $.ajax({
                type: "POST",
                contentType: "application/json;charset=utf-8",
                url: "/public/-/user/create",
                data: JSON.stringify(addUserRequest),
                cache: false,
                success: function (response) {
                    if (response.code === 200) {
                        $.notify(user_message.user_add_new_success, "success");
                        $('#formAddUser').modal('toggle');
                        $("#user-menu").click();
                        $('#edoc-add-user').empty();
                    } else {
                        $.notify(user_message.user_add_new_fail, "error");
                    }
                },
                error: function (error) {
                    $.notify(error.responseText, "error");
                }
            });
        }
    },
    createUserRole: function() {
        let roleId;
        if ($("#adminRoleSelected").is(":checked")) {
            roleId = $("#adminRoleSelected").val();
        } else if ($("#userRoleSelected").is(":checked")) {
            roleId = $("#userRoleSelected").val();
        }
        let userRoleRequest = {
            "userId": userId,
            "roleId": roleId,
        }
        console.log(userRoleRequest);
        $.ajax({
            type: "POST",
            contentType: "application/json;charset=utf-8",
            url: "/public/-/create/role/",
            data: JSON.stringify(userRoleRequest),
            cache: false,
            success: function(response) {
                if (response.code === 201) {
                    $.notify(user_message.user_set_permission_success, "success");
                } else if (response.code === 200) {
                    $.notify(user_message.user_set_permission_success, "success");
                }
            },
            error: function() {
                $.notify(user_message.user_set_permission_fail, "error");
            }
        })
        $('#formPermission').modal('toggle');
    }
}
$(document).ready(function () {
    $(".user-info").on('click', function () {
        let userId = $(this).attr("data-id");
        console.log("user-id" + userId);
        $.get("/public/-/user/" + userId, function (data) {
            console.log(data);
            $('#user-detail').empty();
            $('#userTemplate').tmpl(data).appendTo('#user-detail');
        });
        $('#userInfo').modal({
            backdrop: 'static',
            keyboard: false
        });
    });

    $("#dataTables-user").on('click', 'tbody>tr', function () {
        let userId = $(this).attr("id");
        console.log(userId);
        $.get("/public/-/user/" + userId, function (data) {
            console.log(data);
            $('#user-detail').empty();
            $('#userTemplate').tmpl(data).appendTo('#user-detail');
        });
        $('#userInfo').modal({
            backdrop: 'static',
            keyboard: false
        });
    });
    $("#addUser").on('click', function(e) {
        e.preventDefault();
        $('#formAddUser').modal({
            backdrop: 'static',
            keyboard: false
        });
    });
    $("#addOrganDomain").select2({
        tags: true,
        maximumSelectionLength: 1
    });
    $(document).on('click', 'input[type="checkbox"]', function() {
        $('input[type="checkbox"]').not(this).prop('checked', false);
    });
    $(document).on("click", "#permission-confirm", function(e) {
        e.preventDefault();
        console.log(userId);
        userManage.createUserRole(userId);
    });
});
$(document).on("change", "#importUserFromExcel", function (e) {
    //stop submit the form, we will post it manually.
    e.preventDefault();
    let form = $('#formImportUser')[0];
    let data = new FormData(form);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/public/-/user/import",
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        success: function (data, response) {
            console.log(data);
            console.log(response);
            if (response === "OK")
                $.notify(user_message.user_import_from_excel_success, "success");
            else if (response === "BAD_REQUEST")
                $.notify(user_message.user_import_invalid_format_file, "error");
            // else if (response.code === 409)
            //     $.notify(user_message.user_import_from_excel_conflic, "error");
            else if (response === "NOT_ACCEPTABLE")
                $.notify(user_message.user_import_from_excel_invalid_column, "error");
        },
        error: (e) => {
            $.notify(user_message.user_import_from_excel_fail, "error");
        }
    });
});

$(document).on('click', '#exportUserToExcel', function (e) {
    e.preventDefault();
    $.ajax({
        type: "POST",
        url: "/public/-/user/export",
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        success: function (response) {
            console.log(response);
            $.notify(user_message.user_export_to_excel_success, "success");
        },
        error: (e) => {
            $.notify(user_message.user_export_to_excel_fail, "error");
        }
    })
})

$(".toggle-password").click(function() {
    $(this).toggleClass("fa-eye fa-eye-slash");
    let input = $($(this).attr("toggle"));
    if (input.attr("type") == "password") {
        input.attr("type", "text");
    } else {
        input.attr("type", "password");
    }
});

$(document).on("click", "#btn-addUser-confirm", function (event) {
    event.preventDefault();
    userManage.createUser();
});
$(document).on("click", "#btn-addUser-cancel", function (event) {
    event.preventDefault();
    $("#formAddUser").modal('toggle');
});

$(document).on("click", "#permission-cancel", function (event) {
    event.preventDefault();
    $("#formPermission").modal('toggle');
});

function validateUser(displayName, userName, organDomain, password, emailAddress) {
    let emailRegex = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    if (displayName === "") {
        $("#displayName").notify(
            "Tên người dùng không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (userName == null) {
        $("#userName").notify(
            "Tên tài khoản không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (organDomain === "") {
        $("#organDomain").notify(
            "Đơn vị không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (password === "") {
        $("#password").notify(
            "Mật khẩu không được để trống !",
            {position: "right"}
        );
        return true;
    } else {
        if (password.length < 8) {
            $("#password").notify(
                "Mật khẩu phải nhiều hơn 8 kí tự",
                {position: "right"}
            )
        }
    }
    if (emailAddress === "") {
        $("#emailAddress").notify(
            "Địa chỉ thư điện tử không được để trống !",
            {position: "right"}
        );
        return true;
    } else {
        if (!emailRegex.test(emailAddress)) {
            $("#emailAddress").notify(
                "Địa chỉ thư điện tử không đúng định dạng!",
                {position: "right"}
            );
            return true;
        }
    }
}

function permissionClick(userId) {
    $.get("/public/-/user/" + userId, function (data) {
        console.log(data);
        $('#userPermission').empty();
        $('#displayNamePermissionTemplate').tmpl(data).appendTo('#userPermission');
    });
    $('#formPermission').modal({
        backdrop: 'static',
        keyboard: false
    });
}
