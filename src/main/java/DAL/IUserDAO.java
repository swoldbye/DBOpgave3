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
    void createRole(String roll, int id) throws DALException;
    //Delete
    void deleteRole(String roll) throws DALException;
    //Read
    List<String> getRoleList() throws DALException;
    //Update
    void updateRole(String oldRole, String newRole) throws DALException;


    //helper function   //FixMe Should this be in the interface if only a helper function?
    boolean contains(String s, List<String> array);
}