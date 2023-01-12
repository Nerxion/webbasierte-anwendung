import { reactive, readonly } from "vue";

import { Client } from '@stomp/stompjs';
const wsurl = `ws://${window.location.host}/stompbroker`

import { useLogin } from '@/services/useLogin'
const { logindata } = useLogin()

export function useGebot(angebotid: number) {
    /*
     * Mal ein Beispiel für CompositionFunction mit Closure/lokalem State,
     * um parallel mehrere *verschiedene* Versteigerungen managen zu können
     * (Gebot-State ist also *nicht* Frontend-Global wie Angebot(e)-State)
     */

    // STOMP-Destination
    const DEST = `/topic/gebot/${angebotid}`

    ////////////////////////////////

    // entspricht GetGebotResponseDTO.java aus dem Spring-Backend
    interface IGetGebotResponseDTO {
        gebotid: number,
        gebieterid: number,
        gebietername: string,
        angebotid: number,
        angebotbeschreibung: string,
        betrag: number,
        gebotzeitpunkt: string // kommt als ISO-DateTime String-serialisiert an!
    }

    // Basistyp für gebotState
    interface IGebotState {
        angebotid: number,              // ID des zugehörigen Angebots
        topgebot: number,               // bisher höchster gebotener Betrag
        topbieter: string,              // Name des Bieters, der das aktuelle topgebot gemacht hat
        gebotliste: IGetGebotResponseDTO[], // Liste der Gebote, wie vom Backend geliefert
        receivingMessages: boolean,     // Status, ob STOMP-Messageempfang aktiv ist oder nicht
        errormessage: string            // (aktuelle) Fehlernachricht oder Leerstring
    }


    /* reaktives Objekt auf Basis des Interface <IGebotState> */
    const gebotState:IGebotState = reactive({
        angebotid: 0,
        topgebot: 0,
        topbieter: "",
        gebotliste: [],
        receivingMessages: false,
        errormessage: ""
    })
    


    function processGebotDTO(gebotDTO: IGetGebotResponseDTO) {
        const dtos = JSON.stringify(gebotDTO)
        console.log(`processGebot(${dtos})`)

        /*
         * suche Angebot für 'gebieter' des übergebenen Gebots aus der gebotliste (in gebotState)
         * falls vorhanden, hat der User hier schon geboten und das Gebot wird nur aktualisiert (Betrag/Gebot-Zeitpunkt)
         * falls nicht, ist es ein neuer Bieter für dieses Angebot und das DTO wird vorne in die gebotliste des State-Objekts aufgenommen
         */
        let vorhanden = false;
        for (let i = 0; i < gebotState.gebotliste.length; i++) {
            if (gebotState.gebotliste[i].gebieterid == gebotDTO.gebieterid) {
                gebotState.gebotliste[i].betrag = gebotDTO.betrag;
                gebotState.gebotliste[i].gebotzeitpunkt = gebotDTO.gebotzeitpunkt;
                vorhanden = true;
            }
        }
        if (vorhanden == false) {
            gebotState.gebotliste.unshift(gebotDTO)
        }

        /*
         * Falls gebotener Betrag im DTO größer als bisheriges topgebot im State,
         * werden topgebot und topbieter (der Name, also 'gebietername' aus dem DTO)
         * aus dem DTO aktualisiert
         */
        if (gebotDTO.betrag > gebotState.topgebot) {
            gebotState.topgebot = gebotDTO.betrag;
            gebotState.topbieter = gebotDTO.gebietername;
        }

    }


    function receiveGebotMessages() {
        /*
         * analog zu Message-Empfang bei Angeboten
         * wir verbinden uns zur brokerURL (s.o.),
         * bestellen Nachrichten von Topic DEST (s.o.)
         * und rufen die Funktion processGebotDTO() von oben
         * für jede neu eingehende Nachricht auf diesem Topic auf.
         * Eingehende Nachrichten haben das Format IGetGebotResponseDTO (s.o.)
         * Die Funktion aktiviert den Messaging-Client nach fertiger Einrichtung.
         * 
         * Bei erfolgreichem Verbindungsaubau soll im State 'receivingMessages' auf true gesetzt werden,
         * bei einem Kommunikationsfehler auf false 
         * und die zugehörige Fehlermeldung wird in 'errormessage' des Stateobjekts geschrieben
         */

        console.log("Verbindung zum Server...")
    
        const stompclient = new Client({ brokerURL: wsurl})
        stompclient.onWebSocketError = (event) => {
            gebotState.receivingMessages = false
            gebotState.errormessage = "WebSocketError" //Fehlermeldung
        }
        stompclient.onStompError = (event) => {
            gebotState.receivingMessages = false
            gebotState.errormessage = "StompError" //Fehlermeldung
        }
    
        stompclient.onConnect = (frame) => { //erfolgreicher Verbindungsaufbau zum Broker
            gebotState.receivingMessages = true
            stompclient.subscribe(DEST, (message) => { // Nachricht auf DEST empfangen
                const obj:IGetGebotResponseDTO = JSON.parse(message.body)
                console.log("Verbindung erfolgreich, GebotID: " + obj.gebotid)
                processGebotDTO(obj)
            });
        };
        stompclient.onDisconnect = () => {/* Verbindung abgebaut */}
    
        stompclient.activate();
    }


    async function updateGebote() {
        /*
         * holt per fetch() auf Endpunkt /api/gebot die Liste aller Gebote ab
         * (Array vom Interface-Typ IGetGebotResponseDTO, s.o.)
         * und filtert diejenigen für das Angebot mit Angebot-ID 'angebotid' 
         * (Parameter der useGebot()-Funktion, s.o.) heraus. 
         * Falls erfolgreich, wird 
         *   - das Messaging angestoßen (receiveGebotMessages(), s.o.), 
         *     sofern es noch nicht läuft
         *   - das bisherige maximale Gebot aus der empfangenen Liste gesucht, um
         *     die State-Properties 'topgebot' und 'topbieter' zu initialisieren
         *   - 'errormessage' auf den Leerstring gesetzt
         * Bei Fehler wird im State-Objekt die 'gebotliste' auf das leere Array 
         * und 'errormessage' auf die Fehlermeldung geschrieben.
         */

        fetch('/api/gebot', {
            method: 'GET',
        })
        .then( (response) => {
            if (!response.ok) {
                gebotState.errormessage = response.statusText
            }
            return response.json();
        })
        .then( (jsondata) => {
            receiveGebotMessages()
            let filterliste = jsondata.gebotliste.filter((gebot: { angebotid: number; }) => gebot.angebotid == angebotid)
            let maxgebot = 0;
            let maxbieter = "";
            for (let i = 0; i < filterliste.length; i++) {
                if (filterliste[i].gebot > maxgebot) {
                    maxgebot = filterliste[i].gebot;
                    maxbieter = filterliste[i].gebietername
                }
            }
            gebotState.topgebot = maxgebot;
            gebotState.topbieter = maxbieter;
            gebotState.errormessage = "";
        })
        .catch( (fehler) => {
            gebotState.errormessage = fehler;
        })
    }


    // Analog Java-DTO AddGebotRequestDTO.java
    interface IAddGebotRequestDTO {
        benutzerprofilid: number,
        angebotid: number,
        betrag: number
    }

    async function sendeGebot(betrag: number) {
        /*
         * sendet per fetch() POST auf Endpunkt /api/gebot ein eigenes Gebot,
         * schickt Body-Struktur gemäß Interface IAddGebotRequestDTO als JSON,
         * erwartet ID-Wert zurück (response.text()) und loggt diesen auf die Console
         * Falls ok, wird 'errormessage' im State auf leer gesetzt,
         * bei Fehler auf die Fehlermeldung
         */
        let gebotrequestdto:IAddGebotRequestDTO = {
            benutzerprofilid: logindata.benutzerprofilid,
            angebotid: logindata.angebotid, // gebotState.angebotid ??
            betrag: betrag
        };

        fetch('/api/gebot', {
            method: 'POST',
            body: JSON.stringify(gebotrequestdto)
        })
        .then( (response) => {
            console.log(response.text())
            if (!response.ok) {
                gebotState.errormessage = response.statusText
            }
            return response.json();
        })
        .then( (jsondata) => {
            gebotState.errormessage = "";
        })
        .catch( (fehler) => {
            gebotState.errormessage = fehler;
        })
    }

    // Composition Function -> gibt nur die nach außen freigegebenen Features des Moduls raus
    return {
        gebote: readonly(gebotState),
        updateGebote,
        sendeGebot
    }
}

