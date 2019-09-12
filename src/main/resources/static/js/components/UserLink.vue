<template>
    <router-link
            :to="`/user/${user.id}`"
            class="px-3"
    >
        <v-avatar
                v-if="user && user.picture"
                :size="`${size || 36}px`"
        >
            <img
                    :src="user.picture"
                    :alt="user.name"
            >
        </v-avatar>

        <v-avatar
                v-else
                :size="`${size || 36}px`"
                color="white"
        >
            <v-icon dark>{{ unknown }}</v-icon>
        </v-avatar>
        <span>{{ userName }}</span>
    </router-link>
</template>

<script>
    import { mapState } from 'vuex'

    export default {
        name: 'UserLink',
        props: ['user', 'size'],
        computed: {
            ...mapState(['unknown']),
            userName() {
                return this.user ? this.user.name : 'unknown'
            }
        },
    }
</script>

<style scoped>
</style>