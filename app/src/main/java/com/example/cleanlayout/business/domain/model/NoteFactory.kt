package com.example.cleanlayout.business.domain.model

import com.example.cleanlayout.business.domain.util.DateUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class NoteFactory @Inject constructor(private val dateUtil: DateUtil) {

    fun createSingleNote(id: String? = null, title: String, body: String? = null) = Note(
        id = id ?: UUID.randomUUID().toString(),
        title,
        body = body ?: "",
        createdAt = dateUtil.getCurrentTimestamp(),
        updatedAt = dateUtil.getCurrentTimestamp()
    )

    fun createNoteList(numNotes: Int): List<Note> {
        val list: ArrayList<Note> = ArrayList()
        for (i in 0 until numNotes) {
            list.add(createSingleNote(title = UUID.randomUUID().toString()))
        }
        return list
    }
}