package co.edu.unbosque.poligamia.dto;

import java.util.Objects;

public class AuthResponse {


	private String token;

	public AuthResponse(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "TokenResponse [token=" + token + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(token);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		AuthResponse other = (AuthResponse) obj;
		return Objects.equals(token, other.token);
	}







}
