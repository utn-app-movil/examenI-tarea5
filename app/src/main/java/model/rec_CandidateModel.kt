package cr.ac.utn.appmovil.model

import cr.ac.utn.appmovil.identities.Candidate

object CandidateModel {
    private val candidates = mutableListOf<Candidate>()

    fun addCandidate(candidate: Candidate) {
        candidates.add(candidate)
    }

    fun getCandidates(): List<Candidate> {
        return candidates.toList()
    }

    fun removeCandidate(candidate: Candidate) {
        candidates.remove(candidate)
    }
}