// Funzione per caricare i dati dei prodotti e aggiornarli nel contenitore
function aggiornaProdotti() {
    fetch('/naturnlink/dashboard/agricoltura/json')  // Richiesta AJAX per ottenere i dati dei prodotti
        .then(response => {
            if (!response.ok) {
                throw new Error('Errore nel recupero dei prodotti');
            }
            return response.json();  // Converte la risposta in formato JSON
        })
        .then(data => {
            const container = document.getElementById("prodotti-container");
            container.innerHTML = "";  // Pulisce il contenuto esistente

            // Crea e aggiungi ogni prodotto al contenitore
            data.forEach(prodotto => {
                const prodottoCard = document.createElement('div');
                prodottoCard.classList.add('prodotto-card');
                prodottoCard.innerHTML = `
                    <h3>${prodotto.nome}</h3>
                    <p><strong>Tipo:</strong> ${prodotto.tipo}</p>
                    <p><strong>Quantità:</strong> ${prodotto.quantita}</p>
                    <p><strong>Prezzo:</strong> ${prodotto.prezzo} €</p>
                    <p><strong>Superficie:</strong> ${prodotto.superficie} m²</p>
                `;
                container.appendChild(prodottoCard);
            });
        })
        .catch(error => {
            console.error('Errore nel recupero dei prodotti:', error);
        });
}

// Avvia l'aggiornamento dei prodotti ogni 5 secondi
setInterval(aggiornaProdotti, 5000);
