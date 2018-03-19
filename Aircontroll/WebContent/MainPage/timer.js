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