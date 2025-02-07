document.addEventListener("DOMContentLoaded", function() {
    var myBarChart;
    var myPieChart;
    // Funzione per caricare i prodotti e aggiornare la tabella
    async function caricaProdotti() {

        try {

            // Fetch dei dati tramite l'API fetch
            const response = await fetch("/naturlink/allevamento/datatable-framments");

            // Verifica se la risposta è OK
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            // Parsing dei dati JSON
            const data = await response.json();
            const { animali, tonnellateList = [], tonnellateGuadagno = [], Meteo = [] } = data;

            // Ottenere i riferimenti alla tabella
            const tableBody = document.querySelector("#dataTable tbody");
            tableBody.innerHTML = ""; // Svuotare il corpo della tabella

            // Creare le righe della tabella
            animali.forEach((animale, index) => {
                const row = document.createElement("tr");
                const {id,tipo, prezzo, quantita } = animale;

                row.appendChild(createTableCell(tipo));  // Tipo
                row.appendChild(createTableCell(prezzo)); // Prezzo
                row.appendChild(createTableCell(quantita)); // Quantità
                row.appendChild(createTableCell(tonnellateList[index] || 'N/A'));  // Tonnellate
                row.appendChild(createTableCell(tonnellateGuadagno[index] || 'N/A'));  // Guadagno

                tableBody.appendChild(row);
            });

            // Chiamata per aggiornare il grafico a torta
            aggiornaGrafico(tonnellateGuadagno);



                document.getElementById("precipitazioniValore").textContent = (Meteo[0] || 'N/A') + "mm";
                document.getElementById("umiditaValore").textContent = (Meteo[1] || 'N/A') + "%";
                document.getElementById("temperaturaValore").textContent = (Meteo[2] || 'N/A') + "°C";

                // Calcola e mostra la somma totale
                const sommaTotale = calcolaSommaTotale(tonnellateGuadagno);
                document.getElementById("sommatotale").textContent = sommaTotale || 'N/A';


        } catch (error) {
            console.error("C'è stato un problema con l'operazione fetch:", error);
        }
    }

    // Funzione per calcolare la somma totale
    function calcolaSommaTotale(tonnellateGuadagno) {


        const somma = (lista) => lista.reduce((acc, val) => acc + (parseFloat(val) || 0), 0);

        const sommaList1 = somma(tonnellateGuadagno);


        return sommaList1 ;
    }



    // Funzione per creare una cella della tabella
    function createTableCell(content) {
        const cell = document.createElement("td");
        cell.textContent = content;
        return cell;
    }

    // Funzione per calcolare le percentuali e aggiornare il grafico
    function percentuale(tonnellateGuadagno) {
        const list1 = tonnellateGuadagno.slice(0, 1);
        const list2 = tonnellateGuadagno.slice(1, 2);
        const list3 = tonnellateGuadagno.slice(2, 3);
        const list4 = tonnellateGuadagno.slice(3);

        const somma = (lista) => lista.reduce((acc, val) => acc + (parseFloat(val) || 0), 0);

        const sommaList1 = somma(list1);
        const sommaList2 = somma(list2);
        const sommaList3 = somma(list3);
        const sommaList4 = somma(list4);

        const sommaTotale = sommaList1 + sommaList2 + sommaList3 + sommaList4;

        aggiornaGraficoChart(sommaList1, sommaList2, sommaList3, sommaList4, sommaTotale);

        let percentualeList1 = sommaTotale ? (sommaList1 / sommaTotale) * 100 : 0;
        let percentualeList2 = sommaTotale ? (sommaList2 / sommaTotale) * 100 : 0;
        let percentualeList3 = sommaTotale ? (sommaList3 / sommaTotale) * 100 : 0;
        let percentualeList4 = sommaTotale ? (sommaList4 / sommaTotale) * 100 : 0;

        percentualeList1 = Math.floor(percentualeList1);
        percentualeList2 = Math.floor(percentualeList2);
        percentualeList3 = Math.floor(percentualeList3);
        percentualeList4 = Math.floor(percentualeList4);

        return [percentualeList1, percentualeList2, percentualeList3, percentualeList4];
    }

    // Funzione per aggiornare il grafico a barre
    function aggiornaGraficoChart(sommaList1, sommaList2, sommaList3, sommaList4, sommaTotale) {
        sommaTotale = sommaTotale + 50000;  // Aggiungi un extra per un margine

        var ctx = document.getElementById("myBarChart");

           if (myBarChart) {
                    myBarChart.destroy();
                }
        myBarChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["Bovino", "Suino", "Ovino", "Pollame"],
                datasets: [{
                    label: "Guadagno",
                    backgroundColor: "#4e73df",
                    hoverBackgroundColor: "#2e59d9",
                    borderColor: "#4e73df",
                    data: [sommaList1, sommaList2, sommaList3, sommaList4],
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
                            max: 100000,
                            maxTicksLimit: 5,
                            padding: 20,
                            callback: function(value) {
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
 // Distruggi il grafico esistente se presente
        if (myPieChart) {
            myPieChart.destroy();
        }
        myPieChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ["Ovino", "Suino", "Pollame", "Bovino"],
                datasets: [{
                    data: percentualeList,
                    backgroundColor: ['yellow', '#1cc88a', '#36b9cc', '#e67e22'],
                    hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf', '#e67e22'],
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

    // Chiamata iniziale per caricare i prodotti
    caricaProdotti();

    // Imposta un intervallo per aggiornare i prodotti e il grafico ogni 20 secondi
    setInterval(caricaProdotti, 12000);

});
