package com.shakib.gsk2022.common.base

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    var title: String? = null
    var message: String? = null

    companion object {
        const val TITLE = "data"
        const val MESSAGE = "message"
    }

    protected lateinit var binding: VB

    protected abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.let {
            it.requestFeature(Window.FEATURE_NO_TITLE)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractArguments()
        init(savedInstanceState)
    }

    open fun init(savedInstanceState: Bundle?) {
        configureViews(savedInstanceState)
    }

    open fun configureViews(savedInstanceState: Bundle?) {}

    private fun extractArguments() {
        arguments?.let {
            title = it.getString(TITLE)
            message = it.getString(MESSAGE)
        }
    }
}