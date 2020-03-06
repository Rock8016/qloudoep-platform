package com.hrjk.fin.demo.exception;

import java.util.function.Predicate;
import org.springframework.security.access.AccessDeniedException;

public class RecordFailurePredicate implements Predicate<Throwable> {
    @Override
    public boolean test(Throwable throwable) {
        return !(( throwable instanceof BusinessException ) ||  ( throwable instanceof AccessDeniedException ) );
    }
}