package sample.db;

import sample.db.impl.DbHelperImpl;
import sample.models.*;
import sample.models.dto.OperationDto;
import sample.models.dto.OperationItemDto;
import sample.models.dto.ProductDto;

import java.util.List;

public interface DbHelper {

    DbHelper INSTANCE = new DbHelperImpl();

    Account findUserByLogin(String login);

    boolean isLoginUnique(String login);

    User saveUser(User user);

    boolean saveAccount(Account account);

    List<Category> getAllCategories();

    boolean saveCategory(Category category);

    boolean updateCategory(Category category);

    boolean deactivateCategory(Category category);

    Long saveProduct(Product product);

    List<Category> getAllActiveCategories();

    int  savePrice(Price price);

    List<ProductDto> getAllProducts();


    int saveProductDtoTransactionMethod(ProductDto productDto);

    int updateProductAndPrice(ProductDto productDto);

    ProductDto findProductByBarcode(String barcode);

    int saveOperationAndItems(Long userId,double totalSum, List<OperationItemDto> operationItemDtoList);

    List<OperationDto> getAllOperations();

    List<OperationDetail> getAllOperationDetails(OperationDto newValue);
    void decrementAmount(Long productId);
    void incrementAmount(Long productId,int amount);
}
