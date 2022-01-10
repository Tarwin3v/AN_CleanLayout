import com.example.cleanlayout.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanlayout.business.domain.model.NoteFactory
import com.example.cleanlayout.business.interactors.notelist.GetNumNotes
import com.example.cleanlayout.business.interactors.notelist.GetNumNotes.Companion.GET_NUM_NOTES_SUCCESS
import com.example.cleanlayout.di.DependencyContainer
import com.example.cleanlayout.framework.presentation.notelist.state.NoteListStateEvent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/*
Test cases:
1. getNumNotes_success_confirmCorrect()
    a) get the number of notes in cache
    b) listen for GET_NUM_NOTES_SUCCESS from flow emission
    c) compare with the number of notes in the fake data set
*/
@InternalCoroutinesApi
class GetNumNotesShould {

    // system under test
    private val getNumNotes: GetNumNotes

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private val noteCacheDataSource: NoteCacheDataSource
    private val noteFactory: NoteFactory

    init {
        dependencyContainer.build()
        noteCacheDataSource = dependencyContainer.noteCacheDataSource
        noteFactory = dependencyContainer.noteFactory
        getNumNotes = GetNumNotes(noteCacheDataSource = noteCacheDataSource)
    }


    @Test
    fun successfully_getNumberOfNotesInCache() = runBlocking {
        var numNotes = 0
        getNumNotes.getNumNotes(
            stateEvent = NoteListStateEvent.GetNumNotesInCacheEvent()
        ).collect {
            assertEquals(it?.stateMessage?.response?.message, GET_NUM_NOTES_SUCCESS)
            numNotes = it?.data?.numNotesInCache ?: 0
        }

        val actualNumNotesInCache = noteCacheDataSource.getNumNotes()
        assertTrue { actualNumNotesInCache == numNotes }
    }
}
