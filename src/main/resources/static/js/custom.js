//<editor-fold defaultstate="collapsed" desc="CHECK VALUE EMAIL, SHOW WARNING IF EMAIL VALUE IS CHANGING">
var showMessageChangeEmail = function (notification) {
    $('#txt_email').on('input', function () {
        if ($('#txt_old_email').val() !== $('#txt_email').val()) {
            $('#messageEmail').attr('class', 'txt--yellow font--12 mt--5 block');
            $('#messageEmail').text(notification);
        } else {
            $('#messageEmail').attr('class', 'display--none');
            $('#messageEmail').text('');
        }
    });
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="SHOW ALERT">
var showAlert = function (title, text, type, isOnLoad) {
    if (title === "") {
        switch (type) {
            case "success":
                title = "Successful!";
                break;
            case "info":
                break;
            case "warning":
                title = "Warning!";
                break;
            case "error":
                title = "Error!";
                break;
            default:
                break;
        }
    }
    if (isOnLoad) {
        swal({
            title: title,
            html: true,
            text: text,
            type: type,
            allowOutsideClick: true,
            timer: 30000
        });
    } else {
        $(window).load(function () {
            swal({
                title: title,
                html: true,
                text: text,
                type: type,
                allowOutsideClick: true,
                timer: 30000
            });
        });

    }
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="SHOW ALERT DELETE">
// FIX : need to change to dymanic, not only use for delete function but also for active function, e.g active message template
// param (title, text, confirmButton text, callbackFunction)
var showAlertDelete = function (title, callbackFunction, contentText, yesText, noText) {
    swal({
        title: title,
        text: contentText || "You can not recover it after delete successfully!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#f83f37",
        confirmButtonText: yesText || "Yes",
        cancelButtonText: noText || "Cancel",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function (isConfirm) {
        $(".sweet-overlay, .sweet-alert").remove(); //HACK: use another close method
        if (isConfirm) {
            var functionCallback = new Function(callbackFunction);
            functionCallback();
        }
    });
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="DYNAMIC HEIGHT GRID">
var dynamicHeightGrid = function (heightGrid, maxHeightGrid, elementGrid) {
    if (heightGrid > maxHeightGrid) {
        $(elementGrid).css("height", maxHeightGrid + "px");
    } else {
        $(elementGrid).css("height", heightGrid + 50 + "px");
    }
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="SHOW WAIT">
var showWait = function (ele) {
    if ($(ele).length > 0 && $(ele).html().length > 0) {
        $(ele).show();
        return;
    }
    var messageHtml = '<div class="loading-page">';
    messageHtml += '<div class="loading-center--absolute">';
    messageHtml += '<div class="object object--one"></div>';
    messageHtml += '<div class="object object--two"></div>';
    messageHtml += '<div class="object object--three"></div>';
    messageHtml += '</div>';
    messageHtml += '</div>';
    $(ele).html(messageHtml);
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="HIDE WAIT">
var hideWait = function (ele) {
    $(ele).hide(); // Show waiting for current block with delay 1500 milliseconds
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FORMAT DATETIME">
var formatDatetime = function (dateString) {
    var date = new Date(dateString);
    var year = padDate(date.getFullYear());
    var month = padDate(date.getMonth() + 1);
    var day = padDate(date.getDate());
    return year + "-" + month + "-" + day;
};

var formatMonth = function (dateString) {
    var date = new Date(dateString);
    var year = padDate(date.getFullYear());
    var month = padDate(date.getMonth() + 1);
    return year + "-" + month;
};

var formatFullTime = function (dateString) {
    var date = new Date(dateString);
    var hh = padDate(date.getHours());
    var mm = padDate(date.getMinutes());
    var ss = padDate(date.getSeconds());
    return hh + ":" + mm + ":" + ss;
};

var padDate = function (patternDate) {
    return (patternDate < 10 ? '0' : '') + patternDate;
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GET LAST DAY TIME">
var getLastDaysTime = function (numberOfDayAgo) {
    var today = new Date();
    // if today is monday, and want to get tuesday 00:00:000 last week, numberOfDay is 6
    var lastDay = new Date(today.getTime() - numberOfDayAgo * 24 * 60 * 60 * 1000);
    var yyyy = lastDay.getFullYear();
    var mm = lastDay.getMonth();
    var dd = lastDay.getDate();
    lastDay = new Date(yyyy, mm, dd, 00, 00, 00, 000);
    return lastDay.getTime();
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GET LAST MONTH TIME">
function getLastMonthsTime(numberOfMonthAgo) {
    var today = new Date();
    // get the first day of month ago
    // if this month is February-2019, want to get the 01-September-2018, numberOfMonthAgo is 5
    let thisYear = today.getFullYear();
    let thisMonth = today.getMonth();
    let yearOfMonthAgo = thisYear;
    let monthAgo = 0;
    if (thisMonth - numberOfMonthAgo > 0) {
        monthAgo = thisMonth - numberOfMonthAgo;
    } else if (thisMonth - numberOfMonthAgo < 0) {
        monthAgo = thisMonth - numberOfMonthAgo + 12;
        yearOfMonthAgo = thisYear - 1;
    }
    let firstDayOfMonth = new Date(yearOfMonthAgo, monthAgo, 01, 00, 00, 00, 000);
    return firstDayOfMonth.getTime();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CHANGE GOOGLE SITE">
var changeSite = function (siteID) {
    createCookieWithTimeout("_.site", siteID, 24);
    document.location.reload();
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GET COOKIE">
function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ')
            c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0)
            return c.substring(nameEQ.length, c.length);
    }
    return null;
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="INIT PARAM">
var param = function (name, value) {
    this.name = name;
    this.value = value;
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="DATE RANGE">
var handleDateRange = function (module, mindate) {
    // check cookie date
    var cookieKey = module + "_date";
    var cookieValueDate = decodeURIComponent(getCookie(cookieKey));
    if (cookieValueDate !== 'null' && cookieValueDate && typeof (cookieValueDate) !== 'undefined') {
        var dateRange = $.parseJSON(cookieValueDate);
        initDateRange(new Date(dateRange.dr1from), new Date(dateRange.dr1to), new Date(dateRange.dr2from), new Date(dateRange.dr2to), mindate);
    } else {
        var dr1from = moment().subtract('days', 30);
        var dr1to = moment().subtract('days', 0);
        var dr2from = moment().subtract('days', 31);
        var dr2to = moment().subtract('days', 0);
        initDateRange(dr1from, dr1to, dr2from, dr2to, mindate);
    }

    submitFormDateRange(module);
};

var submitFormDateRange = function(module) {
    $("#daterangeschau").on("apply.event", function () {
        $("#txtDateRange").val($("#date_chau_value").val().replace(/\ /g, ''));
        switch(module) {
            case 'monitor-used':
                $("#frmFilterMonitoringUsed").submit();
                break;
            case 'monitor-api':
                $("#frmFilterMonitoringAPI").submit();
                break;
            case 'ask-review':
                $("#frmFilterAskReview").submit();
                break;
        }
    });
};

var initDateRange = function (dr1from, dr1to, dr2from, dr2to, mindate) {
    var minDate = getMinDate(mindate);
    dr1from = getMinFromDate(dr1from);
    dr2from = new Date(new Date(dr1from).getTime() + (24 * 60 * 60 * 1000));
    $('#daterangeschau').DateRangesChau({
        aggregations: ['-'],
        values: {
            mode: 'tworanges',
            calendars: 2,
            dr1from: dr1from,
            dr1to: dr1to,
            dr2from: dr2from,
            dr2to: dr2to,
            daterangePreset: "custom",
            comparisonEnabled: false,
            removeComparison: true,
            comparisonPreset: "previousperiod",
            maxDate: 0,
            minDate: minDate,
            dateFormat: "yyyy-MM-dd"
        }
    });
    $("#datepicker-dropdown").prependTo("#daterangeschau");
    function resize() {
        var Wcontainer = $(".portlet-title").width();
        var Wdatepicker = $("#datepicker-dropdown").width();
        if (Wdatepicker >= Wcontainer) {
            $('#datepicker-dropdown').css({width: Wcontainer + "px", overflow: "auto"});
            $('.date-ranges-form').css({"margin-right": "10px"});
            $('.main-daterange').css({"padding-bottom": "30px"});
            $(".date-ranges-form .btn_static").css({position: "static"});
        } else if (Wcontainer <= 767) {
            var Wcontainer = $(".mailbox-read-info").width();
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
};
var dataMin;
var getMinDate = function (dateM) {
    if (typeof dateM == 'undefined') {
        dateMin = new Date(2017, 11 - 1, 1);
    } else
        dateMin = new Date(dateM);
    var dateNow = new Date();
    return (Math.round((dateNow - dateMin) / (1000 * 60 * 60 * 24)) + 1);
};
var getMinFromDate = function (dr1from) {
    if (dateMin > dr1from) {
        return dateMin;
    }
    return dr1from;
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="HANDLE CHECKBOX">
var handleCheckbox = function (idElenment) {
    $(idElenment).on("click", function () {
        var value = $(idElenment).is(':checked') ? 1 : 0;
        $(idElenment).val(value);
    });
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="INIT DATE TIME PICKER">
var initDateTimePicker = function (id, format, isTimePicker) {
    // format 'Y-m-d' or Y-m-d H:i:s
    $(id).datetimepicker({
        timepicker: isTimePicker,
        format: format
    });
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ESCAPE HTML">
var escapeHtml = function (str) {
    return (str + '')
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;');
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="SHOW OR HIDE NECESSARY SETTINGS">
var showHideNecessarySettings = function (contextPath, csrfToken) {
    $(".checkPopup").on("click", function () {
        if ($("#cboDoNotShowAgain").is(':checked')) {
            $.ajax({
                url: contextPath + "do-not-show-necessary-settings",
                type: "POST",
                headers: {'X-CSRF-TOKEN': csrfToken},
                success: function (response) {},
                error: function () {}
            });
        } else {
            $.ajax({
                url: contextPath + "show-necessary-settings",
                type: "POST",
                headers: {'X-CSRF-TOKEN': csrfToken},
                success: function (response) {},
                error: function () {}
            });
        }
    });
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CLOSE NECESSARY SETTINGS">
var closeNecessarySettings = function () {
    $("#popupNecessarySettings").addClass("display--none");
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GET PARAM IN URL BROWSER">
var getPramsUrl = function (param) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === param) {
            return decodeURIComponent(pair[1]);
        }
    }

    return(false);
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CREATE COOKIE">
var createCookie = function (name, value) {
    var expires = "";
    var date = new Date();
    var hour = date.getHours();
    var time = (24 - hour);
    date.setTime(date.getTime() + (time * 60 * 60 * 1000));
    expires = "; expires=" + date.toGMTString();
    document.cookie = name + "=" + value + expires + "; path=/";
};

var createCookieWithTimeout = function (name, value, time) {
    var expires = "";
    var date = new Date();
    date.setTime(date.getTime() + (time * 60 * 60 * 1000));
    expires = "; expires=" + date.toGMTString();
    document.cookie = name + "=" + value + expires + "; path=/";
};

function eraseCookie(name) {
    createCookie(name, "", -1);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="SET LANGUAGE">
var setLang = function (databaseLanguage, defaultLanguage) {
    return (typeof (databaseLanguage) !== 'undefined' ? databaseLanguage : defaultLanguage);
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="INIT LANGUAGE">
var initLanguage = function () {
    var language = {};
    if ($("#txtInitLanguage").val().length > 0) {
        language = JSON.parse($("#txtInitLanguage").val());
    }
    return language;
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FORMAT MONEY">
Number.prototype.formatMoney = function (c, d, t) {
    var n = this,
            c = isNaN(c = Math.abs(c)) ? 2 : c,
            d = d === undefined ? "." : d,
            t = t === undefined ? "," : t,
            s = n < 0 ? "-" : "",
            i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "",
            j = (j = i.length) > 3 ? j % 3 : 0;
    return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="SHOW WAIT">
var showWaitBox = function (ele) {
    if ($(ele).length > 0 && $(ele).html().length > 0) {
        $(ele).show();
        return;
    }
    var messageHtml = '<div class="loading-page loading-box">';
    messageHtml += '<div class="loading-center--absolute">';
    messageHtml += '<div class="object object--one"></div>';
    messageHtml += '<div class="object object--two"></div>';
    messageHtml += '<div class="object object--three"></div>';
    messageHtml += '</div>';
    messageHtml += '</div>';
    $(ele).html(messageHtml);
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GET HOST">
var getHost = function (url) {
    if (url === '' || typeof url === 'undefined') {
        return '';
    }
    var a = document.createElement('a');
    a.href = url;
    return a.hostname;
};
//</editor-fold>

