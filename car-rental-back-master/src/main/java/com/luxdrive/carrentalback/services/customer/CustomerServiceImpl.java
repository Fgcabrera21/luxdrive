package com.luxdrive.carrentalback.services.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.luxdrive.carrentalback.dto.BookACarDto;
import com.luxdrive.carrentalback.dto.CarDto;
import com.luxdrive.carrentalback.dto.CarDtoListDto;
import com.luxdrive.carrentalback.dto.CarSearchDto;
import com.luxdrive.carrentalback.entities.BookACar;
import com.luxdrive.carrentalback.entities.Car;
import com.luxdrive.carrentalback.entities.User;
import com.luxdrive.carrentalback.enums.BookCarStatus;
import com.luxdrive.carrentalback.repositories.BookACarRepository;
import com.luxdrive.carrentalback.repositories.CarRepository;
import com.luxdrive.carrentalback.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;


    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());

        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            Car existingCar = optionalCar.get();
            BookACar bookACar = new BookACar();

            bookACar.setUser(optionalUser.get());
            bookACar.setCar(existingCar);
            bookACar.setFromDate(bookACarDto.getFromDate());
            bookACar.setToDate(bookACarDto.getToDate());
            bookACar.setBookCarStatus(BookCarStatus.PENDING);
            long diffInMs = bookACarDto.getToDate().getTime() - bookACarDto.getFromDate().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffInMs);
            bookACar.setDays(days);
            bookACar.setPrice(existingCar.getPrice() * days);

            bookACarRepository.save(bookACar);
            return true;
        }
        return false;
    }

    @Override
    public CarDto getCarById(long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            return optionalCar.map(Car::getCarDto).orElse(null);
        }
        return null;
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(Long userId) {
        return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }

    @Override
    public CarDtoListDto searchCar(CarSearchDto carSearchDto) {
        Car car = new Car();
        car.setBrand(carSearchDto.getBrand());
        car.setType(carSearchDto.getType());
        car.setTransmission(carSearchDto.getTransmission());
        car.setColor(carSearchDto.getColor());

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Car> carExample = Example.of(car, exampleMatcher);
        List<Car> carList = carRepository.findAll(carExample);
        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));
        return carDtoListDto;
    }

}
