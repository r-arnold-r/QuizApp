package com.example.quizapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.quizapp.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object
    {
        fun newInstance(): ProfileFragment
        {
            return ProfileFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModel: MainActivityViewModel by activityViewModels()

        tryToSetUsername(viewModel)
        updateHighScore(viewModel)
        playerNameTextWatcher()
        saveChangesBtnHandler(viewModel)

        return view
    }

    private fun saveChangesBtnHandler(viewModel: MainActivityViewModel)
    {
        binding.saveChangesBtn.setOnClickListener{
            when (val name = binding.playerNameResTv.text.toString())
            {
                "" ->
                {
                    Snackbar.make(it, "Wrong name!", Snackbar.LENGTH_SHORT).show()
                }
                else ->
                {
                    viewModel.userName = name
                    binding.playerNameResTv.setText(name)
                    binding.saveChangesBtn.visibility = View.GONE
                }
            }
        }
    }

    private fun playerNameTextWatcher()
    {
        binding.playerNameResTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (binding.saveChangesBtn.visibility == View.GONE){
                    binding.saveChangesBtn.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun tryToSetUsername(viewModel: MainActivityViewModel)
    {
        if(viewModel.userName != ""){
            binding.playerNameResTv.setText(viewModel.userName)
        }
    }

    private fun updateHighScore(viewModel: MainActivityViewModel)
    {
        binding.highScoreResTv.text = viewModel.highScore.toString()
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}