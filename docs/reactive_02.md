스프링 리액티브 웹 개발 - Operator
=====
* 다루는 내용
	* Operator
	* Reactor
* [강의 링크](https://www.youtube.com/watch?v=DChIxy9g19o): 03/12/2016
- - -
## 목차
1. [복습](#복습)
2. [Operator](#Operator)
3. [Reactor](#Reactor)
4. [참고](#참고)

## 복습
```java
package toby.live.rs_02;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PubSub {
    public static void main(String[] args) {
        // 지난 주(JAVA9)와 다른 Publisher API 사용(∴ JAVA8에서도 사용 가능)
        Publisher<Integer> pub = new Publisher<Integer>() {
            // Publisher의 Data Source
            Iterable<Integer> iter = Stream.iterate(1, a -> a + 1)
                    .limit(10)
                    .collect(Collectors.toList());

            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                // Spec에 따르면 subscribe을 호출하면 DATA를 보내야 하는데,
                // protocol에 따르면 가장 먼저 subscriber의 onSubscribe 호출
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            iter.forEach(s -> sub.onNext(s));
                            sub.onComplete();   // Reactive에서 빠드리면 안되는 것!
                        } catch (Throwable t) {
                            sub.onError(t);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> sub = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);  // 무제한 데이터 받기
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };

        pub.subscribe(sub);
    }
}
```

16:50

- - -
* [Reactive Streams 의존성 추가](https://mvnrepository.com/artifact/org.reactivestreams/reactive-streams/1.0.0)

##### [목차로 이동](#목차)

## Operator



##### [목차로 이동](#목차)

## Reactor



##### [목차로 이동](#목차)

## 참고


##### [목차로 이동](#목차)