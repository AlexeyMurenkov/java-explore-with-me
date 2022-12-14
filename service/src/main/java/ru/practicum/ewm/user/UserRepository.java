package ru.practicum.ewm.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
