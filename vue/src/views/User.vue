<template>
	<div>

		<div style="margin: 10px 0">
		  <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
		  <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
		  <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>
		  <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
		  <el-button type="warning" @click="reset">重置</el-button>
		</div>
		
		<div style="margin: 10px 0">
		  <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
		  <el-popconfirm
		      class="ml-5"
		      confirm-button-text='确定'
		      cancel-button-text='我再想想'
		      icon="el-icon-info"
		      icon-color="red"
		      title="您确定批量删除这些数据吗？"
		      @confirm="delBatch"
		  >
		    <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
		  </el-popconfirm>
		  <el-upload action="http://localhost:8081/user/import" :headers="hand" :show-file-list="false" accept="xlsx" :on-success="(res) => {return handleExcelImportSuccess(res);}" style="display: inline-block">
		    <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
		  </el-upload>
		  <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
		</div>
		
		<el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
		  <el-table-column type="selection" width="55"></el-table-column>
		  <el-table-column prop="id" label="ID" width="80"></el-table-column>
		  <el-table-column prop="username" label="用户名" width="140"></el-table-column>
		  <el-table-column prop="role" label="角色"></el-table-column>
		  <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
		  <el-table-column prop="email" label="邮箱"></el-table-column>
		  <el-table-column prop="phone" label="电话"></el-table-column>
		  <el-table-column prop="address" label="地址"></el-table-column>
		  <el-table-column label="操作"  width="200" align="center">
		    <template slot-scope="scope">
		      <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
		      <el-popconfirm
		          class="ml-5"
		          confirm-button-text='确定'
		          cancel-button-text='我再想想'
		          icon="el-icon-info"
		          icon-color="red"
		          title="您确定删除吗？"
		          @confirm="del(scope.row.id)"
		      >
		        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
		      </el-popconfirm>
		    </template>
		  </el-table-column>
		</el-table>
		<div style="padding: 10px 0">
		  <el-pagination
		      @size-change="handleSizeChange"
		      @current-change="handleCurrentChange"
		      :current-page="pageNum"
		      :page-sizes="[10, 15, 20]"
		      :page-size="pageSize"
		      layout="total, sizes, prev, pager, next, jumper"
		      :total="total">
		  </el-pagination>
		</div>
		
		<el-dialog title="用户信息新增" :visible.sync="dialogFormVisible" width="30%" >
		  <el-form label-width="80px" size="small">
		    <el-form-item label="用户名">
		      <el-input v-model="form.username" autocomplete="off"></el-input>
		    </el-form-item>
			<el-form-item label="角色">
				<el-select clearable v-model="form.role"  placeholder="请选择角色" style="width: 100%;">
					  <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
				</el-select>
			</el-form-item>
		    <el-form-item label="昵称">
		      <el-input v-model="form.nickname" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="邮箱">
		      <el-input v-model="form.email" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="电话">
		      <el-input v-model="form.phone" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="地址">
		      <el-input v-model="form.address" autocomplete="off"></el-input>
		    </el-form-item>
		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="dialogFormVisible = false">取 消</el-button>
		    <el-button type="primary" @click="save">确 定</el-button>
		  </div>
		</el-dialog>
		
		<el-dialog title="用户信息修改" :visible.sync="dialogFormVisible2" width="30%" >
		  <el-form label-width="80px" size="small">
		    <el-form-item label="用户名">
		      <el-input v-model="form.username" autocomplete="off"></el-input>
		    </el-form-item>
			<el-form-item label="角色">
				<el-select clearable v-model="form.role"  placeholder="请选择角色" style="width: 100%;">
					  <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
				</el-select>
			</el-form-item>
		    <el-form-item label="昵称">
		      <el-input v-model="form.nickname" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="邮箱">
		      <el-input v-model="form.email" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="电话">
		      <el-input v-model="form.phone" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="地址">
		      <el-input v-model="form.address" autocomplete="off"></el-input>
		    </el-form-item>
		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="dialogFormVisible2 = false">取 消</el-button>
		    <el-button type="primary" @click="update">确 定</el-button>
		  </div>
		</el-dialog>
	</div>
</template>

<script>
	export default{
		name:"User",
		data(){
			return {
				tableData: [],
				total: 0,
				pageNum: 1,
				pageSize: 10,
				username: "",
				email: "",
				address: "",
				form: {},
				dialogFormVisible: false,
				dialogFormVisible2: false,
				multipleSelection: [],
				hand:{
						token:null
				},
				roles:[]
			}
		},
		created() {
		  // 请求分页查询数据
		  this.load()
		  //获取请求头
		  let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
		  if(user){
		  	this.hand.token= user.token;  // 设置请求头
		  }
		},
		methods: {
			load() {  // 分页查询数据
			  this.request.get("/user/page", {
			    params: {
			      pageNum: this.pageNum,
			      pageSize: this.pageSize,
			      username: this.username,
			      email: this.email,
			      address: this.address,
			    }
			  }).then(res => {
			    this.tableData = res.data.records
			    this.total = res.data.total
			  });
			  
			  this.request.get("/role/getAll").then(res => {
					this.roles = res.data
			  })
			  
			},
			save() {  // 新增用户数据
			  this.request.post("/user/add", this.form).then(res => {
			    if (res.success) {
			      this.$message.success("保存成功")
			      this.dialogFormVisible = false
			      this.load()
			    } else {
			      this.$message.error(res.errorMsg)
				  this.dialogFormVisible = false
			    }
			  })
			},
			update() { //修改用户数据
			  this.request.post("/user/update", this.form).then(res => {
			    if (res.success) {
			      this.$message.success("修改成功")
			      this.dialogFormVisible2 = false
			      this.load()
			    } else {
			      this.$message.error(res.errorMsg)
				  this.dialogFormVisible2 = false
			    }
			  })
			},
			handleAdd() { //打开 新增窗口
			  this.dialogFormVisible = true
			  this.form = {}
			},
			handleEdit(row) { //打开 修改窗口
			  this.dialogFormVisible2 = true
			  this.form = row
			},
			del(id) {  // 根据id删除
			  this.request.delete("/user/delete/" + id).then(res => {
			    if (res.success) {
			      this.$message.success("删除成功")
			      this.load()
			    } else {
			      this.$message.error(res.errorMsg)
			    }
			  })
			},
			handleSelectionChange(val) {
			  console.log(val)
			  this.multipleSelection = val
			},
			delBatch() { //批量删除
			  let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
			  this.request.post("/user/del/batch", ids).then(res => {
			    if (res.success) {
			      this.$message.success("批量删除成功")
			      this.load()
			    } else {
			      this.$message.error(res.errorMsg)
			    }
			  })
			},
			reset() {  //清空搜索栏
			  this.username = ""
			  this.email = ""
			  this.address = ""
			  this.load()
			},
			handleSizeChange(pageSize) { // 调整分页每个页面数据数
			  console.log(pageSize)
			  this.pageSize = pageSize
			  this.load()
			},
			handleCurrentChange(pageNum) { // 调整分页当前页
			  console.log(pageNum)
			  this.pageNum = pageNum
			  this.load()
			},
			exp() { //用户数据导出
				window.open("http://localhost:8081/user/export")
			},
			handleExcelImportSuccess(res) {//用户数据导入
			   if (res.success) {
			     this.$message.success("导入成功")
			     this.load()
			   } else {
			     this.$message.error(res.errorMsg)
			   }
			}
		}
	}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>
