package cz.muni.ics.perunproxyapi.presentation.rest.controllers;

import cz.muni.ics.perunproxyapi.application.facade.impl.ProxyuserFacadeImpl;
import cz.muni.ics.perunproxyapi.persistence.exceptions.EntityNotFoundException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.InvalidRequestParameterException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProxyUserProtectedControllerTest {

    private final ProxyuserFacadeImpl facade;
    private final ProxyUserProtectedController controller;

    private static final String IDP_ENTITY_ID_DECODED = "testIdpEntityId";
    private static final String IDP_ENTITY_ID_ENCODED = Base64Utils.encodeToUrlSafeString(
            IDP_ENTITY_ID_DECODED.getBytes());
    private static final String USERS_LOGIN = "usersLogin";

    private final List<String> uids = new ArrayList<>(Arrays.asList("firstUid", "secondUid", "thirdUid"));


    @Autowired
    ProxyUserProtectedControllerTest() {
        this.facade = mock(ProxyuserFacadeImpl.class);
        this.controller = new ProxyUserProtectedController(facade);
    }


    @Test
    public void FindByExtLoginsCallsFacadesMethodFindByExtLogins() throws PerunUnknownException, PerunConnectionException, EntityNotFoundException, InvalidRequestParameterException {
        controller.findByExtLogins(IDP_ENTITY_ID_ENCODED, uids, new ArrayList<>());

        verify(facade, times(1)).findByExtLogins(IDP_ENTITY_ID_DECODED, uids, new ArrayList<>());
    }

    @Test
    public void getUserByLoginCallsFacadesMethodGetUserByLogin() throws PerunUnknownException, PerunConnectionException, EntityNotFoundException, InvalidRequestParameterException {
        List<String> list = new ArrayList<>();
        controller.getUserByLogin(USERS_LOGIN, list);

        verify(facade, times(1)).getUserByLogin(USERS_LOGIN, list);
    }

    @Test
    public void findByPerunUserIdCallsFacadesMethodFindByPerunUserId() throws PerunUnknownException, PerunConnectionException, EntityNotFoundException, InvalidRequestParameterException {
        List<String> list = new ArrayList<>();
        controller.findByPerunUserId(1L, new ArrayList<>());

        verify(facade, times(1)).findByPerunUserId(1L, new ArrayList<>());
    }

}
