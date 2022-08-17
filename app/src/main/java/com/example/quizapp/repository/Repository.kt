package com.example.quizapp.repository
import com.example.quizapp.api.RetrofitInstance
import com.example.quizapp.model.Post
import retrofit2.Response

class Repository {

    suspend fun getPost(): Response<Post> {
        return RetrofitInstance.api.getPost()
    }

}