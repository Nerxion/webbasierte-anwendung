<script setup lang="ts">
  import GeoLink from '@/components/GeoLink.vue';
  import AngebotListeItem from '@/components/AngebotListeItem.vue';
  import { computed, ref } from "vue";
  import { useFakeAngebot } from '@/services/useFakeAngebot';
  import AngebotListe from '../components/AngebotListe.vue';

  const { angebote, bietenSimulieren } = useFakeAngebot();
  const a = computed(() => angebote.value[0])
</script>


<template>
  <div class="about">
    <h1>This is an about page</h1>
    <GeoLink :lat="52.9849" :lon="9.5794">Visselhövede</GeoLink><br/>
    <GeoLink :lat="52.9849" :lon="9.5794" :zoom="15">VisselhövedeMitZoom</GeoLink>
    <br><br>
    <h2>About</h2>
    <AngebotListeItem :angebot="a"/>
    <button v-on:click="bietenSimulieren()">bieten simulieren</button>
    <br><br>
    <h2>Aktuelle Angebote</h2>
    <AngebotListe :angebote="angebote"/>
  </div>
</template>



<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
