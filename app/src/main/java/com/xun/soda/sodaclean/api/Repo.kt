package com.xun.soda.sodaclean.api

import com.google.gson.annotations.SerializedName

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/11
 */

data class Repo(
    val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("full_name") val fullName: String,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("html_url") val url: String,
    @field:SerializedName("stargazers_count") val stars: Int,
    @field:SerializedName("forks_count") val forks: Int,
    @field:SerializedName("language") val language: String?
)
