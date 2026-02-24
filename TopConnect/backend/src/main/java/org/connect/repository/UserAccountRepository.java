package org.connect.repository;

import org.connect.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserAccount> findByEmailAndStatus(String email, UserAccount.UserStatus status);

    Optional<UserAccount> findByPhoneNumber(String phoneNumber);

    long countByRole(UserAccount.UserRole role);

    Page<UserAccount> findByRole(UserAccount.UserRole role, Pageable pageable);

    Page<UserAccount> findByStatus(UserAccount.UserStatus status, Pageable pageable);

    Page<UserAccount> findByRoleAndStatus(UserAccount.UserRole role, UserAccount.UserStatus status, Pageable pageable);
}

