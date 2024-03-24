package com.edu.kt.gw.simple.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ReactorCacheAspect {
    /*private final ReactiveCacheManager reactiveCacheManager;

    @Autowired
    public ReactorCacheAspect(ReactiveCacheManager reactiveCacheManager) {
        this.reactiveCacheManager = reactiveCacheManager;
    }

    @Pointcut("@annotation(com.kt.edu.thirdproject.common.annotation.ReactorCacheable)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
        Type rawType = parameterizedType.getRawType();

        if (!rawType.equals(Mono.class) && !rawType.equals(Flux.class)) {
            throw new IllegalArgumentException("The return type is not Mono/Flux. Use Mono/Flux for return type. method: " + method.getName());
        }
        ReactorCacheable reactorCacheable = method.getAnnotation(ReactorCacheable.class);
        String cacheName = reactorCacheable.name();
        Object[] args = joinPoint.getArgs();

        // joinpoint.proceed()를 무조건 try/catch로 묶어줘야 해서 가독성을 위해 ThrowingSupplier로 감쌌음
        //ThrowingSupplier retriever = () -> joinPoint.proceed(args);

        Supplier<?> retriever = () -> {
            try {
                return joinPoint.proceed(args);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };

        // 리턴타입이 Mono면
        if (rawType.equals(Mono.class)) {
            Type returnTypeInsideMono = parameterizedType.getActualTypeArguments()[0];
            Class<?> returnClass = ResolvableType.forType(returnTypeInsideMono).resolve();
            //Mono<?> result = (Mono<?>) retriever.get();

            return reactiveCacheManager
                    .findCachedMono(cacheName, generateKey(args), retriever, returnClass)
                    .doOnError(e -> log.error("Failed to processing mono cache. method: " + method.getName(), e));
        }
        // 리턴타입이 Flux면
        else {
            return reactiveCacheManager
                    .findCachedFlux(cacheName, generateKey(args), retriever)
                    .doOnError(e -> log.error("Failed to processing flux cache. method: " + method.getName(), e));
        }
    }

    // argument들의 조합으로 cache key를 생성
    private String generateKey(Object... objects) {
        return Arrays.stream(objects)
                .map(obj -> obj == null ? "" : obj.toString())
                .collect(Collectors.joining(":"));
    }*/
}