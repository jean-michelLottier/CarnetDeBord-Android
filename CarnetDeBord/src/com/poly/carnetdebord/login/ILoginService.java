package com.poly.carnetdebord.login;

import com.poly.carnetdebord.localstorage.SessionManager;
import com.poly.carnetdebord.webservice.Response;

public interface ILoginService {
	public void initSession(Response response);

	public void finishRegister(Response response);

}
