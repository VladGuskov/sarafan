<template>
    <v-list-item>
        <user-link
                :user="comment.author"
        ></user-link>
        <v-list-item-content>
            <v-list-item-title >{{comment.text}}</v-list-item-title>
        </v-list-item-content>
        <v-btn  v-if="profile.id === comment.author.id" icon @click="del" small>
            <v-icon>{{dele}}</v-icon>
        </v-btn>
    </v-list-item>
</template>

<script>
    import UserLink from 'components/UserLink.vue'
    import { mapState, mapActions } from 'vuex'

    export default {
        name: 'CommentItem',
        components: {UserLink},
        props: ['comment'],
        computed: mapState(['dele','profile']),
        methods: {
            ...mapActions(['removeCommentAction']),
            del() {
                this.removeCommentAction(this.comment)
            }
        }
    }
</script>

<style scoped>

</style>