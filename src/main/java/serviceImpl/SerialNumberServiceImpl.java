package serviceImpl;

import enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import model.SerialNumber;
import repository.dao.SerialNumberDao;
import service.SerialNumberService;
import utils.Response;

@Slf4j
public class SerialNumberServiceImpl implements SerialNumberService {

    private final SerialNumberDao serialNumberDao ;

    public SerialNumberServiceImpl(SerialNumberDao serialNumberDao) {
        this.serialNumberDao = serialNumberDao;
    }

    @Override
    public Response updateSerialNumber(SerialNumber serialNumber, boolean isBorrowedBook) {
        Response response = new Response();
        try {
            boolean bookOptional = serialNumberDao.updateSerialNumber(serialNumber,isBorrowedBook);
            if (bookOptional) {
                response = Response.builder().responseObject(null).statusCode(ResponseStatus.SUCCESS).message("Book Update Successful...").build();
            } else {
                response = Response.builder().statusCode(ResponseStatus.Error).message("Book not found...").build();
            }
        } catch (Exception e) {
            Response.builder().statusCode(ResponseStatus.Error).message("Something went wrong").build();
            log.info("Error during UpdateBook :", e);
        }
        return response;
    }
}
