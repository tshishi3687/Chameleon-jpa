package Tshishi.Chameleon.servicies.Users;
import Tshishi.Chameleon.HumanResources.Business.Dtos.RolesDto;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UpdateOrCreateUsers;
import Tshishi.Chameleon.HumanResources.Business.Dtos.UsersVueDto;
import Tshishi.Chameleon.servicies.AbstractServicesTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        assertEquals(usersVueDto.getRolesDtoList().size(), 4);
        assertNotNull(usersVueDto.getContactDetails());
    }

    private UpdateOrCreateUsers createUsersUpdated() {
        LocalDate date = LocalDate.parse("2023-03-27");
        List<RolesDto> rolesDtoList = new ArrayList<>(dto.getRolesDtoList());
        rolesDtoList.addAll(rolesDtos);
        return new UpdateOrCreateUsers(
                dto.getId(),
                "lilo",
                "popo",
                null,
                date,
                "test@test.be",
                "0032554466",
                "lilo,3687",
                rolesDtoList,
                dto.getContactDetails()
        );
    }
}
