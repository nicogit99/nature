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

            const response = await fetch("/naturlink/agricolo/datatable-framments");

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            console.log(data);

            const tableBody = document.querySelector("#dataTable tbody");
            tableBody.innerHTML = ""; // Svuotare la tabella

            const { prodotti, tonnellateList = [], tonnellateGuadagno = [], Meteo = [] } = data;

            prodotti.forEach((prodotto, index) => {
                const row = document.createElement("tr");
                const { nome, tipo, prezzo, giorniCrescita, superficie } = prodotto;

                row.appendChild(createTableCell(nome));  // Nome
                row.appendChild(createTableCell(tipo));  // Tipo
                row.appendChild(createTableCell(prezzo + "€")); // Prezzo
                row.appendChild(createTableCell(giorniCrescita)); // Giorni di crescita
                row.appendChild(createTableCell(superficie + "ha")); // Superficie
                row.appendChild(createTableCell(tonnellateList[index] || 'N/A'));  // Tonnellate
                row.appendChild(createTableCell((tonnellateGuadagno[index] || 0) + "€"));  // Guadagno

                tableBody.appendChild(row);
            });

            // Aggiorna i grafici dopo aver caricato i prodotti
            aggiornaGrafico(tonnellateGuadagno);

            setTimeout(() => {
                loaderVisible = false;
                document.getElementById("loaderBarChart").style.display = "none";
                document.getElementById("loaderPieChart").style.display = "none";
                document.getElementById("myBarChart").style.display = "block";
                document.getElementById("myPieChart").style.display = "block";
                document.getElementById("dataTable").style.display = "table";  // Mostra la tabella
                document.getElementById("loaderDataTable").style.display = "none";  // Nascondi il loader della tabella
                document.getElementById("precipitazioniValore").textContent = (Meteo[0] || 'N/A') + "mm";
                document.getElementById("umiditaValore").textContent = (Meteo[1] || 'N/A') + "%";
                document.getElementById("temperaturaValore").textContent = (Meteo[2] || 'N/A') + "°C";
                // Calcola e mostra il sommatotale dopo che i loader sono spariti
                const sommaTotale = calcolaSommaTotale(tonnellateGuadagno);
                document.getElementById("sommatotale").textContent = sommaTotale || 'N/A';
            }, 2000);

        } catch (error) {
            console.error("C'è stato un problema con l'operazione fetch:", error);
        }
    }

    // Funzione per calcolare la somma totale
    function calcolaSommaTotale(tonnellateGuadagno) {
        const list1 = tonnellateGuadagno.slice(0, 3);
        const list2 = tonnellateGuadagno.slice(3, 6);
        const list3 = tonnellateGuadagno.slice(6, 9);

        const somma = (lista) => lista.reduce((acc, val) => acc + (parseFloat(val) || 0), 0);

        const sommaList1 = somma(list1);
        const sommaList2 = somma(list2);
        const sommaList3 = somma(list3);

        return sommaList1 + sommaList2 + sommaList3;
    }

    // Funzione per formattare i numeri
    function number_format(number, decimals, dec_point, thousands_sep) {
        number = (number + '').replace(',', '').replace(' ', '');
        let n = !isFinite(+number) ? 0 : +number,
            prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
            sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
            dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
            s = '',
            toFixedFix = function(n, prec) {
                var k = Math.pow(10, prec);
                return '' + Math.round(n * k) / k;
            };

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

    // Funzione per aggiornare il grafico a barre
    function aggiornaGraficoChart(sommaList1, sommaList2, sommaList3, sommaTotale) {
        sommaTotale = sommaTotale + 50000;

        var ctx = document.getElementById("myBarChart");
        var myBarChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["Frutta", "Verdura", "Ortaggi"],
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
                    callbacks: {
                        label: function(tooltipItem, chart) {
                            var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                            return datasetLabel + ': €' + number_format(tooltipItem.yLabel);
                        }
                    }
                },
            }
        });
    }

    // Funzione per aggiornare il grafico a torta (doughnut chart)
    function aggiornaGrafico(tonnellateGuadagno) {
        var ctx = document.getElementById("myPieChart");

        const percentualeList = percentuale(tonnellateGuadagno);

        window.myPieChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ["frutta", "verdura", "ortaggi"],
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
    setInterval(caricaProdotti, 20000);

});
