package com.revature.reimburse.util.Security;

import java.math.BigInteger;
import java.util.*;

public class RSA {
    private BigInteger d;
    public BigInteger n, e;

    public RSA() {
        BigInteger p = BigInteger.probablePrime(512, new Random());
        BigInteger q = BigInteger.probablePrime(512, new Random());
        n = p.multiply(q);
        BigInteger Tn = BigMath.fastTotient(p,q);
        //BigInteger Tn = BigMath.totient(n);
        p = null; q = null;
        do {
            do {
                e = BigInteger.probablePrime(Tn.bitLength()-1, new Random());
            } while (e.gcd(Tn).compareTo(BigInteger.ONE) != 0);
            d = e.modInverse(Tn);
        } while(e.compareTo(d) == 0);
        Tn = null;
    }

    /*
    private LinkedHashMap<Character, Character> cypherMap(RSA r) {
        LinkedHashMap<Character, Character> cypher = new LinkedHashMap<>();
        LinkedHashMap<Character, Character> normCypher = new LinkedHashMap<>();
        for(char c = 'a'; c <= 'z'; c++) {
            //cypher.put((char) BigInteger.valueOf((int)c).modPow(r.e, r.n),c);
        }
        List<Character> crypts = new ArrayList<>(cypher.keySet());
        Collections.sort(crypts);
        char[] i = {'a'};
        crypts.forEach(c->normCypher.put(i[0]++, cypher.get(c)));

        return normCypher;
    }*/
}
