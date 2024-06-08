package com.sky.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param expirTime jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long expirTime, Map<String, Object> claims) {
        // 构建 JWT Token
        String token = Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + expirTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact(); // 生成 JWT Token
        return token;
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 将字符串转换为字节数组
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        // 使用 HMAC 算法创建 SecretKey 对象
        SecretKey key = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token);

        // 获取解密后的声明 (claims)
        return claimsJws.getBody();
    }

}
