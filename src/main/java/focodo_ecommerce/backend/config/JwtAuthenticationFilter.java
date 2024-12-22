package focodo_ecommerce.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final List<RequestMatcher> publicEndpoints = Arrays.asList(
            new AntPathRequestMatcher("/api/v1/auth/**"),
            new AntPathRequestMatcher("/api/v1/payment/**"),
            new AntPathRequestMatcher("/api/v1/categories/getCategoriesByOptions"),
            new AntPathRequestMatcher("/api/v1/orders/getAllOrderStatus"),
            new AntPathRequestMatcher("/api/v1/orders/getAllPaymentMethod"),
            new AntPathRequestMatcher("/api/v1/products/**", "GET"),
            new AntPathRequestMatcher("/api/v1/categories/**", "GET"),
            new AntPathRequestMatcher("/api/v1/vouchers/**", "GET"),
            new AntPathRequestMatcher("/api/v1/reviews"),
            new AntPathRequestMatcher("/api/v1/reviews/getReviewsOfProduct/{id}"),
            new AntPathRequestMatcher("/api/v1/reviews/getReviewsByProduct/{id}")
    );
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper mapper;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            // get token
            jwt = authHeader.substring(7);
            // get user from token
            username = jwtService.extractUsername(jwt);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if(jwtService.isTokenValid(jwt,userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details",e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return publicEndpoints.stream()
                .anyMatch(path -> path.matches(request));
    }
}
