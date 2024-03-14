package Tshishi.Chameleon.Securities;

import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Users;
import Tshishi.Chameleon.HumanResources.DataAccess.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String logger) throws UsernameNotFoundException {
        Optional<Users> entity = usersRepository.findUsersByMailOrPhone(logger, logger);
        if (entity.isPresent()) {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(entity.get().getPassword()));
            return new User(entity.get().getMail(), entity.get().getPassword(),
                    authorities);
        }
        return null;
    }

}
