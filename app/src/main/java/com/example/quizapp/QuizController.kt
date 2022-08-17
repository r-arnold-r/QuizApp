package com.example.quizapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.example.quizapp.model.Question
import com.google.android.material.snackbar.Snackbar
import java.net.URLDecoder
import java.util.*

class QuizController(
    private val view: View,
    private val viewModel: MainActivityViewModel,
    private val nextBtn: Button,
    private val questionTV: TextView,
    private val radioLinearLayout: LinearLayout,
    private val name: String
) {

    private var questions: MutableList<Question> = viewModel.questions.toMutableList()
    private var questionCounter = 0

    init
    {
        questions.shuffle()
        questions = questions.take(10).toMutableList()
    }

    fun doQuiz() : Boolean
    {
        if(questions.size > 0)
        {
            viewModel.maxScore = questions.size
            randomizeQuestions()

            val answersSize = questions[0].incorrect_answers.size

            // Create RadioGroup Dynamically
            val radioGroupId = (10000..99999).random()
            val radioGroup = createRadioGroup(radioGroupId)
            // Create RadioButtons Dynamically
            val radioButtons = mutableListOf<RadioButton>()

            var radioButtonId = (1200..9999).random()
            for(i in 0 until answersSize)
            {
                val radioButton = createRadioButton(radioButtonId)

                // add radioButtons to list
                radioButtons.add(radioButton)

                // add radioButtons to the radioGroup
                radioGroup.addView(radioButton)

                radioButtonId++
            }

            //adding button to the radio group container
            radioLinearLayout.addView(radioGroup)

            //set first text
            questionTV.text = questions[0].question

            //set first questions
            initializeRadioButtons(radioButtons, answersSize)

            questionCounter++

            nextBtn.setOnClickListener{
                val selectedRadioButtonId = radioGroup.checkedRadioButtonId
                if(selectedRadioButtonId != -1)
                {
                    if (questionCounter < questions.size)
                    {
                        //proceed to next question
                        val selectedRadioButton = view.findViewById<View>(selectedRadioButtonId) as RadioButton
                        updateQuestion(radioGroup, radioButtons, selectedRadioButton.text.toString())
                    }
                    else
                    {
                        //switch to end fragment
                        switchToEndFragment()
                    }

                    questionCounter++
                }
                else
                {
                    Snackbar.make(view, "Select an answer!", Snackbar.LENGTH_SHORT).show()
                }

            }
            return true
        }
        else{
            Snackbar.make(view, "Not enough questions!", Snackbar.LENGTH_SHORT).show()
            return false
        }


    }

    private fun createRadioGroup(radioGroupId: Int) : RadioGroup
    {
        val radioGroup = RadioGroup(view.context)
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        radioGroup.id = radioGroupId
        params.setMargins(25, 0, 25, 50)
        radioGroup.layoutParams = params

        return radioGroup
    }

    private fun createRadioButton(radioButtonId: Int) : RadioButton
    {
        val radioButton = RadioButton(view.context)
        radioButton.id = radioButtonId
        radioButton.textSize = 15f
        radioButton.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return radioButton
    }

    private fun initializeRadioButtons(radioButtons: MutableList<RadioButton>, answersSize: Int)
    {
        for(i in 0 until answersSize)
        {
            radioButtons[i].text = questions[0].incorrect_answers[i]
        }
    }

    private fun updateQuestion(radioGroup: RadioGroup, radioButtons: MutableList<RadioButton>, answer : String)
    {
        if(answer == questions[questionCounter - 1].correct_answer)
        {
            viewModel.addToScore()
        }

        questionTV.text = questions[questionCounter].question
        val answersSize = questions[questionCounter].incorrect_answers.size

        radioButtons.clear()
        radioGroup.removeAllViews()
        radioGroup.clearCheck()
        var radioButtonId = (1200..9999).random()
        for(i in 0 until answersSize)
        {
            val radioButton = createRadioButton(radioButtonId)

            // add radioButtons to list
            radioButtons.add(radioButton)

            // add radioButtons to the radioGroup
            radioGroup.addView(radioButton)

            radioButtonId++
        }

        for(i in 0 until answersSize) {
            radioButtons[i].text = questions[questionCounter].incorrect_answers[i]
        }
    }

    private fun switchToEndFragment()
    {
        val action = QuestionFragmentDirections.actionQuestionFragmentToQuizEndFragment(name)
        action.achievedPoints = viewModel.achievedScore
        action.maxPoints = viewModel.maxScore
        Navigation.findNavController(view).navigate(action)
    }

    private fun randomizeQuestions() {
        questions.shuffle()
        questions.forEach{
            it.incorrect_answers.shuffle()
        }
    }
}