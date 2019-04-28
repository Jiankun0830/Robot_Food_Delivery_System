package com.omralcorut.jiankun;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    Food f1 = new Food("fries","10");
    History h1 = new History();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date date = new Date();

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    // FOOD

    @Test public void foodCostGetSetTest(){

        f1.setCost("2");
        assertEquals("2",f1.getCost());
    }

    @Test public void foodIdGetSetTest(){
        f1.setId(123);
        assertEquals(f1.getId() , 123);
    }

    @Test public void foodToString(){
        assertEquals("Food{name='fries', cost='10'}",f1.toString());
    }

    @Test public void foodNameGetSetTest(){
        f1.setName("Large Fries");
        assertEquals("Large Fries",f1.getName());
    }


    // HISTORY

    @Test public void testGetSetHistoryDate(){
        h1.setDate(dateFormat.format(date));
        assertEquals("2019/03/22",h1.getDate());
    }

    @Test public void testGetSetHistoryTableNumber(){
        h1.setTableNumber("1");
        assertEquals(Integer.toString(01),h1.getTableNumber());
    }

    @Test public void testGetSetEvent(){
        History h2 = new History("Order 1", "2");
        assertEquals(Integer.toString(02),h2.getTableNumber());
        assertEquals("Order 1",h2.getEvent());
    }

    @Test public void testToStringHistory(){
        h1.setEvent("Order 2");
        h1.setDate(dateFormat.format(date));
        h1.setTableNumber("1");
        assertEquals("History{event='Order 2', date=2019/03/22, tableNumber=1}",h1.toString());
    }


}