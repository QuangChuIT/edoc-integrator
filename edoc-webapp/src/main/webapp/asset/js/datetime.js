Date.prototype.formatDate = function () {
    let dd = this.getDate();
    let mm = this.getMonth();
    mm = mm + 1;
    return [(dd > 9 ? '' : '0') + dd,
        (mm > 9 ? '' : '0') + mm,
        this.getUTCFullYear()
    ].join('/');
};
Date.prototype.formatTime = function () {
    let hh = this.getHours();
    let mm = this.getMinutes();

    return [(hh > 9 ? '' : '0') + hh, (mm > 9 ? '' : '0') + mm].join(":");
};

function convertToDate(dateValue) {
    return new Date(dateValue);
}
function getDate() {
    return new Date();
}
function getCurrentMonth() {
    return new Date().getMonth();
}
