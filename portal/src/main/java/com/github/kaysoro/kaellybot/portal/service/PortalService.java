package com.github.kaysoro.kaellybot.portal.service;

import com.github.kaysoro.kaellybot.portal.model.entity.Portal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PortalService implements IPortalService {

    @Override
    public Mono<Portal> getPortal(String server, String dimension){
        // TODO
        return Mono.just(new Portal());
    }
}
