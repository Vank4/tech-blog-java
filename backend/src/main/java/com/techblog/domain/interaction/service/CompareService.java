package com.techblog.domain.interaction.service;

import com.techblog.domain.interaction.dto.CompareResponse;

public interface CompareService {

    CompareResponse getCompare(String actorEmail);

    CompareResponse addItem(String actorEmail, Long productId);

    void removeItem(String actorEmail, Long productId);
}