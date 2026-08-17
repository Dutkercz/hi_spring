package dutkercz.hi_backend.model;

import dutkercz.hi_backend.model.enums.RoomStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tb_rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String roomNumber;
    private Integer singleBeds;
    private Integer doubleBeds;

    @Enumerated(EnumType.STRING)
    private RoomStatusEnum status;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    private List<Stay> stays = new ArrayList<>();

}