package com.app.templateApp.security;

public class Authority {
    public static final String[] GUEST_AUTHORITIES = {"guest"};
    public static final String[] USER_AUTHORITIES = {"guest", "user"};
    public static final String[] ADMIN_AUTHORITIES = {"guest", "user", "admin"};
    public static final String[] SUPER_ADMIN_AUTHORITIES = {"guest", "user", "admin", "superAdmin"};

}
