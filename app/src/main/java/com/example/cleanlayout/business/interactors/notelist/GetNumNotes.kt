package com.example.cleanlayout.business.interactors.notelist

import com.example.cleanlayout.business.data.cache.CacheResponseHandler
import com.example.cleanlayout.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanlayout.business.data.util.safeCacheCall
import com.example.cleanlayout.business.domain.state.DataState
import com.example.cleanlayout.business.domain.state.MessageType
import com.example.cleanlayout.business.domain.state.Response
import com.example.cleanlayout.business.domain.state.StateEvent
import com.example.cleanlayout.business.domain.state.UIComponentType
import com.example.cleanlayout.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumNotes(private val noteCacheDataSource: NoteCacheDataSource) {

    fun getNumNotes(stateEvent: StateEvent): Flow<DataState<NoteListViewState>?> = flow {
        val cacheResult = safeCacheCall(Dispatchers.IO) {
            noteCacheDataSource.getNumNotes()
        }
        val response = object : CacheResponseHandler<NoteListViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override fun handleSuccess(resultObj: Int): DataState<NoteListViewState> {
                val viewState = NoteListViewState(numNotesInCache = resultObj)
                return DataState.data(
                    response = Response(
                        message = GET_NUM_NOTES_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        const val GET_NUM_NOTES_SUCCESS = "Successfully retrieved the number of notes from the cache."
        const val GET_NUM_NOTES_FAILED = "Failed to get the number of notes from the cache."
    }
}