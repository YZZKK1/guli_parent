import request from "@/utils/request";
export default {
    getAllChapterVideo(courseId){
        return request({
            url: "/eduservice/chapter/getChapterVideo/"+courseId,
            method: "get",
          });

    },
    addVideo(video){

        return request({
            url:"/eduservice/video/addVideo",
            method:"post",
            data:video
        })
    },
    getChapter(chapterId){

        return request({
            url:"/eduservice/chapter/getChapterInfo/"+chapterId,
            method:"get",
           
        })
    },
    updateChapter(chapter){

        return request({
            url:"/eduservice/chapter/updateChapter",
            method:"post",
            data:chapter
        })
    },
    deleteVideo(videoId){

        return request({
            url:"/eduservice/video/"+videoId,
            method:"delete",
            
        })
    },
    deleteAliyunvod(id){

        return request({
            url:"/eduvod/video/removeAlyVideo/"+id,
            method:"delete",
            
        })
    },








}