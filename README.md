# Spring Boot Authentication Starter Guide

This documentation outlines the standard approach for building a "pre-start" application (starter template) for Spring Boot 3+ with robust Authentication and Authorization capabilities.

**Key Features:**
- **Stateless Authentication**: Using JSON Web Tokens (JWT).
- **OAuth2 Integration**: Support for external providers like Google, GitHub, etc.
- **RBAC**: Role-Based Access Control implementation.

---

## 1. Project Dependencies

To implement this standard architecture, add the following dependencies to your `pom.xml`:

```xml
<!-- JWT Support (jjwt) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- OAuth2 Client -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>

<!-- Validation (Optional but recommended) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

## 2. Configuration Strategy

### application.yml
Configure OAuth2 providers and your custom JWT settings using environment variables for security.

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope: email, profile
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            client-name: Google
          # Add other providers (github, facebook) here

app:
  jwt:
    secret: ${JWT_SECRET}       # 256-bit+ HMAC SHA key
    expiration-ms: 86400000     # 24 hours
  frontend:
    redirect-url: "http://localhost:3000/oauth/callback" # Frontend handler
```

---

## 3. Architecture & Implementation Components

The standard way involves the following core components:

### A. Security Configuration (`SecurityConfig.java`)
Define a `SecurityFilterChain` bean that:
1.  **Disables CSRF** (necessary for stateless APIs).
2.  **Sets Session Policy**: `SessionCreationPolicy.STATELESS`.
3.  **Endpoint Security**: Uses `.requestMatchers()` to allow public access to auth endpoints and require authentication for others.
4.  **Filters**: Adds a `JwtAuthenticationFilter` **before** the standard `UsernamePasswordAuthenticationFilter`.
5.  **OAuth2**: Configures `.oauth2Login()` with a custom `AuthenticationSuccessHandler`.

### B. JWT Authentication Filter (`JwtAuthenticationFilter.java`)
This filter executes on every request:
1.  Extracts the `Authorization` header (Bearer token).
2.  Parses and validates the JWT.
3.  If valid, loads user details (roles/authorities).
4.  Sets the `Authentication` object in `SecurityContextHolder`.

### C. OAuth2 Success Handler (`OAuth2LoginSuccessHandler.java`)
Handles the flow after a user successfully logs in with Google:
1.  **Extracts User Info**: get email, name, avatar from `authentication.getPrincipal()`.
2.  **Upsert User**: Checks DB if user exists; if not, registers them automatically ("Just-In-Time" provisioning).
3.  **Generate JWT**: Creates a seamless JWT for the API.
4.  **Redirect**: Redirects to the Frontend URL with the JWT as a query parameter (or cookie), e.g., `?token=eyJ...`.

### D. Authentication Controller (`AuthController.java`)
Provides endpoints for standard (local) authentication:
- `POST /api/auth/login`: Validates username/password -> Returns JWT.
- `POST /api/auth/register`: Creates new local user -> Returns JWT.

---

## 4. Authentication Flows

### Standard Login (Username/Password)
1. Client sends POST to `/login` with JSON body.
2. `AuthenticationManager` authenticates credentials.
3. Server generates JWT Signed with HS256.
4. Server returns JSON: `{ "accessToken": "...", "tokenType": "Bearer" }`.

### OAuth2 Login (e.g., Google)
1. Frontend redirects user to backend: `/oauth2/authorization/google`.
2. User approves permissions on Google's page.
3. Google redirects back to backend `/login/oauth2/code/google`.
4. Spring Security exchanges code for Google Access Token.
5. `OAuth2LoginSuccessHandler` triggers:
   - Saves user to DB.
   - Generates App JWT.
   - Redirects to Frontend: `http://localhost:3000/oauth/callback?token=...`.

---

## 5. Development Tips
- **Secrets**: Never hardcode secrets. Use `.env` file or IDE environment variables.
- **CORS**: Ensure `CorsConfigurationSource` is bean-defined to allow requests from your frontend (localhost:3000, etc.).
- **Exception Handling**: Implement an `AuthenticationEntryPoint` to return proper 401 JSON errors instead of HTML login pages when tokens are invalid.
