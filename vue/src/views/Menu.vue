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
		
		<el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  row-key="id" default-expand-all  @selection-change="handleSelectionChange">
		  <el-table-column type="selection" width="55"></el-table-column>
		  <el-table-column prop="id" label="ID"></el-table-column>
		  <el-table-column prop="name" label="名称"></el-table-column>
		  <el-table-column prop="path" label="路径"></el-table-column>
		  <el-table-column prop="icon" label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
			  <template slot-scope="scope">
				  <i :class="scope.row.icon"/>
			  </template>
		  </el-table-column>
		  <el-table-column prop="pagePath" label="页面路径"></el-table-column>
		  <el-table-column prop="description" label="描述"></el-table-column>
		  <el-table-column label="操作"  width="300" align="center">
		    <template slot-scope="scope">
			  <el-button type="primary" @click="addChildren(scope.row.id)" v-if="!scope.row.path">新增子菜单 <i class="el-icon-circle-plus-outline"></i></el-button>
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
		
		<el-dialog title="菜单信息新增" :visible.sync="dialogFormVisible" width="30%" >
		  <el-form label-width="80px" size="small">
		    <el-form-item label="名称">
		      <el-input v-model="form.name" autocomplete="off"></el-input>
		    </el-form-item>
			<el-form-item label="路径">
			  <el-input v-model="form.path" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="图标">
				<el-select v-model="form.icon" clearable placeholder="请选择">
					  <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
						  <i :class="item.value" /> {{item.name}}
					  </el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="页面路径">
			  <el-input v-model="form.pagePath" autocomplete="off"></el-input>
			</el-form-item>
		    <el-form-item label="描述">
		      <el-input v-model="form.description" autocomplete="off"></el-input>
		    </el-form-item>
		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="dialogFormVisible = false">取 消</el-button>
		    <el-button type="primary" @click="save">确 定</el-button>
		  </div>
		</el-dialog>
		
		<el-dialog title="菜单信息修改" :visible.sync="dialogFormVisible2" width="30%" >
		  <el-form label-width="80px" size="small">
		    <el-form-item label="名称">
		      <el-input v-model="form.name" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="路径">
		      <el-input v-model="form.path" autocomplete="off"></el-input>
		    </el-form-item>
		    <el-form-item label="图标">
		    	<el-select v-model="form.icon" clearable placeholder="请选择">
		    		  <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
		    			  <i :class="item.value" /> {{item.name}}
		    		  </el-option>
		    	</el-select>
		    </el-form-item>
			<el-form-item label="页面路径">
			  <el-input v-model="form.pagePath" autocomplete="off"></el-input>
			</el-form-item>
		    <el-form-item label="描述">
		      <el-input v-model="form.description" autocomplete="off"></el-input>
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
		name:"Menu",
		data(){
			return {
				tableData: [],
				name: "",
				form: {},
				dialogFormVisible: false,
				dialogFormVisible2: false,
				multipleSelection: [],
				options:[]
			}
		},
		created() {
		  // 请求分页查询数据
		  this.load()
		},
		methods: {
			load() {  // 分页查询数据
			  this.request.get("/menu/all", {
			    params: {
			      name: this.name
			    }
			  }).then(res => {
			    this.tableData = res.data
			  })
			},
			save() {  // 新增用户数据
			  this.request.post("/menu/add", this.form).then(res => {
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
			  this.request.post("/menu/update", this.form).then(res => {
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
			  this.request.get("/menu/typeIcon").then(res => {
			    this.options = res.data
			  })
			},
			addChildren(id){
				this.dialogFormVisible = true
				this.form = {}
				if(id){
					this.form.pid=id
				}
			},
			handleEdit(row) { //打开 修改窗口
			  this.dialogFormVisible2 = true
			  this.form = row
			  this.request.get("/menu/typeIcon").then(res => {
			    this.options = res.data
			  })
			},
			del(id) {  // 根据id删除
			  this.request.delete("/menu/delete/" + id).then(res => {
			    if (res.success) {
			      this.$message.success("删除成功")
			      this.load()
			    } else {
			      this.$message.error(res.errorMsg)
			    }
			  })
			},
			handleSelectionChange(val) {
			  this.multipleSelection = val
			},
			delBatch() { //批量删除
			  let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
			  this.request.post("/menu/del/batch", ids).then(res => {
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
			}
		}
	}
</script>

<style>
.headerBg {
  background: #eee!important;
}
.fontSize18{
	font-size: 1.125rem;
}
.fontSize12{
	font-size: 0.75rem;
}
</style>
