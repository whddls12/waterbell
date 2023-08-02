import { createStore } from 'vuex';
import http from '../types/http';
// import axios from 'axios'
export default createStore({
    state: {
        loginUser: null,
        role: 'none',
        underroadListByGugun: [],
        underroadList: [],
        nowUnderroad: {},
        location: { lon: '127', lat: '55' }
    },
    getters: {
        underroadList(state) {
            return state.underroadList;
        }
    },
    mutations: {
        addUnderroad(state, payload) {
            state.underroadList.push(payload);
        }
    },
    actions: {
        async fetchUnderroads(context, payload) {
            if (this.state.role == 'pmanager') {
                await http
                    .get('/facilities/pmanager/roads')
                    .then((res) => {
                    res.data.forEach((element) => {
                        context.commit('addUnderroad', element);
                        console.log(res.data);
                    });
                });
            }
            else {
                await http.get('/facilities/roads').then((res) => {
                    res.data.forEach((element) => {
                        context.commit('addUnderroad', element);
                    });
                });
            }
        }
    },
    modules: {}
});
//# sourceMappingURL=index.js.map