package io.github.manuelernesto.kotlincoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"
    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_action.setOnClickListener {
            //IO, Main, Default
            CoroutineScope(IO).launch {
                fakeAPIRequest()
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

    private suspend fun fakeAPIRequest() {
        val resul1 = getResult1FromApi()
        Log.d(TAG, resul1)
        settextOnMainThred(resul1)

        val resul2 = getResult2FromApi()
        Log.d(TAG, resul2)
        settextOnMainThred(resul2)
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
