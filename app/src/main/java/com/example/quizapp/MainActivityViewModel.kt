package com.example.quizapp

import androidx.lifecycle.ViewModel
import com.example.quizapp.model.Question

class MainActivityViewModel : ViewModel()
{
    var userName : String = ""
    var maxScore : Int = 0
    var achievedScore : Int = 0
    var highScore : Int = 0
    lateinit var questions : MutableList<Question>

    fun addToScore()
    {
        achievedScore++
    }

    fun updateHighScore(newScore : Int)
    {
        if(newScore > highScore)
        {
            highScore = newScore
        }
    }

    fun initQuestions(newQuestions : MutableList<Question>)
    {
        questions = newQuestions
    }

    fun addQuestion(question : Question)
    {
        questions.add(question)
    }

    fun appendToQuestions(appendableQuestions : MutableList<Question>){
        questions.addAll(appendableQuestions)
    }
}