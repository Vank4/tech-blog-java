package com.techblog.domain.interaction.service;

import com.techblog.common.enums.TargetType;
import com.techblog.domain.interaction.dto.InteractionStatusResponse;
import com.techblog.domain.interaction.model.UserFavorite;
import com.techblog.domain.interaction.model.UserLike;
import com.techblog.domain.interaction.repository.UserFavoriteRepository;
import com.techblog.domain.interaction.repository.UserLikeRepository;
import com.techblog.domain.user.model.User;
import com.techblog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final UserLikeRepository userLikeRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public InteractionStatusResponse toggleLike(Long userId, TargetType targetType, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<UserLike> existingLike = userLikeRepository.findByUserAndTargetTypeAndTargetId(user, targetType, targetId);

        if (existingLike.isPresent()) {
            userLikeRepository.delete(existingLike.get());
        } else {
            UserLike newLike = new UserLike();
            newLike.setUser(user);
            newLike.setTargetType(targetType);
            newLike.setTargetId(targetId);
            userLikeRepository.save(newLike);
        }

        return getInteractionStatus(userId, targetType, targetId);
    }

    @Override
    @Transactional
    public InteractionStatusResponse toggleFavorite(Long userId, TargetType targetType, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<UserFavorite> existingFav = userFavoriteRepository.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);

        if (existingFav.isPresent()) {
            userFavoriteRepository.delete(existingFav.get());
        } else {
            UserFavorite newFav = new UserFavorite();
            newFav.setUser(user);
            newFav.setTargetType(targetType);
            newFav.setTargetId(targetId);
            userFavoriteRepository.save(newFav);
        }

        return getInteractionStatus(userId, targetType, targetId);
    }

    @Override
    @Transactional(readOnly = true)
    public InteractionStatusResponse getInteractionStatus(Long userId, TargetType targetType, Long targetId) {
        boolean isLiked = false;
        boolean isSaved = false;

        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                isLiked = userLikeRepository.existsByUserAndTargetTypeAndTargetId(user, targetType, targetId);
                isSaved = userFavoriteRepository.existsByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
            }
        }

        long likeCount = userLikeRepository.countByTargetTypeAndTargetId(targetType, targetId);
        long saveCount = userFavoriteRepository.countByTargetTypeAndTargetId(targetType, targetId);

        return InteractionStatusResponse.builder()
                .isLiked(isLiked)
                .isSaved(isSaved)
                .likeCount(likeCount)
                .saveCount(saveCount)
                .build();
    }
}
