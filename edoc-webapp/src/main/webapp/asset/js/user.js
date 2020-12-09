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
                        let m = "clicked: " + key + ' ' + id;
                        switch(key) {
                            case "delete":
                                instance.deleteUser(id);
                                break;
                            case "edit":
                                editUserClick(userId);
                                break;
                            case "permission":
                                permissionClick(userId);
                                break;
                        }
                    },
                    items: {
                        "edit": {name: user_message.manage_edit_user, icon: "edit", disabled: function(key, opt) {
                            let id = opt.$trigger[0].id;
                            if (id == '164655867')
                                return !this.data('editDisabled');
                            }},
                        "permission": {name: user_message.manage_permission_user, icon: "fa-shield", disabled: function(key, opt) {
                            let id = opt.$trigger[0].id;
                            if (id == '164655867')
                                return !this.data('permissionDisabled');
                        }},
                        "sep1": "---------",
                        "delete": {name: user_message.manage_remove_user, icon: "delete", disabled: function(key, opt) {
                            let id = opt.$trigger[0].id;
                            if (id == '164655867')
                                return !this.data('daleteDisabled');
                        }}
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
        let instance = this;
        if (userId !== null && userId !== "") {
            $.ajax({
                url: "/public/-/user/delete/" + userId,
                type: "DELETE",
                statusCode: {
                    200: function (response) {
                        $.notify(user_message.user_delete_success, "success");
                    },
                    400: function (response) {
                        $.notify(user_message.user_delete_fail, "error");
                    },
                    500: function (response) {
                        $.notify(user_message.user_delete_fail, "error");
                    }
                }
            })
            instance.renderUserDatatable();
        }
    },
    createUser: function () {
        let instance = this;
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

        if (validateAddUser(addDisplayName, addUserName, addOrganDomain, password, addEmailAddress)) {
            console.log(app_message.edoc_validate_document_request_fail);
        } else {
            let addUserRequest = {
                "displayName": addDisplayName,
                "userName": addUserName,
                "organDomain": addOrganDomain,
                "password": password,
                "emailAddress": addEmailAddress
            };
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
                        instance.renderUserDatatable();
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
    editUser: function (userId) {
        let instance = this;
        //get displayName
        let editDisplayName = $("#editDisplayName").val();
        //get organization
        let editOrganDomain = $("#editOrganDomain").val();
        //get emailAddress
        let editEmailAddress = $("#editEmailAddress").val();

        if (validateEditUser(editDisplayName, editOrganDomain, editEmailAddress)) {
            console.log(app_message.edoc_validate_document_request_fail);
        } else {
            let editUserRequest = {
                "userId": userId,
                "displayName": editDisplayName,
                "organDomain": editOrganDomain,
                "emailAddress": editEmailAddress
            };
            $.ajax({
                type: "PUT",
                contentType: "application/json;charset=utf-8",
                url: "/public/-/user/edit",
                data: JSON.stringify(editUserRequest),
                cache: false,
                success: function (response) {
                    if (response.code === 200) {
                        $.notify(user_message.user_edit_success, "success");
                    } else {
                        $.notify(user_message.user_edit_fail, "error");
                    }
                },
                error: function (error) {
                    $.notify(user_message.user_edit_fail, "error");
                }
            })
            $("#formEditUser").modal("toggle");
            $('#edoc-edit-user').empty();
            instance.renderUserDatatable();
        }
    },
    createUserRole: function(userId) {
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

    // Show detail of user-login info
    $(".user-info").on('click', function () {
        let userId = $(this).attr("data-id");
        $.get("/public/-/user/" + userId, function (data) {
            $('#user-detail').empty();
            $('#userTemplate').tmpl(data).appendTo('#user-detail');
        });
        $('#userInfo').modal({
            backdrop: 'static',
            keyboard: false
        });
    });

    // Show info detail of each user in data-table
    $("#dataTables-user").on('click', 'tbody>tr', function () {
        let userId = $(this).attr("id");
        $.get("/public/-/user/" + userId, function (data) {
            $('#user-detail').empty();
            $('#userTemplate').tmpl(data).appendTo('#user-detail');
        });
        $('#userInfo').modal({
            backdrop: 'static',
            keyboard: false
        });
    });

    // Show form add new user
    $("#addUser").on('click', function(e) {
        e.preventDefault();
        $('#formAddUser').modal({
            backdrop: 'static',
            keyboard: false
        });
    });

    $("#addOrganDomain").select2({
        tags: true,
        maximumSelectionLength: 1,
        width: "auto"
    });
    $("#editOrganDomain").select2({
        tags: true,
        maximumSelectionLength: 1,
        width: "auto"
    })
    $(document).on('click', 'input[type="checkbox"]', function() {
        $('input[type="checkbox"]').not(this).prop('checked', false);
    });

    // Click to show modal for upload file
    // Developing...
    // $("#importUserFromExcel").on('click', function(e) {
    //     e.preventDefault();
    //     $('#formImportFromExcel').modal({
    //         backdrop: 'static',
    //         keyboard: false
    //     })
    // })

});

// Call ajax to import users from excel file
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
        success: function (response) {
            if (response === "OK") {
                $.notify(user_message.user_import_from_excel_success, "success");
                $("#importUserFromExcel").val('');
            }
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

// Call ajax to export users to Excel file
$(document).on('click', '#exportUserToExcel', function (e) {
    e.preventDefault();
    $.ajax({
        type: "GET",
        url: "/public/-/user/export",
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        success: function (response) {
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

// Buttons in Add new user form
$(document).on("click", "#btn-addUser-confirm", function (event) {
    event.preventDefault();
    userManage.createUser();
});
$(document).on("click", "#btn-addUser-cancel", function (event) {
    event.preventDefault();
    $("#formAddUser").modal('toggle');
});

// Button in Permission of user modal
$(document).on("click", "#permission-confirm", function(e) {
    e.preventDefault();
    let userId = $(this).attr("data-id");
    userManage.createUserRole(userId);
});
$(document).on("click", "#permission-cancel", function (event) {
    event.preventDefault();
    $("#formPermission").modal('toggle');
});

// Buttons in Edit user form
$(document).on("click", "#btn-editUser-confirm", function (event) {
    event.preventDefault();
    let userId = $(this).attr("data-id");
    userManage.editUser(userId);
});
$(document).on("click", "#btn-editUser-cancel", function (event) {
    event.preventDefault();
    $("#formEditUser").modal('toggle');
});

function validateAddUser(displayName, userName, organDomain, password, emailAddress) {
    let emailRegex = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    if (displayName === "") {
        $("#addDisplayName").notify(
            "Tên người dùng không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (userName === "") {
        $("#addUserName").notify(
            "Tên tài khoản không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (organDomain === "") {
        $("#addOrganDomain").notify(
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
        $("#addEmailAddress").notify(
            "Địa chỉ thư điện tử không được để trống !",
            {position: "right"}
        );
        return true;
    } else {
        if (!emailRegex.test(emailAddress)) {
            $("#addEmailAddress").notify(
                "Địa chỉ thư điện tử không đúng định dạng!",
                {position: "right"}
            );
            return true;
        }
    }
}

function validateEditUser(displayName, organDomain, emailAddress) {
    let emailRegex = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    if (displayName === "") {
        $("#editDisplayName").notify(
            "Tên người dùng không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (organDomain === "") {
        $("#editOrganDomain").notify(
            "Đơn vị không được để trống !",
            {position: "right"}
        );
        return true;
    }
    if (emailAddress === "") {
        $("#editEmailAddress").notify(
            "Địa chỉ thư điện tử không được để trống !",
            {position: "right"}
        );
        return true;
    } else {
        if (!emailRegex.test(emailAddress)) {
            $("#editEmailAddress").notify(
                "Địa chỉ thư điện tử không đúng định dạng!",
                {position: "right"}
            );
            return true;
        }
    }
}

function permissionClick(userId) {
    $.get("/public/-/user/" + userId, function (data) {
        $('#userPermission').empty();
        $('#btn-userPermission').empty();
        $('#displayNamePermissionTemplate').tmpl(data).appendTo('#userPermission');
        $('#btnUserPermissionTemplate').tmpl(data).appendTo('#btn-userPermission');
    });
    $('#formPermission').modal({
        backdrop: 'static',
        keyboard: false
    });
}
function editUserClick(userId) {
    $.get("/public/-/user/" + userId, function (data) {
        $('#edoc-edit-user').empty();
        $('#editUserTemplate').tmpl(data).appendTo('#edoc-edit-user');
    });
    $('#formEditUser').modal({
        backdrop: 'static',
        keyboard: false
    });
}
