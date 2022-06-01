$('form').jsonForm({
      schema: {
       atmosphericPressure: {
          type: 'number',
          title: 'Ciśnienie atmosferyczne',
          default : 0.101
        },
       erosiveBurningArea: {
          type: 'number',
          title: 'Próg powierzchni spalania erozyjnego',
          default : 6
        },
		burnEfficiency: {
          type: 'number',
          title: 'Efektywność spalnia',
          default : 0.95
        },
      },
      onSubmit: function (errors, values)
      {
        if (errors)
        {
            console.log(errors)
        } else
         {
              var beforePage =  JSON.parse(localStorage.getItem('firstData'));
              var dataForm = JSON.stringify(jQuery.extend(beforePage, values));
              console.log(dataForm)
              $.ajax({
                  url: "http://localhost:8080/api/calculatePressure",
                  type: "POST",
                  data: dataForm,
                  contentType: "application/json; charset=utf-8"
              }).done(function(data){
                  localStorage.setItem('secondData', dataForm);
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
                      data.addColumn('number', 'Ciśnienie');


            if(response != null){
              for(i = 0; i < response.length; i++){
                  data.addRow([response[i].t,response[i].mpagage]);
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
                vAxes: {0: {logScale: false,
                            minValue: 1,
                            maxValue: 3,
                            title: "Ciśnienie [MPa]",
                                    titleTextStyle:{
                                    color: '#000000',
                                    fontSize: 21}},
                        1: {logScale: false,
                            maxValue: 29}},
                hAxes: {0 : {title: "Czas [s]",
                        titleTextStyle:{
                        color: '#000000',
                        fontSize: 21}}},
                backgroundColor: '#fafafa'
            }

            chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

            chart.draw(data, options);
        }
