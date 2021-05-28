package sample.db.impl;

import sample.db.DbHelper;
import sample.models.*;
import sample.models.dto.OperationDto;
import sample.models.dto.OperationItemDto;
import sample.models.dto.ProductDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbHelperImpl implements DbHelper {

    @Override
    public Account findUserByLogin(String login) {
        Connection connection = null;
        try {
            connection = getConnection();
            String findByLoginQuery = "SELECT  u.id,u.name,a.login,a.password,a.active FROM accounts as a JOIN users as u on\n" +
                    "a.user_id = u.id WHERE login =?";
            PreparedStatement preparedStatement = connection.prepareStatement(findByLoginQuery);
            preparedStatement.setString(1,login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Account account = new Account();
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                account.setUser(user);
                account.setLogin(resultSet.getString(3));
                account.setPassword(resultSet.getString(4));
                account.setActive(resultSet.getInt(5)==1?true:false);
                return account;
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean isLoginUnique(String login) {
        Connection connection = null;
        try {
            connection = getConnection();
            String isUniqueLoginQuery ="SELECT count(login) FROM accounts WHERE login=?";
            PreparedStatement preparedStatement = connection.prepareStatement(isUniqueLoginQuery);
            preparedStatement.setString(1,login);
            ResultSet resultSet = preparedStatement.executeQuery();
                int result = resultSet.getInt(1);
                if (result >=1) {
                    return false;
                }
                return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public User saveUser(User user) {
        Connection connection = null;
        try {
            connection = getConnection();
            String insertUserQuery = "INSERT INTO users (name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,user.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                user.setId(resultSet.getLong(1));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean saveAccount(Account account) {
        Connection connection = null;
        try {
            connection = getConnection();
            String accountInsertQuery = "INSERT INTO accounts (user_id,login,password,active) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(accountInsertQuery);
            preparedStatement.setLong(1,account.getUser().getId());
            preparedStatement.setString(2,account.getLogin());
            preparedStatement.setString(3,account.getPassword());
            preparedStatement.setInt(4,account.isActive()?1:0);
            int resultOfAffectedRows = preparedStatement.executeUpdate();
            return resultOfAffectedRows==1?true:false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public List<Category> getAllCategories() {
        Connection connection=null;
        try {
            connection = getConnection();
            String selectQuery = "SELECT id,name,active FROM categories";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Category> categoryList = new ArrayList<>();
            while (resultSet.next()){
                Category category = new Category();
                category.setId(resultSet.getLong("id"));
                category.setName(resultSet.getString("name"));
                category.setActive(resultSet.getInt("active")==1?true:false);
                categoryList.add(category);
            }
            return categoryList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean saveCategory(Category category) {
        Connection connection = null;
        try {
            connection = getConnection();
            String insertQuery = "INSERT INTO categories (name,active) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,category.getName());
            preparedStatement.setInt(2,category.isActive()?1:0);
            int result = preparedStatement.executeUpdate();
            return result==1?true:false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateCategory(Category category) {
        Connection connection = null;
        try {
            connection = getConnection();
            String updateQuery = "UPDATE categories SET name=?, active=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1,category.getName());
            preparedStatement.setInt(2,category.isActive()?1:0);
            preparedStatement.setLong(3,category.getId());
            int result = preparedStatement.executeUpdate();
            return result==1?true:false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean deactivateCategory(Category category) {
        Connection connection = null;
        try {
            connection = getConnection();
            String deactivateQuery = "UPDATE categories SET active=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(deactivateQuery);
            preparedStatement.setInt(1,0);
            preparedStatement.setLong(2,category.getId());
            int result = preparedStatement.executeUpdate();
            return result==1?true:false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public Long saveProduct(Product product) {
        Connection connection = null;
        try {
            connection = getConnection();
            String insertProduct = "INSERT INTO products (name,barcode,category_id,active) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertProduct);
            preparedStatement.setString(1,product.getName());
            preparedStatement.setString(2,product.getBarcode());
            preparedStatement.setLong(3,product.getCategory().getId());
            preparedStatement.setInt(4,product.isActive()?1:0);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.getLong(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0l;
    }

    @Override
    public List<Category> getAllActiveCategories() {
        Connection connection = null;
        try {
            connection = getConnection();
            String selectQuery = "SELECT id,name FROM categories where active=1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Category> categoryList = new ArrayList<>();
            while (resultSet.next()){
                Category category = new Category(resultSet.getLong(1),resultSet.getString(2));
                categoryList.add(category);
            }
            return categoryList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public int savePrice(Price price) {
        Connection connection = null;
        try {
            connection = getConnection();
            String priceSaveQuery = "INSERT INTO prices (price,start_date,end_date,product_id) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(priceSaveQuery);
            preparedStatement.setDouble(1,price.getPrice());
            preparedStatement.setString(2,price.getStartDate().toString());
            preparedStatement.setString(3,price.getEndDate().toString());
            preparedStatement.setLong(4,price.getProduct().getId());
            return preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        Connection connection = null;
        try {
            connection = getConnection();
            String selectQuery = "SELECT p.id,p.name,p.barcode,p.active,c.id,c.name,pr.id,pr.price,pr.start_date,pr.end_date FROM products as p join categories as c on p.category_id=c.id join prices as pr on pr.product_id=p.id where pr.end_date>=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1,LocalDate.now().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ProductDto> productDtoList = new ArrayList<>();
            while (resultSet.next()){
                ProductDto productDto = new ProductDto();
                productDto.setId(resultSet.getLong(1));
                productDto.setName(resultSet.getString(2));
                productDto.setBarcode(resultSet.getString(3));
                productDto.setActive(resultSet.getInt(4)==1?true:false);
                Category category = new Category();
                category.setId(resultSet.getLong(5));
                category.setName(resultSet.getString(6));
                productDto.setCategory(category);
                Price price = new Price();
                price.setId(resultSet.getLong(7));
                price.setPrice(resultSet.getDouble(8));
                price.setStartDate(LocalDate.parse(resultSet.getString(9)));
                price.setEndDate(LocalDate.parse(resultSet.getString(10)));
                productDto.setPrice(price);
                productDtoList.add(productDto);
            }
            return productDtoList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
            return new ArrayList<>();
    }


    @Override
    public int saveProductDtoTransactionMethod(ProductDto productDto) {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            String insertProductQuery = "INSERT INTO products (name,barcode,category_id,active) VALUES (?,?,?,?)";
            PreparedStatement preparedStatementProduct = connection.prepareStatement(insertProductQuery);
            preparedStatementProduct.setString(1,productDto.getName());
            preparedStatementProduct.setString(2,productDto.getBarcode());
            preparedStatementProduct.setLong(3,productDto.getCategory().getId());
            preparedStatementProduct.setInt(4,productDto.isActive()?1:0);
            preparedStatementProduct.executeUpdate();
            ResultSet resultSet = preparedStatementProduct.getGeneratedKeys();
            Long productId = resultSet.getLong(1);

            String insertProductRestQuery = "INSERT INTO rests(product_id,amount,max_amount,min_amount) values(?,?,?,?)";
            PreparedStatement preparedStatementRest = connection.prepareStatement(insertProductRestQuery);
            preparedStatementRest.setLong(1,productId);
            preparedStatementRest.setInt(2,0);
            preparedStatementRest.setInt(3,0);
            preparedStatementRest.setInt(4,0);
            preparedStatementRest.executeUpdate();

            String insertPriceQuery = "INSERT INTO prices (price,start_date,end_date,product_id) VALUES (?,?,?,?)";
            PreparedStatement preparedStatementPrice = connection.prepareStatement(insertPriceQuery);
            preparedStatementPrice.setDouble(1,productDto.getPrice().getPrice());
            preparedStatementPrice.setString(2,productDto.getPrice().getStartDate().toString());
            preparedStatementPrice.setString(3,productDto.getPrice().getEndDate().toString());
            preparedStatementPrice.setLong(4,productId);
            int result = preparedStatementPrice.executeUpdate();
            connection.commit();
            return result;
        } catch (SQLException throwables) {
            try {
                System.out.println("rollBack");
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0;
    }

    @Override
    public int updateProductAndPrice(ProductDto productDto) {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            String updateProductQuery = "UPDATE products SET name=?,category_id=?,active=? where id=?";
            PreparedStatement updateProductpreparedStatement = connection.prepareStatement(updateProductQuery);
            updateProductpreparedStatement.setString(1,productDto.getName());
            updateProductpreparedStatement.setLong(2,productDto.getCategory().getId());
            updateProductpreparedStatement.setInt(3,productDto.isActive()?1:0);
            updateProductpreparedStatement.setLong(4,productDto.getId());
            updateProductpreparedStatement.executeUpdate();
            String selectPriceQuery = "SELECT id,price,start_date,end_date, product_id from prices where product_id=? and id!=?";
            PreparedStatement selectAllPricesPreparedStatement = connection.prepareStatement(selectPriceQuery);
            selectAllPricesPreparedStatement.setLong(1,productDto.getId());
            selectAllPricesPreparedStatement.setLong(2,productDto.getPrice().getId());
            ResultSet resultSet = selectAllPricesPreparedStatement.executeQuery();
            List<Price> priceList = new ArrayList<>();
            while (resultSet.next()){
                Price price = new Price();
                price.setId(resultSet.getLong(1));
                price.setPrice(resultSet.getDouble(2));
                price.setStartDate(LocalDate.parse(resultSet.getString(3)));
                price.setEndDate(LocalDate.parse(resultSet.getString(4)));
                Product product = new Product();
                product.setId(resultSet.getLong(5));
                price.setProduct(product);
                priceList.add(price);
            }
            if (!priceList.isEmpty()){
                Connection finalConnection = connection;
                priceList.stream().filter(price->
                        (price.getStartDate().isAfter(productDto.getPrice().getStartDate())||price.getStartDate().isBefore(productDto.getPrice().getStartDate()))
                        &&(price.getEndDate().isAfter(productDto.getPrice().getEndDate())||price.getEndDate().isBefore(productDto.getPrice().getEndDate()))

                        ).forEach(p->{
                            p.setEndDate(LocalDate.now().minusDays(2));
                    String updatePrice = "UPDATE prices set end_date=? where id=?";
                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement = finalConnection.prepareStatement(updatePrice);
                        preparedStatement.setString(1,p.getEndDate().toString());
                        preparedStatement.setLong(2,p.getId());
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }
            String updatePrice = "UPDATE prices SET price=?,start_date=?,end_date=?,product_id=? where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updatePrice);
            preparedStatement.setDouble(1,productDto.getPrice().getPrice());
            preparedStatement.setString(2,productDto.getPrice().getStartDate().toString());
            preparedStatement.setString(3,productDto.getPrice().getEndDate().toString());
            preparedStatement.setLong(4,productDto.getId());
            preparedStatement.setLong(5,productDto.getPrice().getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result;
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0;
    }

    @Override
    public ProductDto findProductByBarcode(String barcode) {
        Connection  connection = null;
        ProductDto productDto = null;

        try {
            connection = getConnection();
            String findProductByBarcode = "SELECT p.id, p.name, pr.price, r.amount FROM products p join prices as pr on pr.product_id=p.id join rests as r on r.product_id=p.id where p.active=1 and p.barcode=? and date(pr.start_date)<=? AND date(pr.end_date)>=?";
            PreparedStatement preparedStatement = connection.prepareStatement(findProductByBarcode);
            preparedStatement.setString(1,barcode);
            preparedStatement.setString(2,LocalDate.now().toString());
            preparedStatement.setString(3,LocalDate.now().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                productDto = new ProductDto();
                productDto.setId(resultSet.getLong(1));
                productDto.setName(resultSet.getString(2));
                productDto.setPrice(new Price(resultSet.getDouble(3)));
                productDto.setAmount(resultSet.getInt(4));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return productDto;
    }

    @Override
    public int saveOperationAndItems(Long userId,double totalSum, List<OperationItemDto> operationItemDtoList) {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            String insertOperationQuery = "INSERT INTO operations (add_date,sum,user_id) VALUES (?,?,?)";
            PreparedStatement preparedStatementOperation = connection.prepareStatement(insertOperationQuery);
            preparedStatementOperation.setString(1,LocalDate.now().toString());
            preparedStatementOperation.setDouble(2,totalSum);
            preparedStatementOperation.setLong(3,userId);
            preparedStatementOperation.executeUpdate();
            ResultSet resultSet = preparedStatementOperation.getGeneratedKeys();
            Long operationId = resultSet.getLong(1);
            String insertOperation = "INSERT INTO operation_details (product_id,operation_id,amount,price) VALUES(?,?,?,?)";
            PreparedStatement preparedStatementItems = connection.prepareStatement(insertOperation);
            for (OperationItemDto op :operationItemDtoList){
                preparedStatementItems.setLong(1,op.getProductId());
                preparedStatementItems.setLong(2,operationId);
                preparedStatementItems.setInt(3,op.getAmount());
                preparedStatementItems.setDouble(4,op.getSum());
                preparedStatementItems.executeUpdate();
            }
            connection.commit();
            return 1;

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0;
    }

    @Override
    public List<OperationDto> getAllOperations() {
        Connection connection = null;
        try {
            connection = getConnection();
            String getOperationsQuery = "SELECT op.id,op.add_date,op.sum,a.login FROM operations as op JOIN accounts as a ON op.user_id=a.user_id";
            PreparedStatement preparedStatement = connection.prepareStatement(getOperationsQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<OperationDto> operationDtoList = new ArrayList<>();
            while (resultSet.next()){
                OperationDto operationDto = new OperationDto();
                operationDto.setId(resultSet.getLong(1));
                operationDto.setAddDate(LocalDate.parse(resultSet.getString(2)));
                operationDto.setSum(resultSet.getDouble(3));
                operationDto.setAccount(new Account(resultSet.getString(4)));
                operationDtoList.add(operationDto);
            }
            return operationDtoList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<OperationDetail> getAllOperationDetails(OperationDto newValue) {
        Connection connection = null;
        try {
            connection = getConnection();
            String selectQuery = "SELECT od.id,od.amount,od.price,pr.name FROM operation_details as od JOIN products as pr on od.product_id=pr.id where od.operation_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setLong(1,newValue.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<OperationDetail> operationDetailList = new ArrayList<>();
            while (resultSet.next()){
                OperationDetail operationDetail = new OperationDetail();
                operationDetail.setId(resultSet.getLong(1));
                operationDetail.setAmount(resultSet.getInt(2));
                operationDetail.setPrice(resultSet.getDouble(3));
                operationDetail.setProduct(new Product(resultSet.getString(4)));
                operationDetailList.add(operationDetail);
            }
            return operationDetailList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        }
        return new ArrayList<>();
    }

    @Override
    public void decrementAmount(Long productId) {
        Connection connection = null;
        try {
            connection = getConnection();
            String decrementQuery = "UPDATE rests SET amount=(SELECT amount FROM rests WHERE product_id=?)-1 WHERE product_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(decrementQuery);
            preparedStatement.setLong(1,productId);
            preparedStatement.setLong(2,productId);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public void incrementAmount(Long productId,int amount) {
        Connection connection = null;
        try {
            connection=getConnection();
            String decrementQuery = "UPDATE rests SET amount=(SELECT amount FROM rests WHERE product_id=?)+1 WHERE product_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(decrementQuery);
            preparedStatement.setLong(1,productId);
            preparedStatement.setLong(2,productId);
            for (int i=amount;i>0;i--) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }


    private Connection getConnection() throws SQLException {

           Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\DBsql\\store.db");
            return connection;

    }
}
