package com.krpo.krpo.mappers.repositories;

import com.krpo.krpo.entites.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByGroupId(Long groupId);

    List<UserGroup> findGroupsByLogin(String login);
}
