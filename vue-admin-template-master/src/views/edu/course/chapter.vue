<template>
    <div class="app-container">
      <h2 style="text-align: center;">发布新课程</h2>
      <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
        <el-step title="填写课程基本信息"/>
        <el-step title="创建课程大纲"/>
        <el-step title="提交审核"/>
      </el-steps>
    
        
<el-button type="text" @click="opeChapterDialog()">添加章节</el-button>
<!-- 章节 -->
<ul class="chanpterList">
    <li
        v-for="chapter in chapterVideoList"
        :key="chapter.id">
        <p>
            {{ chapter.title }}
            <span class="acts">
                <el-button type="text" @click="openVideo(chapter.id)">添加小节</el-button>
                <el-button style="" type="text" @click="openEditChapter(chapter.id)">编辑</el-button>
                <el-button type="text" @click="removeChapter(chapter.id)">删除</el-button>
            </span>
        </p>
        <!-- 视频 -->
        <ul class="chanpterList videoList">
            <li
                v-for="video in chapter.children"
                :key="video.id">
                <p>{{ video.title }}
                    <span class="acts">
  
                        <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
                    </span>
                </p>
            </li>
        </ul>
    </li>
</ul>
<div>
    <el-button @click="previous">上一步</el-button>
    <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    
</div>
<el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
    <el-form :model="chapter" label-width="120px">
        <el-form-item label="章节标题">
            <el-input v-model="chapter.title"/>
        </el-form-item>
        <el-form-item label="章节排序">
            <el-input-number v-model="chapter.sort" :min="0" controls-position="right"/>
        </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
    </div>
</el-dialog>
<el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
  <el-form :model="video" label-width="120px">
    <el-form-item label="课时标题">
      <el-input v-model="video.title"/>
    </el-form-item>
    <el-form-item label="课时排序">
      <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
    </el-form-item>
    <el-form-item label="是否免费">
      <el-radio-group v-model="video.free">
        <el-radio :label="true">免费</el-radio>
        <el-radio :label="false">默认</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="上传视频">
    <el-upload
           :on-success="handleVodUploadSuccess"
           :on-remove="handleVodRemove"
           :before-remove="beforeVodRemove"
           :on-exceed="handleUploadExceed"
           :file-list="fileList"
           :action="BASE_API+'/eduvod/video/uploadAlyiVideo'"
           :limit="1"
           class="upload-demo">
    <el-button size="small" type="primary">上传视频</el-button>
    <el-tooltip placement="right-end">
        <div slot="content">最大支持1G，<br>
            支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
            GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
            MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
            SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
        <i class="el-icon-question"/>
    </el-tooltip>
    </el-upload>
</el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
    <el-button :disabled="saveVideoBtnDisabled" type="primary" @click="saveOrUpdateVideo">确 定</el-button>
  </div>
</el-dialog>

    </div>
  </template>
  <script>





import chapter from '../../../api/edu/chapter'
import video from "../../../api/edu/video"

  export default {
    data() {
      return {
        saveBtnDisabled: false, // 保存按钮是否禁用。
        courseId:"",
        chapterVideoList:[],
        dialogChapterFormVisible:false,
        dialogVideoFormVisible:false,
        chapter:{
          title:"",
          sort:0
          
        },
        video:{
          title:"",
          sort:0,
          free:0,
          videoSourceId:""
        },
        fileList:[],
        BASE_API:process.env.BASE_API
      }
    },
    created() {
      if(this.$route.params&&this.$route.params.id){
        this.courseId=this.$route.params.id
        this.getChapterVideo()
      }
      this.getChapterVideo()
    },
    methods: {
      handleVodRemove(file, fileList){
        video.deleteAliyunvod(this.video.videoSourceId).then(
          response=>{
            this.$message({
            type: 'success',
            message: '删除视频成功!'
        });
        this.fileList=[]
        this.video.videoSourceId=" "
        this.video.videoOriginalName=" "


          }
        )
        


      },
      beforeVodRemove(file, fileList){
        return this.$confirm(`确定移除 ${file.name}？`)
      },
      handleVodUploadSuccess(response, file, fileList){

        this.video.videoSourceId=response.data.videoId
      },

      handleUploadExceed(){this.$message.warning('想要重新上传视频，请先删除已上传的视频')},










      removeVideo(id){
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
       video.deleteVideo(id)
    .then(response => {
        this.$message({
            type: 'success',
            message: '删除成功!'
        })
        this.getChapterVideo()
      
      })
      })



      },


      openVideo(chapterId){
        this.dialogVideoFormVisible=true
        this.video.chapterId=chapterId


      },
      addVideo(){
        this.video.courseId=this.courseId
        video.addVideo(this.video).then(response=>{
          this.dialogVideoFormVisible=false
          this.$message({
          type: "success",
          message: "增加小节信息成功！"
        });
          this.getChapterVideo()

        })
      },

      saveOrUpdateVideo(){
        this.addVideo()



      },
      removeChapter(chapterId){
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
       chapter.deleteChapter(chapterId)
    .then(response => {
        this.$message({
            type: 'success',
            message: '删除成功!'
        })
        this.getChapterVideo()
      
      })
      })
      
      
      
      
      },

      updateChapter(){
        chapter.updateChapter(this.chapter).then(
          response=>{
          this.dialogChapterFormVisible=false
          this.$message({
          type: "success",
          message: "修改章节信息成功！"
        });
          this.getChapterVideo()

          }
        )
      },





      openEditChapter(chapterId){
        this.dialogChapterFormVisible=true
        chapter.getChapter(chapterId).then(
          response=>{
            this.chapter=response.data.chapter

          }
        )



      },
      opeChapterDialog(){
        this.dialogChapterFormVisible=true
        this.chapter.title=""
        this.chapter.sort=0
        this.chapterId=""


      },
      addChapter(){
        this.chapter.courseId=this.courseId
        chapter.addChapter(this.chapter).then(response=>{
          this.dialogChapterFormVisible=false
          this.$message({
          type: "success",
          message: "添加章节信息成功！"
        });
          this.getChapterVideo()
        })


      },

      saveOrUpdate(){
        if(!this.chapter.id){this.addChapter()}
        else{this.updateChapter()}
       
      },
      getChapterVideo(){
        chapter.getAllChapterVideo(this.courseId)
        .then(response=>{
        console.log(response.data.allChapterVideo)
          this.chapterVideoList=response.data.allChapterVideo
        })

      },
      previous() {
        console.log('previous')
        this.$router.push({ path: '/course/info/'+this.courseId })
      },
      next() {
        console.log('next')
        this.$router.push({ path: '/course/publish/'+this.courseId })
      }
    }
  }
  </script>
  