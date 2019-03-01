package org.udv.nrc.caixaudv.security;

import java.util.List;

import org.udv.nrc.caixaudv.domain.Conta;
import org.udv.nrc.caixaudv.domain.enumeration.NivelPermissao;

public final class UserAccountPermissionChecker {
    
    private UserAccountPermissionChecker() {}

    public static boolean checkPermissao(Conta c, List<NivelPermissao> roles) {
        return roles.contains(c.getNivelPermissao());
    }
}