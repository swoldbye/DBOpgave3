package DAL;

import DTO.*;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductDAOTest {
    IProductDAO proDAO = new ProductDAO();

    @Test
    public void ProductDAOBigTest() throws DALException {
        //Create a product
        List<IUserDTO> workers = new ArrayList<>();
//        workers.add(null);  //FixMe Add when UserDTO is added
//        workers.add(null);  //FixMe Add when UserDTO is added
        List<ICommodityDTO> commodities = new ArrayList<>();
        commodities.add(new CommodityDTO(1, 1, 100, false, "Dealer", "Sildenafil"));
        commodities.add(new CommodityDTO(2, 2, 100, false, "Dealer", "Calciumhydrogenphosphat dihydrat"));
//        Date date = new Date(119, 0,0);
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        IProductDTO testProduct = new ProductDTO(9999, "Sildenafil", 1, 1, 2000, workers, commodities, date, false);
        assertTrue(proDAO.createProduct(testProduct));

        //Control if it's in the database
        IProductDTO foundProduct = proDAO.getProduct(9999);
        assertEquals(testProduct.getID(), foundProduct.getID());
        assertEquals(testProduct.getName(), foundProduct.getName());
        assertEquals(testProduct.getRecipeID(), foundProduct.getRecipeID());
        assertEquals(testProduct.getOrderedBy(), foundProduct.getOrderedBy());
        assertEquals(testProduct.getQuantity(), foundProduct.getQuantity());
        assertEquals(testProduct.getWorkers(), foundProduct.getWorkers());
        assertEquals(testProduct.getCommodities().toString(), foundProduct.getCommodities().toString());
        assertEquals(testProduct.getDate().getYear(), foundProduct.getDate().getYear());
        assertEquals(testProduct.getDate().getMonth(), foundProduct.getDate().getMonth());
        assertEquals(testProduct.getDate().getDay(), foundProduct.getDate().getDay());
        assertEquals(testProduct.isManufactured(), foundProduct.isManufactured());

        //Update product information

        //Mark as finished

        //Delete product
    }

    @Test
    public void createProduct() {
    }

    @Test
    public void createProductionLines() {
    }

    @Test
    public void createCommodityLines() {
    }

    @Test
    public void readAllProducts() {
        //TODO Create proper UserDTO objects, when UserDTO is implemented
        IUserDTO user1 = null;
        IUserDTO user2 = null;

//        Date pro1Date = new Date(2019, 5, 7);
//        IProductDTO pro1 = new ProductDTO(1, "Sildenafil", 1, 2000, pro1Workers, pro1Commodities, pro1Date, true);
    }

    @Test
    public void updateProductInfo() {
    }

    @Test
    public void markAsFinished() {
    }

    @Test
    public void deleteProduct() {
    }
}
