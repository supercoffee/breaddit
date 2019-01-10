package com.plusmobileapps.breaddit

import io.reactivex.Observable
import kotlin.math.min
import androidx.lifecycle.Observer as LiveDataObserver

val Any.logTag: String
    get() {
        val className = this.javaClass.simpleName
        val endIndex = min(className.length, 23)
        return className.substring(0, endIndex)
    }

fun <T> androidx.lifecycle.LiveData<T>.toObservable(): Observable<T> {
    return Observable.create<T> { emitter ->

        val observerBridge = LiveDataObserver<T> {
            emitter.onNext(it)
        }

        this.observeForever(observerBridge)
//
//        val disposable = object: Disposable {
//            override fun isDisposed(): Boolean {
//                return false
//            }
//
//            override fun dispose() {
//                this@toObservable.removeObserver(observerBridge)
//            }
//        }
//        emitter.setDisposable(disposable)
    }
}