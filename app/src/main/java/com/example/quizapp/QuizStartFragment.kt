package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.quizapp.databinding.FragmentQuizStartBinding
import com.example.quizapp.utils.PickContact
import com.google.android.material.snackbar.Snackbar


class QuizStartFragment : Fragment() {


    private var _binding: FragmentQuizStartBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    companion object
    {
        fun newInstance(): QuizStartFragment
        {
            return QuizStartFragment()
        }
    }

    //https://stackoverflow.com/questions/63695860/getting-a-contact-without-permissions-using-registerforactivityresult
    private val getPerson = registerForActivityResult(PickContact())
    {
        it?.also { contactUri ->
            val projection = arrayOf(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )

            this.activity?.contentResolver?.query(contactUri, projection, null, null, null)?.apply {
                moveToFirst()
                binding.nameEt.setText(getString(1));
                close()
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        _binding = FragmentQuizStartBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModel: MainActivityViewModel by activityViewModels()

        tryToSetUserName(viewModel)
        chooseContactBtnHandler()
        getStartedBtnHandler(view, viewModel)

        return view
    }

    private fun tryToSetUserName(viewModel: MainActivityViewModel)
    {
        if(viewModel.userName != ""){
            binding.nameEt.setText(viewModel.userName)
        }
    }

    private fun chooseContactBtnHandler()
    {
        binding.chooseContactBtn.setOnClickListener()
        {

            getPerson.launch(null)
        }
    }

    private fun getStartedBtnHandler(view: View, viewModel: MainActivityViewModel)
    {
        binding.getStartedBtn.setOnClickListener()
        {
            when (val name = binding.nameEt.text.toString())
            {
                "" ->
                {
                    Snackbar.make(it, "Give your name!", Snackbar.LENGTH_SHORT).show()
                }
                else ->
                {
                    viewModel.userName = name
                    val action = QuizStartFragmentDirections.actionQuizStartFragmentToQuestionFragment(name)
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }
    }


    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

}