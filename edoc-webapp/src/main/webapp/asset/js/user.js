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

