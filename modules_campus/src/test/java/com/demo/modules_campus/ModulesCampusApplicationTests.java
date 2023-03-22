package com.demo.modules_campus;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
class ModulesCampusApplicationTests {

  private long time = 1000 * 60 * 60 * 24;  // 一天
  private String signature = "admin";

  @Test
  public void JwtTest() {
    JwtBuilder jwtBuilder = Jwts.builder();
    String jwtToken = jwtBuilder  // 创建jwtToken
        // header
        .setHeaderParam("typ", "JWT")
        .setHeaderParam("alg", "HS256")
        // payload
        .claim("userId", 1)
        .claim("userName", "admin")
        .claim("userType", 1)
        .setSubject("admin-test")
        .setExpiration(new Date(System.currentTimeMillis() + time)) // 设置有效时间
        .setId(UUID.randomUUID().toString())
        // signature
        .signWith(SignatureAlgorithm.HS256, signature) //签名
        .compact();
    System.out.println(jwtToken);
  }

  @Test
  public void parse() {
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJOYW1lIjoiYWRtaW4iLCJ1c2VyVHlwZSI6MSwic3ViIjoiYWRtaW4tdGVzdCIsImV4cCI6MTY3OTU2ODY5MiwianRpIjoiMDExMzRjOGUtZjBlMi00YTA0LWFkZjctMTU0NDAxOWM4MTZmIn0.wXSCIH7uPWrP5PTDLCapWx2NrFW2jdslJe2UtWxrHC0";
    JwtParser jwtParser = Jwts.parser();
    Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
    Claims body = claimsJws.getBody();
    System.out.println(body.get("userId"));
    System.out.println(body.get("userName"));
    System.out.println(body.getId());
    System.out.println(body.getSubject());
    System.out.println(body.getExpiration());
  }
}
