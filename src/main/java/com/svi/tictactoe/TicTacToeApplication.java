package com.svi.tictactoe;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class TicTacToeApplication extends Application {
    // Needed to enable Jakarta REST and specify path.
}
