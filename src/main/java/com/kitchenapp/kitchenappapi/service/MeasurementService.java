package com.kitchenapp.kitchenappapi.service;

import com.kitchenapp.kitchenappapi.dto.QuantityDTO;
import com.kitchenapp.kitchenappapi.model.Measurement;
import com.kitchenapp.kitchenappapi.repository.MeasurementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    public Measurement findByIdOrThrow(final int id) {
        return measurementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("measurementId %s not found", id)));
    }

}
