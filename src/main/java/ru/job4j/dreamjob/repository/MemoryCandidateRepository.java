package ru.job4j.dreamjob.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {
    @GuardedBy("this")
    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Oleg", "Candidate for a job", LocalDateTime.now()));
        save(new Candidate(0, "Oleg", "Candidate for a job", LocalDateTime.now()));
        save(new Candidate(0, "Oleg", "Candidate for a job", LocalDateTime.now()));
        save(new Candidate(0, "Oleg", "Candidate for a job", LocalDateTime.now()));
        save(new Candidate(0, "Oleg", "Candidate for a job", LocalDateTime.now()));
        save(new Candidate(0, "Oleg", "Candidate for a job", LocalDateTime.now()));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.putIfAbsent(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id, candidates.get(id));
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription(), candidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
