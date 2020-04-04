package order.services.tool.exception;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderExceptionHandler
{

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInformationModel badRequestError(MethodArgumentNotValidException ex)
    {
        LOGGER.error("Bad request", ex);
        return new ErrorInformationModel(ex.getMessage());
    }
    
    @ExceptionHandler(value = IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInformationModel connectionError(IOException ex)
    {
        LOGGER.error("Connection error", ex);
        return new ErrorInformationModel(ex.getMessage());
    }

//    @ExceptionHandler(value = ApiClientException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public ErrorInformationModel invalidInputError(ApiClientException e)
//    {
//        LOGGER.error("Bad request", e);
//        return new ErrorInformationModel(e.getMessage());
//    }
//

//
//    @ExceptionHandler(value = DataNotFoundException.class)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void dataNotFoundError(DataNotFoundException e)
//    {
//        LOGGER.error("No data found", e);
//
//    }

}
