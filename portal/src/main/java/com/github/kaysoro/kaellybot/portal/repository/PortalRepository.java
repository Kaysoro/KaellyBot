package com.github.kaysoro.kaellybot.portal.repository;

import com.github.kaysoro.kaellybot.portal.model.entity.Portal;
import com.github.kaysoro.kaellybot.portal.model.entity.PortalId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PortalRepository extends ReactiveMongoRepository<Portal, PortalId> {

    Flux<Portal> findAllByPortalIdServer(String server);
}