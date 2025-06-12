package ru.job4j.my;

public class My {

    public int sum(int a, int b) {
        return a + b;
    }
    public static void main(String[] args) {
        My test = new My();
        System.out.println(test.sum(1, 2));
    }
}
