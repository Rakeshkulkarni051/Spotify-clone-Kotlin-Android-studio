package com.rockbuilds.soundify.Data_class.Search

data class Tracks(
    val items: List<Item>,
    val pagingInfo: PagingInfo,
    val totalCount: Int
)