package DAL;

import DTO.IUserDTO;

public interface IUserDAO {
    IUserDTO getUser(int userID);
}
