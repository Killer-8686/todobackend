package ru.javabegin.backend.todo.aop;


import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Log
public class LoggingAspect {

    @Around("execution(* ru.javabegin.backend.todo.controllers..*(..)))")
    public Object profileControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        log.info("_____________ Executing " + className + "." + methodName + " _____________");


        StopWatch coundown = new StopWatch();
        coundown.start();

        Object result = proceedingJoinPoint.proceed();
        coundown.stop();

        log.info("_____________ Execution time of " + className + "." +
                methodName + " :: " + coundown.getTotalTimeMillis() +" _____________");

        return result;

    }
}
