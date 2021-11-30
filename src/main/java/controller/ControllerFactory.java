package controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerFactory {

    private static SimpleGetController simpleGetController = new SimpleGetController();
    private static ControllerFactory controllerFactory = new ControllerFactory();
    private static Map<String, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/user/create", new CreateUserController());
        controllerMap.put("/user/login", new LoginController());
        controllerMap.put("/user/list", new ListUserController());
    }

    private ControllerFactory () {}

    public static Controller getInstance(String requestPath) {
        return controllerMap.getOrDefault(requestPath, simpleGetController);
    }


}
