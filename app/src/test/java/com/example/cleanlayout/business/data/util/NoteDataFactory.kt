package com.example.cleanlayout.business.data.util

import com.example.cleanlayout.business.domain.model.Note
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class NoteDataFactory(private val testClassLoader: ClassLoader) {

    fun produceListOfNotes(): List<Note> {
        return Gson().fromJson(
            getNotesFromFile("note_list.json"),
            object : TypeToken<List<Note>>() {}.type
        )
    }

    fun produceHashMapOfNotes(noteList: List<Note>): HashMap<String, Note> {
        val map = HashMap<String, Note>()
        for (note in noteList) {
            map.put(note.id, note)
        }
        return map
    }

    fun produceEmptyListOfNotes(): List<Note> = ArrayList()


    private fun getNotesFromFile(fileName: String): String {
        return testClassLoader.getResource(fileName).readText()
    }
}