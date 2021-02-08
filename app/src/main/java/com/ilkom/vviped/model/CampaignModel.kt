package com.ilkom.vviped.model

import com.google.gson.annotations.SerializedName

data class CampaignModel (
    @field:SerializedName("id")
    val id: Int? = null,
    @field:SerializedName("username")
    val organization_name: String? = null,
    @field:SerializedName("user_profpic")
    val organization_profpict: String? = null,
    @field:SerializedName("path")
    val image_campaign: String? = null,
    @field:SerializedName("campaign_title")
    val campaign_title: String,
    @field:SerializedName("campaign_desc")
    val campaign_desc: String,
    @field:SerializedName("campaign_category")
    val campaign_category: String? = null,
    @field:SerializedName("campaign_receiver")
    val donation_goes: String,
    @field:SerializedName("usage_details")
    val usage_details: String,
    @field:SerializedName("phone_campaign")
    val phone_number: String
)