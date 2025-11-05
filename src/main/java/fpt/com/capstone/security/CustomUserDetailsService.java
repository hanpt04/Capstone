//package fpt.com.capstone.security;
//
//import fpt.com.capstone.exception.CustomException;
//import fpt.com.capstone.model.Lecturer;
//import fpt.com.capstone.repository.LecturerRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final LecturerRepository lecturerRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Lecturer lecturer = lecturerRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        if (!lecturer.isStatus()) {
//            throw new RuntimeException("Account is disabled");
//        }
//
//        return User.builder()
//                .username(String.valueOf(lecturer.getId()))
//                .password(lecturer.getPassword())
//                .authorities(Collections.singletonList(
//                        new SimpleGrantedAuthority("ROLE_" + lecturer.getRole().name())
//                ))
//                .build();
//    }
//
//    public UserDetails loadUserById(Integer userId) {
//        Lecturer lecturer = lecturerRepository.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("Lecturer not found: " + userId));
//
//        if (!lecturer.isStatus()) {
//            throw new RuntimeException("Account is disabled");
//        }
//
//        return User.builder()
//                .username(String.valueOf(lecturer.getId()))
//                .password(lecturer.getPassword())
//                .authorities(Collections.singletonList(
//                        new SimpleGrantedAuthority("ROLE_" + lecturer.getRole().name())
//                ))
//                .build();
//    }
//}
