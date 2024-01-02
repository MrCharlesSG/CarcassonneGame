package hr.algebra.carcassonnegame2.configuration;

public class GameConfiguration {
    public static final int NUM_FOLLOWERS_PER_PLAYER = 7;
    public static final int NUM_PLAYERS = 2;
    public static boolean IS_GAME_MODE_ONLINE = false;
    private static final String FILES_DIRECTORY = "files/";
    public static final String SAVE_FILE_NAME = FILES_DIRECTORY + "data.ser";
    public static final String MOVES_FILE_NAME = FILES_DIRECTORY + "moves.dat";

    public static final int PENALIZATION_FOR_CHANGING_TILE = -5;
    public static final int POINTS_FOR_CITY = 2;
    public static final int POINTS_FOR_MONASTERY = 9;
    public static final int POINTS_FOR_PATH = 1;
    public static final String TILES_FILE_NAME = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB.json";
    // public static final String TILES_FILE_NAME = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-city-test.json";
    // public static final String TILES_FILE_NAME = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-monastery-test.json";
    //public static final String TILES_FILE_NAME = "src/main/resources/hr/algebra/carcassonnegame2/JSON/tilesDB-path-test.json";
}
