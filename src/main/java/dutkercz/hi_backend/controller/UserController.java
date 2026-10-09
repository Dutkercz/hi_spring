package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.UserRequestDto;
import dutkercz.hi_backend.dto.UserResponseDto;
import dutkercz.hi_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto requestDto,
                                                        UriComponentsBuilder builder) {
        var responseDto = userService.registerUser(requestDto);
        URI uri = builder.path("/api/v1/users/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @PostMapping("/updaload-img")
    public ResponseEntity<Void> uploadUserImage(@AuthenticationPrincipal String username ,
                                                @RequestParam MultipartFile file){
        userService.addUserPhoto(username, file);
        return ResponseEntity.ok().build();
    }


    @GetMapping()
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @GetMapping("/download-img")
    public ResponseEntity<?> getUserImg(@AuthenticationPrincipal String username) throws IOException {
        var path = userService.loadUserImg(username);

        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
           String contentType = Files.probeContentType(path) != null ?
                                    Files.probeContentType(path) : "application/octet-stream";

        return ResponseEntity.ok().
                contentType(MediaType.parseMediaType(
                        contentType ))
                .body(resource);
    }
}
