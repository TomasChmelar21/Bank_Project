package cz.tul.Chmelar.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Model with Override methods from UserDetails class
 */
public class CustomUser implements UserDetails {


    private User user;

    /**
     * Initialize user to this class
     *
     * @param user - user which we are working with
     */
    public CustomUser(User user) {
        this.user = user;
    }

    /**
     * Method to override getAuthorities to return null
     *
     * @return always null
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return null;
    }

    /**
     * override getPassword() method to return users Password
     *
     * @return users password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * override getUsername() method to return users Username (email)
     *
     * @return users email
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * override isAccountNonExpired() method to return true
     *
     * @return always true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * override isAccountNonLocked() method to return true
     *
     * @return always true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * override isCredentialsNonExpired() method to return true
     *
     * @return always true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * override isEnabled() method to return true
     *
     * @return always true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
