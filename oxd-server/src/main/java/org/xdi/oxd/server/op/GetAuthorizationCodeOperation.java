package org.xdi.oxd.server.op;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xdi.oxauth.client.AuthorizationRequest;
import org.xdi.oxauth.client.AuthorizationResponse;
import org.xdi.oxauth.client.AuthorizeClient;
import org.xdi.oxauth.client.ClientUtils;
import org.xdi.oxauth.model.common.Prompt;
import org.xdi.oxauth.model.common.ResponseType;
import org.xdi.oxd.common.Command;
import org.xdi.oxd.common.CommandResponse;
import org.xdi.oxd.common.params.GetAuthorizationCodeParams;
import org.xdi.oxd.common.response.GetAuthorizationCodeResponse;
import org.xdi.oxd.server.service.SiteConfiguration;

import java.util.List;
import java.util.UUID;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 06/10/2015
 */

public class GetAuthorizationCodeOperation extends BaseOperation {

    private static final Logger LOG = LoggerFactory.getLogger(GetAuthorizationCodeOperation.class);

    /**
     * Base constructor
     *
     * @param p_command command
     */
    protected GetAuthorizationCodeOperation(Command p_command, final Injector injector) {
        super(p_command, injector);
    }

    @Override
    public CommandResponse execute() {
        try {
            final GetAuthorizationCodeParams params = asParams(GetAuthorizationCodeParams.class);
            final SiteConfiguration site = getSite(params.getOxdId());

            final AuthorizationRequest request = new AuthorizationRequest(responseTypes(site.getResponseTypes()),
                    site.getClientId(), site.getScope(), site.getAuthorizationRedirectUri(), UUID.randomUUID().toString());
            request.setState("af0ifjsldkj");
            request.setAuthUsername(params.getUsername());
            request.setAuthPassword(params.getPassword());
            request.getPrompts().add(Prompt.NONE);
            request.setNonce(UUID.randomUUID().toString());
            request.setAcrValues(acrValues(params, site));

            final AuthorizeClient authorizeClient = new AuthorizeClient(getDiscoveryService().getConnectDiscoveryResponse().getAuthorizationEndpoint());
            authorizeClient.setRequest(request);
            authorizeClient.setExecutor(getHttpService().getClientExecutor());
            final AuthorizationResponse response1 = authorizeClient.exec();

            ClientUtils.showClient(authorizeClient);

            final String scope = response1.getScope();
            final String authorizationCode = response1.getCode();

            // todo
            return okResponse(new GetAuthorizationCodeResponse());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return CommandResponse.INTERNAL_ERROR_RESPONSE;
    }

    private List<String> acrValues(GetAuthorizationCodeParams params, SiteConfiguration site) {
        List<String> acrs = Lists.newArrayList();
        if (params.getAcrValues() != null && !params.getAcrValues().isEmpty()) {
            acrs.addAll(params.getAcrValues());
        }
        if (acrs.isEmpty() && site.getAcrValues() != null && !site.getAcrValues().isEmpty()) {
            acrs.addAll(site.getAcrValues());
        }
        return acrs;
    }

    private List<ResponseType> responseTypes(List<String> responseTypes) {
        List<ResponseType> result = Lists.newArrayList();
        for (String type : responseTypes) {
            result.add(ResponseType.fromString(type));
        }
        return result;
    }
}