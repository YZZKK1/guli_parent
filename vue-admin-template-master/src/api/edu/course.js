import request from "@/utils/request";
import { get } from "mongoose";
export default {
    addCourseInfo(courseInfo){
        return request({
            url: `/eduservice/course/addCourseInfo`,
            method: 'post',
            data:courseInfo
          })},
    getListTeacher(){
        return request({
            url: `/eduservice/teacher/findAll`,
            method: 'get',
          })},
    getCourseInfoId(id){
        return request({
            url:"/eduservice/course/getCourseInfo/"+id,
            method:"get"
        })
    },

    updateCourseInfo(courseInfo){
        return request({
            url:"/eduservice/course/updateCourseInfo/",
            method:"post",
            data:courseInfo
        })
    },
    getPublicCourseInfo(id){
        return request({
            url:"/eduservice/course/getPublishCourseInfo/"+id,
            method:"get"
            
        })

    },
    PublicCourseInfo(id){
        return request({
            url:"/eduservice/course/publishCourse/"+id,
            method:"post"
            
        })

    },
    getListCourse(){
        return request({
            url:"/eduservice/course/",
            method:"get"
        })
    },
    deleteCourse(courseId){

        return request({
            url:"/eduservice/course/"+courseId,
            method:"delete",
            
        })
    },



    }      
          
          
          
          
       





    






