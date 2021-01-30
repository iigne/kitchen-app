package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.QuantityDTO;
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.UserIngredientMapper;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.model.User;
import com.kitchenapp.kitchenappapi.model.UserIngredient;
import com.kitchenapp.kitchenappapi.repository.IngredientRepository;
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
    private final MeasurementService measurementService;

    public UserIngredientDTO create(final int userId, final UserIngredientDTO dto) {

        final int ingredientId = dto.getIngredientId();

        //TODO this looks like soemthing to be handled in controller
        if(dto.getQuantity() == null && dto.getMetricQuantity() == null) {
            throw new IllegalArgumentException("No quantity specified");
        }
        userIngredientRepository.findByUserIdAndIngredientId(userId, ingredientId).ifPresent(
                ui -> { throw new IllegalStateException(
                        String.format("userId %s and ingredientId %s already exists", userId, ingredientId));
                });
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("userId %s not found", userId)));
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("ingredientId %s not found", ingredientId)));

        Measurement measurement = dto.getQuantity() != null ? measurementService.findByIdOrThrow(dto.getQuantity().getMeasurementId()) : null;

        UserIngredient userIngredient = userIngredientRepository.save(UserIngredientMapper.toEntity(dto, ingredient, user, measurement));
        return UserIngredientMapper.toDTO(userIngredient);
    }

    public List<UserIngredientDTO> listAllForUser(final int userId) {
        return UserIngredientMapper.toDTO(userIngredientRepository.findAllByUserId(userId));
    }

    public UserIngredientDTO update(final int userId, final UserIngredientDTO dto) {

//        final int ingredientId = dto.getIngredientId();
//
//        UserIngredient userIngredient = userIngredientRepository.findByUserIdAndIngredientId(userId, ingredientId).orElseThrow(() ->
//                new EntityNotFoundException(String.format("userId %s and ingredientId %s doesn't exist", userId, ingredientId)));
//
//
        return null;
    }

    public UserIngredientDTO updateQuantity(final int userId, int ingredientId, QuantityDTO dto) {

        UserIngredient userIngredient = userIngredientRepository.findByUserIdAndIngredientId(userId, ingredientId).orElseThrow(() ->
                new EntityNotFoundException(String.format("userId %s and ingredientId %s doesn't exist", userId, ingredientId)));

        final Integer measurementId = dto.getMeasurementId();
        Measurement measurement = measurementId != null ? measurementService.findByIdOrThrow(measurementId) : null;
        double metricQuantity = measurement == null ? dto.getQuantity() : MeasurementConverter.toMetric(dto.getQuantity(), measurement);
        userIngredient.setMetricQuantity(metricQuantity);
        return UserIngredientMapper.toDTO(userIngredientRepository.save(userIngredient));
    }

    public void delete(int id, int ingredientId) {
        userIngredientRepository.deleteByIngredientIdAndUserId(ingredientId, id);
    }

}
