package toby.live.rs_01;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class It {
    public static void main(String[] args) {
        // 1. asList 메소드 리턴타입: List<T>
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        // 2. List 타입은 Iterable 인터페이스의 Sub-type
        /*
         * public interface List<E> extends Collection<E> { }
         * public interface Collection<E> extends Iterable<E> { }
         * public interface Iterable<T> {
         *      - Implementing this interface allows an object to be the target
         *        of the "for-each loop" statement(since 1.5).
         *      - 즉, 컬렉션이 아니더라도 이 인터페이스를 구현한 데이터를 가진
         *        객체가 있다면 for-each 구문 사용 가능
         * }
         */
        Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5);
        for (Integer i : iter) {    // for-each
            System.out.println(i);
        }

        Iterable<Integer> iter1 = new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return null;
            }
        };

        // 인터페이스(Iterable)에 메소드(Iterator) 하나일 경우 람다 이용(since 8)
        Iterable<Integer> iter2 = () ->
                new Iterator<Integer>() {
                    int i = 0;
                    final static int MAX = 10;

                    @Override
                    public boolean hasNext() {
                        return i < MAX;
                    }

                    @Override
                    public Integer next() {
                        return ++i;
                    }
                };

        for (Integer i : iter2) {
            System.out.println(i);
        }

        // 자바5 이전 코드
        /*
         * - for -> while 컴파일
         * - 가져오면서 증가(∴ 증가문 필요 없음)
         */
        for (Iterator<Integer> it = iter2.iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }
}
