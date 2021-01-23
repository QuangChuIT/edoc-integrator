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
                        switch (key) {
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
                        "edit": {
                            name: user_message.manage_edit_user, icon: "edit", disabled: function (key, opt) {
                                let id = opt.$trigger[0].id;
                                return false;
                                /* if (id == AdministratorId || id == SuperAdministratorId)
                                     return !this.data('editDisabled');*/
                            }
                        },
                        "permission": {
                            name: user_message.manage_permission_user,
                            icon: "fa-shield",
                            disabled: function (key, opt) {
                                let id = opt.$trigger[0].id;
                                return false;
                                /*if (id == AdministratorId || id == SuperAdministratorId)
                                    return !this.data('permissionDisabled');*/
                            }
                        },
                        "sep1": "---------",
                        "delete": {
                            name: user_message.manage_remove_user, icon: "delete", disabled: function (key, opt) {
                                let id = opt.$trigger[0].id;
                                return false;

                                /*if (id == AdministratorId || id == SuperAdministratorId)
                                    return !this.data('deleteDisabled');*/
                            }
                        }
                    }
                });
            },
            rowId: "userId",
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
                        $("#" + userId).remove();
                        userManage.renderUserDatatable();
                    },
                    400: function (response) {
                        $.notify(user_message.user_delete_fail, "error");
                    },
                    500: function (response) {
                        $.notify(user_message.user_delete_fail, "error");
                    }
                },
            });
        }
    },
    createUser: function (e) {
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
            e.preventDefault();
            //console.log(app_message.edoc_validate_document_request_fail);
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
                        $('#addNewUser').trigger("reset");
                        $('#formAddUser').modal('toggle');
                        userManage.renderUserDatatable();
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
            edocDocument.renderUserDatatable();
        }
    },
    createUserRole: function (userId) {
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
            success: function (response) {
                if (response.code === 201) {
                    $.notify(user_message.user_set_permission_success, "success");
                } else if (response.code === 200) {
                    $.notify(user_message.user_set_permission_success, "success");
                }
            },
            error: function () {
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
    $("#addUser").on('click', function (e) {
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
    });
    $(document).on('click', 'input[type="checkbox"]', function () {
        $('input[type="checkbox"]').not(this).prop('checked', false);
    });

   /* $.get("/public/-/role/" + role_message.role_administrator, function (data){
        console.log(data);
        AdministratorId = data;
        console.log(data)
    });*/
    // $.get("/public/-/role/" + role_message.role_super_administrator, function (data) {
    //     SuperAdministratorId = data.roleId;
    // });
    $("#email-template-menu").on('click', function (e) {
        e.preventDefault();
    });
});

// Call ajax to import users from excel file
$(document).on("click", "#importUserFromExcel", function (e) {
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
        html: '<a href="/public/-/user/export/sample"><u>hoặc tải về tệp mẫu</u></a>'
    }).then((file) => {
        if (file.value) {
            let formData = new FormData();
            let file = $('.swal2-file')[0].files[0];
            formData.append("fileUserToUpload", file);
            $.ajax({
                type: "POST",
                enctype: 'multipart/form-data',
                url: "/public/-/user/import",
                data: formData,
                processData: false, //prevent jQuery from automatically transforming the data into a query string
                contentType: false,
                cache: false,
                beforeSend: function () {
                    $("#overlay").show();
                },
                success: function (response) {
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
                    $.notify(user_message.user_import_from_excel_fail, "error");
                },
            }).done(function () {
                $("#overlay").hide();
            });
        }
    });
});

// Call ajax to export users to Excel file
$(document).on('click', '#exportUserToExcel', function (e) {
    e.preventDefault();
    let url = "/public/-/user/export"
    $.ajax({
        type: "GET",
        url: url,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        beforeSend: function () {
            $("#overlay").show();
        },
        success: function () {
            let link = document.createElement('a');
            let href = url;
            link.style.display = 'none';
            link.setAttribute('href', href);
            link.click();
        },
        error: (e) => {
            $.notify(user_message.user_export_to_excel_fail, "error");
        }
    }).done(function () {
        $("#overlay").hide();
        $.notify(user_message.user_export_to_excel_success, "success");
    });
})

$(".toggle-password").click(function () {
    $(this).toggleClass("fa-eye fa-eye-slash");
    let input = $($(this).attr("toggle"));
    if (input.attr("type") === "password") {
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
$(document).on("click", "#permission-confirm", function (e) {
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
    let result = false;
    let emailRegex = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    if (displayName === "") {
        $("#addDisplayName").notify(
            "Tên người dùng không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (userName === "") {
        $("#addUserName").notify(
            "Tên tài khoản không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (organDomain === "") {
        $("#addOrganDomain").notify(
            "Đơn vị không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (password === "") {
        $("#password").notify(
            "Mật khẩu không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    } else {
        if (password.length < 8) {
            $("#password").notify(
                "Mật khẩu phải nhiều hơn 8 kí tự",
                {position: "right"}
            );
            result = true;
            return result;
        }
    }
    if (emailAddress === "") {
        $("#addEmailAddress").notify(
            "Địa chỉ thư điện tử không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    } else {
        if (!emailRegex.test(emailAddress)) {
            $("#addEmailAddress").notify(
                "Địa chỉ thư điện tử không đúng định dạng!",
                {position: "right"}
            );
            result = true;
            return result;
        }
    }
}

function validateEditUser(displayName, organDomain, emailAddress) {
    let result = false;
    let emailRegex = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    if (displayName === "") {
        $("#editDisplayName").notify(
            "Tên người dùng không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (organDomain === "") {
        $("#editOrganDomain").notify(
            "Đơn vị không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    }
    if (emailAddress === "") {
        $("#editEmailAddress").notify(
            "Địa chỉ thư điện tử không được để trống !",
            {position: "right"}
        );
        result = true;
        return result;
    } else {
        if (!emailRegex.test(emailAddress)) {
            $("#editEmailAddress").notify(
                "Địa chỉ thư điện tử không đúng định dạng!",
                {position: "right"}
            );
            result = true;
            return result;
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

function progressHandler(event) {
    var percent = (event.loaded / event.total) * 100;
    _("status").innerHTML = Math.round(percent) + "% uploaded... please wait";
}

function completeHandler(event) {
    _("status").innerHTML = event.target.responseText;
}
