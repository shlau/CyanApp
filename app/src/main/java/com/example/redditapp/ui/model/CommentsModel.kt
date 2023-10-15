package com.example.redditapp.ui.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class CommentsModel(val data: CommentsDataModel)

@Serializable
data class CommentsDataModel(val children: List<CommentModel>)

@Serializable
data class CommentModel(
    @Serializable(with = CommentDataSerializer::class)
    val data: CommentDataModel,
    var depth: Int? = null
)

@Serializable
data class CommentDataModel(
    val author: String? = null,
    val replies: CommentsModel? = null,
    val body: String? = null,
    val ups: Int? = null,
)

object CommentDataSerializer :
    JsonTransformingSerializer<CommentDataModel>(CommentDataModel.serializer()) {
    // filter out replies with empty strings
    override fun transformDeserialize(element: JsonElement): JsonElement =
        JsonObject(element.jsonObject.filterNot { (k, v) ->
            k == "replies" && v !is JsonObject && v.jsonPrimitive.content == ""
        })
}