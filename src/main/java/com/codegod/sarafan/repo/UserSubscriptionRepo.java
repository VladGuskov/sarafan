package com.codegod.sarafan.repo;

import com.codegod.sarafan.domain.User;
import com.codegod.sarafan.domain.UserSubscription;
import com.codegod.sarafan.domain.UserSubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author XE on 10.09.2019
 * @project sarafan
 */

public interface UserSubscriptionRepo extends JpaRepository<UserSubscription, UserSubscriptionId> {
    List<UserSubscription> findBySubscriber(User user);

    List<UserSubscription> findByChannel(User channel);

    UserSubscription findByChannelAndSubscriber(User channel, User subscriber);
}
