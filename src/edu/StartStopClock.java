/*
 * Copyright (c) 2017. Aleksey Eremin
 * 12.01.17 15:37
 *
 */

package edu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Calendar;

/**
 * Created by ae on 12.01.2017.
 * Простая многопоточность на примере часов с кнопками Старт и Стоп
 * by novel  http://java-course.ru/begin/multithread_01/  (Остановка потока)
 */

public class StartStopClock extends JFrame implements ActionListener
{
  private static final String START = "Start";
  private static final String STOP  = "Stop";
  
  private JLabel clockLabel = new JLabel();
  private ClockThread clockThread = null;
  
  // страт программы
  public static void main(String[] args)
  {
    StartStopClock cl = new StartStopClock();
    cl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    cl.setVisible(true);
  }
  
  // конструктор объекта
  public StartStopClock()
  {
    // установить заголовок
    setTitle("Clock Start Stop Thread");
    
    // выровнять метку по горизонтали
    clockLabel.setHorizontalAlignment((SwingConstants.CENTER));
    
    // установить размер шрифта для метки
    // Установить размер шрифта для метки  - есть такой метод у Label
    // Для эт ого создаем шрифт и сразу его отдаем методу setFont
    Font f = new Font("Default", Font.BOLD + Font.ITALIC, 24);
    clockLabel.setFont(f);
    
    // добавить строку на основную панель
    //getContentPane().add(clockLabel);
    add(clockLabel);
  
    // Добавить кнопку для старта
    JButton start = new JButton(START);
    start.setActionCommand(START);
    start.addActionListener(this);  // слушатель событий - этот же объект
    add(start, BorderLayout.NORTH); // прижмемся к северу
    
    // Добавить кнопку для остановки
    JButton stop = new JButton(STOP);
    stop.setActionCommand(STOP);
    stop.addActionListener(this);
    add(stop, BorderLayout.SOUTH);
    
    // установить размер окна
    setBounds(400, 300, 300, 200);
  }
  
  public void setTime()
  {
    // более корректный вызов в потоке, который работает с графикой
    SwingUtilities.invokeLater(new Runnable() { // ананимный класс
      @Override
      public void run() {
        // создадим объект для форматирования даты
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        // установим новое значение для строки
        clockLabel.setText(df.format(Calendar.getInstance().getTime()));
      }
    });
  }
  
  @Override
  public void actionPerformed(ActionEvent ae)
  {
    ;;;
    String ac_str = ae.getActionCommand();
    if(ac_str.equals(START)) {
      // нажали кнопку Старт
      if (clockThread == null) {
        clockThread = new ClockThread(this);
        clockThread.start();
      }
    }
    if(ac_str.equals(STOP)) {
      if(clockThread != null) {
        clockThread.stopClock();  // остановим выполнение
        clockThread = null;
      }
    }
  }
  
} // end class  StartStopClock

class ClockThread extends Thread
{
  private StartStopClock clock;
  private volatile boolean isRunning;
  
  public ClockThread(StartStopClock clock)
  {
    this.clock = clock;
    this.isRunning = true;
  }
  
  @Override
  public void run() {
    // бесконечный цикл
    while(isRunning) {
      clock.setTime();
      try {
        Thread.sleep(500);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public void stopClock()
  {
    this.isRunning = false;
  }
  
}// end class  MyThread



