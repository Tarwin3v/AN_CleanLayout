package com.example.cleanlayout.business.interactors.notelist

import com.example.cleanlayout.business.data.cache.CacheResponseHandler
import com.example.cleanlayout.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanlayout.business.data.util.safeCacheCall
import com.example.cleanlayout.business.domain.model.Note
import com.example.cleanlayout.business.domain.state.DataState
import com.example.cleanlayout.business.domain.state.MessageType
import com.example.cleanlayout.business.domain.state.Response
import com.example.cleanlayout.business.domain.state.StateEvent
import com.example.cleanlayout.business.domain.state.UIComponentType
import com.example.cleanlayout.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchNotes(private val noteCacheDataSource: NoteCacheDataSource) {

    fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>> = flow {
        val updatedPage = if (page <= 0) 1 else page
        val cacheResult = safeCacheCall(Dispatchers.IO) {
            noteCacheDataSource.searchNotes(query, filterAndOrder, updatedPage)
        }
        val response = object : CacheResponseHandler<NoteListViewState, List<Note>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override fun handleSuccess(resultObj: List<Note>): DataState<NoteListViewState> {
                var message: String? = SEARCH_NOTES_SUCCESS
                var uiComponentType: UIComponentType = UIComponentType.None()
                if (resultObj.isEmpty()) {
                    message = SEARCH_NOTES_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast()
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType = MessageType.Success()
                    ),
                    data = NoteListViewState(noteList = ArrayList(resultObj)),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        const val SEARCH_NOTES_SUCCESS = "Successfully retrieved list of notes."
        const val SEARCH_NOTES_NO_MATCHING_RESULTS = "There are no notes that match that query."
        const val SEARCH_NOTES_FAILED = "Failed to retrieve the list of notes."
    }
}