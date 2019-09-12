package com.codegod.sarafan.service;

import com.codegod.sarafan.domain.User;
import com.codegod.sarafan.domain.UserSubscription;
import com.codegod.sarafan.repo.UserDetailsRepo;
import com.codegod.sarafan.repo.UserSubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XE on 09.09.2019
 * @project sarafan
 */

@Service
public class ProfileService {

    private final UserDetailsRepo userDetailsRepo;
    private final UserSubscriptionRepo userSubscriptionRepo;

    @Autowired
    public ProfileService(UserDetailsRepo userDetailsRepo, UserSubscriptionRepo userSubscriptionRepo) {
        this.userDetailsRepo = userDetailsRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
    }

    public User changeSubscription(User channel, User subscriber) {
        List<UserSubscription> subscriptions = channel.getSubscribers()
                .stream()
                .filter(subscription ->
                        subscription.getSubscriber().equals(subscriber)
                )
                .collect(Collectors.toList());

        if (subscriptions.isEmpty()) {
            UserSubscription subscription = new UserSubscription(channel, subscriber);
            channel.getSubscribers().add(subscription);
        } else {
            channel.getSubscribers().removeAll(subscriptions);
        }
        return userDetailsRepo.save(channel);
    }

    public List<UserSubscription> getSubscribers(User channel) {
        return userSubscriptionRepo.findByChannel(channel);
    }

    public UserSubscription changeSubscriptionStatus(User channel, User subscriber) {
        UserSubscription subscription = userSubscriptionRepo.findByChannelAndSubscriber(channel, subscriber);
        if (subscription != null && !channel.getId().equals(subscriber.getId())) {
            subscription.setActive(!subscription.isActive());
            return userSubscriptionRepo.save(subscription);
        } else {
            UserSubscription userSubscription = new UserSubscription(subscriber, channel);
            userSubscription.setActive(true);
            return userSubscriptionRepo.save(userSubscription);
        }
    }
}
