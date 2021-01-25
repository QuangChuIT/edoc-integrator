$(function () {
    $('#side-menu').metisMenu();
    $('#report-menu').metisMenu();
});

//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function () {
    let url = window.location;
    let element = $('#side-menu.nav a').filter(function () {
        return this.href === url || url.href.indexOf(this.href) === 0;
    });

    $('.tab-container').hide();
    $('.tab-container:first').show();

    $('#edoc-tab li a').on("click", function () {
        let t = $(this).attr('id');
        $('#edoc-tab li').removeClass("active");
        $(this).parent().addClass("active");
        $('.tab-container').hide();
        $('#' + t + 'C').show();
        $('#' + t + 'C' + " ul li a").removeClass("active");
    });

    $(".edoc-table-statistic").hide();
    $(".edoc-statistic").hide();
    $(".edoc-table-draft").hide();
    $(".edoc-report-table").hide();

    $("#exportReportPDF").on("click", function (e) {
        e.preventDefault();
        $(".buttons-pdf").click();
    });

    $("#exportReportExcel").on("click", function (e) {
        e.preventDefault();
        $(".buttons-excel").click();
    });
});