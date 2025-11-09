package repository.custom.impl;

import db.DBConnection;
import model.Order;
import repository.custom.ItemRepository;
import repository.custom.OrderRepository;
import repository.repositoryFactory;
import util.RepositoryType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    ItemRepository itemRepository = repositoryFactory.getInstance().getFactoryType(RepositoryType.ITEM);

    @Override
    public boolean save(Order order) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try{
            PreparedStatement psTM = connection.prepareStatement("INSERT INTO orders VALUES(?,?,?)");
            psTM.setObject(1,order.getOrderId());
            psTM.setObject(2,order.getOrderDate());
            psTM.setObject(3,order.getCustomerId());
            if (psTM.executeUpdate()>0){
                boolean isOrderDetailsAdd = OrderDetailsRepositoryImpl.getInstance().saveOrderDetail(order.getOrderDetails());
                if (isOrderDetailsAdd){
                    boolean isUpdateStock = itemRepository.updateStock(order.getOrderDetails());
                    if (isUpdateStock){
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.commit();
            return true;
        }finally {
            connection.setAutoCommit(true);
        }

    }

    @Override
    public List<Order> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public Order search(String s) throws SQLException {
        return null;
    }

    @Override
    public boolean update(Order order) throws SQLException {
        return false;
    }
}
