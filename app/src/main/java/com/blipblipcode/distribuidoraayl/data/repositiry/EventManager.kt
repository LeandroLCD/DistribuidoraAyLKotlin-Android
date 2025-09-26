package com.blipblipcode.distribuidoraayl.data.repositiry

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

class EventManager @Inject constructor(dispatchers: CoroutineDispatcher) {
    private val scopeManager = CoroutineScope(SupervisorJob() + dispatchers)
    private val events = ConcurrentLinkedQueue<suspend (EventManager) -> Unit>()
    private var _runningJob: Job? = null

    fun addCall(call: suspend (EventManager) -> Unit) {
        events.add(call)
        execute()
    }
    fun execute(){
        if(_runningJob?.isActive != true){
            _runningJob = scopeManager.launch {
                var currentCall: (suspend (EventManager) -> Unit)? = null
                try {
                    while(events.isNotEmpty()) {
                        currentCall = events.poll()
                        currentCall?.invoke(this@EventManager)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    events.add(currentCall)
                }
            }
        }
    }
    fun cancel() {
        _runningJob?.cancel()
        events.clear()
    }
}