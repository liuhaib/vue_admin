<template>
	<div>
	<el-row :gutter="10" style="margin-bottom: 3.75rem;">
	    <el-col :span="6">
			<el-card shadow="always" style="color: #409EFF;">
			   <div><i class="el-icon-user-solid"/>用户数量:</div>
			   <div style="padding: 0.625rem 0; text-align: center; font-weight: bold;">11582</div>
		   </el-card>
		</el-col>
		<el-col :span="6">
		   <el-card shadow="always" style="color: #909399;">
			   <div><i class="el-icon-s-finance"/>成交金额:</div>
			   <div style="padding: 0.625rem 0; text-align: center; font-weight: bold;">￥11582</div>
		   </el-card>
		</el-col>
		<el-col :span="6">
		  <el-card shadow="always" style="color: #F56C6C;">
			  <div><i class="el-icon-s-marketing"/>销售数量:</div>
			  <div style="padding: 0.625rem 0; text-align: center; font-weight: bold;">11582</div>
		  </el-card>
		</el-col>
		<el-col :span="6">
		   <el-card shadow="always" style="color: #E6A23C;">
			   <div><i class="el-icon-s-goods"/>交易数量:</div>
			   <div style="padding: 0.625rem 0; text-align: center; font-weight: bold;">11582</div>
		   </el-card>
		</el-col>
	</el-row>
	<el-row>
	    <el-col :span="12">
	       <div id="main" style="width: 31.25rem; height: 25rem"></div>
		</el-col>
		<el-col :span="12">
		   <div id="pie" style="width: 31.25rem; height: 25rem"></div>
		</el-col>
	</el-row>
	</div>
</template>

<script>
	import * as echarts from 'echarts'
	
	export default {
	  name: "Home",
	  data(){
		  return{
		  }
	  },
	  mounted(){ //页面元素加载好 在赋值
		 var chartDom = document.getElementById('main');
		 var myChart = echarts.init(chartDom);
		 var option;
		 
		 var piechartDom = document.getElementById('pie');
		 var piemyChart = echarts.init(piechartDom);
		 var pieOption;
		 
		 //折线图
		 option = {
		   xAxis: {
		     type: 'category',
		     data: []
		   },
		   yAxis: {
		     type: 'value'
		   },
		   series: [
		     {
		       data: [],
		       type: 'line'
		     }
		   ]
		 };
		 //饼图
		 pieOption = {
		   title: {
		     text: '各季度会员数量统计',
		     subtext: '比例图',
		     left: 'center'
		   },
		   tooltip: {
		     trigger: 'item'
		   },
		   legend: {
		     orient: 'vertical',
		     left: 'left'
		   },
		   series: [
		     {
		       type: 'pie',
		       radius: '60%',
		       label:{            //饼图图形上的文本标签
		         normal:{
		           show:true,
		           position:'inner', //标签的位置
		           textStyle : {
		             fontWeight : 300 ,
		             fontSize : 14,    //文字的字体大小
		             color: "#fff"
		           },
		           formatter:'{d}%'
		         }
		       },
		       data: [],  // 填空
		       emphasis: {
		         itemStyle: {
		           shadowBlur: 10,
		           shadowOffsetX: 0,
		           shadowColor: 'rgba(0, 0, 0, 0.5)'
		         }
		       }
		     }
		   ]
		 };
		 
		 this.request.get("/echarts/get").then(res => {
		    option.xAxis.data=res.data.x
			option.series[0].data=res.data.y
		 	myChart.setOption(option);
		 }),
		 this.request.get("/echarts/members").then(res => {
		     pieOption.series[0].data = [
		         {name: "第一季度", value: res.data[0]},
		         {name: "第二季度", value: res.data[1]},
		         {name: "第三季度", value: res.data[2]},
		         {name: "第四季度", value: res.data[3]},
		     ]
		     piemyChart.setOption(pieOption)
		 })
	  }
	}
</script>

<style>
</style>
