package com.naumov.pictureoftheday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.naumov.pictureoftheday.BuildConfig.NASA_API_KEY
import com.naumov.pictureoftheday.Model.PODRetrofitImpl
import com.naumov.pictureoftheday.Model.PODServerResponseData
import com.naumov.pictureoftheday.utils.DBY
import com.naumov.pictureoftheday.utils.TODAY
import com.naumov.pictureoftheday.utils.YESTERDAY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayViewModel (
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getData(): LiveData<PictureOfTheDayData> {
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(checkDay:Int) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)

        val apiKey : String = NASA_API_KEY;

        if (apiKey.isBlank()) {
            liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {

            val calendar = Calendar.getInstance()
            val dateKey = when (checkDay){
                TODAY->{
                    val today = calendar.time
                     SimpleDateFormat("yyyy-MM-dd").format(today)
                }
                YESTERDAY->{

                    val yesterday = calendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.time
                    SimpleDateFormat("yyyy-MM-dd").format(yesterday)

                }
                DBY->{
                    val afterYesterday = calendar.apply { add(Calendar.DAY_OF_MONTH, -2) }.time
                    SimpleDateFormat("yyyy-MM-dd").format(afterYesterday)
                }
                else -> {
                    val today = calendar.time
                    SimpleDateFormat("yyyy-MM-dd").format(today)
                }
            }
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey,dateKey).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()

                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }
            })

        }
    }

    fun sendRequestToday() {
        sendServerRequest(TODAY)
    }

    fun sendRequestYT() {
        sendServerRequest(YESTERDAY)
    }

    fun sendRequestTDBY() {
        sendServerRequest(DBY)
    }
}