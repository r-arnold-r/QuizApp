package com.example.quizapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.ActivityMainBinding
import com.example.quizapp.databinding.FragmentQuestionListBinding

class QuestionListFragment : Fragment(), QuestionAdapter.ItemClickListener{
    private var _binding: FragmentQuestionListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: QuizEndFragmentArgs by navArgs()

    private lateinit var recyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter

    companion object
    {
        fun newInstance(): QuestionListFragment
        {
            return QuestionListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentQuestionListBinding.inflate(inflater, container, false)

        val view = binding.root
        val viewModel: MainActivityViewModel by activityViewModels()

        arrayAdapterHandler(view)
        recycleViewAndAdapterHandler(view, viewModel)

        return view
    }

    private fun arrayAdapterHandler(view :View)
    {
        ArrayAdapter.createFromResource(
            view.context,
            R.array.category_array,
            android.R.layout.simple_spinner_item)
            .also{ adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.categoriesSpinner.adapter = adapter
            }

        binding.categoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                questionAdapter.filter.filter(binding.categoriesSpinner.getItemAtPosition(position).toString())
            }

        }
    }

    private fun recycleViewAndAdapterHandler(view : View, viewModel : MainActivityViewModel)
    {
        //creating and setting up adapter with recyclerView
        recyclerView = binding.recyclerViewQuestions

        //creating and setting up adapter with recyclerView
        questionAdapter = QuestionAdapter(view, this, viewModel.questions) //setting the data and listener for adapter

        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = questionAdapter
        questionAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {

    }
}