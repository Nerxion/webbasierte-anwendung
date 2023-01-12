<template>
    <h1>Aktuelle Angebote</h1>
    <div v-if='angebote.errormessage != ""'>
        Errormessage: {{angebote.errormessage}}
    </div>
    <h3>Wir haben aktuell {{angebote.angebotliste.length}} Angebote für Sie</h3>
    <button v-on:click="updateAngebote()">reload</button>
    <AngebotListe :angebote="angebote.angebotliste"/>
</template>


<script setup lang="ts">
    import { receiveAngebotMessage, useAngebot } from '@/services/useAngebot';
    import AngebotListe from '@/components/AngebotListe.vue';
    import { onMounted } from 'vue';

    const { angebote, updateAngebote } = useAngebot();

    onMounted( async() => {
        updateAngebote(),
        receiveAngebotMessage()
    });
</script>