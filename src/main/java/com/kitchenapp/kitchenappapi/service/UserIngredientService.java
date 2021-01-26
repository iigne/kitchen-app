package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO;
import com.kitchenapp.kitchenappapi.mapper.UserIngredientMapper;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.User;
import com.kitchenapp.kitchenappapi.model.UserIngredient;
import com.kitchenapp.kitchenappapi.repository.IngredientRepository;
import com.kitchenapp.kitchenappapi.repository.MeasurementRepository;
import com.kitchenapp.kitchenappapi.repository.UserIngredientRepository;
import com.kitchenapp.kitchenappapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserIngredientService {

    private final UserIngredientRepository userIngredientRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final MeasurementRepository measurementRepository;

    public UserIngredientDTO create(final int userId, final UserIngredientDTO dto) {
        userIngredientRepository.findByUserIdAndIngredientId(userId, dto.getIngredientId()).ifPresent(
                ui -> { throw new IllegalStateException(
                        String.format("userId %s and ingredientId %s already exists", userId, dto.getIngredientId()));
                });
        if(dto.getQuantity() == null && dto.getMetricQuantity() == null) {
            throw new IllegalArgumentException("No quantity specified");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("userId %s not found", userId)));
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("ingredientId %s not found", dto.getIngredientId())));

        Measurement measurement = null;
        if(dto.getQuantity() != null) {
            measurement = measurementRepository.findById(dto.getQuantity().getMeasurementId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("measurementId %s not found", dto.getQuantity().getMeasurementId())));
        }

        UserIngredient userIngredient = userIngredientRepository.save(UserIngredientMapper.toEntity(dto, ingredient, user, measurement));
        return UserIngredientMapper.toDTO(userIngredient);
    }

    public List<UserIngredientDTO> listAllForUser(final int userId) {
        return UserIngredientMapper.toDTO(userIngredientRepository.findAllByUserId(userId));
    }

}
