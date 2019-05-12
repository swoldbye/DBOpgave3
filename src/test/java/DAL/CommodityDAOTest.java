package DAL;

import static org.junit.Assert.*;
import DAL.CommodityDAO;
import DAL.DALException;
import DAL.ICommodityDAO;
import DTO.CommodityDTO;
import DTO.ICommodityDTO;

import java.util.List;

public class CommodityDAOTest{

    ICommodityDAO iCommodityDAO = new CommodityDAO();

    /**
     * Denne test tester alle metoderne i CommodityDAO.
     *
     * @throws DALException
     */
    @org.junit.Test
    public void test() throws DALException{
        try {

            //En test commodity oprettes
            ICommodityDTO testCommodityDTO = new CommodityDTO(50,1,
                    5.2,true,"homecook","Sildenafil");

            //createCommodity testes
            iCommodityDAO.createCommodity(testCommodityDTO);

            //getCommodity testes
            ICommodityDTO returnedCommodity = iCommodityDAO.getCommmodity(50);
            assertEquals(testCommodityDTO.toString(),returnedCommodity.toString());

            //getCommodityList testes.
            List<ICommodityDTO> AllCommodities = iCommodityDAO.getCommodityList();
            boolean found = false;
            for (ICommodityDTO commodity: AllCommodities){
                if (commodity.getBatch_id() == testCommodityDTO.getBatch_id()){
                    assertEquals(testCommodityDTO.toString(),commodity.toString());
                    found = true;
                }
            }
            if (!found){
                fail();
            }

            //updateCommodity testes
            testCommodityDTO.setQuantity(6.4);
            testCommodityDTO.setIs_leftover(false);
            testCommodityDTO.setCommodity_manufacturer("Hjemme hos Andreas");
            iCommodityDAO.updateCommodity(testCommodityDTO);

            returnedCommodity = iCommodityDAO.getCommmodity(50);
            assertEquals(testCommodityDTO.toString(),returnedCommodity.toString());

            //deleteCommodity testes
            iCommodityDAO.deleteCommodity(50);
            for (ICommodityDTO commodity : iCommodityDAO.getCommodityList()){
                if (testCommodityDTO.getBatch_id() == commodity.getBatch_id()){
                    fail();
                }
            }
        }
        catch (DALException e){
            e.printStackTrace();
            fail();
        }
    }


}