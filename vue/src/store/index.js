import Vue from 'vue'
import Vuex from 'vuex'
import router,{RestRouter} from '@/router'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        currentPathName: ''
    },
    mutations: {
        setPath (state) {
            state.currentPathName = localStorage.getItem("currentPathName");
        },
		logout(){
			//清空缓存
			localStorage.removeItem("user");
			localStorage.removeItem("menus");
			//回到登入页面
			router.push("/login");
			//重置路由
			RestRouter();
		}
    }
})

export default store