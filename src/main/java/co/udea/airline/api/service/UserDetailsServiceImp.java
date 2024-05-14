package co.udea.airline.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.udea.airline.api.model.jpa.repository.PersonRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final PersonRepository repository;

    public UserDetailsServiceImp(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
