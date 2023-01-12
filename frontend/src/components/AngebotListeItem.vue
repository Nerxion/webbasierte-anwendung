<template>
    <details>
        <summary>
            <span>{{angebot.beschreibung}}, {{angebot.gebote}} Gebote, {{angebot.topgebot}} EUR</span>
        </summary>
        <p>Letztes Gebot: {{angebot.topgebot}} EUR (Mindestpreis war {{angebot.mindestpreis}} EUR)</p>
        <p>Abholort: <GeoLink :lat="angebot.lat" :lon="angebot.lon">{{angebot.abholort}}</GeoLink></p>
        <p>bei: {{angebot.anbietername}}</p>
        <p>bis: {{angebot.ablaufzeitpunkt}}</p>
    </details>
</template>


<script setup lang="ts">
    import GeoLink from '@/components/GeoLink.vue';
    import type { IAngebotListeItem } from '@/services/IAngebotListeItem';

    const props = defineProps<{angebot: IAngebotListeItem}>()
</script>