# Tools


## Le grappinnnnn

<pre>
                   ()
                 __/\__         
        |\   .-"`      `"-.   /|
        | \.'( ') (' ) (. )`./ |
         \_                  _/
           \  `~"'=::='"~`  /
    ,       `-.__      __.-'       ,
.---'\________( `""~~""` )________/'---.
 >   )       / `""~~~~""` \       (   <
'----`--..__/        -(-)- \__..--`----'
            |_____ __ _____|
            [_____[##]_____]  I HAVE BEEN CHOSEN...
            |              |    FAREWELL MY FRIENDS...
            \      ||      /     I GO ONTO A BETTER PLACE!
       jgs   \     ||     /
          .-"~`--._||_.--'~"-.
         (_____,.--""--.,_____)
</pre>

### Manual

Select it with **Up**. This will make appear hooks if they are platforms near the player (upside : left, right or both).

The player will be deactive until he choose a hook with **left** or **right** or cancel the grapple with **down**.

He will then dangle until he release the hook with **down**. But he can give himselft a little impulse before or choose to climb to the upside platform where he is hooked with **up**.

## how it's coded ?

We use essentially a RopeJoint from Dyn4j the grapple. All the reste depend on the general actionListener in SimplepPatformer.java

To locate the player, a special class has been developped who give us access to a position by grid. It allow us to test the presence or the absence of a platform in the current window with a simple 2D array.

To do all of this, we have extended a very usefull method provided by Dyn4j : SimulationBody.getWorldCenter();