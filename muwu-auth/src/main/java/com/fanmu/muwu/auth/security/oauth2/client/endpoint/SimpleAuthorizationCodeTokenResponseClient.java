/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.auth.security.oauth2.client.endpoint;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

/**
 * The default implementation of an {@link OAuth2AccessTokenResponseClient}
 * for the {@link AuthorizationGrantType#AUTHORIZATION_CODE authorization_code} grant.
 * This implementation uses a {@link RestOperations} when requesting
 * an access token credential at the Authorization Server's Token Endpoint.
 *
 * @author Joe Grandja
 * @since 5.1
 * @see OAuth2AccessTokenResponseClient
 * @see OAuth2AuthorizationCodeGrantRequest
 * @see OAuth2AccessTokenResponse
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc6749#section-4.1.3">Section 4.1.3 Access Token Request (Authorization Code Grant)</a>
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc6749#section-4.1.4">Section 4.1.4 Access Token Response (Authorization Code Grant)</a>
 */
public final class SimpleAuthorizationCodeTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
	private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";

	private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter =
			new OAuth2AuthorizationCodeGrantRequestEntityConverter();

	private Converter<OAuth2AccessTokenoOriginalResponse, OAuth2AccessTokenResponse> responseOriginaConverter =
			new DelegatingOAuth2AccessTokenoResponseConverter(null);

	private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
			new ParameterizedTypeReference<Map<String, Object>>() {};

	private RestOperations restOperations;

	public SimpleAuthorizationCodeTokenResponseClient() {
		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
				new FormHttpMessageConverter(), new FastJsonHttpMessageConverter()));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		this.restOperations = restTemplate;
	}

	@Override
	public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		Assert.notNull(authorizationCodeGrantRequest, "authorizationCodeGrantRequest cannot be null");

		RequestEntity<?> request = this.requestEntityConverter.convert(authorizationCodeGrantRequest);

		OAuth2AccessTokenoOriginalResponse tokenoOriginalResponse;
		try {
			ResponseEntity<Map<String, Object>> response = this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
			tokenoOriginalResponse = new OAuth2AccessTokenoOriginalResponse();
			tokenoOriginalResponse.setResponse(response.getBody());
			tokenoOriginalResponse.setOAuth2AuthorizationCodeGrantRequest(authorizationCodeGrantRequest);
		} catch (RestClientException ex) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_TOKEN_RESPONSE_ERROR_CODE,
					"An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: " + ex.getMessage(), null);
			throw new OAuth2AuthorizationException(oauth2Error, ex);
		}

		OAuth2AccessTokenResponse tokenResponse = responseOriginaConverter.convert(tokenoOriginalResponse);

		if (CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
			// As per spec, in Section 5.1 Successful Access Token Response
			// https://tools.ietf.org/html/rfc6749#section-5.1
			// If AccessTokenResponse.scope is empty, then default to the scope
			// originally requested by the client in the Token Request
			tokenResponse = OAuth2AccessTokenResponse.withResponse(tokenResponse)
					.scopes(authorizationCodeGrantRequest.getClientRegistration().getScopes())
					.build();
		}

		return tokenResponse;
	}

	/**
	 * Sets the {@link Converter} used for converting the {@link OAuth2AuthorizationCodeGrantRequest}
	 * to a {@link RequestEntity} representation of the OAuth 2.0 Access Token Request.
	 *
	 * @param requestEntityConverter the {@link Converter} used for converting to a {@link RequestEntity} representation of the Access Token Request
	 */
	public void setRequestEntityConverter(Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter) {
		Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
		this.requestEntityConverter = requestEntityConverter;
	}

	/**
	 * Sets the {@link Converter} used for converting the {@link OAuth2AccessTokenoOriginalResponse}
	 * to a {@link RequestEntity} representation of the OAuth 2.0 Access Token Request.
	 *
	 * @param responseOriginaConverter the {@link Converter} used for converting to a {@link RequestEntity} representation of the Access Token Request
	 */
	public void setResponseOriginaConverter(Converter<OAuth2AccessTokenoOriginalResponse, OAuth2AccessTokenResponse> responseOriginaConverter) {
		this.responseOriginaConverter = responseOriginaConverter;
	}

	/**
	 * Sets the {@link RestOperations} used when requesting the OAuth 2.0 Access Token Response.
	 *
	 * <p>
	 * <b>NOTE:</b> At a minimum, the supplied {@code restOperations} must be configured with the following:
	 * <ol>
	 *  <li>{@link HttpMessageConverter}'s - {@link FormHttpMessageConverter} and {@link OAuth2AccessTokenResponseHttpMessageConverter}</li>
	 *  <li>{@link ResponseErrorHandler} - {@link OAuth2ErrorResponseErrorHandler}</li>
	 * </ol>
	 *
	 * @param restOperations the {@link RestOperations} used when requesting the Access Token Response
	 */
	public void setRestOperations(RestOperations restOperations) {
		Assert.notNull(restOperations, "restOperations cannot be null");
		this.restOperations = restOperations;
	}
}
