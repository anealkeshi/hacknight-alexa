package me.anilkc.hacknight.controller;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import me.anilkc.hacknight.exception.HackNightException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

  private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handle(MethodArgumentNotValidException exception) {
    // @formatter:off
      return error(exception.getBindingResult().getFieldErrors()
              .stream()
              .map(e -> e.getField() + " " + e.getDefaultMessage())
              .collect(Collectors.toList()));
   // @formatter:on
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handle(HttpMessageNotReadableException exception) {
    // @formatter:off
      return error(exception.getMessage());
   // @formatter:on
  }


  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handle(ConstraintViolationException exception) {
    // @formatter:off
      return error(exception.getConstraintViolations()
              .stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.toList()));
   // @formatter:off
  }
  
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handle(HackNightException exception) {
    // @formatter:off
      return error(exception.getMessage());
   // @formatter:on
  }

  private Map<String, Object> error(Object message) {
    LOG.error(message.toString());
    return Collections.singletonMap("error", message);
  }
}
