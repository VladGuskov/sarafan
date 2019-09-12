<template>
    <v-app>
        <v-app-bar app color="indigo" dark>
            <v-toolbar-title>Sarafan</v-toolbar-title>
            <v-btn text
                    class="ma-7"
                   v-if="profile"
                   :disabled="$route.path === '/'"
                   @click="showMessages">
                Сообщения
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn text
                   v-if="profile"
                   :disabled="$route.path === '/user'"
                   @click="showProfile">
                {{profile.name}}
            </v-btn>
            <v-btn
                    class="ma-3"
                    v-if="profile"
                    icon href="/logout">
                <v-icon>
                    {{logout}}
                </v-icon>
            </v-btn>
        </v-app-bar>
        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import { addHandler } from 'util/ws'
    import { mapState, mapMutations } from 'vuex'

    export default {
        computed: mapState(['profile','logout']),
        methods: {
            ...mapMutations([
                'addMessageMutation',
                'updateMessageMutation',
                'removeMessageMutation',
                'addCommentMutation'
            ]),
            showMessages() {
                this.$router.push('/')
            },
            showProfile() {
                this.$router.push('/user')
            }
        },
        created() {
            addHandler(data => {
                if (data.objectType === 'MESSAGE') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addMessageMutation(data.body);
                            break;
                        case 'UPDATE':
                            this.updateMessageMutation(data.body);
                            break;
                        case 'REMOVE':
                            this.removeMessageMutation(data.body);
                            break;
                        default:
                            console.error(`Тип запроса не найден "${data.eventType}"`)
                    }
                } else if (data.objectType === 'COMMENT') {
                    switch (data.eventType) {
                        case 'CREATE':
                            this.addCommentMutation(data.body);
                            break;
                        case 'REMOVE':
                            this.removeCommentMutation(data.body);
                            break;
                        default:
                            console.error(`Тип запроса не найден "${data.eventType}"`)
                    }
                } else {
                    console.error(`Тип объекта не найден "${data.objectType}"`)
                }
            })
        },
        beforeMount() {
            if (!this.profile){
                this.$router.replace('/auth')
            }
        }
    }
</script>
<style>
</style>