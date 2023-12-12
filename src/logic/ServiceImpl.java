package logic;

import anntations.InjectClass;
import dataAccess.ItemRepository;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class ServiceImpl implements Service {
    @InjectClass
    private ItemRepository repository;
    private List<Item> list = new ArrayList<Item>();

    public List<Item> getAll() {
        return repository.findAll();
    }
}
