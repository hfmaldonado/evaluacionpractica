package com.evaluacion.evaluacionapp.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class EvaluacionUtils {
    public static String encriptarSHA256(String clave)  {
        try
        {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(clave.getBytes());
        byte[] digest = md.digest();
        return String.format("%064x", new BigInteger(1, digest));
        }catch(Exception exc)
        {
            return clave;
        }
    }
}
