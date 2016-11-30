var DATA = {
	
/*return google visualization data*/
	getvisualizationData : function(jsonData){
	
	 var   point1, point2, dataArray = [],
	 
		   data = new google.visualization.DataTable();
		   
	       data.addColumn('string', 'Nombre');
	      
	       data.addColumn('number', 'Matematica');
	      
	       data.addColumn({type: 'string',role: 'tooltip','p': {'html': true}});
	       
	       data.addColumn('number', 'Fisica');
	       
	      data.addColumn({type: 'string',role: 'tooltip','p': {'html': true}});
	      
	      /* for loop code for changing inputdata to 'data' of type google.visualization.DataTable*/
	      $.each(jsonData, function(i,obj){
	    	  
	    	  point1 ="Matematica : "+ obj.matematica +"";
	    	  
	    	  point2 ="Fisica : "+ obj.fisica +"";
	    	  
	    	  dataArray.push([obj.nombre,obj.matematica,DATA.returnTooltip(point1,point2),obj.fisica,DATA.returnTooltip(point1,point2)]);
	      });
	      
	     data.addRows(dataArray);
	     
	     return data;
	},
	/*return options for bar chart: these options are for various configuration of chart*/
	getOptionForBarchart : function(){
		
		  var options = {
		  		        title: 'Calificaciones'
	     		 };
		return   options;		 
		},
	/*Draws a Bar chart*/	
	drawBarChart : function (inputdata) {

		 var  barOptions = DATA.getOptionForBarchart(),
		
			  data = DATA.getvisualizationData(inputdata),
			  
			  chart = new google.visualization.PieChart(document.getElementById('chart-pie'));
			  
			  chart.draw(data, barOptions);
			  /*for redrawing the bar chart on window resize*/
		    $(window).resize(function () {
		    	
		        chart.draw(data, barOptions);
		    });
	 },
	/* Returns a custom HTML tooltip for Visualization chart*/
	 returnTooltip : function(dataPoint1,dataPoint2){
	   
		 return "<div style='height:30px;width:150px;font:12px,roboto;padding:15px 5px 5px 5px;border-radius:3px;'>"+
				 "<span style='color:#68130E;font:12px,roboto;padding-right:20px;'>"+dataPoint1+"</span>"+
				 "<span style='color:#c65533;font:12px,roboto;'>"+dataPoint2+"</span></div>";
	 },
	/*Makes ajax call to servlet and download data */
	getStudentData : function(){
		
			$.ajax({
			
				url: "EstudianteJsonDataServlet",
				
				dataType: "JSON",
				
				success: function(data){
	
					DATA.drawBarChart(data);
				}
			});
	}
};	

google.load("visualization", "1", {packages:["corechart"]});
	
$(document).ready(function(){
	
	DATA.getStudentData();
});
