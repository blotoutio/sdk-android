package com.analytics.blotout.data.database

import android.util.Log
import com.analytics.blotout.BlotoutAnalytics
import com.analytics.blotout.DependencyInjectorImpl
import com.analytics.blotout.data.database.entity.EventEntity
import com.analytics.blotout.model.Events
import com.analytics.blotout.network.ApiDataProvider
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call

class EventDatabaseService {

    private var evenDao = DependencyInjectorImpl.getInstance().getEventDatabase().eventDao()

    companion object {
        private const val TAG = "EventDatabaseService"
    }

    fun insertEvent(event: EventEntity) {

        CoroutineScope(Dispatchers.Default).launch {
            try {
                DependencyInjectorImpl.getInstance().getEventDatabase().eventDao()
                        .insertEvent(event)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun getEvents() {

        CoroutineScope(Dispatchers.Default).launch {

            val idTable = ArrayList<Int>()
            evenDao.getEvents().collect { data ->
                data.forEach {
                    try {
                        if (!idTable.contains(it.id)) {
                            idTable.add(it.id)
                            val eventData = it.eventData
                            Log.d("###Pushing id ", "" + it.id)
                            Log.d("###Pushing events ", "" + eventData)

                            val eventObject: Events = Gson().fromJson(eventData.trim(), Events::class.java)

                            DependencyInjectorImpl.getInstance().getConfigurationManager()
                                    .publishEvents(eventObject, object : ApiDataProvider<Any?>() {
                                        override fun onFailed(
                                                errorCode: Int,
                                                message: String,
                                                call: Call<Any?>
                                        ) {
                                        }

                                        override fun onError(t: Throwable, call: Call<Any?>) {
                                        }

                                        override fun onSuccess(data: Any?) {
                                            CoroutineScope(Dispatchers.Default).launch {
                                                //Log.d("###deleting id ", "" + it.id)
                                                evenDao.deleteEvent(it)
                                            }
                                        }
                                    })
                        }
                    } catch (e: Exception) {
                        when (e) {
                            is JsonSyntaxException, is IllegalStateException -> {
                                val malformedLog = hashMapOf<String, Any>()
                                malformedLog.put(it.id.toString(), it.eventData)
                                BlotoutAnalytics.capture("Malformed Log", malformedLog)
                                evenDao.deleteEvent(it)
                            }
                        }
                    }
                }
            }
        }
    }
}