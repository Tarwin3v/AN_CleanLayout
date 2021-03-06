package com.example.cleanlayout.business.data.cache.implementation

import com.example.cleanlayout.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanlayout.business.domain.model.Note
import com.example.cleanlayout.framework.datasource.cache.abstraction.NoteDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCacheDataSourceImpl @Inject constructor(private val noteDaoService: NoteDaoService) : NoteCacheDataSource {
    override suspend fun insertNote(note: Note) = noteDaoService.insertNote(note)

    override suspend fun deleteNote(primary: String) = noteDaoService.deleteNote(primary)

    override suspend fun deleteNotes(notes: List<Note>) = noteDaoService.deleteNotes(notes)

    override suspend fun updateNote(primaryKey: String, newTitle: String, newBody: String?) =
        noteDaoService.updateNote(primaryKey, newTitle, newBody)

    override suspend fun searchNotes(query: String, filterAndOrder: String, page: Int) = noteDaoService.searchNotes()

    override suspend fun searchNoteById(primaryKey: String) = noteDaoService.searchNoteById(primaryKey)

    override suspend fun getNumNotes() = noteDaoService.getNumNotes()

    override suspend fun insertNotes(notes: List<Note>) = noteDaoService.insertNotes(notes)
}