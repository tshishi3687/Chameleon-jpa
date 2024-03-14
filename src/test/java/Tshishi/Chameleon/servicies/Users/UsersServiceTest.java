package Tshishi.Chameleon.servicies.Users;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UpdateOrCreateUsers;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersVueDto;
import Tshishi.Chameleon.servicies.AbstractServicesTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsersServiceTest extends AbstractServicesTest {

    @Test
    public void updateUsersTest() {
        UpdateOrCreateUsers usersUpdated = createUsersUpdated();
        UsersVueDto usersVueDto = usersService.updateEntity(usersUpdated, usersUpdated.getId());

        assertNotNull(usersVueDto.getId());
        assertEquals(usersVueDto.getInitialName(), usersUpdated.getFirstName().toUpperCase() + " " + usersUpdated.getLastName().toUpperCase());
        assertEquals(usersVueDto.getMail(), mask(usersUpdated.getMail()));
        assertEquals(usersVueDto.getPhone(), mask(usersUpdated.getPhone()));
        assertNotNull(usersVueDto.getRolesDtoList());
        assertNotNull(usersVueDto.getContactDetails());
    }

    private UpdateOrCreateUsers createUsersUpdated() {
        LocalDate date = LocalDate.parse("2023-03-27");
        return new UpdateOrCreateUsers(
                dto.getId(),
                "lilo",
                "popo",
                date,
                "test@test.be",
                "0032554466",
                "lilo,3687",
                dto.getContactDetails()
        );
    }
}
