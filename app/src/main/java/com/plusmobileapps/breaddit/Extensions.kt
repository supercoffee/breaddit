package com.plusmobileapps.breaddit

import kotlin.math.min
import androidx.lifecycle.Observer as LiveDataObserver

val Any.logTag: String
    get() {
        val className = this.javaClass.simpleName
        val endIndex = min(className.length, 23)
        return className.substring(0, endIndex)
    }
