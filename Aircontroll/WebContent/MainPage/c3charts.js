function drawC3Chart(arg, name) {
	var chart = c3.generate({
		bindto : name,
		data :{ 
			json : arg, 
			x:'date',
			xFormat: '%Y-%m-%d %H:%M:%S',
			type: 'line',
			axes: {
				'온도':'y',
				'습도':'y2'
			}
		},
		axis : {
			x: {
				type: 'timeseries',
				tick:{
					format: '%Y-%m-%d %H:%M:%S',
					rotate: 46,
					fit: true
				}
			},
			y : {
				show: true,
				label: {
			          text: '온도',
			          position: 'outer-middle'
		        }
			},
			y2:{
				show: true,
				label: {
					text: '습도',
					position: 'outer-middle'
		        }
			}
		},
		grid : {
			x : {
				show : true,
			},
			y : {
				show : true
			}
		}
	});
}