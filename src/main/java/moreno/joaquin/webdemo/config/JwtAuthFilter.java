package moreno.joaquin.webdemo.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moreno.joaquin.webdemo.exception.DuplicateEntryException;
import moreno.joaquin.webdemo.exception.ExpiredTokenException;
import moreno.joaquin.webdemo.exception.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor //create constructor for each private final field


public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain)
//            throws ServletException, IOException {
//
//            final String authHeader = request.getHeader("Authorization");
//            final String jwt;
//            final String userEmail;
//            if(authHeader == null || !authHeader.startsWith("Bearer ")){
//
//                filterChain.doFilter(request,response);
//                return;
//            }
//            jwt = authHeader.substring(7); //position 7 due to bearer \
//            userEmail =  jwtService.extractUsername(jwt);
//            //If username is not null and there is no authentication
//            if(userEmail != null  && SecurityContextHolder.getContext().getAuthentication()==null){
//                //Load username from db
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//                if(jwtService.isTokenValid(jwt,userDetails)){
//                    //Create the AuthToken needed for the contextHolder
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            userDetails.getPassword(),
//                            userDetails.getAuthorities());
//
//                    //Set details from request
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                }
//
//            }
//    }
//
//

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        try {


        if(authHeader == null || !authHeader.startsWith("Bearer ")){

            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7); //position 7 due to bearer \

            userEmail =  jwtService.extractUsername(jwt);
            //If username is not null and there is no authentication
            if(userEmail != null  && SecurityContextHolder.getContext().getAuthentication()==null){
                //Load username from db
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if(jwtService.isTokenValid(jwt,userDetails)){
                    //Create the AuthToken needed for the contextHolder
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,//userDetails.getPassword(),
                            userDetails.getAuthorities());

                    //Set details from request
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }

            }
        }catch (ExpiredJwtException e){
            filterChain.doFilter(request,response);

            return;
        }

        filterChain.doFilter(request,response);


    }

}
