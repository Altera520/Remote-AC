var spinner;
var opts = {
	lines : 11, // The number of lines to draw
	length : 17, // The length of each line
	width : 7, // The line thickness
	radius : 23, // The radius of the inner circle
	scale : 0.75, // Scales overall size of the spinner
	corners : 0.8, // Corner roundness (0..1)
	color : '#333', // CSS color or array of colors
	fadeColor : 'transparent', // CSS color or array of colors
	opacity : 0, // Opacity of the lines
	rotate : 0, // The rotation offset
	direction : 1, // 1: clockwise, -1: counterclockwise
	speed : 1, // Rounds per second
	trail : 83, // Afterglow percentage
	fps : 20, // Frames per second when using setTimeout() as a fallback in IE
				// 9
	zIndex : 2e9, // The z-index (defaults to 2000000000)
	className : 'spinner', // The CSS class to assign to the spinner
	top : '50%', // Top position relative to parent
	left : '50%', // Left position relative to parent
	shadow : 'none', // Box-shadow for the lines
	position : 'absolute' // Element positioning
};
function changePage(urlLocation) {
//	$.ajax({
//		type : "GET",
//		url : urlLocation,
//		dataType : "html",
//		beforeSend : function() {
//			jQuery(function() {
//				spinner = new Spinner(opts).spin().el;
//				jQuery(document.body).append(spinner);
//			});
//		},
//		error : function() {
//			alert('통신실패!!');
//			stopSpin();
//		},
//		success : function(data) {
//			// $('#main').html(data);
//			stopSpin();
//			$('#main').html(data);
//			// jQuery(document.body).remove(spinner);
//		}
//	});
	$("#main").load(urlLocation);
}

function stopSpin(){
	setTimeout(function(){
		$(spinner).remove();
	},1000);
}
function loadDate() {
	var clock = document.getElementById('clock');
	var date = new Date();
	var hour = date.getHours();
	var min = date.getMinutes();
	var sec = date.getSeconds();
	var option = 'AM';
	if (hour >= 12) {
		option = 'PM';
		hour -= 12;
	}
	clock.innerHTML = fillZero(hour, 2) + ":" + fillZero(min, 2) + ":"
			+ fillZero(sec, 2) + ' ' + option;
	setTimeout('loadDate()', 1000);
}

function fillZero(num, digit) {
	var format = '';
	num = num.toString();
	for (i = 0; i < digit - num.length; i++)
		format += '0';
	return format + num;
}

function logout(){
	location.href='logout.Action';
}