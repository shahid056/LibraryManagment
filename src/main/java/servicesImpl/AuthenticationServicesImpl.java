package servicesImpl;

import entity.User;
import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.dao.AuthenticationDao;
import repository.daoimpl.AuthenticationDoaImpl;
import server.AuthenticationServices;
import utils.Response;

import java.util.Objects;

public class AuthenticationServicesImpl implements AuthenticationServices {

    private Logger log = LogManager.getLogger();

//    private static  AuthenticationServicesImpl authenticationServices;
    private final AuthenticationDao authenticationDao = AuthenticationDoaImpl.getInstance();

    private AuthenticationServicesImpl(){};

    public static AuthenticationServicesImpl getAuthenticationInstance(){
        if(Objects.isNull(authenticationServices)){
            authenticationServices = new AuthenticationServicesImpl();
        }
        return authenticationServices;
    }

    @Override
    public Response userRegistration(User user) {
        Response response = new Response();
        try {
            if(authenticationDao.userRegistration(user)){
               response = new Response(user,ResponseStatus.SUCCESS,"Registration successful..");
            } else  {
                response.setMessage("User Already Exists !!!");
            }
        } catch (Exception e) {
            log.error("Registration error",e);
            response.setMessage("Something ");
        }
        return response;
    }

    @Override
    public Response userLogin(String email,String password) {
        Response response = new Response(null,ResponseStatus.Error,"Wrong Credentials..");
        try {
            User user = authenticationDao.userLogin(email, password);
            if(Objects.nonNull(user)) {
                response = new Response(user,ResponseStatus.SUCCESS,"Welcome "+user.getName());
            }
        } catch (Exception e) {
            response = Response.builder().message("Somth").build();
           log.info("Login error",e);
        }
        return response;
    }
}
