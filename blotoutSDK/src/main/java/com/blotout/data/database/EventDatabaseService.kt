package com.blotout.data.database

import com.blotout.DependencyInjectorImpl
import com.blotout.data.database.entity.EventEntity
import com.blotout.model.Events
import com.blotout.network.ApiDataProvider
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call

class EventDatabaseService {

    var evenDao = DependencyInjectorImpl.getInstance()?.getEventDatabase()?.eventDao()

    fun insertEvent(event: EventEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            DependencyInjectorImpl.getInstance()?.getEventDatabase()?.eventDao()?.insertEvent(event)
        }
    }

     fun getEvents(){
        GlobalScope.launch(Dispatchers.IO) {
            evenDao?.getEvents().collect { data ->
                data.forEach {
                    var eventData = it.eventData
                    var eventObject = Gson().fromJson(eventData, Events::class.java)
                    DependencyInjectorImpl.getInstance().getConfigurationManager().publishEvents(eventObject, object : ApiDataProvider<Any?>() {
                        override fun onFailed(errorCode: Int, message: String, call: Call<Any?>) {
                        }

                        override fun onError(t: Throwable, call: Call<Any?>) {
                        }

                        override fun onSuccess(data: Any?) {
                            GlobalScope.launch(Dispatchers.IO) {
                                evenDao.deleteEvent(it)
                            }
                        }
                    })
                }
            }
        }
    }
}