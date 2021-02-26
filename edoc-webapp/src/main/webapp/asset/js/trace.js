let edocTrace = {
    appSetting: {
        host: "/pubic/trace/",
        mod: "release",
        debugAgent: "web",
        root: $("#main-content"),
        cookie: 1,
        pathPlugin: "/asset",
        requestTimeout: 5000, // Timeout request for ajax,
        dataTable: null,
        mode: "inbox"
    },
    init: function () {
        let instance = this;
        appSettings = instance.appSetting;

        if (typeof jQuery === 'undefined') {
            edocReport.log("Can not load jQuery environment");
            return;
        }
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const docCode = urlParams.get('docCode')
        const fromOrgan = urlParams.get("organ");
        if (docCode !== null && fromOrgan !== null) {
            let endpoint = "/public/-/document/trace" + "?docCode=" + docCode + "&organ=" + fromOrgan;
            $.get(endpoint, function (data){
                let toOrganNames = [];
                data.toOrgan.forEach(function (organ, index) {
                    toOrganNames.push(organ["name"]);
                });
                data.toOrganName = toOrganNames.join(", ");
                data.code = data.codeNumber + "/" + data.codeNotation;
                $("#publicTraceContent").empty();
                $('#edocPublicTraceTmpl').tmpl(data).appendTo('#publicTraceContent');
            });
        } else {
            console.log("lol");
        }
    }
}

$(document).ready(function () {
    edocTrace.init();
});

function getStatusOfTrace(statusCode) {
    let message = "";
    switch (statusCode) {
        case 1:
            message = app_message.edoc_status_arrived;
            break;
        case 2:
            message = app_message.edoc_status_refuse;
            break;
        case 3:
            message = app_message.edoc_status_received;
            break;
        case 4:
            message = app_message.edoc_status_assignment;
            break;
        case 5:
            message = app_message.edoc_status_processing;
            break;
        case 6:
            message = app_message.edoc_status_finish;
            break;
        case 13:
            message = app_message.edoc_status_recover;
            break;
        case 15:
            message = app_message.edoc_status_accept_recover;
            break;
        case 16:
            message = app_message.edoc_status_refuse_recover;
            break;
    }
    return message;
}
