package br.com.fiap.gatewaymanagement.infra.models.response;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.fiap.gatewaymanagement.infra.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserByEmailResponse implements Serializable, UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("role")
    private RoleEnum role;

    /**
     * Método responsável por retornar as autoridades do usuário
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (this.role == RoleEnum.ROLE_ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority(
                            RoleEnum.ROLE_ADMIN.getRole()),
                    new SimpleGrantedAuthority(RoleEnum.ROLE_USER.getRole()));
        } else {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    /**
     * Método responsável por retornar o username do usuário
     *
     * @return
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * Método responsável por retornar se a conta do usuário está expirada
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Método responsável por retornar se a conta do usuário está bloqueada
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    /**
     * Método responsável por retornar se as credenciais do usuário estão
     * expiradas
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Método responsável por retornar se o usuário está habilitado
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    /*
     * Getters and Setters
     */
    @Override
    public String getPassword() {
        return this.password;
    }
}
