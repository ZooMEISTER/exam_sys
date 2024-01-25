package com.zoom.exam_sys_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * @Author ZooMEISTER
 * @Description: JWT工具类
 * @DateTime 2024/1/19 20:31
 **/

public class JWTUtils {
    /**
     * 过期时间(单位:秒)
     */
    public static final int ACCESS_EXPIRE = 604800;
    /**
     * 加密算法
     */
    private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;
    /**
     * 私钥 / 生成签名的时候使用的秘钥secret，一般可以从本地配置文件中读取，切记这个秘钥不能外露，只在服务端使用，在任何场景都不应该流露出去。
     * 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
     * 应该大于等于 256位(长度32及以上的字符串)，并且是随机的字符串
     */
    private final static String SECRET = "secretKey-qwertyuioplkjhgfdsazxcvbnmshjdunuin3654875nn";
    /**
     * 秘钥实例
     */
    public static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    /**
     * jwt签发者
     */
    private final static String JWT_ISS = "ZooMEISTER";
    /**
     * jwt主题
     */
    private final static String SUBJECT = "login-token";

    /*

     */
    /**
     * @Author: ZooMEISTER
     * @Description: 生成token
     *      这些是一组预定义的声明，它们 不是强制性的，而是推荐的 ，以 提供一组有用的、可互操作的声明 。
     *      iss: jwt签发者
     *      sub: jwt所面向的用户
     *      aud: 接收jwt的一方
     *      exp: jwt的过期时间，这个过期时间必须要大于签发时间
     *      nbf: 定义在什么时间之前，该jwt都是不可用的.
     *      iat: jwt的签发时间
     *      jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @DateTime: 2024/1/19 20:43
     * @Params: long userId, int profilev
     * @Return String
    */
    public static String genAccessToken(long userid, int profilev) {
        // 令牌id
        String uuid = UUID.randomUUID().toString();
        Date exprireDate = Date.from(Instant.now().plusSeconds(ACCESS_EXPIRE));

        return Jwts.builder()
                // 设置头部信息header
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                // 设置自定义负载信息payload
                .claim("userid", userid)
                .claim("profilev", profilev)
                // 令牌ID
                .id(uuid)
                // 过期日期
                .expiration(exprireDate)
                // 签发时间
                .issuedAt(new Date())
                // 主题
                .subject(SUBJECT)
                // 签发者
                .issuer(JWT_ISS)
                // 签名
                .signWith(KEY, ALGORITHM)
                .compact();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 解析token
    * @DateTime: 2024/1/19 20:43
    * @Params: String
    * @Return Jws<Claims>
    */
    public static Jws<Claims> parseClaim(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token);
    }

    public static JwsHeader parseHeader(String token) {
        return parseClaim(token).getHeader();
    }

    public static Claims parsePayload(String token) {
        return parseClaim(token).getPayload();
    }

    public static void main(String[] args) {
        String token = genAccessToken(1234L, 1);
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOjExOTEzODY5NTY5OTk2ODgxOTIsInByb2ZpbGV2IjowLCJqdGkiOiIxYmIwNmZlZi0xOGZiLTRjMjAtODMwZS1lYWU3ZTdhMDQxZDQiLCJleHAiOjE3MDYyODIxNTMsImlhdCI6MTcwNTY3NzM1Mywic3ViIjoibG9naW4tdG9rZW4iLCJpc3MiOiJab29NRUlTVEVSIn0.nJDSQVBykGBA09YuOrntjWS4gImBb2bWgKOO15xVM6wI";
        System.out.println(token);
        System.out.println(parseHeader(token));
        System.out.println(parsePayload(token));
        System.out.println(parseClaim(token));
    }
}
