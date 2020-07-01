package edu.harvard.cscis71.curriculum.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("${openapi.curriculum.base-path:/}")
public class UserApiController implements UserApi {
    private final UserApiDelegate delegate;

    public UserApiController(@org.springframework.beans.factory.annotation.Autowired(required = false) UserApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new UserApiDelegate() {
        });
    }

    @Override
    public UserApiDelegate getDelegate() {
        return delegate;
    }
}
