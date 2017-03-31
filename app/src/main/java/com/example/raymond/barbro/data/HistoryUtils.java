package com.example.raymond.barbro.data;

import android.content.Context;

import java.util.LinkedList;

/**
 * Created by raymond on 3/28/17.
 */

public class HistoryUtils {

    private static LinkedList<Integer> historyList = new LinkedList<>();

    public static void addToHistory(Integer drink){

        historyList.addFirst(drink);
    }
}
