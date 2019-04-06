package com.github.kaysoro.kaellybot.portal.service;

import com.github.kaysoro.kaellybot.portal.model.entity.Portal;
import com.github.kaysoro.kaellybot.portal.model.entity.PortalId;
import com.github.kaysoro.kaellybot.portal.repository.PortalRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PortalService implements IPortalService {

    private PortalRepository portalRepository;

    public PortalService(PortalRepository portalRepository){
        this.portalRepository = portalRepository;
    }

    @Override
    public Mono<Portal> getPortal(String server, String dimension){
        return portalRepository.findById(new PortalId()
                .withServer(server)
                .withDimension(dimension));
    }

    @Override
    public Flux<Portal> getPortals(String server) {
        return portalRepository.findAllByPortalIdServerIsLike(server);
    }
}