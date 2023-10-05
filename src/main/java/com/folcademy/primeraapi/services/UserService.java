package com.folcademy.primeraapi.services;

import com.folcademy.primeraapi.exceptions.exceptionkinds.UserNotFoundException;
import com.folcademy.primeraapi.models.Dtos.UserAddDTO;
import com.folcademy.primeraapi.models.Dtos.UserReadDTO;
import com.folcademy.primeraapi.models.entities.UserEntity;
import com.folcademy.primeraapi.models.mappers.UserMapper;
import com.folcademy.primeraapi.models.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserReadDTO> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::userEntityToUserReadDTO)
                .collect(Collectors.toList());
    }

    public UserReadDTO add(UserAddDTO userAddDTO) {
        return userMapper.userEntityToUserReadDTO(
                userRepository.save(
                        userMapper.userAddDTOToUserEntity(userAddDTO)
                )
        );
    }

    public UserReadDTO findById(Integer userId) {
        return userRepository
                .findById(userId)
                .map(userEntity -> userMapper.userEntityToUserReadDTO(userEntity))
                .orElseThrow(()-> new UserNotFoundException("No se encontro un usuario con ese identificador"));
    }

    public UserReadDTO findUserById(Integer userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if(userEntityOptional.isEmpty()) {
            throw new UserNotFoundException("No se encontro un usuario con ese identificador");
        }

        return userMapper.userEntityToUserReadDTO(userEntityOptional.get());
    }
}
