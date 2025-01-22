document.addEventListener("DOMContentLoaded", function() {

    // Funzione per caricare i prodotti e aggiornare la tabella
    async function caricaProdotti() {
        try {

            const response = await fetch("/naturlink/minerali/datatable-framments");


            if (!response.ok) {
                throw new Error('Network response was not ok');
            }


            const data = await response.json();
            console.log(data);


            const tableBody = document.querySelector("#dataTable tbody");
            tableBody.innerHTML = ""; // Svuotare il corpo della tabella


            const { minerali, tonnellateList = [], tonnellateGuadagno = [] } = data;

            console.log(tonnellateGuadagno);  // Log per controllo


            animali.forEach((animali, index) => {
                const row = document.createElement("tr");
                const {nome, tipo, quantita, prezzo,profondita,purezza} = minerali;

                row.appendChild(createTableCell(nome));
                row.appendChild(createTableCell(tipo));
                row.appendChild(createTableCell(quantita));
                row.appendChild(createTableCell(prezzo));
                row.appendChild(createTableCell(profondita));
                row.appendChild(createTableCell(purezza));



                row.appendChild(createTableCell(tonnellateList[index] || 'N/A'));  // Tonnellate
                row.appendChild(createTableCell(tonnellateGuadagno[index] || 'N/A'));  // Guadagno

                tableBody.appendChild(row);
            });


            aggiornaGrafico(tonnellateGuadagno);


        } catch (error) {
            console.error("C'è stato un problema con l'operazione fetch:", error);
        }
    }




function number_format(number, decimals, dec_point, thousands_sep) {

  number = (number + '').replace(',', '').replace(' ', '');
  var n = !isFinite(+number) ? 0 : +number,
    prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
    sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
    dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
    s = '',
    toFixedFix = function(n, prec) {
      var k = Math.pow(10, prec);
      return '' + Math.round(n * k) / k;
    };
  // Fix for IE parseFloat(0.55).toFixed(0) = 0;
  s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
  if (s[0].length > 3) {
    s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
  }
  if ((s[1] || '').length < prec) {
    s[1] = s[1] || '';
    s[1] += new Array(prec - s[1].length + 1).join('0');
  }
  return s.join(dec);
}



    function createTableCell(content) {
        const cell = document.createElement("td");
        cell.textContent = content;
        return cell;
    }

    function percentuale(tonnellateGuadagno){
        const list1 = tonnellateGuadagno.slice(0, 3);  // first element
        const list2 = tonnellateGuadagno.slice(3, 6);  // second element
        const list3 = tonnellateGuadagno.slice(6, 9);  // third element
           // fourth element



        const somma = (lista) => lista.reduce((acc, val) => acc + (parseFloat(val) || 0), 0);


        const sommaList1 = somma(list1);
        const sommaList2 = somma(list2);
        const sommaList3 = somma(list3);




        const sommaTotale = sommaList1 + sommaList2 + sommaList3;

        aggiornaGraficoChart(sommaList1,sommaList2,sommaList3,sommaTotale);

        let percentualeList1 = sommaTotale ? (sommaList1 / sommaTotale) * 100 : 0;
        let percentualeList2 = sommaTotale ? (sommaList2 / sommaTotale) * 100 : 0;
        let percentualeList3 = sommaTotale ? (sommaList3 / sommaTotale) * 100 : 0;



        percentualeList1 = Math.floor(percentualeList1);
        percentualeList2 = Math.floor(percentualeList2);
        percentualeList3 = Math.floor(percentualeList3);


        return [percentualeList1, percentualeList2, percentualeList3];
    }





    function aggiornaGraficoChart(sommaList1, sommaList2, sommaList3,sommaTotale) {
    sommaTotale=sommaTotale+50000;

        var ctx = document.getElementById("myBarChart");
        var myBarChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["Preziosi", "Mediopreziosi", "MenoPreziosi"],
                datasets: [{
                    label: "Revenue",
                    backgroundColor: "#4e73df",
                    hoverBackgroundColor: "#2e59d9",
                    borderColor: "#4e73df",
                    data: [sommaList1, sommaList2, sommaList3],
                }],
            },
            options: {
                maintainAspectRatio: false,
                layout: {
                    padding: {
                        left: 10,
                        right: 25,
                        top: 25,
                        bottom: 0
                    }
                },
                scales: {
                    xAxes: [{
                        time: {
                            unit: 'year'
                        },
                        gridLines: {
                            display: false,
                            drawBorder: false
                        },
                        ticks: {
                            maxTicksLimit: 7, // Limita il numero di tick sull'asse X
                            padding: 25 // Aggiunge spazio tra i tick per evitare sovrapposizioni
                        },
                        maxBarThickness: 30,
                    }],
                    yAxes: [{
                        ticks: {
                            min: 0,
                            max: 100000,
                            maxTicksLimit: 5, // Limita il numero di tick sull'asse Y
                            padding: 20, // Aggiunge spazio tra i tick sull'asse Y
                            // Include un simbolo di valuta nel label
                            callback: function(value, index, values) {
                                return '$' + number_format(value);
                            }
                        },
                        gridLines: {
                            color: "rgb(234, 236, 244)",
                            zeroLineColor: "rgb(234, 236, 244)",
                            drawBorder: false,
                            borderDash: [2],
                            zeroLineBorderDash: [2]
                        }
                    }],
                },
                legend: {
                    display: false
                },
                tooltips: {
                    titleMarginBottom: 10,
                    titleFontColor: '#6e707e',
                    titleFontSize: 14,
                    backgroundColor: "rgb(255,255,255)",
                    bodyFontColor: "#858796",
                    borderColor: '#dddfeb',
                    borderWidth: 1,
                    xPadding: 15,
                    yPadding: 15,
                    displayColors: false,
                    caretPadding: 10,
                    callbacks: {
                        label: function(tooltipItem, chart) {
                            var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                            return datasetLabel + ': $' + number_format(tooltipItem.yLabel);
                        }
                    }
                },
            }
        });
    }




    // Funzione per aggiornare il grafico a torta (doughnut chart)
    function aggiornaGrafico(tonnellateGuadagno) {

        var ctx = document.getElementById("myPieChart");

        // Creare una lista con le percentuali
        const percentualeList = percentuale(tonnellateGuadagno);

        // Inizializzare il grafico a torta con i dati
        window.myPieChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels:  ["Preziosi", "Mediopreziosi", "MenoPreziosi"],
                datasets: [{
                    data: percentualeList,
                    backgroundColor: ['#a4e73df', '#1cc88a', '#36b9cc'],
                    hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf','#e67e22'],
                    hoverBorderColor: "rgba(234, 236, 244, 1)",
                }],
            },
            options: {
                maintainAspectRatio: false,
                tooltips: {
                    backgroundColor: "rgb(255,255,255)",
                    bodyFontColor: "#858796",
                    borderColor: '#dddfeb',
                    borderWidth: 1,
                    xPadding: 15,
                    yPadding: 15,
                    displayColors: false,
                    caretPadding: 10,
                },
                legend: {
                    display: true,  // Impostato a true per mostrare la leggenda
                    position: 'bottom', // Posizionamento della leggenda (top, left, right, bottom)
                    labels: {
                        fontColor: '#858796', // Colore del testo della leggenda
                        fontSize: 14,  // Dimensione del font
                    }
                },
                cutoutPercentage: 80,
            },
        });
    }


    caricaProdotti();


    setInterval(caricaProdotti, 10000);

});
