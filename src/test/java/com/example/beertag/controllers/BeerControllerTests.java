package com.example.beertag.controllers;

import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.entities.dtos.BeerDto;
import com.example.beertag.entities.filter.FilterOptions;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.exceptions.UnauthorizedOperationException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.mappers.BeerMapper;
import com.example.beertag.services.BeerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.beertag.Helpers.*;
import static com.example.beertag.controllers.BeerController.BEER_NAME_NOT_FOUND;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest
public class BeerControllerTests {

    @MockitoBean
    BeerService mockService;

    @MockitoBean
    private AuthenticationHelper authenticationHelper;

    @MockitoBean
    private BeerMapper beerMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getById_Should_Return_StatusOK_When_BeerExists() throws Exception {

        Beer mockBeer = createMockBeer();

        Mockito.when(mockService.get(mockBeer.getId()))
                .thenReturn(mockBeer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/beers/{id}", mockBeer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockBeer.getName()));


    }

    @Test
    public void getByName_Should_Return_StatusNotFound_When_BeerDoesNotExist() throws Exception {
        // Arrange
        Beer mockBeer = createMockBeer();
        Mockito.when(mockService.get(mockBeer.getName()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/beers/search")
                        .param("name", mockBeer.getName()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getByName_Should_Return_StatusOK_When_BeerExists() throws Exception {

        Beer mockBeer = createMockBeer();

        Mockito.when(mockService.get(Mockito.eq(mockBeer.getName())))
                .thenReturn(mockBeer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/beers/search").param("name", mockBeer.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockBeer.getName()));


    }

    @Test
    public void getById_Should_Return_StatusNotFound_When_BeerDoesNotExist() throws Exception {
        // Arrange
        Beer mockBeer = createMockBeer();
        Mockito.when(mockService.get(mockBeer.getId()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/beers/{id}", mockBeer.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getByName_Should_Return_StatusNotFound_When_BeerDoesntExist() throws Exception {


        Mockito.when(mockService.get("Mock"))
                .thenThrow(new ResponseStatusException
                        (HttpStatus.NOT_FOUND, String.format(BEER_NAME_NOT_FOUND, "Mock")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/search", "Mock"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void get_Should_Return_AllBeers_When_NoFiltersAreApplied() throws Exception {
        // Arrange
        List<Beer> mockBeers = List.of(
                new Beer(1, "Beer1", "", 1),
                new Beer(2, "Beer2", "", 2)
        );

        Mockito.when(mockService.get(Mockito.any(FilterOptions.class))).thenReturn(mockBeers);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/beers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(mockBeers.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Beer1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Beer2"));
    }

   @Test
   public void createBeer_Should_Return_CreatedBeer_When_ValidBeerDto() throws Exception {

       Mockito.when(beerMapper.fromDto(Mockito.any(BeerDto.class))).thenReturn(createMockBeerFromBeerDto());
       Mockito.when(authenticationHelper.tryGetUser(Mockito.any())).thenReturn(createMockAdminUser());
       Mockito.doNothing().when(mockService).create(Mockito.any(Beer.class), Mockito.any(User.class));

       // Act, Assert
       mockMvc.perform(MockMvcRequestBuilders.post("/api/beers")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("{\"name\": \"TestBeer\",\"description\": \"TestDescription\"," +
                               " \"styleId\": 1, \"abv\": 5.5, \"breweryId\": 1}")
                       .header(HttpHeaders.AUTHORIZATION, "valid"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestBeer"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("TestDescription"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.style.name").value("MockStyle"))  // Corrected path to style name
               .andExpect(MockMvcResultMatchers.jsonPath("$.abv").value(5.5))
               .andExpect(MockMvcResultMatchers.jsonPath("$.brewery.name").value("TestBrewery"));
   }

    @Test
    public void createBeer_ShouldReturn_NotFound_When_StyleId_IsInvalid() throws Exception {
        // Arrange
        Mockito.when(beerMapper.fromDto(Mockito.any(BeerDto.class))).thenThrow(EntityNotFoundException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"TestBeer\",\"description\": \"TestDescription\"," +
                                " \"styleId\": 10, \"abv\": 5.5, \"breweryId\": 1}")
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createBeer_ShouldReturn_CONFLICT_When_DuplicateName() throws Exception {
        // Arrange
        Mockito.doThrow(DuplicateEntityException.class).when(mockService).create(Mockito.any(), Mockito.any());
        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"TestBeer\",\"description\": \"TestDescription\"," +
                                " \"styleId\": 1, \"abv\": 5.5, \"breweryId\": 1}")
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void createBeer_ShouldReturn_Unauthorized_When_UnauthorizedExcThrown() throws Exception {
        // Arrange
        Mockito.when(authenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(UnauthorizedOperationException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"TestBeer\",\"description\": \"TestDescription\"," +
                                " \"styleId\": 1, \"abv\": 5.5, \"breweryId\": 1}")
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    // UPDATE


    @Test
    public void updateBeer_ShouldReturn_UpdatedBeer_When_ValidRequest() throws Exception {
        // Arrange
        Beer mockBeer = createMockBeerFromBeerDto();

        Mockito.when(beerMapper.fromDto(Mockito.anyInt(), Mockito.any(BeerDto.class))).thenReturn(mockBeer);
        Mockito.when(authenticationHelper.tryGetUser(Mockito.any())).thenReturn(createMockAdminUser());
        Mockito.doNothing().when(mockService)
                .update(Mockito.eq(mockBeer), Mockito.eq(createMockAdminUser()));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/beers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"TestBeer\",\"description\": \"TestDescription\"," +
                                " \"styleId\": 1, \"abv\": 5.5, \"breweryId\": 1}")
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestBeer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("TestDescription"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.style.name").value("MockStyle"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.abv").value(5.5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brewery.name").value("TestBrewery"));
    }

    @Test
    public void updateBeer_ShouldReturn_NotFound_When_StyleId_IsInvalid() throws Exception {
        // Arrange
        Mockito.when(beerMapper.fromDto(Mockito.anyInt(), Mockito.any(BeerDto.class)))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/beers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"TestBeer2\",\"description\": \"TestDescription\"," +
                                " \"styleId\": 1, \"abv\": 5.5, \"breweryId\": 1}")
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateBeer_ShouldReturn_CONFLICT_When_DuplicateName() throws Exception {
        // Arrange
        Mockito.doThrow(DuplicateEntityException.class).when(mockService).update(Mockito.any(), Mockito.any());

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/beers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"TestBeer2\",\"description\": \"TestDescription\"," +
                                " \"styleId\": 1, \"abv\": 5.5, \"breweryId\": 1}")
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void updateBeer_ShouldReturn_Unauthorized_When_UnauthorizedExcThrown() throws Exception {
        // Arrange
        Mockito.when(authenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(UnauthorizedOperationException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/beers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"TestBeer2\",\"description\": \"TestDescription\"," +
                                " \"styleId\": 1, \"abv\": 5.5, \"breweryId\": 1}")
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    // DELETE

    @Test
    public void deleteBeer_ShouldReturn_StatusOK_When_ValidRequest() throws Exception {
        // Arrange


        Mockito.when(authenticationHelper.tryGetUser(Mockito.any())).thenReturn(createMockAdminUser());
        Mockito.doNothing().when(mockService)
                .delete(Mockito.eq(createMockBeer().getId()), Mockito.eq(createMockAdminUser()));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/beers/{id}", createMockBeer().getId())
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteBeer_ShouldReturn_NotFound_When_BeerId_IsInvalid() throws Exception {
        // Arrange
        Mockito.when(authenticationHelper.tryGetUser(Mockito.any())).thenReturn(createMockAdminUser());
        Mockito.doThrow(EntityNotFoundException.class).when(mockService)
                .delete(createMockBeer().getId()+1, createMockAdminUser());

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/beers/{id}", 2)
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void deleteBeer_ShouldReturn_Unauthorized_When_UnauthorizedExcThrown() throws Exception {
        // Arrange
        Mockito.when(authenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(UnauthorizedOperationException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/beers/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "valid"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


}
