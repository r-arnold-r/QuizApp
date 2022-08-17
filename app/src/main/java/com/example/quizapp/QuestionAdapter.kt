package com.example.quizapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.model.Question


class QuestionAdapter(
    private val view: View,
    private val mItemClickListener: ItemClickListener,
    private var questions: MutableList<Question>
)
    : RecyclerView.Adapter<QuestionAdapter.DataViewHolder>(), Filterable {

    var questionFilterList = ArrayList<Question>()
    // exampleListFull . exampleList

    init {
        questionFilterList = questions as ArrayList<Question>
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DataViewHolder {
        val itemView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.question_item, viewGroup, false)
        return DataViewHolder(itemView, mItemClickListener)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.questionTv.text = questionFilterList[position].question
        holder.correctAnswerTv.text = questionFilterList[position].correct_answer
        holder.answerType.text = questionFilterList[position].type


        holder.deleteBtn.setOnClickListener{
            questionFilterList.removeAt(position)
            notifyDataSetChanged()
        }

        holder.detailsBtn.setOnClickListener{

            val action = QuestionListFragmentDirections.actionQuestionListFragmentToQuestionDetailsFragment(questionFilterList[position])
            Navigation.findNavController(view).navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return questionFilterList.size
    }

    class DataViewHolder(view: View, itemClickListener: ItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var questionTv: TextView = view.findViewById(R.id.question_tv)
        var correctAnswerTv: TextView = view.findViewById(R.id.correct_answer_tv)
        var answerType: TextView = view.findViewById(R.id.answer_type_tv)
        var detailsBtn: Button = view.findViewById(R.id.details_btn)
        var deleteBtn: Button = view.findViewById(R.id.delete_btn)
        var mItemClickListener: ItemClickListener? = itemClickListener
        override fun onClick(view: View) {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(adapterPosition)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                questionFilterList = if (charSearch == "Any Category") {
                    questions as ArrayList<Question>
                } else {
                    val resultList = ArrayList<Question>()
                    for (row in questions) {
                        if (row.category.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = questionFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                questionFilterList = results?.values as ArrayList<Question>
                notifyDataSetChanged()
            }
        }
    }
}