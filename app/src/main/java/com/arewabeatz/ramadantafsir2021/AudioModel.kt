package com.arewabeatz.ramadantafsir2021

class AudioModel {

    var publisher: String = ""
    var audioId:String = ""
    var audioTitle:String = ""
    var audioUrl:String = ""
    var malam:String = ""
    var audioDescription:String = ""
    var audioImage:String = ""


    constructor()

    constructor(
        publisher: String,
        audioId: String,
        audioTitle: String,
        audioUrl: String,
        malam: String,
        audioDescription: String,
        audioImage: String
    ) {
        this.publisher = publisher
        this.audioId = audioId
        this.audioTitle = audioTitle
        this.audioUrl = audioUrl
        this.malam = malam
        this.audioDescription = audioDescription
        this.audioImage = audioImage
    }


}