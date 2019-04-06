package com.github.kaysoro.kaellybot.portal.controller;

import com.github.kaysoro.kaellybot.portal.mapper.PortalMapper;
import com.github.kaysoro.kaellybot.portal.model.dto.ExternalPortalDto;
import com.github.kaysoro.kaellybot.portal.model.dto.PortalDto;
import com.github.kaysoro.kaellybot.portal.service.IPortalService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.github.kaysoro.kaellybot.portal.controller.PortalConstants.*;

@RestController
@RequestMapping(API)
public class PortalController {

    private IPortalService service;

    public PortalController(IPortalService service){
        this.service = service;
    }

    @GetMapping(path = SERVER_VAR + PORTALS, params = { DIMENSION_VAR, TOKEN_VAR })
    public Mono<PortalDto> findById(@PathVariable String server, @RequestParam String dimension,
                                          @RequestParam String token){
        // TODO
        return service.findById(server, dimension)
                .map(PortalMapper::map);
    }

    @GetMapping(path = SERVER_VAR + PORTALS, params = { TOKEN_VAR })
    public Flux<PortalDto> findAllByPortalIdServer(@PathVariable String server, @RequestParam String token){
        // TODO
        return service.findAllByPortalIdServer(server)
                .map(PortalMapper::map);
    }

    @PostMapping(path= SERVER_VAR + PORTALS, params = { DIMENSION_VAR, TOKEN_VAR }, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> addPortal(@PathVariable String server, @RequestParam String dimension,
                                       @RequestParam String token, @RequestBody ExternalPortalDto coodinates){
        // TODO
        return Mono.empty();
    }
}