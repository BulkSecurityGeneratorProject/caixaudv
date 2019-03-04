package org.udv.nrc.cantinadorei.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String DBA = "ROLE_DBA";

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String OPERATOR = "ROLE_OPERATOR";
    
    public static final String CANTINIER = "ROLE_CANTINIER";
    
    public static final String CLIENT = "ROLE_CLIENT";
    
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
