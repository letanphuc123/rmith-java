/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: VI (Vietnamese; Tiếng Việt)
 */
$.extend( $.validator.messages, {
	required: "Hãy nhập giá trị này.",
	remote: "Hãy sửa cho đúng.",
	email: "Hãy nhập email hợp lệ.",
	url: "Hãy nhập URL hợp lệ.",
	date: "Hãy nhập ngày.",
	dateISO: "Hãy nhập ngày (ISO).",
	number: "Hãy nhập số.",
	digits: "Hãy nhập chữ số.",
	creditcard: "Hãy nhập số thẻ tín dụng.",
	equalTo: "Hãy nhập lại giá trị tương tự một lần nữa.",
	extension: "Phần mở rộng không đúng.",
        pwcheck: "Mật khẩu phải chứa tối thiểu một ký tự chữ thường, một ký tự chữ hoa, một chữ số và một ký tự đặc biệt..",
        messageValid: "Hãy nhập giá trị này.",
        spaceCheck: "Khoảng trắng phải được thay thế bằng dấu gạch dưới",
        alpha_numeric: "Chỉ được nhập ký tự chữ và số và dấu ('-')",
        clientNameTag: "Cần có thẻ tên khách hàng {clientName}",
        reviewLinkTag: "Thẻ liên kết đánh giá {reviewLink} là bắt buộc",
        contactMessageValid: "Phần tin nhắn không được rỗng",
        planCodeRegex: "Chỉ chấp nhận chữ cái, dấu trừ ('-') và dấu gạch dưới ('_'), không chấp nhận chứa khoảng trắng.",
	maxlength: $.validator.format( "Hãy nhập từ {0} kí tự trở xuống." ),
	minlength: $.validator.format( "Hãy nhập từ {0} kí tự trở lên." ),
	rangelength: $.validator.format( "Hãy nhập từ {0} đến {1} kí tự." ),
	range: $.validator.format( "Hãy nhập từ {0} đến {1}." ),
	max: $.validator.format( "Hãy nhập từ {0} trở xuống." ),
	min: $.validator.format( "Hãy nhập từ {0} trở lên." )
} );