package dutkercz.hi_backend.mapper;

import dutkercz.hi_backend.dto.client.ClientDetailDto;
import dutkercz.hi_backend.dto.client.ClientRequestDto;
import dutkercz.hi_backend.dto.client.ClientResponseDto;
import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.model.Stay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponseDto toClientResponseDto(Client client);

    Client toClient(ClientRequestDto clientRequestDto);

    @Mapping(source = "stays", target = "stays")
    ClientDetailDto toClientDetail(Client client, List<Stay> stays);
}
