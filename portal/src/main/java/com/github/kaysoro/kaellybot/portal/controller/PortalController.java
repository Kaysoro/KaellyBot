package com.github.kaysoro.kaellybot.portal.controller;

import com.github.kaysoro.kaellybot.portal.mapper.PortalMapper;
import com.github.kaysoro.kaellybot.portal.model.dto.PortalDto;
import com.github.kaysoro.kaellybot.portal.service.IPortalService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.github.kaysoro.kaellybot.portal.constants.PortalConstants.*;

@RestController
@RequestMapping(API_PORTALS)
public class PortalController {

    private IPortalService service;

    public PortalController(IPortalService service){
        this.service = service;
    }

    @GetMapping(path = SERVER_VAR, params = { DIMENSION_VAR, TOKEN_VAR })
    public Mono<PortalDto> getPortal(@PathVariable String server, @RequestParam String dimension,
                                          @RequestParam String token){
        // TODO
        return service.getPortal(server, dimension)
                .map(PortalMapper::map);
    }

    @GetMapping(path = SERVER_VAR, params = { TOKEN_VAR })
    public Flux<PortalDto> getPortal(@PathVariable String server, @RequestParam String token){
        // TODO
        return service.getPortals(server)
                .map(PortalMapper::map);
    }

    @PostMapping(path= SERVER_VAR, params = { DIMENSION_VAR, TOKEN_VAR }, consumes = "application/json")
    public Mono<String> addPortal(@PathVariable String server, @RequestParam String dimension,
                                       @RequestParam String token, @RequestBody PortalDto coodinates){
        // TODO
        return Mono.empty();
    }

}