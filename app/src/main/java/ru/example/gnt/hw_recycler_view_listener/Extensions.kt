package ru.example.gnt.hw_recycler_view_listener

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun<T> Context?.showToast(message: T) =
    Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()

fun<T> Fragment.showToast(message: T) =
    context.showToast(message)
