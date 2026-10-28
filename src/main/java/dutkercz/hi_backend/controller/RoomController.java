package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.room.RoomForCardDto;
import dutkercz.hi_backend.dto.room.RoomResponseDto;
import dutkercz.hi_backend.dto.room.RoomUpdateDto;
import dutkercz.hi_backend.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    //@RateLimiter(name = "backendApi", fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<List<RoomForCardDto>> getAllRooms(){
        return ResponseEntity.ok().body(roomService.getAllRooms());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponseDto> addRoom(){
        return ResponseEntity.ok().body(roomService.addRoom());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponseDto> deleteRoom(@PathVariable Long id){
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponseDto> updateRoomConfig(@PathVariable Long id, @RequestBody RoomUpdateDto updateDto){
        return ResponseEntity.ok(roomService.updateRoomConfig(id, updateDto));
    }

    public ResponseEntity<?> rateLimiterFallback(Throwable t){
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(t.getMessage());
    }
}
