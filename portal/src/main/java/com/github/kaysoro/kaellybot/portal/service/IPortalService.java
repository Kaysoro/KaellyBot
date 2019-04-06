package com.github.kaysoro.kaellybot.portal.service;

import com.github.kaysoro.kaellybot.portal.model.entity.Portal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPortalService {

    Mono<Portal> findById(String server, String dimension);

    Flux<Portal> findAllByPortalIdServer(String server);
}
