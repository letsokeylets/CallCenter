package org.example;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Класс обработки звонков
 */
public class CallCenter {

    //Очередь звонков. Данная очередь ConcurrentLinkedQueue выбрана, так как обеспечивает быструю неблокирующую работу
    //А также, так как даёт нам честность обработки звонков
    // (каждый звонок обрабатывается по мере поступления в порядке очереди)
    private ConcurrentLinkedQueue<Integer> queueCall = new ConcurrentLinkedQueue<>();
    //Количество добавляемых звонков
    private final int MAX_CALL = 60;
    //Указывается задержка для добавления звонков
    private final long CALL_ADD_TIMEOUT = 1000;
    //Указывается задержка для обработки звонка
    private final long CALL_IN_WORK_TIMEOUT = 4000;

    /**
     * Метод предназначен для работы потока АПС
     * добавляет в queueCall определённое количество звокнов MAX_CALL
     * с задержкой CALL_ADD_TIMEOUT
     */
    public void addCall() {
        try {
            for (int i = 1; i <= MAX_CALL; i++) {
                Thread.sleep(CALL_ADD_TIMEOUT);
                System.out.println("В очередь добавлен звонок №" + i);
                queueCall.add(i);
            }
            System.out.println("Поток АПС был остановлен");
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод предназначен для работы потоков специалистов
     * разбирает звонки из queueCall
     * если специалист дошёл до обработки последнего звонка MAX_CALL
     * тогда необходимо остановить поток
     */
    public void callInWork() {
        Thread thread = Thread.currentThread();
        while (!thread.isInterrupted()) {
            //Получаем звонок из очереди и сразу же его удаляем, чтобы другие потоки не могли его обработать
            final Integer call = queueCall.poll();
            //Проверяем, что полученный звонок существует (очередь была не пуста)
            if (call != null) {
                try {
                    System.out.println(thread.getName()
                            + " берёт в работу звонок №" + call);
                    Thread.sleep(CALL_IN_WORK_TIMEOUT);
                    //Проверяем, обрабатывается ли последний звонок
                    if (call == MAX_CALL) {
                        System.out.println("Был обработан последний звонок");
                        thread.interrupt();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
