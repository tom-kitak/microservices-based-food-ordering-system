package nl.tudelft.sem.group06b.order.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import nl.tudelft.sem.group06b.order.model.ApplyCouponsToOrderModel;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderProcessor {

    private final transient OrderRepository orderRepository;

    @Getter
    @Setter
    private List<Long> activeOrders;

    private final transient RestTemplate restTemplate;

    private final transient TaskScheduler taskScheduler;

    private final transient String valid = "VALID";
    private final transient String invalidOrderIdMessage = "Invalid order ID";
    private final transient String noActiveOrderMessage = "No active order with this ID";
    private final transient int deadlineOffset = 30;
    private final transient String storeUrl = "http://localhost:8084/api/stores";
    private final transient String storeEmailUrl = "http://localhost:8084/api/email";
    private final transient String couponUrl = "http://localhost:8083/api/coupons";
    private final transient String menuUrl = "http://localhost:8086/api/menu";

    /**
     * Instantiates a new OrderProcessor.
     *
     * @param orderRepository repository of orders
     * @param taskScheduler   scheduler for tasks
     */
    public OrderProcessor(OrderRepository orderRepository, TaskScheduler taskScheduler) {
        this.orderRepository = orderRepository;
        this.taskScheduler = taskScheduler;
        this.activeOrders = new ArrayList<>();
        this.restTemplate = new RestTemplate();

        orderRepository.findAll().forEach(order -> {
            if (order.getStatus() == Status.ORDER_PLACED) {
                activeOrders.add(order.getId());
                scheduleOrderCompletion(order.getId());
            }
        });
    }

    /**
     * Gets order repository.
     *
     * @return order repository
     */
    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    /**
     * Method that instantiates an order to ongoing stage.
     *
     * @param location     location of the order
     * @param memberId     member ID of the person placing the order
     * @param selectedTime selected time of the order
     * @param token        authentication token
     * @return confirmation response if the order was successful and details of what went wrong if not successful
     * @throws Exception when selectedTime or location is not valid
     */
    public String startOrder(String location, String memberId, String selectedTime, String token) throws Exception {
        if (selectedTime == null) {
            throw new Exception("Please select time");
        } else if (location == null) {
            throw new Exception("Please select location");
        } else if (memberId == null) {
            throw new Exception("Member ID invalid");
        }

        // validate if the time is appropriate
        String isTimeValid = isTimeValid(selectedTime, deadlineOffset);
        if (!isTimeValid.equals(valid)) {
            throw new Exception("Order has to be at least " + deadlineOffset + " minutes in the future.");
        }

        // validate location for the store
        validateLocation(location, token);

        // get storedID of the store with provided location from Store microservice
        Long storeId = getStoreIdFromLocation(location, token);

        // creating an order
        Order order = new Order();
        order.setSelectedTime(selectedTime);
        order.setLocation(location);
        order.setStatus(Status.ORDER_ONGOING);
        order.setMemberId(memberId);
        order.setStoreId(storeId);

        orderRepository.save(order);
        activeOrders.add(order.getId());
        System.out.println("Ongoing order: " + order.getId());

        return "Order number " + order.getId() + " ongoing";
    }

    /**
     * Changes the time of an order.
     *
     * @param orderId      id of the order
     * @param selectedTime selected time of the order
     * @return message of outcome
     */
    public String changeSelectedTime(Long orderId, String selectedTime) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        } else if (!activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        }
        if (selectedTime == null) {
            throw new Exception("Please select time");
        }
        String isTimeValid = isTimeValid(selectedTime, 30);
        if (!isTimeValid.equals(valid)) {
            throw new Exception("New time has to be at least " + deadlineOffset + " minutes in the future.");
        }

        Order order = orderRepository.getOne(orderId);
        order.setSelectedTime(selectedTime);
        orderRepository.save(order);
        return "Time for order " + orderId + " changed to " + selectedTime + ".";
    }

    /**
     * Changes the location of the order if the selected order is in ongoing phase.
     *
     * @param location new location
     * @param orderId  ID of the order
     * @param token    authentication token
     * @return message of the outcome
     * @throws Exception when location is not valid
     */
    public String changeSelectedLocation(String location, Long orderId, String token) throws Exception {
        if (location == null) {
            throw new Exception(invalidOrderIdMessage);
        } else if (!activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        } else if (location == null) {
            throw new Exception("Please enter a valid location");
        }

        // validate new location for the store
        validateLocation(location, token);

        // get the new storedID of the store with provided location
        Long storeId = getStoreIdFromLocation(location, token);

        Order order = orderRepository.getOne(orderId);
        order.setStoreId(storeId);
        order.setLocation(location);
        orderRepository.save(order);

        return "Store changed successfully";
    }

    /**
     * Add pizzas to the order.
     *
     * @param memberId ID of the member that placed the order
     * @param orderId  ID of the order
     * @param pizzas   list of pizzas to add
     * @param token    authentication token
     * @return message of the outcome with appropriate reminders of possible allergens
     * @throws Exception when pizza is not valid
     */
    public String addPizzas(String memberId, Long orderId, List<Pizza> pizzas, String token) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        } else if (!activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        } else if (pizzas == null) {
            throw new Exception("Please enter valid pizzas");
        } else if (memberId == null) {
            throw new Exception("Order does not have a valid member Id");
        }
        StringBuilder allergensResponse = new StringBuilder();
        allergensResponse.append("Pizzas successfully added");

        // Query the Menu to see if pizzas are valid
        for (Pizza pizza : pizzas) {
            validatePizza(pizza, token);
        }

        // Query the Menu to see if pizzas contain an allergen and store the response to inform the user
        for (Pizza pizza : pizzas) {
            String responseMessage = containsAllergen(pizza, memberId, token);
            if (responseMessage != null && !responseMessage.equals("")) {
                allergensResponse.append("\n" + responseMessage);
            }
        }

        Order order = orderRepository.getOne(orderId);
        order.getPizzas().addAll(pizzas);
        orderRepository.save(order);

        // TODO
        // encapsulate String responses of allergens to a List<Allergen>
        return allergensResponse.toString();
    }

    /**
     * Applies coupon to an order.
     *
     * @param orderId    id of the order
     * @param couponsIds ids of the coupons entered
     * @return message of outcome
     */
    public String addCoupons(Long orderId, List<String> couponsIds, String token) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        } else if (!activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        } else if (couponsIds == null) {
            throw new Exception("Please enter valid coupons");
        }

        // Call to Coupon-microservice to see if coupons are valid
        for (String coupon : couponsIds) {
            HttpHeaders headers = makeHeader(token);
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    couponUrl + "/checkAvailability/" + coupon,
                    HttpMethod.GET,
                    request,
                    Boolean.class
            );
            if (response.getBody() == false) {
                throw new Exception("Coupon " + coupon + " is not valid");
            }
        }

        Order order = orderRepository.getOne(orderId);
        order.getCouponsIds().addAll(couponsIds);
        orderRepository.save(order);

        return "Coupons successfully added";
    }

    /**
     * Place the order.
     *
     * @param orderId ID of the order to place
     * @param token   authentication token
     * @return receipt of the order
     * @throws Exception when there are no Pizzas in the order
     */
    public String placeOrder(Long orderId, String token) throws Exception {
        if (orderId == null || !activeOrders.contains(orderId)) {
            throw new Exception(noActiveOrderMessage);
        }

        Order order = orderRepository.getOne(orderId);

        // query Menu for the prices
        if (order.getPizzas() == null || !order.getPizzas().isEmpty()) {
            throw new Exception("There are no valid pizzas in the order");
        }
        for (Pizza pizza : order.getPizzas()) {
            pizza.setPrice(getPizzaPriceFromMenu(pizza, token));
        }

        // query Coupon for the discount and coupon used
        if (order.getCouponsIds() != null && !order.getCouponsIds().isEmpty()) {
            ApplyCouponsToOrderModel applyCouponsToResponse = applyCouponsToOrder(order.getPizzas(),
                    order.getCouponsIds(), token);
            if (applyCouponsToResponse.getCoupons() == null || !applyCouponsToResponse.getCoupons().isEmpty()) {
                order.setCouponApplied(null);
            } else {
                order.setCouponApplied(applyCouponsToResponse.getCoupons().get(0));
            }
            order.setPizzas(applyCouponsToResponse.getPizzas());
        }

        order.calculateTotalPrice();
        order.setStatus(Status.ORDER_PLACED);
        scheduleOrderCompletion(orderId);
        orderRepository.save(order);
        activeOrders.remove(orderId);

        // notify the store
        sendEmailToStore(order.getStoreId(), order.formatEmail(), token);

        // send receipt
        String receipt = order.formatReceipt();

        // TODO
        // Return order
        return receipt;
    }

    /**
     * Schedules the order to be set to completed at the due date, if the date was changed reschedules the order.
     *
     * @param orderId ID of the order
     */
    public void scheduleOrderCompletion(long orderId) {
        Order order = orderRepository.getOne(orderId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime due = LocalDateTime.parse(order.getSelectedTime(), formatter);
        if (order.getStatus() == Status.ORDER_PLACED && due.isBefore(LocalDateTime.now())) {
            order.setStatus(Status.ORDER_FINISHED);
            orderRepository.save(order);
        } else {
            taskScheduler.schedule(() ->
                    scheduleOrderCompletion(orderId), Date.from(due.atZone(ZoneId.systemDefault()).toInstant()));
        }
    }

    /**
     * Cancels an order.
     *
     * @param orderId id of the order
     * @return message of outcome
     */
    public String cancelOrder(Long orderId) throws Exception {
        if (orderId == null) {
            throw new Exception(invalidOrderIdMessage);
        }

        // TODO
        // If you are an admin you can cancel anytime and not just 30 min before

        Order order = orderRepository.getOne(orderId);

        String isTimeValid = isTimeValid(order.getSelectedTime(), 30);
        if (!isTimeValid.equals(valid)) {
            throw new Exception("You can no longer cancel the order");
        }

        order.setStatus(Status.ORDER_CANCELED);
        orderRepository.save(order);

        // TODO
        // Notification: Notify the store about the cancellation

        return "Order canceled successfully";
    }

    /**
     * Checks if the time is valid and makes sense.
     *
     * @param time           time to check
     * @param deadlineOffset integer of how many minutes before the provided time is the deadline
     * @return String that indicates if the time is valid or not
     * @throws Exception indication the format of time is not correct
     */
    private String isTimeValid(String time, int deadlineOffset) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        try {
            LocalDateTime orderDate = LocalDateTime.parse(time, formatter);
            LocalDateTime currentDate = LocalDateTime.now();

            if (orderDate.minusMinutes(deadlineOffset).isBefore(currentDate)) {
                return "The time is invalid.";
            }
            return valid;
        } catch (Exception e) {
            throw new Exception("Please provide the correct time format: dd/MM/yyyy HH:mm:ss");
        }
    }

    private void validateLocation(String location, String token) throws Exception {
        HttpHeaders headerForValidation = makeHeader(token);
        HttpEntity requestValidation = new HttpEntity(headerForValidation);
        ResponseEntity<Boolean> responseValidation = restTemplate.exchange(
                storeUrl + "/validateLocation/" + location,
                HttpMethod.GET,
                requestValidation,
                Boolean.class
        );
        if (responseValidation.getBody() == false) {
            throw new Exception("Location " + location + " is not valid");
        }
    }

    private Long getStoreIdFromLocation(String location, String token) throws Exception {
        HttpHeaders headerForStoreId = makeHeader(token);
        HttpEntity requestStoreId = new HttpEntity(headerForStoreId);
        ResponseEntity<Long> responseStoreId = restTemplate.exchange(
                storeUrl + "/getStoreId/" + location,
                HttpMethod.GET,
                requestStoreId,
                Long.class
        );
        if (responseStoreId.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new Exception("Problem with location, please try again");
        }
        return responseStoreId.getBody();
    }

    private void validatePizza(Pizza pizza, String token) throws Exception {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("id", pizza.getPizzaId());
        map.put("toppingIds", pizza.getToppings());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(menuUrl + "/isValid", entity, Boolean.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == false) {
            throw new Exception("Pizza " + pizza.getPizzaId() + " is not valid");
        }
    }

    private String containsAllergen(Pizza pizza, String memberId, String token) {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("id", pizza.getPizzaId());
        map.put("toppingIds", pizza.getToppings());
        map.put("memberId", memberId);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(menuUrl + "/containsAllergen", entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return "";
        }

        return response.getBody();
    }

    private BigDecimal getPizzaPriceFromMenu(Pizza pizza, String token) {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("id", pizza.getPizzaId());
        map.put("toppingIds", pizza.getToppings());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<BigDecimal> response = restTemplate.postForEntity(menuUrl + "", entity, BigDecimal.class);

        return response.getBody();
    }

    private ApplyCouponsToOrderModel applyCouponsToOrder(List<Pizza> pizzas, List<String> coupons, String token) {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("pizzas", pizzas);
        map.put("coupons", coupons);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<ApplyCouponsToOrderModel> response = restTemplate.postForEntity(couponUrl + "/calculatePrice",
                entity, ApplyCouponsToOrderModel.class);

        return response.getBody();
    }

    private void sendEmailToStore(Long storeId, String email, String token) {
        HttpHeaders headers = makeHeader(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("email", email);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(storeEmailUrl + "/sendEmail", entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
        }
        System.out.println("Problem with sending an email");
    }

    /**
     * Makes HttpHeaders with the selected token.
     *
     * @param token authentication token
     * @return HttpHeaders with appropriate Authorization set
     */
    private HttpHeaders makeHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", token));
        return headers;
    }
}

