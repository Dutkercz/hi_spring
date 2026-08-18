package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.room.RoomForCardDto;
import dutkercz.hi_backend.dto.room.RoomResponseDto;
import dutkercz.hi_backend.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomForCardDto>> getAllRooms(){
        return ResponseEntity.ok().body(roomService.getAllRooms());
    }

    @PutMapping("/add-daily/{id}")
    public ResponseEntity<RoomResponseDto> addDaily(@PathVariable Long id){
        return ResponseEntity.ok(roomService.addDaily(id));
    }
}
