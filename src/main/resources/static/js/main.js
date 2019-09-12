import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import 'api/resource'
import router from 'router/router'
import App from 'pages/App.vue'
import store from 'store/store'
import { connect } from './util/ws'
import 'vuetify/dist/vuetify.min.css'
import * as Sentry from '@sentry/browser'
import * as Integrations from '@sentry/integrations'

Sentry.init({
    dsn: 'https://ceef874739644afaa72c020e7b118e1b@sentry.io/1725360',
    integrations: [new Integrations.Vue({Vue, attachProps: true})],
});

Sentry.configureScope(scope =>
    scope.setUser({
        id: profile && profile.id,
        username: profile && profile.name
    })
);

if (profile) {
    connect()
}

Vue.config.devtools = true;

Vue.use(Vuetify);

new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    store,
    router,
    render: a => a(App)
});