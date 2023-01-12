<template>
    <div v-if='angebote.errormessage != ""'>
        Errormessage: {{angebote.errormessage}}
    </div>
    <h1>Versteigerung {{angebot.beschreibung}} ab {{angebot.mindestpreis}} EUR</h1>
    <p> von {{angebot.anbietername}}, abholbar in <GeoLink :lat="angebot.lat" :lon="angebot.lon">{{angebot.abholort}}</GeoLink> bis {{angebot.ablaufzeitpunkt}}</p>

    <p>Bisheriges Topgebot von EUR {{gebote.topgebot}} ist von {{gebote.topbieter}}}</p>
    <input type="number" name="neuesgebot" id="neuesgebot"><button v-on:click="sendeGebot(#neuesgebot)">bieten</button>
    <tr>
        <td v-for="gebot in toptenliste" v-bind:key="gebot">
            <!--<gebot>{{gebot.username}}</gebot>
            {{gebot.username}} hmmm doesnt work....-->
        </td>
    </tr>
    <ul>
        <li>
            <p v-for="gebot in toptenliste" v-bind:key="gebot"></p>
        </li>
    </ul>
</template>


<script setup lang="ts">
    import GeoLink from '@/components/GeoLink.vue';
    import type { AngebotListeDing } from '@/services/IAngebotListeItem';
    import { useAngebot } from '@/services/useAngebot';
    import { useGebot } from '@/services/useGebot';
    import { computed } from '@vue/reactivity';
    import { onMounted } from 'vue';

    const props = defineProps<{angebotidstr: String}>();
    const { angebote, updateAngebote } = useAngebot();

    let angebot:AngebotListeDing;
    for (let i = 0; i < angebote.angebotliste.length; i++) {
        if (angebote.angebotliste[i].angebotid == Number(props.angebotidstr)) {
            angebot = angebote.angebotliste[i];
        }
    }

    const { gebote, updateGebote, sendeGebot } = useGebot(Number(props.angebotidstr));
    
    let toptenliste = computed(() => {
        gebote.gebotliste.slice(0,10).sort(function(a,b) {
            if (a.gebotzeitpunkt.localeCompare(b.gebotzeitpunkt) == 1) return 1;
            if (a.gebotzeitpunkt.localeCompare(b.gebotzeitpunkt) != 1) return -1;
            return 0;
        })
    })

    onMounted( async() => {
        updateGebote()
    });

</script>