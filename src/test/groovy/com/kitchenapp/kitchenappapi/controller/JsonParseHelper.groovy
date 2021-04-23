package com.kitchenapp.kitchenappapi.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.kitchenapp.kitchenappapi.dto.IngredientDTO
import com.kitchenapp.kitchenappapi.dto.ShoppingListItemDTO
import com.kitchenapp.kitchenappapi.dto.UserIngredientDTO
import com.kitchenapp.kitchenappapi.dto.response.ResponseRecipeDTO
import com.kitchenapp.kitchenappapi.error.ApiError


/**
 * Serialises and deserialises JSON requests/responses for mvc integration tests
 */
class JsonParseHelper {

    static String toJson(object) {
        return new ObjectMapper().writeValueAsString(object)
    }

    static IngredientDTO toIngredientDTO(json) {
        return new ObjectMapper().readValue(json, IngredientDTO.class)
    }

    static List<UserIngredientDTO> toUserIngredientDTOList(json) {
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        return objectMapper.readValue(json, new TypeReference<List<UserIngredientDTO>>() {})
    }

    static ApiError toApiError(json) {
        return new ObjectMapper().readValue(json, ApiError.class)
    }

    static List<ShoppingListItemDTO> toShoppingItemDTOList(json) {
        ObjectMapper objectMapper = new ObjectMapper()
        return objectMapper.readValue(json, new TypeReference<List<ShoppingListItemDTO>>() {})
    }

    static List<ResponseRecipeDTO> toRecipeDTOList(json) {
        ObjectMapper objectMapper = new ObjectMapper()
        return objectMapper.readValue(json, new TypeReference<List<ResponseRecipeDTO>>() {})
    }

}
