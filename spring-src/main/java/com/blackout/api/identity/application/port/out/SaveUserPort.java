package com.blackout.api.identity.application.port.out;

import com.blackout.api.identity.domain.User;

public interface SaveUserPort {
    User save(User user);
    void deleteById(String id);
}
