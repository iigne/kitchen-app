package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.MeasurementDTO;
import com.kitchenapp.kitchenappapi.dto.QuantityDTO;
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO;
import com.kitchenapp.kitchenappapi.dto.request.IngredientQuantityDTO;
import com.kitchenapp.kitchenappapi.helper.MeasurementConverter;
import com.kitchenapp.kitchenappapi.mapper.UserIngredientMapper;
import com.kitchenapp.kitchenappapi.model.*;
import com.kitchenapp.kitchenappapi.repository.IngredientRepository;
import com.kitchenapp.kitchenappapi.repository.UserIngredientRepository;
import com.kitchenapp.kitchenappapi.repository.UserRepository;
import com.kitchenapp.kitchenappapi.repository.projection.RecipeUserIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserIngredientService {

    private final UserIngredientRepository userIngredientRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final MeasurementService measurementService;

    public UserIngredientDTO create(final int userId, final UserIngredientDTO dto) {

        final int ingredientId = dto.getIngredient().getId();

        userIngredientRepository.findByUserIdAndIngredientId(userId, ingredientId).ifPresent(
                ui -> { throw new IllegalStateException(
                        String.format("userId %s and ingredientId %s already exists", userId, ingredientId));
                });

        UserIngredient userIngredient = userIngredientRepository.save(mapToEntity(userId, dto));
        return UserIngredientMapper.toDTO(userIngredient);
    }

    private UserIngredient mapToEntity(final int userId, final UserIngredientDTO dto) {
        final int ingredientId = dto.getIngredient().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("userId %s not found", userId)));
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("ingredientId %s not found", ingredientId)));

        Measurement measurement = measurementService.findByIdOrThrow(dto.getQuantity().getMeasurementId());
        return UserIngredientMapper.toEntity(dto, ingredient, user, measurement);
    }

    public List<UserIngredientDTO> listAllForUser(final int userId) {
        return UserIngredientMapper.toDTO(userIngredientRepository.findAllByUserId(userId));
    }

    public UserIngredientDTO updateQuantity(final int userId, int ingredientId, QuantityDTO dto) {

        UserIngredient userIngredient = userIngredientRepository.findByUserIdAndIngredientId(userId, ingredientId).orElseThrow(() ->
                new EntityNotFoundException(String.format("userId %s and ingredientId %s doesn't exist", userId, ingredientId)));

        final Integer measurementId = dto.getMeasurementId();
        Measurement measurement = measurementId != null ? measurementService.findByIdOrThrow(measurementId) : null;
        double metricQuantity = measurement == null ? dto.getQuantity() : MeasurementConverter.toMetric(dto.getQuantity(), measurement);
        userIngredient.setMetricQuantity(metricQuantity);
        //TODO handle measurement change
        return UserIngredientMapper.toDTO(userIngredientRepository.save(userIngredient));
    }

    public List<UserIngredient> updateQuantities(final int userId, List<IngredientQuantityDTO> ingredientQuantities) {
        Map<Integer, IngredientQuantityDTO> ingredientsById = ingredientQuantities.stream().collect(Collectors.toMap(IngredientQuantityDTO::getIngredientId, Function.identity()));
        List<Integer> measurementIds = ingredientQuantities.stream().map(IngredientQuantityDTO::getMeasurementId).collect(Collectors.toList());
        Map<Integer, Measurement> measurementsById = measurementService.findByIdsIn(measurementIds).stream().collect(Collectors.toMap(Measurement::getId, Function.identity()));

        List<UserIngredient> userIngredients = userIngredientRepository.findAllByUserIdAndIngredientIdIn(userId, new ArrayList<>(ingredientsById.keySet()));
        for(UserIngredient ingredient : userIngredients) {
            final int ingredientId = ingredient.getId().getIngredientId();
            IngredientQuantityDTO ingredientQuantity = ingredientsById.get(ingredientId);
            if(ingredientQuantity != null) {
                final int measurementId = ingredientQuantity.getMeasurementId();
                Measurement measurement = measurementsById.get(measurementId);
                if(measurement != null) {
                    double toRemoveMetricQuantity = MeasurementConverter.toMetricIfMetric(ingredientQuantity.getQuantity(), measurement);
                    double difference = ingredient.getMetricQuantity() - toRemoveMetricQuantity;
                    ingredient.setMetricQuantity(difference > 0 ? difference : 0);
                } else {
                    throw new EntityNotFoundException(String.format("measurementId %s does not exist", measurementId));
                }
            } else {
                throw new EntityNotFoundException(String.format("userId %s does not have ingredientId %s", userId, ingredientId));
            }
        }
        return userIngredientRepository.saveAll(userIngredients);
    }

    public List<UserIngredient> getByIds(final List<Integer> ingredientIds, final int userId) {
        return userIngredientRepository.findAllByUserIdAndIngredientIdIn(userId, ingredientIds);
    }

    public void delete(int id, int ingredientId) {
        userIngredientRepository.deleteByIngredientIdAndUserId(ingredientId, id);
    }

    public List<UserIngredient> saveAll(List<UserIngredient> ingredients) {
        return userIngredientRepository.saveAll(ingredients);
    }

}
