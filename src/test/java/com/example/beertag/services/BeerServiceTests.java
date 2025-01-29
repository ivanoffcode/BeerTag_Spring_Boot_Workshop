package com.example.beertag.services;


import ch.qos.logback.core.testUtil.MockInitialContext;
import com.example.beertag.entities.Beer;
import com.example.beertag.entities.User;
import com.example.beertag.entities.filter.FilterOptions;
import com.example.beertag.exceptions.DuplicateEntityException;
import com.example.beertag.exceptions.EntityNotFoundException;
import com.example.beertag.exceptions.UnauthorizedOperationException;
import com.example.beertag.repositories.BeerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.beertag.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTests {

    @Mock
    BeerRepository mockRepository;

    @InjectMocks
    BeerServiceImpl service;

    @Test
    public void getById_Should_ReturnBeer_When_MatchExists() {
        //Arrange
        Mockito.when(mockRepository.get(2))
                .thenReturn(new Beer(2, "MockBeerName", "something", 1.3));

        //Act
        Beer result = service.get(2);

        //Assert
        Assertions.assertEquals(2, result.getId());
        Assertions.assertEquals("MockBeerName", result.getName());
        Assertions.assertEquals("something", result.getDescription());
        Assertions.assertEquals(1.3, result.getAbv());

    }

    @Test
    public void getByName_Should_ReturnBeer_When_MatchExists() {
        //Arrange
        Mockito.when(mockRepository.get(createMockBeer().getName()))
                .thenReturn(createMockBeer());

        //Act
        Beer result = service.get(createMockBeer().getName());

        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("TestBeer", result.getName());
        Assertions.assertEquals("TestDescription", result.getDescription());
        Assertions.assertEquals(5.5, result.getAbv());

    }

    @Test
    public void create_ShouldCallRepo_When_BeerNameAlreadyExists() {
        // Arrange
        var mockBeer = createMockBeer();
        Mockito.when(mockRepository.get(mockBeer.getName()))
                .thenThrow(new EntityNotFoundException("Beer", "name", mockBeer.getName()));

        // Act
        service.create(mockBeer, createMockAdminUser());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Beer.class));
    }

    @Test
    public void getAll_Should_CallRepository() {
        FilterOptions mockFilterOptions = createMockFilterOptions();
        Mockito.when(mockRepository.get(mockFilterOptions)).thenReturn(null);

       service.get(mockFilterOptions);

       Mockito.verify(mockRepository, Mockito.times(1))
               .get(mockFilterOptions);

    }

    @Test
    public void create_Should_Throw_When_BeerWithSameNameExists() {

        Beer mockBeer = createMockBeer();
        User mockUser = createMockAdminUser();

        Mockito.when(mockRepository.get(mockBeer.getName())).thenReturn(mockBeer);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(mockBeer, mockUser));

    }

    @Test
    public void create_Should_CallRepository_When_BeerWithSameNameDoesntExist() {

        Beer mockBeer = createMockBeer();
        User mockUser = createMockNormalUser();

        Mockito.when(mockRepository.get(mockBeer.getName()))
                .thenThrow(new EntityNotFoundException("Beer", "name", mockBeer.getName()));

        service.create(mockBeer, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Beer.class));

    }


    @Test
    public void update_Should_CallRepository_When_BeerWithSameNameDoesntExist() {

        Beer mockBeer = createMockBeer();
        User mockUser = createMockNormalUser();
        mockBeer.setCreatedBy(mockUser);


        Mockito.when(mockRepository.get(mockBeer.getId())).thenReturn(mockBeer);

        Mockito.when(mockRepository.get(mockBeer.getName()))
                .thenReturn(mockBeer);

        service.update(mockBeer, mockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.eq(mockBeer));

    }


    @Test
    public void update_Should_Throw_When_UserIsNotCreatorOrAdmin() {
        Beer mockBeer = createMockBeer();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> service.update(mockBeer, Mockito.mock(User.class)));

    }


    @Test
    public void update_Should_Throw_When_BeerNameIsTaken() {
        Beer mockBeer = createMockBeer();
        Beer anotherMockBeer = createMockBeer();
        anotherMockBeer.setId(2);

        Mockito.when(mockRepository.get(Mockito.anyInt())).thenReturn(mockBeer);

        Mockito.when(mockRepository.get(Mockito.anyString())).thenReturn(anotherMockBeer);

        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(mockBeer, createMockAdminUser()));


    }


    //ToDo
    @Test
    public void update_ShouldNotThrowExc_When_IdMatches() {
        // Arrange
        Beer mockBeer = createMockBeer();
        Beer mockBeer2 = createMockBeer();
        mockBeer2.setName("Random");

        User mockUser2 = createMockAdminUser();
        Mockito.when(mockRepository.get(mockBeer2.getName())).thenReturn(mockBeer);
        Mockito.when(mockRepository.get(mockBeer2.getId())).thenReturn(mockBeer);

        // Assert
        Assertions.assertDoesNotThrow(() -> service.update(mockBeer2, mockUser2));
    }

    @Test
    public void update_Should_Throw_When_UserIsNotCreatorOrAdmin2() {
        // Arrange
        Beer mockBeer = createMockBeer();
        User mockUser = new User(5, "pesho", "123", "123@abv", false);

        Mockito.when(mockRepository.get(mockBeer.getId())).thenReturn(mockBeer);

        // Act, Assert
        Assertions.assertThrows(
                UnauthorizedOperationException.class, () -> service.update(mockBeer, mockUser));
    }

    @Test
    public void update_Should_Throw_When_BeerNameIsTaken2() {
        // Arrange
        var mockBeer = createMockBeer();
        var mockBeer2 = createMockBeer();
        mockBeer2.setId(2);

        var mockUser = createMockAdminUser();

        Mockito.when(mockRepository.get(mockBeer.getId())).thenReturn(mockBeer);
        Mockito.when(mockRepository.get(mockBeer2.getName())).thenReturn(mockBeer2);

        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.update(mockBeer, mockUser));
    }


    @Test
    public void delete_Should_Throw_When_UserIsNotCreatorOrAdmin() {
        Beer mockBeer = createMockBeer();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                ()-> service.delete(mockBeer.getId(), Mockito.mock(User.class)));

    }

    @Test
    public void delete_Should_delete_When_UserIsCreator() {
        Beer mockBeer = createMockBeer();
        User mockUser = createMockNormalUser();
        mockBeer.setCreatedBy(mockUser);

        Mockito.when(mockRepository.get(mockBeer.getId()))
                .thenReturn(mockBeer);

        service.delete(mockBeer.getId(), mockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(Mockito.eq(mockBeer.getId()));

    }



}
