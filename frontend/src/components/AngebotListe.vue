<template>
    <div>
        <div>
            <input type="text" v-model="suchfeld" placeholder="Suchbegriff"/>
            <button v-on:click="suchfeldClear()">clear</button>
        </div>
        <div>
            <table>
                <tbody>
                    <AngebotListeItem :angebot="angebot" v-for="angebot in angeboteliste"/>
                </tbody>
            </table>
        </div>
    </div>
</template>


<script setup lang="ts">
    import type { IAngebotListeItem } from '@/services/IAngebotListeItem';
    import AngebotListeItem from './AngebotListeItem.vue';
    import { computed, ref } from 'vue';

    const props = defineProps<{angebote: Readonly<IAngebotListeItem[]>}>();
    const suchfeld = ref("")

    const angeboteliste = computed(() => {
        return props.angebote.filter(e =>
            e.abholort.toLowerCase()
                .includes(suchfeld.value.toLowerCase())
            || e.beschreibung.toLowerCase()
                .includes(suchfeld.value.toLowerCase())
            || e.anbietername.toLowerCase()
                .includes(suchfeld.value.toLowerCase()))
    })

    function suchfeldClear(): void{
        suchfeld.value = ""
    }
</script>