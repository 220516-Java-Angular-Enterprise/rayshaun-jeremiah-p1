package com.revature.reimburse;

import com.revature.reimburse.util.Security.RSA;

public class UI {
    public static void main(String[] a) {
        RSA r = new RSA();
        System.out.printf("Primary Key P(%s,\n\t\t\t  %s)\n",
                r.e, r.n);
        
    }
}
