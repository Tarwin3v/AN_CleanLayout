package com.example.cleanlayout.business.interactors.notelist

import com.example.cleanlayout.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanlayout.business.data.network.abstraction.NoteNetworkDataSource
import com.example.cleanlayout.business.domain.model.NoteFactory
import com.example.cleanlayout.di.DependencyContainer

class InsertNewNoteShould {

    // system under test
    private val insertNewNote: InsertNewNote

    // dependencies
    private val dependencyContainer: DependencyContainer
    private val noteCacheDataSource: NoteCacheDataSource
    private val noteNetworkDataSource: NoteNetworkDataSource
    private val noteFactory: NoteFactory

    init {
        dependencyContainer = DependencyContainer()
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
}