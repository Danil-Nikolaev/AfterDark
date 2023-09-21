package com.nikolaev.AfterDarkAPI.initializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nikolaev.AfterDarkAPI.models.Candle;
import com.nikolaev.AfterDarkAPI.models.ColorShape;
import com.nikolaev.AfterDarkAPI.models.Gypsum;
import com.nikolaev.AfterDarkAPI.models.Shape;
import com.nikolaev.AfterDarkAPI.models.Smell;
import com.nikolaev.AfterDarkAPI.models.Wax;
import com.nikolaev.AfterDarkAPI.models.Wick;
import com.nikolaev.AfterDarkAPI.repositories.CandleRepository;
import com.nikolaev.AfterDarkAPI.repositories.ColorShapeRepository;
import com.nikolaev.AfterDarkAPI.repositories.GypsumRepository;
import com.nikolaev.AfterDarkAPI.repositories.ShapeRepository;
import com.nikolaev.AfterDarkAPI.repositories.SmellRepository;
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

    @Autowired
    public DataInitializer(WickRepository wickRepository, WaxRepository waxRepository, SmellRepository smellRepository,
            ShapeRepository shapeRepository, GypsumRepository gypsumRepository,
            ColorShapeRepository colorShapeRepository, CandleRepository candleRepository) {
        this.wickRepository = wickRepository;
        this.waxRepository = waxRepository;
        this.smellRepository = smellRepository;
        this.shapeRepository = shapeRepository;
        this.gypsumRepository = gypsumRepository;
        this.colorShapeRepository = colorShapeRepository;
        this.candleRepository = candleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
       for (int i = 0; i < 100; i++) {

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
                100 * i
            );
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
                shape
            );

            candleRepository.save(candle);
       }
    }

    // private void generateWick() {
    //     for (int i = 1; i <= 10; i++) {
           
    //     }
    // }

    // private void generateWax() {
    // for (int i = 1; i <= 10; i++) {
    //     Wax wax = new Wax();
    //     wax.setName("Wax " + i);
    //     wax.setDescription("Description " + i);
    //     wax.setQuanity(10 + i);
    //     wax.setPrice(5 * i);

    //     waxRepository.save(wax);
    // }
    // }

    // private void generateSmell() {
    //     for (int i = 1; i <= 10; i++) {
    //         Smell smell = new Smell();
    //         smell.setName("Smell " + i);
    //         smell.setDescription("Description for Smell " + i);
    //         smell.setQuanity(i * 2); // Например, увеличиваем количество на 2 для каждой записи
    //         smell.setPrice(i * 10); // Например, увеличиваем цену на 10 для каждой записи
    //         smellRepository.save(smell);
    //     }
    // }

    // private void generateShape() {
    //     for (int i = 1; i <= 10; i++) {
    //         Shape shape = new Shape(i, "Shape " + i, "Description " + i, i * 10, i, i * 100);
    //         shapeRepository.save(shape);
    //     }
    // }

    // private void generateGypsum() {
    //     for (int i = 1; i <= 10; i++) {
    //         Gypsum gypsum = new Gypsum();
    //         gypsum.setName("Gypsum " + i);
    //         gypsum.setDescription("Description for Gypsum " + i);
    //         gypsum.setPrice(100 * i);
    //         gypsum.setQuanity(50 + i);

    //         gypsumRepository.save(gypsum);
    //     }
    // }

    // private void generateColorShape() {
    //     for (int i = 1; i <= 10; i++) {
    //         ColorShape colorShape = new ColorShape(
    //             i,
    //             "Shape " + i,
    //             "Description " + i,
    //             100 * i
    //         );
    //         colorShapeRepository.save(colorShape);
    //     }
    // }

    // private void generateCandle() {

    // }
}
