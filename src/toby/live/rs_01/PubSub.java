package toby.live.rs_01;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PubSub {
    public static void main(String[] args) throws InterruptedException {
        Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5);
        ExecutorService es = Executors.newSingleThreadExecutor();

        // Java9
        Publisher p = new Publisher() {
            @Override
            public void subscribe(Subscriber subscriber) {
                Iterator<Integer> it = iter.iterator();

                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        es.execute(() -> {  // 진행 여부 및 결과 상관 없는 경우
                            int i = 0;
                            try {
                                while (i++ < n) {
                                    if (it.hasNext()) {
                                        subscriber.onNext(it.next());
                                    } else {
                                        subscriber.onComplete();
                                        break;
                                    }
                                }
                            } catch (RuntimeException e) {
                                subscriber.onError(e);
                            }
                        });
                        /*
                         * Future
                         *  - 비동기 작업 결과/완료 여부 정보를 제공
                         *  - 이 경우 event이므로 타입은 없으니 와일드 카드 사용
                         *  - 중간에 cancel하는 경우 Future 통해 interrupt 가능
                         */
//                        Future<?> f = es.submit(() -> {   // 진행 상황 체크하고 싶은 경우
//                            try {
//                                while (n-- > 0) {
//                                    if (it.hasNext()) {
//                                        subscriber.onNext(it.next());
//                                    } else {
//                                        subscriber.onComplete();
//                                        break;
//                                    }
//                                }
//                            } catch (RuntimeException e) {
//                                subscriber.onError(e);
//                            }
//                        });
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> s = new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println(Thread.currentThread().getName() + " onSubscribe");
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println(Thread.currentThread().getName() + " onNext " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError: " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println(Thread.currentThread().getName() + " onComplete");
            }
        };

        p.subscribe(s);

        es.awaitTermination(10, TimeUnit.HOURS);
        es.shutdown();
    }
}
