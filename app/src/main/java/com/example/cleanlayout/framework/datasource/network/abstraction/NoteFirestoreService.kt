package com.example.cleanlayout.framework.datasource.network.abstraction

import com.example.cleanlayout.business.domain.model.Note

interface NoteFirestoreService {
    suspend fun insertOrUpdateNote(note: Note)
    suspend fun deleteNote(primaryKey: String)
    suspend fun insertDeletedNote(note: Note)
    suspend fun insertDeletedNotes(notes: List<Note>)
    suspend fun deleteDeletedNote(note: Note)
    suspend fun getDeletedNote(): List<Note>
    suspend fun deleteAllNotes()
    suspend fun searchNote(note: Note): Note?
    suspend fun getAllNotes(): List<Note>
    suspend fun insertOrUpdatesNotes(notes: List<Note>)
}