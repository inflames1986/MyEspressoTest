package com.inflames1986.myespressotest.presenter.details

import com.inflames1986.myespressotest.presenter.PresenterContract

internal interface PresenterDetailsContract : PresenterContract {
    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}