package com.railwaymodelingsystem.config.security;

import com.railwaymodelingsystem.model.UserEntity;
import com.railwaymodelingsystem.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails  loadUserByUsername(String userName) throws UsernameNotFoundException{
        UserEntity user = this.userRepository.findByName(userName);

        if (user == null){
            throw new UsernameNotFoundException("User" + userName + "was not found in database");
        }
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

            return new User(user.getUsername(),
                    user.getPassword(), grantedAuthorityList);
    }
}
