package com.dgnl.smartacademyandlabsupportplatform.interceptor;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import com.dgnl.smartacademyandlabsupportplatform.service.UserService;
import com.dgnl.smartacademyandlabsupportplatform.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("remember-me".equals(cookie.getName())) {
                        try {
                            String token = cookie.getValue();
                            Claims claims = jwtTokenUtil.getClaimsFromToken(token);
                            Long userId = claims.get("id", Long.class);
                            user = userService.getById(userId);
                            if (user != null) {
                                session.setAttribute("user", user);
                                session.setAttribute("role", user.getRole());
                            }
                        } catch (Exception e) {
                            // Token hỏng -> xóa cookie
                            Cookie c = new Cookie("remember-me", null);
                            c.setMaxAge(0);
                            c.setPath("/");
                            response.addCookie(c);
                        }
                    }
                }
            }
        }

        if (uri.equals("/login") || uri.equals("/register")) {
            if (user != null) {
                response.sendRedirect(getHomeUrl(user.getRole()));
                return false;
            }
            return true;
        }

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        String role = user.getRole().toLowerCase();
        if (uri.startsWith("/admin") && !role.equals("admin")) {
            response.sendRedirect(getHomeUrl(role));
            return false;
        }
        if (uri.startsWith("/student") && !role.equals("student")) {
            response.sendRedirect(getHomeUrl(role));
            return false;
        }
        if (uri.startsWith("/lecturer") && !role.equals("lecturer")) {
            response.sendRedirect(getHomeUrl(role));
            return false;
        }

        return true;
    }

    private String getHomeUrl(String role) {
        if (role.equalsIgnoreCase("admin")) return "/admin";
        if (role.equalsIgnoreCase("lecturer")) return "/lecturer";
        return "/student";
    }
}