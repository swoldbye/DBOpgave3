package DAL;

import DTO.IUserDTO;

import java.util.List;

public interface IUserDAO {
    //Create
    void createUser(IUserDTO user) throws DALException;
    //Read
    IUserDTO getUser(int userId) throws DALException;
    List<IUserDTO> getUserList() throws DALException;
    //Update
    void updateUser(IUserDTO user) throws DALException;
    //Delete
    void deleteUser(int userId) throws DALException;

    //Create
    void createRoll(String roll, int id) throws DALException;
    //Delete
    void deleteRoll(String roll) throws DALException;
    //Read
    List<String> getRollList() throws DALException;
    //Update
    void updateRoll(String oldRole, String newRole) throws DALException;


    //helper function   //FixMe Should this be in the interface if only a helper function?
    boolean contains(String s, List<String> array);
}