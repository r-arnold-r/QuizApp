package com.example.quizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: QuestionFragmentArgs by navArgs()

    companion object
    {
        fun newInstance(): QuizStartFragment
        {
            return QuizStartFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel: MainActivityViewModel by activityViewModels()

        initializeScore(viewModel)
        backButtonPressedHandler(viewModel)

        startQuiz(view, viewModel)

        return view
    }

    private fun backButtonPressedHandler(viewModel: MainActivityViewModel)
    {
        requireActivity().onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        buildDialog(viewModel)
                    }
                })
    }

    private fun initializeScore(viewModel: MainActivityViewModel)
    {
        viewModel.maxScore = 0
        viewModel.achievedScore = 0
    }

    private fun buildDialog(viewModel: MainActivityViewModel)
    {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit")
        builder.setMessage("Are you sure you want to end this quiz?")

        builder.setPositiveButton("OK") { _, _ ->
            navigateToEndFragment(viewModel)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun navigateToEndFragment(viewModel: MainActivityViewModel)
    {
        val action = QuestionFragmentDirections.actionQuestionFragmentToQuizEndFragment(viewModel.userName)
        action.achievedPoints = viewModel.achievedScore
        action.maxPoints = viewModel.maxScore
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(action)
    }

    private fun startQuiz(view: View, viewModel: MainActivityViewModel)
    {
        val quizController = QuizController(
            view,
            viewModel,
            binding.nextBtn,
            binding.questionTextView,
            binding.radioLinearLayout,
            args.userName
        )

        if(!quizController.doQuiz())
        {
            navigateToEndFragment(viewModel)
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

}


