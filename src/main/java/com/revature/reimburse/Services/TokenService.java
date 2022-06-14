package com.revature.reimburse.Services;

import com.revature.reimburse.DTOs.responses.PrincipalNS;
import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.JwtConfig;
import com.revature.reimburse.util.Security.RSA;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.security.Principal;
import java.util.Date;
import java.util.logging.Logger;

public class TokenService {
    private static final Logger logger = Logger.getLogger(TokenService.class.getName());
    private JwtConfig jwtConfig;

    public TokenService(){super();}
    public TokenService(JwtConfig jwtConfig){ this.jwtConfig = jwtConfig;}

    public String generateToken(PrincipalNS subject){
        long now = System.currentTimeMillis();
        JwtBuilder tokenBuilder = Jwts.builder()
                .setId(subject.getId())
                .setIssuer("Employee Reimbursement Service")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration()))
                .setSubject(subject.getUsername())
                .claim("role", subject.getRole())
                .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());
        logger.info("Generating token for "+subject.getUsername());
        return tokenBuilder.compact();
    }
    public PrincipalNS extractRequesterDetails(String token){
        try{

            //PrincipalNS strType = Reimbursements.Type.valueOf(type);

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();
            return new PrincipalNS(claims.getId(), claims.getSubject(),claims.get("roles", Users.Roles.class));
        }
        catch(Exception e){
            logger.warning("Failed to extract user. "+e.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

}
