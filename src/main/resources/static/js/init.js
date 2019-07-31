/** *************Init JS*********************
 
 TABLE OF CONTENTS
 ---------------------------
 1.Ready function
 2.Load function
 3.Full height function
 4.admintres function
 5.Resize function
 ** ***************************************/

"use strict";
/*****Ready function start*****/
$(document).ready(function () {
    admintres();
    $('.load-page > .loading').addClass('la--animate');

    if (getCookie("_.language_out_of_login") === null || getCookie("_.language_out_of_login") === "") {
        createCookieWithTimeout("_.language_out_of_login", "",10 * 365 * 24 * 60 * 60);
        $("#changeLanguageByCookie").val("JA");
    } else {
        $("#changeLanguageByCookie").val(getCookie("_.language_out_of_login"));
    }
    $("#changeLanguageByCookie").on("change", function () {
        $("#txtChangeLanguageCookie").val($(this).val());
        createCookieWithTimeout("_.language_out_of_login", $(this).val(), 10 * 365 * 24 * 60 * 60);
        window.location.reload();

    });

    $("#changeLanguage").on("change", function () {
        createCookieWithTimeout("_.language_out_of_login", $(this).val(), 10 * 365 * 24 * 60 * 60);
        $("#txtChangeLanguageSession").val($(this).val());
        var pathName = window.location.pathname.toString();
        var queryString = window.location.search.toString();
        var rediect = pathName.replace($("#txtContextPathChangeLanguageSession").val(), '') + queryString;
        $("#txtChangeLanguageSessionRedirect").val(rediect);
        $("#frmChangeLanguage").submit();
    });

});
/*****Ready function end*****/

/*****Load function start*****/
$(window).on("load", function () {
    $(".load-page").delay(500).fadeOut("slow");
    // Call loading function when need
    /*Progress Bar Animation*/
    var progressAnim = $('.progress-anim');
    if (progressAnim.length > 0) {
        for (var i = 0; i < progressAnim.length; i++) {
            var $this = $(progressAnim[i]);
            $this.waypoint(function () {
                var progressBar = $(".progress-anim .progress-bar");
                for (var i = 0; i < progressBar.length; i++) {
                    $this = $(progressBar[i]);
                    $this.css("width", $this.attr("aria-valuenow") + "%");
                }
            }, {
                triggerOnce: true,
                offset: 'bottom-in-view'
            });
        }
    }
});
/*****Load function* end*****/

/***** Full height function start *****/
var setHeightWidth = function () {
    var height, width;
    // var clickAllowed;
    height = $(window).height();
    width = $(window).width();

    // flag to allow clicking
    // clickAllowed = true;

    $('.full-height').css('height', (height));
    $('.page-wrapper').css('min-height', (height));
};
/***** Full height function end *****/

/***** admintres function start *****/
var $wrapper = $(".wrapper");
var admintres = function () {



    /*Sidebar Collapse Animation*/
    var sidebarNavCollapse = $('.fixed-sidebar-left .side-nav  li .collapse');
    var sidebarNavAnchor = '.fixed-sidebar-left .side-nav  li a';
    $(document).on("click", sidebarNavAnchor, function (e) {
        if ($(this).attr('aria-expanded') === "false")
            $(this).blur();
        $(sidebarNavCollapse).not($(this).parent().parent()).collapse('hide');
    });

    /*Panel Remove*/
    $(document).on('click', '.close-panel', function (e) {
        var effect = $(this).data('effect');
        $(this).closest('.panel')[effect]();
        return false;
    });

    $(document).on('click', '#toggle_nav_btn', function (e) {
        $wrapper.toggleClass('slide-nav-toggle');
        $("i", this).toggleClass("fa-indent fa-outdent");
        return false;
    });

    $(document).on("mouseenter mouseleave", ".wrapper > .fixed-sidebar-left", function (e) {
        if (e.type == "mouseenter") {
            $wrapper.addClass("sidebar-hover");
        } else {
            $wrapper.removeClass("sidebar-hover");
        }
        return false;
    });

    $(document).on("mouseenter mouseleave", ".wrapper > .setting-panel", function (e) {
        if (e.type == "mouseenter") {
            $wrapper.addClass("no-transition");
        } else {
            $wrapper.removeClass("no-transition");
        }
        return false;
    });

    /*Slimscroll*/
    $('.nicescroll-bar').slimscroll({height: '100%', color: '#878787', disableFadeOut: true, borderRadius: 0, size: '4px', alwaysVisible: false});

    /*Fullscreen Init Js*/
    $(document).on("click", ".full-screen", function (e) {
        $(this).parents('.panel').toggleClass('fullscreen');
        window.dispatchEvent(new Event('resize'));
    });
};
/***** admintres function end *****/

/***** Resize function start *****/
$(window).on("resize", function () {
    setHeightWidth();
}).resize();
/***** Resize function end *****/

/***** Tooltip *****/
if ($('[data-tooltip="tooltip"]').length > 0)
    $('[data-tooltip="tooltip"]').tooltip();
if ($('[data-toggle="popover"]').length > 0)
    $('[data-toggle="popover"]').popover();
/***** Tooltip *****/
/***** Scroll Fixed Top Menu *****/
$(window).scroll(function () {
        if ($(window).scrollTop() > ($('.navbar').height() - 0))  {
            $('.fixed-sidebar-left').addClass('is-sticky');
        } else {
            $('.fixed-sidebar-left').removeClass('is-sticky');
        }
    }); 
/***** End Scroll Fixed Top Menu *****/    
