package springapp.controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import springapp.domain.Client;
import springapp.domain.Gender;
import springapp.domain.Pet;
import springapp.service.ClientService;
import springapp.service.PetService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc


public class PetControllerTest {

    static List<Pet> pets = new ArrayList();
    static  {
        pets.add(new Pet(1, "Sparky", Gender.Male, true, 10, 1));
        pets.add(new Pet(2, "Bootsie", Gender.Female, false, 3, 2));

    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetService petService;

    @Test
    @WithMockUser(value = "test", password = "pass", authorities = {"LIST_PETS"})
    public void exampleTest() throws Exception {
        given(petService.getPets()).willReturn(pets);
        MvcResult result = this.mvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(content);
        assertThat(content, doc.select("a[href='/pets/1']").text(), is("1"));
        assertThat(content, doc.select("a[href='/pets/2']").text(), is("2"));
    }

}
