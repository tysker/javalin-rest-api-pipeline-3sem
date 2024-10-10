package dk.lyngby;

import dk.lyngby.config.AppConfig;
import dk.lyngby.util.ApiProps;

public class Main {

    public static void main(String[] args) {
        AppConfig.startServer(ApiProps.API_PORT);
    }
}