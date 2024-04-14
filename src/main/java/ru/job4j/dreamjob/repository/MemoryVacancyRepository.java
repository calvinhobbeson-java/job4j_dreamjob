package ru.job4j.dreamjob.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ThreadSafe
public class MemoryVacancyRepository implements VacancyRepository {
    @GuardedBy("this")
    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "The vacancy of intern Java developer", LocalDateTime.now()));
        save(new Vacancy(0, "Junior Java Developer", "The vacancy of Junior Java developer", LocalDateTime.now()));
        save(new Vacancy(0, "Junior+ Java Developer", "The vacancy of Junior+ Java developer", LocalDateTime.now()));
        save(new Vacancy(0, "Middle Java Developer", "The vacancy of Middle Java developer", LocalDateTime.now()));
        save(new Vacancy(0, "Middle+ Java Developer", "The vacancy of Middle+ Java developer", LocalDateTime.now()));
        save(new Vacancy(0, "Senior Java Developer", "The vacancy of Senior Java developer", LocalDateTime.now()));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.putIfAbsent(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id, vacancies.get(id));
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(), vacancy.getCreationDate())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}