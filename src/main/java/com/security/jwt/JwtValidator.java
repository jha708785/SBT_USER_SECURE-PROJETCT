package com.security.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//onceperrequest filter used for filtration
@Service
public class JwtValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException 
	{
	     String jwt=request.getHeader(JWTConstants.JWT_HEADER);
	     if(jwt!=null) {
	    	 
	    	 jwt=jwt.substring(7);
	    	 try
	    	{
	    		//String email2 = JwtProviders.getEmail(jwt);
			   SecretKey key=Keys.hmacShaKeyFor(JWTConstants.SECRET_KEY.getBytes());
			   Claims claims=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
			   String email=String.valueOf(claims.get("email"));
			   String authorities=String.valueOf(claims.get("authorities"));
			   List<GrantedAuthority>grantauthorities=AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
			   Authentication authentication=new UsernamePasswordAuthenticationToken(email,null, grantauthorities);
			   SecurityContextHolder.getContext().setAuthentication(authentication);
			}
	    	 catch (Exception e) 
	    	 {
				throw new BadCredentialsException("invalid token ....from jwt validator...!!");
			}
	    	 
	     }
	     
	     filterChain.doFilter(request, response);
		
	}
}
