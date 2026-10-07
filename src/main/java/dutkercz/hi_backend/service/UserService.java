package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.UserRequestDto;
import dutkercz.hi_backend.dto.UserResponseDto;
import dutkercz.hi_backend.exceptions.ResourceExistsException;
import dutkercz.hi_backend.exceptions.ResourceNotFoundException;
import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Transactional
    public UserResponseDto registerUser(@Valid UserRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.email())){
            throw new ResourceExistsException("Email já cadastrado");
        }
        var encodedPw = passwordEncoder.encode(requestDto.password());
        var user = new User();
        user.setEmail(requestDto.email());
        user.setPassword(encodedPw);
        userRepository.save(user);
        return new UserResponseDto(user);
    }

    public UserResponseDto getUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Usuário não encontrado"));
        return new UserResponseDto(user);
    }
}
