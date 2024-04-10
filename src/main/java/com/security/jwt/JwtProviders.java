package com.security.jwt;


import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProviders {

	 public static SecretKey key=Keys.hmacShaKeyFor(JWTConstants.SECRET_KEY.getBytes());
	public String generateToken(Authentication auth) {
		
		String jwt=Jwts.builder()
				.setIssuer("sumit kumar jha..!!")
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+84600000))
				.claim("email",auth.getName())
				.signWith(key).compact();
		
		return jwt;
	}
	
	
	
	public JwtProviders() {
		super();
		
	}



	public static  String getEmail(String jwt) {
		jwt=jwt.substring(7);
		Claims claims=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		String email=String.valueOf(claims.get("email"));
		return email;
	}
}
