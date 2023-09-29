package edu.ufrn.imd.Biblicron.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserType {

    COMMON("Common User"),
    ADMIN("Admin User"),
    MANAGER("Manager");

    @Getter
    private final String userType;
}
