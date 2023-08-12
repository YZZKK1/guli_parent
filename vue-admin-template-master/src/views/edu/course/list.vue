<template>
    <div class="app-container">
  
       <el-form :inline="true" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="courseQery.name" placeholder="课程名称"/>
        </el-form-item>
        <el-form-item>
          <el-select v-model="courseQery.level" clearable placeholder="课程状态">
            <el-option :value="Normal" label="已发布"/>
            <el-option :value="Draft" label="未发布"/>
          </el-select>
        </el-form-item>
        <el-form-item label="添加时间">
          <el-date-picker
            v-model="courseQery.begin"
            type="datetime"
            placeholder="选择开始时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            default-time="00:00:00"
          />
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="courseQery.end"
            type="datetime"
            placeholder="选择截止时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            default-time="00:00:00"
          />
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form>
  
      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="数据加载中"
        border
        fit
        highlight-current-row>
        <el-table-column
          label="序号"
          width="70"
          align="center">
          <template slot-scope="scope">
            {{ (page - 1) * limit + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="title" label="课程名称" width="80" />
        <el-table-column label="课程状态" width="80">
          <template slot-scope="scope">
            {{ scope.row.status==="Normal"?'已发布':'未发布' }}
          </template>
        </el-table-column>
        <el-table-column prop="lessonNum" label="课时数" />
        <el-table-column prop="gmtCreate" label="添加时间" width="160"/>
        <el-table-column prop="viewCount" label="浏览数量" width="60" />
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <router-link :to="'/teacher/edit/'+scope.row.id">
              <el-button style="margin-bottom: 5px;" type="primary"  size="mini" icon="el-icon-edit">编辑课程信息</el-button>
            </router-link>
            <router-link :to="'/teacher/edit/'+scope.row.id">
              <el-button style="margin-bottom: 5px;" type="primary" size="mini" icon="el-icon-edit">编辑课程大纲</el-button>
            </router-link>
            <el-button style="margin-bottom: 5px;" type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

    </div>
  </template>
    <script>
  import course from '@/api/edu/course'

  export default{
      data(){
        return{
          list:null,
          page:1,
          limit:5,
          total:0,
          courseQery:{}//属性可以不定义 会自动加到里面
        }
  
      },
      created(){
        this.getList()
      },
      methods:{
        getList(){
          
          course.getListCourse()
          .then(response=>{
            this.list=response.data.list
            console.log(this.list)
            
          
          
          
          
          })
          .catch(error=>{console.log(error)})
        },
        resetData(){
          this.courseQery={}
          this.getList()
  
  
        },
        removeDataById(courseId){
            course.deleteCourse(courseId).then(response=>{
                this.$message({
                type: 'success',
                message: '删除成功!'
        })
        this.getList()



            })
        }
    
  
  
  }
  }
  
  </script>
  