//收益率走势图 (日线)

$(function () {
    $.qicLoading({
        target: 'body',
        text: "努力加载中...",
        modal: false,
        width: 220,
        top: '29%',
        left:'28%',
        postion: "absolute",
        zIndex: 2000
    });

    /*

        var minCapital = (1-daymaxYield/100)*dayInitCapital;//最小回测资金
        var maxCapital =(1+daymaxYield/100)*dayInitCapital;//最大回测资金
        var returnResult =0;


        if(minCapital>0){
            if(minCapital>day_str_average_fund) {
                if(daymaxYield<=60){
                    returnResult = 0;
                } else{
                    returnResult = minCapital- day_str_average_fund;
                }
            }else {
                returnResult = minCapital;
            }

        }else if(minCapital<0){
            if((-minCapital)>day_str_average_fund) {
                if(150<=daymaxYield &&daymaxYield<=300){
                    returnResult = dayInitCapital-day_str_average_fund;
                } else{
                    returnResult =  dayInitCapital;
                }
            } else{
                returnResult = 0//day_str_average_fund+minCapital;
            }
        }else{
            returnResult = 0;

        }
    */

//日线图
    window.dailyChart = new Highcharts.Chart({
        chart: {
            renderTo: 'day_trategy_container',
            plotBorderWidth: 1,
            type: 'areaspline'
          //  spacingRight: 20

        },
        title: {
            text: null
        },
        credits:{
            enabled:false,
            position: {
                align: 'left',
                x: 350,
                verticalAlign: 'bottom'
            }
        },
        legend: {
            enabled:false

        },

        xAxis: {
            type: 'datetime',
            min:dayminDate,  //0表示1月 以此类推
            max:daymaxDate,
            tickInterval :20*24*3600*1000,//X轴点间隔
           // showFirstLabel: false,
            labels: {
                //align: 'left',
                formatter: function() {
                    return Highcharts.dateFormat('%Y-%m-%d', this.value);
                }
            }
        },
        yAxis:  [
            {
                lineWidth:1,
                //  gridLineColor: 'red',
                //endOnTick : true,
                gridLineDashStyle: 'shortdash',
                max:daymaxYield,
                min:-daymaxYield,
                opposite: true,
                tickInterval:day_pre_Yield,
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        if (this.value >0) {
                            return '<span style="fill: red;">' + this.value  + '%</span>';
                        }
                        else if(this.value <0){
                                return '<span style="fill: green;">' + -(this.value)+'%</span>';
                            }
                        else {
                                return this.value+"%";
                            }
                    }
                }
            },
            {

                lineWidth:1,
               // gridLineColor: '#41aef3',
                gridLineDashStyle: 'shortdash',
               // max:maxCapital*0.6,
               // min:minCapital,
                //tickInterval:day_str_average_fund,
                //tickInterval:321,
                max:daymaxYield,
                min:-daymaxYield,
                tickInterval:day_pre_Yield,
                title: {
                    text:null
                },
                labels: {
                    formatter: function() {
                       // return (this.value+returnResult).toFixed(0) ;
                        return  (this.value/100*dayInitCapital+dayInitCapital).toFixed(0)

                    }
                }
            }
        ],
        tooltip: {
            formatter: function() {
                return   this.series.name+'<br/><strong>日期:</strong>'+
                    Highcharts.dateFormat('%Y%m%d', this.x) +'<strong>收益率:</strong> '+ this.y+'%' ;
            }
        },
        plotOptions: {
            areaspline: {
                fillColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                    stops: [
                       // [0, '#41aef3'],
                        [0, '#93caed'],
                        [1, '#E1F1FA']
                        //[1, 'rgba(2,0,0,0)']
                    ]
                },
                lineWidth: 2,
                marker: {
                    enabled: false,
                    //symbol: 'circle',
                    states: {
                        hover: {
                            enabled: true,
                            radius: 5
                        }
                    }
                },
                shadow: false,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            },
            series: {
                lineColor: '#41aef3'
            }

        },
        series: daystrategys

    });
});




//收益率走势图 (周线)
$(function () {


   /* var minCapital = (1-weekmaxYield/100)*weekInitCapital;//最小回测资金
    var maxCapital =(1+weekmaxYield/100)*weekInitCapital;//最大回测资金
    var returnResult =0;


    if(minCapital>0){
        if(minCapital>week_str_average_fund) {
            if(weekmaxYield<=60){
                returnResult = 0;
            } else{
                returnResult = minCapital- week_str_average_fund;
            }
        }else {
            returnResult = minCapital;
        }

    }else if(minCapital<0){
        if((-minCapital)>week_str_average_fund) {
            if(150<=weekmaxYield &&weekmaxYield<=300){
                returnResult = weekInitCapital-week_str_average_fund;
            } else{
                returnResult =  weekInitCapital;
            }
        } else{
            returnResult = 0//week_str_average_fund+minCapital;
        }
    }else{
        returnResult = 0;

    }*/

    //周线图
    window.weeklyChart = new Highcharts.Chart({
        chart: {
            renderTo: 'week_trategy_container',
            plotBorderWidth: 1,
            type: 'areaspline'
        },
        title: {
            text: null
        },
        credits:{
            enabled:false,
            position: {
                align: 'left',
                x: 350,
                verticalAlign: 'bottom'
            }
        },
        legend: {
            enabled:false

        },

        xAxis: {
            type: 'datetime',
            min:weekminDate,  //0表示1月 以此类推
            max:weekmaxDate,
            tickInterval :20*24*3600*1000,//X轴点间隔
            labels: {
                //align: 'left',
                formatter: function() {
                    return Highcharts.dateFormat('%Y-%m-%d', this.value);
                }
            }
        },
        yAxis:  [
            {
                lineWidth:1,
                //  gridLineColor: 'red',
                //endOnTick : true,
                gridLineDashStyle: 'shortdash',
                max:weekmaxYield,
                min:-weekmaxYield,
                opposite: true,
                tickInterval:week_pre_Yield,
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        if (this.value >0) {
                            return '<span style="fill: red;">' + this.value + '%</span>';
                        }
                        else if(this.value <0){
                                return '<span style="fill: green;">' + -(this.value) + '%</span>';
                            }
                        else {
                                return this.value+"%";
                            }
                    }
                }
            },
            {
                lineWidth:1,
                // gridLineColor: 'red',
                gridLineDashStyle: 'shortdash',

               /* max:maxCapital*0.7,
                min:minCapital,
                tickInterval:week_str_average_fund,
               */

                max:weekmaxYield,
                min:-weekmaxYield,
                tickInterval:week_pre_Yield,
                title: {
                    text:null
                },
                labels: {
                    formatter: function() {
                        //return (this.value+returnResult).toFixed(0);
                        return (this.value/100*weekInitCapital+weekInitCapital).toFixed(0)
                    }
                }
            }
        ],
        tooltip: {
            formatter: function() {
                return   this.series.name+'<br/><strong>日期:</strong>'+
                    Highcharts.dateFormat('%Y%m%d', this.x) +'<strong>收益率:</strong> '+ this.y+'%' ;
            }
        },
        plotOptions: {
            areaspline: {
                fillColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                    stops: [
                        // [0, '#41aef3'],
                        [0, '#93caed'],
                        [1, '#E1F1FA']
                        //[1, 'rgba(2,0,0,0)']
                    ]
                },
                lineWidth: 2,
                marker: {
                    enabled: false,
                    //symbol: 'circle',
                    states: {
                        hover: {
                            enabled: true,
                            radius: 5
                        }
                    }
                },
                shadow: false,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            },
            series: {
                lineColor: '#41aef3'
            }

        },
        series: weekstrategys

    });
    $.qicLoading({remove: true});//移除loading。。。
});