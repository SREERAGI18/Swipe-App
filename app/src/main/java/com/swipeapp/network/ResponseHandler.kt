package com.swipeapp.network.resources

sealed class Response<out T : Any>{

    /**
     * abstract function to returns the content.
     */
    abstract fun getSuccessResponse():T?
}

class SuccessResponse<out T : Any>(
    val data: T
) :
    Response<T>() {

    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = data

    override fun getSuccessResponse(): T {
        return data
    }
}

class ErrorResponse<out T : Any>(
    val message: String = "Something Went Wrong",
    val data: T?
) :
    Response<T>() {

    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T? = data

    override fun getSuccessResponse(): T? {
        return data
    }
}