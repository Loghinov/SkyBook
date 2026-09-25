# JWT Auth Flow

**Description**: Stateless authentication: login/register → JWT issued → filter validates on every request → `SecurityContextHolder` populated. Use as the reference when modifying auth or adding token claims.

## Template
```
Login/Register:
  AuthController → AuthenticationManager.authenticate()
               → JwtUtil.generateToken(userDetails, role, userId)
               → return AuthResponse{token, username, role, userId, fullName}

Per-request filter (JwtAuthenticationFilter extends OncePerRequestFilter):
  1. Extract "Bearer <token>" from Authorization header
  2. jwtUtil.extractUsername(token) → username
  3. userDetailsService.loadUserByUsername(username)
  4. jwtUtil.isTokenValid(token, userDetails) → set SecurityContext

Token payload claims: subject=username, role=ROLE_X, userId=<Long>

JwtUtil key methods:
  generateToken(UserDetails, String role, Long userId) → compact JWT
  extractUsername(token) → subject claim
  isTokenValid(token, userDetails) → username match && not expired
```
