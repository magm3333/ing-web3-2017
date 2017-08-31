package ar.com.magm.ti.model;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Proxy;
import org.springframework.security.crypto.codec.Base64;

/**
 * 
 * @author magm
 *
 */
@Entity
@Proxy(lazy = false)
@Access(value = AccessType.FIELD)
@Table(name = "persistent_logins")
public class AuthToken implements Serializable {
	private static final long serialVersionUID = -2431961978969608445L;
	public static String TYPE_DEFAULT = "DEFAULT";
	public static String TYPE_FROM_TO_DATE = "FROM_TO_DATE";
	public static String TYPE_REQUEST_LIMIT = "REQUEST_LIMIT";
	public static String TYPE_TO_DATE = "TO_DATE";

	private Date from;

	private Date last_used;

	@Transient
	private SecureRandom random = new SecureRandom();

	private int requestCount;

	private int requestLimit;

	@Id
	private String series;

	private Date to;

	private String token;

	private String type;

	private String username;

	private int validitySeconds;

	public AuthToken() {
	}

	/**
	 * Constructor para Token DEFAULT
	 * 
	 * @param username
	 *            Usuario que loguea el token
	 * @param validitySeconds
	 *            Cantidad de segundos que el token es válido entre requests
	 */
	public AuthToken(int validitySeconds, String username) {
		setUsername(username);
		setValiditySeconds(validitySeconds);
		setLast_used(new Date());
		setSeries(generateSeriesData());
		setToken(generateTokenData());
		type = TYPE_DEFAULT;
	}

	/**
	 * Constructor para Token TO_DATE
	 * 
	 * @param username
	 *            Usuario que loguea el token
	 * @param to
	 *            Estampa de tiempo que es el limite de uso del token
	 */
	public AuthToken(String username, Date to) {
		setUsername(username);
		setTo(to);
		setSeries(generateSeriesData());
		setToken(generateTokenData());
		type = TYPE_TO_DATE;
	}

	/**
	 * Constructor para Token FROM_TO_DATE
	 * 
	 * @param username
	 *            Usuario que loguea el token
	 * @param from
	 *            Estampa de tiempo a partir de la cual se puede usar el token
	 * @param to
	 *            Estampa de tiempo que es el limite de uso del token
	 */
	public AuthToken(String username, Date from, Date to) {
		setUsername(username);
		setFrom(from);
		setTo(to);
		setSeries(generateSeriesData());
		setToken(generateTokenData());
		type = TYPE_FROM_TO_DATE;
	}

	/**
	 * Constructor para Token REQUEST_LIMIT
	 * 
	 * @param username
	 *            Usuario que loguea el token
	 * @param requestLimit
	 *            Cantidad de veces que se podrá usar el token (cantidad neta de
	 *            requests)
	 */
	public AuthToken(String username, int requestLimit) {
		setUsername(username);
		setRequestLimit(requestLimit);
		setSeries(generateSeriesData());
		setToken(generateTokenData());
		type = TYPE_REQUEST_LIMIT;
	}

	public void addRequest() {
		requestCount++;
	}

	public String encodeCookieValue() {
		String[] cookieTokens = new String[] { getSeries(), getToken() };
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cookieTokens.length; i++) {
			sb.append(cookieTokens[i]);

			if (i < cookieTokens.length - 1) {
				sb.append(":");
			}
		}

		String value = sb.toString();

		sb = new StringBuilder(new String(Base64.encode(value.getBytes())));

		while (sb.charAt(sb.length() - 1) == '=') {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	private String generateSeriesData() {
		byte[] newSeries = new byte[16];
		random.nextBytes(newSeries);
		return new String(Base64.encode(newSeries));
	}

	private String generateTokenData() {
		byte[] newToken = new byte[16];
		random.nextBytes(newToken);
		return new String(Base64.encode(newToken));
	}

	public Date getFrom() {
		return from;
	}

	public Date getLast_used() {
		return last_used;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public int getRequestLimit() {
		return requestLimit;
	}

	public String getSeries() {
		return series;
	}

	public Date getTo() {
		return to;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

	public String getUsername() {
		return username;
	}

	public int getValiditySeconds() {
		return validitySeconds;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public void setLast_used(Date last_used) {
		this.last_used = last_used;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public void setRequestLimit(int requestLimit) {
		this.requestLimit = requestLimit;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setValiditySeconds(int validitySeconds) {
		this.validitySeconds = validitySeconds;
	}

	@Override
	public String toString() {
		return String.format(
				"Token: serie=%s, user=%s, type=%s, tokenValue=%s, lastUsed=%s, validSecs=%s, requestCount=%s, requestLimit=%s, valid=%s",
				getSeries(), getUsername(), getType(), encodeCookieValue(), getLast_used(), getValiditySeconds(),
				getRequestCount(), getRequestLimit(), valid());
	}

	public boolean valid() {
		if (getType().equals(TYPE_DEFAULT))
			return validDefault();
		if (getType().equals(TYPE_TO_DATE))
			return validToDate();
		if (getType().equals(TYPE_FROM_TO_DATE))
			return validFromToDate();
		if (getType().equals(TYPE_REQUEST_LIMIT))
			return validRequestLimit();

		return false;
	}

	private boolean validDefault() {
		return getLast_used().getTime() + getValiditySeconds() * 1000L > System.currentTimeMillis();
	}

	private boolean validFromToDate() {
		return System.currentTimeMillis() >= getFrom().getTime() && System.currentTimeMillis() <= getTo().getTime();
	}

	private boolean validRequestLimit() {
		return getRequestCount() <= getRequestLimit();
	}

	private boolean validToDate() {
		return System.currentTimeMillis() <= getTo().getTime();
	}
}
