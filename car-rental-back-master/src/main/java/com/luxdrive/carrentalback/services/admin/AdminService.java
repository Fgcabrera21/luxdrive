package com.luxdrive.carrentalback.services.admin;

import java.io.IOException;
import java.util.List;

import com.luxdrive.carrentalback.dto.BookACarDto;
import com.luxdrive.carrentalback.dto.CarDto;
import com.luxdrive.carrentalback.dto.CarDtoListDto;
import com.luxdrive.carrentalback.dto.CarSearchDto;

public interface AdminService {
    boolean postCar(CarDto carDto) throws IOException;
    List<CarDto> getAllCars();
    void deleteCar(Long id);
    CarDto getCarById(Long id);
    boolean updateCar(Long carId, CarDto carDto) throws IOException;
    List<BookACarDto> getBookings();
    boolean changeBookingStatus(Long bookingId, String status);
    CarDtoListDto searchCar(CarSearchDto carSearchDto);

}
