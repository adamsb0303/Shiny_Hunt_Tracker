I created this application to help keep track of my shiny hunts, but it also has a main function of a streaming tool<br/>
<h1>How to use:</h1>
https://www.youtube.com/watch?v=4eYv-shSb1E
<h2>Starting</h2>
    The launcher is located at Shiny Hunt Tracker/bin/launcher.sh<br/>
    use Shiny Hunt Tracker/bin/launcher.sh.bat if the previous didn't work<br/><br/>
    Upon start up there will be a window with a single button that reads "Add Hunt" and a Settings menu.<br/>
    When clicked, if the program sees that there is previous hunt data, it will ask if you would like to continue a previous hunt or start a new one.<br/>
    If it doesn't then it will go straight to the hunt selection menu<br/>
<h2>Hunt Selection Menu</h2>
    On the left is a list of all currently available 893 Pokemon.<br/>
    In the center is a list of games<br/>
    on the right is the list of available shiny hunting methods for the previously selected game and pokemon<br/><br/>
    The pokemon are divided up by generation, but if the menu is selected, you may type in the pokemon name and it will select the pokemon that starts with whatever you typed<br/>
        &#8195;So if you type "Sudo", Sudowoodo would be selected.<br/>
    If the selected pokemon has a regional variant, the check boxes just below the menu will be selectable<br/>
    Once a pokemon is selected the game menu will be created<br/><br/>
    The game menu will only display games that the pokemon is available in.<br/>
    In most cases pokemon can be transferred in from other games and breeded, but legendaries can only be found in certain games.<br/>
    Because of this games that the selected legendary pokemon isn't available in won't appear<br/>
        &#8195;If Dialga is selected, Pearl will not appear<br/>
    After both a pokemon and a game is selected, the methods menu will appear<br/><br/>
    The methods that appear will be limited to what will be available to that pokemon.<br/>
        &#8195;If the pokemon is shiny locked in the selected game, no methods will appear.<br/>
        &#8195;If the pokemon can't breed (and no other pokemon in the evolution line can either) then breeding methods will not appear<br/>
    If the selected game was released after Black and White, the Shiny Charm check box will become selectable<br/>
    If the selected game is a lets go game, the lure checkbox will become available<br/><br/>
    Once all 3 of these are selected the Begin Hunt Button will be selectable!<br/>
<h2>Loading a Previous Hunt</h2>
    If the program has information from previous hunts and you select "Continue Previous Hunt" on the Add Hunt menu, a list of all previous hunts will appear.<br/>
    Once one is selected, it will load into a new hunt window.<br/>
<h2>The Hunt Window</h2>
    Once a hunt has begun a hunt window will appear. This window contains several elements<br/>
          &#8195;The sprite of the selected pokemon from the selected game<br/>
          &#8195;The sprites of all pre-evolutions of the selected pokemon from the selected game<br/>
          &#8195;The pokemon name<br/>
          &#8195;The method name<br/>
          &#8195;The game name<br/>
          &#8195;The number of encounters<br/>
          &#8195;The odds<br/>
    If the method that you chose requires information that can change between selections then it will prompt for the needed information<br/>
        &#8195;The methods are DexNav and Total Encounters from SWSH<br/>
    This information will appear in another element if it was required<br/>
    A combo counter will also appear in the window if the method's odds are dependent on combos<br/><br/>
    Once a hunt has started, the Settings Menu in the hunt controller will change<br/>
<h2>Incrementing Encounters</h2>
    When a new hunt has started, a button that reads "Increment (hunt #)" will appear in the controls window<br/>
    When this button is pressed the encounters Text will go up by 1 by default<br/>
    You can also increment the hunt by pressing the assigned key on the keyboard<br/>
    by default hunts 1-9 are assigned to their respective hunt numbers<br/>
    any more hunts than that, you can check in the "Key Binds" settings window<br/>
<h2>The Settings</h2>
    Previously, before any hunt has been selected, the only 2 items in the Settings menu where "Key Binds" and "Previously Caught Settings"<br/>
    When a new hunt begins a new item titled "Hunt (hunt #) Settings"<br/>
    This Menu contains<br/>
        Layout Settings<br/>
        Hunt Settings<br/>
            Change Increment<br/>
            Fail<br/>
            Phase Hunt<br/>
            Pokemon Caught<br/>
            Reset Combo<br/>
            Save Hunt<br/>
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
        &#8195;Stroke (aka outline)<br/>
        &#8195;Stroke Width<br/>
        &#8195;Stroke Color<br/>
        &#8195;Italics, Bold, and Underlined<br/>
        &#8195;Visability<br/><br/>
    For background can change the following:<br/>
        &#8195;Color<br/><br/>
    At the bottom you will also see the buttons "Save and "Load" these are for saving and loading layouts<br/><br/>
    Save will ask for a name for the current layout, and save it<br/>
    If a layout was previously selected, it will ask if you would like to create a new layout or update the previously selected one<br/><br/>
    Load will show a list of all previously saved layouts<br/>
    Once a layout is selected, the hunt window will change to match the saved layout<br/>
<h2>Hunt Settings:</h2>
    In this window you will see the following settings:<br/>
        Change Increment<br/>
            &#8195;This will change what number the encounters goes up by when incremented<br/>
        Fail<br/>
            &#8195;This will reset the encounters Text to 0<br/>
        Phase Hunt<br/>
            &#8195;This will ask what pokemon you found, and save it to the caught pokmeon list<br/>
            &#8195;This will also reset the combo<br/>
        Pokemon Caught<br/>
            &#8195;This will close the hunt window and write the pokemon to the caught pokemon list<br/>
            &#8195;This will also remove the hunt from the previous hunts list that appears when adding a new hunt<br/>
        Reset Combo<br/>
            &#8195;This will reset the Combo and Odds Text<br/>
        Save Hunt<br/>
            &#8195;This will save the hunt without closing<br/>
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
        Save and Load<br/>
            &#8195;Will save and load layouts, similar to the ones in the hunt window settings.<br/>
            &#8195;But these layouts are independent of the hunt layouts<br/>
            &#8195;These Layouts will save the number of pokemon displayed, and the settings for all of the elements<br/><br/>
    This window will also update when a pokemon has been caught, saving the previous layout
<h2>Streaming</h2>
    If you make the background a color, you are able to go into a streaming software and use color key to make it transparent</br>
    The windows are also split up so that you can add them, and move them around, individually in the software
    
