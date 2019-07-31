var addNewCategory, updateCategory;

//<editor-fold defaultstate="collapsed" desc="INIT">
var Categories = function (addNewLang, updateLang) {
    return {
        init: function (addNewLang, updateLang) {
            var sessionLanguage = $("#txtSessionLanguage").val().length > 0 ? $("#txtSessionLanguage").val().toLowerCase() : "ja";
            
            var table = $('#tbl_category').DataTable({
                "language": {
                    "url": $("#txtContextPath").val() + 'static/plugins/data-table/languages/datatable-' + sessionLanguage + '.json'
                },
                "columnDefs": [
                    { "visible": false, "targets": 0 },
                    {"width": "80", "targets": [2]},
                    {"width": "150", "targets": [3]},
                    {"width": "100", "targets": [4]}
                ],
                "order": [[ 0 , 'asc' ]],
                "displayLength": 25,
                "stripeClasses": [ 'odd-row', 'even-row' ],
                "drawCallback": function (settings) {
                    var api = this.api();
                    var rows = api.rows({page:'current'}).nodes();
                    var last = null;
                    api.column(0, {page:'current'}).data().each(function (group, i) {
                        if (last !== group) {
                            $(rows).eq(i).before('<tr class="group"><td colspan="5">' + group + '</td></tr>');
                            last = group;
                        }
                    });
                },
                "initComplete": function () {
                    hideWait("#divCategoriesWaiting");
                }
            });

            // Order by the grouping
            $('#tbl_category tbody').on('click', 'tr.group', function () {
                var currentOrder = table.order()[0];
                if (currentOrder[0] === 0 && currentOrder[1] === 'asc') {
                    table.order([0, 'desc']).draw();
                } else {
                    table.order([0, 'asc']).draw();
                }
            });
            
            addNewCategory = addNewLang;
            updateCategory = updateLang;
        }
    };
}();
 //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FORM VALIDATE">
var formValidate = function () {
    jQuery.validator.addMethod('alpha_numeric', function (value) {
        return value.match(/^([a-zA-Z0-9-]+)$/);
    });

    return $("#frmCategory").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'txt--error font--12 mt--5', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            "categoryId": {
                required: true,
                "alpha_numeric": true
            },
            "categoryName": {
                required: true
            },
            "orderNumber": {
                required: true,
                number: true
            },
            "categoryCode":{
                required: true
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
        submitHandler: function () {
            showWait("#divBlockCategoryWaiting");
            return true;
        }
    });
}();
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="SHOW CATEGORY POPUP">
var showCategoryPopup = function (iCategoryId, iCategoryName, iCategoryCode, iMenuCode, iOrder, iAction, categoryId) {    
    $("#txtCategoryCode").removeAttr("disabled");    
    $("#txtCategoryCode").val(iCategoryCode);   
    $("#txtCategoryName").val(iCategoryName);
    $("#selMenuCode").val(iMenuCode);
    $("#txtOrder").val(iOrder);

    if (iAction === "update") {
        $("#frmCategory").attr("action", $("#txtContextPath").val() + "update-category");
        $("#txtCategoryId").val(iCategoryId);
        $("#myModalLabel").text(updateCategory);
        $("#txtCategoryCode").attr("disabled", "disabled");
    } else {
        $("#frmCategory").attr("action", $("#txtContextPath").val() + "add-new-category");
        $("#txtCategoryId").val("");
        $("#myModalLabel").text(addNewCategory);
        $("#categoryId").removeAttr("name"); //auto set default categoryId for Add Category function
    }
    formValidate.resetForm();
    $("#categoryId").val(categoryId);
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="DETELE CATEGORY">
var deleteCategory = function (idCategoryDelete) {
    showWait("#divBlockCategoryWaiting");
    $("#idCategoryDelete").attr("name", "categoryId");
    $("#idCategoryDelete").val(idCategoryDelete);
    $("#frmDelete").attr("action", $("#txtContextPath").val() + "delete-category");
    $("#frmDelete").submit();
};
 //</editor-fold>
