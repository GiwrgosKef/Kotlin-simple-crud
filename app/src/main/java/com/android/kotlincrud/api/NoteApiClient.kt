package com.android.kotlincrud.api

import com.android.kotlincrud.Note
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NoteApiClient {

    @GET("notes") fun getNotes() : Observable<List<Note>>
    @GET("notes/{id}") fun getSpecificNote(@Path("id")id: Int): Observable<Note>
    @POST("notes") fun addNote(@Body note: Note): Completable
    @PUT("notes/{id}") fun updateNote(@Path("id")id: Int, @Body note: Note) : Completable
    @DELETE("notes/{id}") fun deleteNote(@Path("id") id: Int) : Completable

    companion object {

        fun create(): NoteApiClient {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://private-9aad-note10.apiary-mock.com/")
                //.baseUrl("http://10.0.2.2:3000/")
                .build()

            return retrofit.create(NoteApiClient::class.java)
        }
    }
}