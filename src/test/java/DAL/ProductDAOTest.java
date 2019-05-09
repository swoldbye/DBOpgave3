package DAL;

import DTO.*;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ProductDAOTest {
    IProductDAO proDAO = new ProductDAO();

    @Test
    public void ProductDAOBigTest() throws DALException {
        try {
            //Create a product
            List<IUserDTO> workers = new ArrayList<>();
//        workers.add(null);  //FixMe Add when UserDTO is added
//        workers.add(null);  //FixMe Add when UserDTO is added
            List<ICommodityDTO> commodities = new ArrayList<>();
            commodities.add(new CommodityDTO(1, 1, 100, false, "Dealer", "Sildenafil"));
            commodities.add(new CommodityDTO(2, 2, 100, false, "Dealer", "Calciumhydrogenphosphat dihydrat"));
            Date createDate = new Date(Calendar.getInstance().getTime().getTime());
            IProductDTO testCreateProduct = new ProductDTO(9999, "Sildenafil", 1, 1, 2000, workers, commodities, false);

            assertTrue(proDAO.createProduct(testCreateProduct));

            //Control if it's in the database
            IProductDTO foundProduct = proDAO.getProduct(9999);
            assertEquals(testCreateProduct.getID(), foundProduct.getID());
            assertEquals(testCreateProduct.getName(), foundProduct.getName());
            assertEquals(testCreateProduct.getRecipeID(), foundProduct.getRecipeID());
            assertEquals(testCreateProduct.getOrderedBy(), foundProduct.getOrderedBy());
            assertEquals(testCreateProduct.getQuantity(), foundProduct.getQuantity());
            assertEquals(testCreateProduct.getWorkers().toString(), foundProduct.getWorkers().toString());
            assertEquals(testCreateProduct.getCommodities().toString(), foundProduct.getCommodities().toString());
            assertEquals(testCreateProduct.getDate().getYear(), foundProduct.getDate().getYear());
            assertEquals(testCreateProduct.getDate().getMonth(), foundProduct.getDate().getMonth());
//            assertEquals(testCreateProduct.getDate().getDay(), foundProduct.getDate().getDay());  //FixMe Dates aren't correct at 1:30AM, might be something with timezones?
            assertEquals(testCreateProduct.isManufactured(), foundProduct.isManufactured());

            //Update product information
            List<IUserDTO> updateWorkers = new ArrayList<>();
            Date updateDate = new Date(119, 05, 05);
            IProductDTO testUpdateProduct = new ProductDTO(9999, "Sildenafil", 1, 1, 1500, workers, commodities, updateDate, false);
            assertTrue(proDAO.updateProductInfo(testUpdateProduct, updateWorkers));

            foundProduct = proDAO.getProduct(9999);
            assertEquals(testUpdateProduct.getID(), foundProduct.getID());
            assertEquals(testUpdateProduct.getName(), foundProduct.getName());
            assertEquals(testUpdateProduct.getRecipeID(), foundProduct.getRecipeID());
            assertEquals(testUpdateProduct.getOrderedBy(), foundProduct.getOrderedBy());
            assertEquals(testUpdateProduct.getQuantity(), foundProduct.getQuantity());
            assertEquals(testUpdateProduct.getWorkers().toString(), foundProduct.getWorkers().toString());
            assertEquals(testUpdateProduct.getCommodities().toString(), foundProduct.getCommodities().toString());
            assertEquals(testUpdateProduct.getDate().getYear(), foundProduct.getDate().getYear());
            assertEquals(testUpdateProduct.getDate().getMonth(), foundProduct.getDate().getMonth());
//            assertEquals(testUpdateProduct.getDate().getDay(), foundProduct.getDate().getDay());  //FixMe Dates aren't correct at 1:30AM, might be something with timezones?
            assertEquals(testUpdateProduct.isManufactured(), foundProduct.isManufactured());

            //Mark as finished
            proDAO.markAsFinished(9999);
            foundProduct = proDAO.getProduct(9999);
            assertTrue(foundProduct.isManufactured());
//            assertEquals(createDate, foundProduct.getDate());                                     //FixMe Dates aren't correct at 1:30AM, might be something with timezones?
        }
        finally {
            //Delete product - Always deletes it, even though Create or Update might fail
            proDAO.deleteProduct(9999);
            assertNull(proDAO.getProduct(9999));
        }
    }
}
