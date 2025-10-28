package me.leondorus.memespeaker.savers

import me.leondorus.memespeaker.repos.ReactRepo

class ReactSaver(private val reactUpdateRepo: ReactUpdateRepo, private val reactRepo: ReactRepo) {
    suspend fun startSaving() {
        reactUpdateRepo.getReactUpdates().collect { (oldReacts, newReacts) ->
            for (r in oldReacts)
                reactRepo.delReact(r)
            for (r in newReacts)
                reactRepo.addReact(r)
        }
    }
}