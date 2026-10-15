package com.skylinetransit.auth;

import com.skylinetransit.model.UserAccount;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthSessionService {
    public record SessionUser(String email, String role, String assignedBusId) {}
    private final Map<String, SessionUser> sessions = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();
    public String create(UserAccount user) { byte[] bytes=new byte[32]; random.nextBytes(bytes); String token=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); sessions.put(token,new SessionUser(user.getEmail(),user.getRole(),user.getAssignedBusId())); return token; }
    public SessionUser resolve(String token) { return token == null ? null : sessions.get(token.replaceFirst("(?i)^Bearer\\s+", "").trim()); }
    public boolean hasRole(String token, String role) { SessionUser u=resolve(token); return u != null && u.role().equalsIgnoreCase(role); }
}
