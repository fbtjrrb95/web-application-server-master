package controller;

public class ControllerFactory {

    private static SimpleGetController simpleGetController = new SimpleGetController();
    private static LoginController loginController = new LoginController();
    private static ListUserController listUserController = new ListUserController();
    private static CreateUserController createUserController = new CreateUserController();

    private static ControllerFactory controllerFactory = new ControllerFactory();

    private ControllerFactory () {}

    public static Controller getInstance(String requestPath) {

        if ("/user/login".equals(requestPath)) {
            return loginController;
        }

        if ("/user/create".equals(requestPath)) {
            return createUserController;
        }

        if ("/user/list".equals(requestPath)) {
            return listUserController;
        }

        return simpleGetController;
    }


}
