document.addEventListener("DOMContentLoaded", function() {
    var myBarChart;
    var myPieChart;
    // Funzione per caricare i prodotti e aggiornare la tabella
async function caricaProdotti() {

    try {


        const response = await fetch("/naturlink/sivicoltura/datatable-framments");

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        console.log(data);

        const tableBody = document.querySelector("#dataTable tbody");
        tableBody.innerHTML = ""; // Svuotare la tabella

        const { sivicoltura, tonnellateList = [], tonnellateGuadagno = [], Meteo = [] } = data;

        sivicoltura.forEach((prodotto, index) => {
            const row = document.createElement("tr");
            const { id, nome, tipo, prezzo, giorniCrescita, superficie } = prodotto;

            row.appendChild(createTableCell(nome));  // Nome
            row.appendChild(createTableCell(tipo));  // Tipo
            row.appendChild(createTableCell(prezzo + "€")); // Prezzo
            row.appendChild(createTableCell(giorniCrescita)); // Giorni di crescita
            row.appendChild(createTableCell(superficie)); // Superficie
            row.appendChild(createTableCell(tonnellateList[index] || 'N/A'));  // Tonnellate
            row.appendChild(createTableCell((tonnellateGuadagno[index] || 0) + "€"));  // Guadagno

            // Aggiungi un bottone "Cancella"
            const deleteButtonCell = document.createElement("td");
            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Cancella";
            deleteButton.classList.add("btn", "btn-danger");  // Aggiungi classi per styling (Bootstrap)

            // Aggiungi l'evento di click per inviare una richiesta DELETE al backend
            deleteButton.addEventListener("click", function () {
                // Invia la richiesta DELETE all'endpoint Spring Boot senza Content-Type
                fetch(`/naturlink/siv/${id}`, {  // Correzione: interpolazione della variabile id
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


            // Aggiorna i grafici dopo aver caricato i prodotti
            aggiornaGrafico(tonnellateGuadagno);

           // Nascondi il loader della tabella
                document.getElementById("precipitazioniValore").textContent = (Meteo[0] || 'N/A') + "mm";
                document.getElementById("umiditaValore").textContent = (Meteo[1] || 'N/A') + "%";
                document.getElementById("temperaturaValore").textContent = (Meteo[2] || 'N/A') + "°C";
                // Calcola e mostra il sommatotale dopo che i loader sono spariti
                const sommaTotale = calcolaSommaTotale(tonnellateGuadagno);
                document.getElementById("sommatotale").textContent = sommaTotale || 'N/A';


        } catch (error) {
            console.error("C'è stato un problema con l'operazione fetch:", error);
        }
    }

     function calcolaSommaTotale(tonnellateGuadagno) {
          const somma = (lista) => lista.reduce((acc, val) => acc + (parseFloat(val) || 0), 0);

          const sommaTotale = somma(tonnellateGuadagno);
          return sommaTotale;
      }




    // Funzione per creare una cella della tabella
    function createTableCell(content) {
        const cell = document.createElement("td");
        cell.textContent = content;
        return cell;
    }

    // Funzione per aggiornare il grafico a barre
    function aggiornaGraficoChart(sommaList1, sommaList2, sommaList3, sommaTotale) {
        sommaTotale = sommaTotale + 50000;

        var ctx = document.getElementById("myBarChart");

           if (myBarChart) {
           myBarChart.destroy();
                }

       myBarChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["Foreste tropicali", "Foreste Temperate", "Foreste Boreali"],
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
                            maxTicksLimit: 7,
                            padding: 25
                        },
                        maxBarThickness: 30,
                    }],
                    yAxes: [{
                        ticks: {
                            min: 0,
                            max: 500000,
                            maxTicksLimit: 5,
                            padding: 20,
                            callback: function(value) {
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
                },
            }
        });
    }

    // Funzione per aggiornare il grafico a torta (doughnut chart)
    function aggiornaGrafico(tonnellateGuadagno) {
        var ctx = document.getElementById("myPieChart");

        const percentualeList = percentuale(tonnellateGuadagno);
         if (myPieChart) {
          myPieChart.destroy();
          }
        myPieChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ["Foreste tropicali", "Foreste Temperate", "Foreste Boreali"],
                datasets: [{
                    data: percentualeList,
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
                    display: true,
                    position: 'bottom',
                    labels: {
                        fontColor: '#858796',
                        fontSize: 14,
                    }
                },
                cutoutPercentage: 80,
            },
        });
    }

    // Funzione per calcolare la percentuale per i grafici
    function percentuale(tonnellateGuadagno) {
        const list1 = tonnellateGuadagno.slice(0, 3);
        const list2 = tonnellateGuadagno.slice(3, 6);
        const list3 = tonnellateGuadagno.slice(6, 9);

        const somma = (lista) => lista.reduce((acc, val) => acc + (parseFloat(val) || 0), 0);

        const sommaList1 = somma(list1);
        const sommaList2 = somma(list2);
        const sommaList3 = somma(list3);

        const sommaTotale = sommaList1 + sommaList2 + sommaList3;

        aggiornaGraficoChart(sommaList1, sommaList2, sommaList3, sommaTotale);

        let percentualeList1 = sommaTotale ? (sommaList1 / sommaTotale) * 100 : 0;
        let percentualeList2 = sommaTotale ? (sommaList2 / sommaTotale) * 100 : 0;
        let percentualeList3 = sommaTotale ? (sommaList3 / sommaTotale) * 100 : 0;

        percentualeList1 = Math.floor(percentualeList1);
        percentualeList2 = Math.floor(percentualeList2);
        percentualeList3 = Math.floor(percentualeList3);

        return [percentualeList1, percentualeList2, percentualeList3];
    }

    // Carica i prodotti inizialmente
    caricaProdotti();

    // Imposta un intervallo per aggiornare i prodotti ogni 20 secondi
    setInterval(caricaProdotti, 12000);

});
