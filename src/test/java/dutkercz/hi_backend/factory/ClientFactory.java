package dutkercz.hi_backend.factory;

import dutkercz.hi_backend.dto.client.ClientRequestDto;
import dutkercz.hi_backend.model.Client;

import java.util.ArrayList;

public class ClientFactory {

    private final static  String MOCK_CPF = "12345678900";
    private final static  String CLIENT_NAME = "Cristian";
    private final static  String CLIENT_LASTNAME = "Rosa";
    private final static  String PHONE_NUMBER = "55999663322";


    public static Client createClientWithoutId(){
        return new Client(CLIENT_NAME, CLIENT_LASTNAME, MOCK_CPF, null,
                    PHONE_NUMBER, new ArrayList<>());
    }

    public static ClientRequestDto createClientRequest() {
        return new ClientRequestDto(CLIENT_NAME, CLIENT_LASTNAME, MOCK_CPF, null,
                    PHONE_NUMBER, new ArrayList<>());
    }
}
