package com.example.quizapp.model

data class Post(
    val response_code : Int,
    val results : List<Question>
)