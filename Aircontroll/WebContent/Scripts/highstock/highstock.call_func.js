var xhr = new XMLHttpRequest();
var minTemp=0, maxTemp=0;
var minHumi=0, maxHumi=0;

function dateFormat(x){
	if(x.toString().length<2)
		x='0'+x;
	return x;
}

var element1 = document.getElementById('temp');
var element2 = document.getElementById('humi');
var element3 = document.getElementById('time');
//var element2 = $(".text_time");
//var element3 = $(".state");

var shown = true;

function toggle() {
   if(shown) {
	   element1.style.color='white';
	   element2.style.color='white';
	   element3.style.background='white';
       shown = false;

   } else {
	   element1.style.color='black';
	   element2.style.color='black';
	   element3.style.background='black';
       shown = true;
   }
}

Highcharts.setOptions({
	global : {
		useUTC : false
	}
});

function setTempState(x){
	var state='bad:(';
	var color='rgb(222,12,0)';
	if(x>=15 && x<=20){
		state='good:)';
		color='#8bbc21';
	}
	else if(x>=10 && x<=25){
		state='normal:|';
		color='#f28f43';
	}
	var feel=document.getElementById('feel');
	feel.innerHTML=state;
	feel.style.background=color;
}

// Create the temperature chart
var humi_chart=Highcharts.stockChart('chart_humi', {
	chart : {
		type: 'spline',
		events : {
			load : function() {
			}
		}
	},
	colors: ['#f28f43'],
	rangeSelector : {
		buttons : [ {
			count : 30,
			type : 'minute',
			text : '30분'
		}, {
			count : 60,
			type : 'minute',
			text : '1시간'
		},
		 {
			count : 180,
			type : 'minute',
			text : '3시간'
		},
		{
			count : 360,
			type : 'minute',
			text : '6시간'
		}, {
			type : 'all',
			text : '전체'
		} ],
		inputEnabled : true,
		selected : 0
	},

	/*title : {
		text : 'DHT11센서에서 측정된 습도 그래프'
	},*/
	exporting : {
		enabled : true
	},

	series : [ {
		name : '습도',
		data : (function() {
			if (dataset != null) {
				var data = [], x=dataset['date'], y2=dataset['humi'];
				minHumi=y2[0];
				maxHumi=y2[0];
				for (var i = 0; i < x.length; i++){
					data.push([ new Date(x[i]).getTime(), y2[i] ]);
					if(y2[i]>maxHumi){
						maxHumi=y2[i];
						console.log(maxHumi);
					}
					if(y2[i]<minHumi){
						minHumi=y2[i];
					}
				}
			}
			return data;
		}())
	} ],
	xAxis:{
		minRange: 1440
	},
	yAxis: {
        title: {
            text: 'Humidity'
        },
        plotBands: [{
            from: 31,
            to: 69,
            color: 'rgba(68, 170, 213, 0.2)',
            label: {
                text: '실내 적정 습도',
                align: 'center',
                style: {
                    color: 'gray',
                    fontWeight: 'bold'
                }
            }
        }],
        plotLines: [{
            value: minHumi,
            color: 'orange',
            dashStyle: 'shortdash',
            width: 2,
            id: 'plotline-min',
            label: {
                text: '최저습도',
                style: {
                    color: 'orange',
                    fontWeight: 'bold'
                }
            }
        }, {
            value: maxHumi,
            color: 'orange',
            dashStyle: 'shortdash',
            width: 2,
            id: 'plotline-max',
            label: {
                text: '최고습도',
                style: {
                    color: 'orange',
                    fontWeight: 'bold'
                }
            }
        }]
    }
});

Highcharts.stockChart('chart_temp', {
	chart : {
		type: 'spline',
		events : {
			load : function() {
				var series = this.series;
				xhr.onreadystatechange = function() {
					try {
						if (xhr.readyState == 4 && xhr.status == 200) {
							var x,x1, y1, y2;
							var dto = JSON.parse(xhr.responseText, function(k,
									v) {
								if (k == 'date'){
									x = new Date(v);
									x1=x.getTime();
//									console.log(x);
								}
								else if (k == 'temp'){
									y1 = parseInt(v);
//									console.log('y1='+y1);
									if(y1>maxTemp){
										maxTemp=y1;
										this.yAxis[0].removePlotLine('plotline-max');
										this.yAxis[0].addPlotLine({
								            value: maxTemp,
								            color: 'green',
								            width: 2,
								            id: 'plotline-max'
								        });
									}
									else if(y1<minTemp){
										minTemp=y1;
										this.yAxis[0].removePlotLine('plotline-min');
										this.yAxis[0].addPlotLine({
								            value: minTemp,
								            color: 'green',
								            width: 2,
								            id: 'plotline-min'
								        });
									}
								}
								else if(k == 'humi'){
									y2 = parseInt(v);
//									console.log('y2='+y2);
									if(y2>maxHumi){
										maxHumi=y2;
										humi_chart.yAxis[0].removePlotLine('plotline-max');
										humi_chart.yAxis[0].addPlotLine({
								            value: maxHumi,
								            color: 'orange',
								            width: 2,
								            id: 'plotline-max'
								        });
									}
									else if(y2<minHumi){
										minHumi=y2;
										humi_chart.yAxis[0].removePlotLine('plotline-min');
										humi_chart.yAxis[0].addPlotLine({
								            value: minHumi,
								            color: 'orange',
								            width: 2,
								            id: 'plotline-min'
								        });
									}
								}
							});
							series[0].addPoint([x1, y1 ], true, true);
							humi_chart.series[0].addPoint([x1, y2 ], true, true);
							setTempState(y1);
							var mon=dateFormat(x.getMonth()+1);
							var day=dateFormat(x.getDate());
							var hour=dateFormat(x.getHours());
							var min=dateFormat(x.getMinutes());
							var date=mon+'-'+day+' '+hour+':'+min;
							/*toggle();
							setTimeout(toggle, 500);*/
							document.getElementById('time').innerHTML=date
							document.getElementById('temp').innerHTML=y1+'°C';
							document.getElementById('humi').innerHTML=y2+'%rh';
							
						}
					} 
					catch (e) {
						alert(e);
					}
				}
				// set up the updating of the chart each second
				setInterval(function() {
					xhr.open('GET', 'getData.Action', true);
					xhr.send(null);
				}, 60000);
			}
		}
	},
	colors: ['#8bbc21','#f28f43'],
	rangeSelector : {
		buttons : [ {
			count : 30,
			type : 'minute',
			text : '30분'
		}, {
			count : 60,
			type : 'minute',
			text : '1시간'
		},
		 {
			count : 180,
			type : 'minute',
			text : '3시간'
		},
		{
			count : 360,
			type : 'minute',
			text : '6시간'
		}, {
			type : 'all',
			text : '전체'
		} ],
		inputEnabled : true,
		selected : 0
	},

	/*title : {
		text : 'DHT11센서에서 측정된 온도 그래프'
	},*/
	exporting : {
		enabled : true
	},

	series : [ {
		name : '온도',
		data : (function() {
			// generate an array of random data
			// var data = [], time = (new Date()).getTime(), i;
			//
			// for (i = -999; i <= 0; i += 1) {
			// data.push([ time + i * 1000,
			// Math.round(Math.random() * 100) ]);
			// }
			if (dataset != null) {
				var data = [], x=dataset['date'], y=dataset['temp'];
				minTemp=y[0];
				maxTemp=y[0];
				for (var i = 0; i < x.length; i++){
					data.push([ new Date(x[i]).getTime(), y[i] ]);
					if(y[i]>maxTemp){
						maxTemp=y[i];
					}
					if(y[i]<minTemp){
						minTemp=y[i];
					}
				}
				setTempState(y[x.length-1]);
			}
			return data;
		}())
	}],
	xAxis:{
		minRange: 1440
	},
	yAxis: {
        title: {
            text: 'Temperature'
        },
        plotBands: [{
            from: 15.6,
            to: 20,
            color: 'rgba(68, 170, 213, 0.2)',
            label: {
                text: '실내 적정 온도',
                align: 'center',
                style: {
                    color: 'gray',
                    fontWeight: 'bold'
                }
            }
        }],
        plotLines: [{
            value: minTemp,
            color: 'green',
            dashStyle: 'shortdash',
            width: 2,
            id: 'plotline-min',
            label: {
                text: '최저온도',
                style: {
                    color: 'green',
                    fontWeight: 'bold'
                }
            }
        }, {
            value: maxTemp,
            color: 'green',
            dashStyle: 'shortdash',
            width: 2,
            id: 'plotline-max',
            label: {
                text: '최고온도',
                style: {
                    color: 'green',
                    fontWeight: 'bold'
                }
            }
        }]
    }
});