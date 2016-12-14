var DATA = {

    /*return google visualization data*/
    getvisualizationData: function (jsonData) {

        var point1, dataArray = [],
                data = new google.visualization.DataTable();
        data.addColumn('string', 'Date');
        data.addColumn('number', 'Amount');
        data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});
        /* for loop code for changing inputdata to 'data' of type google.visualization.DataTable*/
        $.each(jsonData, function (i, obj) {
            point1 = "Amount : " + obj.amount + "";
            dataArray.push([obj.date, obj.amount, DATA.returnTooltip(point1)]);
        });
        data.addRows(dataArray);
        return data;
    },

    getvisualizationData2: function (jsonData) {

        var point1, dataArray = [],
                data = new google.visualization.DataTable();

        data.addColumn('string', 'Month');

        data.addColumn('number', 'Amount');

        data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});

        /* for loop code for changing inputdata to 'data' of type google.visualization.DataTable*/
        $.each(jsonData, function (i, obj) {

            point1 = "Month: " + obj.month + " Amount : " + obj.amount + "";


            dataArray.push([obj.month, obj.amount, DATA.returnTooltip(point1)]);
        });
        data.addRows(dataArray);

        return data;
    },

    getvisualizationData3: function (jsonData) {
        var point1, dataArray = [],
                data = new google.visualization.DataTable();
        data.addColumn('string', 'Category');
        data.addColumn('number', 'Amount');
        data.addColumn({type: 'string', role: 'tooltip', 'p': {'html': true}});
        /* for loop code for changing inputdata to 'data' of type google.visualization.DataTable*/
        $.each(jsonData, function (i, obj) {
            point1 = "Category: " + obj.category + " Amount : " + obj.amount + "";
            dataArray.push([obj.category, obj.amount, DATA.returnTooltip(point1)]);
        });
        data.addRows(dataArray);
        return data;
    },

    /*return options for bar chart: these options are for various configuration of chart*/
    getOptionForBarchart: function () {

        var options = {
            title: 'Daily Spending',
            legend: 'none',
            bar: {groupWidth: '95%'},
            vAxis: {gridlines: {count: 4}}
        };
        return   options;
    },
    getOptionForBarchart2: function () {

        var options = {
            title: 'Monthly Spending ',
            legend: 'none',
            bar: {groupWidth: '95%'},
            vAxis: {gridlines: {count: 4}}
        };
        return   options;
    },
    getOptionForBarchart3: function () {

        var options = {
            title: 'Current Year Categorical Spending',
            legend: 'none',
            bar: {groupWidth: '95%'},
            vAxis: {gridlines: {count: 4}}
        };
        return   options;
    },

    /*Draws a Bar chart*/
    drawBarChart: function (inputdata) {

        var barOptions = DATA.getOptionForBarchart(),
                data = DATA.getvisualizationData(inputdata),
                chart = new google.visualization.ColumnChart(document.getElementById('day-line'));

        chart.draw(data, barOptions);
        /*for redrawing the bar chart on window resize*/
        $(window).resize(function () {

            chart.draw(data, barOptions);
        });
    },
    drawBarChart2: function (inputdata) {

        var barOptions = DATA.getOptionForBarchart2(),
                data = DATA.getvisualizationData2(inputdata),
                chart = new google.visualization.ColumnChart(document.getElementById('month-line'));

        chart.draw(data, barOptions);
        /*for redrawing the bar chart on window resize*/
        $(window).resize(function () {

            chart.draw(data, barOptions);
        });
    },
    drawBarChart3: function (inputdata) {

        var barOptions = DATA.getOptionForBarchart3(),
                data = DATA.getvisualizationData3(inputdata),
                chart = new google.visualization.BarChart(document.getElementById('cat-line'));

        chart.draw(data, barOptions);
        /*for redrawing the bar chart on window resize*/
        $(window).resize(function () {

            chart.draw(data, barOptions);
        });
    },
    /* Returns a custom HTML tooltip for Visualization chart*/
    returnTooltip: function (dataPoint1) {
        return dataPoint1;
//		 return "<div style='height:30px;width:150px;font:12px,roboto;padding:15px 5px 5px 5px;border-radius:3px;'>"+
//				 "<span style='color:#68130E;font:12px,roboto;padding-right:20px;'>"+dataPoint1+"</span>"+
//				 "</div>";
    },

    /*Makes ajax call to servlet and download data */
    getTransactionData: function () {

        $.ajax({
            url: "summary",
            dataType: "JSON",
            success: function (data) {
                var d = data.daily;
                DATA.drawBarChart(d);

                var m = data.monthly;
                DATA.drawBarChart2(m);

                var c = data.category;
                DATA.drawBarChart3(c);

            }
        });
    }
};

google.load("visualization", "1", {packages: ["corechart", "table"]});

$(document).ready(function () {

    DATA.getTransactionData();
});
