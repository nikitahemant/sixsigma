var DATA = {

    /*return google visualization data*/
    getvisualizationData: function (jsonData) {

        var point1, dataArray = [],
                data = new google.visualization.DataTable();

        data.addColumn('string', 'Category');
        data.addColumn('number', 'Industry');
        data.addColumn('number', 'Client');
        data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});

        /* for loop code for changing inputdata to 'data' of type google.visualization.DataTable*/
        var sumInd, sumOrg=[]
        $.each(jsonData, function (i, obj) {
            sumInd += obj.indAmount;
            sumOrg += obj.orgAmount;
        });
        alert(sumInd);
        data.addRows(dataArray);
        return data;
    },
    getvisualizationData2: function (jsonData) {

        var point1, dataArray = [],
                data = new google.visualization.DataTable();

        data.addColumn('string', 'Category');
        data.addColumn('number', 'Industry');
        data.addColumn('number', 'Client');
        data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});
        data.addColumn({type: 'string', role: 'style'});
        /* for loop code for changing inputdata to 'data' of type google.visualization.DataTable*/
        $.each(jsonData, function (i, obj) {
            point1 = obj.category + " Client: " +obj.orgAmount;
            dataArray.push([obj.category, obj.indAmount,obj.orgAmount, DATA.returnTooltip(point1),'color: #C0392B']);
        });
        data.addRows(dataArray);
        return data;
    },

    /*return options for bar chart: these options are for various configuration of chart*/
    getOptionForBarchart: function () {

        var options = {
            title: 'Rebate Comparison',
            chartArea: {width: '50%'},
            hAxis: {
                title: 'Rebate Amount',
                minValue: 0
            },
            vAxis: {
                title: 'Type'
            }
        };
        return   options;
    },
    getOptionForBarchart2: function () {

        var options = {
            title: 'Industry Benchmark Rebate Comparison',
            width: 1300,
            height: 500,
            bar: {groupWidth: "75%"},
            chartArea: {width: '70%', height:'100%'},
            hAxis: {
                title: 'Rebate Value',
                minValue: 0
            },
            vAxis: {
                title: 'MCC Categories'
            }
        };
        return   options;
    },
    getOptionForBarchart3: function () {

        var options = {
            title: 'Monthly Spending Prediction',
            curveType: 'function',
            legend: {position: 'bottom'}
        };
        return   options;
    },
    /*Draws a Bar chart*/
    drawBarChart: function (inputdata) {

        var barOptions = DATA.getOptionForBarchart(),
                data = DATA.getvisualizationData(inputdata),
                chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));

        chart.draw(data, barOptions);
        /*for redrawing the bar chart on window resize*/
        $(window).resize(function () {
            chart.draw(data, barOptions);
        });
    },
    drawBarChart2: function (inputdata) {

        var barOptions = 
                DATA.getOptionForBarchart2(),
                data = DATA.getvisualizationData2(inputdata),
                chart = new google.visualization.BarChart(document.getElementById('chart_div2'));

        chart.draw(data, barOptions);
        /*for redrawing the bar chart on window resize*/
        $(window).resize(function () {
            chart.draw(data, barOptions);
        });
    },
    drawBarChart3: function (inputdata) {

        var barOptions = DATA.getOptionForBarchart3(),
                data = google.visualization.arrayToDataTable([
                    ['Month', 'Actual Spending', 'Prediction'],
                    ['Oct-15', 33474.04, null],
                    ['Nov-15', 22580.02, null],
                    ['Dec-15', 22480.68, null],
                    ['Jan-16', 11099.32, null],
                    ['Feb-16', 10742.97, null],
                    ['Mar-16', 14825.37, null],
                    ['Apr-16', 17801.96, null],
                    ['May-16', 22058.47, null],
                    ['Jun-16', 11794.89, null],
                    ['Jul-16', 11051.35, null],
                    ['Aug-16', 18136.83, null],
                    ['Sep-16', null, 20000],
                    ['Oct-16', null, 22000],
                ]);

        var chart = new google.visualization.LineChart(document.getElementById('chart_div3'));

        chart.draw(data, barOptions);
        /*for redrawing the bar chart on window resize*/
        $(window).resize(function () {
            chart.draw(data, barOptions);
        });
    },
    /* Returns a custom HTML tooltip for Visualization chart*/
    returnTooltip: function (dataPoint1) {
        return dataPoint1;
    },
    createPredictionChart: function () {
        DATA.drawBarChart3();
    },
    /*Makes ajax call to servlet and download data */
    getTransactionData: function () {

        $.ajax({
            url: "intelreports",
            dataType: "JSON",
            success: function (data) {
                DATA.drawBarChart2(data);
            }
        });
    }
};

google.load("visualization", "1", {packages: ["corechart"]});

$(document).ready(function () {
    DATA.createPredictionChart();
    DATA.getTransactionData();
});
