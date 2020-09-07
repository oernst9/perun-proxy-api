package cz.muni.ics.perunproxyapi.presentation.rest.controllers;

import cz.muni.ics.perunproxyapi.application.facade.RelyingPartyFacade;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cz.muni.ics.perunproxyapi.presentation.rest.config.PathConstants.AUTH_PATH;
import static cz.muni.ics.perunproxyapi.presentation.rest.config.PathConstants.RELYING_PARTY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * Controller containing methods related to proxy user. Basic Auth is required.
 * methods path: /CONTEXT_PATH/auth/relying-party/**
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@RestController
@RequestMapping(value = AUTH_PATH + RELYING_PARTY)
@Slf4j
public class RelyingPartyProtectedController {

    private final RelyingPartyFacade facade;

    @Autowired
    public RelyingPartyProtectedController(RelyingPartyFacade facade) {
        this.facade = facade;
    }

    @GetMapping(value = "/{rp-identifier}/proxy-user/{login}/entitlements", produces = APPLICATION_JSON_VALUE)
    public List<String> getEntitlements(@PathVariable("rp-identifier") String rpIdentifier,
                                        @PathVariable("login") String login)
            throws PerunUnknownException, PerunConnectionException {
        return facade.getEntitlements(rpIdentifier, login);
    }

}
