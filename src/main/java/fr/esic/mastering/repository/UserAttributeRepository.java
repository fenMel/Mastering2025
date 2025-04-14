package fr.esic.mastering.repository;

import fr.esic.mastering.entities.UserAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAttributeRepository extends JpaRepository<UserAttribute, Long> {
    List<UserAttribute> findByUserId(Long userId);

    Optional<UserAttribute> findByUserIdAndAttributeKey(Long userId, String attributeKey);

    void deleteByUserIdAndAttributeKey(Long userId, String attributeKey);
}