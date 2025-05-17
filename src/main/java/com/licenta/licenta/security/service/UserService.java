package com.licenta.licenta.security.service;

import com.licenta.licenta.security.dto.UserDTO;
import com.licenta.licenta.security.mapper.UserMapper;
import com.licenta.licenta.security.model.Authority;
import com.licenta.licenta.security.model.User;
import com.licenta.licenta.security.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public boolean isClient(Long id) {
        User user = getUserById(id);
        return user.getAuthority().equals(Authority.CLIENT);
    }

    public Set<String> getUserIdsFromUserIdsAndRolesIds(Set<String> ids) {
        Set<String> userIds = new HashSet<>();
        Set<Long> roleIds = new HashSet<>();

        ids.forEach(id -> {
            char type = id.charAt(id.length() - 1);
            String numberPart = id.substring(0, id.length() - 1);

            try {
                if (type == 'e') {
                    userIds.add(numberPart);
                } else if (type == 'r') {
                    roleIds.add(Long.valueOf(numberPart));
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid ID format: " + id);
            }
        });

        getAllUsersByRoleIds(roleIds).forEach(user -> userIds.add(String.valueOf(user.getId())));

        return userIds;
    }

    private List<User> getAllUsersByRoleIds(Set<Long> roleIds) {
        return userRepository.findAllByRoleIdIn(roleIds);
    }

    public List<UserDTO> getAllUsersByOrganisationId(Long organisationId) {
        return userMapper.allToDTO(userRepository.findAllByOrganisationIdAndAuthority(organisationId, Authority.EMPLOYEE)).stream()
                .peek(userDTO -> userDTO.setPassword(null)).collect(Collectors.toList());
    }

    public UserDTO saveUser(UserDTO userDTO) {
        userDTO.setAuthority(String.valueOf(Authority.EMPLOYEE));
        UserDTO savedUserDTO = userMapper.toDTO(userRepository.save(userMapper.toEntity(userDTO)));
        savedUserDTO.setPassword(null);
        return savedUserDTO;
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
