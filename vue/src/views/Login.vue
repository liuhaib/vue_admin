<template>
     <div class="wrapper">
       <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
          <div style="margin: 20px 0; text-align: center; font-size: 1.5625rem"><b>后台管理系统登入</b></div>
             <el-form :model="user" :rules="rules" ref="userForm">
			  <el-form-item prop="username">
				<el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
			  </el-form-item>
			  <el-form-item prop="password">
				<el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
			  </el-form-item>
			  <el-form-item style="margin: 10px 0;text-align: center;">
				<el-button type="primary" size="small"  autocomplete="off" @click="login">登录</el-button>
				<el-button type="warning" size="small"  autocomplete="off"  text-align: right @click="$router.push('/register')">注册</el-button>
				<el-button size="small" @click="resetForm('userForm')">重置</el-button>
			  </el-form-item>
			</el-form>
        </div>
      </div>
</template>

<script>
import {setRoutes}	from "@/router"
	
export default {
	name: "Login",
	data() {
		return{
			user:{},
			rules: {
				username: [
				  { required: true, message: '请输入用户名', trigger: 'blur' },
				  { min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
				],
				password: [
				  { required: true, message: '请输入密码', trigger: 'blur' },
				  { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
				],
			  }
		}
	 },
	methods:{
		login(){//登入方法
			this.$refs['userForm'].validate((valid) => { //表单验证成功
			  if (valid) {
				this.request.post("/admin/login",this.user).then(res => { //发送请求
				  if (res.success) {
					this.$router.push("/");
					this.$message.success("登入成功!");
					localStorage.setItem("user",JSON.stringify(res.data));//储存用户信息到浏览器
					localStorage.setItem("menus",JSON.stringify(res.data.menus));//储存用户菜单信息到浏览器
					setRoutes();
				  } else {  //验证失败不请求
					this.$message.error(res.errorMsg)
				  }
				})
			  } else {
				return false;
			  }
			});
		},
		resetForm(formName) { //重置按钮
        this.$refs[formName].resetFields();
      }
	}
}

</script>

<style scoped>
.wrapper {
    height: 100vh;
    background-image: linear-gradient( 135deg, #92FFC0 10%, #002661 100%);
    overflow: hidden;
  }
</style>
