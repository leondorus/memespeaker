package me.leondorus.savers

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import me.leondorus.data.React
import me.leondorus.data.mid
import me.leondorus.data.uid
import me.leondorus.repos.ReactRepo
import kotlin.test.Test

class ReactSaverTest {
    @Test
    fun `Saves new reacts`() = runTest {
        val reacts = listOf(
            React("a", 5L.mid, 0L.uid),
            React("B", 6L.mid, 0L.uid),
            React("c", 20L.mid, 0L.uid),
            React("d", 127L.mid, 0L.uid),
        )
        val reactUpdateRepo = mockk<ReactUpdateRepo>()
        every { reactUpdateRepo.getReactUpdates() } returns flow { emit(ReactUpdate(listOf(), reacts)) }

        val reactRepo = mockk<ReactRepo>()
        for (m in reacts) {
            coEvery { reactRepo.addReact(m) } returns Unit
        }

        val reactSaver = ReactSaver(reactUpdateRepo, reactRepo)
        reactSaver.startSaving()

        coVerify {
            for (m in reacts)
                reactRepo.addReact(m)
        }
    }


    @Test
    fun `Deletes old reacts`() = runTest {
        val reacts = listOf(
            React("a", 5L.mid, 0L.uid),
            React("a", 6L.mid, 0L.uid),
            React("a", 20L.mid, 0L.uid),
            React("a", 127L.mid, 0L.uid),
        )
        val reactUpdateRepo = mockk<ReactUpdateRepo>()
        every { reactUpdateRepo.getReactUpdates() } returns flow { emit(ReactUpdate(reacts, listOf())) }

        val reactRepo = mockk<ReactRepo>()
        for (m in reacts) {
            coEvery { reactRepo.delReact(m) } returns Unit
        }

        val reactSaver = ReactSaver(reactUpdateRepo, reactRepo)
        reactSaver.startSaving()

        coVerify {
            for (m in reacts)
                reactRepo.delReact(m)
        }
    }


    @Test
    fun `Deletes old and saves new reacts`() = runTest {
        val oldReacts = listOf(
            React("a", 5L.mid, 0L.uid),
            React("a", 6L.mid, 0L.uid),
            React("a", 20L.mid, 0L.uid),
            React("a", 127L.mid, 0L.uid),
        )
        val newReacts = listOf(
            React("a", 5L.mid, 0L.uid),
            React("B", 6L.mid, 0L.uid),
            React("c", 20L.mid, 0L.uid),
            React("d", 127L.mid, 0L.uid),
        )
        val reactUpdateRepo = mockk<ReactUpdateRepo>()
        every { reactUpdateRepo.getReactUpdates() } returns flow { emit(ReactUpdate(oldReacts, newReacts)) }

        val reactRepo = mockk<ReactRepo>()
        for (m in oldReacts) {
            coEvery { reactRepo.delReact(m) } returns Unit
        }
        for (m in newReacts) {
            coEvery { reactRepo.addReact(m) } returns Unit
        }

        val reactSaver = ReactSaver(reactUpdateRepo, reactRepo)
        reactSaver.startSaving()

        coVerify {
            for (m in oldReacts)
                reactRepo.delReact(m)
            for (m in newReacts)
                reactRepo.addReact(m)
        }
    }
}