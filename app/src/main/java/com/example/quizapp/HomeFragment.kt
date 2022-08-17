package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.quizapp.databinding.FragmentHomeBinding
import com.example.quizapp.databinding.FragmentQuizStartBinding
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object
    {
        fun newInstance(): HomeFragment
        {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        testYourSkillsBtnHandler(view)
        readQuestionsBtnHandler(view)
        createQuestionBtnHandler(view)
        backButtonPressedHandler(view)

        return view
    }

    private fun createQuestionBtnHandler(view: View)
    {
        binding.createQuestionBtn.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToQuestionAddFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun testYourSkillsBtnHandler(view : View)
    {
        binding.testYourSkillsBtn.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToQuizStartFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun readQuestionsBtnHandler(view : View)
    {
        binding.readQuestionsBtn.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToQuestionListFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }


    private fun backButtonPressedHandler(view: View)
    {
        var backButtonCount = 0
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed()
                {
                    if (backButtonCount >= 1)
                    {
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addCategory(Intent.CATEGORY_HOME)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    else
                    {
                        Snackbar.make(view, "Press the back button once again to close the application.", Snackbar.LENGTH_SHORT).show()
                        backButtonCount++
                    }
                }
            })
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}
