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
    
    @ExceptionHandler({ IOException.class, DatabaseException.class, DataNotFoundException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInformationModel connectionError(Exception ex)
    {
        LOGGER.error("Internal Server Error", ex);
        return new ErrorInformationModel(ex.getMessage());
    }
 
}
