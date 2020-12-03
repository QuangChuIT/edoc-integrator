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
        console.log(queryString);
        const urlParams = new URLSearchParams(queryString);
        const docCode = urlParams.get('docCode')
        console.log(docCode);
        const  documentCode = docCode.split("/");
        console.log(documentCode);
    }
}

$(document).ready(function () {
    edocTrace.init();
});