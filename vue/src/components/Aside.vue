<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#f4f4f5"
           active-text-color="#409eff"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/img/logo.png" alt="" style="width: 20px; position: relative; top: 5px;">
      <b style="color: white" v-show="logoTextShow">后台管理系统</b>
    </div>
	<div v-for="item in menus" :key="item.id">
	      <div v-if="item.path">
	        <el-menu-item :index="item.path">
	          <i :class="item.icon"></i>
	          <span slot="title">{{ item.name }}</span>
	        </el-menu-item>
	      </div>
	      <div v-else>
	        <el-submenu :index="item.id + ''">
	          <template slot="title">
	            <i :class="item.icon"></i>
	            <span slot="title">{{ item.name }}</span>
	          </template>
	          <div  v-for="subItem in item.children" :key="subItem.id">
	            <el-menu-item :index="subItem.path">
	              <i :class="subItem.icon"></i>
	              <span slot="title">{{ subItem.name }}</span>
	            </el-menu-item>
	          </div>
	        </el-submenu>
	      </div>
		</div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean,
  },
  data(){
	  return{
		  menus:localStorage.getItem("menus")?JSON.parse(localStorage.getItem("menus")):[]
	  }
  }
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: rgb(38, 52, 69) !important;
}
.el-menu-item:hover {
  background-color: rgb(38, 52, 69) !important;
}

.el-submenu__title:hover {
  background-color: rgb(38, 52, 69) !important;
}
/*解决收缩菜单文字不消失问题*/
.el-menu--collapse span {
  visibility: hidden;
}
</style>