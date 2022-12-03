package nl.tudelft.sem.group06b.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public String authenticate(String username, String password) {
        //TODO: implement the method
        return null;
    }

    @Override
    public void register(String username, String password) {
        //TODO: implement the method
    }
}
