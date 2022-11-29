package com.javaprogram.modulespringboot.actuator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.javaprogram.modulespringboot.model.Status;
import com.javaprogram.modulespringboot.repository.UserRepository;

@Component
public class UserStatusHealthIndicator implements HealthIndicator {
    private static final String CURRENT_ACTIVE_METRIC = "currentActive";
    private static final String CURRENT_DELETED_METRIC = "currentDeleted";
    private static final String DETAIL_NAME = "userStatus";

    @Value("${app.health.min-active-users-count}")
    private int minimumActiveUsersRequired;
    @Value("${app.health.max-deleted-users-count}")
    private int maximumDeletedUsersAllowed;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Health health() {
        Integer activeUsers = userRepository.countUserByStatus(Status.ACTIVE);
        Integer deletedUsers = userRepository.countUserByStatus(Status.DELETED);

        Map<String, Integer> userStatus = new HashMap<>();
        userStatus.put(CURRENT_ACTIVE_METRIC, activeUsers);
        userStatus.put(CURRENT_DELETED_METRIC, deletedUsers);

        if (activeUsers < minimumActiveUsersRequired || deletedUsers > maximumDeletedUsersAllowed) {
            return new Health.Builder().down().withDetail(DETAIL_NAME, userStatus).build();
        }
        return new Health.Builder().up().withDetail(DETAIL_NAME, userStatus).build();
    }
}
