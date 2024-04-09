package com.rockbuilds.soundify.Data_class

data class Item(
    val artists: List<Artist>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_urls: ExternalUrlsX,
    val id: String,
    val is_local: Boolean,
    val is_playable: Boolean,
    val name: String,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String
)