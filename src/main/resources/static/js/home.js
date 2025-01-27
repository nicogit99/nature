// Memorizziamo i dati precedenti per calcolare la variazione percentuale
let datiPrecedenti = {
  mining: [
    { paese: 'Cile'  },
    { paese: 'Australia' },
  ],
  livestock: [
    { paese: 'Brasile' },
    { paese: 'USA' },
  ],
  forestry: [
    { paese: 'Canada'  },
    { paese: 'Russia' },
  ],
  fishing: [
    { paese: 'Norvegia'  },
    { paese: 'Giappone'  },
  ],
  agriculture: [
    { paese: 'USA' },
    { paese: 'Brasile' },
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

// Funzione che aggiorna tutti i settori
function aggiornaDati() {
  aggiornaDatiSettore('mining', datiPrecedenti.mining, 'miningTable');
  aggiornaDatiSettore('livestock', datiPrecedenti.livestock, 'livestockTable');
  aggiornaDatiSettore('forestry', datiPrecedenti.forestry, 'forestryTable');
  aggiornaDatiSettore('fishing', datiPrecedenti.fishing, 'fishingTable');
  aggiornaDatiSettore('agriculture', datiPrecedenti.agriculture, 'agricultureTable');
}

// Chiamare la funzione subito per un primo aggiornamento
aggiornaDati();

// Impostare l'aggiornamento ogni 10 secondi (10000 millisecondi)
setInterval(aggiornaDati, 10000);
