package com.github.kaysoro.kaellybot.portal.service;

import com.github.kaysoro.kaellybot.portal.model.constants.Dimension;
import com.github.kaysoro.kaellybot.portal.model.constants.Server;
import com.github.kaysoro.kaellybot.portal.model.entity.Portal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPortalService {

    Mono<Portal> findById(Server server, Dimension dimension);

    Flux<Portal> findAllByPortalIdServer(Server server);
}
