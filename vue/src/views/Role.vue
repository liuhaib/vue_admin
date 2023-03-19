<template>
	<div>

		<div style="margin: 10px 0">
		  <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
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
		</div>
		
		<el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
		  <el-table-column type="selection" width="55"></el-table-column>
		  <el-table-column prop="id" label="ID"></el-table-column>
		  <el-table-column prop="name" label="用户名"></el-table-column>
		  <el-table-column prop="description" label="描述"></el-table-column>
		  <el-table-column prop="flag" label="唯一标识"></el-table-column>
		  <el-table-column label="操作"  width="280" align="center">
		    <template slot-scope="scope">
			  <el-button type="info" slot="reference" @click="SelectMenu(scope.row)">分配菜单 <i class="el-icon-menu"></i></el-button>
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
		
		<el-dialog title="菜单分配" :visible.sync="MenuDialogVis" width="30%" >
		  <el-form label-width="80px" size="small">
			<el-tree
			    :data="menudata"
			    :props="props"
			    show-checkbox
			    ref="tree"
			    node-key="id"
			   :default-expanded-keys="expanded"  
			   :default-checked-keys="checked">
			  <span class="custom-tree-node" slot-scope="{ node, data }">
			    <span><i :class="data.icon"/>{{ data.name }}</span>
			  </span>
			</el-tree>
		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="MenuDialogVis = false">取 消</el-button>
		    <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
		  </div>
		</el-dialog>
		
		<el-dialog title="用户信息新增" :visible.sync="dialogFormVisible" width="30%" >
		  <el-form label-width="80px" size="small">
		    <el-form-item label="用户名">
		      <el-input v-model="form.name" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="描述">
		      <el-input v-model="form.description" autocomplete="off"></el-input>
		    </el-form-item>
			<el-form-item label="唯一标识">
			  <el-input v-model="form.flag" autocomplete="off"></el-input>
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
		      <el-input v-model="form.name" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="描述">
		      <el-input v-model="form.description" autocomplete="off"></el-input>
		    </el-form-item>
			<el-form-item label="唯一标识">
			  <el-input v-model="form.flag" autocomplete="off"></el-input>
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
		name:"Role",
		data(){
			return {
				tableData: [],
				total: 0,
				pageNum: 1,
				pageSize: 10,
				name: "",
				form: {},
				dialogFormVisible: false,
				MenuDialogVis: false,
				dialogFormVisible2: false,
				multipleSelection: [],
				menudata:[],
				props: {
				  label: 'name'
				},
				expanded:[],
				checked:[],
				roleId:0,
				roleFlag:''
			}
		},
		created() {
		  // 请求分页查询数据
		  this.load()
		},
		methods: {
			load() {  // 分页查询数据
			  this.request.get("/role/page", {
			    params: {
			      pageNum: this.pageNum,
			      pageSize: this.pageSize,
			      name: this.name
			    }
			  }).then(res => {
			    this.tableData = res.data.records
			    this.total = res.data.total
			  })
			},
			save() {  // 新增用户数据
			  this.request.post("/role/add", this.form).then(res => {
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
			  this.request.post("/role/update", this.form).then(res => {
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
			  this.request.delete("/role/delete/" + id).then(res => {
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
			  this.request.post("/role/del/batch", ids).then(res => {
			    if (res.success) {
			      this.$message.success("批量删除成功")
			      this.load()
			    } else {
			      this.$message.error(res.errorMsg)
			    }
			  })
			},
			reset() {  //清空搜索栏
			  this.name = ""
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
			SelectMenu(role){
				this.MenuDialogVis = true;
				this.roleId = role.id;
				this.roleFlag = role.flag;
				this.request.get("/menu/all").then(res => {
				  this.menudata = res.data
				  this.expanded = this.menudata.map(v => v.id)
				});
				this.request.get("/role/roleMenu/"+this.roleId).then(res => {
				  this.checked=res.data
				});
				this.request.get("/menu/ids").then(res=>{
					const ids = res.data;
					ids.forEach(id=>{
						if(!this.checked.includes(id)){
							this.$refs.tree.setChecked(id,false);
						}
					})
				})
				
			},
			saveRoleMenu(){
				this.request.post("/role/roleMenu/"+this.roleId,this.$refs.tree.getCheckedKeys()).then(res => {
				  if (res.success) {
				    this.$message.success("保存成功")
					this.MenuDialogVis=false 
					if(this.roleFlag === "ROLE_ADMIN"){
						this.$store.commit("logout");
					}
				  } else {
				    this.$message.error(res.errorMsg)
				  }
				})
			}
		}
	}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>
