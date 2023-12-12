package presentation;

import Container.DIContainer;
import logic.Item;
import logic.Service;
import logic.ServiceImpl;

import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        DIContainer container = new DIContainer();
        DIContainer.autoRegister();
        Service service = (Service) container.getInstance(ServiceImpl.class);

        List<Item> items = service.getAll();
        for (Item item : items) {
            System.out.println(item.getName());
        }
    }
}