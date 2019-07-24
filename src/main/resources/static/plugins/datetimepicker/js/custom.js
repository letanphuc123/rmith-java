//<editor-fold defaultstate="collapsed" desc="DATE PICKER">
$(document).ready(function () {
    $('#daterangeschau').DateRangesChau({
        aggregations: ['-'],
        values: {
            mode: 'tworanges',
            calendars: 2,
            dr1from: moment().subtract('days', 31),
            dr1to: moment().subtract('days', 1),
            dr2from: moment().subtract('days', 62),
            dr2to: moment().subtract('days', 32),
            daterangePreset: "custom",
            comparisonEnabled: false,
            removeComparison: true,
            comparisonPreset: "previousperiod",
            maxDate: 0,
            minDate: 0,
            dateFormat: "yyyy-MM-dd"
        }
    });
    $("#datepicker-dropdown").prependTo("#daterangeschau");
    function resize() {
        var Wcontainer = $(".card-view").width();
        var Wdatepicker = $("#datepicker-dropdown").width();
        if (Wdatepicker >= Wcontainer) {
            $('#datepicker-dropdown').css({width: Wcontainer + "px", overflow: "auto"});
            $('.date-ranges-form').css({"margin-right": "10px"});
            $('.main-daterange').css({"padding-bottom": "30px"});
            $(".date-ranges-form .btn_static").css({position: "static"});
        } else if (Wcontainer <= 767) {
            var Wcontainer = $(".card-view").width();
            $('#datepicker-dropdown').css({width: "100%", "max-width": "767px", overflow: "auto"});
            $('.date-ranges-form').css({"margin-right": "10px"});
            $(".date-ranges-form .btn_static").css({position: "static"});
            if ($("#div_compare").length > 0) {
                $('.main-daterange').css({"padding-bottom": "0px"});
            } else {
                $('.main-daterange').css({"padding-bottom": "30px"});
            }
        } else {
            $('#datepicker-dropdown').css({width: "auto", overflow: "visible"});
            $(".date-ranges-form .btn_static").css({position: "absolute"});
        }
    }
    resize();
    $(window).resize(resize);
});
//</editor-fold>

