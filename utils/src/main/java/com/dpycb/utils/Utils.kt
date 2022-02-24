package com.dpycb.utils

import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatDateToString(time: Long): String {
        val date = Date(time)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}

fun runOnIo(block: () -> Unit): Disposable {
    return Completable
        .fromAction(block)
        .subscribeOn(Schedulers.io())
        .subscribe()
}