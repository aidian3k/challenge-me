package ee.pw.challengeme.application;

import ee.pw.challengeme.domain.user.data.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserFacade userFacade;
}
