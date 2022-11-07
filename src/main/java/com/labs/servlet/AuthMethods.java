package com.labs.servlet;

/**
 * There is no list of auth methods constants in Servlet API. In real project we would use list of all methods hetre.
 */
public class AuthMethods {

    /**
     * Basic authorization.
     */
    public static final String BASIC = "Basic";

    /**
     * Basic realm. Value for WWW-Authenticate http header.
     */
    public static final String BASIC_REALM = "Basic realm=\"%s\"";
}
