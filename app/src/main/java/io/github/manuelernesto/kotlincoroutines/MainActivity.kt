package io.github.manuelernesto.kotlincoroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
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

    private suspend fun fakeAPIRequest() {
        val resul1 = getResult1FromApi()
        Log.d(TAG, resul1)
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private fun logThread(methodName: String) {
        Log.d(TAG, "$methodName : ${Thread.currentThread().name}")
    }
}
