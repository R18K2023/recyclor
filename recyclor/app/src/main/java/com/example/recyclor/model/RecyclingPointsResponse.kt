package com.example.recyclor.model

import com.google.gson.annotations.SerializedName

data class RecyclingPointsResponse(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("previous")
	val previous: String? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null
)

data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<Any?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class ResultsItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("description_en")
	val descriptionEn: String? = null,

	@field:SerializedName("municipality")
	val municipality: String? = null,

	@field:SerializedName("post_office")
	val postOffice: String? = null,

	@field:SerializedName("contact_info")
	val contactInfo: String? = null,

	@field:SerializedName("description_fi")
	val descriptionFi: String? = null,

	@field:SerializedName("description_sv")
	val descriptionSv: String? = null,

	@field:SerializedName("operator")
	val operator: String? = null,

	@field:SerializedName("opening_hours_en")
	val openingHoursEn: String? = null,

	@field:SerializedName("additional_details")
	val additionalDetails: String? = null,

	@field:SerializedName("materials")
	val materials: List<MaterialsItem?>? = null,

	@field:SerializedName("opening_hours_fi")
	val openingHoursFi: String? = null,

	@field:SerializedName("opening_hours_sv")
	val openingHoursSv: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("geometry")
	val geometry: Geometry? = null,

	@field:SerializedName("postal_code")
	val postalCode: String? = null,

	@field:SerializedName("occupied")
	val occupied: Boolean? = null
)

data class MaterialsItem(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("name")
	val name: String? = null
)
