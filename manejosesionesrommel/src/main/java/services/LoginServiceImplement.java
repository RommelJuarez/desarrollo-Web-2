package services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;

public class LoginServiceImplement implements LoginService{
    @Override
    public Optional<String> getUsername(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies() != null ? request.getCookies() : new Cookie[0];

        return Arrays.stream(cookies)
                .filter(c -> "username".equals(c.getName()))
                //Convertimos de cookie a string
                .map(Cookie::getValue)
                .findAny();
    }
}
