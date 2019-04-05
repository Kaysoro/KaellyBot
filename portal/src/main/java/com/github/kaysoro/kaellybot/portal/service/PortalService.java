package com.github.kaysoro.kaellybot.portal.service;

import com.github.kaysoro.kaellybot.portal.model.entity.Portal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PortalService implements IPortalService {

    @Override
    public Mono<Portal> getPortal(String server, String dimension){
        // TODO
        return Mono.just(new Portal());
    }

    @Override
    public Flux<Portal> getPortals(String server) {
        // TODO
        return Flux.just(new Portal(), new Portal());
    }


}
