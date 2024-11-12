package com.hodoo.hodoomall.auth.model.dao;

import com.hodoo.hodoomall.auth.model.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUserId(String userId);
}
