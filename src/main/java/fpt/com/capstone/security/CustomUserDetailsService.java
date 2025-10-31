package fpt.com.capstone.security;

import fpt.com.capstone.exception.CustomException;
import fpt.com.capstone.model.Account;
import fpt.com.capstone.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Account account = accountRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        if (!account.isStatus()) {
            throw new CustomException("Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN);
        }

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(account.getId()),
                account.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()))
        );
    }

    @Transactional
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!account.isStatus()) {
            throw new CustomException("Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN);
        }

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(account.getId()),
                account.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()))
        );
    }
}
