package com.udinn.prcomfy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.udinn.prcomfy.databinding.FragmentHistoryBinding
import com.udinn.prcomfy.response.DataResult
import com.udinn.prcomfy.utils.HistoryViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.json.JSONTokener

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HistoryViewModel>()
//    lateinit var data: DataResult
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHistoryBinding.bind(view)

        val id: TextView = view.findViewById(R.id.tv_id)
        val result: TextView = view.findViewById(R.id.tv_result)

        viewModel.listData.observe(viewLifecycleOwner){
            result.text = it.data.result
            id.text = it.data.id.toString()
//            val hasil = JSONTokener(it).nextValue() as JSONObject

        }
//        viewModel.getData(data)
//        result.text = viewModel.listData.value!!.result




    }

}