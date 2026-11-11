package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.UserRequestDto;
import dutkercz.hi_backend.dto.UserResponseDto;
import dutkercz.hi_backend.exceptions.ResourceExistsException;
import dutkercz.hi_backend.exceptions.ResourceNotFoundException;
import dutkercz.hi_backend.model.Otp;
import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.model.enums.EmailType;
import dutkercz.hi_backend.repository.OtpRepository;
import dutkercz.hi_backend.repository.UserRepository;
import dutkercz.hi_backend.service.utils.Hasher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String IMG_DIR = "imagens_app/";
    private final Path dirPath = Paths.get(IMG_DIR).toAbsolutePath().normalize();;
    private final MailSender mailSender;
    private final OtpRepository otpRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() ->
                new ResourceNotFoundException("Email or password invalid"));
    }

    public void sendRecoveryPwMail(String username) throws NoSuchAlgorithmException {
        var user = (User) loadUserByUsername(username);


        var otpCode = generateOTP();
        log.info("Otp created {}", otpCode);
        var otp = new Otp(user, Hasher.generateHash(otpCode));
        otpRepository.save(otp);

        mailSender.sendMail(user, otpCode, EmailType.RECOVERY_PW);
    }

    private String generateOTP() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
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
        user.setFullName(requestDto.fullName());

        userRepository.save(user);
        return new UserResponseDto(user);
    }

    public UserResponseDto getUser(String username) {
        var user =(User) loadUserByUsername(username);
        return new UserResponseDto(user);
    }

    @Transactional
    public void addUserPhoto(String username, MultipartFile image) {
        var user = (User) loadUserByUsername(username);
        try {
            if (Files.notExists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            if(user.getFileName() != null) {
               deleteOldUserImg(user);
            }

            String fileName = image.getOriginalFilename() + Instant.now().getEpochSecond();
            Path targetLocation = dirPath.resolve(fileName);

            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            user.setFileName(fileName);
            userRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Falha ao tentar adicionar imagem: " + e.getMessage());
        }
    }

    private void deleteOldUserImg(User user) {
        var fileName = user.getFileName();
        var path = dirPath.resolve(fileName).toAbsolutePath().normalize();
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao deletar imagem: " + e.getMessage());
        }
    }

    public Path loadUserImg(String username){
        var user = (User) loadUserByUsername(username);
        if(user.getFileName() != null) {
            return dirPath.resolve(user.getFileName()).normalize();
        }
        return null;
    }

}
