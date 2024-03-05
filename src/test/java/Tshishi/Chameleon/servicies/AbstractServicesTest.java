package Tshishi.Chameleon.servicies;

import Tshishi.Chameleon.HumanResources.Business.Dtos.UpdateOrCreateUsers;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersVueDto;
import Tshishi.Chameleon.HumanResources.Business.Services.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public abstract class AbstractServicesTest {

    @Autowired
    protected  UsersService usersService;
    protected static UsersVueDto dto;

    @Test
    public void addEntitiesTest() throws IOException {
        UpdateOrCreateUsers updateOrCreateUsers = Objects.requireNonNull(readUsersCreatedJson());
        dto = usersService.addEntity(updateOrCreateUsers);
        assertNotNull(dto.getId());
        assertEquals(dto.getInitialName(), updateOrCreateUsers.getFirstName().toUpperCase() + " " + updateOrCreateUsers.getLastName().toUpperCase());
        assertNull(dto.getBusinessNumber());
        assertEquals(dto.getMail(), mask(updateOrCreateUsers.getMail()));
        assertEquals(dto.getPhone(), mask(updateOrCreateUsers.getPhone()));
        assertFalse(dto.getContactDetails().isEmpty());
        assertTrue(dto.isActive());
    }

    protected static String mask(String string) {
        StringBuilder masking = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            if (i <=1) {
                masking.append(string.charAt(i));
            } else if (string.charAt(i) == '@') {
                masking.append(string.charAt(i));
            } else if (i >= string.length() - 2) {
                masking.append(string.charAt(i));
            } else {
                masking.append('*');
            }
        }
        return masking.toString();
    }

    private static UpdateOrCreateUsers readUsersCreatedJson() throws IOException {
        String jsonFilePath = "src/test/java/Tshishi/Chameleon/jsons/UsersCreated.json";
        File jsonFile = new File(jsonFilePath);
        if (!jsonFile.exists()) {
            System.out.println("Le fichier JSON n'existe pas.");
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(jsonFile, UpdateOrCreateUsers.class);
    }
}
