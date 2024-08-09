package com.practiceQ.service.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.practiceQ.entity.Admin;
import com.practiceQ.entity.Doctor;
import com.practiceQ.entity.Patient;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

//import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlAttr.ROLE;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private  String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private int expirationTime;

    private Algorithm algorithm;

    private final static String USER_NAME="username";
    private final static String ROLE="role";

    @PostConstruct
    public void PostConstruct(){
        algorithm=Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(Patient patient){

        return JWT.create()
                .withClaim(USER_NAME, patient.getFirstName())
                .withClaim(ROLE, patient.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String generateTokenForAdmin(Admin admin){

        return JWT.create()
                .withClaim(USER_NAME, admin.getFirstName())
                .withClaim(ROLE, admin.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String generateTokenForDoctor(Doctor doctor){

        return JWT.create()
                .withClaim(USER_NAME, doctor.getFirstName())
                .withClaim(ROLE, doctor.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token){
        DecodedJWT verify = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return  verify.getClaim(USER_NAME).asString();
    }

    public String getRole(String token) {
        DecodedJWT verify = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return verify.getClaim(ROLE).asString();
    }
}
