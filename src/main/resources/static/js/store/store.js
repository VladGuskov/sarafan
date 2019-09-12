import Vue from 'vue';
import Vuex from 'vuex';
import { mdiLogoutVariant, mdiDeleteForeverOutline, mdiAccountCircle } from '@mdi/js'
import messagesApi from 'api/messages'
import commentApi from 'api/comment'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        messages,
        profile,
        ...frontendData,
        logout: mdiLogoutVariant,
        dele : mdiDeleteForeverOutline,
        unknown: mdiAccountCircle
    },
    getters: {
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: {
        addMessageMutation(state,message){
            state.messages = [
                ...state.messages,
                message
            ]
        },
        updateMessageMutation(state,message){
            const updateIndex = state.messages.findIndex(item => item.id === message.id);
            state.messages = [
                ...state.messages.slice(0,updateIndex),
                message,
                ...state.messages.slice(updateIndex + 1)
            ]
        },
        removeMessageMutation(state,message){
            const deleteIndex = state.messages.findIndex(item => item.id === message.id);
            if (deleteIndex > -1){
                state.messages = [
                    ...state.messages.slice(0,deleteIndex),
                    ...state.messages.slice(deleteIndex + 1)
                ]
            }
        },
        addCommentMutation(state,comment){
            const updateIndex = state.messages.findIndex(item => item.id === comment.message.id);
            const message = state.messages[updateIndex];
            if (message.comments === null) {
                state.messages = [
                    ...state.messages.slice(0,updateIndex),
                    {
                        ...message,
                        comments : [comment]
                    },
                    ...state.messages.slice(updateIndex + 1)
                ]
            } else if (!message.comments.find(it => it.id === comment.id)) {
                state.messages = [
                    ...state.messages.slice(0,updateIndex),
                    {
                        ...message,
                        comments: [
                            ...message.comments,
                            comment
                        ]
                    },
                    ...state.messages.slice(updateIndex + 1)
                ]
            }
        },
        removeCommentMutation(state,comment) {
            let messageIndex;
            if (comment.message !==  undefined) {
                messageIndex = state.messages.findIndex(item => item.id === comment.message.id);
                const message = state.messages[messageIndex];
                if(messageIndex > -1 && message.comments !== null) {
                    message.comments = [
                        ...message.comments.slice(0,comment.id),
                        ...message.comments.slice(comment.id + 1),
                    ]
                }
            }
        },
        addMessagePageMutation(state, messages) {
            const targetMessages = state.messages
                .concat(messages)
                .reduce((res, val) => {
                    res[val.id] = val;
                    return res
                }, {});
            state.messages = Object.values(targetMessages)
        },
        updateTotalPagesMutation(state, totalPages) {
            state.totalPages = totalPages
        },
        updateCurrentPageMutation(state, currentPage) {
            state.currentPage = currentPage
        }
    },
    actions: {
        async addMessageAction({commit,state},message){
            const result = await messagesApi.add(message);
            const data = await result.json();
            const index = state.messages.findIndex(item => item.id === data.id);
            if (index > -1) {
                commit('updateMessageMutation', data)
            } else {
                commit('addMessageMutation', data)
            }
        },
        async updateMessageAction({commit},message){
            const result = await messagesApi.update(message);
            const data = await result.json();
            commit('updateMessageMutation', data)
        },
        async removeMessageAction({commit},message){
            const result = await messagesApi.remove(message.id);
            if (result.ok) {
                commit('removeMessageMutation', message)
            }
        },
        async addCommentAction({commit,state},comment){
            const response = await commentApi.add(comment);
            const data = await response.json();
            commit('addCommentMutation', data)
        },
        async removeCommentAction({commit,state},comment){
            const result = await commentApi.remove(comment.id);
            if (result.ok) {
                commit('removeCommentMutation', comment);
            }
        },
        async loadPageAction({commit, state}) {
            const response = await messagesApi.page(state.currentPage + 1);
            const data = await response.json();

            commit('addMessagePageMutation', data.messages);
            commit('updateTotalPagesMutation', data.totalPages);
            commit('updateCurrentPageMutation', Math.min(data.currentPage, data.totalPages - 1))
        }
    }
})