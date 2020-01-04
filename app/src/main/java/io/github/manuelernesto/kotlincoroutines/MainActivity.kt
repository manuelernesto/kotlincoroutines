package io.github.manuelernesto.kotlincoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private val JOB_TIMEOUT = 2100L
    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_action.setOnClickListener {
            //IO, Main, Default
            setNewText("Click!")
            CoroutineScope(IO).launch {
                fakeAPIRequest()
            }
        }
    }

    private suspend fun fakeAPIRequest() {
        withContext(IO) {
            val job = withTimeoutOrNull(JOB_TIMEOUT) {
                val result1 = getResult1FromApi()
                settextOnMainThred(result1)
                Log.d(TAG, result1)

                val result2 = getResult2FromApi()
                settextOnMainThred(result2)
                Log.d(TAG, result2)
            }

            if (job == null) {
                val cancelMessage = "Cancelling job... job took longer than $JOB_TIMEOUT ms"
                Log.d(TAG, cancelMessage)
                settextOnMainThred(cancelMessage)
            }

        }
    }

    private fun setNewText(input: String) {
        val newText = txt_view.text.toString() + "\n$input"
        txt_view.text = newText
    }

    private suspend fun settextOnMainThred(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        Log.d(TAG, "$methodName : ${Thread.currentThread().name}")
    }
}
