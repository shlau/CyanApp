package com.example.redditapp.network

import com.example.redditapp.ui.model.ImageModel
import com.example.redditapp.ui.model.ImgurAlbumModel
import com.example.redditapp.ui.model.SubredditsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ImgurApiService {
    @GET("album/{id}/images")
    suspend fun getAlbumImages(
        @Path("id") id: String
    ): ImgurAlbumModel

    @GET("image/{id}")
    suspend fun getImage(
        @Path("id") id: String
    ): ImageModel
}