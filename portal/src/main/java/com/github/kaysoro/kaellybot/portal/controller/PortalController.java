package com.github.kaysoro.kaellybot.portal.controller;

import com.github.kaysoro.kaellybot.portal.mapper.PortalMapper;
import com.github.kaysoro.kaellybot.portal.model.constants.Dimension;
import com.github.kaysoro.kaellybot.portal.model.constants.Server;
import com.github.kaysoro.kaellybot.portal.model.dto.ExternalPortalDto;
import com.github.kaysoro.kaellybot.portal.model.dto.PortalDto;
import com.github.kaysoro.kaellybot.portal.service.IPortalService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.kaysoro.kaellybot.portal.controller.PortalConstants.*;

@RestController
@RequestMapping(API)
public class PortalController {

    private IPortalService service;

    public PortalController(IPortalService service){
        this.service = service;
    }

    @GetMapping(path = SERVER_VAR + PORTALS, params = { DIMENSION_VAR, TOKEN_VAR })
    public Mono<PortalDto> findById(@PathVariable(value="server") String serverName,
                                    @RequestParam(value="dimension") String dimensionName, @RequestParam String token){
        // TODO
        Server server = Server.valueOf(serverName.toUpperCase());
        if (server == null){
            // TODO
        }

        Dimension dimension = Dimension.valueOf(dimensionName.toUpperCase());
        if (dimension == null){
            // TODO
        }

        return service.findById(server, dimension)
                .map(PortalMapper::map);
    }

    @GetMapping(path = SERVER_VAR + PORTALS, params = { TOKEN_VAR })
    public Flux<PortalDto> findAllByPortalIdServer(@PathVariable(value="server") String serverName,
                                                   @RequestParam String token){
        // TODO
        Server server = Server.valueOf(serverName.toUpperCase());
        if (server == null){
            // TODO
        }
        return service.findAllByPortalIdServer(server)
                .map(PortalMapper::map);
    }

    @PostMapping(path= SERVER_VAR + PORTALS, params = { DIMENSION_VAR, TOKEN_VAR }, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> addPortal(@PathVariable(value="server") String serverName, @RequestParam String dimension,
                                       @RequestParam String token, @RequestBody ExternalPortalDto coodinates){
        // TODO
        Server server = Server.valueOf(serverName.toUpperCase());

        if (server == null){
            // TODO
        }
        return Mono.empty();
    }
}