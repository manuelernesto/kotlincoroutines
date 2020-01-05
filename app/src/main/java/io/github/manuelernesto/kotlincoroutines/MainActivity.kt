package io.github.manuelernesto.kotlincoroutines

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000 //ms
    private val TAG = "TAG"
    private lateinit var job: CompletableJob


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_action.setOnClickListener {
            if (!::job.isInitialized)
                initJob()
            progressBar.startJobOrCancel(job)
        }
    }

    private fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            Log.d(TAG, "$job is already active. Cancelling...")
            resetJob()
        } else {
            btn_action.text = "Cancel Job #1"
            CoroutineScope(IO + job).launch {
                Log.d(TAG, "coroutine $this is activated with this job $job")
                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompeteTextVoew("Job is completed")
            }
        }
    }

    private fun updateJobCompeteTextVoew(text: String) {
        GlobalScope.launch(Main) {
            txt_view.text = text
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCancelled)
            job.cancel(CancellationException("Reseting job"))

        initJob()
    }

    private fun initJob() {
        btn_action.text = "Start Job #1"
        updateJobCompeteTextVoew("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let { message ->
                var msg = message
                if (msg.isNullOrEmpty())
                    msg = "Unknow cancellation error."
                Log.d(TAG, "$job was canvelled. Reason: $msg")
                showToast(msg)
            }
            progressBar.max = PROGRESS_MAX
            progressBar.progress = PROGRESS_START
        }
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Main) {
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
        }
    }
}
