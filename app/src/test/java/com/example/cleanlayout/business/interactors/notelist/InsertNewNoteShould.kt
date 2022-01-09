package com.example.cleanlayout.business.interactors.notelist

import com.example.cleanlayout.business.data.cache.CacheErrors
import com.example.cleanlayout.business.data.cache.FORCE_GENERAL_FAILURE
import com.example.cleanlayout.business.data.cache.FORCE_NEW_NOTE_EXCEPTION
import com.example.cleanlayout.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanlayout.business.data.network.abstraction.NoteNetworkDataSource
import com.example.cleanlayout.business.domain.model.NoteFactory
import com.example.cleanlayout.business.interactors.notelist.InsertNewNote.Companion.INSERT_NOTE_FAILED
import com.example.cleanlayout.business.interactors.notelist.InsertNewNote.Companion.INSERT_NOTE_SUCCESS
import com.example.cleanlayout.di.DependencyContainer
import com.example.cleanlayout.framework.presentation.notelist.state.NoteListStateEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

/*
Test cases:
1. insertNote_success_confirmNetworkAndCacheUpdated()
    a) insert a new note
    b) listen for INSERT_NOTE_SUCCESS emission from flow
    c) confirm cache was updated with new note
    d) confirm network was updated with new note
2. insertNote_fail_confirmNetworkAndCacheUnchanged()
    a) insert a new note
    b) force a failure (return -1 from db operation)
    c) confirm cache was not updated
    d) confirm network was not updated
3. throwException_checkGenericError_confirmNetworkAndCacheUnchanged()
    a) insert a new note
    b) force an exception
    c) listen for CACHE_ERROR_UNKNOWN emission from flow
    d) confirm cache was not updated
    e) confirm network was not updated
 */

class InsertNewNoteShould {

    // system under test
    private val insertNewNote: InsertNewNote

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val noteCacheDataSource: NoteCacheDataSource
    private val noteNetworkDataSource: NoteNetworkDataSource
    private val noteFactory: NoteFactory

    init {
        dependencyContainer.build()
        noteCacheDataSource = dependencyContainer.noteCacheDataSource
        noteNetworkDataSource = dependencyContainer.noteNetworkDataSource
        noteFactory = dependencyContainer.noteFactory
        insertNewNote = InsertNewNote(
            noteCacheDataSource = noteCacheDataSource,
            noteNetworkDataSource = noteNetworkDataSource,
            noteFactory = noteFactory
        )
    }

    @Test
    fun successfully_update_networkAndCacheData() = runBlocking {

        val newNote = noteFactory.createSingleNote(
            id = null,
            title = UUID.randomUUID().toString(),
        )
        insertNewNote.insertNewNote(
            id = newNote.id,
            title = newNote.title,
            stateEvent = NoteListStateEvent.InsertNewNoteEvent(newNote.title)
        ).collect {
            assertEquals(it.stateMessage?.response?.message, INSERT_NOTE_SUCCESS)
        }

        // confirm cache was updated
        val cacheNoteThatWasInserted = noteCacheDataSource.searchNoteById(newNote.id)
        assertTrue { cacheNoteThatWasInserted == newNote }
        val networkNoteThatWasInserted = noteNetworkDataSource.searchNote(newNote)
        assertTrue { networkNoteThatWasInserted == newNote }
    }

    @Test
    fun fail_to_update_cacheAndNetworkData() = runBlocking {

        val newNote = noteFactory.createSingleNote(
            id = FORCE_GENERAL_FAILURE,
            title = UUID.randomUUID().toString(),
        )
        insertNewNote.insertNewNote(
            id = newNote.id,
            title = newNote.title,
            stateEvent = NoteListStateEvent.InsertNewNoteEvent(newNote.title)
        ).collect {
            assertEquals(it.stateMessage?.response?.message, INSERT_NOTE_FAILED)
        }

        // confirm cache was not updated
        val cacheNoteThatWasInserted = noteCacheDataSource.searchNoteById(newNote.id)
        assertTrue { cacheNoteThatWasInserted == null }
        val networkNoteThatWasInserted = noteNetworkDataSource.searchNote(newNote)
        assertTrue { networkNoteThatWasInserted == null }
    }

    @Test
    fun throwException_checkGenericError_confirmNetworkAndCacheUnchanged() = runBlocking {

        val newNote = noteFactory.createSingleNote(
            id = FORCE_NEW_NOTE_EXCEPTION,
            title = UUID.randomUUID().toString(),
        )

        insertNewNote.insertNewNote(
            id = newNote.id,
            title = newNote.title,
            stateEvent = NoteListStateEvent.InsertNewNoteEvent(newNote.title)
        ).collect {
            assert(
                it.stateMessage?.response?.message
                    ?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false
            )
        }
        // confirm network was not changed
        val networkNoteThatWasInserted = noteNetworkDataSource.searchNote(newNote)
        assertTrue { networkNoteThatWasInserted == null }

        // confirm cache was not changed
        val cacheNoteThatWasInserted = noteCacheDataSource.searchNoteById(newNote.id)
        assertTrue { cacheNoteThatWasInserted == null }
    }
}