package com.revature.reimburse.util.Security;

import com.revature.reimburse.util.CustomException.KeyCreationException;
import com.revature.reimburse.util.CustomException.LogCreationFailedException;

import java.io.*;
import java.math.BigInteger;
import java.security.KeyException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.*;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.logging.*;

public class RSA {
    private static final Logger logger = Logger.getLogger(RSA.class.getName());
    private static final File f = new File("src/main/resources/keys/public.key");
    static {
        String logPath = "logs/ers."+new Date(new java.util.Date().getTime())+".log";
        try {
            FileHandler fh = new FileHandler(logPath, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch(IOException | SecurityException ignore) {}
    }
    private final String n, e;
    private final Encoder encoder = Base64.getEncoder();
    private final Decoder decoder = Base64.getDecoder();

    public static RSA getKey() {

        try(FileReader fout = new FileReader(f)) {
            if(!f.setWritable(false, false))
                throw new KeyCreationException("Could not lock file write privileges.");
            StringBuilder exp = new StringBuilder();
            StringBuilder mod = new StringBuilder();
            int c;
            while((c=fout.read()) != (int)'\n') { exp.append((char)c); }
            while((c=fout.read()) != (int)'\n') { mod.append((char)c); }

            return new RSA(new String(exp), new String(mod));
        } catch (FileNotFoundException fnfe) {
            logger.info("Key file does not exists. Attempting to create...");
        } catch (KeyCreationException kce) {
            logger.info(kce.getMessage());
            return null;
        } catch (IOException | SecurityException e) {
            logger.info(e.getMessage());
            if(!f.delete()) logger.info("\tCould not delete key file");
            Arrays.stream(e.getStackTrace()).forEach(elem->logger.fine(String.format("\n\t%s", elem)));
        }

        try(FileWriter fin = new FileWriter(f)) {
            RSA r = new RSA();

            fin.append(r.e).append('\n').append(r.n).append('\n');
            f.setWritable(false, false);

            return r;
        } catch (IOException | SecurityException e) {
            logger.info(e.getMessage());
            if(!f.delete()) logger.info("\tCould not delete key file");
            Arrays.stream(e.getStackTrace()).forEach(elem->logger.fine(String.format("\n\t%s", elem)));
            return null;
        }
    }

    private RSA() {
        BigInteger exp, dec;
        BigInteger p = BigInteger.probablePrime(512, new Random());
        BigInteger q = BigInteger.probablePrime(512, new Random());

        this.n = encoder.encodeToString(p.multiply(q).toByteArray());

        BigInteger Tn = BigMath.fastTotient(p,q); p = null; q = null;
        do {
            do {
                exp = BigInteger.probablePrime(16, new Random());
            } while (exp.gcd(Tn).compareTo(BigInteger.ONE) != 0);
            dec = exp.modInverse(Tn);
        } while(exp.compareTo(dec) == 0); Tn = null; dec = null;

        this.e = encoder.encodeToString(exp.toByteArray());
        //d = encoder.encodeToString(dec.toByteArray());
    }
    private RSA(String e, String n) {
        this.e = e; this.n = n;
    }

    public String encrypt(String s) {
        StringBuilder encrypted = new StringBuilder();
        BigInteger exp = new BigInteger(decoder.decode(e));
        BigInteger mod = new BigInteger(decoder.decode(n));
        for(char c : s.toCharArray()) {
            encrypted.append(encoder.encodeToString(
                    BigInteger.valueOf(c)
                            .modPow(exp, mod)
                            .toByteArray()));
            encrypted.append("-");
        }
        return new String(encrypted);
    }

    private String decrypt(String s) {
        StringBuilder decrypted = new StringBuilder();
        byte[] d = new byte[0];
        BigInteger dec = new BigInteger(decoder.decode(d));
        BigInteger mod = new BigInteger(decoder.decode(n));
        for(String c : s.split("-")) {
            decrypted.append((char)new BigInteger(decoder.decode(c)).modPow(dec,mod).intValueExact());
        }
        return new String(decrypted);
    }

    private LinkedHashMap<Character, String> cypherMap() {
        LinkedHashMap<Character, String> plainToCypher = new LinkedHashMap<>();
        BigInteger exp = new BigInteger(decoder.decode(e));
        BigInteger mod = new BigInteger(decoder.decode(n));
        for(char c = 'a'; c <= 'z'; c++) {
            plainToCypher.put(c,
                    encoder.encodeToString(BigInteger.valueOf(c).modPow(exp, mod).toByteArray()));
        }

        return plainToCypher;
    }
    private LinkedHashMap<String, Character> decodeMap(LinkedHashMap<Character, String> code) {
        LinkedHashMap<String, Character> cypherToPlain = new LinkedHashMap<>();
        byte[] d = new byte[0];
        BigInteger dec = new BigInteger(decoder.decode(d));
        BigInteger mod = new BigInteger(decoder.decode(n));
        code.forEach((plain, cypher)->
                cypherToPlain.put(cypher, (char)new BigInteger(decoder.decode(cypher)).modPow(dec, mod).intValueExact()));

        return cypherToPlain;
    }
}
