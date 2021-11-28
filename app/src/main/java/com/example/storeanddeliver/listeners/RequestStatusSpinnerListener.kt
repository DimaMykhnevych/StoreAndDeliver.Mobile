package com.example.storeanddeliver.listeners

import android.view.View
import android.widget.AdapterView

class RequestStatusSpinnerListener(
    onRequestStatusSelected: (AdapterView<*>?, View?, Int, Long) -> Unit
) :
    AdapterView.OnItemSelectedListener {
    private var callback: (AdapterView<*>?, View?, Int, Long) -> Unit = onRequestStatusSelected
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        callback(parent, view, position, id)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}