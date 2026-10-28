package org.ff.fastfoodbackend.repositories;

import org.ff.fastfoodbackend.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
