package _2190300408chap2.cycleList.doubleList;

public class CycleDoubleLinkedListTest {
    public static void main(String[] args) {
        CycleDoubleLinkedList<Integer> list = new CycleDoubleLinkedList<>();
        list.insertFirst(1).insertFirst(2).insertFirst(3).insert(3, 4).insertLast(5);
        System.out.println(list);
    }
}
