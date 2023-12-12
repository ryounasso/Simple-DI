package dataAccess;

import logic.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    public List<Item> findAll() {
        // Mock
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(1, "apple", 300));
        items.add(new Item(2, "orange", 200));
        items.add(new Item(3, "banana", 100));
        return items;
    }
}
