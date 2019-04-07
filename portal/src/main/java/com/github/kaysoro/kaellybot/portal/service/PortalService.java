package com.github.kaysoro.kaellybot.portal.service;

import com.github.kaysoro.kaellybot.portal.model.constants.Dimension;
import com.github.kaysoro.kaellybot.portal.model.constants.Server;
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
    public Mono<Portal> findById(Server server, Dimension dimension){
        return portalRepository.findById(new PortalId()
                .withServer(server.getName())
                .withDimension(dimension.getName()));
    }

    @Override
    public Flux<Portal> findAllByPortalIdServer(Server server) {
        return portalRepository.findAllByPortalIdServer(server.getName());
    }
}