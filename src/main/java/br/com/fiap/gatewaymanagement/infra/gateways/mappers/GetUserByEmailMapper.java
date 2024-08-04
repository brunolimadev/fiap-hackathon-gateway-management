package br.com.fiap.gatewaymanagement.infra.gateways.mappers;

import br.com.fiap.gatewaymanagement.domain.User;
import br.com.fiap.gatewaymanagement.domain.enums.UserRoleEnum;
import br.com.fiap.gatewaymanagement.infra.enums.RoleEnum;
import br.com.fiap.gatewaymanagement.infra.models.response.GetUserByEmailResponse;

public class GetUserByEmailMapper {

    private GetUserByEmailMapper() {
    }

    public static GetUserByEmailResponse fromDomain(User user) {
        GetUserByEmailResponse userDto = new GetUserByEmailResponse();
        userDto.setActive(user.isActive());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        userDto.setRole(RoleEnum.valueOf(user.getRole().name()));
        return userDto;
    }

    public static User toDomain(GetUserByEmailResponse userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.isActive(),
                userDto.getCreatedAt(),
                UserRoleEnum.valueOf(userDto.getRole().name()));
    }

}
