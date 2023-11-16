package com.example.redditapp.data

import com.example.redditapp.network.ImgurApiService
import com.example.redditapp.ui.model.ImageModel
import javax.inject.Inject


interface ImgurAuthRepository {
    suspend fun getAlbumImages(id: String): List<ImageModel>

    suspend fun getImage(id: String): ImageModel
}

class ImgurAuthRepositoryImp @Inject constructor() : ImgurAuthRepository {
    @Inject
    lateinit var imgurApiService: ImgurApiService
    override suspend fun getAlbumImages(id: String): List<ImageModel> {
        return imgurApiService.getAlbumImages(id).data
    }

    override suspend fun getImage(id: String): ImageModel {
        return imgurApiService.getImage(id)
    }
}