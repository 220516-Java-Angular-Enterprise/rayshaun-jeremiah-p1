package com.revature.reimburse.util.Security;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigMath {
    private static List<BigInteger> primeFactors(BigInteger n) {
        List<BigInteger> factors = new ArrayList<>();
        BigInteger d = BigInteger.ONE;

        d = d.shiftLeft(1);
        while (n.mod(d).compareTo(BigInteger.ZERO) == 0) {
            n = n.shiftRight(1);
            if(!factors.contains(d)) factors.add(d);
        }

        d = d.nextProbablePrime();
        while(d.compareTo(n) <= 0) {
            if(n.mod(d).compareTo(BigInteger.ZERO) != 0) {
                d = d.nextProbablePrime();
                continue;
            }
            while(n.mod(d).compareTo(BigInteger.ZERO) == 0) n = n.divide(d);
            factors.add(d);

            d = d.nextProbablePrime();
        }
        return factors;
    }
    private static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.multiply(b).divide(a.gcd(b));
    }
    public static BigInteger totient(BigInteger n) {
        List<BigInteger> primes = primeFactors(n);

        primes.forEach(i->primes.set(primes.indexOf(i), i.subtract(BigInteger.ONE)));
        primes.subList(1,primes.size()).forEach(i -> primes.set(0, lcm(primes.get(0),i)));
        return primes.get(0);
    }
    public static BigInteger fastTotient(BigInteger p, BigInteger q) {
        return lcm(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));
    }
}
