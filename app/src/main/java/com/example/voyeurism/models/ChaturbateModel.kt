package com.example.voyeurism.models

data class ChaturbateModel(
    //room_list_room
    val Name:String,
    val Image:String,
    // val Label:String,
    val hdLabel:String,
    //age_gender_container
    val Age:String,
    val Gender: String,
    //subject
    val Description:String,
    //sub-info
    // val Location:String,
    val Views:String,
    //link
    val Link:String) {
}