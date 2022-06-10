package com.revature.reimburse;

import com.revature.reimburse.util.Security.RSA;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.LinkedHashMap;

public class UI {
    public static void main(String[] a) {
        RSA r = new RSA();
        System.out.printf("Primary Key P(%s,\n\t\t\t  %s)\n",
                r.e, r.n);
        
        try(RSA r = RSA.getKey()) {
            String sCat = r.encrypt("cat");
            System.out.println(sCat);
        } catch(IOException ignore) {}
    }
}
