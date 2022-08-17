package com.example.quizapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.quizapp.databinding.FragmentHomeBinding
import com.example.quizapp.databinding.FragmentQuestionAddBinding
import com.example.quizapp.model.Question
import com.google.android.material.snackbar.Snackbar

class QuestionAddFragment : Fragment() {

    private var _binding: FragmentQuestionAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object
    {
        fun newInstance(): QuestionAddFragment
        {
            return QuestionAddFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        _binding = FragmentQuestionAddBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModel: MainActivityViewModel by activityViewModels()

        addQuestionBtnHandler(view, viewModel)


        return view
    }

    private fun addQuestionBtnHandler(view : View, viewModel : MainActivityViewModel)
    {
        binding.addQuestionBtn.setOnClickListener{
            val question = binding.questionEt.text.toString()
            val correctAnswer = binding.correctAnswer.text.toString()
            val answer1 = binding.answer1.text.toString()
            val answer2 = binding.answer2.text.toString()
            val answer3 = binding.answer3.text.toString()

            val incorrectAnswers = mutableListOf<String>(answer1, answer2, answer3, correctAnswer)

            if(question != "" && answer1 != "" && answer2 != "" && answer3 != "" && correctAnswer != "")
            {
                val newQuestion = Question("","","", question, correctAnswer, incorrectAnswers)

                if(viewModel.questions.contains(newQuestion))
                {
                    Snackbar.make(it, "Question already exists!", Snackbar.LENGTH_SHORT).show()
                }
                else
                {
                    viewModel.addQuestion(newQuestion)
                    Snackbar.make(it, "Question added!", Snackbar.LENGTH_SHORT).show()
                    val action = QuestionAddFragmentDirections.actionQuestionAddFragmentToHomeFragment()
                    Navigation.findNavController(view).navigate(action)
                }

            }
            else
            {
                Snackbar.make(it, "Please complete the question!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

}