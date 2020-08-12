package com.xun.soda.sodaclean.api

import com.google.gson.annotations.SerializedName

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/11
 */

data class RepoSearchResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<Repo> = emptyList(),
    val nextPage: Int? = null
)