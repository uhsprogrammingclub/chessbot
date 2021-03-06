# chessbot
A UHS Software Development Project

Creating an intelligent chess player and GUI that can play to a moderate level. 

### Version Control

Instead of using the Github GUI, for Java projects we will be using a handy Github plug-in for [Eclipse](https://eclipse.org/users/). Eclipse is a Java IDE, and is very handy with its many features. You can just download the standard version. We will be using the EGit plugin, found [here](https://eclipse.github.io/). You can watch [this](https://www.youtube.com/watch?v=ptK9-CNms98) video for a quick crash course on how to use EGit with Eclipse. 

If you are ever doing any involved changes on an independent part of the project, please ensure that you **branch** from the master file before making your changes. Once you are certain that your changes work, feel free to **merge** with the master branch. This makes all of our lives much easier. Basic workflow tactics can be found [here](http://rogerdudler.github.io/git-guide/). This is using the command line; but the same principles apply for the GUI. 

### Design Overview

There will be several main classes. The **Board** class will hold everything about the chess board, where the pieces are located, the score of the board, etc. It will have many accessory functions. 

### Algorithm Overview

We will be using a fundemental alpha-beta minimax algorithm, with some twists and turns. [Here's](https://www.youtube.com/watch?v=STjW3eH0Cik) an MIT lecture by a professor that covers everything you will need to know about alpha-beta. It's a bit confusing, so come with questions if needed. The lecture is long; feel free to skip around if you get the hang of it. 

For more recent information, please view the wiki file.

>UHS Software Development Club Presidents
