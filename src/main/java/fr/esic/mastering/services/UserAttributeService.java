package fr.esic.mastering.services;

import fr.esic.mastering.entities.UserAttribute;
import fr.esic.mastering.repository.UserAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserAttributeService {

    @Autowired
    private UserAttributeRepository userAttributeRepository;

    public void setAttribute(Long userId, String key, String value) {
        if (value == null || value.isEmpty()) {
            // Optionally skip or delete empty values
            return;
        }

        Optional<UserAttribute> existingAttr = userAttributeRepository.findByUserIdAndAttributeKey(userId, key);

        if (existingAttr.isPresent()) {
            UserAttribute attr = existingAttr.get();
            attr.setAttributeValue(value);
            userAttributeRepository.save(attr);
        } else {
            userAttributeRepository.save(new UserAttribute(userId, key, value));
        }
    }

    public String getAttribute(Long userId, String key) {
        return userAttributeRepository.findByUserIdAndAttributeKey(userId, key)
                .map(UserAttribute::getAttributeValue)
                .orElse(null);
    }

    public Map<String, String> getAllAttributes(Long userId) {
        List<UserAttribute> attributes = userAttributeRepository.findByUserId(userId);
        Map<String, String> attributeMap = new HashMap<>();

        attributes.forEach(attr -> attributeMap.put(attr.getAttributeKey(), attr.getAttributeValue()));

        return attributeMap;
    }

    public void deleteAttribute(Long userId, String key) {
        userAttributeRepository.deleteByUserIdAndAttributeKey(userId, key);
    }

    @Transactional
    public void deleteAllAttributes(Long userId) {
        List<UserAttribute> attributes = userAttributeRepository.findByUserId(userId);
        userAttributeRepository.deleteAll(attributes);
    }
}
