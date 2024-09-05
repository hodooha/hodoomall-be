package com.hodoo.hodoomall.user.model.dao;

import com.hodoo.hodoomall.user.model.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);


}
