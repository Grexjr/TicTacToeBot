<h1> Tic-Tac-Toe Bot </h1>

<p>
A terminal- and Java-based implementation of a Tic-Tac-Toe engine and unbeatable bot.
Designed with an emphasis on clean code, clear use of data structures, and efficient state 
evaluation.
</p>

<h3> Features/Goals: </h3>
<ul>
    <li> <h6>ENGINE:</h6> A CLI engine for playing simple games of Tic-Tac-Toe between two players</li>
    <li> <h6>BOT:</h6> A bot that uses a Minimax algorithm to play Tic-Tac-Toe perfectly against a player</li>
    <li> <h6>PERFORMANCE:</h6> An emphasis on minimizing Big-O runtimes and clean code for maintenance/readability</li>
</ul>

<h3> How to Use: </h3>
<ul>
    <li> <h6>INSTALLATION:</h6> Requires the JRE 24+ to run (older JRE may work, untested) </li>
    <li> <h6>USAGE:</h6> Runs via prompting in the terminal, fairly self-explanatory input requests </li>
    <li> <h6>PLAYING:</h6> The board is indexed 1-9 from the top left square. Enter an index to fill a square. </li>
</ul>

<h3> Technical Details: </h3>
<h5> ENGINE: </h5>
<ul> 
    <h6> Board: </h6>
        <p>
        Currently, this implementation uses a 2D array. However, it recently came to me that a 1D array works perfectly 
        fine, and in fact better, since the squares become more clearly mathematically related to each other that way, 
        making checking victory conditions much easier.
        </p>
    <h6> Input: </h6>
        <p>
        This implementation uses the standard Java System.in input, with input validation for integers when the user is
        inputting which squares to fill.
        </p>
    <h6> Player: </h6>
        <p>
        The player class contains what symbol the player is, as well as the code for running their turn.
        </p>
    <h6> Game: </h6>
        <p>
        The game class includes the board and input objects as well as the 2-member array of players for ease of
        accessing them all easily if need be. The game also contains the logic for checking victory, which works and
        ends the program when a player has achieved three in a row. Currently, the user plays both the X and O side,
        which can be nice for playing against your friends!
        </p>
</ul>
<h5> BOT: </h5>
<p>
Not yet implemented.
</p>
<h5> Install/Build: </h5>
<ul>
    <h6> Installation Specifics: </h6>
        <p>
        As of yet, no built and packed jar is available, but this will be the main method of installation once it is.
        </p>
</ul>
