package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.fakes.FakeShowingsApi
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class ShowingsRepositoryImplTest {

    private val api = FakeShowingsApi()
    private val repository = ShowingsRepositoryImpl(api)

    private fun showing(id: Int, movieId: Int) = Showing(
        id = id,
        auditoriumId = 1,
        movieId = movieId,
        is3D = false,
        startsAt = Instant.fromEpochMilliseconds(1700000000000),
        auditoriumLayoutSnapshot = "",
        movie = null,
        auditorium = null
    )

    @Test
    fun getShowingByIdReturnsShowing() = runTest {
        api.showings = mutableListOf(showing(1, 10))

        val result = repository.getShowingById(1)

        assertTrue(result is ResultOf.Success)
        assertEquals(10, result.value.movieId)
    }

    @Test
    fun getShowingsByMovieIdFiltersCorrectly() = runTest {
        api.showings = mutableListOf(showing(1, 10), showing(2, 20))

        val result = repository.getShowingsByMovieId(10)

        assertTrue(result is ResultOf.Success)
        assertEquals(1, result.value.size)
        assertEquals(1, result.value[0].id)
    }

    @Test
    fun getShowingByIdPropagatesFailure() = runTest {
        api.error = "Not found"

        val result = repository.getShowingById(99)

        assertTrue(result is ResultOf.Failure)
        assertEquals("Not found", result.message)
    }
}
