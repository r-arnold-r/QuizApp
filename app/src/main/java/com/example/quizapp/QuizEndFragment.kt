package com.example.quizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizapp.databinding.FragmentQuizEndBinding

class QuizEndFragment : Fragment() {

    private var _binding: FragmentQuizEndBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: QuizEndFragmentArgs by navArgs()

    companion object
    {
        fun newInstance(): QuizEndFragment
        {
            return QuizEndFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState:
                              Bundle?): View
    {
        _binding = FragmentQuizEndBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModel: MainActivityViewModel by activityViewModels()
        viewModel.updateHighScore(args.achievedPoints)

        backButtonPressedHandler(view)

        pointTextViewHandler()
        tryAgainBtnHandler(view)

        return view
    }

    private fun backButtonPressedHandler(view : View)
    {
        requireActivity().onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val action = QuizEndFragmentDirections.actionQuizEndFragmentToHomeFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                })
    }

    private fun pointTextViewHandler()
    {
        binding.pointTextView.text = args.achievedPoints.toString() + "/" + args.maxPoints.toString()
    }

    private fun tryAgainBtnHandler(view: View)
    {
        binding.tryAgainBtn.setOnClickListener()
        {
            val action = QuizEndFragmentDirections.actionQuizEndFragmentToQuestionFragment(args.userName)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

}