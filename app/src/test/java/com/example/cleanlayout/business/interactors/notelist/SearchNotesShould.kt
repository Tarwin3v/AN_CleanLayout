package com.example.cleanlayout.business.interactors.notelist

import com.example.cleanlayout.business.data.cache.CacheErrors
import com.example.cleanlayout.business.data.cache.FORCE_SEARCH_NOTES_EXCEPTION
import com.example.cleanlayout.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanlayout.business.domain.model.Note
import com.example.cleanlayout.business.domain.model.NoteFactory
import com.example.cleanlayout.business.interactors.notelist.SearchNotes.Companion.SEARCH_NOTES_NO_MATCHING_RESULTS
import com.example.cleanlayout.business.interactors.notelist.SearchNotes.Companion.SEARCH_NOTES_SUCCESS
import com.example.cleanlayout.di.DependencyContainer
import com.example.cleanlayout.framework.datasource.database.ORDER_BY_ASC_DATE_UPDATED
import com.example.cleanlayout.framework.presentation.notelist.state.NoteListStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


/*
Test cases:
1. blankQuery_success_confirmNotesRetrieved()
    a) query with some default search options
    b) listen for SEARCH_NOTES_SUCCESS emitted from flow
    c) confirm notes were retrieved
    d) confirm notes in cache match with notes that were retrieved
2. randomQuery_success_confirmNoResults()
    a) query with something that will yield no results
    b) listen for SEARCH_NOTES_NO_MATCHING_RESULTS emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is notes in the cache
3. searchNotes_fail_confirmNoResults()
    a) force an exception to be thrown
    b) listen for CACHE_ERROR_UNKNOWN emitted from flow
    c) confirm nothing was retrieved
    d) confirm there is notes in the cache
*/
@InternalCoroutinesApi
class SearchNotesShould {
    // system under test
    private val searchNotes: SearchNotes

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val noteCacheDataSource: NoteCacheDataSource
    private val noteFactory: NoteFactory

    init {
        dependencyContainer.build()
        noteCacheDataSource = dependencyContainer.noteCacheDataSource
        noteFactory = dependencyContainer.noteFactory
        searchNotes = SearchNotes(noteCacheDataSource = noteCacheDataSource)
    }

    @Test
    fun successfully_getNotesWithBlankQuery() = runBlocking {

        val query = ""
        var results: ArrayList<Note>? = null
        searchNotes.searchNotes(
            query = query,
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
            page = 1,
            stateEvent = NoteListStateEvent.SearchNotesEvent()
        ).collect {
            assertEquals(it?.stateMessage?.response?.message, SEARCH_NOTES_SUCCESS)
            it?.data?.noteList?.let { list ->
                results = ArrayList(list)
            }
        }

        // confirm notes were retrieved
        assertTrue { results != null }

        // confirm notes in cache match with notes that were retrieved
        val notesInCache = noteCacheDataSource.searchNotes(
            query = query,
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
            page = 1
        )
        assertTrue { results?.containsAll(notesInCache) ?: false }
    }

    @Test
    fun successfully_returnNoResultsWithRandomQuery() = runBlocking {

        val query = "hthrthrgrkgenrogn843nn4u34n934v53454hrth"
        var results: ArrayList<Note>? = null
        searchNotes.searchNotes(
            query = query,
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
            page = 1,
            stateEvent = NoteListStateEvent.SearchNotesEvent()
        ).collect {
            assertEquals(it?.stateMessage?.response?.message, SEARCH_NOTES_NO_MATCHING_RESULTS)
            it?.data?.noteList?.let { list ->
                results = ArrayList(list)
            }
        }

        // confirm nothing was retrieved
        assertTrue { results?.run { size == 0 } ?: true }

        // confirm there is notes in the cache
        val notesInCache = noteCacheDataSource.searchNotes(
            query = "",
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
            page = 1
        )
        assertTrue { notesInCache.isNotEmpty() }
    }

    @Test
    fun fail_to_searchNotesAndReturnNoResult() = runBlocking {

        val query = FORCE_SEARCH_NOTES_EXCEPTION
        var results: ArrayList<Note>? = null
        searchNotes.searchNotes(
            query = query,
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
            page = 1,
            stateEvent = NoteListStateEvent.SearchNotesEvent()
        ).collect {
            assert(it?.stateMessage?.response?.message?.contains(CacheErrors.CACHE_ERROR_UNKNOWN) ?: false)
            it?.data?.noteList?.let { list ->
                results = ArrayList(list)
            }
        }

        // confirm nothing was retrieved
        assertTrue { results?.run { size == 0 } ?: true }

        // confirm there is notes in the cache
        val notesInCache = noteCacheDataSource.searchNotes(
            query = "",
            filterAndOrder = ORDER_BY_ASC_DATE_UPDATED,
            page = 1
        )
        assertTrue { notesInCache.isNotEmpty() }
    }
}