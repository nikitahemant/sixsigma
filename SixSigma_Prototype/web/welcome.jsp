<%-- 
    Document   : welcome
    Created on : Nov 2, 2016, 1:33:23 PM
    Author     : Nikita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Key2Purchase Template</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <style>
    /* Remove the navbar's default margin-bottom and rounded borders */
    .navbar {
      margin-bottom: 0;
      border-radius: 0;
      background-color: #ca0813;
      border-color: #030033;
      font-color: #FFFFFF;

    }
    
    /* Add a gray background color and some padding to the footer */
    footer {
      background-color: #ca0813;
      padding: 25px;
    }
  </style>
</head>
<body>

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Portfolio</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
        <li class="active"><a href="#">Home</a></li>
        <li><a href="#">Summary</a></li>
        <li><a href="#">About</a></li>
        <li><a href="#">Contact</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
      </ul>
    </div>
  </div>
</nav>

<div class="jumbotron">
  <div class="container text-center">
    <h1>Key2Purchase Web Portal</h1>
    <p> the best online banking experience</p>
  </div>
</div>
  
<div class="container-fluid bg-3 text-center">
  <h3>Intelligient Dashboard</h3><br>
  <div class="row">

    <div class="col-sm-4">
      <p>Industry Benchmark</p>
      <div id="chart_div"></div>
    </div>

     <div class="col-sm-4">
      <p>Monthly Spending</p>
        <div id="chart_div2"></div>
    </div>

     <div class="col-sm-4">
      <p>Daily Spending</p>
        <div id="chart_div3"></div>
    </div>    
  </div>
</div><br>



<footer class="container-fluid text-center">
  <p>Made by Six Sigma 2016</p>
</footer>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">

google.charts.load('current', {packages: ['corechart', 'bar']});
google.charts.setOnLoadCallback(drawMultSeries);


function drawMultSeries() {
  var data = google.visualization.arrayToDataTable([
    ['Category', 'Industry', 'Client'],
    ['CLOTHING/SHOES', 27135.14, 3977.11],
    ['BUSINESS/PROFESSIONAL', 24043.57, 1425.87],
    ['FURNISHINGS/APPLIANCES', 18682.86, 11808.33],
    ['ASSOCIATIONS/ORGANIZATIONS', 13713.88, 1392.22],
    ['LODGING/HOTELS', 12201.54, 2997.49]
  ]);

  var data2 = new google.visualization.DataTable();
  data2.addColumn('string', 'Month'); // Implicit domain label col.
  data2.addColumn('number', 'Spending'); // Implicit series 1 data col.
  data2.addRows([
    ['Oct-15',  33474.04],
    ['Nov-15',  22580.02],
    ['Dec-15',  22480.68],
    ['Jan-16',  11099.32],
    ['Feb-16',  10742.97],
    ['Mar-16',  14825.37],
    ['Apr-16',  17801.96],
    ['May-16',  22058.47],
    ['Jun-16',  11794.89],
    ['Jul-16',  11051.35],
    ['Aug-16',  18136.83],
  ]);



  var data3 = new google.visualization.DataTable();
  data3.addColumn('date', 'Day'); // Implicit domain label col.
  data3.addColumn('number', 'Spending'); // Implicit series 1 data col
  data3.addRows([
    [new Date('2016-08-01'),  897],
    [new Date('2016-08-02'), 6859.07],
    [new Date('2016-08-03'),21.88],
    [new Date('2016-08-04'),759.46],
    [new Date('2016-08-05'),489.81],
    [new Date('2016-08-06'),463.2],
    [new Date('2016-08-07'),528.39],
    [new Date('2016-08-08'),649.91],
    [new Date('2016-08-09'),112.3],
    [new Date('2016-08-10'), 106.57],

  ]);




  var options = {
    title: 'Company9 Quarterly Spending(2016 Q3) vs Industry Benchmark',
    chartArea: {width: '50%'},
    hAxis: {
      title: 'Total Spending',
      minValue: 0
    },
    vAxis: {
      title: 'MCC Categories'
    }
  };

  var options2 = {
    title: 'Monthly Spending Summary',

    hAxis: {
      title: 'Month',
      ticks: [0, .3, .6, .9, 1]
 
    },
    vAxis: {
      title: 'Spending'
    }
    
  };

  var options3 = {
    title: 'Dailyly Spending Summary',

    hAxis: {
      title: 'Day',
      format: 'M/d'
 
    },
    vAxis: {
      title: 'Spending'
    }
    
  };


  var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
  chart.draw(data, options);
  var chart2 = new google.visualization.ColumnChart(document.getElementById('chart_div2'));
  chart2.draw(data2, options2);
  var chart3 = new google.visualization.ColumnChart(document.getElementById('chart_div3'));
  chart3.draw(data3, options3);
}
</script>


</body>
</html>