let userManage = {
    userSetting: {
        host: "/user/",
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
                url: "/users",
                dataSrc: ""
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
                }
            ],
            language: app_message.language,
            "order": [[0, "asc"]],
        });
    },
}
$(document).ready(function () {
    $(".user-info").on('click', function () {
        let userId = $(this).attr("data-id");
        console.log("user-id" + userId);
        $.get("/user/" + userId, function (data) {
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
        $.get("/user/" + userId, function (data) {
            console.log(data);
            $('#user-detail').empty();
            $('#userTemplate').tmpl(data).appendTo('#user-detail');
        });
        $('#userInfo').modal({
            backdrop: 'static',
            keyboard: false
        });
    });
});
$(document).on("change", "#importExcel", function (e) {
    //stop submit the form, we will post it manually.
    e.preventDefault();
    let form = $('#formImportUser')[0];
    let data = new FormData(form);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/import/-/user/upload",
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        success: function (response) {
            console.log(response.code);
            if (response.code === 201)
                $.notify(user_message.user_import_from_excel_success, "success");
            else if (response.code === 200)
                $.notify(user_message.user_import_invalid_format_file, "error");
            else if (response.code === 409)
                $.notify(user_message.user_import_from_excel_conflic, "error");
            else if (response.code === 400)
                $.notify(user_message.user_import_from_excel_invalid_column, "error");
        },
        error: (e) => {
            $.notify(user_message.user_import_from_excel_fail, "error");
        }
    });
});
