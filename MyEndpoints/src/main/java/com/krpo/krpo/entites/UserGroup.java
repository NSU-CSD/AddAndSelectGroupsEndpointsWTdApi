package com.krpo.krpo.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "app_user_groups")
public class UserGroup {

    @Id
    private Long groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(nullable = false, name = "login")
    private String login;
}
