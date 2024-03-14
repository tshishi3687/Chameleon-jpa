package Tshishi.Chameleon.servicies.Company;

import Tshishi.Chameleon.Company.Business.Dtos.AddCompanyAndUsers;
import Tshishi.Chameleon.Company.Business.Dtos.CompanyVueDto;
import Tshishi.Chameleon.Company.Business.Services.CompanyService;
import Tshishi.Chameleon.Company.DataAccess.Repository.CompanyRepository;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.ContactDetails;
import Tshishi.Chameleon.HumanResources.DataAccess.Entities.Roles;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void CreateNewCompanyAndUsersTest() throws IOException {
        AddCompanyAndUsers addCompanyAndUsers = transformJsonFromString();

        assert addCompanyAndUsers != null;
        CompanyVueDto companyVueDto = companyService.addEntity(addCompanyAndUsers);
        assert companyVueDto != null;
        assertNotNull(companyVueDto.getId());
        assertEquals(companyVueDto.getName(), addCompanyAndUsers.getCompany().getName());
        assertEquals(companyVueDto.getBusinessNumber(), addCompanyAndUsers.getCompany().getBusinessNumber());

        companyRepository.findById(companyVueDto.getId())
                .ifPresentOrElse(
                        value -> {
                            assertNotNull(value.getContactDetails());
                            assertEquals(value.getContactDetails().getAddress(), addCompanyAndUsers.getCompany().getContactDetails().getAddress());
                            assertEquals(value.getContactDetails().getNumber(), addCompanyAndUsers.getCompany().getContactDetails().getNumber());

                            assert value.getContactDetails().getLocality() != null;
                            assertEquals(value.getContactDetails().getLocality().getName(), addCompanyAndUsers.getCompany().getContactDetails().getLocality().getName().toUpperCase());

                            assert value.getContactDetails().getCountry() != null;
                            assertEquals(value.getContactDetails().getCountry().getName(), addCompanyAndUsers.getCompany().getContactDetails().getCountry().getName().toUpperCase());


                            assert value.getTutors() != null;
                            assertNotNull(value.getTutors().getId());
                            assertEquals(value.getTutors().getFirstName(), addCompanyAndUsers.getUsers().getFirstName());
                            assertEquals(value.getTutors().getLastName(), addCompanyAndUsers.getUsers().getLastName());
                            assertEquals(value.getTutors().getMail(), addCompanyAndUsers.getUsers().getMail());
                            assertEquals(value.getTutors().getPhone(), addCompanyAndUsers.getUsers().getPhone());

                            List<Roles> tutorRoles = value.getTutors().getRolesList();
                            assertNotNull(tutorRoles);

                            List<ContactDetails> contactDetails = value.getTutors().getContactDetails();
                            assertNotNull(contactDetails);
                        },
                        () -> {
                            throw new RuntimeException("Create New CompanyAndUsersTest failed!!");
                        });
    }

    private static AddCompanyAndUsers transformJsonFromString() throws IOException {
        String jsonFilePath = "src/test/resources/json/CreateCompanyAndUsers.json";
        File jsonFile = new File(jsonFilePath);
        if (!jsonFile.exists()) {
            System.out.println("Le fichier JSON n'existe pas.");
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(jsonFile, AddCompanyAndUsers.class);
    }
}
