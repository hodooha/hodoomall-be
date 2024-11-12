package com.hodoo.hodoomall.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value="refreshToken", timeToLive = 1209600) // 14일
public class RefreshToken {

    @Id
    private String userId;
    private String refreshToken;

}
