package DAL;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import DAL.UserDAO;
import DAL.IUserDAO;

public class UserDAOTest {

    IUserDAO userDAO = new UserDAO();

    @org.junit.Test
    public void test() {
        try {

            ArrayList<String> roles = new ArrayList();
            roles.add("admin"); roles.add("user"); roles.add("worker");
            IUserDTO testUser = new UserDTO(52,"swoldbye", "swl", roles, "300698-1234");

            //create the rolls
            userDAO.createRoll("admin", 95);
            userDAO.createRoll("user", 96);
            userDAO.createRoll("worker", 97);
            userDAO.createRoll("full-Time", 99);
            userDAO.createRoll("part-Time", 98);

            //createUser and getUser
            userDAO.createUser(testUser);
            IUserDTO returnedUser = userDAO.getUser(52);
            assertEquals(testUser.getUserName(),returnedUser.getUserName());
            assertEquals(testUser.getCpr(), returnedUser.getCpr());
            assertEquals(testUser.getIni(), returnedUser.getIni());
            assertEquals(testUser.getRoles().get(0), returnedUser.getRoles().get(0));
            assertEquals(testUser.getRoles().get(1), returnedUser.getRoles().get(1));
            assertEquals(testUser.getRoles().get(2), returnedUser.getRoles().get(2));
            assertEquals(testUser.getRoles().size(), returnedUser.getRoles().size());

            //UserList
            List<IUserDTO> allUsers = userDAO.getUserList();
            boolean found = false;
            for(IUserDTO user: allUsers){
                if(user.getUserId() == 52) {
                    if (user.getUserId() == testUser.getUserId()) {
                        assertEquals(testUser.getUserName(), user.getUserName());
                        assertEquals(testUser.getCpr(), user.getCpr());
                        assertEquals(testUser.getIni(), user.getIni());
                        assertEquals(testUser.getRoles().get(0), user.getRoles().get(0));
                        assertEquals(testUser.getRoles().get(1), user.getRoles().get(1));
                        assertEquals(testUser.getRoles().get(2), user.getRoles().get(2));
                        assertEquals(testUser.getRoles().size(), user.getRoles().size());
                        found = true;
                    }
                    break;
                }
            }
            if(!found){fail();}

            roles.clear();
            roles.add("Admin"); //'admin' changed to 'Admin' to test updateRole.
            roles.add("full-Time");
            roles.add("part-Time");
            userDAO.updateRoll("admin", "Admin");

            testUser.setUserName("abcd");
            testUser.setIni("qwer");
            testUser.setCpr("123456-7890");
            testUser.setRoles(roles);
            userDAO.updateUser(testUser);

            returnedUser = userDAO.getUser(52);
            assertEquals(testUser.getUserName(),returnedUser.getUserName());
            assertEquals(testUser.getCpr(), returnedUser.getCpr());
            assertEquals(testUser.getIni(), returnedUser.getIni());
            assertEquals(testUser.getRoles().get(0), returnedUser.getRoles().get(0));
            assertEquals(testUser.getRoles().get(1), returnedUser.getRoles().get(2));
            assertEquals(testUser.getRoles().get(2), returnedUser.getRoles().get(1));
            assertEquals(testUser.getRoles().size(), returnedUser.getRoles().size());



            userDAO.deleteUser(52);
            allUsers = userDAO.getUserList();

            for(IUserDTO user: allUsers){
                if(user.getUserId() == 52) {fail(); }
            }

            int i = 0;
            List<String> allRoles = userDAO.getRollList();
            for(String roll: allRoles){
                if(roll.equals("Admin") || roll.equals("user") || roll.equals("worker")
                        || roll.equals("part-Time") || roll.equals("full-Time")){i++;}
            }
            if(i != 5) {fail();}

            userDAO.deleteRoll("admin");
            userDAO.deleteRoll("user");
            userDAO.deleteRoll("worker");
            userDAO.deleteRoll("part-Time");
            userDAO.deleteRoll("full-Time");

            List<String> newRoles = userDAO.getRollList();
            for(String roll: newRoles){
                if(roll.equals("Admin") || roll.equals("user") || roll.equals("worker")
                        || roll.equals("part-Time") || roll.equals("full-Time")){fail();}
            }
            userDAO.deleteUser(52);

        } catch (IUserDAO.DALException e) {
            e.printStackTrace();
            fail();
        }


    }
}