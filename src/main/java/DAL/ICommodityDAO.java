package DAL;

import DTO.ICommodityDTO;

import java.util.List;

public interface ICommodityDAO {

    void createCommodity(ICommodityDTO commodity) throws DALException;

    ICommodityDTO getCommmodity(int ingredient_id) throws DALException;

    List<ICommodityDTO> getCommodityList() throws DALException;

    void updateCommodity(ICommodityDTO commodity) throws DALException;

    void deleteCommodity(int batch_id) throws DALException;


}
