package com.luxdrive.carrentalback.services.customer;

import java.util.List;

import com.luxdrive.carrentalback.dto.BookACarDto;
import com.luxdrive.carrentalback.dto.CarDto;
import com.luxdrive.carrentalback.dto.CarDtoListDto;
import com.luxdrive.carrentalback.dto.CarSearchDto;

public interface CustomerService {
    List<CarDto> getAllCars();
    boolean bookACar(BookACarDto bookACarDto);
    CarDto getCarById(long carId);
    List<BookACarDto> getBookingsByUserId(Long userId);
    CarDtoListDto searchCar(CarSearchDto carSearchDto);

}
