package com.example.quizapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.text.Html
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.quizapp.MainActivityViewModel
import com.example.quizapp.MainViewModelFactory
import com.example.quizapp.RetroViewModel
import com.example.quizapp.model.Question
import com.example.quizapp.repository.Repository

class PickContact : ActivityResultContract<Int, Uri?>()
{
    override fun createIntent(context: Context, input: Int?) =
            Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI).also{
                it.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri?
    {
        return if (resultCode == AppCompatActivity.RESULT_OK) intent?.data else null
    }
}

fun readQuestions(context : Context, viewModelStoreOwner: ViewModelStoreOwner, lifecycleOwner: LifecycleOwner, viewModel: MainActivityViewModel)
{
    val questions = getQuestions(context, viewModelStoreOwner, lifecycleOwner)

    viewModel.initQuestions(questions)
}

private fun getQuestions(context : Context, viewModelStoreOwner: ViewModelStoreOwner,lifecycleOwner: LifecycleOwner) :  MutableList<Question>
{
    val viewModelRetro : RetroViewModel

    val repository = Repository()
    val viewModelFactory = MainViewModelFactory(repository)
    viewModelRetro = ViewModelProvider(
        viewModelStoreOwner,
        viewModelFactory
    )[RetroViewModel::class.java]

    val questions : MutableList<Question> = mutableListOf()

    viewModelRetro.getPost()
    viewModelRetro.myResponse.observe(lifecycleOwner, Observer{ response ->
        if (response.isSuccessful) {
            response.body()?.results?.forEach{ it ->
                it.incorrect_answers.add(it.correct_answer)
                val incorrectAnswers : MutableList<String> = mutableListOf()
                it.incorrect_answers.forEach { ia ->
                    incorrectAnswers.add(ia.htmlDecode())
                }

                questions.add(Question(
                    it.category.htmlDecode(),
                    it.type.htmlDecode(),
                    it.difficulty.htmlDecode(),
                    it.question.htmlDecode(),
                    it.correct_answer.htmlDecode(),
                    incorrectAnswers))
            }

        } else {
            Log.d("Response", response.errorBody().toString())
        }

    })

    return questions
}

fun String.htmlDecode() : String = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

fun readMyQuestions(context : Context, viewModel: MainActivityViewModel)
{
    val questions: MutableList<Question> = mutableListOf()
    val stringLines: MutableList<String> = mutableListOf()
    context.assets.open("QuizzQuestions.txt").bufferedReader().forEachLine { stringLines.add(it) }

    var counter = 0
    var question = ""
    var correctAnswer: String = ""
    var type : String = ""
    var category : String = ""
    var answers: MutableList<String> = mutableListOf()

    //  read questions + answers from file
    for (i in 0 until stringLines.size) {
        when (counter) {
            0 -> {
                question = stringLines[i]
            }
            5 -> {
                correctAnswer = stringLines[i]
            }
            6 -> {
                category = stringLines[i]
            }
            7 -> {
                type = stringLines[i]

                questions.add(Question(category, type, "", question, correctAnswer, answers))
                answers = mutableListOf()
                counter = -1
            }
            else -> {
                answers.add(stringLines[i])
            }
        }

        counter++
    }

    viewModel.appendToQuestions(questions)
}