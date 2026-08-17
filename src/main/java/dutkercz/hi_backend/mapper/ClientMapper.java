package dutkercz.hi_backend.mapper;

import dutkercz.hi_backend.dto.client.ClientRequestDto;
import dutkercz.hi_backend.dto.client.ClientResponseDto;
import dutkercz.hi_backend.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponseDto toClientResponseDto(Client client);

    Client toClient(ClientRequestDto clientRequestDto);

}
