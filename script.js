google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

$('form').jsonForm({
      schema: {
        chamberDiameter: {
          type: 'number',
          title: 'Średnica komory',
          default :75
        },
        chamberLength: {
          type: 'number',
          title: 'Długość komory',
          default : 470
        },
        grainOutsideDiameter: {
          type: 'number',
          title: 'Średnica ziarna',
          default : 69
        },
        grainLength: {
          type: 'number',
          title: 'Długość ziarna',
          default : 115
        },
        grainCanalDiameter: {
          type: 'number',
          title: 'Średnica kanału ziarna',
          default : 20
        },
        numberOfGrains: {
          type: 'number',
          title: 'Ilość zarien',
          default : 4
        },
        KN: {
          type: 'number',
          title: 'Zacisk początkowy',
          default : 240
        },
        isOutsideExposed: {
          type: 'number',
          title: 'Odsłonięta zewnętrzna strona',
          default : 0
        },
        isCanalExposed: {
          type: 'number',
          title: 'Odsłonięty kanał',
          default : 1
        },
        isEndExposed: {
          type: 'number',
          title: 'Odsłoniete końce',
          default : 1
        },
        nozzleErosion:{
          type: 'number',
          title: 'Zacisk początkowy',
          default : 0
        }
      },
      onSubmit: function (errors, values) {
        if (errors) {
            console.log(errors)
        } else {
            var dataForm = JSON.stringify(values);
 
            $.ajax({
                url: "http://localhost:8080/chamber/calculateKn",
                type: "POST",
                data: dataForm,
                contentType: "application/json; charset=utf-8"
            }).done(function(data){
                localStorage.setItem('firstData', dataForm);
                drawChart(data);
            });
        }
      }
    });
   
        var chart;
 
 
 
 
        function drawChart(response) {
 
               data = new google.visualization.DataTable();
               data.addColumn('number', 'Wypalanie paliwa');
               data.addColumn('number',  'Grubość śćianki');
               data.addColumn('number', 'Kn');
 
            if(response != null){
                for(i = 0; i < response.length-1; i++){
                    data.addRow([response[i].x,response[i].tweb,response[i].kn]);
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
                            maxValue: 350,
                            title: "Zacisk dyszy",
                                 titleTextStyle:{
                                 color: '#000000',
                                 fontSize: 21}},
                        1: {logScale: false,
                            maxValue: 29,
                            title: "Grubość ścianki [mm]",
                                 titleTextStyle:{
                                 color: '#000000',
                                 fontSize: 21}}
                        },
                hAxes: {0 : {title: "Wypalenie paliwa [mm]",
                             titleTextStyle:{
                                    color: '#000000',
                                    fontSize: 21}}},
                series:{
                        0:{targetAxisIndex:1, axis: 'temp'},
                        1:{targetAxisIndex:0},
                        2:{targetAxisIndex:1}},
                backgroundColor: '#fafafa',      
            }
 
            chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
 
            chart.draw(data, options);
        }
