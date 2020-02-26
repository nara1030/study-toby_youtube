package toby.live.rs_01;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("deprecation")
public class Ob {
    // Iterable <----> Observable(duality)
    // Pull            Push
    public static void main(String[] args) {
        Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(Thread.currentThread().getName() + " " + arg);
            }
        };

        /*
         * 이제부터 Observable이 던지는 모든 Event를 Observer가 받음
         * Swing, Andriod 등 EventDriven에 해당하는 곳에선 이 패턴 사용
         */
        IntObservable io = new IntObservable();
        io.addObserver(ob);

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(io);

        System.out.println(Thread.currentThread().getName() + " EXIT");
        es.shutdown();
    }

    // Source(Event/Data) -> Observer
    // Data를 만들어내는 행위를 실행하는 메소드 -> Runnable로 비동기적 구현
    // since 1.0(∴ 람다 등 적용 없음)
    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                setChanged();   // 변화 생겼음을 알림
                notifyObservers(i);
            }
        }
    }
}
