// Call the dataTables jQuery plugin
$(document).ready(function() {
  $('#dataTable').DataTable();
});

  document.addEventListener("DOMContentLoaded", function() {
        // Funzione per caricare i prodotti con fetch
        function caricaProdotti() {
            fetch("/naturlink/dashboard/agricoltura/fragment")
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Errore nella risposta');
                    }
                    return response.text();
                })
                .then(data => {
                    // Inserisce il contenuto ricevuto nel contenitore dei prodotti
                    document.getElementById("prodotti-container").innerHTML = data;
                })
                .catch(error => {
                    console.error("Errore durante la richiesta: ", error);
                });
        }

        // Inizializza la prima chiamata per caricare i prodotti
        caricaProdotti();

        // Imposta un intervallo per aggiornare i prodotti ogni 6 secondi
        setInterval(caricaProdotti, 10000);
    });