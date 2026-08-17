package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.room.RoomForCardDto;
import dutkercz.hi_backend.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
