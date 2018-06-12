package com.example.akat2.smack.model

/**
 * Created by Ayush Kataria on 12-06-2018.
 */
class Channel(val name: String,val description: String,val id: String) {

    override fun toString(): String {
        return "#$name"
    }
}