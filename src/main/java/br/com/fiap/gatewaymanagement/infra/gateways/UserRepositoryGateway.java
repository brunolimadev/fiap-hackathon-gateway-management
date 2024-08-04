package br.com.fiap.gatewaymanagement.infra.gateways;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.fiap.gatewaymanagement.application.gateways.UserGateway;
import br.com.fiap.gatewaymanagement.domain.User;

public class UserRepositoryGateway implements UserGateway, UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

    @Override
    public User getUserByEmail(String email) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserByEmail'");
    }

}
