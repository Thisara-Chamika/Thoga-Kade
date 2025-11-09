package service.custom.impl;

import model.Order;
import repository.custom.ItemRepository;
import repository.custom.OrderRepository;
import repository.repositoryFactory;
import service.custom.OrderService;
import util.RepositoryType;

import java.sql.SQLException;

public class OrderServiceImpl implements OrderService {
    OrderRepository repository = repositoryFactory.getInstance().getFactoryType(RepositoryType.ORDER);

    @Override
    public boolean placeOrder(Order order) throws SQLException {
        System.out.println("Order : "+order);
        repository.save(order);
        return true;
    }
}
