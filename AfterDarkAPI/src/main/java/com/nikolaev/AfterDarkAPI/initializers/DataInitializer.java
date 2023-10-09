package com.nikolaev.AfterDarkAPI.initializers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nikolaev.AfterDarkAPI.enummuration.PaymentMethod;
import com.nikolaev.AfterDarkAPI.enummuration.PurchaseService;
import com.nikolaev.AfterDarkAPI.enummuration.StageOfWork;
import com.nikolaev.AfterDarkAPI.models.Basket;
import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.models.ColorShape;
import com.nikolaev.AfterDarkAPI.models.Gypsum;
import com.nikolaev.AfterDarkAPI.models.Order;
import com.nikolaev.AfterDarkAPI.models.Shape;
import com.nikolaev.AfterDarkAPI.models.Smell;
import com.nikolaev.AfterDarkAPI.models.User;
import com.nikolaev.AfterDarkAPI.models.Wax;
import com.nikolaev.AfterDarkAPI.models.Wick;
import com.nikolaev.AfterDarkAPI.repositories.BasketRepository;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;
import com.nikolaev.AfterDarkAPI.repositories.ColorShapeRepository;
import com.nikolaev.AfterDarkAPI.repositories.GypsumRepository;
import com.nikolaev.AfterDarkAPI.repositories.OrderRepository;
import com.nikolaev.AfterDarkAPI.repositories.ShapeRepository;
import com.nikolaev.AfterDarkAPI.repositories.SmellRepository;
import com.nikolaev.AfterDarkAPI.repositories.UserRepository;
import com.nikolaev.AfterDarkAPI.repositories.WaxRepository;
import com.nikolaev.AfterDarkAPI.repositories.WickRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private WickRepository wickRepository;
    private WaxRepository waxRepository;
    private SmellRepository smellRepository;
    private ShapeRepository shapeRepository;
    private GypsumRepository gypsumRepository;
    private ColorShapeRepository colorShapeRepository;
    private CandleRepository candleRepository;
    private UserRepository userRepository;
    private BasketRepository basketRepository;
    private OrderRepository orderRepository;

    @Autowired
    public DataInitializer(WickRepository wickRepository, WaxRepository waxRepository, SmellRepository smellRepository,
            ShapeRepository shapeRepository, GypsumRepository gypsumRepository,
            ColorShapeRepository colorShapeRepository, CandleRepository candleRepository, UserRepository userRepository,
            BasketRepository basketRepository, OrderRepository orderRepository) {
        this.wickRepository = wickRepository;
        this.waxRepository = waxRepository;
        this.smellRepository = smellRepository;
        this.shapeRepository = shapeRepository;
        this.gypsumRepository = gypsumRepository;
        this.colorShapeRepository = colorShapeRepository;
        this.candleRepository = candleRepository;
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i < 100; i++) {

            Wick wick = new Wick((long) i, "Wick " + i, "Description " + i, 10 + i, 5 + i);
            wickRepository.save(wick);

            Wax wax = new Wax();
            wax.setName("Wax " + i);
            wax.setDescription("Description " + i);
            wax.setQuanity(10 + i);
            wax.setPrice(5 * i);

            waxRepository.save(wax);

            Smell smell = new Smell();
            smell.setName("Smell " + i);
            smell.setDescription("Description for Smell " + i);
            smell.setQuanity(i * 2); // Например, увеличиваем количество на 2 для каждой записи
            smell.setPrice(i * 10); // Например, увеличиваем цену на 10 для каждой записи
            smellRepository.save(smell);

            Shape shape = new Shape(i, "Shape " + i, "Description " + i, i * 10, i, i * 100);
            shapeRepository.save(shape);

            Gypsum gypsum = new Gypsum();
            gypsum.setName("Gypsum " + i);
            gypsum.setDescription("Description for Gypsum " + i);
            gypsum.setPrice(100 * i);
            gypsum.setQuanity(50 + i);

            gypsumRepository.save(gypsum);

            ColorShape colorShape = new ColorShape(
                    i,
                    "Shape " + i,
                    "Description " + i,
                    100 * i);
            colorShapeRepository.save(colorShape);

            Candle candle = new Candle(
                    i, // id
                    "Candle " + i, // name
                    "Description for Candle " + i, // description
                    i * 2, // quantity
                    i * 10, // price
                    i % 2 == 0, // custom (чередующееся значение true/false)
                    colorShape,
                    smell,
                    wax,
                    wick,
                    shape);

            candleRepository.save(candle);
        }

        User user1 = new User();
        user1.setName("John Doe");
        user1.setLogin("john_doe");
        user1.setPassword("password123");

        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setLogin("jane_smith");
        user2.setPassword("password456");

        userRepository.save(user2);

        List<Candle> candlesInBasket1 = new ArrayList<>();

        candlesInBasket1.add(candleRepository.findById((long) 1).orElse(null));
        candlesInBasket1.add(candleRepository.findById((long) 2).orElse(null));
        candlesInBasket1.add(candleRepository.findById((long) 3).orElse(null));

        Basket basket1 = new Basket();
        basket1.setUser(user1);
        basket1.setCandles(candlesInBasket1);

        basketRepository.save(basket1);

        List<Candle> candlesInBasket2 = new ArrayList<>();
        Basket basket2 = new Basket();
        basket2.setUser(user2);
        basket2.setCandles(candlesInBasket2);

        basketRepository.save(basket2);

        Order order1 = new Order();
        order1.setPurchaseService(PurchaseService.TELEGRAM);
        order1.setPaymentMethod(PaymentMethod.CASH);
        order1.setStageOfWork(StageOfWork.ORDERED);
        order1.setCommunication("john_doe@example.com");
        order1.setAddress("123 Main St, City");
        order1.setPaid(true);
        order1.setDateOfPurchase("2023-09-25");
        order1.setDateOfDelivery("2023-10-02");
        order1.setPrice(10);
        order1.setCandles(candlesInBasket1);
        order1.setUser(user1);

        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setPurchaseService(PurchaseService.REAL);
        order2.setPaymentMethod(PaymentMethod.CARD);
        order2.setStageOfWork(StageOfWork.ATWORK);
        order2.setCommunication("jane_smith@example.com");
        order2.setAddress("456 Elm St, Town");
        order2.setPaid(true);
        order2.setDateOfPurchase("2023-09-26");
        order2.setDateOfDelivery("2023-10-03");
        order2.setPrice(15);
        order2.setCandles(candlesInBasket2);
        order2.setUser(user2);

        orderRepository.save(order2);
    }
}
