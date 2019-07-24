var sessionLanguage = "";
var dbLang = {};
var dfLang = {
    "lb_select_all": "All",
    "lb_noti_change_email": "After changed email. A verification link will be sent to new email.",
    "lb_alert_warning": "Warning!",
    "lb_not_active": "Your account is unverified! Please check your email to active your account.",
    "lb_button_resend_mail": "Resend email",
    "lb_button_close": "Close",
    "lb_new_mail_not_active": "Your new email is unverified! Please check your email to activate your new email.",
    "lb_alert_new_mail": "Email not activated yet"
};

var Accounts = function () {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    return {
        init: function () {
            // init language
            dbLang = initLanguage();
            sessionLanguage = $("#txtSessionLanguage").val().length > 0 ? $("#txtSessionLanguage").val().toLowerCase() : "ja";
            // Format grid Account
            formatGridAccount();
            //Date Range    
            initDateTimePicker('#txt_start_date', 'Y-m-d H:i:s', true);
            initDateTimePicker('#txt_end_date', 'Y-m-d H:i:s', true);
            // Handle checkbox
            handleCheckbox("#cbo_is_admin");
            handleCheckbox("#cbo_active_account");
            //Edit Account
            handleFormEditAccount();
            $('#tbl_account').on('click', '.btnEdit', function () {
                removeError();
                if ($(this).attr('data-admin') === "1") {
                    $("#cbo_is_admin").prop('checked', true);
                    $("#cbo_is_admin").val(1);
                } else {
                    $("#cbo_is_admin").prop('checked', false);
                    $("#cbo_is_admin").val(0);
                }
                if ($(this).attr('data-status') === "1") {
                    $("#cbo_active_account").prop('checked', true);
                    $("#cbo_active_account").val(1);
                } else {
                    $("#cbo_active_account").prop('checked', false);
                    $("#cbo_active_account").val(0);
                }
                $("#txt_firstname").val($(this).attr('data-first-name'));
                $("#txt_lastname").val($(this).attr('data-last-name'));
                $("#txt_company").val($(this).attr('data-company-name'));
                $("#txt_mobile").val($(this).attr('data-mobile'));
                $("#txt_email").val($(this).attr('data-email'));
                $("#txt_old_email").val($(this).attr('data-email'));
                $("#txt_accountId").val($(this).attr('data-account-id'));
                $("#txt_start_date").val($(this).attr('data-date-start'));
                $("#txt_end_date").val($(this).attr('data-date-end'));
                $("#txt_end_date").attr("data-old", $(this).attr('data-date-end'));
                $("#cbo_group_user").val($(this).attr('data-group-id'));
                $("#txt_old_group").val($(this).attr('data-group-id'));
                $("#txt_old_package").val($(this).attr('data-package-id'));
                $("#cbo_package").val($(this).attr('data-package-id'));
                $("#cbo_language").val($(this).attr('data-language-code'));
                $("#txt_memo").val($(this).attr('data-memo'));
                
                let mailWaitActive = $(this).attr("data-mail-wait-active");
                $("#txt_email_wait_active").val(mailWaitActive);
                if(mailWaitActive) {
                    $('#txt_email').prop('disabled', true);
                    $('#messageEmail').attr('class', 'txt--navyblue font--12 mt--5 block');
                    $('#messageEmail').text(setLang(dbLang.lb_alert_new_mail, dfLang.lb_alert_new_mail) + ': ' + mailWaitActive);
                    $('#messageResend').attr('class', 'txt--danger font--12 mt--5 block');
                    $('#messageResend').attr('onclick', 'showPopupReSendActiveEmail("' + mailWaitActive + '", "ChangeNewEmail")');
                } else {
                    $('#txt_email').prop('disabled', false);
                    $('#messageEmail').attr('class', 'display--none');
                    $('#messageResend').attr('class', 'display--none');
                }          
            });
            
            //show message when user change email.
            showMessageChangeEmail(setLang(dbLang.lb_noti_change_email, dfLang.lb_noti_change_email));
        }
    };
    //</editor-fold>

}();

//<editor-fold defaultstate="collapsed" desc="FORMAT GRID ACCOUNTS">
var formatGridAccount = function () {
    $('#tbl_account').DataTable({
        "language": {
            "url": $("#txtContextPath").val() + "static/plugins/data-table/languages/datatable-" + sessionLanguage + ".json"
        },
        "lengthMenu": [[25, 50, 100, -1], [25, 50, 100, setLang(dbLang.lb_select_all, dfLang.lb_select_all)]],
        "columnDefs": [
            {"className": "dt-nowrap", "targets": [0, 1, 2, 5, 6, 8, 9, 10, 11]},
            {"className": "dt-center dt-nowrap", targets: [3, 4, 5]},
            {orderable: false, "targets": [3, 4, 5, 11]},
//            { "width": "292", "targets": "0" }
        ],
        "initComplete": function () {
            hideWait("#divAccountWaiting");
            // Enable TFOOT scoll bars
            $('.dataTables_scrollFoot').css('overflow', 'auto');

            $('.dataTables_scrollHead').css('overflow', 'auto');

            // Sync TFOOT scrolling with TBODY
            $('.dataTables_scrollFoot').on('scroll', function () {
                $('.dataTables_scrollBody').scrollLeft($(this).scrollLeft());
            });

            $('.dataTables_scrollHead').on('scroll', function () {
                $('.dataTables_scrollBody').scrollLeft($(this).scrollLeft());
            });
        },
        "scrollX": true,
        "scrollCollapse": true,
        "scrollY": false,
        "paging": true,
        "info": true,
        "order": [[10, 'desc']],
        "deferRender": true,
//        "sScrollX": "190%"

    });
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FORM EDIT ACCOUNT">
var handleFormEditAccount = function () {
    $("#frm_edit_account").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'txt--error font--12 mt--5', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            "firstName": {
                maxlength: 50
            },
            "lastName": {
                maxlength: 50
            },
            "email": {
                required: true,
                email: true,
                maxlength: 150
            },
            "accountLanguage": {
                maxlength: 2
            },
            "mobile": {
                maxlength: 50
            },
            "companyName": {
                maxlength: 500
            }
        },
        highlight: function (element) { // hightlight error inputs
            $(element).closest('.form-group').addClass('has--error'); // set error class to the control group
        },
        unhighlight: function (element) { // revert the change done by hightlight
            $(element).closest('.form-group').removeClass('has--error'); // set error class to the control group
        },
        success: function (label) {
            label.closest('.form-group').removeClass('has--error'); // set success class to the control group
        },
        submitHandler: function (e) {
            var isChangeDateEnd = $("#txt_end_date").val() !== $("#txt_end_date").attr("data-old") ? 1 : 0;
            $("#txtIsChangeDateEnd").val(isChangeDateEnd);
            $(".close").click();
            showWait("#divBlockAccountWaiting");
            e.preventDefault();
            return true;
        }
    });
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="DELETE ACCOUNT">
var showPopupDeleteAccount = function (email, accountId, packageId, groupId) {
    // Reset input email confirm delete account
    $("#txtEmailDelete").val("");
    $("#btnDeleteAccount").attr("disabled", true);

    // Save old email confirm
    $("#txtEmailConfirmDelete").val(email);
    $("#mdConfirmEmail").modal("show");
    $("#btnDeleteAccount").click(function () {
        $("#txt_delete_accountId").val(accountId);
        $("#txtDeletePackageId").val(packageId);
        $("#txtDeleteGroupId").val(groupId);
        $("#frmDeleteAccount").submit();
        $("#mdConfirmEmail").modal("hide");
        showWait("#divBlockAccountWaiting");
    });
};

var enableButtonDeleteAccount = function () {
    var disabledBtnDelete = $("#txtEmailConfirmDelete").val() !== $("#txtEmailDelete").val();
    $("#btnDeleteAccount").attr("disabled", disabledBtnDelete);
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="SHOW POPUP RESEND ACTIVE EMAIL">
var showPopupReSendActiveEmail = function (email, action) {
        swal({
            title: setLang(dbLang.lb_alert_warning, dfLang.lb_alert_warning),
            text: action === "ChangeNewEmail" ? setLang(dbLang.lb_new_mail_not_active, dfLang.lb_new_mail_not_active) : setLang(dbLang.lb_not_active, dfLang.lb_not_active),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#f83f37",
            confirmButtonText: setLang(dbLang.lb_button_resend_mail, dfLang.lb_button_resend_mail),
            cancelButtonText: setLang(dbLang.lb_button_close, dfLang.lb_button_close),
            closeOnConfirm: false,
            closeOnCancel: false
        }, function (isConfirm) {
            $(".sweet-overlay, .sweet-alert").remove(); //HACK: use another close method
            if (isConfirm) {
                $("#emailVerify").val(email);
                $("#redirect").val(action === "ChangeNewEmail" ? "ChangeNewEmail" : "accounts");
                $("#frmResendActiveEmail").submit();
            }
        });
    };
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="REMOVE ERROR">
var removeError = function () {
    $(".txt--error").remove();
    $("#txt_email").removeClass("has--error");
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="INIT">
$(document).ready(function () {
    Accounts.init();
});
//</editor-fold>
