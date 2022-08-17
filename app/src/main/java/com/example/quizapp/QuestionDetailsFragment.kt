package com.example.quizapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.quizapp.databinding.FragmentQuestionDetailsBinding


class QuestionDetailsFragment() : Fragment() {

    private var _binding: FragmentQuestionDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: QuestionDetailsFragmentArgs by navArgs()

    companion object
    {
        fun newInstance(): QuestionDetailsFragment
        {
            return QuestionDetailsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:
        Bundle?
    ): View
    {
        _binding = FragmentQuestionDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        questionHandler(requireContext())

        return view
    }


    private fun questionHandler(context : Context)
    {
        val question = args.question

        val myTextViews = mutableListOf<TextView>() // create an empty array;

        binding.questionTv.text = question.question

        for(i in 0 until question.incorrect_answers.size)
        {
            // create a new textview
            val rowTextView : TextView = TextView(context)
            rowTextView.text = question.incorrect_answers[i]
            if(question.incorrect_answers[i] == question.correct_answer)
            {
                rowTextView.setTextColor(Color.BLUE)
            }
            binding.answersLL.addView(rowTextView)
        }


    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

}