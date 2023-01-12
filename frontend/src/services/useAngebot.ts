import { reactive, readonly } from "vue";
import type { IAngebotListeItem } from "./IAngebotListeItem";
import { Client, type Message } from '@stomp/stompjs';
import type { IBackendInfoMessage } from "./IBackendInfoMessage";

export interface IAngebotState {
    angebotliste: IAngebotListeItem[],
    errormessage: string
}

const angebotState:IAngebotState = reactive({
    angebotliste: [], 
    errormessage: ""
})

export function useAngebot() {
    return {
        angebote: readonly(angebotState),
        updateAngebote
    }
}

export function updateAngebote() {
    fetch('/api/angebot', {
        method: 'GET',
    })
    .then( (response) => {
        if (!response.ok) {
            angebotState.errormessage = response.statusText
        }
        return response.json();
    })
    .then( (jsondata) => {
        angebotState.angebotliste = jsondata;
        angebotState.errormessage = "";
    })
    .catch( (fehler) => {
        angebotState.errormessage = fehler
    })
}

export function receiveAngebotMessage() {
    console.log("Verbindung zum Server...")
    const wsurl = `ws://${window.location.host}/stompbroker`
    const DEST = "/topic/angebot";

    const stompclient = new Client({ brokerURL: wsurl})
    stompclient.onWebSocketError = (event) => {/* WebSocket-Fehler */}
    stompclient.onStompError = (event) => {/* STOMP-Fehler */}

    stompclient.onConnect = (frame) => { //erfolgreicher Verbindungsaufbau zum Broker
        stompclient.subscribe(DEST, (message) => { // Nachricht auf DEST empfangen
            const obj:IBackendInfoMessage = JSON.parse(message.body)
            console.log("Verbindung erfolgreich, Operation: " + obj.operation)
            updateAngebote()
        });
    };
    stompclient.onDisconnect = () => {/* Verbindung abgebaut */}

    stompclient.activate();
}