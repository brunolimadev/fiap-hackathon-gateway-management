package br.com.fiap.gatewaymanagement.infra.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.gatewaymanagement.application.usecases.GenerateJwtInteractor;
import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.domain.enums.UserRoleEnum;
import br.com.fiap.gatewaymanagement.infra.enums.RoleEnum;
import br.com.fiap.gatewaymanagement.infra.models.dtos.AuthDto;
import br.com.fiap.gatewaymanagement.infra.models.dtos.SignInResponseDto;
import br.com.fiap.gatewaymanagement.infra.models.response.GetUserByEmailResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AuthControllerTest {

        private WebTestClient webTestClient;

        private AuthController authController;

        @Mock
        private ReactiveAuthenticationManager authenticationManager;

        @Mock
        private GenerateJwtInteractor generateJwtInteractor;

        @BeforeEach
        void setup() {
                MockitoAnnotations.openMocks(this);
                authController = new AuthController(authenticationManager, generateJwtInteractor);
                webTestClient = WebTestClient.bindToController(authController).build();
        }

        @Test
        void testSignIn() throws Exception {
                // Mock input data
                AuthDto credentials = new AuthDto("test@example.com", "password");

                // Mock authentication result
                Authentication authentication = createMockAuthentication();
                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(Mono.just(authentication));

                // Mock user details
                User user = new User("teste", "mail@mail.com.br", "Teste@123", LocalDateTime.now(),
                                UserRoleEnum.ROLE_ADMIN);
                GetUserByEmailResponse getUserByEmailResponse = new GetUserByEmailResponse();
                getUserByEmailResponse.setName(user.getName());
                getUserByEmailResponse.setEmail(user.getEmail());
                getUserByEmailResponse.setRole(RoleEnum.valueOf(user.getRole().name()));
                getUserByEmailResponse.setActive(true);
                getUserByEmailResponse.setCreatedAt(user.getCreatedAt());
                getUserByEmailResponse.setId(user.getId());

                when(authentication.getPrincipal()).thenReturn(getUserByEmailResponse);

                // Mock JWT generation
                String jwt = "mocked-jwt";
                when(generateJwtInteractor.execute(user)).thenReturn(jwt);

                String bearer = String.format("Bearer %s", jwt);

                SignInResponseDto response = new SignInResponseDto(bearer);

                webTestClient.post().uri("/api/autenticacao")
                                .bodyValue(credentials)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(SignInResponseDto.class);

                Assertions.assertThat(getUserByEmailResponse.getAuthorities().iterator().next().getAuthority())
                                .isEqualTo("ADMIN");
                Assertions.assertThat(getUserByEmailResponse.getEmail()).isEqualTo(user.getEmail());
                Assertions.assertThat(getUserByEmailResponse.getName()).isEqualTo(user.getName());
                Assertions.assertThat(getUserByEmailResponse.getRole()).isEqualTo(RoleEnum.ROLE_ADMIN);
                Assertions.assertThat(getUserByEmailResponse.isActive()).isTrue();
                Assertions.assertThat(getUserByEmailResponse.getCreatedAt()).isEqualTo(user.getCreatedAt());
                Assertions.assertThat(getUserByEmailResponse.getId()).isEqualTo(user.getId());
                Assertions.assertThat(getUserByEmailResponse.getUsername()).isEqualTo(user.getEmail());
                Assertions.assertThat(getUserByEmailResponse.isAccountNonExpired()).isTrue();
                Assertions.assertThat(getUserByEmailResponse.isAccountNonLocked()).isTrue();
                Assertions.assertThat(getUserByEmailResponse.isCredentialsNonExpired()).isTrue();
                Assertions.assertThat(getUserByEmailResponse.isEnabled()).isTrue();

        }

        @Test
        void testSignIn_AndReturnError() throws Exception {
                // Mock input data
                AuthDto credentials = new AuthDto("test@example.com", "password");

                // Mock authentication result
                Authentication authentication = createMockAuthentication();
                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(Mono.just(authentication));

                // Mock user details
                User user = new User("teste", "mail@mail.com.br", "Teste@123", LocalDateTime.now(),
                                UserRoleEnum.ROLE_ADMIN);

                // Mock JWT generation
                String jwt = "mocked-jwt";
                when(generateJwtInteractor.execute(user)).thenCallRealMethod();

                // Mock response entity
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

                webTestClient.post().uri("/api/autenticacao")
                                .bodyValue(credentials)
                                .exchange()
                                .expectStatus().isBadRequest();

        }

        private Authentication createMockAuthentication() {
                Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
                SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
                UserDetails userDetails = org.mockito.Mockito.mock(UserDetails.class);

                when(securityContext.getAuthentication()).thenReturn(authentication);
                SecurityContextHolder.setContext(securityContext);
                when(authentication.getPrincipal()).thenReturn(userDetails);

                return authentication;
        }

        @Test
        void testSignInWithError() throws Exception {
                // Mock input data
                AuthDto credentials = new AuthDto("test@example.com", "password");

                // Mock authentication error
                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                "Authentication failed")));

                // Test the sign-in endpoint with error
                Mono<ResponseEntity<SignInResponseDto>> result = authController.signIn(credentials);
                StepVerifier.create(result)
                                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                                .verifyComplete();
        }
}