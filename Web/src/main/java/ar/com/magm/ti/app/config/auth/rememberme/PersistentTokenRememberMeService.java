package ar.com.magm.ti.app.config.auth.rememberme;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import ar.com.magm.ti.app.config.auth.UserDetailService;
import ar.com.magm.ti.exception.NotFoundException;
import ar.com.magm.ti.model.AuthToken;
import ar.com.magm.ti.model.service.IAuthTokenService;
import ar.com.magm.ti.service.exception.ServiceException;

public class PersistentTokenRememberMeService extends AbstractRememberMeServicesCustom {
	private static Logger LOG = LoggerFactory.getLogger(PersistentTokenRememberMeService.class);

	private IAuthTokenService authTokenService;

	public PersistentTokenRememberMeService(String key, UserDetailsService userDetailsService,
			IAuthTokenService authTokenService) {
		super(key, userDetailsService, authTokenService);
		this.authTokenService = authTokenService;

	}

	/**
	 * Re Logins
	 */
	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
			HttpServletResponse response) {
		boolean byToken = cookieTokens.length == 3;

		if (cookieTokens.length < 2 || cookieTokens.length > 3) {
			throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '"
					+ Arrays.asList(cookieTokens) + "'");
		}

		final String presentedSeries = cookieTokens[0];
		final String presentedToken = cookieTokens[1];
		AuthToken authToken;
		try {
			authToken = authTokenService.load(presentedSeries);
		} catch (ServiceException e1) {
			throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
		} catch (NotFoundException e1) {
			throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
		}

		if (!presentedToken.equals(authToken.getToken())) {
		}

		System.out.println(authToken);
		if (!authToken.valid()) {
			try {
				if (authToken.getType().equals(AuthToken.TYPE_DEFAULT)
						|| authToken.getType().equals(AuthToken.TYPE_TO_DATE)
						|| authToken.getType().equals(AuthToken.TYPE_REQUEST_LIMIT)) {
					authTokenService.delete(authToken);
				}
				if (authToken.getType().equals(AuthToken.TYPE_FROM_TO_DATE)) {
					if (authToken.getTo().getTime() < System.currentTimeMillis()) {
						authTokenService.delete(authToken);
					}
				}
			} catch (ServiceException e) {
			} catch (NotFoundException e) {
			}
			throw new RememberMeAuthenticationException("Remember-me login has expired");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Refreshing persistent login token for user '" + authToken.getUsername() + "', series '"
					+ authToken.getSeries() + "'");
		}

		authToken.setLast_used(new Date());
		authToken.addRequest();
		// authToken.setValiditySeconds(getTokenValiditySeconds());

		try {
			authTokenService.update(authToken);
			addCookie(authToken, request, response);
		} catch (Exception e) {
			LOG.error("Failed to update token: ", e);
			throw new RememberMeAuthenticationException("Autologin failed due to data access problem");
		}

		String messageService = UserDetailService.AUTOLOGIN;
		if (byToken)
			messageService = UserDetailService.AUTOLOGIN_BYTOKEN;

		return getUserDetailsService().loadUserByUsername(messageService + authToken.getUsername());
	}

	private void addCookie(AuthToken token, HttpServletRequest request, HttpServletResponse response) {
		String[] tokens = new String[] { token.getSeries(), token.getToken() };
		setCookie(tokens, getTokenValiditySeconds(), request, response);
		String cookieValue = encodeCookie(tokens);
		response.addHeader("x-auth-token", cookieValue);
	}

	/**
	 * Luego del primero login correcto
	 */
	protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		String username = successfulAuthentication.getName();
		LOG.debug("Creating new persistent login for user " + username);
		AuthToken authToken = new AuthToken(getTokenValiditySeconds(), username);
		try {
			authTokenService.save(authToken);
			addCookie(authToken, request, response);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public String[] decodificarCookie(String cookieValue) throws InvalidCookieException {
		return decodeCookie(cookieValue);
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		super.logout(request, response, authentication);
		try {
			String rememberMeCookie = extractRememberMeCookie(request);
			String[] cookieTokens = decodeCookie(rememberMeCookie);
			if (cookieTokens.length > 0) {
				final String presentedSeries = cookieTokens[0];
				AuthToken authToken = new AuthToken();
				authToken.setSeries(presentedSeries);
				authTokenService.delete(authToken);
			}
		} catch (Exception e) {
		}

	}
}
