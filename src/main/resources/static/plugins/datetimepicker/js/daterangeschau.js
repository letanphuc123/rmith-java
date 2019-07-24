/**
 * DateRangerChau 1.0.0
 * Depends:
 *   jquery.js
 */
(function ($) {

    //<editor-fold defaultstate="collapsed" desc="Valiable">
    var $current_target;
    var $dropdown;
    var $datepicker;
    var $daterangePreset;
    var $enableComparison;
    var $comparisonPreset;
    var default_options = {
        aggregations: ['-'],
        values: {
            mode: 'tworanges',
            calendars: 4,
            dr1from: moment().subtract('days', 31),
            dr1to: moment().subtract('days', 1),
            dr2from: moment().subtract('days', 62),
            dr2to: moment().subtract('days', 32),
            daterangePreset: "custom",
            comparisonEnabled: false,
            removeComparison: false,
            comparisonPreset: "previousperiod",
            maxDate: 1,
            minDate: 0,
            dateFormat: "yyyy/MM/dd"
        }

    };
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Date ranges presets">
    var default_aggregation = 'whole';
    var db = {
        aggregations: {
            'whole': {
                title: "Whole period",
                presets: ['custom', 'yesterday', 'lastweeks', 'thismonths', 'lastmonths', 'last7days', 'last30days', 'last90days']
            }
        },
        date_presets: {
            'custom': {
                title: "Custom",
                dates: function () {
                    return null;
                }
            },
            'yesterday': {
                title: "Yesterday",
                dates: function () {
                    var dates = [];
                    dates[0] = moment().subtract('days', 1).valueOf();
                    dates[1] = moment().subtract('days', 1).valueOf();
                    return dates;
                }
            },
            'last7days': {
                title: "Past 7 days",
                dates: function () {
                    var dates = [];
                    dates[0] = moment().subtract('days', 7).valueOf();
                    dates[1] = moment().subtract('days', 1).valueOf();

                    return dates;
                }
            },
            'thismonths': {
                title: "This Month",
                dates: function () {
                    var dates = [];
                    dates[0] = moment().subtract('month', 0).startOf('month').valueOf();
                    dates[1] = moment().subtract('days', 0).valueOf();
                    return dates;
                }
            },
            'lastweeks': {
                title: "Last Week",
                dates: function () {
                    var dates = [];
                    dates[0] = moment().subtract('days', 7).isoWeekday(1).valueOf();
                    dates[1] = moment().subtract('days', 7).isoWeekday(7).valueOf();
                    return dates;
                }
            },
            'lastmonths': {
                title: "Last Month",
                dates: function () {
                    var dates = [];
                    dates[0] = moment().subtract('month', 1).startOf('month').valueOf();
                    dates[1] = moment().subtract('month', 1).endOf('month').valueOf();
                    return dates;
                }
            },
            'last30days': {
                title: "Past 30 days",
                dates: function () {
                    var dates = [];
                    dates[0] = moment().subtract('days', 31).valueOf();
                    dates[1] = moment().subtract('days', 1).valueOf();

                    return dates;
                }
            },
            'last90days': {
                title: "Past 90 days",
                dates: function () {
                    var dates = [];
                    dates[0] = moment().subtract('days', 90).valueOf();
                    dates[1] = moment().subtract('days', 1).valueOf();

                    return dates;
                }
            }
        }

    };
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Init method">
    var methods = {
        init: function (options) {
            if (!options) {
                options = default_options;
            }
            return this.each(function () {
                var $this = $(this);
                var data = $this.data('DateRangesChau');
                $this.data('test', internal);
                // initialize data in dom element
                if (data) {
                    $this.removeData('DateRangesChau');
                    $this.unbind('click');
                    $dropdown = undefined;
                    $("#datepicker-dropdown").remove();
                    var effective_options = $.extend({}, default_options, options);
                    $this.data('DateRangesChau', {
                        options: effective_options
                    });
                }
                if (!data) {
                    var effective_options = $.extend({}, default_options, options);
                    $this.data('DateRangesChau', {
                        options: effective_options
                    });
                }
                internal.createElements($this);
                internal.updateDateField($this);
            });
        }
    };
    //</editor-fold>

    var internal = {
        //<editor-fold defaultstate="collapsed" desc="Refresh form event">
        refreshForm: function ($target) {
            var lastSel = $datepicker.DatePickerGetLastSel();
            var values = $target.data("DateRangesChau").options.values;

            if ($('.comparison-preset', $dropdown).val() != 'custom') {
                lastSel = lastSel % 2;
                $datepicker.DatePickerSetLastSel(lastSel);
            }
            $('.dr', $dropdown).removeClass('active');
            $('.dr[lastSel=' + lastSel + ']', $dropdown).addClass('active');

            var dates = $datepicker.DatePickerGetDate()[0];

            var newFrom = $.format.date(dates[0], values.dateFormat);
            var newTo = $.format.date(dates[1], values.dateFormat);

            var oldFrom = $('.dr1.from', $dropdown).val();
            var oldTo = $('.dr1.to', $dropdown).val();

            if (newFrom != oldFrom || newTo != oldTo) {
                $('.dr1.from', $dropdown).val(newFrom);
                $('.dr1.to', $dropdown).val(newTo);

            }

            $('.dr1.from_millis', $dropdown).val(dates[0].getTime());
            $('.dr1.to_millis', $dropdown).val(dates[1].getTime());

            if (dates[2]) {
                $('.dr2.from', $dropdown).val($.format.date(dates[2], values.dateFormat));
            }
            if (dates[3]) {
                $('.dr2.to', $dropdown).val($.format.date(dates[3], values.dateFormat));
            }
        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Create element event">
        createElements: function ($target) {

            //<editor-fold defaultstate="collapsed" desc="Html container">
            $target.html(
                '<div class="date-range-field">' +
                '<span class="main"></span>' +
                '<span class="comparison"><span> Compare to: </span><span class="comparison-date"></span></span>' +
                '<input id="date_chau_value" type="hidden"/>' +
                '</div>'
                );
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Html option date">
            if (!$dropdown) {

                //<editor-fold defaultstate="collapsed" desc="Html option">
                $dropdown = $(
                    '<div id="datepicker-dropdown" >' +
                    '<div class="Grid">' +
                    '<div class="date-ranges-picker Grid-cell"></div>' +
                    '<div class="date-ranges-form Grid-cell">' +
                    '<div class="main-daterange">' +
                    '<div>' +
                    '<label>Date Range:</label>' +
                    '<div class="select">' +
                    '<select class="daterange-preset">' +
                    '</select>' +
                    '<div class="select__arrow"></div>' +
                    '</div>' +
                    '</div>' +
                    '<input type="text" class="dr dr1 from" lastSel="0"  readonly/> - <input type="text" class="dr dr1 to" lastSel="1" readonly/>' +
                    '<input type="hidden" class="dr dr1 from_millis" lastSel="2" /><input type="hidden" class="dr dr1 to_millis" lastSel="3" />' +
                    '<div id="div_compare">' +
                    '<label class="control control--checkbox"> Compare to: <input type="checkbox" class="enable-comparison"/> <div class="control__indicator"></div> </label>' +
                    '<div class="select">' +
                    '<select class="comparison-preset">' +
                    '<option value="custom">Custom</option>' +
                    '<option value="previousperiod" selected>Previous period</option>' +
//                        '<option value="previousyear">Previous year</option>' +
                    '</select>' +
                    '<div class="select__arrow"></div>' +
                    '</div>' +
                    '</div>' +
                    '<div class="comparison-daterange" style="display: none;">' +
                    '<input type="text" class="dr dr2 from" lastSel="2" /> - <input type="text" class="dr dr2 to" lastSel="3" />' +
                    '<input type="hidden" class="dr dr2 from_millis" lastSel="2" /><input type="hidden" class="dr dr2 to_millis" lastSel="3" />' +
                    '</div>' +
                    '</div>' +
                    '<div class="pull-right btn_static">' +
                    '<button class="btn btn-mini btn-cancel" id="button-cancel">Cancel</button>' +
                    '<button class="btn btn-mini btn-apply" id="button-ok">Apply</button>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>');

                $dropdown.appendTo($('body'));
                $datepicker = $('.date-ranges-picker', $dropdown);
                $daterangePreset = $('.daterange-preset', $dropdown);
                $enableComparison = $('.enable-comparison', $dropdown);
                $comparisonPreset = $('.comparison-preset', $dropdown);
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Inherit options from DRW options">
                var values = $target.data("DateRangesChau").options.values;
                if (values.removeComparison) {
                    $("#div_compare").remove();
                }
                $datepicker.DatePicker({
                    mode: values.mode,
                    starts: 0,
                    calendars: values.calendars,
                    inline: true,
                    date: [values.dr1from, values.dr1to],
                    current: values.dr1to,
                    selectableDates: [values.minDate == 0 ? null : moment().subtract('days', values.minDate), moment().subtract('days', values.maxDate)],
                    onChange: function (dates, el, options) {
                        // user clicked on datepicker
                        internal.setDaterangePreset('custom');
                    }
                });
                //</editor-fold>

                /**
                 * Handle change of datePreset
                 */
                $daterangePreset.change(function () {
                    $('.dr1', $dropdown).prop('disabled', ($daterangePreset.val() == 'custom' ? false : true));
                    internal.recalculateDaterange($target);
                });

                /**
                 * Handle enable/disable comparison.
                 */
                $enableComparison.change(function () {
                    internal.setComparisonEnabled($(this).is(':checked'));
                    if ($(this).is(":checked")) {
                        $(".comparison-daterange").css("display", "block");
                        $comparisonPreset.prop('disabled', false);
                    } else {
                        $(".comparison-daterange").css("display", "none");
                        $comparisonPreset.prop('disabled', true);
                    }
                });

                /**
                 * Handle change of comparison preset.
                 */
                $comparisonPreset.change(function () {
                    internal.recalculateComparison($target);
                    if ($(this).val() === "custom") {
                        $datepicker.DatePickerSetLastSel(2);
                        $('.dr', $dropdown).removeClass('active');
                        $('.dr[lastSel=' + 2 + ']', $dropdown).addClass('active');
                    } else if ($(this).val() === "previousperiod") {
                        $datepicker.DatePickerSetLastSel(0);
                        $('.dr', $dropdown).removeClass('active');
                        $('.dr[lastSel=' + 0 + ']', $dropdown).addClass('active');
                    }
                });

                /**
                 * Handle clicking on date field.
                 */
                $('.dr', $dropdown).click(function () {
                    // set active date field for datepicker
                    $datepicker.DatePickerSetLastSel($(this).attr('lastSel'));
                    $('.dr', $dropdown).removeClass('active');
                    $('.dr[lastSel=' + $(this).attr('lastSel') + ']', $dropdown).addClass('active');
                });

                /**
                 * Handle clicking on OK button.
                 */
                $('#button-ok', $dropdown).click(function () {
                    internal.retractDropdown($current_target);
                    internal.saveValues($current_target);
                    internal.updateDateField($current_target);
                    $("#daterangeschau").trigger("apply.event");
                    return false;
                });

                /**
                 * Handle clicking on OK button.
                 */
                $('#button-cancel', $dropdown).click(function () {
                    internal.retractDropdown($current_target);
                    $("#daterangeschau").trigger("cancel.event");
                    return false;
                });

            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Handle expand/retract of dropdown.">
            $target.bind('click', function () {
                var $this = $(this);
                if ($this.hasClass('DRWClosed')) {
                    internal.expandDropdown($this);
                } else {
                    internal.retractDropdown($this);
                }
                return false;
            });
            $dropdown.click(function (e) {
                e.stopPropagation();
            });
            $("body").click(function () {
                internal.retractDropdown($target);
            });

            $target.addClass('DRWInitialized');
            $target.addClass('DRWClosed');
            //</editor-fold>

        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Recalculater Date Ranges">
        recalculateDaterange: function ($target) {
            var date_preset = internal.getDaterangePreset();

            var dates = $datepicker.DatePickerGetDate()[0];

            // TODO: remove
            if (date_preset.dates == undefined)
                throw date_preset.title + " doesn't have dates()";

            var d = date_preset.dates();
            if (d != null) {
                dates[0] = d[0];
                dates[1] = d[1];
            }
            $datepicker.DatePickerSetDate(dates);
            internal.recalculateComparison($target);
        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Recalculate Comparision">
        recalculateComparison: function ($target) {
            var dates = $datepicker.DatePickerGetDate()[0];
            if (dates.length >= 2) {
                var comparisonPreset = internal.getComparisonPreset();
                switch (comparisonPreset) {
                    case 'previousperiod':
                        var days = parseInt((dates[1] - dates[0]) / (24 * 3600 * 1000));
                        dates[2] = new Date(dates[0]).setDate(dates[0].getDate() - (days + 1));
                        dates[3] = new Date(dates[1]).setDate(dates[1].getDate() - (days + 1));
                        break;
                    case 'previousyear':
                        dates[2] = new Date(dates[0]).setFullYear(dates[0].getFullYear(dates[0]) - 1);
                        dates[3] = new Date(dates[1]).setFullYear(dates[1].getFullYear(dates[1]) - 1);
                        break;
                }
                $datepicker.DatePickerSetDate(dates);
                $('.comparison-daterange input.dr', $dropdown).prop('disabled', (comparisonPreset == 'custom' ? false : true));
                internal.refreshForm($target);
            }
        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Loads values from target element's data to controls.">
        loadValues: function ($target) {
            var values = $target.data('DateRangesChau').options.values;
            // handle initial values
            $('.dr1.from', $dropdown).val(values.dr1from);
            $('.dr1.from', $dropdown).change();
            $('.dr1.to', $dropdown).val(values.dr1to);
            $('.dr1.to', $dropdown).change();
            $('.dr2.from', $dropdown).val(values.dr2from);
            $('.dr2.from', $dropdown).change();
            $('.dr2.to', $dropdown).val(values.dr2to);
            $('.dr2.to', $dropdown).change();

            $daterangePreset.val(values.daterangePreset);
            $daterangePreset.change();

            $enableComparison.prop('checked', values.comparisonEnabled);
            $enableComparison.change();

            $comparisonPreset.val(values.comparisonPreset);
            $comparisonPreset.change();
        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Stores values from controls to target element's data.">
        saveValues: function ($target) {
            var data = $target.data('DateRangesChau');
            var values = data.options.values;
            values.daterangePreset = internal.getDaterangePresetVal();

            values.dr1from = $('.dr1.from', $dropdown).val();
            values.dr1to = $('.dr1.to', $dropdown).val();
            values.dr1from_millis = $('.dr1.from_millis', $dropdown).val();
            values.dr1to_millis = $('.dr1.to_millis', $dropdown).val();

            values.comparisonEnabled = internal.getComparisonEnabled();
            values.comparisonPreset = internal.getComparisonPreset();

            values.dr2from = $('.dr2.from', $dropdown).val();
            values.dr2to = $('.dr2.to', $dropdown).val();
            values.dr2from_millis = $('.dr2.from_millis', $dropdown).val();
            values.dr2to_millis = $('.dr2.to_millis', $dropdown).val();

            $target.data('DateRangesChau', data);

            if ($target.data().DateRangesChau.options.onChange)
                $target.data().DateRangesChau.options.onChange(values);

        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Updates target div with data from target element's data">
        updateDateField: function ($target) {
            var values = $target.data("DateRangesChau").options.values;

            if (values.dr1from && values.dr1to) {
                $('span.main', $target).text($.format.date(new Date(values.dr1from), values.dateFormat) + ' - ' + $.format.date(new Date(values.dr1to), values.dateFormat));
            } else if (values.daterangePreset) {
                var dates = db.date_presets[values.daterangePreset].dates();
                $('span.main', $target).text($.format.date(dates[0], values.dateFormat) + ' - ' + $.format.date(dates[1], values.dateFormat));
            } else {
                $('span.main', $target).text($.format.date(new Date(values.dr1from), values.dateFormat) + ' - ' + $.format.date(new Date(values.dr1to), values.dateFormat));
                if (values.comparisonEnabled) {
                    $('span.comparison-date', $target).text($.format.date(new Date(values.dr2from), values.dateFormat) + ' - ' + $.format.date(new Date(values.dr2to), values.dateFormat));
                    $('span.comparison', $target).show();
                    $('span.comparison-divider', $target).show();
                }
            }
            if (values.comparisonEnabled && values.dr2from && values.dr2to) {
                $('span.comparison-date', $target).text($.format.date(new Date(values.dr2from), values.dateFormat) + ' - ' + $.format.date(new Date(values.dr2to), values.dateFormat));
                $('span.comparison', $target).show();
                $('span.comparison-divider', $target).show();
            } else {
                $('span.comparison-divider', $target).hide();
                $('span.comparison', $target).hide();
            }

            $("#date_chau_value").val('{"comparison": ' + values.comparisonEnabled +
                ',"dr1from":"' + $.format.date(new Date(values.dr1from), values.dateFormat) +
                '", "dr1to":"' + $.format.date(new Date(values.dr1to), values.dateFormat) +
                '", "dr2from":"' + $.format.date(new Date(values.dr2from), values.dateFormat) +
                '", "dr2to":"' + $.format.date(new Date(values.dr2to), values.dateFormat) +
                '"}');

            return true;
        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Set & get value">
        getDaterangePresetVal: function () {
            return $daterangePreset.val();
        },
        getDaterangePreset: function () {
            return db.date_presets[$daterangePreset.val()];
        },
        setDaterangePreset: function (value) {
            $daterangePreset.val(value);
            $daterangePreset.change();
        },
        setComparisonEnabled: function (enabled) {
            $datepicker.DatePickerSetMode(enabled ? 'tworanges' : 'range');
        },
        getComparisonEnabled: function () {
            return $enableComparison.prop('checked');
        },
        getComparisonPreset: function () {
            return $comparisonPreset.val();
        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Set date ranges presets (yesterday, month,...)">
        populateDateRangePresets: function () {
            var main_presets_keys = db.aggregations[default_aggregation].presets;
            var valueBackup = $daterangePreset.val();
            $daterangePreset.html('');

            // add main presets
            $.each(main_presets_keys, function (i, main_preset_key) {
                var date_preset = db.date_presets[main_preset_key];
                if (date_preset == undefined)
                    throw 'Invalid preset "' + main_preset_key + '".';
                $daterangePreset.append($("<option/>", {
                    value: main_preset_key,
                    text: date_preset.title
                }));
            });

            $daterangePreset.val(valueBackup);
        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Show popup event">
        expandDropdown: function ($target) {
            var options = $target.data("DateRangesChau").options;
            $current_target = $target;

            // init aggregations
            if (options.aggregations.length > 0) {
                internal.populateDateRangePresets();
            }

            internal.loadValues($target);


            // retract all other dropdowns
            $('.DRWOpened').each(function () {
                internal.retractDropdown($(this));
            });

            var leftDistance = $target.offset().left;
            var rightDistance = $(document).width() - $target.offset().left - $target.width();
            $dropdown.show();
            if (rightDistance > leftDistance) {
                // align left edges
                $dropdown.offset({
                    left: $target.offset().left,
                    top: $target.offset().top + $target.height() - 1
                });

            } else {
                // align right edges
                var fix = parseInt($dropdown.css('padding-left').replace('px', '')) +
                    parseInt($dropdown.css('padding-right').replace('px', '')) +
                    parseInt($dropdown.css('border-left-width').replace('px', '')) +
                    parseInt($dropdown.css('border-right-width').replace('px', ''));
                $dropdown.offset({
                    left: $target.offset().left + $target.width() - $dropdown.width() - fix,
                    top: $target.offset().top + $target.height() - 1
                });

            }


            // switch to up-arrow


            $target.addClass('DRWOpened');
            $target.removeClass('DRWClosed');

            //default compare
            if (options.values.comparisonEnabled) {
                $enableComparison.prop('checked', true);
                internal.setComparisonEnabled(internal.getComparisonEnabled());
                if (internal.getComparisonEnabled()) {
                    $(".comparison-daterange").css("display", "block");
                } else {
                    $(".comparison-daterange").css("display", "none");
                }
            } else {
                $comparisonPreset.prop('disabled', true);
            }

            // refresh
            internal.recalculateDaterange($target);
        },
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Hide popup event">
        retractDropdown: function ($target) {
            $dropdown.hide();


            $target.addClass('DRWClosed');
            $target.removeClass('DRWOpened');
        }
        //</editor-fold>
    };

    //<editor-fold defaultstate="collapsed" desc="Init">
    $.fn.DateRangesChau = function (method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.DateRangesChau');
        }
    };
    //</editor-fold>

})(jQuery);
