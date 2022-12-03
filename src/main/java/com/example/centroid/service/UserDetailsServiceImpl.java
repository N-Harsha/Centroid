package com.example.centroid.service;

import com.example.centroid.model.User;
import com.example.centroid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Transactional
//    @Cacheable(value = "userDetilsImpl",key = "#username")
    public UserDetailsImpl getUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user Not Found with username : "+username));
        return UserDetailsImpl.build(user);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user Not Found with username : "+username));
        return UserDetailsImpl.build(user);
    }
}
