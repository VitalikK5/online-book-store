package com.example.bookhub.repository.role;

import com.example.bookhub.model.Role;
import com.example.bookhub.model.enums.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
