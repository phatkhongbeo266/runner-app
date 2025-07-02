package com.example.run_tracker_native_app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.run_tracker_native_app.adapter.ChatAdapter
import com.example.run_tracker_native_app.databinding.FragmentChatBoxBinding
import com.example.run_tracker_native_app.utils.Constant
import com.example.run_tracker_native_app.viewmodels.ChatViewModel
import com.example.run_tracker_native_app.viewmodels.HistoryViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import io.noties.markwon.Markwon
import kotlinx.coroutines.launch

class ChatBoxFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentChatBoxBinding
    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val historyViewModel: HistoryViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    private lateinit var chatAdapter: ChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBoxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            historyViewModel.runningListAllHistoryData.observe(viewLifecycleOwner) { historyList ->
                if (historyList != null) {
                    val data = historyList.map {
                        mapOf(
                            "distance" to it.distance,
                            "duration" to it.timeInSeconds,
                            "calories" to it.kcal,
                            "avgSpeed" to it.avgSpeed)
                    }
                    val userData = Gson().toJson(data)
                    val prompt = String.format(Constant.PROMPT, userData)
                    Log.e("anth", "onViewCreated: $prompt")
                    viewModel.sendMessage(prompt, true)
                }
            }
        }

        chatAdapter = ChatAdapter(Markwon.create(requireContext()))
        binding.rcvMessage.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        binding.ivSend.setOnClickListener {
            val userMessage = binding.edtMessage.text.toString()
            if (userMessage.isNotBlank()) {
                binding.edtMessage.text.clear()
                viewModel.sendMessage(userMessage)
            }
        }

        lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                chatAdapter.submitList(messages)
                binding.rcvMessage.post {
                    binding.rcvMessage.scrollToPosition(messages.size - 1)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.progress.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            // Set 80% screen height
            it.layoutParams.height = (resources.displayMetrics.heightPixels * 0.8).toInt()
            val behavior = BottomSheetBehavior.from(it)

            // Expand fully and lock state
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            behavior.isHideable = false
            behavior.isDraggable = false
        }
    }
}