package com.casaque.pdmchat.Model

data class Message(val sender: String, val timestamp: String, val content: String, val recipient: String){
    constructor() : this("", "", "", "")
}