// Memorizziamo i dati precedenti per calcolare la variazione percentuale
let datiPrecedenti = {
  mining: [
    { paese: 'Cile' },
    { paese: 'Australia' },
    { paese: 'Italia' },
    { paese: 'Francia' }
  ],
  livestock: [
    { paese: 'Brasile' },
    { paese: 'USA' },
    { paese: 'Canada' },
    { paese: 'Norvegia' }
  ],
  forestry: [
    { paese: 'Canada' },
    { paese: 'Russia' },
    { paese: 'Grecia' },
    { paese: 'Korea sud' }
  ],
  fishing: [
    { paese: 'Norvegia' },
    { paese: 'Giappone' },
    { paese: 'Cina' },
    { paese: 'Colombia' }
  ],
  agriculture: [
    { paese: 'USA' },
    { paese: 'Brasile' },
    { paese: 'Norvegia' },
    { paese: 'Polonia' }
  ]
};

// Funzione per calcolare la variazione percentuale
function calcolaVariazionePercentuale(precedente, attuale) {
  if (precedente === 0) return 0; // Se la quantità precedente è 0, non possiamo calcolare la variazione
  return ((attuale - precedente) / precedente) * 100;
}

// Funzione per aggiornare i dati per ciascun settore
function aggiornaDatiSettore(settore, datiSettore, tabellaId) {
  const nuoviDati = datiSettore.map(dato => ({
    ...dato,
    variazionePercentuale: (Math.random() * 10 - 5).toFixed(2), // Aggiungiamo una variazione percentuale casuale
  }));

  // Selezioniamo il corpo della tabella
  const tableBody = document.getElementById(tabellaId).getElementsByTagName('tbody')[0];
  // Puliamo la tabella esistente
  tableBody.innerHTML = '';

  // Popoliamo la tabella con i nuovi dati e calcoliamo la variazione percentuale
  nuoviDati.forEach(dato => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${dato.paese}</td>
      <td>${dato.variazionePercentuale}%</td>
      <td>${new Date().toLocaleString()}</td>
    `;
    tableBody.appendChild(row);
  });

  // Aggiorniamo i dati precedenti per il prossimo calcolo
  datiPrecedenti[settore] = [...nuoviDati];
}

// Funzione per generare il grafico
function generaGrafico() {
  const settori = ['mining', 'livestock', 'forestry', 'fishing', 'agriculture'];
  const settoriLabels = ['Minerario', 'Allevamento', 'Silvicoltura', 'Pesca', 'Agricoltura'];
  const variazioniPercentuali = settori.map(settore => {
    const settoreData = datiPrecedenti[settore];
    const mediaVariazione = settoreData.reduce((sum, dato) => sum + parseFloat(dato.variazionePercentuale), 0) / settoreData.length;
    return mediaVariazione.toFixed(2);
  });

  const ctx = document.getElementById('myChart').getContext('2d');

  // Creiamo il grafico
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: settoriLabels,
      datasets: [{
        label: 'Variazione Percentuale Media per Settore',
        data: variazioniPercentuali,
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(153, 102, 255, 0.2)'
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)'
        ],
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}

// Funzione che aggiorna tutti i settori e genera il grafico
function aggiornaDati() {
  aggiornaDatiSettore('mining', datiPrecedenti.mining, 'miningTable');
  aggiornaDatiSettore('livestock', datiPrecedenti.livestock, 'livestockTable');
  aggiornaDatiSettore('forestry', datiPrecedenti.forestry, 'forestryTable');
  aggiornaDatiSettore('fishing', datiPrecedenti.fishing, 'fishingTable');

  aggiornaDatiSettore('agriculture', datiPrecedenti.agriculture, 'agricultureTable');
  generaGrafico();  // Aggiorna il grafico con le nuove variazioni percentuali
}

// Chiamare la funzione subito per un primo aggiornamento
aggiornaDati();

// Impostare l'aggiornamento ogni 10 secondi (10000 millisecondi)
setInterval(aggiornaDati, 10000);
