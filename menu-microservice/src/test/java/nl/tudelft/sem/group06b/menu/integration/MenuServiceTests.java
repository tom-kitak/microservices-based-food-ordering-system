package nl.tudelft.sem.group06b.menu.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.group06b.menu.authentication.AuthManager;
import nl.tudelft.sem.group06b.menu.domain.Allergy;
import nl.tudelft.sem.group06b.menu.domain.AllergyRepository;
import nl.tudelft.sem.group06b.menu.domain.Pizza;
import nl.tudelft.sem.group06b.menu.domain.PizzaRepository;
import nl.tudelft.sem.group06b.menu.domain.Topping;
import nl.tudelft.sem.group06b.menu.domain.ToppingRepository;
import nl.tudelft.sem.group06b.menu.service.MenuAllergyService;
import nl.tudelft.sem.group06b.menu.service.MenuPizzaService;
import nl.tudelft.sem.group06b.menu.service.MenuToppingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuServiceTests {

    @Mock
    ToppingRepository toppingRepository;

    @Mock
    AllergyRepository allergyRepository;

    @Mock
    PizzaRepository pizzaRepository;

    @Mock
    AuthManager authManager;

    @InjectMocks
    MenuAllergyService menuAllergyService;

    @InjectMocks
    MenuToppingService menuToppingService;

    @InjectMocks
    MenuPizzaService menuPizzaService;

    Pizza p1;

    Pizza p2;

    Pizza p3;

    Topping t1;

    Topping t2;

    Topping t3;

    Allergy aaa;

    Allergy bbb;

    Allergy ccc;

    /**
     * sets up the tests.
     */
    @BeforeEach
    public void setUp() throws Exception {
        this.toppingRepository = Mockito.mock(ToppingRepository.class);
        this.pizzaRepository = Mockito.mock(PizzaRepository.class);
        this.allergyRepository = Mockito.mock(AllergyRepository.class);
        this.authManager = Mockito.mock(AuthManager.class);
        this.aaa = new Allergy(1L, "Peanuts");
        this.bbb = new Allergy(2L, "Hawaii");
        this.ccc = new Allergy(3L, "Food");
        this.t1 = new Topping(10L, "Potato", List.of(aaa, ccc), new BigDecimal("10.99"));
        this.t2 = new Topping(11L, "Fries", List.of(aaa), new BigDecimal("10.99"));
        this.t3 = new Topping(12L, "Butter", new ArrayList<>(), new BigDecimal("3.2"));
        when(this.toppingRepository.findToppingById(10L)).thenReturn(Optional.of(t1));
        when(this.toppingRepository.findToppingById(11L)).thenReturn(Optional.of(t2));
        when(this.toppingRepository.findToppingById(12L)).thenReturn(Optional.of(t3));
        when(this.allergyRepository.findAllergyByNameIsIgnoreCase("Peanuts")).thenReturn(Optional.of(aaa));
        when(this.allergyRepository.findAllergyByNameIsIgnoreCase("Hawaii")).thenReturn(Optional.of(bbb));
        when(this.allergyRepository.findAllergyByNameIsIgnoreCase("Food")).thenReturn(Optional.of(ccc));
        when(this.allergyRepository.findAllergyById(1L)).thenReturn(Optional.of(aaa));
        when(this.allergyRepository.findAllergyById(2L)).thenReturn(Optional.of(bbb));
        when(this.allergyRepository.findAllergyById(3L)).thenReturn(Optional.of(ccc));
        this.p1 = new Pizza(40L, List.of(t1, t3), "Pepperoni", new BigDecimal("10.99"));
        this.p2 = new Pizza(41L, List.of(t3), "Bepperoni", new BigDecimal("8.99"));
        this.p3 = new Pizza(42L, List.of(t1, t2, t3), "Cepperoni", new BigDecimal("78.99"));
        when(this.pizzaRepository.findPizzaById(40L)).thenReturn(Optional.of(p1));
        when(this.pizzaRepository.findPizzaById(41L)).thenReturn(Optional.of(p2));
        when(this.pizzaRepository.findAll()).thenReturn(List.of(p1, p2, p3));
        when(this.toppingRepository.findAll()).thenReturn(List.of(t1, t2, t3));
        when(authManager.getRole()).thenReturn("regional_manager");
        this.menuToppingService = new MenuToppingService(toppingRepository, authManager, allergyRepository, pizzaRepository);
        this.menuAllergyService = new MenuAllergyService(allergyRepository, pizzaRepository, toppingRepository, authManager);
        this.menuPizzaService = new MenuPizzaService(pizzaRepository, authManager, allergyRepository, toppingRepository);

    }

    @Test
    public void addAllergyTest_wrongRole_returnsFalse() {
        when(authManager.getRole()).thenReturn("customer");
        Allergy allergy = new Allergy(2L, "allergy");
        when(allergyRepository.findAllergyById(3L)).thenReturn(Optional.of(allergy));
        Assertions.assertThat(menuAllergyService.addAllergy(allergy)).isFalse();
    }

    @Test
    public void addAllergyTest_notPresent_returnsFalse() {
        when(authManager.getRole()).thenReturn("regional_manager");
        when(allergyRepository.findAllergyById(3L)).thenReturn(Optional.empty());
        Assertions.assertThat(menuAllergyService.addAllergy(new Allergy(2L, "allergy"))).isFalse();
    }

    @Test
    public void addPizzaTest() throws Exception {
        Assertions.assertThat(this.menuPizzaService.addPizza(this.p1)).isFalse();
        Pizza p = new Pizza(43L, List.of(t1, t2, t3), "Depperoni", new BigDecimal("78.99"));
        Assertions.assertThat(this.menuPizzaService.addPizza(p)).isTrue();
        Pizza p1 = new Pizza(44L, List.of(t1, t2, t3), "Fepperoni", new BigDecimal("78.99"));
        when(this.toppingRepository.findToppingById(10L)).thenReturn(Optional.empty());
        Assertions.assertThat(this.menuPizzaService.addPizza(p1)).isFalse();
        when(authManager.getRole()).thenReturn("customer");
        when(this.toppingRepository.findToppingById(10L)).thenReturn(Optional.ofNullable(t1));
        Assertions.assertThat(this.menuPizzaService.addPizza(p)).isFalse();
        when(authManager.getRole()).thenReturn("regional_manager");
        when(this.toppingRepository.findToppingById(10L)).thenReturn(Optional.ofNullable(t2));
        Assertions.assertThat(this.menuPizzaService.addPizza(p)).isFalse();
    }

    @Test
    public void priceTest() throws Exception {
        Assertions.assertThat(this.menuPizzaService.getPrice(40L, List.of(11L, 12L))).isEqualTo(new BigDecimal("39.37"));
    }

    @Test
    public void checkForAllergies() throws Exception {
        Assertions.assertThat(
                this.menuPizzaService.checkForAllergies(40L, List.of(10L), List.of("Food"))).isPresent();
        Assertions.assertThat(
                this.menuPizzaService.checkForAllergies(41L, List.of(10L), List.of("Food"))).isPresent();
        Assertions.assertThat(
                this.menuPizzaService.checkForAllergies(40L, List.of(11L), List.of("Peanuts"))).isPresent();
        Assertions.assertThat(
                this.menuPizzaService.checkForAllergies(41L, List.of(12L), List.of("Peanuts"))).isEmpty();
        Assertions.assertThat(
                this.menuPizzaService.checkForAllergies(41L, List.of(12L), List.of("Peanuts"))).isEmpty();
    }

    @Test
    public void filterAllergies() {
        //simple test for filtering out one
        Assertions.assertThat(
                this.menuAllergyService.filterPizzasByAllergens(List.of("Peanuts"))).hasSameElementsAs(List.of(p2));
        //test for filtering out no pizzas
        Assertions.assertThat(
                this.menuAllergyService.filterPizzasByAllergens(List.of("Sugar"))).hasSameElementsAs(List.of(p1, p2, p3));

        ArrayList<Topping> toppings = new ArrayList<>();
        toppings.addAll(p3.getToppings());
        toppings.add(new Topping(6L, "Salami", List.of(bbb), new BigDecimal("32.99")));
        this.p3.setToppings(toppings);

        Assertions.assertThat(
                this.menuAllergyService.filterPizzasByAllergens(List.of("Hawaii"))).hasSameElementsAs(List.of(p1, p2));

        Assertions.assertThat(
                this.menuAllergyService.filterPizzasByAllergens(
                        List.of("Peanuts", "Hawaii", "Food"))).hasSameElementsAs(List.of(p2));
        verify(allergyRepository, times(15)).flush();
    }

    @Test
    public void filterToppings() {
        Assertions.assertThat(
                this.menuAllergyService.filterToppingsByAllergens(List.of("Peanuts"))).hasSameElementsAs(List.of(t3));
        Assertions.assertThat(
                this.menuAllergyService.filterToppingsByAllergens(List.of("Sugar"))).hasSameElementsAs(List.of(t1, t2, t3));
        verify(allergyRepository, times(5)).flush();
    }

    @Test
    public void isValidPizzaList() {
        Assertions.assertThat(this.menuPizzaService.isValidPizzaList(40L, List.of(11L, 12L))).isTrue();
        Assertions.assertThat(this.menuPizzaService.isValidPizzaList(99L, List.of())).isFalse();
        Assertions.assertThat(this.menuPizzaService.isValidPizzaList(40L, List.of(99999L))).isFalse();
        Assertions.assertThat(this.menuPizzaService.isValidPizzaList(40L, List.of(11L, 99999L))).isFalse();
        Assertions.assertThat(this.menuPizzaService.isValidPizzaList(null, null)).isFalse();

    }

    @Test
    public void getAllergyByNameTest() {
        Assertions.assertThat(this.menuAllergyService.getAllergyByName(null)).isEmpty();
        Assertions.assertThat(this.menuAllergyService.getAllergyByName("Peanuts")).isPresent();
    }

    @Test
    public void getAllToppingsTest_emptyList() {
        when(toppingRepository.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertThat(menuToppingService.getAllToppings()).isEqualTo(Collections.emptyList());
        verify(toppingRepository, times(1)).flush();
    }

    @Test
    public void getAllToppingsTest_listWithElements() {
        Topping t1 = new Topping(18L, "Potato", List.of(this.aaa, this.bbb), new BigDecimal("10.99"));
        Topping t2 = new Topping(19L, "Potato2", List.of(this.aaa, this.ccc), new BigDecimal("10.99"));

        when(toppingRepository.findAll()).thenReturn(List.of(t1, t2));
        Assertions.assertThat(menuToppingService.getAllToppings()).containsExactlyInAnyOrder(t2, t1);
        verify(toppingRepository, times(1)).flush();
    }

    @Test
    public void addTopping() {
        Allergy ddd = new Allergy(988L, "Something made up");
        Topping t = new Topping(18L, "Potato", List.of(this.aaa, ddd), new BigDecimal("10.99"));
        Assertions.assertThat(this.menuToppingService.addTopping(t)).isFalse();
        Topping t2 = new Topping(19L, "Potato", List.of(this.aaa, this.ccc), new BigDecimal("10.99"));
        Assertions.assertThat(this.menuToppingService.addTopping(t2)).isTrue();
        Assertions.assertThat(this.menuToppingService.addTopping(null)).isFalse();
        verify(toppingRepository, times(1)).flush();
        verify(toppingRepository, times(1)).save(any());
    }

    @Test
    public void addTopping_throws_returnsFalse() throws Exception {
        when(toppingRepository.findToppingById(any(Long.class))).thenThrow(new Exception("Database error"));
        Assertions.assertThat(this.menuToppingService.addTopping(
                new Topping(2L, "topping", null, null))).isFalse();
    }

    @Test
    public void getAllergyById() {
        Assertions.assertThat(this.menuAllergyService.getAllergyById(null)).isEmpty();
        verify(allergyRepository, times(1)).flush();
    }

    @Test
    public void getPizzaById() {
        Assertions.assertThat(this.menuPizzaService.getPizzaById(null)).isEmpty();
    }

    @Test
    public void getToppingById() throws Exception {
        Assertions.assertThat(this.menuToppingService.getToppingById(null)).isEmpty();
        verify(toppingRepository, times(1)).flush();
    }

    @Test
    public void removePizzaById() {
        Assertions.assertThat(this.menuPizzaService.removePizzaById(null)).isFalse();
        Assertions.assertThat(this.menuPizzaService.removePizzaById(3948793L)).isFalse();
        Assertions.assertThat(this.menuPizzaService.removePizzaById(40L)).isTrue();
    }

    @Test
    public void removeToppingById() throws Exception {
        Assertions.assertThat(this.menuToppingService.removeToppingById(null)).isFalse();
        Assertions.assertThat(this.menuToppingService.removeToppingById(3948793L)).isFalse();
        Assertions.assertThat(this.menuToppingService.removeToppingById(10L)).isTrue();
        verify(toppingRepository, times(1)).flush();
    }

    @Test
    public void removeAllergyById() {
        Assertions.assertThat(this.menuAllergyService.removeAllergyById(null)).isFalse();
        Assertions.assertThat(this.menuAllergyService.removeAllergyById(3948793L)).isFalse();
        Assertions.assertThat(this.menuAllergyService.removeAllergyById(1L)).isTrue();
        verify(allergyRepository, times(4)).flush();
    }



}
