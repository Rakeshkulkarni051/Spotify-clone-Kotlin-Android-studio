package com.rockbuilds.soundify.Data_class.Search

data class DataX(
    val albumOfTrack: AlbumOfTrack,
    val artists: Artists,
    val contentRating: ContentRating,
    val duration: Duration,
    val id: String,
    val name: String,
    val playability: Playability,
    val uri: String
)