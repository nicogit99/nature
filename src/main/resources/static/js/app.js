<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prodotti Agricoli</title>
</head>
<body>
    <!-- Contenitore per visualizzare i prodotti -->
    <div id="prodotti-container"></div>

    <script th:inline="javascript">
        /*<![CDATA[*/
        // Passa la lista di prodotti 'agri' e 'tonnellateList' come array JavaScript
        var prodotti = /*[[${agri}]]*/ [];
        var tonnellateList = /*[[${tonnellateList}]]*/ [];

        // Verifica il contenuto delle variabili
        console.log(prodotti);
        console.log(tonnellateList);

        // Funzione per recuperare e aggiornare i prodotti dinamicamente
        function aggiornaProdotti() {
            // Supponiamo che 'prodotti' e 'tonnellateList' siano già passati da Thymeleaf
            const container = document.getElementById("prodotti-container");
            container.innerHTML = "";  // Pulisci il contenuto esistente

            // Cicla sui dati ricevuti e crea le card per ciascun prodotto
            prodotti.forEach((prodotto, index) => {
                const prodottoCard = document.createElement('div');
                prodottoCard.classList.add('prodotto-card');
                prodottoCard.innerHTML = `
                    <h3>${prodotto.nome}</h3>
                    <p><strong>Tipo:</strong> ${prodotto.tipo}</p>
                    <p><strong>Quantità:</strong> ${prodotto.quantita}</p>
                    <p><strong>Prezzo:</strong> ${prodotto.prezzo} €</p>
                    <p><strong>Superficie:</strong> ${prodotto.superficie} m²</p>
                    <p><strong>Giorni Crescita:</strong> ${prodotto.giorniCrescita}</p>
                    <p><strong>Tonnellate:</strong> ${tonnellateList[index] || 'N/A'} t</p>
                `;
                container.appendChild(prodottoCard);
            });
        }

        // Aggiorna i prodotti ogni 5 secondi
        setInterval(aggiornaProdotti, 5000);

        // Inizializza la prima chiamata per aggiornare i prodotti subito
        aggiornaProdotti();
        /*]]>*/
    </script>
</body>
</html>
