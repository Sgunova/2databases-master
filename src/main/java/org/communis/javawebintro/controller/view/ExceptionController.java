package org.communis.javawebintro.controller.view;

import lombok.extern.log4j.Log4j2;
import org.communis.javawebintro.exception.NotFoundException;
import org.communis.javawebintro.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@ControllerAdvice(basePackages = {
        "org.communis.javawebintro.controller.view"
})
public class ExceptionController {

    /**
     * Путь к представлению страницы  ошибки
     */
    private static final String ERROR_PAGE = "errors/error";

    /**
     * Обрабатывает 404-ую ошибку (ресурс не найден)
     *
     * @return представление страницы 404-ой ошибки
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NoHandlerFoundException.class, NotFoundException.class})
    public ModelAndView notFoundHandler(Exception ex) {
        ModelAndView mav = new ModelAndView(ERROR_PAGE);
        mav.addObject("errorCode", "404");
        mav.addObject("errorMessage", ex instanceof NotFoundException ?
                ((ServerException) ex).getFriendlyMessage() : "Страница не найдена");
        return mav;
    }

    /**
     * Обрабатывает 500-ую ошибку (ошибка на сервере)
     *
     * @param ex исключение
     * @return представление страницы 500-ой ошибки
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception ex) {
        log.error(ex instanceof ServerException ?
                ((ServerException) ex).getFriendlyMessage() : ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView(ERROR_PAGE);
        mav.addObject("errorCode", "500");
        mav.addObject("errorMessage", ex instanceof ServerException ?
                ((ServerException) ex).getFriendlyMessage() : "Ошибка на сервере");
        return mav;
    }

    /**
     * Обрабатывает 405-ую ошибку (неподдерживаемый метод запроса)
     *
     * @param ex      исключение
     * @param request запрос
     * @return представление страницы 405-ой ошибки
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView methodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView(ERROR_PAGE);
        mav.addObject("errorCode", "405");
        mav.addObject("errorMessage", "Метод не поддерживается");
        return mav;
    }

    /**
     * Обрабатывает 403-ую ошибку (нет доступа)
     *
     * @return представление страницы 403-ой ошибки
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView forbiddenHandler(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView(ERROR_PAGE);
        mav.addObject("errorCode", "403");
        mav.addObject("errorMessage", "Доступ запрещен");
        return mav;
    }
}