//<editor-fold defaultstate="collapsed" desc="CROP IMAGE PROFILE">
var initCroppie = function (imageCroppie) {
    $('#' + imageCroppie.idInputFile).on('change', function () {
        previewFile(this);
        $('#' + imageCroppie.idInputFile).attr("data-filename", $('#' + imageCroppie.idInputFile).val().split('\\').pop());
    });

    $("#" + imageCroppie.popupCropImage + " .cancel-btn").click(function () {
        $("#" + imageCroppie.popupCropImage).css('display', 'none');
        $('#' + imageCroppie.divImg).croppie('destroy');
        $("#" + imageCroppie.imgContainerDiv).html("");
        $('#' + imageCroppie.idInputFile).val('');
    });

    function previewFile(e) {
        var file = e.files[0];
        var reader = new FileReader();

        reader.addEventListener("load", function () {
            var img = document.createElement("img");
            img.src = reader.result; 
            img.crossOrigin = "Anonymous";
            img.id = imageCroppie.divImg;
            $("#" + imageCroppie.imgContainerDiv).html("");
            $("#" + imageCroppie.imgContainerDiv).append(img);
            $("#" + imageCroppie.divImg).load(function () {
                $uploadCrop = $('#' + imageCroppie.divImg).croppie({
                    viewport: {
                        width: imageCroppie.viewPortWidth,
                        height: imageCroppie.viewPortHeight,
                        type: 'square'
                    },
                    enforceBoundary: false,
                    showZoomer: false,
                    enableExif: true,
                    quality: 0.5
                });
                $("#" + imageCroppie.popupCropImage + " .upload-result").unbind();
                $("#" + imageCroppie.popupCropImage + " .upload-result").click(function () {
                    $uploadCrop.croppie('result', {
                        type: 'base64',
//                         size: { width: 300, height: 300 },
                        format: 'png'
                    }).then(function (resp) {
                        $("#" + imageCroppie.uploadImage).css("display", "none");
                        popupResult({
                            src: resp
                        });
                    });
                });
                $("#" + imageCroppie.popupCropImage).css('display', 'block');
            });
        }, false);

        if (file) {
            reader.readAsDataURL(file);
        }
    }
    
    function popupResult(result) {
        var html;
        if (result.html) {
            html = result.html;
        }
        if (result.src) {
            html = '<img src="' + result.src + '" />';
            $("#profile-photo-data").val(result.src);
            $("#" + imageCroppie.txtImage).val(result.src);
            imgData = result.src;
        }
        $("#" + imageCroppie.idDom).html("");
        $("#" + imageCroppie.idDom).html(html);
        $("#" + imageCroppie.idDom).show();
        $("#" + imageCroppie.popupCropImage).css('display', 'none');
        $('#' + imageCroppie.divImg).croppie('destroy');
        $("#" + imageCroppie.imgContainerDiv).html("");
        $('#' + imageCroppie.idInputFile).val('');
    }
};
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CROP IMAGE CUSTOMIZE SIZE">
var initCroppieBanner = function (imageCroppie) {
    previewFile(document.getElementById(imageCroppie.idInputFile).files, "100x60");
    $("#size_val").val("100x60");
    document.getElementById("size_width").readOnly = true;
    document.getElementById("size_height").readOnly = true;
    $('#size_width').val(100);
    $('#size_height').val(60);

    $("#cbo_size").val($("#cbo_size option:first").val());
    $('#cbo_size').unbind();
    $('#cbo_size').on("change", function (e) {
        if ($('#cbo_size').val() === "custom") {
            $('#banner_container_div').html("");
            previewFile(document.getElementById(imageCroppie.idInputFile).files, "120x120");
            document.getElementById("size_width").readOnly = false;
            document.getElementById("size_height").readOnly = false;
            $('#size_width').val(120);
            $('#size_height').val(120);
            $(".cr-viewport").width(120);
            $(".cr-viewport").height(120);
            $("#size_val").val("120x120");

            $("#size_width").on('change', function (e) {
                var wi = document.getElementById("size_width").value;
                var he = document.getElementById("size_height").value;
                $(".cr-viewport").width(wi);
                $(".cr-viewport").height(he);
                $("#size_val").val(wi + "x" + he);
            });

            $("#size_height").on('change', function () {
                var wi = document.getElementById("size_width").value;
                var he = document.getElementById("size_height").value;
                $(".cr-viewport").width(wi);
                $(".cr-viewport").height(he);
                $("#size_val").val(wi + "x" + he);
            });
        } else {
            $('#banner_container_div').html("");
            $("#size_val").val($('#cbo_size').val());
            document.getElementById("size_width").readOnly = true;
            document.getElementById("size_height").readOnly = true;
            $('#size_width').val($('#cbo_size').val().split('x')[0]);
            $('#size_height').val($('#cbo_size').val().split('x')[1]);
            previewFile(document.getElementById(imageCroppie.idInputFile).files, $('#cbo_size').val());
        }
    });

    $("#btn_cancel").unbind();
    $("#btn_cancel").click(function () {
        $("#pop_crop_banner").modal('hide');
        $('#div_banner').croppie('destroy');
        $("#banner_container_div").html("");
        $(".fileUpload").find("span.help-block").remove();
    });

    function previewFile(files, size) {
        var file = files[0];
        var reader = new FileReader();

        reader.addEventListener("load", function () {
            var img = document.createElement("img");
            img.src = reader.result; 
            img.crossOrigin = "Anonymous";
            img.id = "div_banner";

            $("#banner_container_div").append(img);
            $("#div_banner").load(function () {
                $uploadCrop = $('#div_banner').croppie({
                    viewport: {
                        width: size.split('x')[0],
                        height: size.split('x')[1],
                        type: 'square'
                    },
                    enforceBoundary: false,
                    showZoomer: true
                });
                $("#pop_crop_banner").modal('show');
            });
        }, false);
        if (file) {
            reader.readAsDataURL(file);
        }
    }

    $("#btn_crop").unbind();
    $("#btn_crop").click(function () {
        var size = $("#size_val").val();
        $("#" + imageCroppie.idSizeLogo).removeClass("display--none");
        $("#" + imageCroppie.idImageWith).val(size.split('x')[0]);
        $("#" + imageCroppie.idImageHeight).val(size.split('x')[1]);
        $uploadCrop.croppie('result', {
            type: 'base64',
            size: {width: size.split('x')[0], height: size.split('x')[1]},
            format: 'png'
        }).then(function (resp) {
            popupResult({
                src: resp
            });
        });
    });

    function popupResult(result) {
        if (result.src) {
            var name = document.getElementById(imageCroppie.idInputFile).files[0].name.split('.')[0] + "-" + $("#size_val").val();
            $("#" + imageCroppie.idViewElement).attr("src", result.src);
            $("#" + imageCroppie.txtImage).val(result.src);
            $("#" + imageCroppie.idSrcElement).val(result.src);
            $("#" + imageCroppie.idFileNameElement).val(name);
        }
        $("#pop_crop_banner").modal('hide');
        $("#" + imageCroppie.idDom).show();
        $("#" + imageCroppie.uploadImage).hide();
        $('#div_banner').croppie('destroy');
        $("#banner_container_div").html("");
        $(".fileUpload").find("span.help-block").remove();
    }
};
//</editor-fold>


