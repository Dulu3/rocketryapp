$('form').jsonForm({
      schema: {
        AeAt: {
          type: 'number',
          title: 'Stosunek pól powierzchni krytycznej i wylotowej dyszy',
          default : 9
        },
		nozzlePerformance: {
          type: 'number',
          title: 'Wydajność dyszy',
          default : 0.75
        }
      },
      onSubmit: function (errors, values)
      {
        if (errors)
        {
            console.log(errors)
        } else
         {
              var beforePage =  JSON.parse(localStorage.getItem('secondData'));
              console.log(beforePage);
              var dataForm = JSON.stringify(jQuery.extend(beforePage, values));
              console.log(dataForm);
              $.ajax({
                  url: "http://localhost:8080/api/calculateThrust",
                  type: "POST",
                  data: dataForm,
                  contentType: "application/json; charset=utf-8"
              }).done(function(data){
                  drawChart(data);
              });
        }
    }});
    

        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        var chart;




        function drawChart(response) {

        data = new google.visualization.DataTable();
                      data.addColumn('number', 'Czas');
                      data.addColumn('number', 'Ciąg');


            if(response != null){
              for(i = 0; i < response.length; i++){
                  data.addRow([response[i].t,response[i].Fn]);
               }
            } else {

            }

            var options = {
                curveType: 'function',
                legend: { position: 'bottom' },
                animation:{
                    startup: true,
                    duration: 400,
                    easing: 'in'
                 },
                vAxes: {0: {logScale: false, minValue: 1, maxValue: 3,
							title: "Ciąg [N]",
                                    titleTextStyle:{
                                    color: '#000000',
                                    fontSize: 21}},
                        1: {logScale: false, maxValue: 29}},
			 hAxes: {0 : {title: "Czas [s]",
                        titleTextStyle:{
                        color: '#000000',
                        fontSize: 21}}}
            }

            chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

            chart.draw(data, options);
        }