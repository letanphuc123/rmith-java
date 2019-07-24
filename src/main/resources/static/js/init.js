"use strict";
/*****Ready function start*****/
$(document).ready(function () {
    admintres();
});
/*****Ready function end*****/

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
