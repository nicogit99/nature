document.addEventListener("DOMContentLoaded", function() {

    // Funzione per caricare i prodotti e aggiornare la tabella
    async function caricaProdotti() {
      let loaderVisible = false;
              try {
                  if (!loaderVisible) {
                      // Mostra i loader circolari prima di caricare i grafici
                      document.getElementById("loaderBarChart").style.display = "block";
                      document.getElementById("loaderPieChart").style.display = "block";
                      document.getElementById("myBarChart").style.display = "none";  // Nascondi il grafico
                      document.getElementById("myPieChart").style.display = "none";  // Nascondi il grafico
                      document.getElementById("dataTable").style.display = "none";  // Nascondi la tabella
                      document.getElementById("loaderDataTable").style.display = "block";  // Mostra il loader della tabella
                      loaderVisible = true;
                  }

            const response = await fetch("/naturlink/pesca/datatable-framments");

            if (!response.ok) {
               throw new Error('Network response was not ok');
                }

             const data = await response.json();
              console.log(data);

            // Ottenere i riferimenti alla tabella
            const tableBody = document.querySelector("#dataTable tbody");
            tableBody.innerHTML = ""; // Svuotare il corpo della tabella

            // Destrutturazione dei dati per una lettura più semplice
            const { pesci, tonnellateList = [], tonnellateGuadagno = [],Meteo=[] } = data;

            console.log(tonnellateGuadagno);  // Log per controllo

            // Creare le righe della tabella
           pesci.forEach((pesci, index) => {
                const row = document.createElement("tr");
                const { id,nome, tipo, stockPesce, profondita,prezzo } = pesci;

                row.appendChild(createTableCell(nome));  // Nome
                row.appendChild(createTableCell(tipo));
                row.appendChild(createTableCell(stockPesce));// Tipo
                row.appendChild(createTableCell(profondita)); // Prezzo
                row.appendChild(createTableCell(prezzo)); // Giorni di crescita


                // Aggiungere le celle per Tonnellate e Guadagno
                row.appendChild(createTableCell(tonnellateList[index] || 'N/A'));  // Tonnellate
                row.appendChild(createTableCell(tonnellateGuadagno[index] || 'N/A'));  // Guadagno

                   // Aggiungi un bottone "Cancella"
                            const deleteButtonCell = document.createElement("td");
                            const deleteButton = document.createElement("button");
                            deleteButton.textContent = "Cancella";
                            deleteButton.classList.add("btn", "btn-danger");  // Aggiungi classi per styling (Bootstrap)

                            // Aggiungi l'evento di click per inviare una richiesta DELETE al backend
                            deleteButton.addEventListener("click", function () {
                                // Invia la richiesta DELETE all'endpoint Spring Boot senza Content-Type
                                fetch(`/naturlink/pesc/${id}`, {  // Correzione: interpolazione della variabile id
                                    method: 'DELETE',  // Metodo DELETE per eliminare il prodotto
                                    // Non includere 'Content-Type' in questo caso
                                })
                                    .then(response => {
                                        if (response.ok) {

                                            row.remove();
                                            caricaProdotti();
                                        }
                                    })
                                    .catch(error => {
                                        console.error("Errore nella richiesta DELETE:", error);
                                        alert("Errore nella richiesta.");
                                    });
                            });

                            deleteButtonCell.appendChild(deleteButton);
                            row.appendChild(deleteButtonCell);














                tableBody.appendChild(row);
            });

            // Chiamata per aggiornare il grafico a torta
            aggiornaGrafico(tonnellateGuadagno);

             setTimeout(() => {
               loaderVisible = false;
               document.getElementById("loaderBarChart").style.display = "none";
                document.getElementById("loaderPieChart").style.display = "none";
                 document.getElementById("myBarChart").style.display = "block";
                document.getElementById("myPieChart").style.display = "block";
                document.getElementById("dataTable").style.display = "table";  // Mostra la tabella
                document.getElementById("loaderDataTable").style.display = "none";  // Nascondi il loader della tabella
                  document.getElementById("umiditaValore").textContent = Meteo[1]+"%"|| 'N/A';
                   document.getElementById("temperaturaValore").textContent = Meteo[2]+"°C"|| 'N/A';
                   const sommaTotale = calcolaSommaTotale(tonnellateGuadagno);
                     document.getElementById("sommatotale").textContent = sommaTotale || 'N/A';
                        }, 10000);  // Ridotto a 2 secondi per velocizzare il caricamento

        } catch (error) {
            console.error("C'è stato un problema con l'operazione fetch:", error);
        }
    }

   // Funzione per calcolare la somma totale
    function calcolaSommaTotale(tonnellateGuadagno) {
        const somma = (lista) => lista.reduce((acc, val) => acc + (parseFloat(val) || 0), 0);

        const sommaTotale = somma(tonnellateGuadagno);
        return sommaTotale;
    }



//  funzioni

function number_format(number, decimals, dec_point, thousands_sep) {
  // *     example: number_format(1234.56, 2, ',', ' ');
  // *     return: '1 234,56'
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


    // Funzione per creare una cella della tabella
    function createTableCell(content) {
        const cell = document.createElement("td");
        cell.textContent = content;
        return cell;
    }

    function percentuale(tonnellateGuadagno){
        const list1 = tonnellateGuadagno.slice(0,4);
        const list2 = tonnellateGuadagno.slice(4,9);
        const list3 = tonnellateGuadagno.slice(9,14);


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





    function aggiornaGraficoChart(sommaList1, sommaList2, sommaList3, sommaTotale) {
    sommaTotale=sommaTotale+50000;

        var ctx = document.getElementById("myBarChart");
        var myBarChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["Predatori", "Carapaci", "Molluschi"],
                datasets: [{
                    label: "Guadagno",
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
                            max: 400000,
                            maxTicksLimit: 5, // Limita il numero di tick sull'asse Y
                            padding: 20, // Aggiunge spazio tra i tick sull'asse Y
                            // Include un simbolo di valuta nel label
                            callback: function(value, index, values) {
                                return '€' + number_format(value);
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
                labels: ["Predatori", "Carapaci", "Molluschi"], // Etichette per ogni categoria
                datasets: [{
                    data: percentualeList,  // Guadagno per ogni prodotto
                    backgroundColor: ['#a4e73df', '#1cc88a', '#36b9cc'],
                    hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
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
