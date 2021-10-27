package org.example;

/**
 * Main Class
 */
public class Main {
    public static void main(String[] args) {
        CallCenter callCenter = new CallCenter();
        //Создаём поток АТС
        Thread ats = new Thread(callCenter::addCall, "Поток-АТС");
        //Создаём 3 потока специалистов
        Thread spec1 = new Thread(callCenter::callInWork, "Специалист 1");
        Thread spec2 = new Thread(callCenter::callInWork, "Специалист 2");
        Thread spec3 = new Thread(callCenter::callInWork, "Специалист 3");
        //Стартуем потоки
        ats.start();
        spec1.start();
        spec2.start();
        spec3.start();
        while (true) {
            //Выполняется пока не увидит, что хотя бы один специалист заокнчил работу
            if (spec1.isInterrupted() || spec2.isInterrupted()
                    || spec3.isInterrupted()) {
                //Если хотя бы 1 специалист закончил, значит звоноков больше нет = останавливаем все потоки
                spec1.interrupt();
                spec2.interrupt();
                spec3.interrupt();
                System.out.println("Все потоки остановлены");
                break;
            }
        }
    }
}
