package com.krpo.krpo.services;

import com.krpo.krpo.entites.UserGroup;
import com.krpo.krpo.mappers.repositories.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;

    public boolean existsByLoginAndGroupId(String login, Long groupId) {
        UserGroup userGroup = userGroupRepository.findByGroupId(groupId).orElse(null);
        if (null == userGroup) {
            return false;
        }
        return (userGroup.getLogin().equals(login));
    }

    public void saveUserGroup(UserGroup userGroup) {
        userGroupRepository.save(userGroup);
    }

}
