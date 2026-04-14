package com.github.kurbatov.kappi_port.repos;

import com.github.kurbatov.kappi_port.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.kurbatov.kappi_port.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByRole(Role role);
}
