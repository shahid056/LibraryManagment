package service;

import model.SerialNumber;
import utils.Response;

public interface SerialNumberService {

    Response updateSerialNumber(SerialNumber serialNumber, boolean isBorrowedBook);

}
