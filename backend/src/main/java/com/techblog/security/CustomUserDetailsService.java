package com.techblog.security;

// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

/**
 * Custom UserDetails Service
 * Load thông tin user từ MongoDB để Spring Security xác thực.
 */
// @Service
public class CustomUserDetailsService /* implements UserDetailsService */ {

    // TODO: Inject UserRepository
    // private final UserRepository userRepository;

    // @Override
    // public UserDetails loadUserByUsername(String usernameOrEmail)
    // throws UsernameNotFoundException {
    // User user = userRepository.findByUsernameOrEmail(usernameOrEmail,
    // usernameOrEmail)
    // .orElseThrow(() -> new UsernameNotFoundException(
    // "User not found with username or email: " + usernameOrEmail));
    //
    // return new org.springframework.security.core.userdetails.User(
    // user.getEmail(),
    // user.getPassword(),
    // user.getRoles().stream()
    // .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
    // .collect(Collectors.toList())
    // );
    // }

}
