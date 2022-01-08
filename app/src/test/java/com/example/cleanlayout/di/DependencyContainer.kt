package com.example.cleanlayout.di

import com.example.cleanlayout.business.data.cache.FakeNoteCacheDataSourceImpl
import com.example.cleanlayout.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanlayout.business.data.network.FakeNoteNetworkDataSourceImpl
import com.example.cleanlayout.business.data.network.abstraction.NoteNetworkDataSource
import com.example.cleanlayout.business.domain.model.NoteFactory
import com.example.cleanlayout.business.domain.util.DateUtil
import com.example.cleanlayout.util.isUnitTest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DependencyContainer {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil = DateUtil(dateFormat)
    lateinit var noteNetworkDataSource: NoteNetworkDataSource
    lateinit var noteCacheDataSource: NoteCacheDataSource
    lateinit var noteFactory: NoteFactory

    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {
        noteFactory = NoteFactory(dateUtil)
        noteNetworkDataSource = FakeNoteNetworkDataSourceImpl(
            notesData = HashMap(),
            deletedNotesData = HashMap()
        )
        noteCacheDataSource = FakeNoteCacheDataSourceImpl(
            notesData = HashMap(),
            dateUtil = dateUtil
        )
    }

}