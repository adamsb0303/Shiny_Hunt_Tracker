I created this application to help keep track of my shiny hunts, but it also has a main function of a streaming tool<br/>
<h1>How to use:</h1>
https://youtu.be/Ye-Y5v2R7kU
<h2>Starting</h2>
    The launcher titled "launch.bat"<br/><br/>
    Upon start up click the + button to add a new hunt.<br/>
    When clicked, if the program sees that there is previous hunt data, it will ask if you would like to continue a previous hunt or start a new one.<br/>
    If it doesn't then it will go straight to the hunt selection menu<br/>
<h2>Hunt Selection Menu</h2>
    On the left is a drop down of every game and method<br/>
    On the right is a list of every available pokemon<br/>
    At the top of the pokemon list is a search field, type in the name of the pokemon that you would like to hunt and the list will update.<br/>
    When you select any new value (Pokemon, Game, or Method) the other fields will update so that they only show options that line up with all currently selected data options.<br/>
    When all 3 options are chosen, the Begin Hunt button can be pressed<br/>
<h2>The Hunt Window</h2>
    Once a hunt has begun a hunt window will appear. This window contains several elements<br/>
          &#8195;The sprite of the selected pokemon from the selected game<br/>
          &#8195;The sprites of all family members of the selected pokemon from the selected game<br/>
          &#8195;The pokemon name<br/>
          &#8195;The method name<br/>
          &#8195;The game name<br/>
          &#8195;The number of encounters<br/>
          &#8195;The odds<br/>
    Game Specific modifiers and combo information is available under the "Odds" settings<br/>
    Once a hunt has started, the Popup button will change to a settings button<br/>
<h2>Incrementing Encounters</h2>
    When a new hunt has started, a button that reads "Increment (hunt #)" will appear in the controls window<br/>
    When this button is pressed the encounters Text will go up by 1 by default<br/>
    You can also increment the hunt by pressing the assigned key on the keyboard<br/>
    by default hunts 1-9 are assigned to their respective hunt numbers<br/>
    any more hunts than that, you can check in the "Key Binds" settings window<br/>
<h2>The Settings</h2>
    When a new hunt is added to the controller, a number of buttons will appear.<br/>
    Exit Hunt - Removes hunt from controller<br/>
    Increment(+ and -) Increases or Decreases encounter number<br/>
    Caught - Removes hunt from saved hunts and adds it to list of caught pokemon<br/>
    Popout & Popout Settings - Creates a popout and changes the button to the settings for the popout<br/>
    Hunt Settings<br/>
        &#8195;Change Increment - Adds or subtracts n from encounters when incrementing or decrementing<br/>
        &#8195;Fail - Resets encounters to 0<br/>
        &#8195;Phase - Resets encounters to 0 and adds new pokemon to caught list<br/>
        &#8195;DV Table - Only appears for Gen 1 hunts. Shows required stats for pokemon to be shiny<br/>
    Help - Shows information and links relating to the method that the hunt is performing
<h2>Layout Settings</h2>
    In this menu you will find drop down menus of all previously mentioned elements plus a "Background" menu<br/><br/>
    For Images you can change the following:<br/>
        &#8195;Pokemon Form (If appliable)<br/>
        &#8195;Scale<br/>
        &#8195;X Location<br/>
        &#8195;Y Location<br/>
        &#8195;Visability<br/><br/>
    For Text you can change the following:<br/>
        &#8195;Scale<br/>
        &#8195;X Location<br/>
        &#8195;Y Location<br/>
        &#8195;Font<br/>
        &#8195;Color<br/>
        &#8195;Outline<br/>
        &#8195;Outline Width<br/>
        &#8195;Outline Color<br/>
        &#8195;Italics, Bold, and Underlined<br/>
        &#8195;Visability<br/><br/>
    For background can change the following:<br/>
        &#8195;Background Color<br/><br/>
    At the bottom you will also see the button "Saved Layouts". This creates a popup of all saved layouts and allows you to save a new one.<br/><br/>
    Save will ask for a name for the current layout, and save it<br/>
    If a layout was previously selected, it will ask if you would like to create a new layout or update the previously selected one<br/><br/>
    Once a layout is selected, the hunt window will change to match the saved layout<br/>
<h2>Key Bindings</h2>
    In this menu you will see a list of all currently opened hunt windows' assigned key binding<br/>
    to change this, enter the character that you would like the window's increment to be assigned to and press enter<br/>
    Now, when you are tabbed into the hunt controls window, when that button is pressed, the encounters will increase<br/>
<h2>Previously Caught Pokemon Settings:</h2>
    In this menu you will see the following<br/>
        Display Previously Caught
            &#8195;When a number is inputted into the display previously caught field, a window will appear<br/>
            &#8195;This window contains the last x pokemon that have been caught and their information<br/>
            &#8195;The settings for all these elements will also appear<br/>
        Background<br/>
            &#8195;Changes the color of the background<br/>
        Saved Layouts<br/>
            &#8195;Will save and load layouts, similar to the ones in the hunt window settings.<br/>
            &#8195;But these layouts are independent of the hunt layouts<br/>
            &#8195;These Layouts will save the number of pokemon displayed, and the settings for all of the elements<br/><br/>
    This window will also update when a pokemon has been caught, saving the previous layout   
