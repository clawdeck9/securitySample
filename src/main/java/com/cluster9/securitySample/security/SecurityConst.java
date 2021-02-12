package com.cluster9.securitySample.security;


public class SecurityConst {
    public static final String SECRET = "NotSoSecret";
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING ="Authorization";
}
